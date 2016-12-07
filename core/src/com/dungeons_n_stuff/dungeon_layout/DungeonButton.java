package com.dungeons_n_stuff.dungeon_layout;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.burnt_toast.dungeons_n_stuff.MainFrame;
import com.burnt_toast.toast_layout.SheetButton;

public class DungeonButton extends SheetButton{

	public DungeonButton(String passButtonTxt, MainFrame passMain) {
		super(new TextureRegion(passMain.mainTileset, 112, 24, 8, 8));
		this.setButtonText(passButtonTxt);
		this.setRefCorn(2, 2);
		this.setRefHoriz(4,  1);
		this.setRefVert(1, 4);
		this.setRefMiddle(6, 6);
		// TODO Auto-generated constructor stub
	}
	


}
