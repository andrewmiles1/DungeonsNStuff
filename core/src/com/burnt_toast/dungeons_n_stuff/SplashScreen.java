package com.burnt_toast.dungeons_n_stuff;

import java.util.LinkedList;
import java.util.function.Consumer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGeneratorLoader;
import com.badlogic.gdx.graphics.g2d.freetype.FreetypeFontLoader;
import com.badlogic.gdx.graphics.g2d.freetype.FreetypeFontLoader.FreeTypeFontLoaderParameter;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.FitViewport;

public class SplashScreen implements Screen{

	MainFrame main;
	private Stage splashStage;
	private TextureRegion toastImg;
	private float imgScale = 10;
	
	private int loadIndex;
	private LinkedList<Consumer<Object>> loadCode;//the lines of code to run when loading
	//pretty sure this one won't have much, but it's possible.
	
	private float loadTimerMax = 2.5f;//when this is reached, it shows loading on screen
	private float loadTimer = 0;
	
	
	public SplashScreen(MainFrame passMain){
		main = passMain;
		main.assets.load("textures/mainTileset.png", Texture.class);
		main.assets.load("textures/spriteSheet.png", Texture.class);
		
		main.generator = new FreeTypeFontGenerator(Gdx.files.internal("8bitOperator.ttf"));
		main.parameter = new FreeTypeFontParameter();
		main.parameter.size = 12;
		main.gameFont = main.generator.generateFont(main.parameter);//generate font manually
		
		//load code stuff. this doesn't actually run right here, it just que's it for loading later
		loadIndex = 4;//max it out and go down to -1.
		assignLoaded();//assings loaded assets. And finished loading assets.

	}
	
	/**
	 * finish loading everything and assign the variables to the
	 * loaded assets
	 */
	public void assignLoaded(){
		main.assets.finishLoading();
		
		main.mainTileset = main.assets.get("textures/mainTileset.png", Texture.class);
		main.characterTexture = main.assets.get("textures/spriteSheet.png", Texture.class);
		toastImg = new TextureRegion(main.mainTileset, 120, 16, 8, 8);
		splashStage = new Stage(new ExtendViewport(MainFrame.SCREEN_WIDTH, MainFrame.SCREEN_HEIGHT));
		
		main.glyphLayout = new GlyphLayout();

		
	}
	
	@Override
	public void show() {
		// TODO Auto-generated method stub
		main.fadeIn = true;
		main.fadeOut = false;
		//about to show screen set to fade in
		main.gameFont.getData().scale(0.5f);
	}

