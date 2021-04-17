package com.example.elliestockapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProfilePage extends AppCompatActivity {
    private List<String> favList;
    private String[] sAbb;
    private String[] sName;
    private String[] sPrice;
    private String[] sChange;
    public static String API_Key = "0dafdcf8a7msh77231e3339cca48p1b86bejsnc51903bec2d7";
    private RequestQueue queue;
    private int numrequests;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profilepage);
        getSupportActionBar().hide();

        queue = Volley.newRequestQueue(this);

        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Intent intent;
                switch (item.getItemId()) {
                    case R.id.action_search:
                        intent = new Intent(getApplicationContext(), SearchPage.class);
                        //intent.putExtra("isLoc", true);
                        startActivity(intent);
                        break;
                    case R.id.action_settings:
                        intent = new Intent(getApplicationContext(), LogOutPage.class);
                        //intent.putExtra(EXTRA_MESSAGE, coordMessage);
                        startActivity(intent);
                        break;
                }
                return true;
            }
        });

        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference uidRef = FirebaseDatabase.getInstance().getReference().child("users").child(uid);
        uidRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                UserClass user = snapshot.getValue(UserClass.class);
                favList = user.favList;
                UpdateUI();
            }

            @Override
            public void onCancelled(DatabaseError error) {}
        });

    }

    private void UpdateUI() {
        String uname = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
        TextView profileTitle = findViewById(R.id.profileTitle);
        profileTitle.setText(uname + "'s Favorites List:");

        getStockAPIData();
    }

    private void getStockAPIData() {
        int s = favList.size();
        sAbb = new String[s];
        sName = new String[s];
        sPrice = new String[s];
        sChange = new String[s];
        numrequests = 4*s;

        for (int i = 0; i < favList.size(); i++) {
            String url = "https://alpha-vantage.p.rapidapi.com/query?function=GLOBAL_QUOTE&symbol="+favList.get(i)+"&outputsize=compact&datatype=json";
            int it = i;

            JsonObjectRequest gq = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        JSONObject quote = response.getJSONObject("Global Quote");

                        sAbb[it] = quote.getString("01. symbol");
                        sPrice[it] = quote.getString("05. price");
                        sChange[it] = quote.getString("09. change");

                        numrequests--;
                        if (numrequests==0) runAdaptor();
                    } catch (JSONException e) {
                        Log.i("test", "JSON Explosion "+it);
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.i("test", "Error With Volley Request");
                }
            }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String, String> headers = new HashMap<String, String>();
                    headers.put("x-rapidapi-key", API_Key);
                    headers.put("x-rapidapi-host", "alpha-vantage.p.rapidapi.com");
                    return headers;
                }
            };

            url = "https://alpha-vantage.p.rapidapi.com/query?keywords="+favList.get(i)+"&function=SYMBOL_SEARCH&datatype=json";

            JsonObjectRequest srch = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        JSONArray bestMatches = response.getJSONArray("bestMatches");
                        JSONObject bm0 = bestMatches.getJSONObject(0);

                        sName[it] = bm0.getString("2. name");

                        numrequests--;
                        if (numrequests==0) runAdaptor();
                    } catch (JSONException e) {
                        Log.i("test", "JSON Explosion "+it);
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.i("test", "Error With Volley Request");
                }
            }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String, String> headers = new HashMap<String, String>();
                    headers.put("x-rapidapi-key", API_Key);
                    headers.put("x-rapidapi-host", "alpha-vantage.p.rapidapi.com");
                    return headers;
                }
            };
            queue.add(gq);
            queue.add(srch);
        }
    }

    private void runAdaptor() {
        ListView historyList = findViewById(R.id.fav_list);
        StockListAdapter SLAdapter = new StockListAdapter(getApplicationContext(), sAbb, sName, sPrice, sChange);
        historyList.setAdapter(SLAdapter);
    }
}