package com.example.vertx.core;

import io.vertx.core.AbstractVerticle;

public abstract class EventGenerator<OUT> extends AbstractVerticle implements EventEmitter<OUT>{
  private String name;

  private EmitHelper<OUT> emitHelper;

  public EventGenerator(String name){
    this.name = name;
    this.emitHelper = new EmitHelper<>(name + ".out", this);
  }

  @Override
  public void start() {
    try {
      init();
      vertx.setPeriodic(1000, t -> emitHelper.logProgress());
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  protected abstract void init();

  public void emit(OUT message){
    emitHelper.emit(message);
  }

  public void signalEnd(){
    emitHelper.signalEnd();
  }

  protected void logProgress(){
    emitHelper.logProgress();
  }
}
