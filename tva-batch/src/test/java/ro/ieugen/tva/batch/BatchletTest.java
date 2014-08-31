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

public class BatchletTest {

    @Test
    public void batchJobStarts() throws Exception {

        Properties properties = new Properties();
        properties.setProperty("input", "src/test/resources/vat_codes.csv");
        properties.setProperty("output", "target/batch-out");

        final JobOperator jobOperator = BatchRuntime.getJobOperator();

        long jobId = jobOperator.start("batchlet", properties);

        Batches.waitForEnd(jobOperator, jobId);

        JobExecution execution = jobOperator.getJobExecution(jobId);

        assertThat(execution.getJobParameters().size(), is(2));
        assertThat(execution.getJobParameters().getProperty("output_file"), is("target/batch-out"));

        assertThat(execution.getBatchStatus(), is(BatchStatus.COMPLETED));
    }

}
