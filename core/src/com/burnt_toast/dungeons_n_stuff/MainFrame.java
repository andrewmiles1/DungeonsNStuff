package com.burnt_toast.dungeons_n_stuff;

import java.util.function.Consumer;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;

public class MainFrame extends Game {
	public SpriteBatch batch;
	public AssetManager assets;
	
	private InputMultiplexer inputMulti;
	
	public static Texture mainTileset;
	public Texture characterTexture;
	
	public PlayScreen playScreen;
	public MenuScreen menuScreen;
	public SplashScreen splashScreen;
	
	public GlyphLayout glyphLayout;
	public BitmapFont gameFont;
	public FreeTypeFontGenerator generator;
	public FreeTypeFontParameter parameter;//used for loading fonts
	
	//Texture Regions
	public static TextureRegion silverFrame;
	
	public static TextureRegion[] archerFrames;//0 and 1 are person and 2 is meelee and 3 is arrow
	public static TextureRegion[] wizardFrames;//0 and 1 are person and 2 is meelee 
	public static TextureRegion[] warriorFrames;//0 and 1 are person and 2 is meelee and 3 is throwing sword.
	
	public static TextureRegion[] swordStages;//0 1 and 2 are the 3 levels
	public static TextureRegion[] ringStages;
	public static TextureRegion[] arrowStages;
	public Consumer fadeOutCode;//code that's run when a fade out completes.
	
	public float fadeTracker;//to track the fade function
	public float fadeFactor = 2;
	public boolean fadeIn, fadeOut;
	public String fadeCodename;//since I can't use lambda's I have to return a codename of who called me
	
	public static final float SCREEN_WIDTH = 16 * 30;//480
	public static final float SCREEN_HEIGHT = 10 * 30;
	
	public static final float TILE_SIZE = 8;//8 * 2
	
	TmxMapLoader mapLoader;
	public OrthogonalTiledMapRenderer otmr;
	
	public MainFrame(){
		inputMulti = new InputMultiplexer();
		fadeCodename = "none";
	}
	
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		assets = new AssetManager();
		splashScreen = new SplashScreen(this);
		this.setScreen(splashScreen);
	}

	@Override
	public void render () {
		super.render();
	}
	@Override
	public void dispose(){
		assets.dispose();
		batch.dispose();
	}
	public void setFadeCode(Consumer<Object> passCode){
		fadeOutCode = passCode;
	}
	public String updateFade(){
		if(fadeIn){//fading in
			if(fadeTracker < 1){
				fadeTracker += (fadeFactor * Gdx.graphics.getDeltaTime());
				if(fadeTracker > 1){
					fadeTracker = 1;
					fadeIn = false;
					return fadeCodename;//finished now at 1
				}
				return "null";//not finished
			}
			return fadeCodename;//finished if not less than 0
		}
		if(fadeOut){//fading out
			if(fadeTracker > 0){
				fadeTracker -= (fadeFactor * Gdx.graphics.getDeltaTime());
				if(fadeTracker < 0){
					fadeTracker = 0;
					fadeOut = false;
					return fadeCodename;//finished, now at 0
				}
				return "null";//not finished
			}
			return fadeCodename;//finished if not larger than 0
		}
		return "null";//returns if neither fadeIn or fadeOut were true;
	}
	public <T> void fade(T t){
		if(t.getClass() == Music.class){
			//
			((Music)t).setVolume(fadeTracker);
			System.out.println(fadeTracker);
		}
		if(t.getClass() == SpriteBatch.class){
			((SpriteBatch)t).setColor(fadeTracker, fadeTracker, fadeTracker, 1);
		}
		if(t.getClass() == BitmapFont.class){
			((BitmapFont)t).setColor(((BitmapFont)t).getColor().r, ((BitmapFont)t).getColor().g,
					((BitmapFont)t).getColor().b, fadeTracker);
		}
	}
	public float getHeightOf(String str){
		glyphLayout.setText(gameFont, str);
		return glyphLayout.height;
	}
	public float getWidthOf(String str){
		glyphLayout.setText(gameFont, str);
		return glyphLayout.width;
	}
	/**
	 * adds passed processor to the input multiplexer found in MainFrame
	 * @param passProcessor
	 */
	public void addInputProcessor(InputProcessor passProcessor){
		inputMulti.addProcessor(passProcessor);
	}
	/**
	 * removes the passed processor from the Input Multiplexer found in MainFrame
	 * @param passProcessor
	 */
	public void removeInputProcessor(InputProcessor passProcessor){
		inputMulti.removeProcessor(passProcessor);
	}
	/**
	 * returns the input multiplexer for setting in the splash screen.
	 * @return
	 */
	public InputMultiplexer getInputMultiplexer(){
		return inputMulti;
	}
}
