package battleshipMain;

public class Submarine extends Ship{
	
	public Submarine(GridPosition gp, Orientation o){
		super(gp, o);
	}

	@Override
	public String getShipType() {
		// TODO Auto-generated method stub
		return "Submarine";
		
	}

	@Override
	protected int getSize() {
		// TODO Auto-generated method stub
		return 3;
	}
}