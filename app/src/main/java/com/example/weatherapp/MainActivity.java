package com.example.weatherapp;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class MainActivity extends AppCompatActivity {


    TextView textView1;
    EditText editText;
    Button button;
    String main = "";
    String description = "";

    public void checkWeather(View view) {


        GetWeatherJSON json = new GetWeatherJSON();
        try {
            String encodedString = URLEncoder.encode(editText.getText().toString(), "UTF-8");
            json.execute("https://api.openweathermap.org/data/2.5/weather?q=" + encodedString + "&appid=a9f0de43f7ae80b8312ceb33a386b6b3&units=metric").get();

            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(editText.getWindowToken(), 0);
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Could not find weather >.<", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView1 = findViewById(R.id.textView2);
        editText = findViewById(R.id.editTextTextPersonName);
        ImageView imageView = findViewById(R.id.imageView);
        textView1.setVisibility(View.VISIBLE);


    }

    class GetWeatherJSON extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {
            String result = "";

            try {
                URL url = new URL(urls[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = connection.getInputStream();
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                int data = inputStreamReader.read();

                while (data != -1) {
                    char current = (char) data;
                    result += current;
                    data = inputStreamReader.read();
                }
                return result;
            } catch (Exception e) {
                e.printStackTrace();

                return null;
            }
        }


        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            try {
                JSONObject json = new JSONObject(s);

                String weatherInfo = json.getString("weather");
                JSONArray weatherArray = new JSONArray(weatherInfo);

                for (int i = 0; i < weatherArray.length(); i++) {
                    JSONObject jsonObject = weatherArray.getJSONObject(i);

                    main = jsonObject.getString("main");
                    description = jsonObject.getString("description");
                    System.out.println(main + "" + description);
                }
                if (main.isEmpty() && description.isEmpty()) {
                    textView1.setText("Please enter a valid city");
                } else {
                    textView1.setText(main + " : " + description);
                }


            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), "Could not find weather >.<", Toast.LENGTH_SHORT).show();
                e.printStackTrace();

            }
        }
    }
}