	@Override
	public void render(float delta) {
		//ERASE
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		// TODO Auto-generated method stub
		//ERIC
		
		
		//REDRAW
		if(main.fadeIn || main.fadeOut){
			main.updateFade();
			main.fade(splashStage.getBatch());
			main.fade(main.gameFont);
		}
		
		splashStage.act();
		splashStage.draw();
		
		splashStage.getBatch().begin();
		splashStage.getBatch().draw(toastImg, splashStage.getWidth()/2 - (toastImg.getRegionWidth()*imgScale/2),
				(splashStage.getHeight()/2 - (toastImg.getRegionHeight()*imgScale / 2) + 100),
				toastImg.getRegionWidth() * imgScale, (toastImg.getRegionHeight() * imgScale));
		main.gameFont.draw(splashStage.getBatch(), "Burnt_Toast", (splashStage.getWidth()/2) - (main.getWidthOf("Burnt_Toast") / 2), 
				(splashStage.getHeight()/2) - (main.getHeightOf("Burnt_Toast") / 2));
	
		if(loadTimer >= loadTimerMax && main.assets.getQueuedAssets() != 0 && loadIndex < loadCode.size()){//if it's taking too long tell them it's loading.
			main.gameFont.draw(splashStage.getBatch(), "Loading...", splashStage.getWidth() / 2 - main.getWidthOf("Loading..")/2,
					splashStage.getHeight()/2 - main.getHeightOf("Loading...")/2 - 100);
			//main.assets.finishLoading();
		}
		
		
		//LOADING TIMER
		//okay so if I do it like this it never works so sorry.
		
		if(loadTimer < loadTimerMax){
			loadTimer+= Gdx.graphics.getDeltaTime();
		}
		else{
			if(main.assets.getQueuedAssets() == 0 && loadIndex == -1){
				//we're done!
				main.fadeOut = true;
				if(main.fadeTracker == 0)main.setScreen(main.menuScreen);
			}
		}
		
		splashStage.getBatch().end();
		
		//INPUT

		//CALCULATE
		if(loadIndex > -1){
			loadSomeCode();
			loadIndex--;
		}
		main.assets.update();
		//checks if last asset loaded
		
	}
	public void loadSomeCode(){
		switch (loadIndex){
		case 4://THIS ONE HAPPENS FIRST AND GOES DOWN
			main.mapLoader = new TmxMapLoader();
			main.assets.load("textures/spriteSheet.png", Texture.class);
			break;
		case 3:
			main.archerFrames = new TextureRegion[5];
			main.archerFrames[0] = new TextureRegion(main.characterTexture, 0, 10, 9, 10);//person
			main.archerFrames[1] = new TextureRegion(main.characterTexture, 9, 10, 8, 9);//person step attack
			main.archerFrames[2] = new TextureRegion(main.characterTexture, 17, 11, 8, 8);//meelee
			main.archerFrames[3] = new TextureRegion(main.characterTexture, 25, 12, 3, 5);//blue normal arrow
			main.archerFrames[4] = new TextureRegion(main.characterTexture, 28, 12, 3, 5);//green poison arrow
			
			main.arrowStages = new TextureRegion[3];
			main.arrowStages[0] = new TextureRegion(main.mainTileset, 0, 69, 10, 10);
			main.arrowStages[1] = new TextureRegion(main.mainTileset, 10, 69, 10, 10);
			main.arrowStages[2] = new TextureRegion(main.mainTileset, 20, 69, 10, 10);
			main.swordStages = new TextureRegion[3];
			main.arrowStages[0] = new TextureRegion(main.mainTileset, 0, 49, 10, 10);
			main.swordStages[1] = new TextureRegion(main.mainTileset, 10, 49, 10, 10);
			main.swordStages[2] = new TextureRegion(main.mainTileset, 20, 49, 10, 10);
			main.ringStages = new TextureRegion[3];
			main.ringStages[0] = new TextureRegion(main.mainTileset, 0, 59, 10, 10);
			main.ringStages[1] = new TextureRegion(main.mainTileset, 10, 59, 10, 10);
			main.ringStages[2] = new TextureRegion(main.mainTileset, 20, 59, 10, 10);
			break;
		case 2:
			main.wizardFrames = new TextureRegion[5];
			main.wizardFrames[0] = new TextureRegion(main.characterTexture, 0, 20, 8, 9);//person
			main.wizardFrames[1] = new TextureRegion(main.characterTexture, 8, 20, 8, 9);//person step & attack
			main.wizardFrames[2] = new TextureRegion(main.characterTexture, 17, 20, 8, 8);//melee
			break;
		case 1:
			main.warriorFrames = new TextureRegion[5];
			main.warriorFrames[0] = new TextureRegion(main.characterTexture, 0, 0, 8, 9);//person 
			main.warriorFrames[1] = new TextureRegion(main.characterTexture, 8, 0, 9, 9);//person step and attack
			main.warriorFrames[2] = new TextureRegion(main.characterTexture, 18, 1, 6, 6);//meelee.
			break;
		case 0:
			main.silverFrame = new TextureRegion(MainFrame.mainTileset, 112, 24, 8, 8);
			Gdx.input.setInputProcessor(main.getInputMultiplexer());
			main.menuScreen = new MenuScreen(main);
			main.playScreen = new PlayScreen(main);
			break;
		}
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
		splashStage.getViewport().update(width, height);
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		splashStage.dispose();
		
	}

}
