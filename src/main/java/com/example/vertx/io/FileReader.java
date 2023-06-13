package com.example.vertx.io;

import com.example.vertx.core.EventGenerator;
import com.example.vertx.starter.SaleTransaction;
import io.vertx.core.Handler;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.file.OpenOptions;
import io.vertx.core.parsetools.RecordParser;
import lombok.Builder;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class FileReader extends EventGenerator<String> {

    private String filePath;

    private String headerLine;

    private OpenOptions openOptions;

    private RecordParser recordParser;

    private Map<String, AtomicInteger> aggregatedData = new HashMap<>();

    @Builder
    public FileReader(String name, String filePath, String headerLine, OpenOptions openOptions, RecordParser recordParser) {
        super(name);
        this.filePath = filePath;
        this.headerLine = headerLine == null ? "DEFAULT_HEADER_LINE" : headerLine;
        this.openOptions = openOptions == null ? new OpenOptions().setRead(true) : openOptions;
        this.recordParser = recordParser == null ? RecordParser.newDelimited("\n") : recordParser;
    }

    protected void init() {
        Handler<Buffer> handler1 = buffer -> {
            String line = buffer.toString();
            if (!line.startsWith(headerLine)) {
                vertx.runOnContext(v -> mapToEntity(line));
            }
        };

        recordParser
                .handler(handler1)
                .exceptionHandler(this::onReadError)
                .endHandler(v -> {
                    System.out.println("Record parsing done");
                });

        vertx.fileSystem()
                .open(filePath, openOptions)
                .onSuccess(asyncFile -> asyncFile
                        .handler(recordParser)
                        .endHandler(this::onReadCompletion))
                .onFailure(this::onReadError);

    }

    private void onReadCompletion(Void v) {
        logProgress();
        System.out.println(aggregatedData);
        vertx.close();
    }

    private void onReadError(Throwable t) {
        System.out.println("Error while reading file.. Shutting down!!");
        t.printStackTrace(System.err);
        vertx.close();
    }

    private void mapToEntity(String message) {
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
        vertx.runOnContext(v -> aggregateData(record));
    }

    private void aggregateData(SaleTransaction saleTransaction) {
        aggregatedData
                .computeIfAbsent(saleTransaction.getCarMake(), any -> new AtomicInteger(0))
                .getAndIncrement();
    }

}


