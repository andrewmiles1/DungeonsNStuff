package com.burnt_toast.maze_generator;

import java.util.HashMap;
import java.util.LinkedList;

//0,0 IS ON BOTTOM LEFT CORNER

public class MazeGenerator {
	private LinkedList<LinkedList<MazeCell>> families;
	
	private LinkedList<HashMap<Float, Integer>> availableCells;
	private HashMap<Float, Integer> availableNeighbors;
	private MazeCell[][] boxes; //the physical boxes. First two boxes are the grid, third one is id.
	private int[][] finalizedMaze;//what I return
	private MazeCell currentCell;//for mid generation so I don't have to retype box[randomX*2][randomY*2]
	private MazeCell selectedNeighborCell;//for mid generationso I don't have to retype stuff like above
	private int numberOfFamilies;
	private int width;
	private int height;
	private int mazeSize;
	private int randomX;//used for randomizing which cell to choose
	private int randomY;
	private MazeCell tempCell;//just in case.
	private Integer tempInt;
	private boolean verbose;//if true, will output the changes to the maze every loop.
	
	private int currentCellX;
	private int currentCellY;
	
	public MazeGenerator(){
		families = new LinkedList<LinkedList<MazeCell>>();
		availableCells = new LinkedList<HashMap<Float, Integer>>();
		availableNeighbors = new HashMap<Float, Integer>();
		tempCell = new MazeCell();
		tempInt = new Integer(0);
		//boxes isn't initialized until maze generation.
		//nor is width and height.
	}
	
	public int[][] generateMaze(int size, boolean passVerbose){
		verbose = passVerbose;
		return generateMaze(size);
	}
	
