package model;

/**
 * Представляет преподавателя факультета.
 * Преподаватель идентифицируется своим уникальным табельным номером (id).
 * ФИО используется только для отображения.
 */
public final class Teacher {
    private final String id;
    private final String fullName;

    /**
     * Создает нового преподавателя.
     *
     * @param id       уникальный табельный номер (не может быть пустым)
     * @param fullName ФИО преподавателя (не может быть пустым)
     * @throws IllegalArgumentException если табельный номер или ФИО пусты
     */
    public Teacher(String id, String fullName) {
        if (id == null || id.isBlank()) throw new IllegalArgumentException("id пуст");
        if (fullName == null || fullName.isBlank()) throw new IllegalArgumentException("fullName пуст");
        this.id = id;
        this.fullName = fullName;
    }

    /**
     * Возвращает табельный номер преподавателя.
     *
     * @return табельный номер
     */
    public String id() {
        return id;
    }

    /**
     * Возвращает ФИО преподавателя.
     *
     * @return ФИО
     */
    public String fullName() {
        return fullName;
    }

    /**
     * Сравнивает данного преподавателя с другим объектом.
     * Два преподавателя считаются равными, если совпадают их табельные номера.
     *
     * @param o объект для сравнения
     * @return true, если табельные номера совпадают, иначе false
     */
    @Override
    public boolean equals(Object o) {
        return o instanceof Teacher t && id.equals(t.id);
    }

    /**
     * Возвращает хэш-код преподавателя, основанный на табельном номере.
     *
     * @return хэш-код объекта
     */
    @Override
    public int hashCode() {
        return id.hashCode();
    }

    /**
     * Возвращает строковое представление преподавателя в формате "ФИО [Табельный номер]".
     *
     * @return строковое представление преподавателя
     */
    @Override
    public String toString() {
        return fullName + " [" + id + "]";
    }
}