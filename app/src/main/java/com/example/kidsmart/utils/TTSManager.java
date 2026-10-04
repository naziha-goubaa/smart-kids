package com.example.kidsmart.utils;

import android.content.Context;
import android.speech.tts.TextToSpeech;

import java.util.Locale;

public class TTSManager {
    private TextToSpeech tts;

    public TTSManager(Context ctx) {
        tts = new TextToSpeech(ctx, status -> {
            if (status == TextToSpeech.SUCCESS) {
                tts.setLanguage(Locale.FRANCE);
                tts.setSpeechRate(0.95f);
            }
        });
    }

    public void speak(String text) {
        if (tts!=null) tts.speak(text, TextToSpeech.QUEUE_ADD, null, "UTT");
    }


    public void shutdown(){
        if (tts!=null){ tts.shutdown(); tts = null; }
    }
}
