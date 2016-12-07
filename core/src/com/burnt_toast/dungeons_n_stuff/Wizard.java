package com.burnt_toast.dungeons_n_stuff;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Wizard extends Character{

	public Wizard() {
		super(MainFrame.wizardFrames[1].getRegionWidth(), MainFrame.wizardFrames[1].getRegionHeight());
		// TODO Auto-generated constructor stub
	}

	@Override
	public void draw(SpriteBatch batch) {
		// TODO Auto-generated method stub
		//if flipped, then it draws, if not, then it doesn't draw flipped. long function holy macaroni.
		batch.draw(MainFrame.wizardFrames[animationIndex],
				flipped? collisionRect.x + MainFrame.wizardFrames[animationIndex].getRegionWidth():collisionRect.x,
				collisionRect.y,
				flipped?
				MainFrame.wizardFrames[animationIndex].getRegionWidth()*-1:MainFrame.wizardFrames[animationIndex].getRegionWidth(),
				MainFrame.wizardFrames[animationIndex].getRegionHeight());
	
	}

	@Override
	public void attack() {
		// TODO Auto-generated method stub
		
	}

}
