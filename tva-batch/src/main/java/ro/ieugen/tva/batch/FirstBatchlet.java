package ro.ieugen.tva.batch;

import lombok.extern.slf4j.Slf4j;

import javax.batch.api.Batchlet;
import javax.batch.runtime.context.JobContext;
import javax.batch.runtime.context.StepContext;
import javax.inject.Inject;

@Slf4j
public class FirstBatchlet implements Batchlet {

    @Inject
    private JobContext jobContext;
    @Inject
    private StepContext stepContext;

    @Override
    public String process() throws Exception {
        log.info("This is my first batchlet ! {}", jobContext.getExecutionId());

        return "hello";
    }

    @Override
    public void stop() throws Exception {
        log.info("Called stop");
    }
}
