package com.Oovever.easyHttp.request;

import org.apache.http.impl.client.FutureRequestExecutionMetrics;
import org.apache.http.impl.client.FutureRequestExecutionService;
import org.apache.http.impl.client.HttpRequestFutureTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 *
 * HttpRequestFutureTask包装器,带有FutureRequestExecutionMetrics
 * 这是一个异步的执行结果, 你可以取消任务
 * 并且带有跟踪指标,可以查看这任务的开始、结束时间，任务执行时长，请求数量
 * @author OovEver
 * 2018/7/3 16:18
 */
public class RequestFuture<V>   {
    /**
     * 日志
     */
    protected static Logger logger = LoggerFactory.getLogger(RequestFuture.class);

    /**
     * 任务
     */
    private HttpRequestFutureTask<V> task;
    //通过FutureRequestExecutionService异步访问
    private FutureRequestExecutionService executionService;

    /**
     * @param task 要执行的任务
     * @param executionService FutureRequestExecutionService实例 用于异步访问
     */
    public RequestFuture(final HttpRequestFutureTask<V> task, final FutureRequestExecutionService executionService) {
        this.task = task;
        this.executionService = executionService;
    }

    /**
     * 取消任务
     * @param mayInterruptIfRunning 是否终止任务
     * @return 返回取消结果
     */
    public boolean cancel(final boolean mayInterruptIfRunning) {
        return task.cancel(mayInterruptIfRunning);
    }
    /**
     * 返回任务的执行事件
     * @return the time in millis the task was scheduled.
     *
     */
    public long scheduledTime() {
        return task.scheduledTime();
    }

    /**
     * 返回任务的开始时间
     * @return the time in millis the task was started.
     */
    public long startedTime() {
        return task.startedTime();
    }

    /**
     * 返回任务的结束时间
     * @return the time in millis the task was finished/cancelled.
     */
    public long endedTime() {
        return task.endedTime();
    }

    /**
     *
     * @return 请求的执行事件
     */
    public long requestDuration() {
        return task.requestDuration();
    }

    /**
     * @return 任务持续时间
     */
    public long taskDuration() {
        return task.taskDuration();
    }

    /**
     * 任务指标实例
     * @return 获取{@link FutureRequestExecutionMetrics}
     */
    public FutureRequestExecutionMetrics metrics() {
        return executionService.metrics();
    }

    /**
     * 关闭连接池,将中断所有任务的执行
     */
    public void shutdown(){
        try {
            executionService.close();
        } catch (IOException e) {
            logger.warn(e.getMessage());
        }
    }

    /**
     * 阻塞获取返回值
     * @return 阻塞获取的返回值
     * @throws InterruptedException
     * @throws ExecutionException
     */
    public V get() throws InterruptedException, ExecutionException{
        return task.get();
    }

    /**
     * 阻塞获取返回值,当超时后抛出异常
     * @param timeout 超时时长
     * @param unit 时间单位
     * @return 阻塞获取返回值,当超时后抛出异常
     * @throws InterruptedException
     * @throws ExecutionException
     * @throws TimeoutException
     */
    public V get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException{
        return task.get(timeout, unit);
    }

    @Override
    public String toString() {
        return task.toString();
    }
}
