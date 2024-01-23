package battleshipMain;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Random;
import java.util.Scanner;

import WOPR.SearchValues;
import WOPR.WOPRBattleship;

public class Main {
	private static String ships[] = {"Carrier","Battleship","Cruiser","Submarine","Destroyer"};
	
	public static void main(String[] args) throws IOException, InterruptedException{
		int choice = -99;
		while(choice !=0){
			Scanner input = new Scanner(System.in);
			System.out.println("Welcome to Battleship\nChoose from one of the options below:");
			System.out.println("1 - Play against another human player");
			System.out.println("2 - Play against the AI");
			System.out.println("3 - Watch the AI play against itself");
			System.out.println("0 - Exit");
			choice = input.nextInt();
			switch(choice){
			case 1:
				twoPlayer();
				break;
			case 2:
				onePlayerVsAI();
				break;
			case 3:
				twoAI();
				break;
			case 0:
				break;
			default:
				System.out.println("That was not an option, try again...");
				break;
			}
			input.close();
		}
	}
	
	/**
	 * For one human player to play against one WOPR AI
	 * @throws IOException
	 */
	public static void onePlayerVsAI() throws IOException{
		boolean aiFirstMove = true;
		int turn = 0;
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		System.out.println("Enter the name for player one: ");
		String player1 = br.readLine();
		GameBoard gb1 = new GameBoard();
		WOPRBattleship wopr1 = new WOPRBattleship();
		String player2 = "WOPR";
		GameBoard gb2 = new GameBoard();
		//buildMap(ships, player1,gb1);
		randomPlacement(ships,gb1);
		randomPlacement(ships, gb2);
		System.out.println("Good luck...");
		System.out.printf("gb1 ship count: %d\n", gb1.getShipsRemainingCount());
		while(gb1.getShipsRemainingCount()>0 && gb2.getShipsRemainingCount()>0){
			drawMap(gb1, gb2);
			if(turn%2==0){
				playerTurn(gb2, player1, player2);
			} else {
				AttackResult ar = null;
				if(aiFirstMove){
					wopr1.setStart();
					ar = gb1.attack(wopr1.getStart());
					wopr1.registerAttack(ar, wopr1.getStart());
					aiFirstMove = false;
				} else{
					//System.out.printf("Start of turn %d\n", turn);
					wopr1.action();
					GridPosition gp = wopr1.getResult();
					ar = gb1.attack(gp);
					wopr1.registerAttack(ar, gp);
					if(ar==AttackResult.DIRECT_HIT){
						System.out.printf("wopr1 hit %s's %s at %s\n", player1, gb1.getShipAtPosition(gp).getShipType(), gp.toString());
						gb1.getShipAtPosition(gp).increaseDamage();
					}
					if(ar==AttackResult.DIRECT_HIT && gb1.getShipAtPosition(gp).isDestroyed()){
						System.out.printf("wopr1 sunk %s, resuming search\n", gb1.getShipAtPosition(gp).getShipType());
						wopr1.setDir(SearchValues.SEARCH);
						//wopr1.resetCheck();
						//System.out.printf("mode is: %s\n", wopr1.getMode());
					}
					if(gb1.getShipsRemainingCount()==0){
						System.out.printf("Turn: %d wopr1 wins!\n",turn);
					}
					//System.out.printf("End of turn %d\n", turn);
				}
			}
			turn++;
		}
	}
	
	/**
	 * For playing one human player against another
	 * @throws IOException
	 */
	public static void twoPlayer() throws IOException{
		int turn = 0;
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		System.out.println("Enter the name for player one: ");
		String player1 = br.readLine();
		GameBoard gb1 = new GameBoard();
		System.out.println("Enter the name for player two: ");
		String player2 = br.readLine();
		GameBoard gb2 = new GameBoard();
		System.out.printf("Time for %s to place their ships on the board. %s, look away!",player1, player2);
		buildMap(ships, player1,gb1);
		System.out.printf("Now it's time for %s to place their ships on the board. %s, look away!",player2, player1);
		buildMap(ships, player2,gb2);
		while(gb1.getShipsRemainingCount()>0 && gb2.getShipsRemainingCount()>0){
			drawMap(gb1, gb2);
			if(turn%2==0){
				playerTurn(gb2, player1, player2);
			} else {
				playerTurn(gb1, player2, player1);
			}
			turn++;
		}
	}
	
