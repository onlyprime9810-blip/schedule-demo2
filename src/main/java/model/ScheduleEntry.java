package model;

import java.util.Objects;

/**
 * Представляет конкретную запись в расписании.
 * Связывает день недели, учебное занятие и аудиторию, в которой оно проходит.
 */
public final class ScheduleEntry {
    private final WeekDay day;
    private final Lesson lesson;
    private final Audience audience;

    /**
     * Создает новую запись в расписании.
     *
     * @param day      день недели (не может быть null)
     * @param lesson   учебное занятие (не может быть null)
     * @param audience аудитория (не может быть null)
     * @throws NullPointerException если хотя бы один из аргументов равен null
     */
    public ScheduleEntry(WeekDay day, Lesson lesson, Audience audience) {
        this.day = Objects.requireNonNull(day, "day");
        this.lesson = Objects.requireNonNull(lesson, "lesson");
        this.audience = Objects.requireNonNull(audience, "audience");
    }

    /**
     * Возвращает день недели.
     *
     * @return день недели
     */
    public WeekDay day() {
        return day;
    }

    /**
     * Возвращает учебное занятие.
     *
     * @return занятие
     */
    public Lesson lesson() {
        return lesson;
    }

    /**
     * Возвращает аудиторию.
     *
     * @return аудитория
     */
    public Audience audience() {
        return audience;
    }

    /**
     * Возвращает строковое представление записи расписания.
     * Включает день, номер пары, время, тип занятия, дисциплину,
     * преподавателя, аудиторию и список групп.
     *
     * @return строковое представление записи
     */
    @Override
    public String toString() {
        var p = lesson.pair();
        return day + " " + p + " (" + p.start() + "-" + p.end() + ") | "
                + lesson.kind() + " | " + lesson.discipline().title()
                + " | " + lesson.teacher().fullName() + " | " + audience
                + " | группы: " + lesson.groups().stream().map(Group::code).toList();
    }
}