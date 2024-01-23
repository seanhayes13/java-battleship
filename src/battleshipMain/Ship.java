package battleshipMain;

import java.util.ArrayList;

public abstract class Ship{
	private GridPosition startLoc;
	public GridPosition getStartLoc() {
		return startLoc;
	}

	private Orientation orient;
	private int health;
	
	/**
	 * Constructor
	 * @param position Upper-left starting position of the ship
	 * @param orientation Orientation of the ship, Horizontal or Vertical
	 */
	protected Ship(GridPosition position, Orientation orientation){
		startLoc = position;
		orient = orientation;
		health = getSize();
	}
	
	/**
	 * Gets a list of the GridPositions that a ship occupies
	 * @return Returns the list of GridPositions
	 */
	public ArrayList<GridPosition> getBoardSpan(){
		ArrayList<GridPosition> result = new ArrayList<>();
		int count = getSize();
		int newCol = startLoc.column;
		char newRow = startLoc.row;
		//result.add(startLoc);
		if(orient == Orientation.HORIZONTAL){
			for(int i = 0; i < count; i++){
				result.add(new GridPosition(newRow,newCol));
				newCol++;
			}
		}
		if(orient == Orientation.VERTICAL){
			for(int i = 0; i < count; i++){
				result.add(new GridPosition(newRow,newCol));
				newRow++;
			}
		}
		return result;
	}
	
	public Orientation getOrientation(){
		return orient;
	}
	
	public abstract String getShipType();
	
	protected abstract int getSize();
	
	public void increaseDamage(){
		health--;
	}
	
	public boolean isDestroyed(){
		if(health==0)return true;
		else return false;
	}
	
	public boolean atPosition(GridPosition gp){
		if(getBoardSpan().contains(gp)) return true;
		else return false;
	}
}