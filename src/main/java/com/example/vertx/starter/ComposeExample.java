package com.example.vertx.starter;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Promise;

import java.util.function.Function;

/**
 * An example showing how to use {@link io.vertx.core.Future#compose(Function)}
 */
public class ComposeExample extends AbstractVerticle {
    @Override
    public void start() throws Exception {
        Future<String> future = readRawRecord();
        future.compose(this::parseToEntity)
                .onComplete(ar -> {
                    if (ar.failed()) {
                        System.out.println("Something bad happened");
                        ar.cause().printStackTrace();
                    } else {
                        System.out.println("Result: " + ar.result());
                    }
                });
    }

    private Future<String> readRawRecord() {
        Promise<String> promise = Promise.promise();
        // mimic something that take times
        vertx.setPeriodic(1000, l -> promise.complete("Customer_" + l));
        return promise.future();
    }

    private Future<SaleTransaction> parseToEntity(String name) {
        Promise<SaleTransaction> promise = Promise.promise();
        // mimic something that take times
        vertx.setTimer(100, l -> promise.complete(SaleTransaction.builder().customerName(name).build()));
        return promise.future();
    }

}