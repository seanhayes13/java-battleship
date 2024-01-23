package battleshipMain;

public class Battleship extends Ship{
	
	public Battleship(GridPosition gp, Orientation o){
		super(gp, o);
	}

	@Override
	public String getShipType() {
		// TODO Auto-generated method stub
		return "Battleship";
		
	}

	@Override
	protected int getSize() {
		// TODO Auto-generated method stub
		return 4;
	}

}