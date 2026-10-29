package model;

import java.util.Objects;

/**
 * Представляет учебную аудиторию.
 * Аудитория идентифицируется комбинацией номера корпуса и номера комнаты.
 * Вместимость аудитории учитывается при проверке возможности проведения занятия.
 */
public final class Audience {
    private final String building;
    private final String room;
    private final int capacity;

    /**
     * Создает новую аудиторию.
     *
     * @param building номер корпуса (не может быть пустым)
     * @param room     номер аудитории (не может быть пустым)
     * @param capacity вместимость аудитории (должна быть больше 0)
     * @throws IllegalArgumentException если переданы некорректные данные
     */
    public Audience(String building, String room, int capacity) {
        if (building == null || building.isBlank()) throw new IllegalArgumentException("building пуст");
        if (room == null || room.isBlank()) throw new IllegalArgumentException("room пуст");
        if (capacity <= 0) throw new IllegalArgumentException("capacity должна быть > 0");
        this.building = building;
        this.room = room;
        this.capacity = capacity;
    }

    /**
     * Возвращает номер корпуса.
     *
     * @return номер корпуса
     */
    public String building() {
        return building;
    }

    /**
     * Возвращает номер аудитории (комнаты).
     *
     * @return номер аудитории
     */
    public String room() {
        return room;
    }

    /**
     * Возвращает вместимость аудитории.
     *
     * @return количество посадочных мест
     */
    public int capacity() {
        return capacity;
    }

    /**
     * Сравнивает данную аудиторию с другим объектом.
     * Две аудитории считаются равными, если совпадают их корпус и номер комнаты.
     *
     * @param o объект для сравнения
     * @return true, если объекты равны, иначе false
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Audience other)) return false;
        return building.equals(other.building) && room.equals(other.room);
    }

    /**
     * Возвращает хэш-код аудитории, основанный на номере корпуса и комнаты.
     *
     * @return хэш-код объекта
     */
    @Override
    public int hashCode() {
        return Objects.hash(building, room);
    }

    /**
     * Возвращает строковое представление аудитории в формате "Корпус-Комната (мест: N)".
     *
     * @return строковое представление аудитории
     */
    @Override
    public String toString() {
        return building + "-" + room + " (мест: " + capacity + ")";
    }
}