	/**
	 * The repeating code for each turn for a human player
	 * @param gb The GameBoard that the active player is shooting at
	 * @param atk The active player's name
	 * @param def The defending player's name
	 * @throws IOException
	 */
	public static void playerTurn(GameBoard gb, String atk, String def) throws IOException{
		System.out.printf("\n%s, call your shot: \n", atk);
		boolean validGP = false;
		GridPosition shot = null;
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String tempLoc;
		while(!validGP){
			tempLoc = br.readLine();
			GridPosition gp = GridPosition.parse(tempLoc);
			if(GridPosition.verifyGrid(gp)) shot = gp;
			validGP = true;			
		}
		AttackResult ar = gb.attack(shot);
		if(ar == AttackResult.DIRECT_HIT){
			System.out.printf("%s hit %s's %s\n", atk, def, gb.getShipAtPosition(shot).getShipType());
			gb.getShipAtPosition(shot).increaseDamage();
			if(ar==AttackResult.DIRECT_HIT && gb.getShipAtPosition(shot).isDestroyed()){
				System.out.printf("%s sunk %s's %s, resuming search\n", atk, def, gb.getShipAtPosition(shot).getShipType());
			}
		}
		if(ar == AttackResult.PREVIOUS_HIT || ar == AttackResult.PREVIOUS_MISS){
			System.out.println("You already shot that position");
		}
		if(ar == AttackResult.MISS){
			System.out.println("You struck a critical catastrophic blow against nothing!");
		}
	}
	
	/**
	 * Displays a basic grid. This will display each player's miss/hit board, not the board with the ships themselves
	 * @param g1 Player1's board
	 * @param g2 Player2's board
	 */
	public static void drawMap(GameBoard g1, GameBoard g2){
		System.out.print("   ");
		for(int i = 1; i <= 10; i++){
			System.out.printf(" %d ",i);
		}
		System.out.print("     ");
		for(int i = 1; i <= 10; i++){
			System.out.printf(" %d ",i);
		}
		int row = 1;
		char rowDisp = 'A';
		while(row <= 10){
			System.out.printf("\n %s ", rowDisp);
			for(int i = 1; i <= 10; i++){
				System.out.printf(" %s ", g1.checkShots(new GridPosition(Character.toLowerCase(rowDisp),i)));
			}
			System.out.printf("|   %s ", rowDisp);
			for(int i = 1; i <= 10; i++){
				System.out.printf(" %s ", g2.checkShots(new GridPosition(Character.toLowerCase(rowDisp),i)));
			}
			System.out.print("|");
			row++;
			rowDisp++;
		}
		System.out.print("\n");
	}
	
	/**
	 * Used with human players to place ships on the board
	 * @param sh Array of ship names
	 * @param p The player's name
	 * @param gb The player's GameBoard
	 * @throws IOException
	 */
	public static void buildMap(String[] sh, String p, GameBoard gb) throws IOException{
		GridPosition tempGP = null;
		Orientation orient = null;
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		System.out.println("Enter grid positions by first selecting the letter row, a comma, and the number column.\nExample (without quotes): 'a,1' or 'j,10'");
		for(String s : sh){
			boolean shipAdded = false;
			boolean orientationChk = false;
			while (!shipAdded){
				boolean gpChk = false;
				while(!gpChk){
					System.out.printf("%s, enter the top left of your %s",p,s);
					String tempLoc = br.readLine();
					GridPosition gp = GridPosition.parse(tempLoc);
					if(GridPosition.verifyGrid(gp)) tempGP = gp;
					gpChk = true;
				}
				String orientationInput;
				while(!orientationChk){
					System.out.println("Horizontal or vertical (enter h or v)");
					orientationInput = br.readLine();
					char o = orientationInput.toLowerCase().charAt(0);
					if(o == 'h' || o == 'v'){
						orientationChk = true;
					}
					if(o == 'h') orient = Orientation.HORIZONTAL;
					if(o=='v') orient = Orientation.VERTICAL;
				}
				shipAdded = populateGB(s,tempGP,orient,gb);
				if(!shipAdded)System.out.println("whoops...");
				else System.out.printf("%s added to the board\n",s);
			}
		}
	}
	
	
	/**
	 * Primarily used when working with AI to place ships on board
	 * @param sh Array of strings for each ship
	 * @param gb The GameBoard for AI that is placing it's ships
	 * @throws IOException
	 */
	public static void randomPlacement(String[] sh, GameBoard gb) throws IOException{
		GridPosition tempGP = null;
		Orientation orient = null;
		for(String s : sh){
			boolean shipAdded = false;
			while (!shipAdded){
				tempGP = GridPosition.generateRandom();
				Random rand = new Random();
				int orientSel = rand.nextInt(50);
				if(orientSel%2==0) orient = Orientation.HORIZONTAL;
				else orient = Orientation.VERTICAL;
				shipAdded = populateGB(s,tempGP,orient,gb);
				if(!shipAdded){
					System.out.println("whoops...");
				} else{
					System.out.printf("%s added to the board\n",s);
				}
			}
		}
	}
	