	public int[][] generateMaze(int size){//has to be odd
		if(size % 2 == 0){//if even numbered size. Since it has to be.
			//getready
			mazeSize = size;
			finalizedMaze = new int[mazeSize+1][mazeSize+1];
			boxes = new MazeCell[mazeSize][mazeSize];//0: passable or not 1:ID
			for(int i = 0; i < mazeSize; i++){
				for (int k = 0; k < mazeSize; k++){
					boxes[i][k] = new MazeCell();
					if(i == 0 || k == 0 || i == mazeSize-1 || k == mazeSize-1){//if outer wall.
						boxes[i][k].setCollision(1);//wall not passable
						boxes[i][k].setFamily(-1);;//id none, not used in generation.
					}
					else if(i % 2 == 0 && k % 2 == 0){//if even even combo
						boxes[i][k].setCollision(1);
						boxes[i][k].setFamily(-1);//wall so id none
					}
					else{
						boxes[i][k].setCollision(1);//not passable yet.
						boxes[i][k].setFamily(-1);//id has none. NO FAMILY
					}
				}
			}//end double for
			for(int i = 0; i < size/2; i++){
				availableCells.add(new HashMap<Float, Integer>());
				for (int k = 0; k < size/2; k++){
					availableCells.get(i).put(getHash(i, k), k);
				}
			}
			numberOfFamilies = 0;
			//end getting ready
			
			//start loop
			try{
			do{
				//0,0 is BOTTOM LEFT CORNER
				
				//as long as the available cells aren't empty
				//choose random number available from available cells
				//check sides and choose another random number
				//add to family. Marry the cells.
				//take all in that family to the other family
				//check if still available to mate
				//set not if not, take out of available cells if not available
				//LKJASLKDJF NEEDS TO LAND ON ODDS
				randomX = (int) (availableCells.size() * Math.random());//get random for both x and y
				System.out.println(availableCells.size());
				if(randomX == availableCells.size()){
					System.out.println("LAKSJFDLJ");
				}
				while(availableCells.get(randomX).isEmpty()){
					randomX++;//iterate trhough until not empty
					if(randomX==(availableCells.size())){//ifreached end
						randomX = 0;
					}
					System.out.println("make bigger: " + randomX);
					
				}
				
				randomY = (int) availableCells.get(randomX).get(
						availableCells.get(randomX).keySet().toArray()[(int)(Math.random() * availableCells.get(randomX).size())]);
				//randomY = availableCells.get(randomX).get((int) (availableCells.get(randomX).size() * Math.random()));//-1 because start at 0
				currentCell = boxes[randomX*2+1][randomY*2+1];
				currentCellX = randomX*2+1;
				currentCellY = randomY*2+1;
				if(currentCell.getCollision() == 1)currentCell.setCollision(0);
				if(currentCell.getFamily()== -1){//if no family
					families.add(new LinkedList<MazeCell>());//make a family for them
					families.getLast().add(currentCell);
					currentCell.setFamily(families.size()-1);//-1 because starts at 0
				}
				checkNeighbors(currentCell, size, currentCellX, currentCellY);//sets the available neighbors array in the function.
				/*
				if(availableNeighbors.isEmpty())
					this.displayMazeCollision();
					*/
				if(!availableNeighbors.isEmpty()){
					//availableNeighbors.keySet().toArray()[(int)(Math.random()* availableNeighbors.size())]
					switch(Math.round((Float)(availableNeighbors.keySet().toArray()[(int)(Math.random()* availableNeighbors.size())]))){
					case 0://if it's up
						boxes[randomX * 2 + 1][randomY * 2 + 2].setCollision(0);
						selectedNeighborCell = boxes[randomX * 2+1][randomY * 2+1 + 2];//up
						if(selectedNeighborCell.getFamily() < currentCell.getFamily() && selectedNeighborCell.getFamily() != -1){
							combineFamilies(currentCell, selectedNeighborCell);
						}//we want to switch to the lower families. if selected is lower, it's will be the new fam
						else{
						combineFamilies(selectedNeighborCell, currentCell);//pass family love
						}
						selectedNeighborCell.setCollision(0);
						//HERE
						if(!checkNeighbors(currentCell, size, currentCellX, currentCellY))availableCells.get(randomX).remove(getHash(randomX, randomY));//remove from avail if not avail anymore
						if(!checkNeighbors(selectedNeighborCell, size, currentCellX, currentCellY+2/*up*/))availableCells.get(randomX).remove(getHash((randomX), (randomY+1)));//check neighbor them too
						//if no available neighbors now take it out of available
						//bunch of +2 for the neighbors
						if(currentCellY+2 != size-1 && !checkNeighbors(boxes[currentCellX][currentCellY+2 + 2], size, currentCellX, currentCellY+2+2)){//up
							availableCells.get(randomX).remove(getHash((randomX), (randomY+2)));//remove the neighbor's neighbor if no available stuff
						}
						if(currentCellX != size-1 && !checkNeighbors(boxes[currentCellX+2][currentCellY+2], size, currentCellX+2, currentCellY+2)){//right
							availableCells.get(randomX+1).remove(getHash((randomX+1), (randomY+1)));
						}
						if(currentCellX != 1 && !checkNeighbors(boxes[currentCellX-2][currentCellY+2], size, currentCellX-2, currentCellY+2)){//left
							availableCells.get(randomX-1).remove(getHash((randomX-1), (randomY+1)));
						}
						System.out.println("UP");
						break;
						
					case 1://if it's right
						boxes[currentCellX + 1][currentCellY].setCollision(0);//set inbetween to 0xxx                                              
						selectedNeighborCell = boxes[randomX * 2+1 + 2][randomY * 2+1];//right
						if(selectedNeighborCell.getFamily() < currentCell.getFamily() && selectedNeighborCell.getFamily() != -1){
							combineFamilies(currentCell, selectedNeighborCell);
						}//we want to switch to the lower families. if selected is lower, it's will be the new fam
						else{
						combineFamilies(selectedNeighborCell, currentCell);//pass family love
						}
						selectedNeighborCell.setCollision(0);
						//HERE
						if(!checkNeighbors(currentCell, size, currentCellX, currentCellY))availableCells.get(randomX).remove(getHash(randomX, randomY));//remove from avail if not avail anymore
						if(!checkNeighbors(selectedNeighborCell, size, currentCellX+2/*right*/, currentCellY))availableCells.get((randomX+1)).remove(getHash(randomX+1, randomY));//check neighbor them too
						if(currentCellY != size-1 && !checkNeighbors(boxes[currentCellX+2][currentCellY + 2], size, currentCellX+2, currentCellY+2)){//up
							availableCells.get(randomX+1).remove(getHash((randomX+1), randomY+1));//remove the neighbor if no available stuff
						}
						if(currentCellX+2 != size-1 && !checkNeighbors(boxes[currentCellX+2+2][currentCellY], size, currentCellX+2+2, currentCellY)){//right
							availableCells.get(randomX+2).remove(getHash((randomX+2), randomY));
						}
						if(currentCellY != 1 && !checkNeighbors(boxes[currentCellX+2][currentCellY-2], size, currentCellX+2, currentCellY-2)){//down
							availableCells.get(randomX+1).remove(getHash((randomX+1), randomY-1));
						}
						System.out.println("RIGHT");
						break;
					case 2://if it's down
						boxes[randomX * 2 + 1][randomY * 2 +1 -1].setCollision(0);
						selectedNeighborCell = boxes[randomX * 2+1][randomY*2+1 - 2];//down
						if(selectedNeighborCell.getFamily() < currentCell.getFamily() && selectedNeighborCell.getFamily() != -1){
							combineFamilies(currentCell, selectedNeighborCell);
						}//we want to switch to the lower families. if selected is lower, it's will be the new fam
						else{
						combineFamilies(selectedNeighborCell, currentCell);//pass family love
						}
						selectedNeighborCell.setCollision(0);
						//HERE
						if(!checkNeighbors(currentCell, size, currentCellX, currentCellY))availableCells.get(randomX).remove(getHash(randomX, randomY));//remove from avail if not avail anymore
						if(!checkNeighbors(selectedNeighborCell, size, currentCellX, currentCellY-2/*down*/))availableCells.get(randomX).remove(getHash(randomX, randomY-1));//check neighbor them too
						if(currentCellX != size-1 && !checkNeighbors(boxes[currentCellX+2][currentCellY-2], size, currentCellX+2, currentCellY-2)){//right
							availableCells.get(randomX+1).remove(getHash(randomX+1, randomY-1));
						}
						if(currentCellY-2 != 1 && !checkNeighbors(boxes[currentCellX][currentCellY-2-2], size, currentCellX, currentCellY-2-2)){//down
							availableCells.get(randomX).remove(getHash(randomX, randomY-2));
						}
						if(currentCellX != 1 && !checkNeighbors(boxes[currentCellX-2][currentCellY-2], size, currentCellX-2, currentCellY-2)){//left
							availableCells.get(randomX-1).remove(getHash(randomX-1, randomY-1));
						}
						System.out.println("DOWN");
						break;
					case 3://if it's left
						boxes[randomX * 2 +1 - 1][randomY * 2 + 1].setCollision(0);
						selectedNeighborCell = boxes[randomX * 2+1 - 2][randomY * 2+1];//left
						if(selectedNeighborCell.getFamily() < currentCell.getFamily() && selectedNeighborCell.getFamily() != -1){
							combineFamilies(currentCell, selectedNeighborCell);
						}//we want to switch to the lower families. if selected is lower, it's will be the new fam
						else{
						combineFamilies(selectedNeighborCell, currentCell);//pass family love
						}
						selectedNeighborCell.setCollision(0);
						//OR HERE
	                    if(!checkNeighbors(currentCell, size, currentCellX, currentCellY))availableCells.get(randomX).remove(getHash(randomX, randomY));//remove from avail if not avail anymore
						if(!checkNeighbors(selectedNeighborCell, size, currentCellX-2/*left*/, currentCellY))availableCells.get((randomX-1)).remove(getHash((randomX-1), randomY));//check neighbor them too
						if(currentCellY != size-1 && !checkNeighbors(boxes[currentCellX-2][currentCellY + 2], size, currentCellX-2, currentCellY+2)){//up
							availableCells.get(randomX-1).remove(getHash(randomX-1, randomY+1));//remove the neighbor if no available stuff
						}
						if(currentCellY != 1 && !checkNeighbors(boxes[currentCellX-2][currentCellY-2], size, currentCellX-2, currentCellY-2)){//down
							availableCells.get(randomX-1).remove(getHash(randomX-1, randomY-1));
						}
						if(currentCellX-2 != 1 && !checkNeighbors(boxes[currentCellX-2-2][currentCellY], size, currentCellX-2-2, currentCellY)){//left
							availableCells.get(randomX-2).remove(getHash(randomX-2, randomY));
						}
						System.out.println("LEFT");
						break;
					}//end switch
					//check neighbors
					try{
						if(currentCellY != size-1 && !checkNeighbors(boxes[randomX*2+1][randomY*2+1 + 2], size, currentCellX, currentCellY+2)){//up
							availableCells.get(randomX).remove(getHash(randomX, randomY+1));//remove the neighbor if no available stuff
						}
						if(currentCellX != size-1 && !checkNeighbors(boxes[randomX*2+1+2][randomY*2+1], size, currentCellX+2, currentCellY)){//right
							availableCells.get(randomX+1).remove(getHash(randomX+1, randomY));
						}
						if(currentCellY != 1 && !checkNeighbors(boxes[randomX*2+1][randomY*2+1-2], size, currentCellX, currentCellY-2)){//down
							availableCells.get(randomX).remove(getHash(randomX, randomY-1));
						}
						if(currentCellX != 1 && !checkNeighbors(boxes[randomX*2+1-2][randomY*2+1], size, currentCellX-2, currentCellY)){//left
							availableCells.get(randomX-1).remove(getHash(randomX-1, randomY));
						}
						//then check all neighbors of the selected neighbor
					}
					catch(Exception e){
						System.out.println(randomX + ", " + randomY);
						System.out.println(availableCells.get(randomX).size());
						e.printStackTrace();
						System.out.println("Lol yeah it broke");
					}
				}
				//while(families.getLast().isEmpty())
					//families.removeLast();//remove last one if empty
				//and then end.
				if(verbose){
					System.out.println("");

					System.out.println("");
					this.displayMazeLogic();
					System.out.print("");
					this.displayAvailableCells();
					System.out.println("");
					if(families.get(0).size() == Math.pow((size/2), 2)){
						System.out.println("Done");
					}
				}
				
			}while (!(families.get(0).size() == Math.pow((size/2), 2)));//end cell while
			}catch(Exception e){
				e.printStackTrace();
				System.out.println(randomX + ", " + randomY);
				System.out.println(availableCells.get(randomX).size());

			}
			for(int i = 0; i < finalizedMaze.length; i++){
				for(int k = 0; k < finalizedMaze[i].length; k++){
					if(i < boxes.length && k < boxes.length){//if within bounds
						finalizedMaze[i][k] = boxes[i][k].getCollision();//copy over boxes collision
					}
					else{
						//no change to become a not wall sorry
						finalizedMaze[i][k] = 1;//it's outside of bounds so it's a wall.
					}
				}
			}
			if(verbose){
				System.out.println("Finalzied Maze:");
				displayMazeCollision(finalizedMaze);
			}
			return finalizedMaze;
		}
		else return null;
	}
	
