package model;

import java.util.List;

/**
 * Представляет практическое занятие.
 * Проводится для одной учебной группы.
 * Реализует интерфейс {@link Lesson}.
 */
public final class Practice implements Lesson {
    private final Discipline discipline;
    private final Teacher teacher;
    private final PairNumber pair;
    private final Group group;

    /**
     * Создает новое практическое занятие.
     *
     * @param discipline учебная дисциплина
     * @param teacher    преподаватель, ведущий занятие
     * @param pair       номер пары (время проведения)
     * @param group      учебная группа
     * @throws IllegalArgumentException если группа равна null
     */
    public Practice(Discipline discipline, Teacher teacher, PairNumber pair, Group group) {
        if (group == null) throw new IllegalArgumentException("group == null");
        this.discipline = discipline;
        this.teacher = teacher;
        this.pair = pair;
        this.group = group;
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
     * Для практики это всегда одна группа.
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
     * @return строку "Практика"
     */
    @Override
    public String kind() {
        return "Практика";
    }
}