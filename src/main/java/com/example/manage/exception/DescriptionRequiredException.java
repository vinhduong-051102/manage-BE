package com.example.manage.exception;

public class DescriptionRequiredException extends RuntimeException {
  public DescriptionRequiredException(String message) {
    super(message);
  }
}
