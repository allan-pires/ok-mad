package com.example.allanvictor.okmad;

import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import java.util.Locale;

public class MainActivity extends AppCompatActivity implements TextToSpeech.OnInitListener {

    private TextToSpeech engine;
    private Excuse excuse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        engine = new TextToSpeech(this,this);
        excuse = new Excuse(getResources().openRawResource(R.raw.excuses_list));
        setContentView(R.layout.activity_main);
        updateExcuse();
    }

    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            engine.setLanguage(Locale.forLanguageTag("pt-BR"));
        }
    }

    public void updateExcuseView(View view) {
        updateExcuse();
    }

    private void updateExcuse() {
        String current_excuse = excuse.getRandom();

        updateExcuseText(current_excuse);
        speakExcuse(current_excuse);
    }

    private void updateExcuseText(String message) {
        TextView excuse_text = findViewById(R.id.excuse_text);
        excuse_text.setText(message);
    }

    private void speakExcuse(String message) {
        engine.speak(message, TextToSpeech.QUEUE_FLUSH, null, null);
    }

}
