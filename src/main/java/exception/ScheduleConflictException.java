package exception;

import model.ScheduleEntry;

/**
 * Исключение, выбрасываемое при попытке добавить занятие, которое создает конфликт.
 * Конфликт может возникнуть, если аудитория, преподаватель или группа уже заняты
 * в указанное время.
 */
public class ScheduleConflictException extends RuntimeException {

    /**
     * Запись расписания, с которой возник конфликт.
     */
    private final ScheduleEntry conflicting;

    /**
     * Создает новое исключение с указанным сообщением и записью, вызвавшей конфликт.
     *
     * @param message    текстовое описание ошибки
     * @param conflicting запись расписания, с которой произошел конфликт
     */
    public ScheduleConflictException(String message, ScheduleEntry conflicting) {
        super(message);
        this.conflicting = conflicting;
    }

    /**
     * Возвращает запись расписания, из-за которой возник конфликт.
     *
     * @return запись расписания, вызвавшая конфликт
     */
    public ScheduleEntry conflicting() {
        return conflicting;
    }
}