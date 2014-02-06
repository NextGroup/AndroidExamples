package org.nhnnext.android.day2;

public class Professor {
	private String name;
	private String village;
	private String imgPath;
	
	Professor(String name, String village, String imgPath) {
		this.name = name;
		this.village = village;
		this.imgPath = imgPath;
	}
	
	//커스텀 어댑터에서 사용하기 위해 get 메소드들이 필요합니다.
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
