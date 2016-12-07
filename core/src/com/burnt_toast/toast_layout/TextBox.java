package com.burnt_toast.toast_layout;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;

public class TextBox extends Sheet implements InputProcessor{

	private boolean isActive; //detirmens whether the user is typing in it or not
	private Label myText; //the current text in the text box
	private int maxCharacters; //maximum number of characters that can be typed
	//action to perform on enter
	private float cursorTimerMax = .5f;//half a second
	private float cursorAnimTimer;
	private boolean cursorShown;
	private boolean mouseActivated;//uses mouse to click to activate or something else. 
	private BiConsumer EnterAction;//the action the button takes when pressed enter.
	
	/**
	 * Constructor for Text Box. Needs a reference for the sheet,
	 * coordinates, width and height, and defaults the text to "0" defaults the
	 * allignment of the text to centered Y and to the left, and defaults the text size for the label at 10. 
	 * @param passReference
	 * @param passX
	 * @param passY
	 * @param passHeight
	 * @param passWidth
	 */
	public TextBox(TextureRegion passReference, float passX, float passY, float passHeight, float passWidth,
			OrthographicCamera passedCamera){
		super(passReference);
		this.setX(passX);
		this.setY(passY);
		this.setWindowSize(passWidth, passHeight);
		myText = new Label(this.getX() + this.getVertWidth(), this.getY() + this.getHorizHeight(), "0", Sheet.Allignment.CENTERED_Y, 10);
		cursorAnimTimer = 0;
		cursorShown = false;
	}
	
	/**
	 * draws button
	 * @param batch the batch being used
	 * @param bitFont the 
	 */
	public void draw(SpriteBatch batch, BitmapFont bitFont){
		super.draw(batch);
		
	}
	public void update(){
		cursorAnimTimer += Gdx.graphics.getDeltaTime();
		if(cursorAnimTimer > cursorTimerMax){
			cursorAnimTimer = 0; 
			cursorShown = cursorShown? false:true;//if cursorShown is true, we want to change to false, if it's false, we want to change to true.
		}
		
	}
	/**
	 * sets whether the text box is active for being typed into or not. 
	 * @param passIfActive
	 */
	public void setActive(boolean passIfActive){
		isActive = passIfActive;
	}
	

	/**
	 * checks to see if coordinates are within this text box
	 * @param passCoords is the coordinates to test
	 * @return returns true if its in bounds or false if it's not. 
	 */
	public boolean checkCoordsInside(Vector3 passCoords){
		if((passCoords.x > this.getX() && passCoords.x < this.getX() + this.getWidth()) &&
				(passCoords.y > this.getY() && passCoords.y < this.getY() + this.getHeight())){
			return true;
		}
		return false;
	}

	/**
	 * Check if the coordinates are in boundries, if they are, then returns this as a touch input. 
	 * @param touchCoords the coordinates that were touched or clicked. Remember to unproject coords. 
	 * @return this object as an input processor, RETURNS NULL IF the coords aren't in bounds.
	 */
	public TextBox checkClick(Vector3 touchCoords){
		if((touchCoords.x > this.getX() && touchCoords.x < (this.getX() + this.getWidth())) &&
				(touchCoords.y > this.getY() && touchCoords.y < (this.getY() + this.getHeight()))){//if touch coords in bounds
			isActive = true;
			return this;
		}
		if(isActive){//if it was active but they clicked out of it. 
			isActive = false;
		}
		//else do nothing.
		return null;
		
	}
	/**
	 * marks the text box as no longer active. When 
	 */
	public void loseActivity(){
		isActive = false;
	}
	@Override
	public boolean keyDown(int keycode) {
		// TODO Auto-generated method stub
		if(isActive){//if I'm active
			if(keycode == Keys.BACKSPACE){
				//removes the last letter of thing. sub string of 0 to length subtracted by 2. 
				myText.setText(myText.text.substring(0, (myText.text.length() - 2)));//removes the last letter of thing. sub string of 0 to length. 
			}
			myText.setText(myText.text + Keys.toString(keycode));
			return true;
		}
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
		// TODO Auto-generated method stub
		
			
		
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		// TODO Auto-generated method stub
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
