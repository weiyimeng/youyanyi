package com.uyu.device.devicetraining.data.entity.type;

public enum DefaultTrainingContent {
	EnglishLetter("英文字母", -1), Add("添加", 0);


	/**
	 * 名称
	 */
	private String name;
	/**
	 * 值
	 */
	private int value;

	/*
	 * 构造方法
	 */
	private DefaultTrainingContent(String name, int value) {
		this.name = name;
		this.value = value;
	}

	/**
	 * 获取名称
	 * 
	 * @return
	 */
	public String getName() {
		return name;
	}

	/**
	 * 获取值
	 * 
	 * @return
	 */
	public int getValue() {
		return value;
	}
}