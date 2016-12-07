package com.burnt_toast.dungeons_n_stuff;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ExtendViewport;

public class PlayScreen implements Screen, InputProcessor{

	private MainFrame main;
	private OrthogonalTiledMapRenderer otmr;
	private TiledMap mazeMap;
	private MapCreationTool mapTool;
	private static int[][] collisionMap;
	private Stage playStage;
	private Stage hudStage;
	private OrthographicCamera orthoCam;
	private boolean pause;
	
	private TiledMapTileLayer map;
	//hud
	private TextureRegion healthBar;
	private TextureRegion healthBorder;
	private TextureRegion currentWeapon;//never initialized! on purpose
	
	private float widthWithZoom;
	private float heightWithZoom;
	
	//moving character drag stuff
	float dragDifX;
	private float dragDifY;
	private Vector2 dragCoordsThisFrame;
	private Vector2 dragCoordsLastFrame;
	private float dragChangeX;
	private float dragChangeY;
	private float dragDeadZoneSame;
	private float dragDeadZoneOpposite;
	
	private Vector2 touchCoordsTemp;
	
	private Character currentPlayer;
	
	public PlayScreen(MainFrame passedMain){
		main = passedMain;
		orthoCam = new OrthographicCamera(MainFrame.SCREEN_WIDTH, MainFrame.SCREEN_HEIGHT);
		playStage = new Stage(new ExtendViewport(MainFrame.SCREEN_WIDTH, MainFrame.SCREEN_HEIGHT, orthoCam));
		hudStage = new Stage(new ExtendViewport(MainFrame.SCREEN_WIDTH, MainFrame.SCREEN_HEIGHT));
		mazeMap = main.mapLoader.load("maps/rogueMap.tmx");
		otmr = new OrthogonalTiledMapRenderer(mazeMap);
		pause = false;
		
		collisionMap = new int[50][50];
		
		//DRAG STUFF
		dragCoordsThisFrame = new Vector2();
		dragCoordsLastFrame = new Vector2();
		dragDeadZoneSame = 10;//just a beginners estimate
		dragDeadZoneOpposite = 3;
		touchCoordsTemp = new Vector2();
		
		//MAP CREATION
		mapTool = new MapCreationTool(((TiledMapTileLayer)mazeMap.getLayers().get(0)),
				mazeMap.getTileSets().getTileSet(0));
		
		//HUD
		healthBar = new TextureRegion(MainFrame.mainTileset, 44, 49, 31, 6);
		healthBorder = new TextureRegion(MainFrame.mainTileset, 43, 41, 33, 8);

	}
	
	
	
	@Override
	public void show() {
		// TODO Auto-generated method stub

		otmr.setMap(mazeMap);
		main.fadeIn = true;
		main.fadeOut = false;
		orthoCam.update();
		currentPlayer.setPosition(3 * MainFrame.TILE_SIZE, 3 * MainFrame.TILE_SIZE);
		currentPlayer.setDirection('u');//up at default.
		main.addInputProcessor(this);
		loadMap();
		
	}

