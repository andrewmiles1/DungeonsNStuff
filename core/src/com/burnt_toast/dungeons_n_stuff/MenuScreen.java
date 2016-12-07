
package com.burnt_toast.dungeons_n_stuff;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.burnt_toast.toast_layout.MenuLayout;
import com.burnt_toast.toast_layout.Sheet;
import com.dungeons_n_stuff.dungeon_layout.DungeonButton;

public class MenuScreen implements Screen, InputProcessor{

	private TiledMap menuMap;
	private MainFrame main;
	private Stage menuStage;
	private float widthWithZoom;
	private float heightWithZoom;
	private OrthographicCamera orthoCam;
	
	private MenuLayout mainMenu;//play and options buttons.
	private MenuLayout characterPick;//choose your character
	private MenuLayout loadingNControls;//loads the level and shows controls
	private MenuLayout currentLayout;
	
	private DungeonButton playButton;
	private DungeonButton optionsButton;
	
	//character pick stuff
	private DungeonButton archerButton;
	private DungeonButton warriorButton;
	private DungeonButton wizardButton;
	private DungeonButton playWithCharacter;
	private String selectedClass;//used to know which class is going right now.
	
	private TextureRegion[] selectedClassFrames;
	private DungeonButton selectedClassButton;//just a pointer to whatever button is current.
	private int animationIndex;
	private float animationTimer;//used to animate
	private float animationTimerMax = 0.5f;//switches ever half second
	
	private float mapWidth;
	private float mapHeight;
	
	private Character currentPlayer;
	

	
	private Vector3 temp;//used to unproject click coords
	
	public MenuScreen(MainFrame passedMain){
		main = passedMain;
		orthoCam = new OrthographicCamera(MainFrame.SCREEN_WIDTH, MainFrame.SCREEN_HEIGHT);
		menuStage = new Stage(new ExtendViewport(MainFrame.SCREEN_WIDTH, MainFrame.SCREEN_HEIGHT, orthoCam));
		menuMap = main.mapLoader.load("maps/MenuMap.tmx");
		main.otmr = new OrthogonalTiledMapRenderer(menuMap);
		
		((OrthographicCamera)(menuStage.getCamera())).zoom -= 0.25;
		widthWithZoom = menuStage.getWidth() * ((OrthographicCamera)(menuStage.getCamera())).zoom;
		heightWithZoom = menuStage.getHeight() * ((OrthographicCamera)(menuStage.getCamera())).zoom;
		System.out.println("MenuWidth: " + menuStage.getWidth());
		

		
		//CHARACTER STUFF
		currentPlayer = new Archer();

		//calculate the map width and height
		mapWidth = menuMap.getProperties().get("width" , Integer.class) *
				8;
		mapHeight = menuMap.getProperties().get("height", Integer.class) *
				 8;
		//menu layouts
		mainMenu = new MenuLayout();
		characterPick = new MenuLayout();
		loadingNControls = new MenuLayout();
		currentLayout = mainMenu;
		
		//buttons
		playButton = new DungeonButton("Play", passedMain);
		playButton.setWindowSize(MainFrame.TILE_SIZE * 10, MainFrame.TILE_SIZE * 4);
		//"0 + " because the tiledmap always is rendered at 0. No other way to render another way.
		playButton.setWindowCoords(0 + 11 * MainFrame.TILE_SIZE - 2,  0 + 8 * MainFrame.TILE_SIZE);
		playButton.setBorderScale(2);
		playButton.setTextColor(Color.WHITE);
		playButton.setTextSize(2);
		mainMenu.addSheet(playButton);
		
		optionsButton = new DungeonButton("Options", passedMain);
		optionsButton.setWindowSize(MainFrame.TILE_SIZE * 10, MainFrame.TILE_SIZE * 4);
		optionsButton.setWindowCoords(0 + 21 * MainFrame.TILE_SIZE + 2, 0 + 8 * MainFrame.TILE_SIZE);
		optionsButton.setBorderScale(2);
		optionsButton.setTextColor(Color.WHITE);
		optionsButton.setTextSize(2);
		mainMenu.addSheet(optionsButton);
		
		//CHARACTER PICK SCREEN
		animationTimer = 0;
		
		//archer button
		archerButton = new DungeonButton("archer", passedMain);
		archerButton.setWindowSize(widthWithZoom / 3 - 5, heightWithZoom / 4);
		archerButton.setWindowCoords(0 + 5/2, heightWithZoom - heightWithZoom / 4 - 5);//first button
		archerButton.setBorderScale(2);
		archerButton.setDisplayButtonText(false);
		characterPick.addSheet(archerButton);
		
		
		
		//wizard button
		wizardButton = new DungeonButton("wizard", passedMain);
		wizardButton.setWindowSize(widthWithZoom / 3 - 5, heightWithZoom / 4);
		wizardButton.setWindowCoords(widthWithZoom / 3 + 5/2, heightWithZoom - heightWithZoom/4 - 5);//second button
		wizardButton.setBorderScale(2);
		wizardButton.setDisplayButtonText(false);
		characterPick.addSheet(wizardButton);
		
		
		//warrior button
		warriorButton = new DungeonButton("warrior", passedMain);
		warriorButton.setWindowSize(widthWithZoom / 3 - 5, heightWithZoom / 4);
		warriorButton.setWindowCoords(widthWithZoom / 3 * 2 + 5/2, heightWithZoom - heightWithZoom/4 - 5);//third button
		warriorButton.setBorderScale(2);
		warriorButton.setDisplayButtonText(false);
		characterPick.addSheet(warriorButton);
		
		
		//PLAY button to play with selected character
		playWithCharacter = new DungeonButton("Start", passedMain);
		playWithCharacter.setWindowSize(80, 40);
		playWithCharacter.setWindowCoords(widthWithZoom - playWithCharacter.getWidth(), 0);
		playWithCharacter.setBorderScale(2);
		playWithCharacter.setTextColor(Color.WHITE);
		playWithCharacter.setTextSize(2);
		characterPick.addSheet(playWithCharacter);
		
		System.out.println(widthWithZoom);
		
		//current selection frame
		//meele label
		//ranged label
		//special label
		//meelee descripLabel
		//ranged descripLabel
		//specialDescripLabel
		//playCharButton
		
		temp = new Vector3();

		
	}
	
