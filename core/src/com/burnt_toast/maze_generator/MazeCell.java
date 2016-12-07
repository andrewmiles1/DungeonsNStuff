package com.burnt_toast.maze_generator;

public class MazeCell {

	int famId;
	int collision;
	boolean canConnect;//true if has neighbors not of the same family.
	
	public MazeCell(){
		//doesn't do anything.
	}
	
	public int getFamily(){
		return famId;
	}
	public int getCollision(){
		return collision;
	}
	public void setFamily(int passFam){
		famId = passFam;
	}
	public void setCollision(int passCollision){
		collision = passCollision;
	}
}
