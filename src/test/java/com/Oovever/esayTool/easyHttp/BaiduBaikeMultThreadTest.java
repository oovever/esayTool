package com.Oovever.esayTool.easyHttp;

import com.Oovever.easyHttp.request.RequestBase;
import com.Oovever.easyHttp.request.RequestFuture;
import com.Oovever.easyHttp.util.HttpUtil;
import org.apache.http.impl.client.FutureRequestExecutionMetrics;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 多线程测试百度百科
 * @author OovEver
 * 2018/7/17 16:09
 */
public class BaiduBaikeMultThreadTest {
    //百度百科地址
    String url = "http://baike.baidu.com/api/openapi/BaikeLemmaCardApi?scope=103&format=json&appid=379020&bk_key=%s&bk_length=600";

    //输出结果
    public void printFutureTask(RequestFuture<String> taskWrap) throws InterruptedException, ExecutionException {

        System.out.println("结果为:" + taskWrap.get());
        FutureRequestExecutionMetrics metrics = taskWrap.metrics();
        System.out.println("startedTime:" + taskWrap.startedTime());
        System.out.println("endedTime:" + taskWrap.endedTime());
        System.out.println("taskDuration:" + taskWrap.taskDuration());
        System.out.println("scheduledTime:" + taskWrap.scheduledTime());

        System.out.println("getActiveConnectionCount:" + metrics.getActiveConnectionCount());
        System.out.println("getFailedConnectionAverageDuration:" + metrics.getFailedConnectionAverageDuration());
        System.out.println("getFailedConnectionCount:" + metrics.getFailedConnectionCount());
        System.out.println("getRequestAverageDuration:" + metrics.getRequestAverageDuration());
        System.out.println("getRequestCount:" + metrics.getRequestCount());
        System.out.println("getScheduledConnectionCount:" + metrics.getScheduledConnectionCount());
        System.out.println("getSuccessfulConnectionAverageDuration:" + metrics.getSuccessfulConnectionAverageDuration());
    }

    //多线程请求百度百科Api
    @Test
    public void requestBaikeTest()  {
        //1000个请求,30个线程
        ExecutorService executorService = null;

        try {
//            创建一个大小30的线程池。此线程池支持定时以及周期性执行任务的需求
            executorService = Executors.newScheduledThreadPool(30);

            int                         maxCount  = 1000;
            List<RequestFuture<String>> taskWraps = new ArrayList<RequestFuture<String>>(maxCount);
            RequestFuture<String>       task;
            RequestBase                 request   = null;

            long start = System.currentTimeMillis();
            for (int i = 0; i < maxCount; i++) {
                request =  HttpUtil.get(String.format(url, (i + 1)+""));
                task = request.executeCallback(executorService, new MyResponseHandler());
                taskWraps.add(task);
            }

            for (RequestFuture<String> taskWrap : taskWraps) {
                printFutureTask(taskWrap);
                //taskWrap.get();
            }

            System.out.println("耗时:" + (System.currentTimeMillis() - start) + "ms");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (executorService != null) {
                executorService.shutdown();
            }

        }
    }

    //百度百科获取json,转Map
    @Test
    public void getJsonTest(){
        RequestBase request =  HttpUtil.get(String.format(url, "Java"));
        Map<?, ?>   map     = request.execute().getJson(Map.class);
        System.out.println(map);
    }
}
