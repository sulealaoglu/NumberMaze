import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Random;
import java.util.Scanner;

import enigma.core.Enigma;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import enigma.console.TextAttributes;

import javax.swing.*;

public class Main {
    static int countt = 0;
    static Music msc = new Music();
    static String path; //music path
    static Player player = new Player(15, 5);
    static Stack leftBag = new Stack(8), rightBag = new Stack(8);
    static public KeyListener klis;
    static int bonusTime = 0, counter = 0, sec = 0, min = 0, timer_ret_value = 0, keypr, rkey, board_i = 0, board_j = 0, score = 0, time_pass = 0, tempTime = 0, yellow_current_i = -1, yellow_previous_j = -1, tempBonusTime = 0;
    static boolean first_bonus = true, bonusPrint = false, flag = true, right_check = false, warning_on_screen = false, appropriate = true;
    static Color crl = new Color(144, 107, 160), clr = new Color(176, 196, 222), clr2 = new Color(73, 162, 206), colour = new Color(255, 60, 0, 255), music1 = new Color(255, 140, 0);
    static TextAttributes attr = null, colourr = new TextAttributes(colour), attr2 = new TextAttributes(Color.black, clr2), attr3 = new TextAttributes(Color.black, Color.white), reset = new TextAttributes(Color.white), warning = new TextAttributes(Color.red), musicc = new TextAttributes(music1), bonus = new TextAttributes(Color.black, new Color(0xCECEEE));
    static enigma.console.Console cn;
    static Object[][] board = new Object[23][55];
    static boolean[][] mazeData = new boolean[23][55];
    static CircularQueue inputList = new CircularQueue(26);
    static int difficulty = 25;
    static int hearts = 3;

    public static void main(String[] args) throws IOException, InterruptedException {
        cn = Enigma.getConsole("NumberMaze", 150, 35);
        createBoard(true);
        Menu menu = new Menu();
        Menu.howToPlay();
        StaticScreen();
        game();
    }

    public static void createBoard(boolean flag) throws FileNotFoundException {
        File f = new File("maze.txt");
        Scanner sc = new Scanner(f, "UTF-8");
        int row = 0;
        while (sc.hasNextLine()) {
            String line = sc.nextLine();
            for (int i = 0; i < 55; i++) {
                board[row][i] = line.charAt(i);
            }
            row++;
        }
        sc.close();
        if (flag)
            board[15][5] = player;
    }

