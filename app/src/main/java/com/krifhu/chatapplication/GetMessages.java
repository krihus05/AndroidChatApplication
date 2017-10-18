package com.krifhu.chatapplication;

import android.os.AsyncTask;
import android.os.Message;
import android.util.JsonReader;
import android.util.Log;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class GetMessages extends AsyncTask<URL,Integer,List<Messages>> {
    public interface OnPostExecute {
        void onPostExecute(List<Messages> messages);
    }

    OnPostExecute callback;

    public GetMessages(OnPostExecute callback) {
        this.callback = callback;
    }

    @Override
    protected List<Messages> doInBackground(URL... urls) {
        if(urls.length < 1) return Collections.EMPTY_LIST;


        //SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        List<Messages> result = new ArrayList<>();

        HttpURLConnection con = null;
        try {
            con = (HttpURLConnection)urls[0].openConnection();
            JsonReader jr = new JsonReader(new InputStreamReader(con.getInputStream()));
            jr.beginArray();
            while (jr.hasNext()) {
                String messageBody = null;
                int messageID = 0;
                String sender = null;
                String receiver = null;
                //Timestamp version;
                jr.beginObject();
                while (jr.hasNext()) {
                    switch (jr.nextName()) {
                        case "messageBody":
                            messageBody = jr.nextString();
                            break;
                        case "messageID":
                            messageID = jr.nextInt();
                            break;
                        case "sender":
                            sender = jr.nextString();
                            break;
                        case "receiver":
                            receiver = jr.nextString();
                            break;
                        default:
                            jr.skipValue();
                    }
                }
                jr.endObject();
                result.add(new Messages(messageBody, messageID, sender, receiver));
            }
            jr.endArray();
        } catch (IOException e) {
            Log.e("LoadThumb","Failed to load photos from " + urls[0],e);
        }


        return result;
    }

    @Override
    protected void onPostExecute(List<Messages> messages) {
        if(callback != null)
            callback.onPostExecute(messages);
    }
}