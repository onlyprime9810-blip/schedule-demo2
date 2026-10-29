package model;

import java.util.List;

/**
 * Представляет лекционное занятие.
 * Проводится для потока, состоящего из одной или нескольких учебных групп.
 * Реализует интерфейс {@link Lesson}.
 */
public final class Lecture implements Lesson {
    private final Discipline discipline;
    private final Teacher teacher;
    private final PairNumber pair;
    private final List<Group> groups;

    /**
     * Создает новое лекционное занятие.
     *
     * @param discipline учебная дисциплина
     * @param teacher    преподаватель, ведущий занятие
     * @param pair       номер пары (время проведения)
     * @param groups     непустой список учебных групп (поток)
     * @throws IllegalArgumentException если список групп пуст или равен null
     */
    public Lecture(Discipline discipline, Teacher teacher, PairNumber pair, List<Group> groups) {
        if (groups == null || groups.isEmpty()) throw new IllegalArgumentException("нужен хотя бы один поток");
        this.discipline = discipline;
        this.teacher = teacher;
        this.pair = pair;
        this.groups = List.copyOf(groups);
    }

    /**
     * Возвращает учебную дисциплину.
     *
     * @return дисциплина
     */
    @Override
    public Discipline discipline() {
        return discipline;
    }

    /**
     * Возвращает преподавателя.
     *
     * @return преподаватель
     */
    @Override
    public Teacher teacher() {
        return teacher;
    }

    /**
     * Возвращает номер пары.
     *
     * @return номер пары
     */
    @Override
    public PairNumber pair() {
        return pair;
    }

    /**
     * Возвращает список групп, участвующих в лекции (поток).
     * Возвращается неизменяемая копия списка для соблюдения инкапсуляции.
     *
     * @return список групп
     */
    @Override
    public List<Group> groups() {
        return groups;
    }

    /**
     * Возвращает тип занятия.
     *
     * @return строку "Лекция"
     */
    @Override
    public String kind() {
        return "Лекция";
    }
}