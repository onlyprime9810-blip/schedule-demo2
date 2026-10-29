package model;

/**
 * Представляет учебную группу студентов.
 * Группа идентифицируется своим уникальным кодом (например, "ПИ-21-1").
 * Количество студентов (размер группы) используется для проверки вместимости аудитории.
 */
public final class Group {
    private final String code;
    private final int size;

    /**
     * Создает новую учебную группу.
     *
     * @param code уникальный код группы (не может быть пустым)
     * @param size количество студентов в группе (должно быть больше 0)
     * @throws IllegalArgumentException если передан пустой код или размер <= 0
     */
    public Group(String code, int size) {
        if (code == null || code.isBlank()) throw new IllegalArgumentException("code пуст");
        if (size <= 0) throw new IllegalArgumentException("size должен быть > 0");
        this.code = code;
        this.size = size;
    }

    /**
     * Возвращает код группы.
     *
     * @return код группы
     */
    public String code() {
        return code;
    }

    /**
     * Возвращает количество студентов в группе.
     *
     * @return размер группы
     */
    public int size() {
        return size;
    }

    /**
     * Сравнивает данную группу с другим объектом.
     * Две группы считаются равными, если совпадают их коды.
     *
     * @param o объект для сравнения
     * @return true, если коды групп совпадают, иначе false
     */
    @Override
    public boolean equals(Object o) {
        return o instanceof Group g && code.equals(g.code);
    }

    /**
     * Возвращает хэш-код группы, основанный на ее коде.
     *
     * @return хэш-код объекта
     */
    @Override
    public int hashCode() {
        return code.hashCode();
    }

    /**
     * Возвращает строковое представление группы в формате "Группа [Код] ([Количество] чел.)".
     *
     * @return строковое представление группы
     */
    @Override
    public String toString() {
        return "Группа " + code + " (" + size + " чел.)";
    }
}