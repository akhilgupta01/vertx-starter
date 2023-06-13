package com.example.vertx.starter;

import com.example.vertx.core.EventSink;
import com.fasterxml.jackson.core.Base64Variant;
import lombok.Builder;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class SalesAggregator extends EventSink<SaleTransaction> {

  private Map<String, AtomicInteger> aggregatedData = new HashMap<>();

  @Builder
  public SalesAggregator(String name, String eventQueue){
    super(name, eventQueue);
  }

  @Override
  protected void processEvent(SaleTransaction saleTransaction) {
    aggregatedData.computeIfAbsent(saleTransaction.getCarMake(), any -> new AtomicInteger(0))
      .getAndIncrement();
  }

  @Override
  protected void onCompletion() {
    logProgress();
    System.out.println(aggregatedData);
  }
}

