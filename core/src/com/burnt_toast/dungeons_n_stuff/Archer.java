package com.burnt_toast.dungeons_n_stuff;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Archer extends Character{
	private int arrowCount;//how many arrows do I have
	private int arrowMax;//how many arrows can I carry
	
	public Archer(){
		super(MainFrame.archerFrames[1].getRegionWidth(), MainFrame.archerFrames[1].getRegionHeight());
		frameSizeX = MainFrame.archerFrames[1].getRegionWidth();
		frameSizeY = MainFrame.archerFrames[1].getRegionHeight();

		
	}

	@Override
	public void draw(SpriteBatch batch) {
		//if flipped, then it draws, if not, then it doesn't draw flipped. long function holy macaroni.
		batch.draw(MainFrame.archerFrames[animationIndex],
				flipped? collisionRect.x + MainFrame.archerFrames[animationIndex].getRegionWidth():collisionRect.x,
				collisionRect.y,
				flipped?
				MainFrame.archerFrames[animationIndex].getRegionWidth()*-1:MainFrame.archerFrames[animationIndex].getRegionWidth(),
				MainFrame.archerFrames[animationIndex].getRegionHeight());
	}

	@Override
	public void attack() {
		// TODO Auto-generated method stub
		
	}
	
	

}
