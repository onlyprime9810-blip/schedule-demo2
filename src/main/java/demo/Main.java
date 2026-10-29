package demo;

import exception.ScheduleConflictException;
import exception.ValidationException;
import model.*;
import service.Schedule;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Главный класс консольного приложения для демонстрации работы системы расписания.
 * Показывает создание объектов, проверку конфликтов, вывод расписаний и поиск окон.
 */
public class Main {

    /**
     * Точка входа в программу.
     *
     * @param args аргументы командной строки (не используются)
     */
    public static void main(String[] args) {
        /** Включаем UTF-8, чтобы русский текст в консоли читался нормально */
        System.setOut(new java.io.PrintStream(System.out, true, java.nio.charset.StandardCharsets.UTF_8));

        /** Старт семестра - 3 февраля 2025 (понедельник) */
        var schedule = new Schedule(LocalDate.of(2025, 2, 3));
        schedule.addHoliday(LocalDate.of(2025, 3, 8));

        /** Создаем аудитории */
        var a101 = new Audience("A", "101", 120);
        var a202 = new Audience("A", "202", 30);
        var a303 = new Audience("B", "303", 30);
        var a404 = new Audience("C", "404", 50);
        var a505 = new Audience("D", "505", 25);

        /** Создаем преподавателей */
        var t1 = new Teacher("T-01", "Иванов И.И.");
        var t2 = new Teacher("T-02", "Петров П.П.");
        var t3 = new Teacher("T-03", "Сидоров С.С.");
        var t4 = new Teacher("T-04", "Кузнецов А.А.");
        var t5 = new Teacher("T-05", "Смирнова Е.В.");
        var t6 = new Teacher("T-06", "Волков Д.С.");
        var t7 = new Teacher("T-07", "Морозова Л.П.");

        /** Создаем группы */
        var g1 = new Group("ПИ-21-1", 25);
        var g2 = new Group("ПИ-21-2", 24);
        var g3 = new Group("ПИ-22-1", 22);
        var g4 = new Group("ПИ-22-2", 20);
        var g5 = new Group("ПИ-23-1", 28);
        var g6 = new Group("ПИ-23-2", 26);
        var g7 = new Group("ПИ-23-3", 21);

        /** Создаем дисциплины */
        var math = new Discipline("MATH", "Математический анализ");
        var prog = new Discipline("PROG", "Программирование");
        var db = new Discipline("DB", "Базы данных");
        var physics = new Discipline("PHYS", "Физика");
        var chemistry = new Discipline("CHEM", "Химия");

        /**
         * ДОБАВЛЕНИЕ ЗАНЯТИЙ (всего 8 записей)
         */
        int added = 0;
        try {
            schedule.add(WeekDay.MONDAY, new Lecture(math, t1, PairNumber.FIRST, List.of(g1, g2)), a101); added++;
            schedule.add(WeekDay.MONDAY, new Practice(prog, t2, PairNumber.SECOND, g1), a202); added++;
            schedule.add(WeekDay.MONDAY, new Lab(prog, t3, PairNumber.THIRD, g1, "1"), a303); added++;
            schedule.add(WeekDay.TUESDAY, new Lecture(prog, t2, PairNumber.THIRD, List.of(g1, g2, g3)), a101); added++;
            schedule.add(WeekDay.WEDNESDAY, new Practice(db, t3, PairNumber.FOURTH, g2), a202); added++;
            schedule.add(WeekDay.THURSDAY, new Lecture(physics, t4, PairNumber.FIRST, List.of(g4, g5)), a404); added++;
            schedule.add(WeekDay.FRIDAY, new Practice(chemistry, t5, PairNumber.SECOND, g4), a505); added++;
            schedule.add(WeekDay.SATURDAY, new Lab(physics, t4, PairNumber.THIRD, g5, "2"), a404); added++;
        } catch (ScheduleConflictException | ValidationException ex) {
            System.out.println("Ошибка при добавлении: " + ex.getMessage());
        }
        System.out.println("Успешно добавлено записей: " + added);

        /**
         * ДЕМОНСТРАЦИЯ ОШИБОК
         */
        System.out.println("\n--- Проверка конфликтов и ошибок ---");

        /** Конфликт аудитории */
        try {
            schedule.add(WeekDay.MONDAY, new Practice(db, t3, PairNumber.FIRST, g3), a101);
        } catch (ScheduleConflictException ex) {
            System.out.println("[ОШИБКА АУДИТОРИИ]: " + ex.getMessage());
        }

        /** Конфликт преподавателя */
        try {
            schedule.add(WeekDay.MONDAY, new Practice(db, t2, PairNumber.SECOND, g2), a303);
        } catch (ScheduleConflictException ex) {
            System.out.println("[ОШИБКА ПРЕПОДАВАТЕЛЯ]: " + ex.getMessage());
        }

        /** Конфликт группы */
        try {
            schedule.add(WeekDay.MONDAY, new Practice(db, t1, PairNumber.SECOND, g1), a202);
        } catch (ScheduleConflictException ex) {
            System.out.println("[ОШИБКА ГРУППЫ]: " + ex.getMessage());
        }

        /** Ошибка валидации (аудитория слишком мала) */
        try {
            schedule.add(WeekDay.FRIDAY, new Practice(db, t3, PairNumber.FIFTH, g3), new Audience("C", "1", 5));
        } catch (ValidationException ex) {
            System.out.println("[ОШИБКА ВАЛИДАЦИИ]: " + ex.getMessage());
        }

        /**
         * ВЫВОД РАСПИСАНИЯ
         */
        System.out.println("\n--- Расписание группы ПИ-21-1 ---");
        schedule.byGroup("ПИ-21-1").forEach(System.out::println);

        System.out.println("\n--- Расписание преподавателя T-01 (Иванов) ---");
        schedule.byTeacher("T-01").forEach(System.out::println);

        System.out.println("\n--- Расписание аудитории B-303 ---");
        schedule.byAudience(a303).forEach(System.out::println);

        /**
         * НАГРУЗКА ПРЕПОДАВАТЕЛЕЙ
         */
        System.out.println("\n--- Нагрузка преподавателей (ак. часы) ---");
        for (var t : List.of(t1, t2, t3, t4, t5, t6, t7)) {
            System.out.println(t.fullName() + ": " + schedule.teacherLoadHours(t.id()) + " ч.");
        }

        /**
         * РАСПИСАНИЕ НА ДИАПАЗОН ДАТ
         */
        System.out.println("\n--- Расписание на 03.02.2025–10.02.2025 ---");
        var dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        var startDate = LocalDate.of(2025, 2, 3);
        var endDate = LocalDate.of(2025, 2, 10);

        for (var date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {
            final var currentDate = date;
            var dailySchedule = schedule.range(currentDate, currentDate);

            if (dailySchedule.isEmpty()) {
                System.out.println(currentDate.format(dateFormatter) + " -> [Выходной / Нет занятий]");
            } else {
                dailySchedule.forEach(e ->
                        System.out.println(currentDate.format(dateFormatter) + " -> " + e.getValue())
                );
            }
        }

        /**
         * ПОИСК СВОБОДНОГО ОКНА
         */
        System.out.println("\n--- Поиск свободного окна 90 мин в A-202 ---");
        try {
            var w = schedule.findFreeWindow(a202, LocalDate.of(2025, 2, 3), Duration.ofMinutes(90), 7);
            System.out.println("Найдено окно начиная с " + w[0]);
        } catch (ValidationException ex) {
            System.out.println(ex.getMessage());
        }
    }
}