	/**
	 * Adds ships to the player's GameBoard, carried over from buildMap and randomPlacement
	 * @param s The string representing the ship to be placed
	 * @param gp The starting position of the ship
	 * @param or The orientation of the ship (horizontal or vertical)
	 * @param gb The GameBoard that the ship is going to be added to
	 * @return
	 */
	public static boolean populateGB(String s, GridPosition gp, Orientation or, GameBoard gb){
		boolean success = true;
		switch(s){
			case "Carrier":
				Carrier cv = new Carrier(gp,or);
				success = gb.addShip(cv);
				break;
			case "Battleship":
				Battleship bb = new Battleship(gp,or);
				success = gb.addShip(bb);
				break;
			case "Cruiser":
				Cruiser ca = new Cruiser(gp, or);
				success = gb.addShip(ca);
				break;
			case "Submarine":
				Submarine sub = new Submarine(gp,or);
				success = gb.addShip(sub);
				break;
			case "Destroyer":
				Destroyer dd = new Destroyer(gp,or);
				success = gb.addShip(dd);
				break;
		}
		return success;
	}
	
	/**
	 * Testing use only, one AI hunting for one set of ships
	 * @throws InterruptedException
	 * @throws IOException
	 */
	public static void oneAITest() throws InterruptedException, IOException{
		int turn = 1;
		while(turn <100){
			WOPRBattleship wopr1 = new WOPRBattleship();
			GameBoard gb1 = new GameBoard();
			randomPlacement(ships, gb1);
			wopr1.setStart();
			AttackResult ar = gb1.attack(wopr1.getStart());
			wopr1.registerAttack(ar, wopr1.getStart());
			if(ar==AttackResult.DIRECT_HIT){
				//System.out.printf("wopr1 hit wopr2's %s at %s\n", gb1.getShipAtPosition(wopr1.getStart()).getShipType(), wopr1.getStart().toString());
				gb1.getShipAtPosition(wopr1.getStart()).increaseDamage();
			}
			while(gb1.getShipsRemainingCount()>0){
				aiTurn(wopr1,gb1,turn,"wopr1","no one's");
			}
			turn++;
		}
	}
	
	/**
	 * One stop shop for checking stats of each shot, primarily used for testing
	 * @param who The player currently firing
	 * @param turn The current turn count
	 * @param shotsFired How many shots that player has fired
	 * @param calledShotsSize Same
	 * @param hitShotsSize How many of those shots were hits
	 * @param ar The AttackResult for the current shot
	 */
	public static void shotDetails(String who, int turn, int shotsFired, int calledShotsSize, int hitShotsSize, String ar){
		System.out.printf("%s - Turn: %d - Shots fired at gb2: %d | Called shots: %d | Hit Shots: %d | Attack result: %s \n", 
		turn, shotsFired, calledShotsSize, hitShotsSize, ar);		
	}
	
	/**
	 * The AI equivalent of the playerTurn method
	 * @param w The AI that is firing
	 * @param gb The GameBoard they are firing at
	 * @param turn Turn counter
	 * @param w1 String for the attacking AI's name
	 * @param w2 String for the defending AI's name
	 * @throws InterruptedException
	 */
	public static void aiTurn(WOPRBattleship w, GameBoard gb, int turn, String w1, String w2) throws InterruptedException{
		AttackResult ar = null;
		Thread.sleep(1000);
		w.action();
		GridPosition gp = w.getResult();
		ar = gb.attack(gp);
		//shotDetails("wopr1",turn, gb.getShotsFired(), w.getCalledShotsSize(), gb.getHitShotsSize(), ar.toString());
		w.registerAttack(ar, gp);
		if(ar==AttackResult.DIRECT_HIT){
			System.out.printf("%s hit %s's %s at %s\n", w1, w2, gb.getShipAtPosition(gp).getShipType(), gp.toString());
			gb.getShipAtPosition(gp).increaseDamage();
		}
		if(ar==AttackResult.DIRECT_HIT && gb.getShipAtPosition(gp).isDestroyed()){
			System.out.printf("%s sunk %s's %s, resuming search\n", w1,w2,gb.getShipAtPosition(gp).getShipType());
			w.setDir(SearchValues.SEARCH);
		}
	}

