/*
 * General rules: Max 20 rupees on the field at once.
 * FIX BOMB DISPLAY NOT SHOWING, FIX UPDATES.
 */

import java.awt.*;
import java.awt.event.*;
import javax.swing.*; 
import javax.swing.border.Border;
import java.io.*; 
import javax.swing.text.*;

public class zelda {
    //All images.
    static ImageIcon link = new ImageIcon("link.jpg");
    static ImageIcon linkR = new ImageIcon("linkR.jpg");
    static ImageIcon linkL = new ImageIcon("linkL.jpg");
    static ImageIcon linkU = new ImageIcon("linkU.jpg");
    static ImageIcon swordU = new ImageIcon("sword.jpg");
    static ImageIcon swordR = new ImageIcon("swordR.jpg");
    static ImageIcon swordL = new ImageIcon("swordL.jpg");
    static ImageIcon swordD = new ImageIcon("swordD.jpg");
    static ImageIcon chuchu = new ImageIcon("mob.jpg");
    static ImageIcon rupeeImage = new ImageIcon("rupee.jpg");
    static ImageIcon bombImage = new ImageIcon("bomb.jpg");
    static ImageIcon backgroundImage = new ImageIcon("background.jpg");
    static ImageIcon bombDisplayImage = new ImageIcon("bombDisplay.jpg");
    static ImageIcon lifeDisplayImage = new ImageIcon("life.jpg");

    //Basic labels for sprites.
    static JFrame frame = new JFrame("Game");
    static JPanel pBig = new JPanel();
    static JLabel hero = new JLabel(link);
    static JLabel sword = new JLabel();
    static JLabel bomb = new JLabel();
    static JLabel mob1 = new JLabel(chuchu);
    static JLabel mob2 = new JLabel(chuchu);
    static JLabel background = new JLabel(backgroundImage);
    //Side panel variables.
    static int kills = 0;
    static int cash = 0;
    static JLabel display = new JLabel();
    static JLabel difficulty = new JLabel("Difficulty: 0");
    static JLabel statistics = new JLabel("Kills: " + kills + " Rupees: " + cash);
    //Global pos. and count variables.
    static boolean equipDrawn = false;
    static boolean shopOpen = false;
    static boolean disableMvmt = false;
    static int heroX = 400;
    static int heroY = 225;
    static int equipX = 400;
    static int equipY = 250;
    static int lives = 3;
    static int i = 0;
    static int q = 0;
    static int lastDir = 4; //For determining what position the sword will draw.
    static int bombDir = 0;
    static boolean bombPurchased = false;
    static String wepEquip = "sword";
    //Variable that controls monster speed, decreases with progress (monster speed is inversely correlated to tempo).
    static int tempo = 1000;
    //Rupee variables.
    static JLabel[] rupees = new JLabel[20]; //Max of 20 rupees on the field at once.
    static int rupeeCount = 0;
    static int[] rupeeX = new int[20];
    static int[] rupeeY = new int[20];
    //Mobs will spawn one on each side of map.
    static int mob1X =  25 * (int) (Math.random() * 12);
    static int mob1Y =  25 * (int) (Math.random() * 17);
    static int mob2X =  400 + 25 * (int) (Math.random() * 12);
    static int mob2Y =  25 * (int) (Math.random() * 17);
    //Shop variables and components.
    static JFrame shop = new JFrame("Shop");
    static JLabel cashDisplay = new JLabel(cash + " RUPEES");
    static JButton buyBomb = new JButton("Buy Bombs! (50)");
    static JLabel bombDisplay = new JLabel(bombDisplayImage);
    static JButton buyLife = new JButton("Buy Heart Piece! (100)");
    static JLabel lifeDisplay = new JLabel(lifeDisplayImage);

