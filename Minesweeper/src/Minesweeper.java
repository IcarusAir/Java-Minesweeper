/*
 * Nicholas Mair
 * ICS3U Final Project
 * January 2019
 * Mine Sweeper drawings and game content
 * 
 *   1  |  2  |  3  
 *   --------------
 *   4  |  5  |  6
 *   --------------
 *   7  |  8  |  9
 */

import java.awt.Graphics; //Graphics
import java.awt.Color;
import java.awt.Font;

import javax.swing.AbstractButton; //Buttons and Window
import javax.swing.JPanel;
import javax.swing.JButton;

import java.awt.event.MouseEvent; //Mouse and Button Listeners
import java.awt.event.MouseListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.*;

public class Minesweeper extends JPanel implements MouseListener,ActionListener{
	private static final long serialVersionUID = 1L; 

	//All variables used across multiple methods
	public static boolean[] bombID;
	public static int mouseX = 15,mouseY = 40,oldX,oldY;
	public static int bombsLeft = 25; //25 Bombs
	public static int[] mineRN = new int[25];
	public static int[] clues = new int[145];
	public static int winCount = 0;
	public static boolean runs = true;
	protected JButton Fbutt,Dbutt;
	public boolean win = false;
	public boolean lose = false;
	
	public String buttIn = "nothing";
	public static int index;
	public boolean buttonPress = false;
	
	//Ranges Used for calculations
	static int[] range1 = {1,13,25,37,49,61,73,85,97,109,121,133};
	static int[] range2 = {12,24,36,48,60,72,84,96,108,120,132,144};;

	//Important Variables
	public static int[] xValue = new int[150];
	public static int[] yValue = new int[150];
	public boolean[] dug = new boolean[145];
	public boolean[] flagged = new boolean[145];
	public boolean[] blanks = new boolean[145];

	public Minesweeper() {                           //All buttons are added and defined here, mouse listener is also added
		addMouseListener(this);
		
		//Buttons
		Dbutt = new JButton("D");
		Dbutt.setVerticalTextPosition(AbstractButton.TOP);

		Dbutt.setActionCommand("dig");
		
		Fbutt = new JButton("F");
		Fbutt.setVerticalTextPosition(AbstractButton.TOP);
		Fbutt.setHorizontalTextPosition(AbstractButton.LEFT);
		Fbutt.setMnemonic(KeyEvent.VK_F);
		Fbutt.setActionCommand("flag");
		
		Dbutt.addActionListener(this);
        Fbutt.addActionListener(this);
        
      //Add Components to this container, using the default FlowLayout.
        add(Dbutt);
        add(Fbutt);
	}

