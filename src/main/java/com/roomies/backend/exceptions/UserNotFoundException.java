package com.roomies.backend.exceptions;

public class UserNotFoundException extends RuntimeException {
  public UserNotFoundException(String message) {
    super(message); // Передаємо повідомлення про помилку в базовий клас
  }
}