package com.example.vertx.core;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.eventbus.Message;

import java.util.concurrent.atomic.AtomicLong;


public abstract class EventSink<IN> extends AbstractVerticle {
  private String name;

  private String eventQueue;

  private final AtomicLong processedCount = new AtomicLong(0);

  public EventSink(String name, String eventQueue){
    this.name = name;
    this.eventQueue = eventQueue;
  }

  @Override
  public void start() {
    vertx.eventBus()
      .consumer(eventQueue, this::handleEvent)
      .exceptionHandler(this::handleException);
    vertx.setPeriodic(1000, t -> logProgress());
  }

  @Override
  public void stop() {
    logProgress();
    onCompletion();
  }

  private void handleException(Throwable throwable) {
    throwable.printStackTrace(System.out);
  }

  protected void handleEvent(Message<IN> message){
    try {
      processEvent(message.body());
      processedCount.incrementAndGet();
    }catch(Exception e){
      e.printStackTrace(System.out);
    }
  }

  public void logProgress() {
    System.out.println("Processed " + processedCount.get() + " messages by " + name);
  }

  protected abstract void processEvent(IN input);

  protected abstract void onCompletion();

}
