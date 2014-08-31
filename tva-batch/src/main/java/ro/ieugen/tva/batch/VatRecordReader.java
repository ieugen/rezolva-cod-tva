package ro.ieugen.tva.batch;

import ro.ieugen.tva.batch.csv.VatRecord;

import javax.batch.api.BatchProperty;
import javax.batch.api.chunk.ItemReader;
import javax.inject.Inject;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.Serializable;

public class VatRecordReader implements ItemReader {

    @Inject
    @BatchProperty
    private String input;

    private BufferedReader reader;

    @Override
    public void open(Serializable checkpoint) throws Exception {
        if (input == null) {
            throw new RuntimeException("Can't find input.");
        }

        final File file = new File(input);
        if (!file.exists()) {
            throw new RuntimeException("'" + input + "' does not exist.");
        }

        reader = new BufferedReader(new FileReader(file));
    }

    @Override
    public void close() throws Exception {
        if (reader != null) {
            reader.close();
        }
    }

    @Override
    public Object readItem() throws Exception {
        String readLine = reader.readLine();
        VatRecord record = null;
        if (readLine != null) {
            String[] line = readLine.split(";");

            record = VatRecord.builder()
                    .submittedName(line[0])
                    .vatCode(line[1])
                    .build();
        }

        return record;
    }

    @Override
    public Serializable checkpointInfo() throws Exception {
        return null;
    }
}