    public static void printBoard() {
        int y = 5;
        for (int i = 0; i < board.length; i++) {
            int x = 10;
            for (int j = 0; j < board[i].length; j++) {
                String str = String.valueOf(board[i][j]);
                cn.getTextWindow().setCursorPosition(x, y);
                if (board[i][j].equals('#')) {
                    cn.getTextWindow().output(str, attr);
                    cn.getTextWindow().setCursorPosition(x + 1, y);

                    cn.getTextWindow().output(str, attr);
                } else if (board[i][j] instanceof Number) {
                    Number number = (Number) board[i][j];
                    str = String.valueOf(number.getValue());
                    char color = number.getColor();
                    TextAttributes attr_number;
                    switch (color) {
                        case 'g': {
                            Color green = new Color(13, 187, 62);
                            attr_number = new TextAttributes(Color.black, green);
                            break;
                        }
                        case 'y': {
                            Color yellow = new Color(255, 241, 61);
                            attr_number = new TextAttributes(Color.black, yellow);
                            break;
                        }
                        case 'r': {
                            Color red = new Color(245, 52, 78);
                            attr_number = new TextAttributes(Color.black, red);
                            break;
                        }
                        default:
                            attr_number = reset;
                    }
                    cn.getTextWindow().output(str, attr_number);
                    cn.getTextWindow().setCursorPosition(x + 1, y);
                    cn.getTextWindow().output(' ', reset);
                } else if (board[i][j] instanceof Player) {
                    cn.getTextWindow().output(String.valueOf(player.getValue()), attr2);
                    cn.getTextWindow().setCursorPosition(x + 1, y);
                } else if (board[i][j] instanceof Bonus) {
                    str = ((Bonus) board[i][j]).getSign();
                    cn.getTextWindow().output(str, bonusColor());
                    cn.getTextWindow().setCursorPosition(x + 1, y);
                    cn.getTextWindow().output(' ', reset);
                } else {
                    cn.getTextWindow().output(str, reset);
                    cn.getTextWindow().setCursorPosition(x + 1, y);
                    cn.getTextWindow().output(' ', reset);
                }
                x += 2;
            }
            y++;
        }
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                if (board[i][j] instanceof Number && ((Number) board[i][j]).getColor() == 'r') {
                    Number temp = (Number) board[i][j];
                    printPath(temp);
                }
            }

        }
        cn.getTextWindow().setCursorPosition(player.getCoordinate_j() * 2 + 10, player.getCoordinate_i() + 5);
    }

    public static TextAttributes bonusColor() {
        Color clrp = new Color(RandomInRange(0, 256), RandomInRange(0, 256), RandomInRange(0, 256));
        TextAttributes attrp = new TextAttributes(Color.black, clrp);
        return attrp;
    }

    public static void printPath(Number number) {
        Coordinate c;
        if (number.getFinalPath() != null && !number.getFinalPath().isEmpty()) {
            Stack temp = createCopyStack(number.getFinalPath());
            while (temp.size() != 1) {
                c = (Coordinate) temp.pop();
                if (!(board[c.getI()][c.getJ()] instanceof Number || board[c.getI()][c.getJ()] instanceof Player)) {
                    cn.getTextWindow().setCursorPosition((c.getJ() * 2) + 10, c.getI() + 5);
                    cn.getTextWindow().output(".", number.getPathColor());
                }
            }
        }
    }

    public static Stack createCopyStack(Stack path) { // create copy stack to avoid losing main stack

        Stack tempStack = new Stack(path.size());
        Stack tempStack2 = new Stack(path.size()); // for reverse
        while (!path.isEmpty()) {
            tempStack.push(path.pop());
        }
        while (!tempStack.isEmpty()) { // reverse
            path.push(tempStack.peek());
            tempStack2.push(tempStack.pop());
        }
        return tempStack2;
    }

    public static void NumberGenerator() {
        while (inputList.Size() != 25) {
            Number number = new Number();
            number.setPathColor(pathColor());
            inputList.Enqueue(number);
        }
    }

    public static void printQueue(CircularQueue queue) {
        cn.getTextWindow().setCursorPosition(125, 7);
        for (int i = 0; i < queue.Size(); i++) {
            Number temp = (Number) queue.Peek();
            if (i < 10) {
                System.out.print(temp.getValue() + " ");
            }
            queue.Enqueue(queue.Dequeue());
        }
    }

    static int RandomCoordinateGenerator_i() {
        Random rnd = new Random();
        int index = rnd.nextInt(23);
        return index;
    }

    static int RandomCoordinateGenerator_j() {
        Random rnd = new Random();
        int index = rnd.nextInt(55);
        return index;
    }

    static int Timer() throws InterruptedException {
        Thread.sleep(20);
        timer_ret_value++;
        counter++;
        if (counter == 50) {
            sec += 1;
            counter = 0;
            if (sec == 60) {
                min++;
                sec = 0;
            }
        }
        cn.getTextWindow().setCursorPosition(125, 27);
        cn.setTextAttributes(attr2);
        if (min != 0)
            System.out.println("Time :" + min + " min " + sec + " sec");
        else
            System.out.println("Time :" + sec);
        cn.setTextAttributes(reset);
        return timer_ret_value;
    }

    public static void StaticScreen() {   // ekran bilgileri, paketler, score vs
        cn.getTextWindow().setCursorPosition(132, 5);
        cn.getTextWindow().output("Input", attr2);
        cn.getTextWindow().setCursorPosition(125, 6);
        cn.getTextWindow().output("<<<<<<<<<<<<<<<<<<<");
        cn.getTextWindow().setCursorPosition(125, 8);
        cn.getTextWindow().output("<<<<<<<<<<<<<<<<<<<");
        cn.getTextWindow().setCursorPosition(129, 12);
        cn.getTextWindow().output("Backpacks");

        for (int i = 0; i < 8; i++) {
            int x = 0;
            for (int j = 0; j < 4; j++) {
                cn.getTextWindow().setCursorPosition(125 + x, 14 + i);
                cn.getTextWindow().output("|");
                if (j % 2 == 0) {
                    x += 6;
                } else {
                    x += 4;
                }
            }
        }
        int x = 0;

        for (int i = 0; i < 2; i++) {
            cn.getTextWindow().setCursorPosition(125 + x, 22);
            cn.getTextWindow().output("+-----+");
            x += 10;
        }
        NumberGenerator();
        for (int i = 0; i < 25; i++) {
            Number temp = (Number) inputList.Dequeue();
            RandomPlacer(temp);
        }
        cn.getTextWindow().setCursorPosition(126, 23);
        cn.getTextWindow().output("Left");
        cn.getTextWindow().setCursorPosition(136, 23);
        cn.getTextWindow().output("Right");
        cn.getTextWindow().setCursorPosition(128, 24);
        cn.getTextWindow().output("Q");
        cn.getTextWindow().setCursorPosition(138, 24);
        cn.getTextWindow().output("W");
        cn.getTextWindow().setCursorPosition(125, 26);
        TextAttributes atttr = new TextAttributes(Color.black, clr);
        cn.getTextWindow().output("Score: ", atttr);

    }

    public static void game() throws InterruptedException {
        try {
            int sec = 10;
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
            // ----------------------------------------------------
            int px = 20, py = 20;
            boolean first_step = true;
            boolean game = true;
            int counter = 0;
            Coordinate bonus = null;
            while (true) {
                appropriate = true;
                NumberGenerator();
                if (time_pass % 500 == 0 && time_pass != 0) {
                    bonus = bonusGenerator();
                    bonusTime = time_pass;
                }
                if (time_pass - bonusTime == 250 && bonus != null) {
                    bonusTime = 0;
                    board[bonus.getI()][bonus.getJ()] = ' ';
                    printBoard();
                    bonus = null;
                }
                if (tempTime > 0)
                    turnsIntoTwo();

                printBoard();
                point();
                printHearts();
                if (!game) {
                    cn.getTextWindow().setCursorPosition(52, 3);
                    msc.stop();
                    msc.playMusic("game_over.wav", false);
                    String str = "G  A  M  E    O   V   E   R";
                    for (int i = 0; i < str.length(); i++) {
                        cn.getTextWindow().output(str.charAt(i), attr2);
                        Thread.sleep(50);
                    }
                    break;
                }
                if (first_step) {
                    printQueue(inputList);
                    PathFinding();
                    first_step = false;
                }
                if (keypr == 1) { // if keyboard button pressed
                    if (player.getValue() > 1) {

                        if (rkey == KeyEvent.VK_LEFT && px > 12 && !board[board_i][board_j - 1].equals('#')) {
                            board[board_i][board_j] = ' ';
                            px -= 2;
                            counter++;
                        } else if (rkey == KeyEvent.VK_RIGHT && px < 117 && !board[board_i][board_j + 1].equals('#')) {
                            board[board_i][board_j] = ' ';
                            px += 2;
                            counter++;
                        } else if (rkey == KeyEvent.VK_UP && py > 6 && !board[board_i - 1][board_j].equals('#')) {
                            board[board_i][board_j] = ' ';
                            py -= 1;
                            counter++;

                        } else if (rkey == KeyEvent.VK_DOWN && py < 26 && !board[board_i + 1][board_j].equals('#')) {
                            board[board_i][board_j] = ' ';
                            py += 1;
                            counter++;
                        } else if (rkey == KeyEvent.VK_H)
                            Menu.howToPlay();
                        else if (rkey == KeyEvent.VK_P)
                            JOptionPane.showMessageDialog(null, "Paused...\nPress OK or Close Button to Keep Playing", "Paused...", 1);
                    }


                    cn.getTextWindow().setCursorPosition(player.getCoordinate_j() * 2 + 10, player.getCoordinate_i() + 5);
                    printBoard();
                    cn.getTextWindow().setCursorPosition(player.getCoordinate_j() * 2 + 10, player.getCoordinate_i() + 5);
                    if (rkey == KeyEvent.VK_Q) {
                        if (!rightBag.isEmpty() && !leftBag.isFull()) {
                            leftBag.push(rightBag.pop());
                        }
                    } else if (rkey == KeyEvent.VK_W) {
                        if (rightBag.size() == 8) {
                            cn.getTextWindow().setCursorPosition(10, 3);
                            cn.getTextWindow().output("Right Backpack is full!", warning);
                            warning_on_screen = true;
                        }
                        if (!leftBag.isEmpty() && !rightBag.isFull()) {
                            rightBag.push(leftBag.pop());
                        }

                    }
                    if (!rightBag.isFull()) {
                        ClearCurrentLine(10, 3, 56);
                    }
                    keypr = 0; // last action
                }

                time_pass = Timer();

                if ((time_pass % (difficulty * 10) == 0)) {
                    Number temp = (Number) inputList.Dequeue();
                    RandomPlacer(temp);
                    if (temp.getColor() == 'r') {
                        UpdateData();
                        temp.PathFinding(player);
                        printPath(temp);
                    }
                    printQueue(inputList);
                    PathFinding();
                }
                if (time_pass - tempBonusTime == 500 && time_pass != 500)//10 sn bonus süresi bitiyor
                {
                    difficulty -= 10;
                    tempBonusTime = 0;
                    bonusPrint = false;
                    cn.getTextWindow().setCursorPosition(50, 30);
                    cn.getTextWindow().output("                                                         ");
                }

                board_j = (px - 10) / 2;
                board_i = py - 5;
                player.setCoordinate_i(board_i);
                player.setCoordinate_j(board_j);
                game = check(board_i, board_j);
                //capture durumu varsa
                if (game) {
                    board[board_i][board_j] = player;
                }
                //addPath();
                if (counter == 1) {
                    PathFinding();
                    counter = 0;
                }
                if (time_pass % difficulty * 10 == 0 || time_pass == 1) {//sadece hareketli numaralar için yarım saniyede bir
                    RandomMove();
                    redMove();

                }

                if (bonusPrint) {
                    if (first_bonus) {
                        cn.getTextWindow().setCursorPosition(50, 30);
                        cn.getTextWindow().output("Slower Bonus is Actived --> Remaining Time: " + 10);
                        first_bonus = false;
                    } else if (time_pass % 50 == 0) {
                        ClearCurrentLine(95, 30, 1);
                        cn.getTextWindow().setCursorPosition(50, 30);
                        cn.getTextWindow().output("Slower Bonus is Actived --> Remaining Time: " + sec);
                        sec--;
                        countt++;
                    }

                } else {
                    ClearCurrentLine(50, 30, 50);
                    sec = 9;
                }
            }
        } catch (Exception e) {
            System.out.println();
        }

    }

    public static void redMove() {
        int red_current_i = -1, red_previous_j = -1;
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                if (board[i][j] instanceof Number && ((Number) board[i][j]).getColor() == 'r' && !(i == red_current_i && j == red_previous_j)) {
                    Number number = (Number) board[i][j];
                    Stack stack = number.getFinalPath();
                    if (!stack.isEmpty()) {
                        Coordinate next = (Coordinate) stack.peek();
                        while (board[next.getI()][next.getJ()] instanceof Number || board[next.getI()][next.getJ()] instanceof Bonus) {
                            number.PathFinding(player);
                            stack = number.getFinalPath();
                            if (!stack.isEmpty()) {
                                next = (Coordinate) stack.peek();
                            } else
                                break;
                        }
                    }
                    if (!stack.isEmpty()) {
                        Coordinate location = (Coordinate) stack.pop();
                        board[location.getI()][location.getJ()] = number;
                        board[number.getCoordinate_i()][number.getCoordinate_j()] = ' ';
                        if (location.getI() == number.getCoordinate_i() + 1 && location.getJ() == number.getCoordinate_j()) {
                            red_current_i = i + 1;
                            red_previous_j = j;
                        } else if (location.getJ() == number.getCoordinate_j() + 1 && location.getI() == number.getCoordinate_i()) {
                            j++;
                        }
                        number.setCoordinate_i(location.getI());
                        number.setCoordinate_j(location.getJ());
                    }

                }
            }
        }
        UpdateData();
    }

    public static void turnsIntoTwo() {
        if (time_pass - tempTime == 200 && tempTime > 0) {
            player.setValue(2);
        }
    }

    public static Object Find(Stack stack, int index) {
        Stack temp = new Stack(stack.size());
        Object object = null;
        while (!stack.isEmpty()) {
            if (stack.size() - 1 == index) {
                object = stack.peek();
                break;
            }
            temp.push(stack.pop());
        }
        while (!temp.isEmpty()) {
            stack.push(temp.pop());
        }
        return object;
    }

    public static void Delete(Stack stack, int index) {
        Stack temp = new Stack(stack.size());
        while (!stack.isEmpty()) {
            if (stack.size() - 1 != index) {
                temp.push(stack.pop());
            } else {
                stack.pop();
            }
        }
        while (!temp.isEmpty()) {
            stack.push(temp.pop());
        }
    }

    public static void RandomPlacer(Number number) {
        int random_i = RandomCoordinateGenerator_i();
        int random_j = RandomCoordinateGenerator_j();
        Object element = board[random_i][random_j];
        while ((element.equals('#') || element instanceof Number || element instanceof Player)) {
            random_i = RandomCoordinateGenerator_i();
            random_j = RandomCoordinateGenerator_j();
            element = board[random_i][random_j];

        }
        number.setCoordinate_i(random_i);
        number.setCoordinate_j(random_j);
        board[random_i][random_j] = number;
    }

    public static Coordinate bonusGenerator() {
        Coordinate bonusC;
        Bonus bonus = new Bonus();
        int random_i = RandomCoordinateGenerator_i();
        int random_j = RandomCoordinateGenerator_j();
        Object element = board[random_i][random_j];
        while ((element.equals('#') || element instanceof Number || element instanceof Player)) {
            random_i = RandomCoordinateGenerator_i();
            random_j = RandomCoordinateGenerator_j();
            element = board[random_i][random_j];
        }
        board[random_i][random_j] = bonus;
        bonusC = new Coordinate(random_i, random_j);
        return bonusC;
    }

    public static void RandomMove() throws InterruptedException {
        yellow_previous_j = -1;
        yellow_current_i = -1;
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                Random rnd = new Random();
                if (board[i][j] instanceof Number && ((Number) board[i][j]).getColor() == 'y' && !(i == yellow_current_i && j == yellow_previous_j)) {
                    Number number = (Number) board[i][j];
                    DirectionCheck(i, j, number);
                    while (number.isContact_with_bariier()) {
                        //sıkışma kontrolü
                        if ((board[i - 1][j].equals('#') || board[i - 1][j] instanceof Number || board[i - 1][j] instanceof Bonus || RedControl(i - 1, j)) &&
                                (board[i + 1][j].equals('#') || board[i + 1][j] instanceof Number || board[i + 1][j] instanceof Bonus || RedControl(i + 1, j)) &&
                                (board[i][j + 1].equals('#') || board[i][j + 1] instanceof Number || board[i][j + 1] instanceof Bonus || RedControl(i, j + 1)) &&
                                (board[i][j - 1].equals('#') || board[i][j - 1] instanceof Number || board[i][j - 1] instanceof Bonus) || RedControl(i, j - 1)) {
                            break;
                        }
                        number.setDirection(rnd.nextInt(4));
                        DirectionCheck(i, j, number);

                    }
                    if (right_check) {
                        j++;
                    }
                }
            }
        }
        UpdateData();
    }

    public static boolean RedControl(int y, int x) {
        boolean flag = false;
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                if (board[i][j] instanceof Number && ((Number) board[i][j]).getColor() == 'r') {
                    Stack stack = ((Number) board[i][j]).getFinalPath();
                    if (!stack.isEmpty()) {
                        Coordinate next = (Coordinate) stack.peek();
                        if (next.getI() == y && next.getJ() == x) {
                            flag = true;
                            break;
                        }
                    }
                }
            }
            if (flag) {
                break;
            }
        }

        return flag;
    }

    public static void DirectionCheck(int i, int j, Number number) {
        right_check = false;
        switch (number.getDirection()) {
            //up
            case 0:
                if (!(board[i - 1][j].equals('#') || board[i - 1][j] instanceof Number || board[i - 1][j] instanceof Bonus || RedControl(i - 1, j))) {
                    number.setCoordinate_i(number.getCoordinate_i() - 1);
                    board[number.getCoordinate_i()][number.getCoordinate_j()] = number;
                    board[i][j] = ' ';
                    number.setContact_with_bariier(false);
                } else {
                    number.setContact_with_bariier(true);

                }
                break;
            //down
            case 1:
                if (!(board[i + 1][j].equals('#') || board[i + 1][j] instanceof Number || board[i + 1][j] instanceof Bonus || RedControl(i + 1, j))) {

                    number.setCoordinate_i(number.getCoordinate_i() + 1);
                    board[number.getCoordinate_i()][number.getCoordinate_j()] = number;
                    board[i][j] = ' ';
                    number.setContact_with_bariier(false);
                    yellow_current_i = i + 1;
                    yellow_previous_j = j;

                } else {
                    number.setContact_with_bariier(true);
                }
                break;
            //right
            case 2:
                if (!(board[i][j + 1].equals('#') || board[i][j + 1] instanceof Number || board[i][j + 1] instanceof Bonus || RedControl(i, j + 1))) {
                    number.setCoordinate_j(number.getCoordinate_j() + 1);
                    board[number.getCoordinate_i()][number.getCoordinate_j()] = number;
                    board[i][j] = ' ';
                    number.setContact_with_bariier(false);
                    right_check = true;
                } else {
                    number.setContact_with_bariier(true);
                }
                break;
            //left
            case 3:
                if (!(board[i][j - 1].equals('#') || board[i][j - 1] instanceof Number || board[i][j - 1] instanceof Bonus || RedControl(i, j - 1))) {
                    number.setCoordinate_j(number.getCoordinate_j() - 1);
                    board[number.getCoordinate_i()][number.getCoordinate_j()] = number;
                    board[i][j] = ' ';
                    number.setContact_with_bariier(false);
                } else {
                    number.setContact_with_bariier(true);
                }
                break;
        }

    }

    public static boolean check(int i, int j) //capture
    {
        //capture ---> true
        if (board[i][j] instanceof Number) {
            Number number = (Number) board[i][j];
            if (number.getValue() <= player.getValue()) {
                if (leftBag.isFull()) {
                    leftBag.pop();
                }
                leftBag.push(number);
                msc.playMusic("bip.wav", false);
            } else {
                hearts--;
                if (hearts > 0) {
                    JOptionPane.showMessageDialog(null, "You still have extra lifes, Press OK for Restart", "Restart", JOptionPane.INFORMATION_MESSAGE);
                    board[board_i][board_j] = player;
                }
                if (hearts == 0)
                    flag = false;
            }
        }
        if (board[i][j] instanceof Bonus) {
            Bonus bonus = (Bonus) board[i][j];
            switch (bonus.getType()) {
                case 0:
                    bonus.increaseHearts();
                    break;
                case 1:
                    bonus.increaseValue();
                    if (player.getValue() == 1)
                        tempTime = time_pass;
                    break;
                case 2:
                    bonus.slowerEnemy();
                    tempBonusTime = time_pass;
                    bonusPrint = true;
                    break;
                case 3:
                    bonus.increaseScore();
                    break;
            }
        }
        printStack(leftBag, 128);
        printStack(rightBag, 138);
        return flag;
    }

    public static void printHearts() {
        TextAttributes a = new TextAttributes(Color.white, Color.red.darker());
        cn.getTextWindow().setCursorPosition(128, 10);
        cn.getTextWindow().output("HP:");
        cn.getTextWindow().output("                  ");
        cn.getTextWindow().setCursorPosition(133, 10);
        for (int i = 0; i < hearts; i++) {
            cn.getTextWindow().output("<3", a);
            cn.getTextWindow().output(" ");

        }

    }

    public static void printStack(Stack stack1, int x) {

        int y = 21;
        Stack tempstack = new Stack(8);
        while (!stack1.isEmpty()) {
            if (stack1.peek() != null)
                tempstack.push(stack1.pop());
        }

        for (int i = 0; i < 8; i++) {
            if (!tempstack.isEmpty()) {
                stack1.push(tempstack.pop());
                Number temp = (Number) stack1.peek();
                cn.getTextWindow().setCursorPosition(x, y);
                cn.getTextWindow().output(String.valueOf(temp.getValue()), colourr);

            } else {
                cn.getTextWindow().setCursorPosition(x, y);
                cn.getTextWindow().output(" ");
            }
            y--;
        }
    }

    public static void point() throws InterruptedException {
        int min = Math.min(leftBag.size(), rightBag.size());
        for (int i = 0; i < min; i++) {
            Number number1 = (Number) Find(leftBag, i);
            Number number2 = (Number) Find(rightBag, i);
            if (number1 != null && number2 != null && number1.getValue() == number2.getValue()) {
                score += number1.getValue() * number1.getScoreFactor();
                if (player.increaseValue())
                    tempTime = time_pass;
                Delete(leftBag, i);
                Delete(rightBag, i);
                msc.playMusic("pop.wav", false);
            }

        }
        cn.getTextWindow().setCursorPosition(131, 26);
        TextAttributes atttr = new TextAttributes(Color.black, clr);
        cn.getTextWindow().output(String.valueOf(score), atttr);

    }

    public static void ClearCurrentLine(int i, int j, int length) {
        cn.getTextWindow().setCursorPosition(i, j);
        for (int k = 0; k < length; k++) {
            cn.getTextWindow().output(" ");
        }
    }

    public static void UpdateData() {
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                if (board[i][j].equals(' ')) {
                    mazeData[i][j] = true;
                } else
                    mazeData[i][j] = false;
            }
        }
    }

    public static void PathFinding() {
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                if (board[i][j] instanceof Number && ((Number) board[i][j]).getColor() == 'r') {
                    ((Number) board[i][j]).PathFinding(player);
                }
            }
        }
    }

    static int RandomInRange(int min, int max) {
        Random rnd = new Random();
        return rnd.nextInt((max - min)) + min;
    }

    public static TextAttributes pathColor() {
        Color clrp = new Color(RandomInRange(0, 256), RandomInRange(0, 256), RandomInRange(0, 256));
        TextAttributes attrp = new TextAttributes(clrp);
        return attrp;
    }
}