package com.krifhu.chatapplication;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConversationActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private String user1 = null;
    private String user2 = null;

    private boolean newMessages = true;
    private int lastMessageID = 0;


    private boolean stop = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversation);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /*
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        user1 = bundle.getString("username");
        user2 = bundle.getString("convPartner");
        //System.out.println("User1: " + user1 + " User2: " + user2);

        recyclerView = (RecyclerView) findViewById(R.id.conversation_recycler_view);
        // use this setting to
        // improve performance if you know that changes
        // in content do not change the layout size
        // of the RecyclerView
        recyclerView.setHasFixedSize(true);
        // use a linear layout manager
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        final List<Messages> input = new ArrayList<>();
        //for (int i = 0; i < 7; i++) {
        //    input.add("Test" + i);
        //}// define an adapter
        //mAdapter = new MyAdapter(input);
        //recyclerView.setAdapter(mAdapter);



        final Handler handler = new Handler();
        final int delay = 2500; //milliseconds

        handler.postDelayed(new Runnable(){
            public void run(){
                if(!stop){
                    //System.out.println("Handler is executing");

                    try {
                        new GetMessages(new GetMessages.OnPostExecute() {
                            @Override
                            public void onPostExecute(List<Messages> messages) {
                                if(messages.isEmpty()){
                                    //System.out.println("No match in the database");
                                }
                                for(Messages m : messages) {
                                    //System.out.println(m.getMessageID() + ", " + m.getMessageBody() + ", " + m.getSender() + ", " + m.getReceiver());
                                    int currentID = m.getMessageID();
                                    if(currentID > lastMessageID){
                                        lastMessageID = currentID;
                                        //System.out.println("New Message In the system");
                                        input.add(m);
                                        newMessages = true;
                                    }
                                    if(currentID < lastMessageID){
                                        //System.out.println("The message is already in the system");
                                        newMessages = false;
                                    }


                                    //input.add(m);
                                }
                                if(newMessages){
                                    mAdapter = new MessageAdapter(user1, user2, input);
                                    recyclerView.setAdapter(mAdapter);
                                    recyclerView.scrollToPosition(input.size() - 1);
                                }

                            }
                        }).execute(new URL("http://158.38.193.201:8080/ChatApplication/api/conversations/getConversation?user1=" + user1 + "&user2=" + user2)); //(new url.("http://158.38.92.103:8080/pstore/api/store/images/"));
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    }

                    handler.postDelayed(this, delay);
                }

            }
        }, delay);


        /*
        try {
            new GetMessages(new GetMessages.OnPostExecute() {
                @Override
                public void onPostExecute(List<Messages> messages) {
                    if(messages.isEmpty()){
                        System.out.println("No match in the database");
                    }
                    for(Messages m : messages) {
                        //System.out.println(m.getMessageID() + ", " + m.getMessageBody() + ", " + m.getSender() + ", " + m.getReceiver());
                        input.add(m);
                    }
                    mAdapter = new MessageAdapter(user1, user2, input);
                    recyclerView.setAdapter(mAdapter);
                    recyclerView.scrollToPosition(input.size() - 1);
                }
            }).execute(new URL("http://192.168.1.43:8080/ChatApplicationGit/api/conversations/getConversation?user1=" + user1 + "&user2=" + user2)); //(new url.("http://158.38.92.103:8080/pstore/api/store/images/"));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }*/
        //System.out.println("mAdapter: " + (mAdapter.getItemCount() - 1));
        //recyclerView.scrollToPosition(mAdapter.getItemCount() - 1);





    }

    @Override
    public void onBackPressed() {
        System.out.println("Back has been pressed");
        stop = true;
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.conversation, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_account) {
            // Handle the camera action
        } else if (id == R.id.nav_settings) {

        } else if (id == R.id.nav_logout) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void sendMessage(View view){
        EditText massageToSend = (EditText) findViewById(R.id.messageToSend);
        final String message = massageToSend.getText().toString();
        //System.out.println("Message: " + message);

        RequestQueue queue = Volley.newRequestQueue(this);
        String url ="http://158.38.193.201:8080/ChatApplication/api/conversations/add?sender=" + user1 + "&receiver=" + user2 + "&messageBody=" + message;
        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        //System.out.println("Response is: "+ response /*response.substring(0,500)*/);
                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("That didn't work!");
            }

        }){
            @Override
            public String getBodyContentType() {
                return "application/json";
            }
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> params = new HashMap<String,String>();
                params.put("Content-Type", " application/json");
                return params;
            }
        };
        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }
}
