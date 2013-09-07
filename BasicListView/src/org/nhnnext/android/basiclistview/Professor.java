package org.nhnnext.android.basiclistview;

public class Professor {
	private String name;
	private String village;
	private String imgPath;
	
	Professor(String name, String village, String imgPath) {
		this.name = name;
		this.village = village;
		this.imgPath = imgPath;
	}
	
	public String getName() {
		return name;
	}
	public String getVillage() {
		return village;
	}
	public String getImgPath() {
		return imgPath;
	}
	
}
