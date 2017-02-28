package com.example.apoorvajaiswal.visionaid;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.view.Menu;

/**
 * Created by Apoorva Jaiswal on 2/25/2017.
 */

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;

import java.util.Locale;

public class Splash3 extends Activity {

    /** Duration of wait **/
    private final int SPLASH_DISPLAY_LENGTH = 3000;
    String speech="Top";
    String s="Right",s1="Bottom",s2="Left";
    TextToSpeech textToSpeech;
    Intent checkIntent;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.splash3);
        checkIntent = new Intent();
        checkIntent.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
        startActivityForResult(checkIntent,1);
        /* New Handler to start the Menu-Activity
         * and close this Splash-Screen after some seconds.*/
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                /* Create an Intent that will start the Menu-Activity. */
                Intent mainIntent = new Intent(Splash3.this,Next.class);
                Splash3.this.startActivity(mainIntent);
                Splash3.this.finish();

            }
        }, SPLASH_DISPLAY_LENGTH);
    }
    protected void onActivityResult(
            int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if (resultCode == TextToSpeech.Engine.CHECK_VOICE_DATA_PASS) {
                // success, create the TTS instance
                textToSpeech = new TextToSpeech(Splash3.this, new TextToSpeech.OnInitListener() {
                    @Override
                    public void onInit(int status) {
                        if(status != TextToSpeech.ERROR) {
                            textToSpeech.setLanguage(Locale.UK);
                            textToSpeech.speak(speech+s+s1+s2, TextToSpeech.QUEUE_FLUSH, null);
                           // textToSpeech.speak(s, TextToSpeech.QUEUE_FLUSH, null);
                            //textToSpeech.speak(s1, TextToSpeech.QUEUE_FLUSH, null);
                            //textToSpeech.speak(s2, TextToSpeech.QUEUE_FLUSH, null);

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
