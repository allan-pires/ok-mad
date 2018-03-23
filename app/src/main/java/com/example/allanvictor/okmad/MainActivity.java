package com.example.allanvictor.okmad;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.example.allanvictor.okmad.entities.Mad;
import com.example.allanvictor.okmad.config.Response;
import com.example.allanvictor.okmad.config.VoiceSpeaker;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import edu.cmu.pocketsphinx.Assets;
import edu.cmu.pocketsphinx.Hypothesis;
import edu.cmu.pocketsphinx.RecognitionListener;
import edu.cmu.pocketsphinx.SpeechRecognizer;
import edu.cmu.pocketsphinx.SpeechRecognizerSetup;

public class MainActivity extends AppCompatActivity implements RecognitionListener {

    private static boolean hasPermission = false;

    private SpeechRecognizer recognizer;
    private VoiceSpeaker voiceSpeaker;

    private Response response;
    private List<Response> responseList = new LinkedList<>();
    private Mad mad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        voiceSpeaker = new VoiceSpeaker(this);
        mad = new Mad(getResources().openRawResource(R.raw.excuses_list));
        responseList.add(mad);

        setContentView(R.layout.activity_main);

        ActivityCompat.requestPermissions(MainActivity.this,
                new String[]{Manifest.permission.RECORD_AUDIO},
                1);

    }

    @Override
    protected void onResume() {
        super.onResume();

        if (hasPermission)
            runRecognizerSetup();
        else {
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.RECORD_AUDIO},
                    1);
        }
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
                    for (Response r : responseList) {
                        recognizer.startListening(r.getSentenceName());

                    }
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

        for (Response r : responseList) {
            recognizer.addKeyphraseSearch(r.getSentenceName(), r.getSentence());
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (recognizer != null) {
            recognizer.stop();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        recognizer.cancel();
        recognizer.shutdown();
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

        for (Response r : responseList) {
            if (r.getSentence().equals(hypothesis.getHypstr())) {
                recognizer.stop();
                response = r;
            }
        }
    }

    @Override
    public void onResult(Hypothesis hypothesis) {
        if (hypothesis != null) {
            String textToSpeak = response.getRandomSentence();
            voiceSpeaker.speak(textToSpeak);
            recognizer.startListening(response.getSentenceName());
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
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    hasPermission = true;
                } else {
                    Toast.makeText(MainActivity.this, "Permission denied to record your audio", Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }


}
