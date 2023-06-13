package com.example.vertx.starter;

import io.vertx.core.buffer.Buffer;
import io.vertx.core.eventbus.MessageCodec;

public class SalesTransactionCodec implements  MessageCodec<SaleTransaction, SaleTransaction> {
  @Override
  public void encodeToWire(Buffer buffer, SaleTransaction customMessage) {
  }

  @Override
  public SaleTransaction decodeFromWire(int position, Buffer buffer) {
    return null;
  }

  @Override
  public SaleTransaction transform(SaleTransaction customMessage) {
    // If a message is sent *locally* across the event bus.
    // This example sends message just as is
    return customMessage;
  }

  @Override
  public String name() {
    // Each codec must have a unique name.
    // This is used to identify a codec when sending a message and for unregistering codecs.
    return this.getClass().getSimpleName();
  }

  @Override
  public byte systemCodecID() {
    // Always -1
    return -1;
  }
}
