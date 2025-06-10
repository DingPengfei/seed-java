package com.seed.common.exception;

/**
 * 密码错误异常
 * 
 * @author Joey
 */

public class UserPasswordNotMatchException extends Exception {
  public UserPasswordNotMatchException() {
  }

  public UserPasswordNotMatchException(String msg) {
    super(msg);
  }
}