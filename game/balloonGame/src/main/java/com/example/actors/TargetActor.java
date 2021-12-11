package com.example.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.example.Configure;
import com.example.GameScreen;

/**
 * Created by guo on 2017/1/10.
 */
public class TargetActor extends Actor {
    private Texture back;
    private Sprite target;
    private float mRotation;
    private GameScreen game;
    private float swidth, sheight, width, height;
    public TargetActor(GameScreen game) {
        this.game = game;
        back = new Texture(Gdx.files.internal("picture/" + this.initTexture(game.gatingColor, game.gatingWidth)));
        this.height = back.getHeight();
        this.width = this.back.getWidth();
        this.swidth = Configure.WORLD_WIDTH;
        this.sheight = Configure.WORLD_HEIGHT;
        this.target = new Sprite(back);
        this.target.setPosition((this.swidth - this.width) / 2, (this.sheight - this.height) / 2);
    }


    private String initTexture(int color, int width){
        switch (color){
            case Configure.BLACK:
                if(width == Configure.GATING_WIDTH_FINE)
                    return "bg_black3.jpg";
                if(width == Configure.GATING_WIDTH_WIDE)
                    return "bg_black1.jpg";
                return "bg_black2.jpg";
            case Configure.GREEN:
                if(width == Configure.GATING_WIDTH_FINE)
                    return "bg_green3.jpg";
                if(width == Configure.GATING_WIDTH_WIDE)
                    return "bg_green1.jpg";
                return "bg_green2.jpg";
            case Configure.RED:
                if(width == Configure.GATING_WIDTH_FINE)
                    return "bg_red3.jpg";
                if(width == Configure.GATING_WIDTH_WIDE)
                    return "bg_red1.jpg";
                return "bg_red2.jpg";
        }
        return "bg_red1.jpg";
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        mRotation += game.gatingSpeed * Gdx.graphics.getDeltaTime();
        this.target.setRotation(mRotation);
        this.target.draw(batch);
    }

}
