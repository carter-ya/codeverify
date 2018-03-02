package com.ifengxue.code.verify;

import java.util.Objects;

public final class Args {

  public static void isTrue(boolean condition, String message) {
    if (!condition) {
      throw new IllegalArgumentException(message);
    }
  }

  public static void isFalse(boolean condition, String message) {
    isTrue(!condition, message);
  }

  public static void isNotNull(Object obj) {
    Objects.requireNonNull(obj);
  }

  public static void isNotNull(Object obj, String message) {
    Objects.requireNonNull(obj, message);
  }

  public static void isNotNull(Object... objects) {
    for (Object obj : objects) {
      isNotNull(obj);
    }
  }
}
