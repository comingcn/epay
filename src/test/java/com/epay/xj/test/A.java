package com.epay.xj.test;

public class A {
	private int id;
	private String code;
	private int money;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public int getMoney() {
		return money;
	}

	public void setMoney(int money) {
		this.money = money;
	}

	@Override
	public String toString() {
		return "A [id=" + id + ", code=" + code + ", money=" + money + "]";
	}
}
