package com.burnt_toast.toast_layout;

import java.util.LinkedList;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.Rectangle;

public class SheetButton extends Sheet{
	private TextureRegion normalReference;
	private TextureRegion hoveredReference;
	private GlyphLayout glyph;
	protected String buttonText;
	protected boolean displayButtonText;
	protected float buttonTextWidth;
	protected float buttonTextHeight;
	private boolean hovered;
	private Rectangle buttonRect;
	protected Color myTextColor;
	protected float myTextSize;//the size of the text
	private LinkedList myList;
	private BiConsumer<Object, Object> clickAction;//action to complete when clicked. 
	private static Color textColorTemp;
	
	/**
	 * just passing the reference texture region. hovered reference 
	 * is none, and pass button text is defaulted to "button"
	 * @param passReference the texture region reference
	 */
	public SheetButton(TextureRegion passReference){
		super(passReference);
		buttonText = "Button";
		hovered = false;
		buttonRect = new Rectangle();
		myList = new LinkedList<Integer>();
		glyph = new GlyphLayout();
		displayButtonText = true;
	}
	
	public SheetButton(TextureRegion passReference, TextureRegion passHoveredReference, String passButtonTxt){
		super(passReference);
		hoveredReference = passReference;
		buttonText = passButtonTxt;
		hovered = false;
		buttonRect = new Rectangle();
		displayButtonText = true;
	}
	
	@Override
	public void setWindowSize(float passedWidth, float passedHeight){
		super.setWindowSize(passedWidth, passedHeight);
		buttonRect.setSize(passedWidth, passedHeight);
	}
	@Override
	public void setWindowCoords(float passedX, float passedY){
		super.setWindowCoords(passedX, passedY);
		buttonRect.setPosition(passedX, passedY);
	}
	

	
	public void setHovered(boolean passHovered){
		hovered = passHovered;
	}
	
	public void setButtonText(String passButtonText){
		buttonText = passButtonText;
	}
	public String getButtonText(){
		return buttonText;
	}
	public void setTextColor(Color passedColor){
		myTextColor = passedColor;
	}
	public void setTextSize(float passSize){
		myTextSize = passSize;
	}
	public void setHoveredReference(TextureRegion passHover){
		hoveredReference = passHover;
	}
	
	/**
	 * this method must be overloaded to include the specific font to the game, other wise there will be no font. 
	 * @batch the Sprite Batch that's used for drawing the button
	 * @drawFont the font that you're drawing with. 
	 */
	public void draw(SpriteBatch batch, BitmapFont drawFont){
		if(hovered && hoveredReference != null){
			super.draw(batch, hoveredReference);
		}
		else{//non hovered
			super.draw(batch);
		}
		//if drawing a certain color, then draw it! 
		if(myTextColor != null){
			textColorTemp = drawFont.getColor();
			drawFont.setColor(myTextColor);
		}
		//set glyph for width and height getting
		glyph.setText(drawFont, this.buttonText);
		//draw that puppy like there's no tomorrow right smack dab middle
		if(displayButtonText){
			drawFont.draw(batch, this.buttonText, this.getX() + this.getWidth()/2 - glyph.width/2,
					this.getY() + this.getHeight()/2 + glyph.height/2);// IT'S PLUS BECAUSE IT DRAWS FROM TOP RIGHT
		}
		//returns the font color back to original
		drawFont.setColor(textColorTemp);
		
	}
	
	@Override
	public void setX(float passX){
		super.setX(passX);
		buttonRect.x = passX;
	}
	@Override
	public void setY(float passY){
		super.setY(passY);
		buttonRect.y = passY;
	}
	public void setDisplayButtonText(boolean passIfDisplay){displayButtonText = passIfDisplay;}
	public void update(float mouseX, float mouseY){
		
		if(buttonRect.contains(mouseX, mouseY)){
			hovered = true;
		}
		else{
			hovered = false;
		}
	}
	
	public <A, B> void  setButtonAction(BiConsumer<Object, Object> passAction){
		clickAction = passAction;//BAM double llambdas. 
		//figure that out.
		//jk, it's just so I don't have to hold the generics in the object. That way it just deals with the
		//generics in the function.
	}
	/**
	 * used to check click input with the button
	 * @param mouseX
	 * @param mouseY
	 * @return
	 */
	public boolean checkClick(float mouseX, float mouseY){
		if(buttonRect.contains(mouseX,  mouseY)){
			clickAction.accept(null, null);
			return true;
		}
		return false;
		
	}
	/**
	 * used for a panel check on buttons, returns the button text if clicked
	 * at the passed coordinates
	 * @param mouseX
	 * @param mouseY
	 * @return
	 */
	public String checkClickString(float mouseX, float mouseY){
		if(buttonRect.contains(mouseX, mouseY)){
			return this.buttonText;
		}
		else{
			return "null";
		}
	}

}
