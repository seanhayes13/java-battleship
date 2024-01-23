package battleshipMain;

import java.util.Random;

public class GridPosition {
	public final char row;
	public final int column;
	
	public boolean equals(GridPosition gp){
		if(gp.row==row && gp.column==column) return true;
		else return false;
	}
	
	public GridPosition(char r, int c){
		row = r;
		column = c;
	}
	
	public String toString(){
		String result = String.format("%s, %d", row, column);
		return result;
	}
	
	public static GridPosition generateRandom(){
		Random rand = new Random();
		int c = rand.nextInt((10-1)+1)+1;
		//System.out.printf("Random column: %d\n",c);
		char r = (char)(rand.nextInt(10)+'a');
		//System.out.printf("Random row: %s\n",r);
		return new GridPosition(r, c);
	}
	
	public static GridPosition parse (String s){
		//System.out.printf("Passed in: %s\n",s);
		String[] first = s.split(",");
		return new GridPosition(first[0].charAt(0),Integer.parseInt(first[1]));
	}
	
	public static boolean verifyGrid(GridPosition gp){
		if(gp.column < 1 || gp.column > 10 || gp.row >'j' || gp.row < 'a') return false;
		else return true;
	}
}