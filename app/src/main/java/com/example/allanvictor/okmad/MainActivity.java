package com.example.allanvictor.okmad;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        updateExcuseText();
    }

    private void updateExcuseText() {
        TextView excuse_text = findViewById(R.id.excuse_text);
        Excuse excuse = new Excuse(getResources().openRawResource(R.raw.excuses_list));
        excuse_text.setText(excuse.getRandom());
    }

    public void updateExcuseView(View view) {
        updateExcuseText();
    }

}
