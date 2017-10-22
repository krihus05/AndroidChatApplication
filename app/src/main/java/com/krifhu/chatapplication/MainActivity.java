package com.krifhu.chatapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import java.net.MalformedURLException;
import java.util.List;
import java.net.URL;

import static android.os.AsyncTask.execute;

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



public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void checkLogin(View view){
        final EditText editUsername = (EditText) findViewById(R.id.username);
        final EditText editPassword = (EditText) findViewById(R.id.password);

        final String username = editUsername.getText().toString();
        final String password = editPassword.getText().toString();

        if(!username.equals("") && !password.equals("")){
            //System.out.println("It works! OMG");
            try {
                new LoginCheck(new LoginCheck.OnPostExecute() {
                    @Override
                    public void onPostExecute(List<Users> users) {
                        if(users.isEmpty()){
                            //System.out.println("No match in the database");
                        }
                        for(Users u : users) {
                            System.out.println("User: " + u.getUsername());
                            if(username.equals(u.getUsername())){
                                //System.out.println("We have a match in the database");
                                Intent intent = new Intent(MainActivity.this, UsersActivity.class);
                                intent.putExtra("username", u.getUsername());

                                editPassword.setText("");
                                editUsername.setText("");

                                startActivity(intent);
                            }
                        }
                    }
                }).execute(new URL("http://192.168.1.43:8080/ChatApplicationGit/api/users/getUser?username=" + username)); //(new url.("http://158.38.92.103:8080/pstore/api/store/images/"));
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }
    }

    public void checkRegister(View view){
    }
}
