package battleshipMain;

public class Carrier extends Ship{
	
	public Carrier(GridPosition gp, Orientation o){
		super(gp, o);
	}

	@Override
	public String getShipType() {
		// TODO Auto-generated method stub
		return "Carrier";
		
	}

	@Override
	protected int getSize() {
		// TODO Auto-generated method stub
		return 5;
	}

}