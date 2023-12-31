package com.example.vertx.starter;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;

@Data
@Builder
public class SaleTransaction implements Serializable {
  private LocalDate date;
  private String salesperson;
  private String customerName;
  private String carMake;
  private String carModel;
  private int carYear;
  private double salePrice;
  private double commissionRate;
  private double commissionEarned;
}
