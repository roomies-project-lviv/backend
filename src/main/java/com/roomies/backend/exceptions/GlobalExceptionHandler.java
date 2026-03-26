package com.roomies.backend.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@ControllerAdvice // Вказує Spring, що цей клас обробляє помилки для всіх контролерів
public class GlobalExceptionHandler {

  // Якщо десь виникає UserNotFoundException, спрацює цей метод
  @ExceptionHandler(UserNotFoundException.class)
  public ResponseEntity<String> handleUserNotFound(UserNotFoundException ex) {
    // Повертаємо повідомлення помилки і статус 404 (NOT_FOUND)
    return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
  }
  // Коли ввели неправильний формат ID (наприклад, з дужками - 400)
  @ExceptionHandler(MethodArgumentTypeMismatchException.class)
  public ResponseEntity<String> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
    String errorMessage = "Некоректний формат ID. Будь ласка, переконайтеся, що ви ввели правильний UUID без дужок чи лапок.";

    // Повертаємо наше повідомлення і статус 400 (BAD_REQUEST)
    return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
  }
}