	public void paintComponent(Graphics g) {                      //Main content of the game, one run through is one turn
		if (runs) {
			vars(g); //Runs the variable definitions (Only once)
		}
		if (!(win || lose)) {  //This runs if the game hasn't ended
			drawDeSelect(oldX,oldY,g);    //Update red selection square position
			drawSelect(mouseX,mouseY,g);
			
			for (int i = 1;i<145;i++) { //Defines index, the variable that controls the index of every array variable
				if (mouseX == xValue[i] && mouseY == yValue[i]) {
					index = i;
				}
			}
			
			if (buttonPress) {   //Prevents infinite loops
				buttonPress = false;
				if (buttIn.equals("dig")) {  //Is the Dig button pressed?
					if (!dug[index]) {
						dug[index] = true;
						for (int i = 0;i<25;i++) { //Mine Dug
							if (index == mineRN[i]) {
								lose = true;
								drawMine(xValue[index],yValue[index],g);
							}
						}
						if (!lose) { //only runs if you haven't dug a bomb
							for (int i = 1;i<145;i++) {
								if (index == i) {
									drawClue(xValue[index],yValue[index],clues[i],g);
									if (clues[i] == 0) {
										DigAndCheck(g); //Only runs if the space is blank
									}
								}
							}
						}
					}
				}
				if (buttIn.equals("flag")) {   //Is the Flag button pressed?
					if (flagged[index] && !dug[index]) { //Only runs if space is already Flagged
						drawFill(xValue[index],yValue[index],g);
						flagged[index] = false;
						bombsLeft ++;
						if (!(bombsLeft == 0)) {
							drawBombsLeft(g);
						}
						WinCondition();
					}
					else if (!flagged[index] && !dug[index]){ //Only runs if space is Blank
						drawFlag(xValue[index],yValue[index],g);
						bombsLeft--;
						if (!(bombsLeft == 0)) {
							drawBombsLeft(g);
						}
						flagged[index] = true;
						WinCondition();
					}
				}
			}
		}
		else if (win) {
			drawBombsLeft(g);
			System.out.println("\nCongratulations! You Win!");
			winScreen(g);
		}
		else if (lose) {
			System.out.println("Too Bad, You Exploded!");
			for (int i = 0;i<25;i++) {
				drawMine(xValue[mineRN[i]],yValue[mineRN[i]],g);
			}
		}
		
		
	}
	public void vars(Graphics g) {                                //All complex variable definitions
		g.setColor(Color.gray);
		g.fillRect(0,0,400,400);
		g.setColor(Color.black);
		int x = 31;
		for (int i = 0;i<11;i++) {
			g.drawLine(x, 26, x, 400);
			x+=32;
		}
		int y = 26;
		for (int i = 0;i<12;i++) {
			g.drawLine(0, y, 400, y);
			y+=28;
		}
		drawBombsLeft(g);
		for (int i = 1;i<145;i++) {
			flagged[i] = false;
		}
		for (int i = 1;i<145;i++) {
			dug[i] = false;
		}
		//Drawing Done ---------------------------------------------------
		int add;
		int count1 = 0, count2 = 40;
		for (int i = 0;i<150;i+=10) {
			for (int j = 0;j<10;j++) {
				add = j+i;
				while (count1 < 133) {
					if (add>count1) {
						yValue[add] = count2;
						for (int k = 0;k<12;k++) {
							if (add-12*k == 1) {xValue[add] = 15;}
							else if (add-12*k == 2) {xValue[add] = 48;}
							else if (add-12*k == 3) {xValue[add] = 80;}
							else if (add-12*k == 4) {xValue[add] = 112;}
							else if (add-12*k == 5) {xValue[add] = 144;}
							else if (add-12*k == 6) {xValue[add] = 176;}
							else if (add-12*k == 7) {xValue[add] = 208;}
							else if (add-12*k == 8) {xValue[add] = 240;}
							else if (add-12*k == 9) {xValue[add] = 272;}
							else if (add-12*k == 10) {xValue[add] = 304;}
							else if (add-12*k == 11) {xValue[add] = 336;}
							else if (add-12*k == 12) {xValue[add] = 368;}
						}	
					}
					count1 += 12;
					count2 += 28;
				}
				count1 = 0;
				count2 = 40;
			}
		}
		
		
		//Making Bombs
		Random rn = new Random();
		for (int i = 0;i<25;i++) { //WORKS, HA!
			mineRN[i] = rn.nextInt(144)+1;
			for (int j = 0;j<i;j++) {
				while (mineRN[i] == mineRN[j]) {
					mineRN[i] = rn.nextInt(144)+1;
					i = 0;
				}
			}
		}
		//Making Clues
		int minesAround = 0;
		for (int i = 1;i<145;i++) {
			minesAround = 0;
			if (i == 1) { //Top Left Corner
				for (int j = 0;j<25;j++) {
					if (i+1 == mineRN[j]) {minesAround+=1;}//right
					if (i+12 == mineRN[j]) {minesAround+=1;}//bottom
					if (i+13 == mineRN[j]) {minesAround+=1;}//bottom-right
				}
			}
			else if (i == 12) {//Top Right Corner
				for (int j = 0;j<25;j++) {
					if (i-1 == mineRN[j]) {minesAround+=1;}//left
					if (i+11 == mineRN[j]) {minesAround+=1;}//bottom-left
					if (i+12 == mineRN[j]) {minesAround+=1;}//bottom
				}
			}
			else if (i == 133) {//Bottom Left Corner
				for (int j = 0;j<25;j++) {
					if (i-12 == mineRN[j]) {minesAround+=1;}//top
					if (i-11 == mineRN[j]) {minesAround+=1;}//top-right
					if (i+1 == mineRN[j]) {minesAround+=1;}//right
				}
			}
			else if (i == 144) {//Bottom Right Corner
				for (int j = 0;j<25;j++) {
					if (i-13 == mineRN[j]) {minesAround+=1;}//top-left
					if (i-12 == mineRN[j]) {minesAround+=1;}//top
					if (i-1 == mineRN[j]) {minesAround+=1;}//left
				}
			}
			else if (i < 12 && i > 1) {//Top Row
				for (int j = 0;j<25;j++) {
					if (i-1 == mineRN[j]) {minesAround+=1;}//left
					if (i+1 == mineRN[j]) {minesAround+=1;}//right
					if (i+11 == mineRN[j]) {minesAround+=1;}//bottom-left
					if (i+12 == mineRN[j]) {minesAround+=1;}//bottom
					if (i+13 == mineRN[j]) {minesAround+=1;}//bottom-right
				}
			}
			else if (i<144 && i > 133){//Bottom Row
				for (int j = 0;j<25;j++) {
					if (i-13 == mineRN[j]) {minesAround+=1;}//top-left
					if (i-12 == mineRN[j]) {minesAround+=1;}//top
					if (i-11 == mineRN[j]) {minesAround+=1;}//top-right
					if (i-1 == mineRN[j]) {minesAround+=1;}//left
					if (i+1 == mineRN[j]) {minesAround+=1;}//right
				}
			}
			else if (i == 13||i == 25||i == 37||i == 49||i == 61||i == 73||i == 85||i == 97||i == 109||i == 121||i == 133){ //Left Column
				for (int j = 0;j<25;j++) {
					if (i-12 == mineRN[j]) {minesAround+=1;}//top
					if (i-11 == mineRN[j]) {minesAround+=1;}//top-right
					if (i+1 == mineRN[j]) {minesAround+=1;}//right
					if (i+12 == mineRN[j]) {minesAround+=1;}//bottom
					if (i+13 == mineRN[j]) {minesAround+=1;}//bottom-right
				}
			}
			else if (i == 12||i == 24||i == 36||i == 48||i == 60||i == 72||i == 84||i == 96||i == 108||i == 120||i == 132){ //Right Column
				for (int j = 0;j<25;j++) {
					if (i-13 == mineRN[j]) {minesAround+=1;}//top-left
					if (i-12 == mineRN[j]) {minesAround+=1;}//top
					if (i-1 == mineRN[j]) {minesAround+=1;}//left
					if (i+11 == mineRN[j]) {minesAround+=1;}//bottom-left
					if (i+12 == mineRN[j]) {minesAround+=1;}//bottom
				}
			}
			else { //Every other square
				for (int j = 0;j<25;j++) {
					if (i-13 == mineRN[j]) {minesAround+=1;}//top-left
					if (i-12 == mineRN[j]) {minesAround+=1;}//top
					if (i-11 == mineRN[j]) {minesAround+=1;}//top-right
					if (i-1 == mineRN[j]) {minesAround+=1;}//left
					if (i+1 == mineRN[j]) {minesAround+=1;}//right
					if (i+11 == mineRN[j]) {minesAround+=1;}//bottom-left
					if (i+12 == mineRN[j]) {minesAround+=1;}//bottom
					if (i+13 == mineRN[j]) {minesAround+=1;}//bottom-right
				}
				
			}
			clues[i] = minesAround;
		}
		for (int i = 1;i<145;i++) {
			if (clues[i] == 0) {
				blanks[i] = true;
				for (int j = 0;j<25;j++) {
					if (i==mineRN[j]) {
						blanks[i] = false;
					}
				}
			}
		}
		//for (int i = 1;i<145;i++) {
			//drawClue(xValue[i],yValue[i],clues[i],g);
		//}
		//for (int i = 0;i<25;i++) {
		//	drawMine(xValue[mineRN[i]],yValue[mineRN[i]],g);
		//}
		runs = false;
	}
	
