package com.dungeons_n_stuff.dungeon_layout;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.burnt_toast.dungeons_n_stuff.MainFrame;
import com.burnt_toast.toast_layout.Panel;

public class DungeonSilverPanel extends Panel{

	public DungeonSilverPanel(){
		super(MainFrame.silverFrame);
		this.setRefCorn(2, 2);
		this.setRefHoriz(4,  1);
		this.setRefVert(1, 4);
		this.setRefMiddle(6, 6);
	}

}
