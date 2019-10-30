package com.example.a7th_assignment;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {
    final String url = "https://loremflickr.com/json/g/320/240/";
    // Layouts
    LinearLayout mainLayout;
    LinearLayout toolbar;
    // Views
    Button buttonFind;
    TextView editTextSearch;
    ListView listView;
    // List
    ArrayList<CustomListItem> list;
    CustomAdapter customAdapter;
    Toast loading;
    // Request HTTP
    private RequestQueue requestQueue;
    // Internet connection status
    private ConnectivityManager connMan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loading = Toast.makeText(getApplicationContext(), "Loading..", Toast.LENGTH_SHORT);

        // Layouts
        mainLayout = findViewById(R.id.mainLayout);
        toolbar = findViewById(R.id.toolbar);

        // Views
        listView = findViewById(R.id.listView);
        editTextSearch = new EditText(this);
        editTextSearch.setLayoutParams(new LinearLayout.LayoutParams(0,
                LinearLayout.LayoutParams.MATCH_PARENT, 80));
        buttonFind = new Button(this);
        buttonFind.setText(getString(R.string.buttonFind));
        buttonFind.setLayoutParams(new LinearLayout.LayoutParams(0,
                LinearLayout.LayoutParams.MATCH_PARENT, 20));

        // Add button and editText to toolbar
        toolbar.addView(editTextSearch);
        toolbar.addView(buttonFind);

        // Button functionality
        buttonFind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addRequestToQueue(jsonRequestBuilder(editTextSearch.getText(), url));
            }
        });

        // Setup ListView
        list = new ArrayList<>();
        customAdapter = new CustomAdapter(this, list);
        listView.setAdapter(customAdapter);

        // Get connection manager for internet connection status check.
        connMan = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);


        //// Request
        /*
         * API data example
         * JSON
         * {
         *  "file":"https:\/\/loremflickr.com\/cache\/resized\/65535_48691638487_3584a7fca6_320_240_g.jpg",
         *  "license":"cc-nc-sa",
         *  "owner":"Carbon Arc",
         *  "width":320,"height":240,
         *  "filter":"g",
         *  "tags":"cat",
         *  "tagMode":"all"
         * }
         * */
        requestQueue = Volley.newRequestQueue(this);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private JsonObjectRequest jsonRequestBuilder(CharSequence searchWord, String baseUrl) {
        // Exits the program IF search is empty
        if (searchWord.length() < 1) {
            this.finish();
        }
        String requestUrl = baseUrl + searchWord + "/all";
        clearSearch();
        loading.show();

        return new JsonObjectRequest
                (Request.Method.GET, requestUrl, null, new Response.Listener<JSONObject>() {
                    String imgUrl, owner, license;

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            imgUrl = response.getString("file");
                            owner = response.getString("owner");
                            license = response.getString("license");
                            addRequestToQueue(imageRequestBuilder(imgUrl, owner, license));

                        } catch (JSONException err) {
                            err.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), "Query failed...", Toast.LENGTH_LONG).show();
                    }
                });


    }

    private ImageRequest imageRequestBuilder(final String url, final String owner, final String license) {

        return new ImageRequest
                (url, new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap bitmap) {
                        list.add(new CustomListItem(owner, license, bitmap));
                        customAdapter.notifyDataSetChanged();
                        loading.cancel();
                    }
                }, 320, 240, null, Bitmap.Config.RGB_565, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), "Query failed...", Toast.LENGTH_LONG).show();
                    }
                });


    }

    private void addRequestToQueue(Request request) {
        // IF there is no internet connection. Prompt user and don't add query to queue.
        if (!this.checkInternetConnection()) {
            Toast.makeText(getApplicationContext(), "Internet connection not available!", Toast.LENGTH_LONG).show();
            return;
        }
        requestQueue.add(request);
    }

    private void clearSearch() {
        editTextSearch.setText("");
        editTextSearch.clearFocus();
        hideKeyboard();
    }

    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        try {
            imm.hideSoftInputFromWindow(mainLayout.getWindowToken(), 0);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }


    private boolean checkInternetConnection() {
        // Return true IF there is a active network with internet connection.
        NetworkInfo network = connMan.getActiveNetworkInfo();
        return network != null && network.isConnectedOrConnecting();
    }
}
