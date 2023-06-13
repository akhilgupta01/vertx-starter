package com.example.vertx.core;

public interface EventEmitter<OUT> {
  void emit(OUT message);
}
