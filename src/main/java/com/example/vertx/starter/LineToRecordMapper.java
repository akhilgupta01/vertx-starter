package com.example.vertx.starter;

import com.example.vertx.core.EventProcessor;
import lombok.Builder;

import java.time.LocalDate;

public class LineToRecordMapper extends EventProcessor<String, SaleTransaction> {

  @Builder
  public LineToRecordMapper(String name, String eventQueue) {
    super(name, eventQueue);
  }

  @Override
  protected SaleTransaction processEvent(String message) {
    String[] tokens = message.split(",");
    SaleTransaction record = SaleTransaction.builder()
      .date(LocalDate.parse(tokens[0]))
      .salesperson(tokens[1])
      .customerName(tokens[2])
      .carMake(tokens[3])
      .carModel(tokens[4])
      .carYear(Integer.parseInt(tokens[5]))
      .salePrice(Double.parseDouble(tokens[6]))
      .commissionRate(Double.parseDouble(tokens[7]))
      .commissionEarned(Double.parseDouble(tokens[8]))
      .build();

    return record;
  }
}

