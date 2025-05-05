package ru.practicum.shareit.advisor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

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
        log.error("400 {}.", e.getMessage());
        return violations.get(0);
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
        log.error("Error", e);
        return new ErrorMessage(e.getMessage());
    }
}

