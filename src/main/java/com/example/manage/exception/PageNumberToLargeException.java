package com.example.manage.exception;

public class PageNumberToLargeException extends RuntimeException {
  public PageNumberToLargeException(String message) {
    super(message);
  }
}
