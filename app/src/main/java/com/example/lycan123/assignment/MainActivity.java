package com.example.lycan123.assignment;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.UnknownHostException;

public class MainActivity extends AppCompatActivity {

    RunTimePermissions runTimePermissions;

    TextView httpResponse;
    EditText searchBar;
    Button searchBtn;
    Button RenderBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        runTimePermissions = new RunTimePermissions(getApplicationContext(), this);

        // Checking for Internet Access
        //runTimePermissions.checkInternetAccess();

        // Getting the References for the Views MainActivity Layout
        httpResponse = findViewById(R.id.httpResponse);
        searchBar = findViewById( R.id.searchBar);
        searchBtn = findViewById(R.id.searchBtn);
        RenderBtn = findViewById(R.id.Render);

        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String response = searchBar.getText().toString();
                new ReadHTTPResponse().execute(response);
            }
        });

       RenderBtn.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               String response = searchBar.getText().toString();
               Intent myIntent = new Intent(v.getContext(),WebViewActivity.class);
               myIntent.putExtra("url",response);
               startActivity(myIntent);
           }
       });

    }

    private class ReadHTTPResponse extends AsyncTask<String, Void, String> {

        int errorFlag = 0;

        @Override
        protected String doInBackground(String...url) {
            return readFromURL(url[0]);
        }

        @Override
        protected void onPostExecute(String s) {

            if (errorFlag == 1) {
                Toast.makeText(getApplicationContext(), "Invalid URL", Toast.LENGTH_LONG).show();
                httpResponse.setText("Invalid URL");
            } else if (errorFlag == 2) {
                Toast.makeText(getApplicationContext(), "IOException Occurred", Toast.LENGTH_LONG).show();
                httpResponse.setText("IO Exception");
            } else if (errorFlag == 3) {
                Toast.makeText(getApplicationContext(), "Unknown Exception Occurred", Toast.LENGTH_LONG).show();
                httpResponse.setText("Unknown Exception");
            } else {
                Toast.makeText(getApplicationContext(), "Done Loading web page", Toast.LENGTH_LONG).show();
                httpResponse.setText(s);
            }
        }

        public String readFromURL(String url) {

            StringBuilder stringBuilder = new StringBuilder();

            try {
                URL Url = new URL(url);
                URLConnection urlConnection = Url.openConnection();
                BufferedReader in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                String line;
                while ((line = in.readLine()) != null) {
                    stringBuilder.append(line);
                }

                if (stringBuilder.length() == 0) {
                    return null;
                }

                in.close();
            } catch (MalformedURLException e) {
                e.printStackTrace();
                errorFlag = 1;
            } catch (UnknownHostException e) {
                e.printStackTrace();
                errorFlag = 1;
            } catch (IOException e) {
                e.printStackTrace();
                errorFlag = 2;
            } catch (Exception e) {
                e.printStackTrace();
                errorFlag = 3;
            }

            return stringBuilder.toString();
        }

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        runTimePermissions.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}

