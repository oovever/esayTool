package com.Oovever.easyHttp.request;

import org.apache.http.impl.client.FutureRequestExecutionService;
import org.apache.http.impl.client.HttpRequestFutureTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author OovEver
 * 2018/7/3 16:18
 */
public class RequestFuture<V> {
    /**
     * 日志
     */
    protected static Logger logger = LoggerFactory.getLogger(RequestFuture.class);

    /**
     * 任务
     */
    private HttpRequestFutureTask<V> task;

    private FutureRequestExecutionService executionService;

    /**
     * @param task
     * @param executionService
     */
    public RequestFuture(final HttpRequestFutureTask<V> task, final FutureRequestExecutionService executionService) {
        this.task = task;
        this.executionService = executionService;
    }
}
