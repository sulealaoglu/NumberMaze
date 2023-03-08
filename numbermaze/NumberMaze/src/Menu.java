import enigma.console.TextAttributes;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Menu {
    private KeyListener klis;
    private int keypr, rkey =0;
    private enigma.console.Console cn = Main.cn;
    private Color backcolor = new Color(243, 213, 191);
    private TextAttributes back = new TextAttributes( Color.black,backcolor);

    public Menu() throws InterruptedException {
        klis = new KeyListener() {
            public void keyTyped(KeyEvent e) {
            }

            public void keyPressed(KeyEvent e) {
                if (keypr == 0) {
                    keypr = 1;
                    rkey = e.getKeyCode();
                }
            }

            public void keyReleased(KeyEvent e) {
            }
        };
        cn.getTextWindow().addKeyListener(klis);
        int x, y, choice = 0;
        int directionCheck = 12;
        String music = "              .,,,.n" +
                "           .;;;;;;;;;,n" +
                "          ;;;'    `;;;,n" +
                "         ;;;'      `;;;n" +
                "         ;;;        ;;;n" +
                "         ;;;.      ;;;'n" +
                "         `;;;.    ;;;'n" +
                "          `;;;.  ;;;'n" +
                "           `;;',;;'n" +
                "            ,;;;'n" +
                "         ,;;;',;' ...,,,,...n" +
                "      ,;;;'    ,;;;;;;;;;;;;;;,n" +
                "   ,;;;'     ,;;;;;;;;;;;;;;;;;;,n" +
                "  ;;;;'     ;;;',,,   `';;;;;;;;;;n" +
                " ;;;;,      ;;   ;;;     ';;;;;;;;;n" +
                ";;;;;;       '    ;;;      ';;;;;;;n" +
                ";;;;;;            .;;;      ;;;;;;;n" +
                ";;;;;;,            ;;;;     ;;;;;;'n" +
                " ;;;;;;,            ;;;;    ;;;;;'n" +
                "  `;;;;;             ;;;;   ;;;;'n" +
                "   `;;;;;             ;;;;  ;;'n" +
                "      `;;;;;;;;;;;;;;;;;;;;;'n" +
                "          '''''''''''''';;;.n" +
                "                         ;;;.n" +
                "               ,,,,,      ;;;.n" +
                "              ;;;; '      ;;;;n" +
                "              ;;;;,      ;;;;n" +
                "              `;;;;;;;;;;;;;'n" +
                "                `;;;;;;;;;'";

        sMenuPrint(8,3,music, back);
        cn.getTextWindow().setCursorPosition(69,10);
        cn.getTextWindow().output(" PLEASE CHOOSE MUSIC ", back);
        cn.getTextWindow().setCursorPosition(76,12);
        cn.getTextWindow().output(" PACMAN ", back);
        cn.getTextWindow().setCursorPosition(76,13);
        cn.getTextWindow().output(" MARIO  ", back);
        cn.getTextWindow().setCursorPosition(76,14);
        cn.getTextWindow().output(" ZELDA  ", back);
        String music2 = "              I;         n" +
                "             +It.         n" +
                "             +II+         n" +
                "             +III;        n" +
                "             +IIII;       n" +
                "             iIIIII;      n" +
                "             titIIII;     n" +
                "             I;.iIIII;    n" +
                "            .I;  =IIII;   n" +
                "            .I;   ;IIII:  n" +
                "            .I;    :tIIt  n" +
                "            .I;     .tII= n" +
                "            .I;      :III n" +
                "            :I;       +II;n" +
                "            :I;       .II=n" +
                "            :I:        iIin" +
                "            :I.        ;Iin" +
                "            ;I.        =I;n" +
                "            ;I.        tt n" +
                "            +I        ;I; n" +
                "            +I       ;I;  n" +
                "            tI      ;I;   n" +
                "     :;itIIIII     +t:    n" +
                "   ;tIIIIIIIII   :t;      n" +
                " .tIIIIIIIIIIi  ;i.       n" +
                ".tIIIIIIIIIII;.;:         n" +
                ";IIIIIIIIIII;             n" +
                ";IIIIIIIIIt;              n" +
                " ;tIIIIt+;                n" +
                "   ....";

        sMenuPrint(113,2,music2, back);
        Main.path = "pacman.wav";
        Main.msc.playMusic(Main.path, true);
        int mcd =1;
        boolean menucheck=true;
        while(true) {
            cn.getTextWindow().output(67, directionCheck, '<');
            cn.getTextWindow().output(89, directionCheck, '>');
            if (keypr == 1) {
                if (rkey == KeyEvent.VK_DOWN) {
                    cn.getTextWindow().output(67, directionCheck, ' ');
                    cn.getTextWindow().output(89, directionCheck, ' ');
                    if(directionCheck == 14){
                        directionCheck=12;
                    }
                    else
                        directionCheck++;
                } else if (rkey == KeyEvent.VK_UP) {
                    cn.getTextWindow().output(67, directionCheck, ' ');
                    cn.getTextWindow().output(89, directionCheck, ' ');
                    if(directionCheck == 12){
                        directionCheck=14;
                    }
                    else
                        directionCheck--;
                } else if (rkey == KeyEvent.VK_ENTER) {
                    mcd++;
                    ClearConsole();
                    menucheck=true;
                }

                choice = directionCheck - 11;
                switch (mcd) {
                    case 1:{
                        switch (choice) {
                            case 1 :
                                Main.path="pacman.wav";
                                break;
                            case 2:
                                Main.path = "mario.wav";
                                break;
                            case 3:
                                Main.path = "zelda.wav";
                                break;
                        }
                        Main.msc.stop();
                        Main.msc.playMusic(Main.path, true);
                        break;
                    }
                    case 2:{
                        if(menucheck) {
                            TextAttributes colorr = new TextAttributes(Color.black,Main.clr);
                            Color lilac=new Color(124, 139, 253);
                            TextAttributes attr = new TextAttributes(Color.black,lilac);
                            Color pink=new Color(200, 162, 200);
                            TextAttributes attr1 = new TextAttributes(Color.BLACK, pink);
                            cn.getTextWindow().setCursorPosition(68, 10);
                            cn.getTextWindow().output(" PLEASE CHOOSE COLOR ",back);
                            cn.getTextWindow().setCursorPosition(73, 12);
                            cn.getTextWindow().output(" LIGHT BLUE ", colorr);
                            cn.getTextWindow().setCursorPosition(73, 13);
                            cn.getTextWindow().output("   LILAC    ", attr);
                            cn.getTextWindow().setCursorPosition(73, 14);
                            cn.getTextWindow().output("   PINK     ", back);
                            menucheck=false;

                        }
                        TextAttributes colorr = null;
                        switch (choice) {
                            case 1:
                                colorr = new TextAttributes(Main.clr);
                                Main.attr= new TextAttributes(Main.clr,Main.clr);
                                break;
                            case 2:
                                Color lilac=new Color(124, 139, 253);
                                colorr = new TextAttributes(lilac);
                                Main.attr= new TextAttributes(lilac,lilac);
                                break;
                            case 3:
                                Color pink=new Color(200, 162, 200);
                                colorr =new TextAttributes(backcolor);
                                Main.attr=new TextAttributes(backcolor,backcolor);
                                break;
                        }

                        String string =
                                       "  ___________________________________ n" +
                                      "| _____ |   | ___ | ___ ___ | |   | |n" +
                                      "| |   | |_| |__ | |_| __|____ | | | |n" +
                                      "| | | |_________|__ |______ |___|_| |n" +
                                      "| |_|   | _______ |______ |   | ____|n" +
                                      "| ___ | |____ | |______ | |_| |____ |n" +
                                      "|___|_|____ | |   ___ | |________ | |n" +
                                      "|   ________| | |__ | |______ | | | |n" +
                                      "| | | ________| | __|____ | | | __| |n" +
                                      "|_| |__ |   | __|__ | ____| | |_| __|n" +
                                      "|   ____| | |____ | |__ |   |__ |__ |n" +
                                      "|_________|_______|___|___|___|_____|";





                        sMenuPrint(59,18,string,colorr);
                        break;
                    }
                    case 3:{
                        if(menucheck) {
                            cn.getTextWindow().setCursorPosition(63, 10);
                            cn.getTextWindow().output(" PLEASE CHOOSE DIFFICULTY LEVEL ", back);
                            cn.getTextWindow().setCursorPosition(75, 12);
                            cn.getTextWindow().output("  EASY  ", back);
                            cn.getTextWindow().setCursorPosition(75, 13);
                            cn.getTextWindow().output(" NORMAL ", back);
                            cn.getTextWindow().setCursorPosition(75, 14);
                            cn.getTextWindow().output("  HARD  ", back);
                            menucheck = false;
                        }
                        Main.difficulty = 35-10*(choice-1);
                        String difficult =
                                "   ________________________________________n" +
                                        " /                                        \\n" +
                                        "|   +-----------------------------------+  |n" +
                                        "|   |               EASY                |  |n" +
                                        "|   +-----------------------------------+  |n" +
                                        "|                                          |n" +
                                        "|                    90                    |n" +
                                        "|                                          |n" +
                                        "|                                          |n" +
                                        "|                                          |n" +
                                        "|    0      <------ (+)               180  |n" +
                                        " \\________________________________________/";


                        String difficult2 =
                                "   ________________________________________n" +
                                        " /                                        \\n" +
                                        "|   +-----------------------------------+  |n" +
                                        "|   |              NORMAL               |  |n" +
                                        "|   +-----------------------------------+  |n" +
                                        "|                                          |n" +
                                        "|                    90                    |n" +
                                        "|                    ^                     |n" +
                                        "|                    |                     |n" +
                                        "|                    |                     |n" +
                                        "|    O              (+)               180  |n" +
                                        " \\________________________________________/";

                        String difficult3 =
                                "   ________________________________________n" +
                                        " /                                        \\n" +
                                        "|   +-----------------------------------+  |n" +
                                        "|   |               HARD                |  |n" +
                                        "|   +-----------------------------------+  |n" +
                                        "|                                          |n" +
                                        "|                    90                    |n" +
                                        "|                                          |n" +
                                        "|                                          |n" +
                                        "|                                          |n" +
                                        "|    O              (+) ------>       180  |n" +
                                        " \\________________________________________/";
                        if (Main.difficulty == 35) {
                            sMenuPrint(55,16,difficult,new TextAttributes(backcolor));
                        }
                        else if (Main.difficulty == 25) {
                            sMenuPrint(55,16,difficult2,new TextAttributes(backcolor));
                        }
                        else
                            sMenuPrint(55,16,difficult3,new TextAttributes(backcolor));

                        break;
                    }
                }
                keypr = 0;
            }
            Thread.sleep(20);
            if(mcd == 4){
                ClearConsole();
                break;
            }

        }
    }
    public void ClearConsole() {
        for (int i = 0; i < 33; i++) {
            for (int j = 0; j <150; j++) {
                cn.getTextWindow().setCursorPosition(j,i);
                cn.getTextWindow().output(" ");
            }
        }
    }
    public void sMenuPrint(int a, int b, String file, TextAttributes color){
        int x =a;
        int y =b;
        for (int i = 0; i < file.length(); i++) {
            if(file.charAt(i)=='n') {
                y++;
                x=a;
            }
            else{
                cn.getTextWindow().setCursorPosition(x, y);
                if(file.charAt(i)!=' ')
                    cn.getTextWindow().output(file.charAt(i), color);
                else
                    cn.getTextWindow().output(file.charAt(i));
            }
            x++;
        }

    }
    public static void howToPlay(){
        JOptionPane.showMessageDialog(null,"HOW TO PLAY: \n1) Cursor Keys: To move human player number \n2) Q:Transfer one item from right backpack to left backpack\n3) W:Transfer one item from left backpack to right backpack\n4) P: Pauses the game\n5) H:Displays the help screen  \nBONUSES:\n(^): Increases players value +1\n(+): Increases your hearts +1\n($): Increase score +10 points\n(- ): Decreases the speed of moving numbers for 10 seconds\n BONUSES ARE CREATED EVERY 10 SECONDS AND DELETED FROM THE SCREEN IN 5 SECONDS, HURRY TO CAPTURE!\n" +
                "\n                                                                    <<<<<< WISH YOU GOOD GAMES! >>>>>>","Welcome to Number Maze Game!",3);
    }
}