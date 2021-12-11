package com.example;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.StretchViewport;

/**
 * Created by guo on 2017/1/17.
 */
public class StartScreen implements Screen {

    private MainGame game;
    private Texture back, settingBack;
    private Texture startUp, settingUp, settingOkUp, checkBox_true;
    private Texture startDown, settingDown, settingOkDown, checkBox_false;
    private Button start, setting, settingOK;
    private Label timeLabel, levelLabel;
    private BitmapFont font;
    private CheckBox min_3, min_5, min_8;
    private CheckBox levelEasy, levelMiddle, levelHigh;

    private Stage stage;
    private Stage settingStage;

    private Music music;
    private float time;
    private boolean isPlay, isHideSetting;
    private Image backgroud, settingBg;

    public StartScreen(final MainGame game){
        this.game = game;

        this.back = new Texture(Gdx.files.internal("picture/back1_2.jpg"));
        this.startUp = new Texture(Gdx.files.internal("picture/start_up2.jpg"));
        this.startDown = new Texture(Gdx.files.internal("picture/start_down2.jpg"));

        this.settingUp = new Texture(Gdx.files.internal("picture/setting_up2.jpg"));
        this.settingDown = new Texture(Gdx.files.internal("picture/setting_down2.jpg"));

        this.settingOkUp = new Texture(Gdx.files.internal("picture/setting_ok_up.jpg"));
        this.settingOkDown = new Texture(Gdx.files.internal("picture/setting_ok_down.jpg"));

        this.settingBack = new Texture(Gdx.files.internal("picture/setting_bg3.png"));

        this.checkBox_false = new Texture(Gdx.files.internal("picture/checkbox2_1.png"));
        this.checkBox_true = new Texture(Gdx.files.internal("picture/checkbox_ok.png"));

        this.font = new BitmapFont(Gdx.files.internal("font/main.fnt"));

        initButton();

        this.stage = new Stage(new StretchViewport(Configure.WORLD_WIDTH, Configure.WORLD_HEIGHT));
        this.settingStage = new Stage(new StretchViewport(Configure.WORLD_WIDTH, Configure.WORLD_HEIGHT));

        this.backgroud = new Image(this.back);
        this.stage.addActor(this.backgroud);

        this.music = Gdx.audio.newMusic(Gdx.files.internal("audio/start.mp3"));
        this.music.setOnCompletionListener(new Music.OnCompletionListener() {
            @Override
            public void onCompletion(Music music) {
                stage.addActor(start);
                stage.addActor(setting);
            }
        });

        initSettingStage();
    }


