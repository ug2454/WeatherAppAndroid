package com.example.weatherapp;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {


    TextView textView1;
    EditText editText;
    Button button;
    String main="";
    String description="";

    public void checkWeather(View view) throws ExecutionException, InterruptedException {




        GetWeatherJSON json = new GetWeatherJSON();
        json.execute("https://api.openweathermap.org/data/2.5/weather?q="+editText.getText()+"&appid=YOURAPIKEY&units=metric").get();





    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView1=findViewById(R.id.textView2);
        editText=findViewById(R.id.editTextTextPersonName);


    }
    class GetWeatherJSON extends AsyncTask<String,Void,String>{

        @Override
        protected String doInBackground(String... urls) {
            String result="";

            try {
                URL url=new URL(urls[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = connection.getInputStream();
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                int data = inputStreamReader.read();

                while(data!=-1){
                    char current  = (char) data;
                    result+=current;
                    data=inputStreamReader.read();
                }
                return result;
            } catch (MalformedURLException e) {
                e.printStackTrace();
                return null;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            try {
                JSONObject json = new JSONObject(s);
                if(json!=null) {
                    String weatherInfo = json.getString("weather");
                    JSONArray weatherArray = new JSONArray(weatherInfo);

                    for (int i = 0; i < weatherArray.length(); i++) {
                        JSONObject jsonObject = weatherArray.getJSONObject(i);

                        main = jsonObject.getString("main");
                        description = jsonObject.getString("description");
                        System.out.println(main + "" + description);
                    }

                    textView1.setText(main + " : " + description);
                }
                else{
                    textView1.setText("Please select a valid city");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
