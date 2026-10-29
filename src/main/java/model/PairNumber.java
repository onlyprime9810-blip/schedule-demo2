package model;

import java.time.Duration;
import java.time.LocalTime;

/**
 * Представляет номер учебной пары с фиксированным временем начала и окончания.
 * Используется для определения времени проведения занятий и поиска свободных окон.
 */
public enum PairNumber {

    /** Первая пара (08:30 - 10:00) */
    FIRST(LocalTime.of(8, 30), LocalTime.of(10, 0)),

    /** Вторая пара (10:10 - 11:40) */
    SECOND(LocalTime.of(10, 10), LocalTime.of(11, 40)),

    /** Третья пара (11:50 - 13:20) */
    THIRD(LocalTime.of(11, 50), LocalTime.of(13, 20)),

    /** Четвертая пара (13:50 - 15:20) */
    FOURTH(LocalTime.of(13, 50), LocalTime.of(15, 20)),

    /** Пятая пара (15:30 - 17:00) */
    FIFTH(LocalTime.of(15, 30), LocalTime.of(17, 0)),

    /** Шестая пара (17:10 - 18:40) */
    SIXTH(LocalTime.of(17, 10), LocalTime.of(18, 40));

    private final LocalTime start;
    private final LocalTime end;

    /**
     * Конструктор перечисления.
     *
     * @param start время начала пары
     * @param end   время окончания пары
     */
    PairNumber(LocalTime start, LocalTime end) {
        this.start = start;
        this.end = end;
    }

    /**
     * Возвращает время начала пары.
     *
     * @return время начала
     */
    public LocalTime start() {
        return start;
    }

    /**
     * Возвращает время окончания пары.
     *
     * @return время окончания
     */
    public LocalTime end() {
        return end;
    }

    /**
     * Вычисляет длительность пары.
     *
     * @return продолжительность пары
     */
    public Duration duration() {
        return Duration.between(start, end);
    }

    /**
     * Находит номер пары по времени ее начала.
     *
     * @param start время начала пары
     * @return соответствующий номер пары
     * @throws IllegalArgumentException если пары с таким временем начала не существует
     */
    public static PairNumber fromStart(LocalTime start) {
        for (var p : values()) {
            if (p.start.equals(start)) return p;
        }
        throw new IllegalArgumentException("Нет пары с началом в " + start);
    }
}