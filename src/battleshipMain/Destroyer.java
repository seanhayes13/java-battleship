package battleshipMain;

public class Destroyer extends Ship{
	
	public Destroyer(GridPosition gp, Orientation o){
		super(gp, o);
	}

	@Override
	public String getShipType() {
		// TODO Auto-generated method stub
		return "Destroyer";
		
	}

	@Override
	protected int getSize() {
		// TODO Auto-generated method stub
		return 2;
	}
}