	@Override
	public void show() {
		// TODO Auto-generated method stub
		main.fadeIn = true;
		main.fadeOut = false;


	
		//move camera to place not viewport mmk
		menuStage.getCamera().update();
		widthWithZoom = menuStage.getWidth() * ((OrthographicCamera)(menuStage.getCamera())).zoom;
		heightWithZoom = menuStage.getHeight() * ((OrthographicCamera)(menuStage.getCamera())).zoom;
		System.out.println(widthWithZoom);
		menuStage.getViewport().apply();
		main.addInputProcessor(this);
		selectedClass = "nonelol";//no classes have been selected yet.
		
		System.out.println(((OrthographicCamera)(menuStage.getCamera())).zoom);
		
		
		menuStage.getViewport().apply();
		orthoCam.update();
	}

	@Override
	public void render(float delta) {
		//ERASE
		//ha I can make the clear color fade too cool huh
		//except I have to make them ALL FLOATS
		if(currentLayout == mainMenu){
			Gdx.gl.glClearColor(
					55f * (1f/255f) * main.fadeTracker,
					65f * (1f/255f) * main.fadeTracker,
					113f * (1f/255f) * main.fadeTracker, 1
					);
		}
		else{
			Gdx.gl.glClearColor(0, 0, 0, 1);
		}
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		// TODO Auto-generated method stub
		//ERIC
		if(currentLayout == characterPick)
		orthoCam.position.set(widthWithZoom / 2, heightWithZoom / 2, 0);
		if(currentLayout == mainMenu)
		orthoCam.position.set(mapWidth / 2 , mapHeight / 2, 0);
	


		
		//REDRAW
		//batch fade
		if(main.fadeIn || main.fadeOut){
			fadeCode(main.updateFade());
			main.fade(menuStage.getBatch());
			main.fade(main.gameFont);
			main.fade(main.otmr.getBatch());
			
		}
		menuStage.act();
		menuStage.draw();
		main.otmr.setView(orthoCam);	
		
		//if it's the main menu show the map. otherwise don't.
		if(currentLayout == mainMenu){
			main.otmr.render();
		}
		menuStage.getBatch().begin();
		currentLayout.draw((SpriteBatch) menuStage.getBatch(), main.gameFont);
		if(currentLayout == characterPick)this.actionsPerRenderCharPick();
		currentLayout.update(temp);
		menuStage.getBatch().end();
		


		

		//INPUT
		if(Gdx.input.isKeyPressed(Keys.A)){
			//playButton.setWindowCoords(orthoCam.position.x, orthoCam.position.y);

			System.out.println(menuStage.getCamera().position.toString());
			
		}
		//CALCULATE
		temp.x = Gdx.input.getX();
		temp.y = Gdx.input.getY();
		orthoCam.unproject(temp);//unproject the coords getting sent to the current layout
		currentLayout.update(temp);
		orthoCam.update(true);
		menuStage.getViewport().apply();
		
		//character select animation
		if(currentLayout == characterPick){
			animationTimer += Gdx.graphics.getDeltaTime();
			if(animationTimer >= animationTimerMax){
				//timer maxed, animate that boyeeee
				animationIndex = (animationIndex == 0)? 1 : 0;//is the index == 0? true then it's equal to 1, false then it's 0.
				animationTimer = 0;
			}
			
		}
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
		menuStage.getViewport().update(width, height, true);
		
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
		main.removeInputProcessor(this);
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}
	
