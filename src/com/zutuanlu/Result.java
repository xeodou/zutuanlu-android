package com.zutuanlu;

public class Result {
	public int code;
	
	public String msg;
	
	public Result(){
		
	}
	public void setResult(int code, String msg){
		this.code = code;
		this.msg = msg;
	}
	
	public int getCode(){
		return this.code;
	}
	
	public String getMsg(){
		return this.msg;
	}
}
