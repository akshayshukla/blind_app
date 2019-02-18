package com.major.project.drishti;

/**
 * Created by akshay on 15/3/18.
 **/

import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

public class WebRequest {

    static String response = null;
    public final static int GETRequest = 1;
    public final static int POSTRequest = 2;
    String complete_url = "https://maps.googleapis.com/maps/api/directions/json?";

    public WebRequest() {
    }

    public String makeWebServiceCall(String urladdress, int requestmethod) {
        return this.makeWebServiceCall(urladdress, requestmethod, null);
    }

    public String makeWebServiceCall(String urladdress, int requestmethod,
                                     HashMap<String, String> params) {
        URL url;
        complete_url = complete_url+urladdress;
        String response = "";
        try {
            url = new URL(complete_url);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            //conn.setReadTimeout(15001);
            //conn.setConnectTimeout(15001);
            //conn.setDoInput(true);
            //conn.setDoOutput(true);
            if (requestmethod == POSTRequest) {
                conn.setRequestMethod("POST");
            } else if (requestmethod == GETRequest) {
                conn.setRequestMethod("GET");
            }

            if (params != null) {
                OutputStream ostream = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(ostream, "UTF-8"));
                StringBuilder requestresult = new StringBuilder();
                boolean first = true;
                for (Map.Entry<String, String> entry : params.entrySet()) {
                    if (first)
                        first = false;
                    else
                        requestresult.append("&");
                    requestresult.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
                    requestresult.append("=");
                    requestresult.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
                }
                writer.write(requestresult.toString());

                writer.flush();
                writer.close();
                ostream.close();
            }
            int reqresponseCode = conn.getResponseCode();

            if (reqresponseCode == HttpsURLConnection.HTTP_OK) {
                String line;
                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                while ((line = br.readLine()) != null) {
                    response += line;
                }
            } else {
                response = "";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }
}
