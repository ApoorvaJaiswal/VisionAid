package com.example.apoorvajaiswal.visionaid;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.Locale;

/**
 * Created by Apoorva Jaiswal on 2/25/2017.
 */

public class Next extends Activity {
    Button b;
    String speech="Make a gesture and click OK";
    TextToSpeech textToSpeech;
    Intent checkIntent;
    Intent ser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.next);
        b=(Button)findViewById(R.id.button);
        checkIntent = new Intent();
        checkIntent.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
        startActivityForResult(checkIntent,1);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(Next.this,"Nice gesture!",Toast.LENGTH_SHORT).show();
                startActivityForResult(checkIntent,2);
            }
        });
        ser= new Intent(Next.this,Blue.class);
        //Log.e("hi","works in Onbind");
        startService(ser);

    }
    protected void onActivityResult(
            int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if (resultCode == TextToSpeech.Engine.CHECK_VOICE_DATA_PASS) {
                // success, create the TTS instance
                textToSpeech = new TextToSpeech(Next.this, new TextToSpeech.OnInitListener() {
                    @Override
                    public void onInit(int status) {
                        if (status != TextToSpeech.ERROR) {
                            textToSpeech.setLanguage(Locale.UK);
                            textToSpeech.speak(speech, TextToSpeech.QUEUE_FLUSH, null);

                        }
                    }
                });
            } else if (requestCode == 2) {
                if (resultCode == TextToSpeech.Engine.CHECK_VOICE_DATA_PASS) {
                    // success, create the TTS instance
                    textToSpeech = new TextToSpeech(Next.this, new TextToSpeech.OnInitListener() {
                        @Override
                        public void onInit(int status) {
                            if (status != TextToSpeech.ERROR) {
                                textToSpeech.setLanguage(Locale.UK);
                                textToSpeech.speak("Nice gesture", TextToSpeech.QUEUE_FLUSH, null);

                            }
                        }
                    });

                }
            }else {
                    // missing data, install it
                    Intent installIntent = new Intent();
                    installIntent.setAction(
                            TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
                    startActivity(installIntent);
                }
            }
        }
    }
