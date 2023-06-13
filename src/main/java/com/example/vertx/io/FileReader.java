package com.example.vertx.io;

import com.example.vertx.core.EventGenerator;
import io.vertx.core.file.OpenOptions;
import io.vertx.core.parsetools.RecordParser;
import lombok.Builder;

public class FileReader extends EventGenerator<String> {

  private String filePath;

  private String headerLine;

  private OpenOptions openOptions;

  private RecordParser recordParser;

  @Builder
  public FileReader(String name, String filePath, String headerLine, OpenOptions openOptions, RecordParser recordParser) {
    super(name);
    this.filePath = filePath;
    this.headerLine = headerLine == null? "DEFAULT_HEADER_LINE" : headerLine;
    this.openOptions = openOptions == null ? new OpenOptions().setRead(true) : openOptions;
    this.recordParser = recordParser == null ? RecordParser.newDelimited("\n") : recordParser;
  }

  protected void init() {
    recordParser.handler(buffer -> {
        String line = buffer.toString();
        if (!line.startsWith(headerLine)) {
          emit(line);
        }
      })
      .exceptionHandler(this::onReadError)
      .endHandler(v -> {
        System.out.println("Record parsing done");
      });

    vertx.fileSystem().open(filePath, openOptions)
      .onSuccess(asyncFile -> asyncFile
        .handler(recordParser)
        .endHandler(this::onReadCompletion))
      .onFailure(this::onReadError);

  }

  private void onReadCompletion(Void v) {
    logProgress();
    //vertx.close();
  }

  private void onReadError(Throwable t) {
    System.out.println("Error while reading file.. Shutting down!!");
    t.printStackTrace(System.err);
    vertx.close();
  }

}

