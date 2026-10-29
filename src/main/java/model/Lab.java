package model;

import java.util.List;

/**
 * Представляет лабораторное занятие.
 * Проводится для подгруппы одной учебной группы.
 * Реализует интерфейс {@link Lesson}.
 */
public final class Lab implements Lesson {
    private final Discipline discipline;
    private final Teacher teacher;
    private final PairNumber pair;
    private final Group group;
    private final String subgroup;

    /**
     * Создает новое лабораторное занятие.
     *
     * @param discipline учебная дисциплина
     * @param teacher    преподаватель, ведущий занятие
     * @param pair       номер пары (время проведения)
     * @param group      учебная группа
     * @param subgroup   название или номер подгруппы (не может быть пустым)
     * @throws IllegalArgumentException если группа равна null или подгруппа пуста
     */
    public Lab(Discipline discipline, Teacher teacher, PairNumber pair, Group group, String subgroup) {
        if (group == null) throw new IllegalArgumentException("group == null");
        if (subgroup == null || subgroup.isBlank()) throw new IllegalArgumentException("subgroup пуст");
        this.discipline = discipline;
        this.teacher = teacher;
        this.pair = pair;
        this.group = group;
        this.subgroup = subgroup;
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
     * Возвращает список групп, участвующих в занятии.
     * Для лабораторной работы это всегда одна группа.
     *
     * @return список из одной группы
     */
    @Override
    public List<Group> groups() {
        return List.of(group);
    }

    /**
     * Возвращает тип занятия.
     *
     * @return строку "Лабораторная"
     */
    @Override
    public String kind() {
        return "Лабораторная";
    }
}