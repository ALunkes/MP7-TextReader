package com.example.anna.apptextreader;

import android.content.Intent;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.speech.tts.TextToSpeech.OnInitListener;
import java.util.Locale;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONObject;

public class TextReader extends AppCompatActivity implements OnInitListener {
    private TextToSpeech myTTS;
    private EditText inputedText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_reader);

        Intent checkTTSIntent = new Intent();
        checkTTSIntent.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
        startActivityForResult(checkTTSIntent, 0);

        final Button read = findViewById(R.id.readText);
        read.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                Log.d("fuck", "Read button clicked");
                inputedText = (EditText)findViewById(R.id.addText);
                String stringTextInput = inputedText.getText().toString();
                Toast.makeText(getApplicationContext(),inputedText.getText() , Toast.LENGTH_LONG).show();
                startRead(stringTextInput);
            }
        });
        final Button pause = findViewById(R.id.pause);
        pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                Log.d("fuck", "Stop button clicked");
                stopReading();
            }
        });
            final Button quote = findViewById(R.id.quote);
            quote.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    Log.d("fuck", "Stop button clicked");
                    new GetQuote().execute();
                    try {
                    Thread.sleep(1000);
                    } catch (Exception e) {
                        Log.d("fuck", "wtf");
                    }
                    Log.d("fuck", GetQuote.quote);
                    startRead(parseJson(GetQuote.quote));
                }
            });
    }
    public void onInit(int initStatus) {
        if (initStatus == TextToSpeech.SUCCESS) {
            myTTS.setLanguage(Locale.US);
        }
    }
    private void startRead(String textToRead) {
        //EditText enteredText
       // String textToRead = inputedText.getText().toString();
        Toast.makeText(getApplicationContext(), "Got here",
                Toast.LENGTH_LONG).show();
        myTTS.speak(textToRead, TextToSpeech.QUEUE_FLUSH, null);
    }
    private void stopReading() {
        myTTS.stop();
    }
    @Override
    public void onActivityResult(final int requestCode, final int resultCode,
                                 final Intent data) {
        if (requestCode == 0) {
            if (resultCode == TextToSpeech.Engine.CHECK_VOICE_DATA_PASS) {
                myTTS = new TextToSpeech(this, this);
            } else {
                Intent installTTSIntent = new Intent();
                installTTSIntent.setAction(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
                startActivity(installTTSIntent);
            }
        }
    }
    public String parseJson(String json) {
        JsonParser parser = new JsonParser();
        JsonObject result = parser.parse(json).getAsJsonObject();
        JsonObject contents = result.get("contents").getAsJsonObject();
        JsonArray quotes = contents.get("quotes").getAsJsonArray();
        JsonObject firstIndex = quotes.get(0).getAsJsonObject();
        String quote = firstIndex.get("quote").getAsString();
        return quote;
    }
}
