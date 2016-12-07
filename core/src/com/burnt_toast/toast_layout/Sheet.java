package com.burnt_toast.toast_layout;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Sheet {
	private int referenceHorizWidth;
	private int referenceHorizHeight;
	private int referenceVertWidth;
	private int referenceVertHeight;
	private int referenceCornerWidth;
	private int referenceCornerHeight;
	private int referenceMiddleWidth;
	private int referenceMiddleHeight;
	private float scaleNum;//amount to scale the borders
	
	private Allignment personalAllignment;
	
	private float height;
	private float width;
	private float x;
	private float y;
	
	public static enum Allignment{
		CENTERED_X, CENTERED_Y, CENTERED_XY, CUSTOM
	}
	
	private TextureRegion reference;//referenced img
	private TextureRegion referenceTemp;//used for drawing the window
	
	public Sheet(){
		reference = new TextureRegion();
		referenceTemp = new TextureRegion();
	}
	public Sheet(TextureRegion passReference){
		reference = passReference;
		referenceTemp = new TextureRegion();
		scaleNum = 1;//default no scale
	}
	public Sheet(int num){System.out.println("num test");}
	
	
	
	//all the get functions
	public int getCornWidth(){return referenceCornerWidth;}
	public int getCornHeight(){return referenceCornerHeight;}
	public int getHorizWidth(){return referenceHorizWidth;}
	public int getHorizHeight(){return referenceHorizHeight;}
	public int getVertWidth(){return referenceVertWidth;}
	public int getVertHeight(){return referenceVertHeight;}
	public int getMiddleWidth(){return referenceMiddleWidth;}
	public int getMiddleHeight(){return referenceMiddleHeight;}
	
	public Allignment getAllignment(){return personalAllignment;}
	public void setAllignment(Allignment allign){
		personalAllignment = allign;
	}
	
	/**
	 * sets the reference vertical sides height and width
	 * @param passWidth
	 * @param passHeight
	 */
	public void setRefVert(int passWidth, int passHeight){
		referenceVertWidth = passWidth;
		referenceVertHeight = passHeight;
	}
	
	/**
	 * sets the reference horizontal sides width and height
	 * @param passWidth
	 * @param passHeight
	 */
	public void setRefHoriz(int passWidth, int passHeight){
		referenceHorizWidth = passWidth;
		referenceHorizHeight = passHeight;
	}
	
	/**
	 * sets the reference corner's width and height
	 * @param passWidth
	 * @param passHeight
	 */
	public void setRefCorn(int passWidth, int passHeight){
		referenceCornerWidth = passWidth;
		referenceCornerHeight = passHeight;
	}
	
	/**
	 * sets the reference corner's width and height
	 * @param passWidth
	 * @param passHeight
	 */
	public void setRefMiddle(int passWidth, int passHeight){
		referenceMiddleWidth = passWidth;
		referenceMiddleHeight = passHeight;
	}
	
	public void setWindowSize(float passedWidth, float passedHeight){
		width = passedWidth;
		height = passedHeight;
		
	}
	
	public void setRefImg(TextureRegion passRef){
		reference = passRef;
	}
	public float getWidth(){return width;}
	public float getHeight(){return height;}
	
	public void setWindowCoords(float passedX, float passedY){
		x = passedX;
		y = passedY;
	}
	public void setBorderScale(float passScale){
		scaleNum = passScale;
	}
	public float getBorderScale(){return scaleNum;}
	
	public float getX(){return x;}
	public float getY(){return y;}
	public void setX(float passX){this.x = passX;}
	public void setY(float passY){this.y = passY;}
	
	/**
	 * gets the Reference Image for the button. 
	 * Can be used to really tell if it has the right reference image.
	 * @return returns reference image of the sheet.
	 */
	public TextureRegion getRefImage(){
		return reference;
	}
	

	
	public void update(){
		//do nothing, nothing to update. Only here for inheritance reasons
	}
	public void draw(SpriteBatch batch){
		
		//change reference to bottom left corner 
		referenceTemp.setRegion(reference, 0, referenceCornerHeight + referenceVertHeight, referenceCornerWidth, referenceCornerHeight);
		batch.draw(referenceTemp, x, y, referenceCornerWidth * scaleNum, referenceCornerHeight * scaleNum);
		
		//change reference to left side
		referenceTemp.setRegion(reference, 0, referenceCornerHeight, referenceVertWidth - 1, referenceVertHeight - 1);
		batch.draw(referenceTemp, x, y + referenceCornerHeight * scaleNum,
				referenceVertWidth * scaleNum, height - (referenceCornerHeight * 2 * scaleNum));//draw side
		
		//change reference to bottom side * draws wrong color
		referenceTemp.setRegion(reference, referenceCornerWidth, referenceHorizHeight + referenceMiddleHeight, referenceHorizWidth, referenceHorizHeight);
		batch.draw(referenceTemp, x + referenceCornerWidth*scaleNum, y, 
				width - (referenceHorizWidth * scaleNum), referenceHorizHeight * scaleNum);//draw
		
		//for now, until I think of a better way to implement a middle
		//it's just the middle color found just to the right and left below
		//the top left corner. :'/
		//change reference to middle * 
		referenceTemp.setRegion(reference, referenceCornerWidth, referenceCornerHeight, 1, 1);
		batch.draw(referenceTemp, x + referenceCornerWidth*scaleNum, y + referenceCornerHeight*scaleNum,
				width - (referenceVertWidth * 2 * scaleNum), (height - (referenceHorizHeight * 2 * scaleNum)));//draw
		
		//change temp to Top Left corner 
		referenceTemp.setRegion(reference, 0, 0, referenceCornerWidth, referenceCornerHeight);
		batch.draw(referenceTemp, x, y + (height - referenceCornerHeight*scaleNum),
				referenceCornerWidth * scaleNum, referenceCornerHeight * scaleNum);//draw
		
		//change temp to Bottom Right corner 
		referenceTemp.setRegion(reference, referenceCornerWidth + referenceHorizWidth, referenceCornerHeight + referenceVertHeight, referenceCornerWidth, referenceCornerHeight);
		batch.draw(referenceTemp, x + (width - referenceCornerWidth*scaleNum), y, referenceCornerWidth * scaleNum, referenceCornerHeight * scaleNum);//no strech on corner draw
		
		//change temp to Top Side *
		referenceTemp.setRegion(reference, referenceCornerWidth, 0, referenceHorizWidth, referenceHorizHeight);
		batch.draw(referenceTemp, x + referenceCornerWidth * scaleNum, y + height - referenceHorizHeight * scaleNum,
				width - (referenceCornerWidth * 2 * scaleNum), referenceHorizHeight * scaleNum);//draw
		
		//change temp to Right Side *
		referenceTemp.setRegion(reference, referenceVertWidth + referenceMiddleWidth, referenceCornerHeight, referenceVertWidth, referenceVertHeight);
		batch.draw(referenceTemp, x + (width - referenceVertWidth*scaleNum), y + referenceCornerHeight*scaleNum,
				referenceVertWidth * scaleNum, height - (referenceCornerHeight * 2 * scaleNum));//draw with stretch
		
		//change temp to Top Right corner *
		referenceTemp.setRegion(reference, referenceCornerWidth + referenceHorizWidth, 0,
				referenceCornerWidth, referenceCornerHeight);
		batch.draw(referenceTemp, x + width - referenceCornerWidth*scaleNum, y + height - referenceCornerHeight*scaleNum,
				referenceCornerWidth * scaleNum, referenceCornerHeight * scaleNum);
	}
	
	/**
	 * draws with a custom reference
	 * @param batch the SpriteBatch
	 * @param customReference the desired custom reference
	 */
	public void draw(SpriteBatch batch, TextureRegion customReference){
		//change customReference to bottom left corner *
		referenceTemp.setRegion(customReference, 0, referenceCornerHeight + referenceVertHeight, referenceCornerWidth, referenceCornerHeight);
		batch.draw(referenceTemp, x, y, referenceCornerWidth * scaleNum, referenceCornerHeight * scaleNum);
		
		//change customReference to left side
		referenceTemp.setRegion(customReference, 0, referenceCornerHeight, referenceVertWidth, referenceVertHeight);
		batch.draw(referenceTemp, x, y + referenceCornerHeight * scaleNum,
				referenceVertWidth * scaleNum, height - (referenceCornerHeight * 2 * scaleNum));//draw side
		
		//change customReference to bottom side *
		referenceTemp.setRegion(customReference, referenceCornerWidth, referenceCornerHeight + 1/*middle height*/, referenceHorizWidth, referenceHorizHeight);
		batch.draw(referenceTemp, x + referenceCornerWidth*scaleNum, y, 
				width - (referenceCornerWidth * 2 * scaleNum), referenceHorizHeight * scaleNum);//draw
		
		//change customReference to middle *
		referenceTemp.setRegion(customReference, referenceCornerWidth, referenceCornerHeight, 1, 1);
		batch.draw(referenceTemp, x + referenceCornerWidth*scaleNum, y + referenceCornerHeight*scaleNum,
				width - (referenceVertWidth * 2 * scaleNum), (height - (referenceHorizHeight * 2 * scaleNum)));//draw
		
		//change temp to Top Left corner *
		referenceTemp.setRegion(customReference, 0, 0, referenceCornerWidth, referenceCornerHeight);
		batch.draw(referenceTemp, x, y + (height - referenceCornerHeight*scaleNum),
				referenceCornerWidth * scaleNum, referenceCornerHeight * scaleNum);//draw
		
		//change temp to Bottom Right corner *
		referenceTemp.setRegion(customReference, referenceCornerWidth + referenceHorizWidth, referenceCornerHeight + referenceVertHeight, referenceCornerWidth, referenceCornerHeight);
		batch.draw(referenceTemp, x + (width - referenceCornerWidth*scaleNum), y, referenceCornerWidth * scaleNum, referenceCornerHeight * scaleNum);//no strech on corner draw
		
		//change temp to Top Side *
		referenceTemp.setRegion(customReference, referenceCornerWidth, 0, referenceHorizWidth, referenceHorizHeight);
		//referenceTemp.setRegion(customReference, referenceCornerWidth, 1/*middle size*/ + referenceHorizHeight, referenceHorizWidth, referenceHorizHeight);
		batch.draw(referenceTemp, x + referenceCornerWidth * scaleNum, y + height - referenceCornerHeight * scaleNum,
				width - (referenceCornerWidth * 2 * scaleNum), referenceHorizHeight * scaleNum);//draw
		
		//change temp to Right Side *
		referenceTemp.setRegion(customReference, referenceCornerWidth + referenceHorizWidth, referenceCornerHeight, referenceVertWidth, referenceVertHeight);
		batch.draw(referenceTemp, x + (width - referenceVertWidth*scaleNum), y + referenceCornerHeight*scaleNum,
				referenceVertWidth * scaleNum, height - (referenceCornerHeight * 2 * scaleNum));//draw with stretch
		
		//change temp to Top Right corner *
		referenceTemp.setRegion(customReference, referenceCornerWidth + referenceHorizWidth, 0,
				referenceCornerWidth, referenceCornerHeight);
		batch.draw(referenceTemp, x + width - referenceCornerWidth*scaleNum, y + height - referenceCornerHeight*scaleNum,
				referenceCornerWidth * scaleNum, referenceCornerHeight * scaleNum);
	}

}
