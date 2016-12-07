package com.burnt_toast.dungeons_n_stuff;

import java.util.LinkedList;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.maps.tiled.TiledMapTileSet;
import com.burnt_toast.maze_generator.MazeGenerator;

public class MapCreationTool {

	int[][] smallCollisionMap;
	int[][] currentCollisionMap;
	TiledMapTileLayer visualLayer;
	TiledMapTile floor;
	TiledMapTile wall;
	MazeGenerator mazeGen;
	int sizeUpFactor = 3;
	
	TiledMapTile topLeftTile;
	TiledMapTile topTile;
	TiledMapTile topRightTile;
	TiledMapTile rightTile;
	TiledMapTile leftTile;
	TiledMapTile bottomTile;
	TiledMapTile bottomLeftTile;
	TiledMapTile bottomRightTile;
	TiledMapTile middleBrickTile;
	TiledMapTile leftBrickTile;
	TiledMapTile rightBrickTile;
	TiledMapTile topRightCorner;
	TiledMapTile topLeftCorner;
	TiledMapTile botLeftCorner;
	TiledMapTile botRightCorner;
	
	//used for making map look pretty
	int idSum;
	
	public MapCreationTool(TiledMapTileLayer passVisLayer, TiledMapTileSet tileSet){
		visualLayer = passVisLayer;
		floor = tileSet.getTile(20);
		
		wall = tileSet.getTile(6);
		mazeGen = new MazeGenerator();
		
		topLeftTile = tileSet.getTile(3);
		topRightTile = tileSet.getTile(5);
		topTile = tileSet.getTile(4);
		rightTile = tileSet.getTile(21);
		leftTile = tileSet.getTile(19);
		bottomTile = tileSet.getTile(36);
		bottomRightTile = tileSet.getTile(37);
		bottomLeftTile = tileSet.getTile(35);
		middleBrickTile = tileSet.getTile(22);
		rightBrickTile = tileSet.getTile(54);
		leftBrickTile = tileSet.getTile(38);
		topLeftCorner = tileSet.getTile(1);
		topRightCorner = tileSet.getTile(2);
		botLeftCorner = tileSet.getTile(17);
		botRightCorner = tileSet.getTile(18);
		

	}
	
