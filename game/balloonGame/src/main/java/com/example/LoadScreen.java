package com.example;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.viewport.StretchViewport;

/**
 * Created by guo on 2017/1/19.
 */
public class LoadScreen implements Screen {

    private Texture bgTexture;
    private Image bg;

    private Stage stage;

    public LoadScreen(){
        bgTexture = new Texture(Gdx.files.internal("picture/load.jpg"));
        bg = new Image(new TextureRegion(bgTexture));
        bg.setPosition(0, 0);
        stage = new Stage(new StretchViewport(Configure.WORLD_WIDTH, Configure.WORLD_HEIGHT));
        stage.addActor(bg);
    }


    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1,1,1,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
