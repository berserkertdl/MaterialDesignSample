package com.md.helper.json_xml_paser.utils;

import java.io.Serializable;

/**代表专辑的实体类，在进行xml解析时用来封装数据*/
public class CDEntity implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1692431541356358640L;
	/**专辑标题*/
	private String title;
	/**专辑歌手*/
	private String artist;
	/**专辑国家*/
	private String country;
	/**唱片公司*/
	private String company;
	/**价格*/
	private float price;
	/**年份*/
	private int year;
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getArtist() {
		return artist;
	}
	public void setArtist(String artist) {
		this.artist = artist;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public String getCompany() {
		return company;
	}
	public void setCompany(String company) {
		this.company = company;
	}
	public float getPrice() {
		return price;
	}
	public void setPrice(float price) {
		this.price = price;
	}
	public int getYear() {
		return year;
	}
	public void setYear(int year) {
		this.year = year;
	}
	
	public CDEntity() {
		super();
	}
	public CDEntity(String title, String artist, String country,
			String company, float price, int year) {
		super();
		this.title = title;
		this.artist = artist;
		this.country = country;
		this.company = company;
		this.price = price;
		this.year = year;
	}
	@Override
	public String toString() {
		return "CD名字："+title;
	}
	
	
}
