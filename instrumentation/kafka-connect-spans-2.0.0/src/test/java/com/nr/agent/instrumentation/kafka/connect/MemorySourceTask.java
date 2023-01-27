package com.nr.agent.instrumentation.kafka.connect;

import org.apache.kafka.connect.data.Schema;
import org.apache.kafka.connect.source.SourceRecord;
import org.apache.kafka.connect.source.SourceTask;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;

public class MemorySourceTask extends SourceTask {
    private ArrayBlockingQueue<String> queue = new ArrayBlockingQueue<>(2);
    private int offset = 0;
    private static final Map<String, String> SOURCE_PARTITION = Collections.singletonMap("location", "memory");
    @Override
    public String version() {
        return "1";
    }

    @Override
    public void start(Map<String, String> props) {

    }

    @Override
    public List<SourceRecord> poll() throws InterruptedException {
        List<SourceRecord> records = new ArrayList<>();
        while(true) {
            String value = queue.poll(1, TimeUnit.SECONDS);
            if (value == null) {
                break;
            }
            offset += 1;
            records.add(newSourceRecord(value));
        }
        if (records.isEmpty()) {
            return null;
        }
        return records;
    }

    @NotNull
    private SourceRecord newSourceRecord(String value) {
        Map<String, Integer> sourceOffset = Collections.singletonMap("position", offset);
        return new SourceRecord(SOURCE_PARTITION, sourceOffset, "topic", 0, Schema.STRING_SCHEMA, value);
    }

    @Override
    public void stop() {

    }
}
