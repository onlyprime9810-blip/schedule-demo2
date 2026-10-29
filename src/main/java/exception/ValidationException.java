package exception;

/**
 * Исключение, выбрасываемое при нарушении правил валидации данных.
 * Например, если вместимость аудитории меньше, чем количество студентов,
 * или если переданы некорректные аргументы при создании объектов.
 */
public class ValidationException extends RuntimeException {

    /**
     * Создает новое исключение с указанным сообщением об ошибке.
     *
     * @param message текстовое описание ошибки валидации
     */
    public ValidationException(String message) {
        super(message);
    }
}