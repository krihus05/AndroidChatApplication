package com.krifhu.chatapplication;

import android.os.AsyncTask;
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

public class GetUsers extends AsyncTask<URL,Integer,List<Users>> {
    public interface OnPostExecute {
        void onPostExecute(List<Users> users);
    }

    OnPostExecute callback;

    public GetUsers(OnPostExecute callback) {
        this.callback = callback;
    }

    @Override
    protected List<Users> doInBackground(URL... urls) {
        if(urls.length < 1) return Collections.EMPTY_LIST;


        //SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        List<Users> result = new ArrayList<>();

        HttpURLConnection con = null;
        try {
            con = (HttpURLConnection)urls[0].openConnection();
            JsonReader jr = new JsonReader(new InputStreamReader(con.getInputStream()));
            jr.beginArray();
            while (jr.hasNext()) {
                String username = null;
                long size = -1;
                Date date = null;

                jr.beginObject();
                while (jr.hasNext()) {
                    switch (jr.nextName()) {
                        case "username":
                            username = jr.nextString();
                            break;
                        default:
                            jr.skipValue();
                    }
                }
                jr.endObject();
                result.add(new Users(username));
            }
            jr.endArray();
        } catch (IOException e) {
            Log.e("LoadThumb","Failed to load photos from " + urls[0],e);
        }


        return result;
    }

    @Override
    protected void onPostExecute(List<Users> users) {
        if(callback != null)
            callback.onPostExecute(users);
    }
}