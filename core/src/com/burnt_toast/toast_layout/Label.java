package com.burnt_toast.toast_layout;

import java.util.LinkedList;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Label {
	public float x;
	public float y;
	public String text;
	Sheet.Allignment allignment;
	public GlyphLayout tempGlyphLayout;
	public float size;
	protected Color myColor;
	public float textWrapWidth;
	private LinkedList<String> textList;
	private LinkedList<Character> textColorList;
	private int splitFlagBeg;//used to split the string into parts
	private float drawingXCursor;
	private float drawingYCursor;
	
	//DRAW FUNCTION IS SPECIFIC TO PROJECT BECAUSE OF FONT
	
	public Label(float passX, float passY, String passText, Sheet.Allignment passAllign,
			float passSize){
		x = passX;
		y = passY;
		text = passText;
		allignment = passAllign;
		size = passSize;
		myColor = Color.BLACK;
		textWrapWidth = 0;
		textList = new LinkedList<String>();
		textColorList = new LinkedList<Character>();
		tempGlyphLayout  = new GlyphLayout();
	}
	public Label(){}
	public void setCoords(float passX, float passY){
		x = passX;
		y = passY;
	}
	public void setText(String passText){
		text = passText;
	}
	public void setAllignment(Sheet.Allignment passAllign){
		allignment = passAllign;
	}
	public void setSize(float passSize){
		size = passSize;
	}
	public void setWrapWidth(float passWidth){
		textWrapWidth = passWidth;
	}
	public float getWrapWidth(){
		return textWrapWidth;
	}
	/**
	 * this method is to be overridden by a subclass because of game specific fonts
	 * @param batch the used spritebatch
	 */
	public void draw(SpriteBatch batch){
		//nothing
	}
	public void drawLabel(SpriteBatch batch, BitmapFont bitmap){
		splitString();//split the string according to color
		drawingXCursor = 0;
		drawingYCursor = 0;
		bitmap.getData().setScale(size);
		for(String str: textList){
			changeColor(textList.indexOf(str), bitmap);
			for(int i = 0; i < str.split(" ").length; i ++){
				tempGlyphLayout.setText(bitmap, str);
				if(drawingXCursor + tempGlyphLayout.width >= textWrapWidth){
					drawingYCursor += bitmap.getLineHeight();//wrap to next line
					drawingXCursor = 0;//restart at begining of line
				}
				bitmap.draw(batch, str.split(" ")[i] + " ", x + drawingXCursor, y - drawingYCursor);
				tempGlyphLayout.setText(bitmap, str.split(" ")[i] + " ");
				drawingXCursor += tempGlyphLayout.width;
			}
		}
		bitmap.setColor(Color.BLACK);//reset the font
		bitmap.getData().setScale(size);
	}
	/**
	 * the split string of "text" is put into the "textList" along with the colors in "textColorList"
	 */
	public void splitString(){
		textList.clear();
		textColorList.clear();
		splitFlagBeg = 0;
		for(int i = 0; i < text.length();i++){
			if(text.charAt(i) == '|'){
				if(splitFlagBeg != i){//add normal text
					textList.add(text.substring(splitFlagBeg,  i));//add whatever text is before
					textColorList.add('0');//no color change
				}
				splitFlagBeg = i + 3;
				textColorList.add(text.charAt(i + 1));
			}
			if(text.charAt(i) == ']'){
				textList.add(text.substring(splitFlagBeg, i));
				splitFlagBeg = i + 1;
			}
			else if(i == (text.length() - 1)){
				textList.add(text.substring(splitFlagBeg, i));
				textColorList.add('0');
			}
		}
		if(textList.isEmpty()){//if there was no color flags in text
			textList.add(text);
		}
	}
	public void changeColor(int index, BitmapFont font){
		switch(textColorList.get(index)){
		case '0':
			font.setColor(myColor);
			break;
		case 'W':
			font.setColor(Color.WHITE);
			break;
		case 'R':
			font.setColor(Color.RED);
			break;
		case 'A'://gray
			font.setColor(Color.GRAY);
			break;
		case 'Y'://yellow
			font.setColor(Color.YELLOW);
			break;
		}
	}
	public float getWidth(){
		//override getwidth()
		return 1;
	}
	public float getHeight(){
		//override getHeight()
		return 1;
	}

}
