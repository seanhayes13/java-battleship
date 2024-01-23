package battleshipMain;

public class Cruiser extends Ship{
	
	public Cruiser(GridPosition gp, Orientation o){
		super(gp, o);
	}

	@Override
	public String getShipType() {
		// TODO Auto-generated method stub
		return "Cruiser";
		
	}

	@Override
	protected int getSize() {
		// TODO Auto-generated method stub
		return 3;
	}

}