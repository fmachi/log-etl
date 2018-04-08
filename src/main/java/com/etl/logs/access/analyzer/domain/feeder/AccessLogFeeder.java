package com.etl.logs.access.analyzer.domain.feeder;

import com.etl.logs.access.analyzer.domain.accesslog.Access;
import com.etl.logs.access.analyzer.port.io.LogLinesSource;
import com.etl.logs.access.analyzer.port.persistence.accesslog.LogAccessRepository;
import io.reactivex.Flowable;
import io.reactivex.functions.Action;
import io.reactivex.internal.operators.flowable.FlowableInternalHelper;
import io.reactivex.internal.subscribers.LambdaSubscriber;
import io.reactivex.schedulers.Schedulers;
import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Subscriber;

import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
public class AccessLogFeeder {

    private final LogLinesSource logLinesSource;
    private final LogAccessRepository logAccessRepository;

    private AtomicInteger readLinesCounter = new AtomicInteger(0);
    private AtomicInteger writtenRowsCounter = new AtomicInteger(0);
    private final int parallelism;
    private final int bufferSize;
    private final int prefetchSize;

    public AccessLogFeeder(LogLinesSource logLinesSource,
                           LogAccessRepository logAccessRepository,
                           PipelineConfiguration pipelineConfiguration) {
        this.logLinesSource = logLinesSource;
        this.logAccessRepository = logAccessRepository;
        log.info("Log access feeder will run with pipeline configuration {}",pipelineConfiguration);
        parallelism = pipelineConfiguration.getParallelism();
        bufferSize = pipelineConfiguration.getBufferSize();
        prefetchSize = pipelineConfiguration.getPrefetchSize();
    }

    public void feed() {

        logAccessRepository.cleanPreviousElaboration();

        CountDownLatch countDownLatch = new CountDownLatch(parallelism);

        Flowable<String[]> logLinesSourceFlowable = buildFlowable();

        Subscriber<? super List<Access>>[] subscribers = buildSubscribers(countDownLatch);

        logLinesSourceFlowable
                .map(this::extractAccess)
                .buffer(bufferSize)
                .parallel(parallelism, prefetchSize)
                .runOn(Schedulers.computation())
                .subscribe(subscribers);

        awaitForTermination(countDownLatch);
    }

    private void awaitForTermination(CountDownLatch countDownLatch) {
        try {
            countDownLatch.await();
        } catch (Exception ex) {
            log.error("Thread stopped while awaiting for log lines feeder to complete");
        }
    }

    private Subscriber<? super List<Access>>[] buildSubscribers(CountDownLatch countDownLatch) {
        Subscriber<? super List<Access>>[] subscribers = new Subscriber[parallelism];

        for (int i = 0; i < parallelism; i++) {
            subscribers[i] = new LambdaSubscriber<>(this::handleBatch,
                    handleError(countDownLatch),
                    handleComplete(countDownLatch),
                    FlowableInternalHelper.RequestMax.INSTANCE
            );
        }
        return subscribers;
    }

    private Flowable<String[]> buildFlowable() {
        return Flowable.generate(
                () -> logLinesSource,
                (iterator, emitter) -> {
                    Iterator<String[]> logLinesIterator = logLinesSource.iterator();
                    if (logLinesIterator.hasNext()) {
                        readLinesCounter.incrementAndGet();
                        emitter.onNext(logLinesIterator.next());
                    } else {
                        emitter.onComplete();
                    }
                },
                LogLinesSource::close
        );
    }

    private Action handleComplete(CountDownLatch countDownLatch) {
        return () -> {
            log.info("Finished {}", countDownLatch.getCount());
            countDownLatch.countDown();
        };
    }

    private io.reactivex.functions.Consumer<Throwable> handleError(CountDownLatch countDownLatch) {
        return throwable -> {
            log.error("An error occurred publishing batch", throwable);
            countDownLatch.countDown();
        };
    }

    void handleBatch(List<Access> rows) {
        writtenRowsCounter.updateAndGet(a -> a + rows.size());
        long started = System.currentTimeMillis();
        logAccessRepository.insertLogRows(rows);
        log.info("Writing {} {} in {}", readLinesCounter.get(), writtenRowsCounter.get(), System.currentTimeMillis() - started);
    }

    Access extractAccess(String[] tokens) {
        int index =0;
        return new Access(
                tokens[index++],
                tokens[index++],
                tokens[index++],
                tokens[index++],
                tokens[index]
        );
    }
}
