package ru.faculty.schedule;

import org.junit.jupiter.api.Test;
import exception.ScheduleConflictException;
import exception.ValidationException;
import model.*;
import service.Schedule;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Набор модульных тестов для проверки работы системы расписания.
 * Покрывает контракты equals/hashCode, граничные случаи и выброс исключений.
 */
class ScheduleTest {

    private final Audience a101 = new Audience("A", "101", 120);
    private final Audience small = new Audience("A", "202", 10);
    private final Teacher t1 = new Teacher("T1", "Иванов");
    private final Teacher t2 = new Teacher("T2", "Петров");
    private final Group g1 = new Group("G1", 20);
    private final Group g2 = new Group("G2", 18);
    private final Discipline math = new Discipline("M", "Математика");
    private final Schedule schedule = new Schedule(LocalDate.of(2025, 2, 3));

    /**
     * Проверяет контракт equals/hashCode для класса Audience.
     * Аудитории с одинаковым корпусом и комнатой должны быть равны, даже если разная вместимость.
     */
    @Test
    void audienceEqualsHashCode() {
        var a = new Audience("A", "101", 120);
        var b = new Audience("A", "101", 50);
        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
    }

    /**
     * Проверяет контракт equals/hashCode для класса Teacher.
     * Преподаватели с одинаковым id должны быть равны, даже если разное ФИО.
     */
    @Test
    void teacherEqualsHashCode() {
        assertEquals(new Teacher("T1", "X"), new Teacher("T1", "Y"));
        assertEquals(new Teacher("T1", "X").hashCode(), new Teacher("T1", "Y").hashCode());
        assertNotEquals(new Teacher("T1", "X"), new Teacher("T2", "X"));
    }

    /**
     * Проверяет контракт equals/hashCode для класса Group.
     * Группы с одинаковым кодом должны быть равны, даже если разное количество студентов.
     */
    @Test
    void groupEqualsHashCode() {
        var a = new Group("G1", 20);
        var b = new Group("G1", 99);
        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
    }

    /**
     * Проверяет успешное добавление занятия в пустое расписание.
     */
    @Test
    void addSimpleLesson() {
        schedule.add(WeekDay.MONDAY, new Practice(math, t1, PairNumber.FIRST, g1), a101);
        assertEquals(1, schedule.byGroup("G1").size());
    }

    /**
     * Проверяет выброс ScheduleConflictException при конфликте аудитории.
     * В одну аудиторию на одно время нельзя поставить два занятия.
     */
    @Test
    void audienceConflict() {
        schedule.add(WeekDay.MONDAY, new Practice(math, t1, PairNumber.FIRST, g1), a101);
        assertThrows(ScheduleConflictException.class, () ->
                schedule.add(WeekDay.MONDAY, new Practice(math, t2, PairNumber.FIRST, g2), a101));
    }

    /**
     * Проверяет выброс ScheduleConflictException при конфликте преподавателя.
     * Один преподаватель не может вести два занятия одновременно.
     */
    @Test
    void teacherConflict() {
        schedule.add(WeekDay.MONDAY, new Practice(math, t1, PairNumber.FIRST, g1), a101);
        var otherAud = new Audience("B", "1", 30);
        assertThrows(ScheduleConflictException.class, () ->
                schedule.add(WeekDay.MONDAY, new Practice(math, t1, PairNumber.FIRST, g2), otherAud));
    }

    /**
     * Проверяет выброс ScheduleConflictException при конфликте группы.
     * Одна группа не может находиться на двух занятиях одновременно.
     */
    @Test
    void groupConflict() {
        schedule.add(WeekDay.MONDAY, new Practice(math, t1, PairNumber.FIRST, g1), a101);
        var otherAud = new Audience("B", "1", 30);
        assertThrows(ScheduleConflictException.class, () ->
                schedule.add(WeekDay.MONDAY, new Practice(math, t2, PairNumber.FIRST, g1), otherAud));
    }

    /**
     * Проверяет выброс ValidationException, если аудитория слишком мала для группы.
     */
    @Test
    void smallAudienceValidation() {
        assertThrows(ValidationException.class, () ->
                schedule.add(WeekDay.MONDAY, new Practice(math, t1, PairNumber.FIRST, g1), small));
    }

    /**
     * Проверяет корректный подсчет нагрузки преподавателя (1 пара = 2 академических часа).
     */
    @Test
    void teacherLoad() {
        schedule.add(WeekDay.MONDAY, new Practice(math, t1, PairNumber.FIRST, g1), a101);
        schedule.add(WeekDay.MONDAY, new Practice(math, t1, PairNumber.SECOND, g1), a101);
        assertEquals(4, schedule.teacherLoadHours("T1"));
        assertEquals(0, schedule.teacherLoadHours("T2"));
    }

    /**
     * Проверяет корректность фильтрации расписания по аудитории и преподавателю.
     */
    @Test
    void byAudienceAndTeacher() {
        schedule.add(WeekDay.MONDAY, new Practice(math, t1, PairNumber.FIRST, g1), a101);
        assertEquals(1, schedule.byTeacher("T1").size());
        assertEquals(1, schedule.byAudience(a101).size());
    }

    /**
     * Граничный случай: попытка создать лекцию без групп должна выбрасывать исключение.
     */
    @Test
    void lectureRequiresGroups() {
        assertThrows(IllegalArgumentException.class, () ->
                new Lecture(math, t1, PairNumber.FIRST, List.of()));
    }

    /**
     * Граничный случай: неверный диапазон дат (конец раньше начала) должен выбрасывать исключение.
     */
    @Test
    void rangeInvalid() {
        assertThrows(ValidationException.class, () ->
                schedule.range(LocalDate.of(2025, 2, 10), LocalDate.of(2025, 2, 3)));
    }

    /**
     * Проверяет, что при построении расписания на диапазон дат пропускаются праздничные дни.
     */
    @Test
    void rangeSkipsHolidays() {
        schedule.addHoliday(LocalDate.of(2025, 2, 5)); // Среда - праздник
        schedule.add(WeekDay.MONDAY, new Practice(math, t1, PairNumber.FIRST, g1), a101);
        schedule.add(WeekDay.WEDNESDAY, new Practice(math, t2, PairNumber.FIRST, g2), a101);

        var res = schedule.range(LocalDate.of(2025, 2, 3), LocalDate.of(2025, 2, 9));

        assertEquals(1, res.size());
    }

    /**
     * Проверяет поиск номера пары по времени начала, а также выброс исключения при некорректном времени.
     */
    @Test
    void pairNumberFromStart() {
        assertEquals(PairNumber.FIRST, PairNumber.fromStart(LocalTime.of(8, 30)));
        assertThrows(IllegalArgumentException.class, () ->
                PairNumber.fromStart(LocalTime.of(3, 0)));
    }
}