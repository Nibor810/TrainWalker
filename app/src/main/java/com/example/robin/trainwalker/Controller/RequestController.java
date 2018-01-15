package com.example.robin.trainwalker.Controller;

import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Arthur on 10-1-2018.
 */

public class RequestController extends AsyncTask<String, Void, String> {

    private AsyncResponse delegate = null;
    private String username;
    private String password;
    private String urlString;
    private boolean authentication;

    public RequestController(String username, String password, String urlString, AsyncResponse delegate) {

        this.username = username;
        this.password = password;
        this.urlString = urlString;
        this.delegate = delegate;
        authentication = true;
    }

    public RequestController(String urlString, AsyncResponse delegate) {

        this.urlString = urlString;
        this.delegate = delegate;
        authentication = false;
    }

    @Override
    protected String doInBackground(String... params) {

        HttpURLConnection connection = null;
        BufferedReader reader = null;

        String result;

        try {

            if(authentication == true) {

                final String userPassString = username + ":" + password;
                final String basicAuth = "Basic " + Base64.encodeToString(userPassString.getBytes(), Base64.NO_WRAP);

                URL url = new URL(urlString);
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setRequestProperty("Authorization", basicAuth);
                connection.setUseCaches(false);
                connection.connect();
            }
            else {

                URL url = new URL(urlString);
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setUseCaches(false);
                connection.connect();
            }

            Log.d("RESPONSE", "Response code: " + connection.getResponseCode());
            Log.d("RESPONSE", "Response message: " + connection.getResponseMessage());
            Log.d("RESPONSE", "Request method: " + connection.getRequestMethod());

            InputStream inputStream = connection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {

                return null;
            }

            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {

                buffer.append(line + "\n");
            }

            if (buffer.length() == 0) {

                // Stream was empty.  No point in parsing.
                return null;
            }

            return buffer.toString();
        }
        catch (IOException e) {

            Log.e("PlaceholderFragment", "Error ", e);
            // If the code didn't successfully get the API data, there's no point in attemping to parse it.
            return null;
        }
        finally {

            //Reset
            if (connection != null) {

                connection.disconnect();
            }
            if (reader != null) {

                try {

                    reader.close();
                }
                catch (final IOException e) {

                    Log.e("PlaceholderFragment", "Error closing stream", e);
                }
            }
        }
    }

    @Override
    protected void onPostExecute(String resultString) {
        
        delegate.processFinished(resultString);
    }
}