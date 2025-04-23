package ru.practicum.shareit;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.shareit.excepton.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Класс обработки исключений при обработке поступивших http запросов
 */
@Slf4j
@RestControllerAdvice
public class ErrorAdvisor {

    /**
     * Обработка исключения MethodArgumentNotValidException - при проверке аргумента метода
     *
     * @param e - исключение
     * @return - список нарушений для отображения в теле ответа
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorMessage onMethodArgumentNotValidException(
            MethodArgumentNotValidException e
    ) {
        final List<ErrorMessage> violations = e.getBindingResult().getFieldErrors().stream()
                .map(error -> new ErrorMessage("[" + error.getField() + "] "
                        + error.getDefaultMessage()))
                .collect(Collectors.toList());
        log.info("400 {}.", e.getMessage());
        return violations.get(0);
    }

    /**
     * Метод обработки пользовательского исключения ValidationException
     *
     * @param exception - исключение проверки данных
     * @return - объект для http ответа с сообщением об ошибке
     */
    @ExceptionHandler(ValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorMessage onValidationException(ValidationException exception) {
        log.info("400 {}.", exception.getMessage());
        return new ErrorMessage(exception.getMessage());
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorMessage notFoundObject(NotFoundException exception) {
        log.info("404 {}.", exception.getMessage());
        return new ErrorMessage(exception.getMessage());
    }

    /**
     * Обработка исключения HttpMessageNotReadableException при поступлении пустого запроса
     *
     * @param e - исключение генерируемое при отсутствии обязательных данных в теле запроса
     * @return - объект для http ответа с сообщением об ошибке
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorMessage onHttpMessageNotReadableException(
            HttpMessageNotReadableException e) {
        log.info("400 {}.", e.getMessage());
        return new ErrorMessage("В запросе отсутствуют необходимые данные." + e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorMessage onInternalException(final InternalServerException e) {
        log.warn("500 {}", e.getMessage());
        return new ErrorMessage(e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ErrorMessage onAccessDeniedException(final AccessDeniedException e) {
        log.warn("403 {}", e.getMessage());
        return new ErrorMessage(e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorMessage onConflictException(final ConflictException e) {
        log.warn("409 {}", e.getMessage());
        return new ErrorMessage(e.getMessage());
    }

    /**
     * Обработка непредвиденного исключения
     *
     * @param e - исключение
     * @return - сообщение об ошибке
     */
    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorMessage handleException(final Exception e) {
        log.warn("Error", e);
        return new ErrorMessage(e.getMessage());
    }
}
