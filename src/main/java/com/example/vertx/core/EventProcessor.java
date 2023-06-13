package com.example.vertx.core;

import com.example.vertx.starter.SaleTransaction;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.eventbus.Message;

import java.util.concurrent.atomic.AtomicInteger;


public abstract class EventProcessor<IN, OUT> extends AbstractVerticle {
  private String name;

  private String eventQueue;

  private EmitHelper<OUT> emitHelper;

  private AtomicInteger processedCounter = new AtomicInteger(0);

  protected EventProcessor(String name, String eventQueue){
    this.name = name;
    this.eventQueue = eventQueue;
    this.emitHelper = new EmitHelper<>(name + ".out", this);
  }

  @Override
  public void start() {
    vertx.eventBus().localConsumer(eventQueue, this::handleEvent);
    vertx.setPeriodic(1000, t -> emitHelper.logProgress());
  }

  protected void handleEvent(Message<IN> message){
    try {
      OUT output = processEvent(message.body());
      processedCounter.getAndIncrement();
      emitHelper.emit(output);
    }catch(Exception e){
      e.printStackTrace(System.out);
      message.fail(500, "The message failed");
    }
  }

  protected abstract OUT processEvent(IN input);

}