    public static void main(String[] args) {
        frame.setSize(1000,450);
        pBig.setLayout(null);
        shop.setSize(800,421);
        shop.setLayout(null);

        KeyListenerTester listener = new KeyListenerTester();
        ButtonHandler bListener = new ButtonHandler();

        //Adding rupees to game.
        for (int p = 0; p < 20; p++) {
            rupees[p] = new JLabel(rupeeImage);            
            rupees[p].setBounds(1,1,0,0);
            pBig.add(rupees[p]);
            rupeeX[p] = 1;
            rupeeY[p] = 1;
        }

        //MAIN PANEL
        //Placement.
        hero.setBounds(heroX,heroY,25,25);
        sword.setBounds(equipX,equipY,25,25);
        bomb.setBounds(equipX,equipY,25,25);
        display.setBounds(800,0,200,150);
        difficulty.setBounds(800,150,200,150);
        statistics.setBounds(800,300,200,150);
        background.setBounds(0,0,800,450);
        //Randomize placement of mobs.
        mob1.setBounds(mob1X,mob1Y,25,25);
        mob2.setBounds(mob2X,mob2Y,25,25);
        //Border and positioning.
        Border border = BorderFactory.createLineBorder(Color.BLACK, 1); 
        display.setBorder(border);
        difficulty.setBorder(border);
        statistics.setBorder(border);
        lifeDisplay.setBorder(border);
        bombDisplay.setBorder(border);
        display.setHorizontalAlignment(SwingConstants.CENTER);
        difficulty.setHorizontalAlignment(SwingConstants.CENTER);
        statistics.setHorizontalAlignment(SwingConstants.CENTER);
        //Adding.
        pBig.add(hero);
        pBig.add(sword);
        pBig.add(bomb);
        pBig.add(mob1);
        pBig.add(mob2);
        pBig.add(display);
        pBig.add(difficulty);
        pBig.add(statistics);
        pBig.add(background);

        //SHOP PANEL
        //Placement.
        cashDisplay.setBounds(10,10,150,50);
        bombDisplay.setBounds(10,70,200,200);
        buyBomb.setBounds(10,280,200,100);
        lifeDisplay.setBounds(220,70,200,200);
        buyLife.setBounds(220,280,200,100);
        //Font.
        cashDisplay.setFont(new Font("SANS-SERIF", Font.BOLD, 20));
        //Adding.
        shop.add(cashDisplay);
        shop.add(bombDisplay);
        shop.add(buyBomb);
        shop.add(buyLife);
        shop.add(lifeDisplay);

        hero.addKeyListener(listener);
        frame.addKeyListener(listener);
        pBig.addKeyListener(listener);
        shop.addKeyListener(listener);
        buyBomb.addActionListener(bListener);
        buyLife.addActionListener(bListener);

        frame.setFocusable(true);
        shop.setFocusable(true);
        buyBomb.setFocusable(false);
        buyLife.setFocusable(false);

        JOptionPane.showMessageDialog (null, "- Move with WASD keys. \n" + "- Press space to draw sword. \n" + "- Open shop with P.");
        JOptionPane.showMessageDialog (null, lives + " lives left.");

        frame.getContentPane().add(pBig); //panel to frame 
        frame.setVisible(true); // Shows frame on screen
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        while (i == 0) {
            //GAME UPDATES

            //BOMB THROW 
            if (bombDir != 0) { 
                while (q < 6) {
                    sleep(200);
                    if (bombDir == 1) {
                        equipX -= 25;
                    } else if (bombDir == 2) {
                        equipX += 25;
                    } else if (bombDir == 3) {
                        equipY -= 25;
                    } else if (bombDir == 4) {
                        equipY += 25;
                    }
                    bomb.setBounds(equipX,equipY,25,25);
                    mob();
                    q++;
                    disableMvmt = true;
                }
                bombDir = 0;
                q = 0;
                disableMvmt = false;
            } else {
                //Colored text.
                if (tempo >= 700) {
                    difficulty.setForeground(Color.green);
                    difficulty.setText("Difficulty: " + (1000 - tempo) + " (EASY)");
                } else if (tempo < 700 && tempo >= 400) {
                    difficulty.setForeground(Color.blue);
                    difficulty.setText("Difficulty: " + (1000 - tempo) + " (MEDIUM)");
                } else if (tempo < 400) {
                    difficulty.setForeground(Color.red);
                    difficulty.setText("Difficulty: " + (1000 - tempo) + " (HARD)");
                } else if (tempo == 100) {
                    JOptionPane.showMessageDialog (null, "Congratulations! You've beat the game!");
                    int confirm = JOptionPane.showConfirmDialog(
                            null,
                            "Would you like to keep playing?",
                            "Keep Playing?",
                            JOptionPane.YES_NO_OPTION);
                    if (confirm == JOptionPane.YES_OPTION) {
                        tempo-=20;
                        difficulty.setText("Difficulty: " + (1000 - tempo) + " (INSANE)");
                        cash+=1000;
                    } else {
                        System.exit(0);
                    }
                } else {
                    difficulty.setForeground(Color.orange);
                    difficulty.setText("Difficulty: " + (1000 - tempo) + " (INSANE)");
                sleep(tempo);

                statistics.setText("Kills: " + kills + " Rupees: " + cash);

                //MOB MOVEMENT
                //Number either -25, 0, or 25.
                mob1X += 25 * (int) (0.5 + Math.random() * 2) - 25;
                mob1Y += 25 * (int) (0.5 + Math.random() * 2) - 25;
                if (mob1Y < 0) {
                    mob1Y = 0;
                } else if (mob1Y > 425) {
                    mob1Y = 425;
                } else if (mob1X < 0) {
                    mob1X = 0;
                } else if (mob1X > 775) {
                    mob1X= 775;
                } 
                mob1.setBounds(mob1X,mob1Y,25,25);

                mob2X += 25 * (int) (0.5 + Math.random() * 2) - 25;
                mob2Y += 25 * (int) (0.5 + Math.random() * 2) - 25;       
                if (mob2Y < 0) {
                    mob2Y = 0;
                } else if (mob2Y > 425) {
                    mob2Y = 425;
                } else if (mob2X < 0) {
                    mob2X = 0;
                } else if (mob2X > 775) {
                    mob2X= 775;
                } 
                mob2.setBounds(mob2X,mob2Y,25,25);

                //Updating location.
                hero.setBounds(heroX,heroY,25,25);
                sword.setBounds(equipX,equipY,25,25);
            }
        }

    }

    private static class ButtonHandler implements ActionListener { 
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == buyBomb) {
                if (cash >= 50) {
                    int confirm = JOptionPane.showConfirmDialog(
                            null,
                            "Are you sure you'd like to buy this item? (50)",
                            "Confirm Purchase",
                            JOptionPane.YES_NO_OPTION);
                    if (confirm == JOptionPane.YES_OPTION) {
                        cash -= 50;
                        bombPurchased = true;
                        buyBomb.setEnabled(false);
                        JOptionPane.showMessageDialog (null, "Bombs Purchased! \n" + "Press 2 to equip.");
                    }
                } else {
                    JOptionPane.showMessageDialog (null, "Not enough rupees!");
                }
            } else if (e.getSource() == buyLife) {
                if (cash >= 100) {
                    int confirm = JOptionPane.showConfirmDialog(
                            null,
                            "Are you sure you'd like to buy this item? (100)",
                            "Confirm Purchase",
                            JOptionPane.YES_NO_OPTION);
                    if (confirm == JOptionPane.YES_OPTION) {
                        cash -= 100;
                        lives+=3;
                        JOptionPane.showMessageDialog (null, "Heart Container Purchased! \n" + "+3 Lives!");
                    }
                } else {
                    JOptionPane.showMessageDialog (null, "Not enough rupees!");
                }
            }
            cashDisplay.setText(cash + " RUPEES");
        }
    }

    private static class KeyListenerTester implements KeyListener {
        public void keyTyped(KeyEvent e) {

            //PLAYER MOVEMENT AND ACTIONS
            int key = e.getKeyChar();

            if (!disableMvmt) {
                if (key == 49) { //1
                    wepEquip = "sword";
                    bomb.setIcon(null);
                    display.setText("Sword Equipped!");
                } else if (key == 50 && bombPurchased == true) { //2
                    wepEquip = "bomb";
                    sword.setIcon(null);
                    bomb.setIcon(bombImage);
                    display.setText("Bomb Equipped!");
                }

                if (key == 97) { //A
                    heroX -= 25;
                    if (equipDrawn == true) {
                        if (wepEquip.equals("sword")) {
                            sword.setIcon(swordL);
                        } else if (wepEquip.equals("bomb")) {
                            bomb.setIcon(bombImage);
                        } 
                    }
                    equipX = heroX - 25;
                    equipY = heroY;
                    hero.setIcon(linkL);
                    lastDir = 1;
                }

                if (key == 100) { //D
                    heroX += 25;
                    if (equipDrawn == true) {
                        if (heroX >= 775) {
                            if (wepEquip.equals("sword")) {
                                sword.setIcon(null);
                                display.setText("Sword Sheathed!");
                                equipDrawn = false;
                            } else {
                                bomb.setIcon(null);
                            }   
                        } else {
                            if (wepEquip.equals("sword")) {
                                sword.setIcon(swordR);
                            } else if (wepEquip.equals("bomb")) {
                                bomb.setIcon(bombImage);
                            } 
                        }
                    } 
                    equipX = heroX + 25;
                    equipY = heroY;
                    hero.setIcon(linkR);
                    lastDir = 2;
                }

                if (key == 119) { //W      
                    heroY -= 25;
                    if (equipDrawn == true) {
                        if (wepEquip.equals("sword")) {
                            sword.setIcon(swordU);
                        } else if (wepEquip.equals("bomb")) {
                            bomb.setIcon(bombImage);
                        } 
                    }
                    equipY = heroY - 25;
                    equipX = heroX;
                    hero.setIcon(linkU);
                    lastDir = 3;
                }

                if (key == 115) { //S
                    heroY += 25;
                    if (equipDrawn == true) {
                        if (wepEquip.equals("sword")) {
                            sword.setIcon(swordD);
                        } else if (wepEquip.equals("bomb")) {
                            bomb.setIcon(bombImage);
                        } 
                    }
                    equipY = heroY + 25;
                    equipX = heroX;
                    hero.setIcon(link);
                    lastDir = 4;
                }

                if (key == 27) { //ESC
                    System.exit(0);
                }

                if (key == 112) { //SHOP (P)
                    if (shopOpen == false) {
                        shopOpen = true;
                        shop.setVisible(true);
                        cashDisplay.setText(cash + " RUPEES");
                    } else if (shopOpen == true) {
                        shopOpen = false;
                        shop.setVisible(false);
                    }
                }

                //Draws sword on keypress instead of holding down.
                if (e.getKeyChar() == KeyEvent.VK_SPACE) { //Uses equipped weapon.
                    if (wepEquip.equals("sword")) {
                        if (equipDrawn == false) {
                            if (heroX == 775) {
                                display.setText("Unable To Wield!");
                            } else {
                                equipDrawn = true;
                                display.setText("Sword Drawn!");
                                if (lastDir == 1) {
                                    sword.setIcon(swordL);  
                                } else if (lastDir == 2) {
                                    sword.setIcon(swordR);                            
                                } else if (lastDir == 3) {
                                    sword.setIcon(swordU);                            
                                } else {                    
                                    sword.setIcon(swordD);
                                }
                            }
                        } else if (equipDrawn == true) {
                            sword.setIcon(null);
                            equipDrawn = false;
                            display.setText("Sword Sheathed!");
                        }
                    } else if (wepEquip.equals("bomb")) { //Only one state of bomb, always drawn.
                        if (heroX == 775) {
                            display.setText("Unable To Throw!");
                        } else {
                            if (lastDir == 1) {
                                //Left throw.
                                bombDir = 1;
                            } else if (lastDir == 2) {
                                //Right throw.    
                                bombDir = 2;
                            } else if (lastDir == 3) {
                                //Upwards throw.      
                                bombDir = 3;
                            } else if (lastDir == 4) {                    
                                //Downwards throw.
                                bombDir = 4;
                            } 
                        }
                    }
                }
            }

            //Out of bounds, will prevent escaping the boundaries.
            if (heroY < 0) {
                heroY = 0;
            } else if (heroY > 425) {
                heroY = 425;
            } else if (heroX < 0) {
                heroX = 0;
            } else if (heroX > 775) {
                heroX = 775;
            } 

            //Do mob interactions.
            mob();

            //Landing on coins.
            for (int j = 0; j < 20; j++) {
                if (heroX == rupeeX[j] && heroY == rupeeY[j]) {
                    int k = (int) (Math.random() * ((5000/tempo) * (5000/tempo))) + 5; //scales as game goes on
                    //Rupee disappears.
                    rupees[j].setBounds(1,1,0,0);
                    rupeeX[j] = 1;
                    rupeeY[j] = 1;
                    cash += k;
                    rupeeCount--;
                    display.setText("+" + k + " Rupees!");
                }
            }

            //Death.
            if (heroX == mob1X && heroY == mob1Y || heroX == mob2X && heroY == mob2Y) {
                display.setText("You Died!");
                lives--;
                JOptionPane.showMessageDialog (null, lives + " lives left.");
                heroX = 400;
                heroY = 250;
                hero.setIcon(link);
                sword.setIcon(null);
            }

            //GG
            if (lives == 0) {
                JOptionPane.showMessageDialog (null, "OUT OF LIVES! GAME OVER");
                System.exit(0);
            }

            //Updating location.
            hero.setBounds(heroX,heroY,25,25);
            sword.setBounds(equipX,equipY,25,25);
            bomb.setBounds(equipX,equipY,25,25);
        }

        @Override
        public void keyPressed(KeyEvent e) {

        }

        @Override
        public void keyReleased(KeyEvent e) {

        }
    }

    public static void sleep(int time) {
        try {
            Thread.sleep(time);
        } catch (Exception e) {

        }
    } 

    public static void mob() {
        //Mob interactions.
        if (equipX == mob1X && equipY == mob1Y && !wepEquip.equals("") && equipDrawn == true) {
            display.setText("Mob Killed!");
            //Replaces mob with rupees. Gets X and Y and puts into position of rupee array.
            rupeeX[rupeeCount] = mob1X + 25 * (int) (0.5 + Math.random() * 2) - 25;
            rupeeY[rupeeCount] = mob1Y + 25 * (int) (0.5 + Math.random() * 2) - 25;
            rupees[rupeeCount].setBounds(rupeeX[rupeeCount],rupeeY[rupeeCount],25,25);
            rupeeCount++;
            //Relocates mob.
            mob1X = 25 * (int) (Math.random() * 12);
            mob1Y = 25 * (int) (Math.random() * 17);
            mob1.setBounds(mob1X,mob1Y,25,25);
            tempo -= 20;
            kills++;
        } else if (equipX == mob2X && equipY == mob2Y && !wepEquip.equals("") && equipDrawn == true) {
            display.setText("Mob Killed!");
            //Replaces mob with rupees.
            rupeeX[rupeeCount] = mob2X + 25 * (int) (0.5 + Math.random() * 2) - 25;
            rupeeY[rupeeCount] = mob2Y + 25 * (int) (0.5 + Math.random() * 2) - 25;
            rupees[rupeeCount].setBounds(rupeeX[rupeeCount],rupeeY[rupeeCount],25,25);
            rupeeCount++;
            //Relocates mob.
            mob2X =  400 + 25 * (int) (Math.random() * 12);
            mob2Y =  25 * (int) (Math.random() * 17);
            mob2.setBounds(mob2X,mob2Y,25,25);
            tempo -= 20;
            kills++;
        }
    }

}
