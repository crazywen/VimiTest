package com.example.vimitest.entity;

/**
 * ÁªÏµÈË
 * 
 * @author crazywen
 * 
 */

public class Contact {

	private int id;
	private String name;
	private String url;
	private Category category;

	public Contact(int id, String name, String url, Category category) {
		super();
		this.id = id;
		this.name = name;
		this.url = url;
		this.category = category;
	}

	public Contact() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Contact(String name, String url, Category category) {
		super();
		this.name = name;
		this.url = url;
		this.category = category;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

}