	public void WinCondition() {                                  //Checks If the game has been won
		if (bombsLeft == 0) {
			int winCount = 0;
			for (int i = 1;i<145;i++) {
				if (flagged[i]) {
					for (int j = 0;j<25;j++) {
						if (i == mineRN[j]) {
							winCount++;
						}
					}
				}
			}
			if (winCount == 25) {
				win = true;
			}
		}
	}
	public static void drawMine(int x,int y,Graphics g) {         //Draws a Mine
		if (x<16) {x=16;};
		if (y<14) {y=14;};
		g.setColor(Color.red);
		g.fillRect(x-16,y-13,31,27);
		g.setColor(Color.black);
		g.fillOval(x-10, y-10, 20,20);
		g.fillRect(x-1, y-14, 3, 28);
		g.fillRect(x-14, y-1, 28, 3); 
		g.drawLine(x-10, y+10, x+10, y-10);
		g.drawLine(x+10, y+10, x-10, y-10);
		
	}
	public static void drawBombsLeft(Graphics g) {                //Draws the "Bombs Left" in the top right corner
		g.setColor(Color.black);
		g.fillRect(350, 5, 35, 20);
		g.setColor(Color.white);
		g.drawString(Integer.toString(bombsLeft), 360, 20);
		g.setFont(new Font("Arial", Font.BOLD, 14));
		g.setColor(Color.black);
		g.drawString("Bombs Left:", 255, 20);
		
	}
	public static void drawFlag(int x,int y,Graphics g) {         //Draws a Flag
		if (x<16) {x=16;};
		if (y<14) {y=14;};
		g.setColor(Color.red);
		g.fillRect(x-5,y-8,13,10);
		g.setColor(Color.black);
		g.fillRect(x-8, y-8, 3, 20);
		
	}
	public static void drawClue(int x,int y,int id,Graphics g) {  //Draws a Clue
		if (x<16) {x=16;};
		if (y<14) {y=14;};
		g.setColor(Color.lightGray);
		g.fillRect(x-16,y-13,31,27);
		g.setFont(new Font("Arial", Font.BOLD, 24));
		if (id == 1) {g.setColor(Color.blue);g.drawString("1", x-8, y+10);}
		if (id == 2) {g.setColor(Color.green);g.drawString("2", x-8, y+10);}
		if (id == 3) {g.setColor(Color.red);g.drawString("3", x-8, y+10);}
		if (id == 4) {g.setColor(Color.magenta);g.drawString("4", x-8, y+10);}
		if (id == 5) {g.setColor(Color.pink);g.drawString("5", x-8, y+10);}
		if (id == 6) {g.setColor(Color.yellow);g.drawString("6", x-8, y+10);}
		if (id == 7) {g.setColor(Color.orange);g.drawString("7", x-8, y+10);}
		if (id == 8) {g.setColor(Color.cyan);g.drawString("8", x-8, y+10);}
		drawDeSelect(x,y,g);
	}
	public static void drawSelect(int x,int y,Graphics g) {       //Draws the selection box
		if (x<16) {x=16;};
		if (y<14) {y=14;};
		g.setColor(Color.red);
		g.drawRect(x-16,y-13,30,26);
		g.drawRect(x-15,y-12,28,24);
	}
	public static void drawDeSelect(int x,int y,Graphics g) {     //Erases the selection box
		if (x<16) {x=16;};
		if (y<14) {y=14;};
		g.setColor(Color.gray);
		g.drawRect(x-16,y-13,30,26);
		g.drawRect(x-15,y-12,28,24);
	}
	public static void drawBlank(int x, int y,Graphics g){        //Draws a blank (light gray)
		if (x<16) {x=16;};
		if (y<14) {y=14;};
		g.setColor(Color.lightGray);
		g.fillRect(x-16,y-13,30,26);
		drawDeSelect(x,y,g);
	}
	public static void drawFill(int x, int y,Graphics g){         //Draws a blank (gray)
		if (x<16) {x=16;};
		if (y<14) {y=14;};
		g.setColor(Color.gray);
		g.fillRect(x-16,y-13,30,26);
	}
	
