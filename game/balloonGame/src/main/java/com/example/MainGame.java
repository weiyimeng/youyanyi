package com.example;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;

public class MainGame extends Game {
	private StartScreen start;
	private GameScreen game;
	private LoadScreen loading;


	//游戏每关的参数
	public static Chapter[] chapters = new Chapter[]{
			new Chapter(Configure.GATING_SPEED_STATIC, Configure.GATING_WIDTH_WIDE),
			new Chapter(Configure.GATING_SPEED_STATIC, Configure.GATING_WIDTH_MIDDLE),
			new Chapter(Configure.GATING_SPEED_STATIC, Configure.GATING_WIDTH_FINE),
			new Chapter(Configure.GATING_SPEED_SLOW, Configure.GATING_WIDTH_WIDE),
			new Chapter(Configure.GATING_SPEED_SLOW, Configure.GATING_WIDTH_MIDDLE),
			new Chapter(Configure.GATING_SPEED_SLOW, Configure.GATING_WIDTH_FINE),
			new Chapter(Configure.GATING_SPEED_MIDDLE, Configure.GATING_WIDTH_WIDE),
			new Chapter(Configure.GATING_SPEED_MIDDLE, Configure.GATING_WIDTH_MIDDLE),
			new Chapter(Configure.GATING_SPEED_MIDDLE, Configure.GATING_WIDTH_FINE),
			new Chapter(Configure.GATING_SPEED_FAST, Configure.GATING_WIDTH_WIDE),
			new Chapter(Configure.GATING_SPEED_FAST, Configure.GATING_WIDTH_MIDDLE),
			new Chapter(Configure.GATING_SPEED_FAST, Configure.GATING_WIDTH_FINE)
	};


	/**
	 * 游戏参数
	 */
	public int chapter = 0;  //关数
	public float pass = 0.8F; //通过率
	public int time = 3;   //设置游戏时长
	public int color = 0;

	@Override
	public void create() {
		Gdx.app.setLogLevel(Application.LOG_DEBUG);
		this.time = 3;
		this.color = 0;
		this.start = new StartScreen(this);
		this.game = new GameScreen(this);
		this.loading = new LoadScreen();
		this.setScreen(start);
		Resourses.load();
	}


	//进入训练界面；
	public void play(){
		this.setScreen(game);
		if(this.start != null){
			this.start.dispose();
			this.start = null;
		}
	}

	//设置难度级别
	public void setPass(int level){
		switch (level) {
			case Configure.HIGH_LEVEL:
				pass = 0.9F;
				break;
			case Configure.MIDDLE_LEVEL:
				pass = 0.85F;
				break;
			case Configure.EASY_LEVEL:
				pass = 0.8F;
				break;
			default:
				pass = 0.8F;
				break;
		}
	}

	//随机选择颜色
	private void selectColor(){
		this.color = (int)(System.currentTimeMillis() % 10) % 3;
	}

	//下一关
	public void next(){
		this.chapter ++;
		if(this.chapter == 12){
			start = new StartScreen(this);
			this.setScreen(start);
		}else {
			this.setScreen(loading);
		}
		if(this.game != null){
			game.dispose();
			game = null;
		}
		this.selectColor();
		this.game = new GameScreen(this);
		this.setScreen(game);
	}

	//重完此关
	public void rePlay(){
		this.setScreen(loading);
		if(this.game != null){
			game.dispose();
			game = null;
		}
		this.selectColor();
		this.game = new GameScreen(this);
		this.setScreen(game);
	}


	@Override
	public void dispose() {
		super.dispose();
		if(this.start != null){
			this.start.dispose();
		}

		Resourses.release();

		if(this.game != null)
			this.game.dispose();
	}

	public void shoot() {
		this.game.shoot();
	}
}
