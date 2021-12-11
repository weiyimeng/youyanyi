package com.example.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.example.Configure;
import com.example.GameScreen;

/**
 * Created by guo on 2017/1/10.
 */
public class BalloonActor extends Actor {
    Texture balloon;
    Sprite ball;
    private Rectangle region;
    GameScreen game;
    private boolean isBoom = false;
    private long showBoom = 0L;
    public BalloonActor(float x, GameScreen f){
        balloon = new Texture(Gdx.files.internal("picture/q1_2.png"));
        this.ball = new Sprite(balloon);
        this.ball.setPosition(x, -10);
        this.game = f;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        if(this.ball.getY() < Configure.WORLD_HEIGHT) {
            if(isBoom){
                if(System.currentTimeMillis() - this.showBoom > 200){
                    this.remove();
                }
            }else {
                this.ball.translateY(Configure.BALLOON_SPEED * Gdx.graphics.getDeltaTime());
            }
        }else{
            this.balloon.dispose();
            this.game.getBalloons().remove(this);
            this.remove();
        }
        this.ball.draw(batch);
    }

    public void boom(){
        this.balloon.dispose();
        this.ball.setTexture(new Texture(Gdx.files.internal("picture/boom1_2.png")));
        this.isBoom = true;
        this.showBoom = System.currentTimeMillis();
    }

    public Rectangle getRectangle(){
        return this.ball.getBoundingRectangle();
    }
}
