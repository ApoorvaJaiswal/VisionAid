package com.example.apoorvajaiswal.visionaid;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

import java.util.Locale;

import static android.R.attr.onClick;

public class MainActivity extends Activity {

    /** Duration of wait **/
    private final int SPLASH_DISPLAY_LENGTH = 5000;
    String speech="Welcome";
    String speech1="Vision Aid";
    TextToSpeech textToSpeech;
    Intent checkIntent;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.activity_main);
        checkIntent = new Intent();
        checkIntent.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
        startActivityForResult(checkIntent,1);
        /* New Handler to start the Menu-Activity
         * and close this Splash-Screen after some seconds.*/
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                /* Create an Intent that will start the Menu-Activity. */
                Intent mainIntent = new Intent(MainActivity.this,Splash.class);
                MainActivity.this.startActivity(mainIntent);
               //MainActivity.this.finish();

            }
        }, SPLASH_DISPLAY_LENGTH);
    }
    protected void onActivityResult(
            int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if (resultCode == TextToSpeech.Engine.CHECK_VOICE_DATA_PASS) {
                // success, create the TTS instance
                textToSpeech = new TextToSpeech(MainActivity.this, new TextToSpeech.OnInitListener() {
                    @Override
                    public void onInit(int status) {
                        if(status != TextToSpeech.ERROR) {
                            textToSpeech.setLanguage(Locale.UK);
                            textToSpeech.speak(speech1+speech, TextToSpeech.QUEUE_FLUSH, null);
                           // textToSpeech.speak(speech, TextToSpeech.QUEUE_FLUSH, null);

                        }
                    }
                });
            } else {
                // missing data, install it
                Intent installIntent = new Intent();
                installIntent.setAction(
                        TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
                startActivity(installIntent);
            }
        }
    }
}