	/**
	 * This function makes the thing look so good. 
	 */
	public void makeItLookPretty(){
		/*
		 * double for loop to loop through every single block
		 * check 
		 */
		for(int k = currentCollisionMap.length-1; k > 0; k--){
			for(int i = 0; i < currentCollisionMap.length;i++){
				if(currentCollisionMap[i][k] == 0)continue;//if it is a floor, don't change
				idSum = 0;
				//System.out.println("Test------------------------------------------------");
				if(k != currentCollisionMap.length-1){//if not on top
					if(i != 0){//if not on left
						if(currentCollisionMap[i-1][k+1] == 0){//if top left is floor
							idSum+= 128;
						}//end if top left is floor
					}//end if not left
					if(i != currentCollisionMap.length-1){//if not on right
						if(currentCollisionMap[i + 1][k + 1] == 0){//if top right is floor
							idSum += 2;
						}//end if floor top right
					}//end if not on right
					
					if(currentCollisionMap[i][k+1] == 0){
						idSum +=1;//if top is floor, add.
					}
				}//end if not on top
				if(k != 0){//if not on bottom
					if(i != 0){//if not on left
						if(currentCollisionMap[i-1][k-1]== 0){//if bottom left is floor
							idSum+=32;//add to sum
						}//end if bottom left is not floor
					}//if not on left
					if(i != currentCollisionMap.length-1){//if not on right side of map
						if(currentCollisionMap[i+1][k-1] == 0){//if bottom right is floor
							idSum += 8;//add 8 to sum if that there is floor
						}//end if bottom right is floor
					}//end if not on right
					if(currentCollisionMap[i][k-1] == 0){//if direkt below is floor
						idSum += 16;//down
					}//end check below
				}//end if not on bottom
				if(i != 0){//if not on left
					if(currentCollisionMap[i-1][k] == 0){//if to the left is floor
						idSum+= 64;
					}
				}//end if not on left
				if(i != currentCollisionMap.length-1){//if not on right
					if(currentCollisionMap[i+1][k] == 0){//if right is floor
						idSum+= 4;
					}
				}
				switch(idSum){
				case 6:case 12:case 14://if left
					visualLayer.getCell(i, k).setTile(leftTile);//sets left
					break;
				case 0:
					//don't set anything
					break;
				case 143: //if top right corner
					visualLayer.getCell(i, k).setTile(topRightCorner);
					break;
				case 227: //if top left
					visualLayer.getCell(i, k).setTile(topLeftCorner);
					break;
				case 248: //if bottom left
					visualLayer.getCell(i,k).setTile(leftBrickTile);
					visualLayer.getCell(i, k+1).setTile(botLeftCorner);
					break;
				case 62://if bottom right
					visualLayer.getCell(i, k).setTile(rightBrickTile);
					visualLayer.getCell(i, k+1).setTile(botRightCorner);
					break;
				case 96:case 192:case 224://if right
					visualLayer.getCell(i, k).setTile(rightTile);
					break;
				case 129:case 130:case 131:case 3://if down
					visualLayer.getCell(i, k).setTile(bottomTile);
					break;
				case 32://if top right
					visualLayer.getCell(i, k).setTile(rightTile);
					break;
				case 8://if top left
					visualLayer.getCell(i, k).setTile(leftTile);
					break;
				case 2://if bottom left
					visualLayer.getCell(i, k).setTile(bottomLeftTile);
					break;
				case 128://if bottom right
					visualLayer.getCell(i, k).setTile(bottomRightTile);
					break;
				case 56://brick wall
					visualLayer.getCell(i, k).setTile(middleBrickTile);
					visualLayer.getCell(i, k+1).setTile(topTile);
					break;
				case 24://left brick wall
					visualLayer.getCell(i, k).setTile(middleBrickTile);
					visualLayer.getCell(i, k+1).setTile(topTile);
					visualLayer.getCell(i-1, k+1).setTile(topLeftTile);
					break;
				case 48://right brick wall
					visualLayer.getCell(i, k).setTile(middleBrickTile);
					visualLayer.getCell(i, k+1).setTile(topTile);
					visualLayer.getCell(i+1, k+1).setTile(topRightTile);
					break;
				case 4://left wall
					visualLayer.getCell(i, k).setTile(leftTile);
					break;
				default:
						//don't set anything it's a floor
					break;
				}//end switch
				System.out.print(idSum + ";");
			}//end column for
			System.out.println("");
		}//end row for
		//visualLayer.getCell(1, 1).setTile(this.middleBrickTile);
		//visualLayer.getCell(1, 2).setTile(this.middleBrickTile); UP
		//visualLayer.getCell(2, 1).setTile(this.middleBrickTile); RIGHT
	}
	
	/**
	 * The only parameter is the SMALL version of the size. 
	 * The small version is what the maze generator uses,
	 * and then the maze generation tool AUTOMATICALLY makes a 
	 * size that's double the size so yeah. Just do the small.
	 * @param mapSizeSmall So yeah this is the size that will be doubled.
	 */
	public int[][] prepareMap(int mapSizeSmall){

		smallCollisionMap = mazeGen.generateMaze(mapSizeSmall, true);
		currentCollisionMap = new int[(smallCollisionMap.length * sizeUpFactor)][(smallCollisionMap.length * sizeUpFactor)];
		
		for(int i = 0; i < smallCollisionMap.length; i++){//Row Normal
			for(int k = 0; k < sizeUpFactor; k++){//row doubled 
				for(int l = 0; l < smallCollisionMap.length; l++){//column normal
					for(int m = 0; m < sizeUpFactor; m++){//column normal
						currentCollisionMap[i*sizeUpFactor+k][l*sizeUpFactor+m] = smallCollisionMap[i][l];
					}
				}
			}
		}
		for(int i = 0; i < currentCollisionMap.length; i++){
			for(int k = 0; k < currentCollisionMap.length; k++){
				System.out.print(currentCollisionMap[i][k]);
			}
			System.out.println();
		}
		for(int i = 0; i < currentCollisionMap.length;i++){
			for(int k = 0; k < currentCollisionMap.length;k++){
				if(currentCollisionMap[i][k] == 0)
				visualLayer.getCell(i, k).setTile(floor);
				else if(currentCollisionMap[i][k] == 1)
				visualLayer.getCell(i, k).setTile(wall);
			}
		}
		makeItLookPretty();
		return currentCollisionMap;
	}
}