	@Override
	public void render(float delta) {
		// TODO Auto-generated method stub
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		if(main.fadeIn || main.fadeOut){
			//if fading
			main.updateFade();
			main.fade(playStage.getBatch());
			main.fade(otmr.getBatch());
		}
		
		//ERIC
		otmr.setView(orthoCam);
		playStage.act();
		playStage.draw();
		hudStage.act();
		hudStage.draw();
		
		otmr.render();
		otmr.getBatch().begin();
		otmr.renderTileLayer((TiledMapTileLayer)mazeMap.getLayers().get(0));
		otmr.getBatch().end();
		
		playStage.getBatch().begin();
		currentPlayer.draw((SpriteBatch)playStage.getBatch());
		main.gameFont.draw(playStage.getBatch(), "X : " + dragChangeX + "|Y: " + dragChangeY, 0, 0);
		playStage.getBatch().end();
		
		hudStage.getBatch().begin();
		hudStage.getBatch().draw(healthBorder, hudStage.getWidth()/2, hudStage.getHeight()-35,
				healthBorder.getRegionWidth() * 3, healthBorder.getRegionHeight() * 3);
		hudStage.getBatch().end();
		
		
		
		//INPUT
		
		//CALCULATE
		widthWithZoom = playStage.getWidth() * ((OrthographicCamera)(playStage.getCamera())).zoom;
		heightWithZoom = playStage.getHeight() * ((OrthographicCamera)(playStage.getCamera())).zoom;
		currentPlayer.update();
		orthoCam.position.set(currentPlayer.getX(), currentPlayer.getY(), 0);
		
		
		
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
		playStage.getViewport().update(width, height, true);
		orthoCam.update();
		
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

	public static float hash(int x, int y){
		return (float) (x * Math.sqrt(y));
	}
	public void loadMap(){//mapWidth = menuMap.getProperties().get("width" , Integer.class)
		//load collision:
		for(int i = 0; i < 49;i++){
			for(int j = 0; j < 49;j++){
				if(((TiledMapTileLayer)mazeMap.getLayers().get(2)).getCell(i, j) != null){
					if(((TiledMapTileLayer)mazeMap.getLayers().get("collision")).getCell(i, j).getTile().getId() == 32){
						//tile is red in collision map
						collisionMap[i][j] = 1;//passable
					}//end if red
					else{
						collisionMap[i][j] = 0;//unpassable
						System.out.println(((TiledMapTileLayer)mazeMap.getLayers().get("collision")).getCell(i, j).getTile().getId());
						System.out.println(mazeMap.getProperties().get("width" , Integer.class) + "ALKSJDF");
					}//end else
				}//endif null
				else{
					collisionMap[i][j] = 0;//unpassable
				}
			}
		}//end nested for loop for loading collision
		System.out.println(collisionMap[0].length-1);
		for(int i = 0; i < collisionMap[0].length-1;i++){
			for (int j = 0; j < collisionMap[0].length-1;j++){
				System.out.print(collisionMap[i][j]);
			}
			System.out.println();
		}
		this.collisionMap = mapTool.prepareMap(10);
	}
	public void buttonCode(String buttonName){
		
	}
	
	//check collision
	public static boolean checkCollisionAt(float x, float y,float width, float height){
		if(collisionMap[round(x, 8, false)/8][round(y,8, false)/8] == 1 |//bottom left corner
				collisionMap[round(x, 8, false)/8][round(y + height, 8, false)/8] == 1 |//top left corner
				collisionMap[round(x + width, 8, false)/8][round(y + height, 8, false)/8] == 1 |//top right corner
				collisionMap[round(x + width, 8, false)/8][round(y, 8, false)/8] == 1){
			System.out.println("Collide in Check");
			return true;}//bottom right corner. return true if collide
		else{return false;}
	}
	/**
	 * rounds num to the round number with an option to round up or down. (true = round up::false = round down)
	 * same rounding function as round(num, round) just with option of up or down
	 * @param num number to round
	 * @param round factor to round to
	 * @param roundDirection true = round up::false = round down
	 * @return
	 */
	public static int round(float num, int round, boolean roundDirection){
		if(roundDirection){//round num up
			return (int) (num + (round - (num % round)));//return the uppper round
		}
		else{//round num down
			return (int) (num - (num % round));//return the lower round
		}
	}
	public void setCharacter(char character){
		switch(character){
		case 'a':
			//archer
			currentPlayer = new Archer();
			currentWeapon = main.arrowStages[0];
			break;
		case 'w':
			//wizard
			currentPlayer = new Wizard();
			currentWeapon = main.ringStages[0];
			break;
		case 'r':
			//warrior
			currentPlayer = new Warrior();
			currentWeapon = main.swordStages[0];
			break;
		default:
			//archer
			currentPlayer = new Archer();
			currentWeapon = main.arrowStages[0];
		}
	}

	@Override
	public boolean keyDown(int keycode) {
		// TODO Auto-generated method stub
		if(keycode == Keys.SPACE){
			currentPlayer.setPosition(0, 0);
			return true;
		}
		else if(keycode == Keys.W){
			currentPlayer.setDirection('u');
			currentPlayer.setIfMoving(true);
			return true;
		}
		else if(keycode == Keys.A){
			currentPlayer.setDirection('l');
			currentPlayer.setIfMoving(true);
			return true;
		}
		else if(keycode == Keys.S){
			currentPlayer.setDirection('d');
			currentPlayer.setIfMoving(true);
			return true;
		}
		else if(keycode == Keys.D){
			currentPlayer.setDirection('r');
			currentPlayer.setIfMoving(true);
			return true;
		}
		return false;
	}


	@Override
	public boolean keyUp(int keycode) {
		// TODO Auto-generated method stub
		if(keycode == Keys.A ||keycode == Keys.S || keycode == Keys.D || keycode == Keys.W){
			if(!Gdx.input.isButtonPressed(Keys.A) &&
			!Gdx.input.isButtonPressed(Keys.S) &&
			!Gdx.input.isButtonPressed(Keys.D) &&
			!Gdx.input.isButtonPressed(Keys.W)){
				currentPlayer.setIfMoving(false);
			}

		}
		return false;
	}


	@Override
	public boolean keyTyped(char character) {
		// TODO Auto-generated method stub
		return false;
	}


	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		touchCoordsTemp.x = screenX;
		touchCoordsTemp.y = screenY;
		playStage.getViewport().unproject(touchCoordsTemp);
		System.out.println(screenX);
		if(screenX < Gdx.graphics.getWidth() / 2){
			//if its on the left side,
			currentPlayer.setIfMoving(true);//start moving
			return true;
		}
		
		return false;
	}


	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		touchCoordsTemp.x = screenX;
		touchCoordsTemp.y = screenY;
		playStage.getViewport().unproject(touchCoordsTemp);
		if(touchCoordsTemp.x < Gdx.graphics.getWidth() / 2){
			//if its on the left side,
			currentPlayer.setIfMoving(false);//stop moving, 
			dragCoordsThisFrame.x = -1;
			dragCoordsThisFrame.y = -1;
		}
		return false;
	}


	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		if(dragCoordsLastFrame.x == -1){
			dragCoordsLastFrame.x = dragCoordsThisFrame.x;
			dragCoordsLastFrame.y = dragCoordsThisFrame.y;
			dragCoordsThisFrame.x = screenX; dragCoordsThisFrame.y = screenY;//set this frame's
			hudStage.getViewport().unproject(dragCoordsThisFrame);
		}
		else{
			dragCoordsLastFrame.x = dragCoordsThisFrame.x;
			dragCoordsLastFrame.y = dragCoordsThisFrame.y;
			dragCoordsThisFrame.x = screenX; dragCoordsThisFrame.y = screenY;//set this frame's
			hudStage.getViewport().unproject(dragCoordsThisFrame);
			dragDifX = dragCoordsThisFrame.x - dragCoordsLastFrame.x;//difference of x drags.
			dragDifY = dragCoordsThisFrame.y - dragCoordsLastFrame.y;//difference of y drags.
		}
		System.out.println(dragDifX + ", " + dragDifY);

		if (dragCoordsThisFrame.x > widthWithZoom/2){
			//currentPlayer.setIfMoving(false);//they dragged out of moving
			//System.out.println(screenX + ", " + screenY + ", " + (widthWithZoom/2));
		}
		if(currentPlayer.getIfMoving() == false && dragCoordsThisFrame.x < widthWithZoom/2){
			//if they're not moving and they dragged into the moving, then move.
			//currentPlayer.setIfMoving(true);
		}
		
		if(dragCoordsThisFrame.x < Gdx.graphics.getWidth()/2){//in moving bounds.
			/*if(currentPlayer.getDirection() == 'r'){//GOING RIGHT
				if((dragDifX + dragChangeX) > dragChangeX && Math.abs(dragChangeY) < dragDeadZoneOpposite){//we're going right. minus because diff would be negative
					//if we're going right then reset if it's negative
					//if(dragChangeX < 0)dragChangeX = 0;
					dragChangeX += dragDifX;//just add it I guess. Don't really do anything.
					dragChangeY += dragDifY;
				}
				else{//we're going right but they dragged otherwize or equal to
					if(dragChangeX > 0)dragChangeX = 0; //deal with it we might go another way.
					dragChangeX += dragDifX;//
					if(dragChangeX < (dragDeadZoneSame * -1))
					{
						currentPlayer.setDirection('l');//lol yeah we goin' left
						dragChangeY = 0;//reset the other so it doesn't trip.
					}
					
					dragChangeY += dragDifY;//add to y any differences. even if goes up or down no big deal
					if (dragChangeY > dragDeadZoneOpposite){//if dragging up
						currentPlayer.setDirection('u');//we goin' up
						dragChangeX = 0;
					}
					else if(dragChangeY < (dragDeadZoneOpposite*-1)){//if dragging down
						currentPlayer.setDirection('d');//we goin' down
						dragChangeX = 0;
					}
				}
			}//END GOING RIGHT*/
			if(currentPlayer.getDirection() == 'r'){//GOING RIGHT
				if(Math.abs(dragDifY) >= Math.abs(dragDifX)){//if y is bigger
					if(Math.abs(dragDifX) > dragDeadZoneOpposite){//if y is greater than dead zone
						if(dragDifY > 0){
							//going up
							currentPlayer.setDirection('u');
						}
						else{
							currentPlayer.setDirection('d');
						}
					}
				}
				else{
					if(dragDifX < dragDeadZoneSame*-1)
						//going left
						currentPlayer.setDirection('l');
				}
				
			}//END GOING RIGHT
			if(currentPlayer.getDirection() == 'l'){//GOING LEFT
				if(Math.abs(dragDifY) >= Math.abs(dragDifX)){//if y diff is bigger than x
					if(Math.abs(dragDifY) > dragDeadZoneOpposite){
						if(dragDifY > 0){
							//going up
							currentPlayer.setDirection('u');//set up
						}
						if(dragDifY < 0){
							//going down
							currentPlayer.setDirection('d');
						}
					}
				}
				else{
					if(dragDifX > dragDeadZoneSame){
						//going right
						currentPlayer.setDirection('r');
					}
				}
				
			}//END GOING LEFT
			if(currentPlayer.getDirection() == 'u'){//GOING UP
				if(Math.abs(dragDifX) >= Math.abs(dragDifY)){//if y diff is bigger than x
					if(Math.abs(dragDifX) > dragDeadZoneOpposite){
						if(dragDifX > 0){
							//going right
							currentPlayer.setDirection('r');//set right
						}
						if(dragDifX < 0){
							//going left
							currentPlayer.setDirection('l');
						}
					}
				}
				else{
					if(dragDifY < dragDeadZoneSame*-1){
						//going down
						currentPlayer.setDirection('d');
					}
				}
				
			}//END GOING UP
			if(currentPlayer.getDirection() == 'd'){//GOING DOWN
				if(Math.abs(dragDifX) >= Math.abs(dragDifY)){//if y diff is bigger than x
					if(Math.abs(dragDifX) > dragDeadZoneOpposite){
						if(dragDifX > 0){
							//going right
							currentPlayer.setDirection('r');//set right
						}
						if(dragDifX < 0){
							//going left
							currentPlayer.setDirection('l');
						}
					}
				}
				else{
					if(dragDifY > dragDeadZoneSame){
						//going up
						currentPlayer.setDirection('u');
					}
				}
				
			}//END GOING DOWN
		}//END IF IN MOVING BOUNDS
		
		
		
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
		orthoCam.zoom += 0.1;
		orthoCam.update();
		return false;
	}
	

}
