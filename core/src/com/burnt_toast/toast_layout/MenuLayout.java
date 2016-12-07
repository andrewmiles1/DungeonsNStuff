package com.burnt_toast.toast_layout;

import java.util.LinkedList;
import java.util.function.Consumer;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;


public class MenuLayout {
	
	public LinkedList<Sheet> sheetsInLayout;
	
	public LinkedList<Consumer<? extends Object>> anythingElsePerRender;//other stuff that's not a sheet. 
	//like a Tiled Map Renderer. does it per Render. The next one does it per update
	public LinkedList<Consumer<? extends Object>> anythingElsePerUpdate;
	
	public MenuLayout(){
		sheetsInLayout = new LinkedList<Sheet>();
		anythingElsePerRender = new LinkedList<Consumer<? extends Object>>();
		anythingElsePerUpdate = new LinkedList<Consumer<? extends Object>>();
	}
	
	/**
	 * adds anything else that needs to be drawn or happen per frame when
	 * this layout is the current layout
	 * @param action the action to be done per frame
	 */
	public void addActionPerRender(Consumer<? extends Object> renderAction){
		anythingElsePerRender.add(renderAction);
	}
	/**
	 * adds anything else that needs to happen per frame in update when
	 * this layout is the current layout
	 * @param updateAction the action to be done every update
	 */
	public void addActionPerUpdate(Consumer<? extends Object> updateAction){
		anythingElsePerUpdate.add(updateAction);
	}
	/**
	 * add a sheet object to be drawn and updated
	 * @param sh
	 */
	public void addSheet(Sheet sh){
		sheetsInLayout.add(sh);
	}
	
	public void draw(SpriteBatch batch){
		for(Sheet sh : sheetsInLayout){
			sh.draw(batch);
		}
		for(Consumer cons : anythingElsePerRender){
			//cons.accept(null);//do the stuff in the else
		}
	}
	public void draw(SpriteBatch batch, BitmapFont gameFont){
		draw(batch);
		for(int i = 0; i < sheetsInLayout.size(); i++){//for loop
			if(sheetsInLayout.get(i) instanceof SheetButton){//if it's a button
				((SheetButton)sheetsInLayout.get(i)).draw(batch, gameFont);//draw the the button in here.
			}
		}
	}
	public void update(Vector3 touchCoords){
		for(Sheet sh : sheetsInLayout){
			if(sh instanceof SheetButton){
				((SheetButton)sh).update(touchCoords.x, touchCoords.y);
			}//if it's a button, then update it that way.
			else
			sh.update();
		}
		for(Consumer cons : anythingElsePerUpdate){
			//cons.accept(null);//do the stuff in the else.
		}
	}
	public TextBox checkTextBoxsClick(Vector3 touchCoords){
		for(int i = 0; i < sheetsInLayout.size(); i++){
			if(sheetsInLayout.get(i).getClass() == TextBox.class){//check each sheet if it's a text box
				if(((TextBox)sheetsInLayout.get(i)).checkClick(touchCoords) != null)//if the click test was true and didn't return null
					return (TextBox)sheetsInLayout.get(i);//then they clicked the text box. 
			}
		}
		//if no sheets returned, then just return here at the end with null.
		return null;
	}
	public void checkButtonsClick(Vector3 touchCoords){
		for(int i = 0; i < sheetsInLayout.size(); i++){
			if(sheetsInLayout.get(i) instanceof SheetButton){
				((SheetButton)sheetsInLayout.get(i)).checkClick(touchCoords.x, touchCoords.y);
			}
		}
	}
	public String checkButtonsClickString(Vector3 touchCoords){
		for(int i = 0; i < sheetsInLayout.size(); i++){
			if(sheetsInLayout.get(i) instanceof SheetButton){
				if(((SheetButton)sheetsInLayout.get(i)).checkClickString(touchCoords.x, touchCoords.y) != "null"){
					//if it did return true.
					return ((SheetButton)sheetsInLayout.get(i)).getButtonText();
				}
			}
		}
		return null;
	}

}