    /**
     * 初始化Button
     */
    private void initButton(){
        Button.ButtonStyle style = new Button.ButtonStyle();
        style.up = new TextureRegionDrawable(new TextureRegion(this.startUp));
        style.down = new TextureRegionDrawable(new TextureRegion(this.startDown));
        this.start = new Button(style);
        this.start.setPosition(350, 215);

        this.start.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("Click start");
                game.play();
            }
        });

        Button.ButtonStyle style2 = new Button.ButtonStyle();
        style2.up = new TextureRegionDrawable(new TextureRegion(this.settingUp));
        style2.down = new TextureRegionDrawable(new TextureRegion(this.settingDown));
        this.setting = new Button(style2);

        this.setting.setPosition(350, 155);

        this.setting.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("Click setting");
                hideSettingStage();
            }
        });
    }


    //初始化复选框
    private void initCheckBox(){
        CheckBox.CheckBoxStyle style = new CheckBox.CheckBoxStyle();
        // 设置 style 的 选中 和 未选中 状态的纹理区域
        style.checkboxOn = new TextureRegionDrawable(new TextureRegion(checkBox_true));
        style.checkboxOff = new TextureRegionDrawable(new TextureRegion(checkBox_false));
        // 设置复选框文本的位图字体
        style.font = font;
        style.checkedFontColor = new Color(1,0,0,1);
        style.fontColor = new Color(0, 0, 0, 1);
        //创建CheckBox
        min_3 = new CheckBox("3 分钟", style);
        min_5 = new CheckBox("5 分钟", style);
        min_8 = new CheckBox("8 分钟", style);
        min_3.setScale(0.8F);
        min_5.setScale(0.8F);
        min_8.setScale(0.8F);

        min_3.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if(min_3.isChecked()){
                    game.time = 3;
                    min_5.setChecked(false);
                    min_8.setChecked(false);
                }
            }
        });

        min_5.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if(min_5.isChecked()){
                    game.time = 5;
                    min_3.setChecked(false);
                    min_8.setChecked(false);
                }
            }
        });

        min_8.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if(min_8.isChecked()){
                    game.time = 8;
                    min_5.setChecked(false);
                    min_3.setChecked(false);
                }
            }
        });


        levelEasy = new CheckBox("简单", style);
        levelMiddle =  new CheckBox("中等", style);
        levelHigh = new CheckBox("困难", style);
        levelEasy.setScale(0.8F);
        levelMiddle.setScale(0.8F);
        levelHigh.setScale(0.8F);

        switch (game.time){
            case 3:
                min_3.setChecked(true);
                break;
            case 5:
                min_5.setChecked(true);
                break;
            case 8:
                min_8.setChecked(true);
                break;
        }

        levelEasy.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if(levelEasy.isChecked()){
                    game.setPass(Configure.EASY_LEVEL);
                    levelMiddle.setChecked(false);
                    levelHigh.setChecked(false);
                }
            }
        });

        levelMiddle.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if(levelMiddle.isChecked()){
                    game.setPass(Configure.MIDDLE_LEVEL);
                    levelEasy.setChecked(false);
                    levelHigh.setChecked(false);
                }
            }
        });

        levelHigh.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if(levelHigh.isChecked()){
                    game.setPass(Configure.HIGH_LEVEL);
                    levelMiddle.setChecked(false);
                    levelEasy.setChecked(false);
                }
            }
        });

        if(game.pass == 0.8F)
            levelEasy.setChecked(true);
        if(game.pass == 0.85F)
            levelMiddle.setChecked(true);
        if(game.pass == 0.9F)
            levelHigh.setChecked(true);

    }

    //初始化标签
    private void initLable(){
        Label.LabelStyle style = new Label.LabelStyle();
        style.font = this.font;
        style.fontColor = new Color(0,0,0,1);
        this.timeLabel = new Label("时间: ", style);
        this.levelLabel = new Label("等级: ", style);

        this.timeLabel.setScale(1.2F);
        this.levelLabel.setScale(1.2F);
    }


    //处理设置界面显示，更改事件监听
    private void hideSettingStage(){
        this.isHideSetting = !isHideSetting;
        if(!this.isHideSetting)
            Gdx.input.setInputProcessor(this.settingStage);
        else
            Gdx.input.setInputProcessor(this.stage);
    }


    //初始化，设置界面
    private void initSettingStage(){
        Button.ButtonStyle style2 = new Button.ButtonStyle();
        style2.up = new TextureRegionDrawable(new TextureRegion(this.settingOkUp));
        style2.down = new TextureRegionDrawable(new TextureRegion(this.settingOkDown));
        this.settingOK = new Button(style2);

        this.settingOK.setPosition(350, 155);

        this.settingOK.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("Setting");
                hideSettingStage();
            }
        });

        this.settingBg = new Image(this.settingBack);
        this.settingBg.setPosition(225, 50);
        this.settingStage.addActor(this.settingBg);

        this.initCheckBox();
        this.initLable();

        this.timeLabel.setPosition(250, 320);
        this.min_3.setPosition(350, 320);
        this.min_5.setPosition(350, 285);
        this.min_8.setPosition(350, 250);

        this.levelLabel.setPosition(250, 200);
        this.levelEasy.setPosition(350, 200);
        this.levelMiddle.setPosition(350, 165);
        this.levelHigh.setPosition(350, 130);

        this.settingOK.setPosition(320, 80);


        this.settingStage.addActor(this.timeLabel);
        this.settingStage.addActor(this.min_3);
        this.settingStage.addActor(this.min_5);
        this.settingStage.addActor(this.min_8);
        this.settingStage.addActor(this.levelLabel);
        this.settingStage.addActor(this.levelEasy);
        this.settingStage.addActor(this.levelMiddle);
        this.settingStage.addActor(this.levelHigh);


        this.settingStage.addActor(this.settingOK);
    }


    @Override
    public void show() {
        time = 0;
        this.isPlay = false;
        this.isHideSetting = true;
        Gdx.input.setInputProcessor(this.stage);
    }

    @Override
    public void render(float delta) {

        this.time += delta;
        if(time >= 0.2F && !isPlay) {
            this.music.play();
            this.isPlay = true;
        }
        Gdx.gl.glClearColor(1,1,1,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

            this.stage.act();
            this.stage.draw();

        if(!isHideSetting){
            this.settingStage.act();
            this.settingStage.draw();
        }
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
        if(this.back != null){
            this.back.dispose();
        }
        if(this.startUp != null)
            this.startUp.dispose();
        if(this.startDown != null)
            this.startDown.dispose();
        if(this.settingUp != null)
            this.settingUp.dispose();
        if(this.settingDown != null)
            this.settingDown.dispose();
        if(this.music != null)
            this.music.dispose();
        if(this.stage != null)
            this.stage.dispose();
    }
}
