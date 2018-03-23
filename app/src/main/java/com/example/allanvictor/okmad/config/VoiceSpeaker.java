package com.example.allanvictor.okmad.config;

import android.content.Context;
import android.speech.tts.TextToSpeech;

import java.util.Locale;

public class VoiceSpeaker implements TextToSpeech.OnInitListener {

    private static TextToSpeech engine;

    public VoiceSpeaker(Context context) {
        engine = new TextToSpeech(context, this);
    }

    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            engine.setLanguage(Locale.forLanguageTag("pt-BR"));
        }
    }

    public void speak(String message) {
        engine.speak(message, TextToSpeech.QUEUE_FLUSH, null, null);
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}