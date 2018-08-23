package edu.rpi.legup.user;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class UsageStatistics {

    private static final String url = "https://legup-3b4a5.firebaseio.com/databases/test.json";

    public UsageStatistics() {

    }

    public boolean sendErrorReport() {
        try {
            HttpClient httpclient = new DefaultHttpClient();

            HttpPost httppost = new HttpPost(url);

            // Request parameters and other properties.
            httppost.setEntity(new StringEntity("{\"test\": \"jeff\"}"));
//            List<NameValuePair> params = new ArrayList<>(2);
//            params.add(new BasicNameValuePair("param-1", "12345"));
//            params.add(new BasicNameValuePair("param-2", "Hello!"));
//            httppost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));

            //Execute and get the response.
            HttpResponse response = httpclient.execute(httppost);
            HttpEntity entity = response.getEntity();

            if (entity != null) {
                InputStream instream = entity.getContent();

                try {
                    System.err.println(new String(instream.readAllBytes()));
                } finally {
                    instream.close();
                }
            }
        } catch(IOException e) {
            return false;
        }
        return false;
    }

    public static void main(String[] args) {
        new UsageStatistics().sendErrorReport();
    }
}