	public void DigAndCheck(Graphics g) {                  //Digs an 8-block circle when blank
		int rIndex1 = 0,rIndex2 = 0,rIndex3 = 0,rIndex4 = 0,rIndex5 = 0,rIndex6 = 0,rIndex7 = 0;
		for (int i = 0;i<12;i++) {
			if (index > range1[i]-1 && index < range2[i]+1) {rIndex1 = i;}
			if (index-1 > range1[i]-1 && index-1 < range2[i]+1) {rIndex2 = i;}
			if (index+1 > range1[i]-1 && index+1 < range2[i]+1) {rIndex3 = i;}
			if (index-11 > range1[i]-1 && index-11 < range2[i]+1) {rIndex4 = i;}
			if (index-13 > range1[i]-1 && index-13 < range2[i]+1) {rIndex5 = i;}
			if (index+11 > range1[i]-1 && index+11 < range2[i]+1) {rIndex6 = i;}
			if (index+13 > range1[i]-1 && index+13 < range2[i]+1) {rIndex7 = i;}
		}
		if (rIndex1 == rIndex5+1) {drawClue(xValue[index-13],yValue[index-13],clues[index-13],g);dug[index-13] = true;}
		if (index-12 > 0) {drawClue(xValue[index-12],yValue[index-12],clues[index-12],g);dug[index-12] = true;}
		if (rIndex1 == rIndex4+1) {drawClue(xValue[index-11],yValue[index-11],clues[index-11],g);dug[index-11] = true;}
		if (rIndex1 == rIndex2) {drawClue(xValue[index-1],yValue[index-1],clues[index-1],g);dug[index-1] = true;}
		if (rIndex1 == rIndex3) {drawClue(xValue[index+1],yValue[index+1],clues[index+1],g);dug[index+1] = true;}
		if (rIndex1 == rIndex6-1) {drawClue(xValue[index+11],yValue[index+11],clues[index+11],g);dug[index+11] = true;}
		if (index+12 < 145) {drawClue(xValue[index+12],yValue[index+12],clues[index+12],g);dug[index+12] = true;}
		if (rIndex1 == rIndex7-1) {drawClue(xValue[index+13],yValue[index+13],clues[index+13],g);dug[index+13] = true;}
		
		
	}
	
