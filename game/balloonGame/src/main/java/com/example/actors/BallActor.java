package com.example.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.example.Configure;
import com.example.GameScreen;
import com.example.Resourses;

/**
 * Created by guo on 2017/1/9.
 */
public class BallActor extends Actor {
    private Texture texture;
    private float x, y;
    private float height, width;
    private Sprite pao;
    private  GameScreen game;
    long sid;
    boolean isShoot;
    public BallActor(float x, float width, float height, GameScreen f) {
        texture = new Texture(Gdx.files.internal("picture/dan_3.png"));
        this.height = texture.getHeight();
        this.width = this.texture.getWidth();
        this.pao = new Sprite(texture);
        this.pao.setPosition(x + width / 2 - 10, height - 10);
        this.game = f;
        this.isShoot = false;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {

        if(!isShoot){
            Resourses.shootMusic();
            System.out.println("bofang:::: + " + sid);
            this.isShoot = true;
        }
        if(this.pao.getY() <= Gdx.graphics.getHeight()){
            synchronized (game.getBalloons()){
                for(BalloonActor b : game.getBalloons()){
                    if(this.getRectangle().overlaps(b.getRectangle())){
                        game.getBalloons().remove(b);
                        this.remove();
                        b.boom();
                        game.addScore();
                        break;
                    }
                }
            }

            this.pao.translateY(Configure.BALL_SPEED * Gdx.graphics.getDeltaTime());
        }else{
            this.remove();
        }
        this.pao.draw(batch);
    }

    @Override
    public Actor hit(float x, float y, boolean touchable) {
        if(x >= 0 && y >= 0 && this.height >y && this.width > x){
            return this;
        }else {
            return null;
        }
    }

    public Rectangle getRectangle(){
        return this.pao.getBoundingRectangle();
    }

}
