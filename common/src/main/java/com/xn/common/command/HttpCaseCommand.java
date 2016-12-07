package com.xn.common.command;/**
 * Created by xn056839 on 2016/10/31.
 */

import com.alibaba.fastjson.JSON;
import com.xn.common.response.Response;
import com.xn.common.result.Report;
import com.xn.common.util.FileUtil;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.*;

public class HttpCaseCommand implements CaseCommand {
    private static final Logger logger = LoggerFactory.getLogger(HttpCaseCommand.class);
    private Response response = new Response();
    private  String request;
    private String casePath;
    private String result;
    private  String url;
    private String timeout;


    public HttpCaseCommand(String casePath, String request, String url, String timeout) {
        this.request = request;
        this.casePath = casePath;
        this.url = url;
        this.timeout = timeout;

    }



    public Response getResponse() {
        return response;
    }


    public String getRequest() {
        return request;
    }


    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public void httpRequest() {
        // Post请求的url，与get不同的是不需要带参数
        URL postUrl = null;
        try {
            postUrl = new URL(url);

            // 打开连接
            HttpURLConnection connection = (HttpURLConnection) postUrl
                    .openConnection();
            // Output to the connection. Default is
            // false, set to true because post
            // method must write something to the
            // connection
            // 设置是否向connection输出，因为这个是post请求，参数要放在
            // http正文内，因此需要设为true
            connection.setDoOutput(true);
            connection.setConnectTimeout(Integer.parseInt(timeout));
            // Read from the connection. Default is true.
            connection.setDoInput(true);
            // Set the post method. Default is GET
            connection.setRequestMethod("POST");
            // Post cannot use caches
            // Post 请求不能使用缓存
            connection.setUseCaches(false);
            // This method takes effects to
            // every instances of this class.
            // URLConnection.setFollowRedirects是static函数，作用于所有的URLConnection对象。
            // connection.setFollowRedirects(true);

            // This methods only
            // takes effacts to this
            // instance.
            // URLConnection.setInstanceFollowRedirects是成员函数，仅作用于当前函数
            connection.setInstanceFollowRedirects(true);
            // Set the content type to urlencoded,
            // because we will write
            // some URL-encoded content to the
            // connection. Settings above must be set before connect!
            // 配置本次连接的Content-type，配置为application/x-www-form-urlencoded的
            // 意思是正文是urlencoded编码过的form参数，下面我们可以看到我们对正文内容使用URLEncoder.encode
            // 进行编码
            connection.setRequestProperty("Content-Type",
                    "application/x-www-form-urlencoded");
            // 连接，从postUrl.openConnection()至此的配置必须要在connect之前完成，
            // 要注意的是connection.getOutputStream会隐含的进行connect。
            connection.connect();
            DataOutputStream out = new DataOutputStream(connection
                    .getOutputStream());
            // The URL-encoded contend
            // 正文，正文内容其实跟get的URL中'?'后的参数字符串一致

            // DataOutputStream.writeBytes将字符串中的16位的unicode字符以8位的字符形式写道流里面
            out.writeBytes(request);

                logger.info("Http request start: params=[{}]", request);

            out.flush();
            out.close(); // flush and close
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    connection.getInputStream()));
            String line;
            String result = "";
            while ((line = reader.readLine()) != null) {
                result += line;
            }
            Object obj = JSONObject.fromObject(result);
            response.setBody(obj);
            FileUtil.fileWrite(casePath, "请求参数：" + request + "\n返回结果：" + response.toString());
            logger.info("Rpc response:{}", new Object[]{JSON.toJSONString(response)});
            reader.close();
            connection.disconnect();
        } catch (MalformedURLException e) {
            response.setException(e);
            e.printStackTrace();
            result = "error";
            Report.errorPlus();
        } catch (ProtocolException e) {
            e.printStackTrace();
            response.setException(e);
            result = "error";
            Report.errorPlus();
        } catch (ConnectException e){

            e.printStackTrace();
            response.setException(e);
            result = "error";
            Report.errorPlus();
        } catch (IOException e) {
            e.printStackTrace();
            response.setException(e);
            result = "error";
            Report.errorPlus();
        }
    }

    @Override
    public void execute() {
        httpRequest();
    }

    @Override
    public void executeWithException() throws Exception {

    }

    public static void main(String[] args) throws IOException {
        String request = "{\n" +
                "\"buyer_uin\":\"87f4246c-831b-4296-be28-b45cab4a410e\",\n" +
                "\"input_charset\":\"UTF-8\",\n" +
                "\"partner_id\":\"10002\",\n" +
                "\"partner_trade_no\":\"20161206123456\",\n" +
                "\"sign_type\":\"md5\",\n" +
                "\"sign\":\"fvdasfsdfdg\"\n" +
                "}";
        String url1 = "http://10.17.2.125:8098/unipay/gateway/query_payment.do";
//        String url1 = "www.2cto.com";
        try {
            //创建连接
            URL url = new URL(url1);
            HttpURLConnection connection = (HttpURLConnection) url
                    .openConnection();
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setRequestMethod("POST");
            connection.setUseCaches(false);
            connection.setInstanceFollowRedirects(true);
            connection.setConnectTimeout(20000);
            connection.setReadTimeout(300000);
            connection.setRequestProperty("Content-Type", "application/json; charset=utf-8");

            connection.connect();

            //POST请求
            DataOutputStream out = new DataOutputStream(
                    connection.getOutputStream());
JSONObject object=JSONObject.fromObject(request);

            out.writeBytes(object.toString());
            out.flush();
            out.close();
//            InputStream is;
//            if (connection.getResponseCode() >= 400) {
//                is = connection.getErrorStream();
//            } else {
//                is = connection.getInputStream();
//            }


            //读取响应
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    connection.getInputStream()));
            String lines;
            StringBuffer sb = new StringBuffer("");
            while ((lines = reader.readLine()) != null) {
                lines = new String(lines.getBytes(), "utf-8");
                sb.append(lines);
            }
            System.out.println(sb);
            reader.close();
            // 断开连接
            connection.disconnect();
        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }}
}
