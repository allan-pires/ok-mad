package com.example.allanvictor.okmad;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.Locale;

import edu.cmu.pocketsphinx.Assets;
import edu.cmu.pocketsphinx.Hypothesis;
import edu.cmu.pocketsphinx.RecognitionListener;
import edu.cmu.pocketsphinx.SpeechRecognizer;
import edu.cmu.pocketsphinx.SpeechRecognizerSetup;

public class MainActivity extends AppCompatActivity implements RecognitionListener, TextToSpeech.OnInitListener {

    private static final String KEYPHRASENAME = "OKMAD";
    private static final String KEYPHRASE = "okay mad";

    private SpeechRecognizer recognizer;

    private TextToSpeech engine;
    private Excuse excuse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        engine = new TextToSpeech(this, this);
        excuse = new Excuse(getResources().openRawResource(R.raw.excuses_list));
        setContentView(R.layout.activity_main);

        ActivityCompat.requestPermissions(MainActivity.this,
                new String[]{Manifest.permission.RECORD_AUDIO},
                1);

    }

    private void runRecognizerSetup() {
        new AsyncTask<Void, Void, Exception>() {
            @Override
            protected Exception doInBackground(Void... params) {
                try {
                    Assets assets = new Assets(MainActivity.this);
                    File assetDir = assets.syncAssets();
                    setupRecognizer(assetDir);
                } catch (IOException e) {
                    return e;
                }
                return null;
            }

            @Override
            protected void onPostExecute(Exception result) {
                if (result != null) {
                    System.out.println(result.getMessage());
                } else {
                    recognizer.startListening(KEYPHRASENAME);
                }
            }
        }.execute();
    }

    private void setupRecognizer(File assetsDir) throws IOException {
        recognizer = SpeechRecognizerSetup.defaultSetup()
                .setAcousticModel(new File(assetsDir, "en-us-ptm"))
                .setDictionary(new File(assetsDir, "cmudict-en-us.dict"))
                .getRecognizer();
        recognizer.addListener(this);
        recognizer.addKeyphraseSearch(KEYPHRASENAME, KEYPHRASE);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (recognizer != null) {
            recognizer.cancel();
            recognizer.shutdown();
        }
    }


    @Override
    public void onBeginningOfSpeech() {

    }

    @Override
    public void onEndOfSpeech() {
    }

    @Override
    public void onPartialResult(Hypothesis hypothesis) {
        if (hypothesis == null)
            return;

        System.out.println("FALA:" + hypothesis.getHypstr());
        if (KEYPHRASE.equals(hypothesis.getHypstr()))
            recognizer.stop();
    }

    @Override
    public void onResult(Hypothesis hypothesis) {
        if (hypothesis != null) {
            updateExcuse();
            recognizer.startListening(KEYPHRASENAME);
        }
    }

    @Override
    public void onError(Exception error) {
        System.out.println("hello errorrrrrr " + error.getMessage());
    }

    @Override
    public void onTimeout() {
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    runRecognizerSetup();
                } else {
                    Toast.makeText(MainActivity.this, "Permission denied to record your audio", Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }


    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            engine.setLanguage(Locale.forLanguageTag("pt-BR"));
        }
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
