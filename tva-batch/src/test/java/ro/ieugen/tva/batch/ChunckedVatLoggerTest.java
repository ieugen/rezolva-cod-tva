package ro.ieugen.tva.batch;

import org.apache.batchee.util.Batches;
import org.junit.Test;

import javax.batch.operations.JobOperator;
import javax.batch.runtime.BatchRuntime;
import javax.batch.runtime.BatchStatus;
import javax.batch.runtime.JobExecution;
import java.util.Properties;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class ChunckedVatLoggerTest {

    @Test
    public void chunkedTest() throws Exception {

        Properties properties = new Properties();
        properties.setProperty("input", "tva-batch/src/test/resources/vat_codes.csv");
        properties.setProperty("output", "target/work/tva.txt");

        final JobOperator jobOperator = BatchRuntime.getJobOperator();

        long jobId = jobOperator.start("vat-logger", properties);

        Batches.waitForEnd(jobOperator, jobId);

        JobExecution execution = jobOperator.getJobExecution(jobId);

        assertThat(execution.getJobParameters().size(), is(2));
        assertThat(execution.getJobParameters().getProperty("output"), is("target/work/tva.txt"));

        assertThat(execution.getBatchStatus(), is(BatchStatus.COMPLETED));

    }
}
