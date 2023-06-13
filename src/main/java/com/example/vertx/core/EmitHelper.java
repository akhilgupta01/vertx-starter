package com.example.vertx.core;

import io.vertx.core.AbstractVerticle;

import java.util.concurrent.atomic.AtomicLong;

public class EmitHelper<OUT> implements EventEmitter<OUT> {
  private String eventQueue;
  private AbstractVerticle verticle;

  private final AtomicLong emittedCount = new AtomicLong(0);

  protected EmitHelper(String eventQueue, AbstractVerticle verticle){
    this.eventQueue = eventQueue;
    this.verticle = verticle;
  }

  @Override
  public void emit(OUT message) {
    verticle.getVertx().eventBus().publish(eventQueue, message);
    emittedCount.getAndIncrement();
  }

  public long emitCount(){
    return emittedCount.get();
  }

  public void logProgress() {
    System.out.println("Emitted " + emitCount() + " messages on " + eventQueue);
  }

  public void signalEnd() {
    verticle.getVertx().eventBus().publish("admin.queue", "END");
  }
}
