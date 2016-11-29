package com.renke.core.entity;

public class ByteArray {
	private byte[] array;
	private int position;
	private int limit;
	private int mark;
	
	public ByteArray(byte[] array){
		this(array,0,array.length);
	}
	
	public ByteArray(byte[] array,int position){
		this(array,position,array.length);
	}

	public ByteArray(byte[] array,int position,int limit){
		this.array = array;
		this.position = position;
		this.limit = limit;
	}
	
	public final void clear(){
		this.position = 0;
		this.limit = -1;
		this.array = null;
	}
	
	public final int position(int pos){
		this.position = pos;
		return this.position;
	}
	
	public final int limit(int lim){
		this.limit = lim;
		return this.limit;
	}
	
	public final int position(){
		return this.position;
	}
	
	public final int next(){
		return ++this.position;
	}
	
	public final int moveByN(int n){
		this.position = position + n;
		return this.position;
	}
	
	public final int moveToN(int n){
		this.position = n;
		return this.position;
	}
	
	public final int mark(){
		this.mark = this.position;
		return this.mark;
	}
	
	public final int limit(){
		return this.limit;
	}
	
	public final byte[] getArray(){
		return this.array;
	}
	
	public final byte get(int index){
		return array[index];
	}
}
