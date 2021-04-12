package com.demo;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: neals
 * Date: Nov 17, 2014
 * Time: 11:34:51 PM
 * Description: this is a util for call
 * 1. absolute URI
 * 2. request method: GET/POST
 * 3. timeout, default 15s
 * //TODO: simple tool, will refactor
 * 4. request type: http/https
 * 5. response type: json/string/xml
 * 6. request content type: json/string/xml
 * 7. allow redirect after request failed
 * 8. failed log
 */
public class URLConnectionUtil {

    public static final String GET = "GET";
    public static final String POST = "POST";

    public static String request(String requestUrl, String requestMethod, String timeout){
        return request(requestUrl, requestMethod, null, timeout, null);
    }


    public static String request(String requestUrl, String requestMethod, String requestBody, String timeout){
        return request(requestUrl, requestMethod, requestBody, timeout, null);
    }

    public static String request(String requestUrl, String requestMethod, String requestBody, String timeout, Map headers){
    	BufferedReader br = null;
        try {
            URL url = new URL(requestUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            requestMethod = (null == requestMethod || ("").equals(requestMethod)) ? URLConnectionUtil.GET : requestMethod;

            if(headers != null){
                for(Iterator it = headers.entrySet().iterator(); it.hasNext();){
                    Map.Entry obj = (Map.Entry) it.next();
                    conn.setRequestProperty(String.valueOf(obj.getKey()), String.valueOf(obj.getValue()));
                }
            }
            conn.setRequestMethod(requestMethod);
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setUseCaches(false);
            conn.setDefaultUseCaches(false);
            conn.setInstanceFollowRedirects(false);
            HttpURLConnection.setFollowRedirects(false);
            // time out - default: 15s
            timeout = (null == timeout || ("").equals(timeout)) ? "60000" : timeout;
            System.setProperty ("sun.net.client.defaultConnectTimeout", timeout);
            System.setProperty ("sun.net.client.defaultReadTimeout", timeout);
            // request
            if(requestBody != null){
                BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream(),"utf-8"));
                bw.write(requestBody);
                bw.flush();
                bw.close();
            }

            // response
            InputStream httpin = conn.getInputStream();
            if (httpin == null) {
                httpin = conn.getErrorStream();
            }
            br= new BufferedReader(new InputStreamReader(httpin, "utf-8"));

            StringBuffer sb = new StringBuffer();
            String inputLine;
            while((inputLine=br.readLine())!=null){
                sb.append(inputLine);
            }


            return sb.toString();

        } catch (Throwable t) {
            System.out.println(t.getMessage());
            t.printStackTrace();//TODO: Find a more elegant way to handle the error
        } finally{
			try {
				if(br!=null){
					br.close();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }

        return "";
    }
}
