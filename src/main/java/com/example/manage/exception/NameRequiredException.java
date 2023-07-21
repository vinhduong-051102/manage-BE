package com.example.manage.exception;

public class NameRequiredException extends RuntimeException {
  public NameRequiredException(String message) {
    super(message);
  }
}
