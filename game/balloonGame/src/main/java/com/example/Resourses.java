package com.example;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;

/**
 * Created by yuanwenjuan on 2017/1/22.
 */

public class Resourses {

    private static Sound sound;
    public static void load(){
        sound = Gdx.audio.newSound(Gdx.files.internal("audio/shoot.mp3"));
    }

    public static void shootMusic(){
        if(sound != null) {
            sound.play();
        }
    }

    public static void release(){
        if(sound != null){
            sound.dispose();
        }
    }

}
