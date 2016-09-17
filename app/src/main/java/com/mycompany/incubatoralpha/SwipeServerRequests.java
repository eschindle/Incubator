package com.mycompany.incubatoralpha;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

public class SwipeServerRequests {
    ProgressDialog progressDialog;
    public static final String SERVER_ADDRESS = "http://cgi.soic.indiana.edu/~team41/";

    public SwipeServerRequests(SwipeFlingAdapterView.onFlingListener context) {

    }

    private String getEncodedData(Map<String, String> data) {
        StringBuilder sb = new StringBuilder();
        for (String key : data.keySet()) {
            String value = null;
            try {
                value = URLEncoder.encode(data.get(key), "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            if (sb.length() > 0)
                sb.append("&");

            sb.append(key).append("=").append(value);
        }
        return sb.toString();
    }

    public void storeLikeInBackground(Idea idea, GetIdeaCallback ideaCallback) {
        new StoreLikeAsyncTask(idea, ideaCallback).execute();
    }

    public class StoreLikeAsyncTask extends AsyncTask<Void, Void, Void> {

        Idea idea;
        GetIdeaCallback ideaCallback;

        public StoreLikeAsyncTask(Idea idea, GetIdeaCallback ideaCallback) {
            this.idea = idea;
            this.ideaCallback = ideaCallback;
        }

        @Override
        protected Void doInBackground(Void... params) {
            Map<String, String> dataToSend = new HashMap<>();
            dataToSend.put("uid",idea.uid+"");
            dataToSend.put("iid", idea.iid+"");
            dataToSend.put("like", idea.like+"");
            dataToSend.put("ouid", idea.ouid+"");


            String encodedStr = getEncodedData(dataToSend);
            BufferedReader reader = null;

            try {
                URL url = new URL(SERVER_ADDRESS + "swipe.php");

                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setReadTimeout(15000);
                con.setConnectTimeout(15000);
                con.setRequestMethod("POST");
                con.setDoOutput(true);

                OutputStream os = con.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                writer.write(encodedStr);
                writer.flush();
                writer.close();
                os.close();

                StringBuilder sb = new StringBuilder();
                reader = new BufferedReader(new InputStreamReader(con.getInputStream()));

                String line;
                while ((line = reader.readLine()) != null) {
                    sb.append(line).append("\n");
                }
                line = sb.toString();

                Log.i("custom_check", "The values received in the store part are as follows:");
                Log.i("custom_check", line);

                con.disconnect();

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            ideaCallback.done(null);
            super.onPostExecute(aVoid);
        }
    }

}
