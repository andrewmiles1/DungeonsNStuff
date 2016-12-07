package com.burnt_toast.dungeons_n_stuff.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.burnt_toast.dungeons_n_stuff.MainFrame;

public class DesktopLauncher {
	public static void main (String[] arg) {
		MainFrame main = new MainFrame();
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = (int) MainFrame.SCREEN_WIDTH * 4;
		config.height = (int) MainFrame.SCREEN_HEIGHT * 4;
		new LwjglApplication(main, config);
	}
}