	@Override
	public void mouseClicked(MouseEvent e) { //Runs every time a mouse is clicked
		oldX = mouseX;
		oldY = mouseY;
		mouseX = e.getX();
        mouseY = e.getY();
        for (int i = 1;i<145;i++) {
       	 	if (mouseX <= xValue[i]+16 && mouseX >= xValue[i]-16){
       		 	mouseX = xValue[i];
       	 	}
       	 	if (mouseY <= yValue[i]+14 && mouseY >= yValue[i]-14){
       	 		mouseY = yValue[i];
       	 	}
       	 	if (mouseY < 40) {
       	 		mouseY = yValue[i];
       	 	}
        }
        repaint(); //Goes back to paintComponent
	}
	@Override
	public void actionPerformed(ActionEvent e) { //Runs every time a button is pressed
		buttIn = e.getActionCommand();
		buttonPress = true;
		repaint(); //Goes back to paintComponent
	}
	
	
	@Override
	public void mouseExited(MouseEvent arg0) {//Don't Need - used if a mouse exits the window
	}
	@Override
	public void mousePressed(MouseEvent arg0) {//Don't Need - used if the mouse is pressed
	}
	@Override
	public void mouseReleased(MouseEvent arg0) {//Don't Need - used if the mouse is released
	}
	@Override
	public void mouseEntered(MouseEvent arg0) {	//Don't Need - used if the mouse enters the window
	}

	public void winScreen(Graphics g) {
		g.setColor(Color.green);
		g.drawOval(0, 0, 380, 380);
	}
}