	public void combineFamilies(MazeCell oldFamilyCell, MazeCell newFamilyCell){
		if(oldFamilyCell.getFamily() == -1){
			families.get(newFamilyCell.getFamily()).add(oldFamilyCell);
			oldFamilyCell.setFamily(newFamilyCell.getFamily());
		}
		else{
			/*
			if(oldFamilyCell.getFamily() < newFamilyCell.getFamily()){
				tempCell = oldFamilyCell;
				oldFamilyCell = newFamilyCell;//switch numbers, lower wins
				newFamilyCell = tempCell;
			}
			*/
			tempInt = oldFamilyCell.getFamily();
			for(MazeCell cell : families.get(tempInt)){
				System.out.println("from " + cell.getFamily() + " to " + newFamilyCell.getFamily());
				families.get(newFamilyCell.getFamily()).add(cell);
				cell.setFamily(newFamilyCell.getFamily());
			}//move all cells from old family to new
			oldFamilyCell.setFamily(newFamilyCell.getFamily());
			/*
			for(int i = 0;i < families.get(oldFamilyCell.getFamily()).size();i++){
				System.out.println("from " + oldFamilyCell.getFamily() + " to " + newFamilyCell.getFamily());
				families.get(tempInt).get(i).setFamily(newFamilyCell.getFamily());
				families.get(newFamilyCell.getFamily()).addLast(families.get(tempInt).get(i));
			}
			*/
			families.get(tempInt).clear();// clear old family

		}
	}

