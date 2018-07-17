package com.Oovever.esayTool.easyHttp;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.util.EntityUtils;
import org.omg.CORBA.portable.OutputStream;
import org.omg.CORBA.portable.ResponseHandler;

import java.io.IOException;

/**
 * @author OovEver
 * 2018/7/17 16:19
 */
public class MyResponseHandler implements org.apache.http.client.ResponseHandler<String> {
    @Override
    public String handleResponse(HttpResponse response) throws ClientProtocolException, IOException {
        //ResponseWrap wrap = new ResponseWrap(response);
        //注意:getEntity是不可重复读取的对象,如果需要重复读取,请使用ResponseWrap包装器
        return EntityUtils.toString(response.getEntity());
    }
}
