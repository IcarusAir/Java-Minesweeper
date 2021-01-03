/*
 * Nicholas Mair
 * ICS3U - Intro to Computer Studies
 * November 2018
 * Creates the Window from Minesweeper
 */
import javax.swing.JFrame;
import java.util.Scanner;

public class RunMe extends JFrame {
	private static final long serialVersionUID = 1L;
	public static boolean runs = true;
	
	public static void main(String[] args) {
		if (runs) {	
			Scanner sc = new Scanner(System.in);
			Intro(sc);
			sc.close();
		}
		
	}
	public RunMe() {
		super("Minesweeper");
		
		setContentPane(new Minesweeper());
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//Necessary to close window
        setSize(400,400);
        setVisible(true);
	}
	public static void Intro(Scanner sc) {
		runs = false;
		System.out.println("Welcome to Minesweeper!");
				System.out.println("------------------------------------------------");
				System.out.println("Have you played before?");
				String answer = sc.next();
				answer = answer.toLowerCase();
				if (answer.equals("no")) {
					System.out.println("RULES:");
					System.out.println("1. Minesweeper is a game where there are 25 mines in a 12x12 grid, your job is to mark all of the mines with flags without detonating"
							+ " them, the mines will detonate if you dig them up.");
					System.out.println("2. Use the mouse and click to select a square, then click the \"D\" button to dig the square and the \"F\" button to flag or unflag a square.");
					System.out.println("3. The game can only be won once exactly 25 flags have marked every single bomb.");
					System.out.println("4. Clue squares (squares with a number on it) tell you how many mines there are in the 8 squares around it.");
					System.out.println("5. Bomb squares should never be dug, only marked. Once they are dug, the game is lost.");
					System.out.println("6. Blank squares will dig all 8 squares around it (Don't worry, a bomb is never beside a blank square).");
					System.out.println("7. Once all bombs have been flagged, you win the game!");
				}
				System.out.println("Enter anything to continue");
				answer = sc.next();
				new RunMe();
				System.out.println("Open the window that just appeared, and let's play!");
	}

}