	public boolean checkNeighbors(MazeCell cellToCheck, int size, int boxIndexX, int boxIndexY){
		availableNeighbors.clear();
		if(boxIndexY == size-1){//if on top side
			//then don't add 0 as an available neighbor
		}
		else{//if not you can check for a top neighbor
			if(boxes[boxIndexX][boxIndexY + 2].getFamily() != currentCell.getFamily()){
				//if it's not of the same family
				availableNeighbors.put(0f, 0);//add it in
			}
			
		}
		if(boxIndexX == size-1){//if on right side.
			//then don't add 1 as available nieghbor
		}
		else{//if not right side then check
			if(boxes[boxIndexX + 2][boxIndexY].getFamily() != currentCell.getFamily()){
				//if not same family on right
				availableNeighbors.put(1f,1);
			}
		}//end right check
		//bottom check
		if(boxIndexY == 1){
			//System.out.println("It's totally equal to 1 son");
		}
		if(boxIndexY == 1){
			//if on bottom side then plese don'tcheck down
		}
		else{//if not on bottom, then go ahead and check bottom neoghboiejf
			if(boxes[boxIndexX][boxIndexY - 2].getFamily() != currentCell.getFamily()){
				availableNeighbors.put(2f, 2);//then add into the available list you bachelor
			}
		}//end bottom check
		//start left check
		if(boxIndexX == 1){//if on left side
			//then don't check left if it's on left side
		}
		else{//if not on left side
			if(boxes[boxIndexX-2][boxIndexY].getFamily() != currentCell.getFamily()){
				availableNeighbors.put(3f, 3);//add to available you bachelor
			}
		}//end left check

		if(availableNeighbors.isEmpty())return false;
		else return true;
	}
	public void displayMazeCollision(int[][] maze){
		for(int i = 0; i < maze.length; i++){
			for (int k = 0; k < maze.length; k ++){
				System.out.print(maze[k][i]);
			}
			System.out.println();
		}
	}
	public void displayMazeLogic(){
		for(int i = 0; i < mazeSize; i++){
			for(int k = 0; k < mazeSize; k++){
				if(boxes[k][i].getFamily()== -1)
					System.out.print('n');
				else
					System.out.print(boxes[k][i].getFamily());
			}
			System.out.println();
		}
	}
	public void displayAvailableCells(){
		for(int i = 0; i < availableCells.size();i++){
			for(int k = 0; k < availableCells.get(i).size();k++){
				System.out.print((Integer)availableCells.get(i).values().toArray()[k] + ":");
			}
			System.out.println();
		}
	}
	public float getHash(int x, int y){
		return (float)(Math.sqrt(x) + Math.sqrt(y));
	}
	
}