	public void buttonCode(String buttonName){
		if(currentLayout == mainMenu){

			if(buttonName == "Play"){
				
				main.fadeOut = true;
				main.fadeCodename = "play";
			}
		}
		if(currentLayout == characterPick){
			if(buttonName == "archer"){
				//clicked archer button
				animationTimer = 0;
				animationIndex = 1;
				selectedClassButton = archerButton;
				selectedClassFrames = main.archerFrames;
				selectedClass = "archer";
				main.playScreen.setCharacter('a');
			}
			if(buttonName  == "wizard"){
				animationTimer = 0;
				animationIndex = 1;
				selectedClassButton = wizardButton;
				selectedClassFrames = main.wizardFrames;
				selectedClass = "wizard";
				main.playScreen.setCharacter('w');
			}
			if(buttonName == "warrior"){
				animationTimer = 0;
				animationIndex = 1;
				selectedClassButton = warriorButton;
				selectedClassFrames = main.warriorFrames;
				selectedClass = "warrior";
				main.playScreen.setCharacter('r');
			}
			if(buttonName == "Start"){//start with character
				main.fadeOut = true;
				System.out.println("fade code is start");
				main.fadeCodename = "Start";

			}
		}
	}
	public void fadeCode(String fadeCodename){
		if(fadeCodename != "null")System.out.println("Fade Codename: " + fadeCodename);
		if(currentLayout == mainMenu){

			if(fadeCodename == "play"){
				currentLayout = characterPick;
				main.fadeIn = true;
			}

		}
		if(currentLayout == characterPick){
			if(fadeCodename == "Start"){
				//play with character
				
				main.setScreen(main.playScreen);
				main.fadeIn = true;
			}
		}
	}
	public void actionsPerRenderCharPick(){
		if(selectedClass != "nonelol"){
		menuStage.getBatch().draw(selectedClassFrames[animationIndex],
				selectedClassButton.getX() + selectedClassButton.getWidth()/2 - selectedClassFrames[animationIndex].getRegionWidth()/2*2,
				archerButton.getY() + archerButton.getHeight()/2 - selectedClassFrames[animationIndex].getRegionHeight()/2*2,
				selectedClassFrames[animationIndex].getRegionWidth()*2,
				selectedClassFrames[animationIndex].getRegionHeight()*2);
		}
		if(this.selectedClass != "archer"){
			menuStage.getBatch().draw(main.archerFrames[0],
					archerButton.getX() + archerButton.getWidth()/2 - main.archerFrames[0].getRegionWidth()/2*2,
					archerButton.getY() + archerButton.getHeight()/2 - main.archerFrames[0].getRegionHeight()/2*2,
					main.archerFrames[0].getRegionWidth()*2, main.archerFrames[0].getRegionHeight() * 2);
		}
		if(this.selectedClass != "wizard"){
			menuStage.getBatch().draw(main.wizardFrames[0],
					wizardButton.getX() + wizardButton.getWidth()/2 - main.wizardFrames[0].getRegionWidth()/2*2,
					wizardButton.getY() + wizardButton.getHeight()/2 - main.wizardFrames[0].getRegionHeight()/2*2,
					main.wizardFrames[0].getRegionWidth()*2, main.wizardFrames[0].getRegionHeight()*2);
		}
		if(selectedClass != "warrior"){
			menuStage.getBatch().draw(main.warriorFrames[0],
					warriorButton.getX() + warriorButton.getWidth()/2 - main.wizardFrames[0].getRegionWidth()/2*2, 
					warriorButton.getY() + warriorButton.getHeight()/2 - main.wizardFrames[0].getRegionHeight()/2*2,
					main.warriorFrames[0].getRegionWidth()*2, main.wizardFrames[0].getRegionHeight()*2);
		}
	}

	@Override
	public boolean keyDown(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {

		temp.x = screenX;
		temp.y = screenY;
		orthoCam.unproject(temp);
		buttonCode(currentLayout.checkButtonsClickString(temp));
		
		//if x is less than half screen, then move character
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {

		
		
		
		
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		// TODO Auto-generated method stub
		return false;
	}

}
