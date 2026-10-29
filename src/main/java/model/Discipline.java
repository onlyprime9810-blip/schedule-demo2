package model;

/**
 * Представляет учебную дисциплину (предмет).
 * Является неизменяемым объектом (record), состоящим из кода и названия.
 *
 * @param code  уникальный код дисциплины (не может быть пустым)
 * @param title название дисциплины (не может быть пустым)
 */
public record Discipline(String code, String title) {

    /**
     * Компактный конструктор для валидации полей.
     * Проверяет, что код и название не являются пустыми строками.
     *
     * @throws IllegalArgumentException если код или название пусты
     */
    public Discipline {
        if (code == null || code.isBlank()) throw new IllegalArgumentException("code пуст");
        if (title == null || title.isBlank()) throw new IllegalArgumentException("title пуст");
    }

    /**
     * Возвращает строковое представление дисциплины в формате "Название (Код)".
     *
     * @return строковое представление дисциплины
     */
    @Override
    public String toString() {
        return title + " (" + code + ")";
    }
}