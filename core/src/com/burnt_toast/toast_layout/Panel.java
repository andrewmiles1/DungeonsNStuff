package com.burnt_toast.toast_layout;

import java.util.LinkedList;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Panel extends Sheet{
	LinkedList<Sheet> sheets;
	LinkedList<Label> labels;
	LinkedList<SheetButton> buttons;
	private boolean visible;
	
	/**
	 * @return the visible
	 */
	public boolean isVisible() {
		return visible;
	}

	/**
	 * @param visible the visible to set
	 */
	public void setVisible(boolean passVisible) {
		visible = passVisible;
	}

	public Panel(TextureRegion passReference){
		super(passReference);
		sheets = new LinkedList<Sheet>();
		labels = new LinkedList<Label>();
		buttons = new LinkedList<SheetButton>();
	}

	public <T extends Sheet> void addItem(T passObj){
		if(passObj.getAllignment() == Sheet.Allignment.CENTERED_X){
			passObj.setWindowCoords(this.getX() + this.getWidth()/2 - passObj.getWidth()/2, passObj.getY());
			//allign to centerX
		}
		sheets.add(passObj);
		if(passObj.getAllignment() != Sheet.Allignment.CENTERED_X){
			passObj.setX(this.getX() + passObj.getX());
		}
		passObj.setY(this.getY() + passObj.getY());
		
	}
	/**
	 * the specific class of the label for allignment
	 * @param passLabel
	 * @param clazz
	 */
	public <T extends Label> void addItem(T passLabel, Class<T> clazz){
		switch(passLabel.allignment){// alligns the label based on it's allignment
		case CENTERED_X:
			passLabel.setCoords(this.getX() + this.getWidth()/2 - ((float)passLabel.getWidth())/2, passLabel.y + this.getY());
			break;
		case CENTERED_Y:
			passLabel.setCoords(passLabel.x + this.getX(), this.getY() + this.getHeight()/2 - passLabel.getHeight()/2);
			break;
		case CENTERED_XY:
			passLabel.setCoords(this.getX() + this.getWidth()/2 - passLabel.getWidth()/2,
					this.getY() + this.getHeight()/2 - passLabel.getHeight()/2);
			break;
		default:
			break;
		}
		passLabel.x += this.getX();
		passLabel.y += this.getY();
		labels.add(passLabel);
	}
	public <T extends SheetButton> void addItem(T passButton){
		switch(passButton.getAllignment()){// alligns the label based on it's allignment
		case CENTERED_X:
			passButton.setWindowCoords(this.getX() + this.getWidth()/2 - (passButton.getWidth())/2, passButton.getY() + this.getY());
			System.out.println(passButton.getButtonText() + " button was centered");
			break;
		case CENTERED_Y:
			passButton.setWindowCoords(passButton.getX() + this.getX(), this.getY() + this.getHeight()/2 - passButton.getHeight()/2);
			break;
		case CENTERED_XY:
			passButton.setWindowCoords(this.getX() + this.getWidth()/2 - passButton.getWidth()/2,
					this.getY() + this.getHeight()/2 - passButton.getHeight()/2);
			break;
		default:
			break;
		}
		passButton.setX(this.getX() + passButton.getX());
		passButton.setY(this.getY() + passButton.getY());
		buttons.add(passButton);
	}
	
	public void updateButtons(float mouseX, float mouseY){
		for(SheetButton button: buttons){
			button.update(mouseX, mouseY);
		}
	}
	
	/**
	 * returns "null" if none of the buttons were clicked, if a button was clicked
	 * it returns the text in the button
	 * @param mouseX
	 * @param mouseY
	 * @return
	 */
	public String checkButtonClick(float mouseX, float mouseY){
		for(SheetButton button: buttons){
			if(button.checkClickString(mouseX, mouseY) != "null"){
				System.out.println("Button returned not null");
				return button.checkClickString(mouseX, mouseY);
				
			}
		}
		return "null";
	}
	
	
	public void drawPanel(SpriteBatch batch){
		this.draw(batch);
		for(Sheet sheetObj: sheets){
			sheetObj.draw(batch);
		}
		for(Label lb: labels){
			lb.draw(batch);
		}
		for(SheetButton bt: buttons){
			bt.draw(batch);
		}
	}
	
	@Override
	public void  setWindowCoords(float x, float y){
		super.setWindowCoords(x, y);
		for(Sheet sh: sheets){//re-allign each item on panel
			switch(sh.getAllignment()){
			case CENTERED_X:
				sh.setWindowCoords(this.getX() + this.getWidth()/2 - sh.getWidth()/2, this.getY() + sh.getY());
				break;
			case CENTERED_XY:
				sh.setWindowCoords(this.getX() + this.getWidth()/2 - sh.getWidth()/2,
						this.getY() + this.getHeight()/2 - sh.getHeight()/2);
				break;
			case CENTERED_Y:
				sh.setWindowCoords(sh.getX(), this.getY() + this.getHeight()/2 - sh.getHeight()/2);
				break;
			default:
				//do nothing
				break;
			}
		}
		for(Label lb: labels){
			switch(lb.allignment){
			case CENTERED_X:
				lb.setCoords(this.getX() + this.getWidth()/2 - lb.getWidth()/2, this.getY() + lb.y);
				break;
			case CENTERED_XY:
				lb.setCoords(this.getX() + this.getWidth()/2 - lb.getWidth()/2,
						this.getY() + this.getHeight()/2 - lb.getHeight()/2);
				break;
			case CENTERED_Y:
				lb.setCoords(lb.x, this.getY() + this.getHeight()/2 - lb.getHeight()/2);
				break;
			default:
				//do nothing
				break;
			}
		}
		for(SheetButton bt: buttons){
			switch(bt.getAllignment()){
			case CENTERED_X:
				bt.setWindowCoords(this.getX() + this.getWidth()/2 - bt.getWidth()/2, this.getY() + bt.getY());
				break;
			case CENTERED_XY:
				bt.setWindowCoords(this.getX() + this.getWidth()/2 - bt.getWidth()/2,
						this.getY() + this.getHeight()/2 - bt.getHeight()/2);
				break;
			case CENTERED_Y:
				bt.setWindowCoords(bt.getX(), this.getY() + this.getHeight()/2 - bt.getHeight()/2);
				break;
			default:
				//do nothing
				break;
			}
		}
		
	}
	/**
	 * draws the planel with the specific labels within
	 * @param batch the Spritebatch
	 * @param clazz the class of the label
	 */
	public <T extends Label> void drawPanel(SpriteBatch batch, Class<T> clazz){
		this.draw(batch);
		for(Sheet sheetObj: sheets){
			sheetObj.draw(batch);
		}
		for(Label lb: labels){
			clazz.cast(lb).draw(batch);
		}
		for(SheetButton bt: buttons){
			bt.draw(batch);
		}
	}

}