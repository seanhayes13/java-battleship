package battleshipMain;

import java.util.ArrayList;

public class GameBoard {
	private ArrayList<Ship> shipList = new ArrayList<>();
	private ArrayList<GridPosition> gPList = new ArrayList<>();
	private ArrayList<GridPosition> hitShots = new ArrayList<>();
	private ArrayList<GridPosition> missShots = new ArrayList<>();
	private int shotsFired = 0;
	
	public GameBoard(){};
	
	public int getHitShotsSize(){
		return hitShots.size();
	}
	
	public void reset(){
		shipList = new ArrayList<>();
		gPList = new ArrayList<>();
		hitShots = new ArrayList<>();
		missShots = new ArrayList<>();
		shotsFired = 0;
	}
	
	/**
	 * Adds a ship with parameters determined by the user to the GameBoard by storing each GridPosition that a ship occupies in an ArrayList
	 * @param ship The ship being added to the GameBoard
	 * @return
	 */
	public boolean addShip(Ship ship){
		boolean add = true;
		for (Ship s : shipList){
			if(s.getShipType().equals(ship.getShipType())){
				add = false;
				System.out.println("That ship has already been added");
			}
		}
		ArrayList<GridPosition> temp = ship.getBoardSpan();
		for(GridPosition gp1 : temp){
			for(GridPosition gp2 : gPList){
				if(gp2.equals(gp1)){
					//gpCheck = false;
					System.out.println("Someone is already there...");
					add = false;
				}
			}
		}
		if((ship.getOrientation()==Orientation.HORIZONTAL && (ship.getStartLoc().column+ship.getSize()>10)) ||
				(ship.getOrientation()==Orientation.VERTICAL && (ship.getStartLoc().row+ship.getSize()>'j'))){
			add = false;
		}
		if(add){
				for(GridPosition gp : temp){
					//System.out.println("Adding...");
					gPList.add(gp);
				}
			//}
			shipList.add(ship);
			System.out.printf("Added ship: %s\n",ship.getShipType());	
		}		
		return add;
	}
	
	public int getShotsFired() {
		return shotsFired;
	}

	/**
	 * Counts how many ships haven't been completely destroyed
	 * @return Number of ships still afloat
	 */
	public int getShipsRemainingCount(){
		int result = 0;
		for(Ship ship : shipList){
			if(!ship.isDestroyed()) result++;
		}
		return result;
	}
	
	/**
	 * Takes the GridPosition being shot at and returns one of four values from the AttackResult enum based on whether or not anything is hit
	 * @param position The GridPosition being shot at
	 * @return Returns a value from the AttackResult enum
	 */
	public AttackResult attack(GridPosition position){
		shotsFired++;
		AttackResult r1 = null;
		boolean found = false;
		for(GridPosition gp : gPList){
			if(gp.equals(position)){
				r1 = AttackResult.DIRECT_HIT;
				for (GridPosition gp2 : hitShots){
					if(gp2.equals(position))
						r1 = AttackResult.PREVIOUS_HIT;
				}
				hitShots.add(position);
				found = true;
			}
		}
		if(!found){
			r1 = AttackResult.MISS;
			for (GridPosition gp2 : missShots){
				if(gp2.equals(position))
					r1 = AttackResult.PREVIOUS_MISS;
			}
			missShots.add(position);
		}
		return r1;
	}
	
	/**
	 * Finding what ship is located at a given location
	 * @param position The GridPosition we are looking at
	 * @return
	 */
	public Ship getShipAtPosition(GridPosition position){
		Ship result = null;
		for(Ship s : shipList){
			for(GridPosition gp : s.getBoardSpan()){
				if(gp.equals(position)) result = s;
			}
		}
		return result;
	}
	
	/**
	 * Custom check of miss and hit shot arraylists
	 * @param gp The GridPosition being looked for
	 * @return
	 */
	public char checkShots(GridPosition gp){
		char res = ' ';
		for(GridPosition g : missShots){
			if(g.toString().equals(gp.toString())){
				res = 'M';
			}
		}
		for(GridPosition g : hitShots){
			if(g.toString().equals(gp.toString())){
				res = 'H';
			}
		}
		return res;
	}
}