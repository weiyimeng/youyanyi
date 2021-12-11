package com.example.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.example.Configure;

/**
 * Created by guo on 2017/1/9.
 */
public class BatteryActor extends Actor{
    private Texture texture;
    private float x, y;
    private float stateTime;
    private float height, width;
    Sprite pao;
    int direction = 1;
    private float right, left;
    public BatteryActor() {
        texture = new Texture(Gdx.files.internal("picture/pao_2.png"));
        this.height = texture.getHeight();
        this.width = this.texture.getWidth();
        this.pao = new Sprite(texture);
        this.left = Configure.GUN_LEFT;
        this.right = Configure.GUN_RIGHT;
        this.pao.setPosition(this.left, 0);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        stateTime += Gdx.graphics.getDeltaTime();

        if(direction == 1 && this.pao.getX() <= this.right){
            this.pao.translateX(Configure.GUN_SPEED * Gdx.graphics.getDeltaTime());
        }else{
            direction = -1;
        }
        if(direction == -1 && this.pao.getX() >= this.left){
            this.pao.translateX((-1) * Configure.GUN_SPEED * Gdx.graphics.getDeltaTime());
        }else{
            direction = 1;
        }
        this.pao.draw(batch);
    }

    public float getPX(){
        return this.pao.getX();
    }

    public float getPY(){
        return this.pao.getY();
    }

    public float getPW(){
        return this.pao.getWidth();
    }

    public float getPH(){
        return this.pao.getHeight();
    }

    @Override
    public Actor hit(float x, float y, boolean touchable) {
        if(x >= 0 && y >= 0 && this.height >y && this.width > x){
            return this;
        }else {
            return null;
        }
    }
}
