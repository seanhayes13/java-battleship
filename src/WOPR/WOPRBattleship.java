package WOPR;

//import java.util.ArrayList;
import java.util.Deque;
import java.util.HashSet;
import java.util.LinkedList;
//import java.util.Queue;
import java.util.Set;

import battleshipMain.AttackResult;
import battleshipMain.GridPosition;

public class WOPRBattleship {
	private Set<GridPosition> calledShots = new HashSet<GridPosition>();
	//private ArrayList<GridPosition> missedShots = new ArrayList<>();
	//private ArrayList<GridPosition> hitShots = new ArrayList<>();
	private GridPosition start = null;
	private Deque<GridPosition> hitList = new LinkedList<>();
	private String mode = "search";
	private GridPosition tgtGrid = null;
	private GridPosition result = null;
	private GridPosition firstHit = null;
	private SearchValues directive = SearchValues.SEARCH;
	
	public void setDir(SearchValues sv){
		directive = sv;
	}
	
	public void addToCalledShots(GridPosition gp){
		calledShots.add(gp);
	}
	
	public int getCalledShotsSize(){
		return calledShots.size();
	}
	
	public GridPosition getResult() {
		return result;
	}
	
	public String getMode(){
		return mode;
	}
	
	public void setMode(String s){
		mode = s;
	}

	public void setStart(){
		start = GridPosition.generateRandom();
		//calledShots.add(start);
		//System.out.printf("Start: %s\n", start.toString());
	}
	
	public GridPosition getStart(){
		return start;
	}
	
	public void action(){
		if(directive == SearchValues.SEARCH){
			search_SH();
		}
		if(directive != SearchValues.SEARCH){
			hunt_SH(tgtGrid);
		}
	}
	
	public void hunt_SH(GridPosition gp){
		//System.out.printf("directive: %s\n", directive.toString());
		boolean resultSet = false;
		GridPosition temp = null;
		int c = gp.column;
		char r = gp.row;
		if(directive == SearchValues.CHECK_UP){
			temp = new GridPosition(--r, c);
			resultSet=true;
		}
		if(directive == SearchValues.CHECK_DOWN){
			temp = new GridPosition(++r, c);
			resultSet=true;
		}
		if(directive == SearchValues.CHECK_LEFT){
			temp = new GridPosition(r, --c);
			resultSet=true;
		}
		if(directive == SearchValues.CHECK_RIGHT){
			temp = new GridPosition(r, ++c);
			resultSet=true;
		}
		if(checkCalledShots(temp) && resultSet){
			result = temp;
		}
		if(!resultSet){
			result = firstHit;
		}
	}
	
	public void search_SH(){
		boolean valid = false;
		while(!valid){
			GridPosition temp = GridPosition.generateRandom();
			for(GridPosition gp : calledShots){
				if(gp.equals(temp)){
					temp = GridPosition.generateRandom();
					//contains = true;
				}
			}
			int a = Math.abs(temp.column - start.column);
			int b = Math.abs(Character.toLowerCase(temp.row)-Character.toLowerCase(start.row));
			if((a%2==0 && b%2==0) || (a%2==1 && b%2==1)){
				result = temp;
				//calledShots.add(temp);
				valid = true;
			}
		}
	}
	
	public void registerAttack(AttackResult ar, GridPosition gp){
		//System.out.printf("AttackResult: %s at %s\n", ar.toString(),gp.toString());
		calledShots.add(gp);
		if(ar == AttackResult.DIRECT_HIT){
			if(directive == SearchValues.SEARCH){
				firstHit = gp;
				//System.out.printf("firstHit is now: %s\n",firstHit.toString());
				directive = SearchValues.CHECK_UP;
			} else {
				hitList.add(tgtGrid);
			}
			tgtGrid = gp;
			//System.out.printf("tgtGrid is now: %s\n",tgtGrid.toString());
		} else{
			boolean change = false;
			if(directive==SearchValues.CHECK_UP && !change){
				directive = SearchValues.CHECK_DOWN;
				change = true;
			}
			if(directive==SearchValues.CHECK_DOWN && !change){
				directive = SearchValues.CHECK_LEFT;
				change = true;
			}
			if(directive==SearchValues.CHECK_LEFT && !change){
				directive = SearchValues.CHECK_RIGHT;
				change = true;
			}
			if(directive==SearchValues.CHECK_RIGHT && !change){
				directive = SearchValues.SEARCH;
				change = true;
			}
			tgtGrid = firstHit;
			/*if(tgtGrid!=null){
				System.out.printf("firstHit is: %s || tgtGrid is now: %s\n",firstHit.toString(),tgtGrid.toString());
			}*/
		}
	}
    
	public boolean checkCalledShots(GridPosition gp){
		boolean res = true;
		for(GridPosition g : calledShots){
			if(g.toString().equals(gp.toString())){
				res = false;
			}
		}
		return res;
	}
}