package com.burnt_toast.dungeons_n_stuff;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Warrior extends Character{

	public Warrior() {
		super(MainFrame.warriorFrames[1].getRegionWidth(), MainFrame.warriorFrames[1].getRegionHeight());
		// TODO Auto-generated constructor stub
	}

	@Override
	public void draw(SpriteBatch batch) {
		// TODO Auto-generated method stub
		//if flipped, then it draws, if not, then it doesn't draw flipped. long function holy macaroni.
		batch.draw(MainFrame.warriorFrames[animationIndex],
				flipped? collisionRect.x + MainFrame.warriorFrames[animationIndex].getRegionWidth():collisionRect.x,
				collisionRect.y,
				flipped?
				MainFrame.warriorFrames[animationIndex].getRegionWidth()*-1:MainFrame.warriorFrames[animationIndex].getRegionWidth(),
				MainFrame.warriorFrames[animationIndex].getRegionHeight());
	}

	@Override
	public void attack() {
		// TODO Auto-generated method stub
	}

}
