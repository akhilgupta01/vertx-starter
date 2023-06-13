package com.example.vertx.starter;

import com.example.vertx.io.FileReader;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;

public class ApplicationMain {

  public static void main(String[] args) {
    Vertx vertx = Vertx.vertx();

    vertx.deployVerticle(fileReader());
    vertx.deployVerticle(ApplicationMain::lineToRecordMapper, new DeploymentOptions().setInstances(1));
    vertx.deployVerticle(getSalesAggregator());

//    vertx.deployVerticle(new ComposeExample());
  }

  private static FileReader fileReader(){
    return FileReader.builder()
      .name("file.reader")
      .headerLine("Date,Salesperson,Customer Name")
      .filePath("src/main/resources/car_sales_data.csv")
      .build();
  }

  private static LineToRecordMapper lineToRecordMapper() {
    return LineToRecordMapper.builder()
      .name("record.mapper")
      .eventQueue("file.reader.out")
      .build();
  }

  private static SalesAggregator getSalesAggregator() {
    return SalesAggregator.builder()
      .name("sales.aggregator")
      .eventQueue("record.mapper.out").build();
  }
}

