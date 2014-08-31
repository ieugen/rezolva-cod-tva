package ro.ieugen.tva.batch;

import lombok.extern.slf4j.Slf4j;

import javax.batch.api.chunk.ItemWriter;
import java.io.Serializable;
import java.util.List;

@Slf4j
public class VatRecordLogWriter implements ItemWriter {
    @Override
    public void open(Serializable checkpoint) throws Exception {
        // noop
    }

    @Override
    public void close() throws Exception {
        // noop
    }

    @Override
    public void writeItems(List<Object> items) throws Exception {
        for (Object item : items) {
            log.info("Writing record {}", item);
        }
    }

    @Override
    public Serializable checkpointInfo() throws Exception {
        return null;
    }
}