	/**
	 * Gameplay skeleton for setting two AI's against each other
	 * @throws InterruptedException
	 * @throws IOException
	 */
	public static void twoAI() throws InterruptedException, IOException{
		//Uncomment these two lines and the incrementers below for bulk testing
		//int wopr1Win = 0;
		//int wopr2Win = 0;
		int repeat = 0;
		//Reactivate this for loop for bulk testing
		//for(int ctr = 0; ctr < 10; ctr++){
			System.out.printf("Starting round %d\n",repeat);
			int turn = 0;
			boolean keepPlaying = true;
			WOPRBattleship wopr1 = new WOPRBattleship();
			WOPRBattleship wopr2 = new WOPRBattleship();
			GameBoard gb1 = new GameBoard();
			GameBoard gb2 = new GameBoard();
			//wopr1 ships
			randomPlacement(ships, gb1);
			//wopr2 ships
			randomPlacement(ships, gb2);
			wopr1.setStart();
			AttackResult ar = gb2.attack(wopr1.getStart());
			//shotDetails("wopr1",turn, gb2.getShotsFired(), wopr1.getCalledShotsSize(), gb2.getHitShotsSize(), ar.toString());
			wopr1.registerAttack(ar, wopr1.getStart());
			if(ar==AttackResult.DIRECT_HIT){
				System.out.printf("wopr1 hit wopr2's %s at %s\n", gb2.getShipAtPosition(wopr1.getStart()).getShipType(), wopr1.getStart().toString());
				gb2.getShipAtPosition(wopr1.getStart()).increaseDamage();
			}
			wopr2.setStart();
			ar = gb1.attack(wopr2.getStart());
			//shotDetails("wopr1",turn, gb1.getShotsFired(), wopr2.getCalledShotsSize(), gb1.getHitShotsSize(), ar.toString());
			wopr2.registerAttack(ar, wopr2.getStart());
			if(ar==AttackResult.DIRECT_HIT){
				System.out.printf("wopr2 hit wopr1's %s at %s\n", gb1.getShipAtPosition(wopr2.getStart()).getShipType(), wopr2.getStart().toString());
				gb1.getShipAtPosition(wopr2.getStart()).increaseDamage();
			}
			while(keepPlaying){
				drawMap(gb1, gb2);
				if(turn%2==0){
					aiTurn(wopr1,gb2,turn,"wopr1","wopr2");
					if(gb2.getShipsRemainingCount()==0){
						System.out.println("wopr1 wins!");
						//wopr1Win++;
						keepPlaying = false;
					}
				} else {
					aiTurn(wopr2,gb1,turn,"wopr2","wopr1");
					if(gb1.getShipsRemainingCount()==0){
						System.out.println("wopr1 wins!");
						//wopr1Win++;
						keepPlaying = false;
					}
				}
				turn++;
			//} //Uncomment for bulk testing
			repeat++;
		}
		//} //Uncomment for bulk testing
		//System.out.printf("Win statistics out of %d games\nwopr1     %d\nwopr2     %d", repeat, wopr1Win,wopr2Win);
	}

	/**
	 * Testing purposes only, used in early development to test main blocks were working properly
	 */
	public void test(){
		Carrier cv = new Carrier(new GridPosition('h',2), Orientation.HORIZONTAL);
		Battleship bb = new Battleship(new GridPosition('f',4), Orientation.VERTICAL);
		Cruiser ca = new Cruiser(new GridPosition('a',7), Orientation.VERTICAL);
		System.out.println("Carrier");
		for(GridPosition gp : cv.getBoardSpan()){
			System.out.printf("%s - %d\n", gp.row, gp.column);
		}
		System.out.println(bb.getShipType());
		for(GridPosition gp : bb.getBoardSpan()){
			System.out.printf("%s - %d\n", gp.row, gp.column);
		}
		
		GameBoard gb1 = new GameBoard();
		gb1.addShip(cv);
		//gb1.addShip(cv); //Checking to not add the same the same shi[ twice or not to add a ship that occupies the same space
		gb1.addShip(bb);
		gb1.addShip(ca);
		System.out.printf("Undestroyed ship count: %d\n", gb1.getShipsRemainingCount());
		for(int c = 0; c < cv.getSize(); c++){
			cv.increaseDamage();
			if(!cv.isDestroyed()){
				System.out.println("It's just a flesh wound");
			} else {
				System.out.println("Abandon ship");
			}
		}
		System.out.printf("Undestroyed ship count: %d\n", gb1.getShipsRemainingCount());
		
		System.out.println("Checking attack results");
		System.out.println("Attack (hit)");
		AttackResult r1 = gb1.attack(new GridPosition('h',3));
		System.out.println(r1.toString());
		System.out.println("Attack (miss)");
		AttackResult r2 = gb1.attack(new GridPosition('g',3));
		System.out.println(r2.toString());
		System.out.println("Attack (previous hit)");
		AttackResult r3 = gb1.attack(new GridPosition('h',3));
		System.out.println(r3.toString());
		System.out.println("Attack (previous miss)");
		AttackResult r4 = gb1.attack(new GridPosition('g',3));
		System.out.println(r4.toString());
		
		System.out.println("Checking getShotsFiredCount");
		System.out.printf("Shots fired: %d\n", gb1.getShotsFired());
		
		System.out.println("Checking getShipAtPosition");
		Ship check = gb1.getShipAtPosition(new GridPosition('h',3));
		System.out.println(check.getShipType());
	}
}