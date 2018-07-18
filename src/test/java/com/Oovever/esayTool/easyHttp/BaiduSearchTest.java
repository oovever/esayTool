package com.Oovever.esayTool.easyHttp;

import com.Oovever.easyHttp.util.HttpUtil;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author OovEver
 * 2018/7/18 21:57
 */
public class BaiduSearchTest {

    String url = "https://baidu.com";

    //请求百度首页
    //注意,这里会自动处理https单向认证, 可以直接请求https
    @Test
    public void baiduIndexTest(){
        String html = HttpUtil.get(url).execute().getString();
        System.out.println(html);
        Assert.assertTrue(html.contains("百度一下，你就知道"));
    }

    //百度搜索 关键字 alibaba
    @Test
    public void baiduSearchTest(){
        String format = String.format(url + "/s?wd=%s&tn=98012088_5_dg&ch=11", "alibaba");
        String html = HttpUtil.get(format).execute().getString();
        System.out.println(html);
    }

    //百度搜索 关键字 alibaba, 并写入文件
    @Test
    public void baiduSearchWriteFileTest(){
        String format = String.format(url + "/s?wd=%s&tn=98012088_5_dg&ch=11", "alibaba");
        HttpUtil.get(format).execute().transferTo("d:/baiduSearchResult.html");
    }
}
