package com.example;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.example.actors.BallActor;
import com.example.actors.BalloonActor;
import com.example.actors.BatteryActor;
import com.example.actors.TargetActor;


import java.util.ArrayList;

/**
 * Created by guo on 2017/1/17.
 */
public class GameScreen implements Screen {

    private MainGame game;
    private Stage stage, resultStage;
    private ArrayList<BalloonActor> balloons;
    private SpriteBatch batch;  //绘图使用SpriteBatch
    private BatteryActor battery;
    private Label label;
    private Label score, hitPrecent;
    private Label timeLabel;
    private BitmapFont font;
    private Image resultBg;
    private Button next, replay;


    public int gatingColor = 0;
    public int gatingWidth = 0;
    public float gatingSpeed = 0;
    int allTime = 0; //本关游戏的总时长；
    float time = 0; //气球放飞的时间间隔；
    private float timer; //本界面计时器；
    private int count;   //
    private int myScore;  //角色得分
    private int sum;  //气球总数
    private int hit;
    Chapter chapter;

    private static float[] ballonSites = new float[]{
        1F, 2F, 3F, 4F, 5L, 4L, 3F, 2F, 1F, 0F
    } ;

    public GameScreen(MainGame game){
        this.game = game;

        this.chapter = MainGame.chapters[game.chapter];
        this.gatingColor = game.color;
        this.gatingWidth = chapter.gratingWidth;
        this.gatingSpeed = chapter.speed;
        this.balloons = new ArrayList<BalloonActor>();
        stage = new Stage(new StretchViewport(Configure.WORLD_WIDTH, Configure.WORLD_HEIGHT));
        resultStage = new Stage(new StretchViewport(Configure.WORLD_WIDTH, Configure.WORLD_HEIGHT));

        initLabel();

        this.stage.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                //System.out.println("click.");
                //shoot();
            }
        });

        this.resultBg = new Image(new TextureRegion(new Texture(Gdx.files.internal("picture/bg_result_1.jpg"))));
        this.resultBg.setPosition(175, 15);
        this.resultStage.addActor(resultBg);

        initButton();


        this.batch = new SpriteBatch();
        battery = new BatteryActor();
        stage.addActor(new TargetActor(this));
        stage.addActor(battery);
        this.stage.addActor(this.label);
        this.stage.addActor(this.score);
        this.stage.addActor(this.timeLabel);
    }


    //初始化标签
    private void initLabel(){
        this.font = new BitmapFont(Gdx.files.internal("font/main.fnt"));
        Label.LabelStyle style = new Label.LabelStyle();
        style.font = this.font;
        if(gatingColor == Configure.BLACK) {
            style.fontColor = new Color(1,0,0,1);
        }else
            style.fontColor = new Color(0,0,0,1);
        this.label = new Label("关卡: " + (game.chapter + 1), style);
        this.label.setPosition(20, 420);
        this.label.setScale(1.5f);

        this.score = new Label("得分: " + myScore, style);
        this.score.setScale(1.5f);
        this.score.setPosition(650, 420);

        this.timeLabel = new Label("时间: " + this.allTime, style);
        this.timeLabel.setPosition(20, 380);

        this.hitPrecent =  new Label("命中率: " + hit, style);
        this.hitPrecent.setScale(1.8F);
        this.hitPrecent.setPosition(300, 250);

    }

    //初始化结果界面的按钮
    private void initButton(){
        Button.ButtonStyle style = new Button.ButtonStyle();
        style.down = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("picture/next_down.jpg"))));
        style.up = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("picture/next_up.jpg"))));
        this.next = new Button(style);
        this.next.setPosition(350, 150);
        this.next.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.next();
            }
        });
        Button.ButtonStyle stylere = new Button.ButtonStyle();
        stylere.down = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("picture/replay_down.jpg"))));
        stylere.up = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("picture/replay_up.jpg"))));
        this.replay = new Button(stylere);
        this.replay.setPosition(350, 150);
        this.replay.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.rePlay();
            }
        });
    }


    //初始化initResultStage
    private void initResultStage(){
        float hitf = (myScore * 1.0F) / (sum * 1.0f);
        if(hitf >= game.pass){
            this.resultStage.addActor(next);
        }else{
            this.resultStage.addActor(replay);
        }
        this.hit = (int)(hitf * 100);
        this.hitPrecent.setText("命中率: " + hit + "%");
        this.resultStage.addActor(hitPrecent);
        Gdx.input.setInputProcessor(resultStage);
    }


    //发射子弹；
    public void shoot(){
        stage.addActor(new BallActor(battery.getPX(), battery.getPW(), battery.getPH(), GameScreen.this));
    }

    @Override
    public void show() {
        time = 0;
        this.myScore = 0;
        count = 0;
        this.sum = 1;
        System.out.println("关卡：" + game.chapter);
        this.allTime = this.game.time * 60;
        this.timeLabel.setText("时间: " + this.allTime);
        this.label.setText("关卡: " + (this.game.chapter + 1));
        Gdx.input.setInputProcessor(stage);
        BalloonActor b = new BalloonActor(Configure.GUN_LEFT + Configure.BALLOON_INTERVAL_SPACE, this);
        stage.addActor(b);
        this.balloons.add(b);
    }


    public void addScore(){
        this.myScore++;
        this.score.setText("得分: " + myScore);
    }

    @Override
    public void render(float delta) {

        this.timer += delta;
        time += delta;

        if(Gdx.input.justTouched()){
            System.out.println("touch");
            shoot();
        }

        if(this.timer <= this.allTime) {
            Gdx.gl.glClearColor(0.96f, 0.96f, 0.85f, 1);
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
            stage.act(Gdx.graphics.getDeltaTime());
            this.timeLabel.setText("时间: " + (this.allTime - (int)timer));
            if (time >= Configure.BALLOON_INTERVAL_TIME) {
                count++;
                count %= 10;
                BalloonActor b = new BalloonActor(Configure.GUN_LEFT + ballonSites[count] * Configure.BALLOON_INTERVAL_SPACE, this);
                stage.addActor(b);
                this.balloons.add(b);
                this.sum++;
                time = 0;
            }
            stage.draw();
        }else{
            stage.draw();
            initResultStage();
            resultStage.draw();
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
        if(this.stage != null)
            this.stage.dispose();
    }


    public ArrayList<BalloonActor> getBalloons(){
        return this.balloons;
    }

}
