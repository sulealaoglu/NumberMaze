import enigma.console.TextAttributes;
import java.util.Random;

public class Number {
    private int scoreFactor;
    private char color;
    private int value;
    private int coordinate_i;
    private int coordinate_j;
    private int direction;
    private TextAttributes pathColor;
    private Stack finalPath;
    private int[] numbers = {1, 2, 3, 4, 5, 6, 7, 8, 9};
    private boolean contact_with_bariier;
    boolean flag;
    Coordinate current = new Coordinate(coordinate_i, coordinate_j);
    private Stack path;
    private int[][] mark;

    public Number() {
        int number = randomNumberGenerator();
        if (number < 4) {
            scoreFactor = 1;
            color = 'g';
            value = number;
        } else if (number < 7) {
            scoreFactor = 5;
            color = 'y';
            value = number;
            contact_with_bariier = true;
        } else {
            scoreFactor = 25;
            color = 'r';
            value = number;
        }
        direction = -1;
    }

    public int randomNumberGenerator() {
        Random rnd = new Random();
        int index = 0;
        int a = rnd.nextInt(125-Main.difficulty);
        if (a < 75)
            index = rnd.nextInt(3);
        else if (a < 95)
            index = rnd.nextInt(3) + 3;
        else
            index = rnd.nextInt(3) + 6;
        return numbers[index];
    }

    public boolean insideOfConditions(int choice, Coordinate current) {
        boolean check = true;
        switch (choice) {
            case 1: //right
                check = Main.mazeData[current.getI()][current.getJ() + 1] && mark[current.getI()][current.getJ() + 1] == 0;
                break;
            case 2: //left
                check = Main.mazeData[current.getI()][current.getJ() - 1] && mark[current.getI()][current.getJ() - 1] == 0;
                break;
            case 3: //up
                check = Main.mazeData[current.getI() - 1][current.getJ()] && mark[current.getI() - 1][current.getJ()] == 0;
                break;
            case 4://down
                check = Main.mazeData[current.getI() + 1][current.getJ()] && mark[current.getI() + 1][current.getJ()] == 0;
                break;
        }
        return check;
    }

    public void commands(int choice, Coordinate current) {
        switch (choice) { //command
            case 1: //right
                current.setJ(current.getJ() + 1);
                flag = true;
                break;
            case 2: //left
                current.setJ(current.getJ() - 1);
                flag = true;
                break;
            case 3://up
                current.setI(current.getI() - 1);
                flag = true;
                break;
            case 4://down
                current.setI(current.getI() + 1);
                flag = true;
                break;
            default:
                break;
        }

    }

    public boolean priority(int a, int b, int c, int d, Coordinate current) {
        boolean check = true;//for else
        switch (a) { //first command
            case 1: //right
                if (insideOfConditions(a, current)) { //right
                    current.setJ(current.getJ() + 1);
                    flag = true;
                } else if (insideOfConditions(b, current))
                    commands(b, current);
                else if (insideOfConditions(c, current))
                    commands(c, current);
                else if (insideOfConditions(d, current))
                    commands(d, current);
                else
                    check = false;
                break;
            case 2: //left
                if (insideOfConditions(a, current)) {//left
                    current.setJ(current.getJ() - 1);
                    flag = true;
                } else if (insideOfConditions(b, current))
                    commands(b, current);
                else if (insideOfConditions(c, current))
                    commands(c, current);
                else if (insideOfConditions(d, current))
                    commands(d, current);
                else
                    check = false;
                break;
            case 3: //up
                if (insideOfConditions(a, current)) { //up
                    current.setI(current.getI() - 1);
                    flag = true;
                } else if (insideOfConditions(b, current))
                    commands(b, current);
                else if (insideOfConditions(c, current))
                    commands(c, current);
                else if (insideOfConditions(d, current))
                    commands(d, current);
                else
                    check = false;
                break;
            case 4://down
                if (insideOfConditions(a, current)) { //down
                    current.setI(current.getI() + 1);
                    flag = true;
                } else if (insideOfConditions(b, current))
                    commands(b, current);
                else if (insideOfConditions(c, current))
                    commands(c, current);
                else if (insideOfConditions(d, current))
                    commands(d, current);
                else
                    check = false;
                break;
        }

        return check;

    }

    public boolean elseCondition(Stack path, Coordinate current,int [][] mark) {
        boolean flag = true;
        mark[current.getI()][current.getJ()] = -1;
        if (!path.isEmpty()) {
            path.pop();
            Coordinate temp = (Coordinate) path.peek();
            current.setI(temp.getI());
            current.setJ(temp.getJ());
            flag = false;
        }
        return flag;

    }

    public void PathFinding(Player player) {
        path = new Stack(10000);
        finalPath = new Stack(10000);
        mark = new int[23][55];
        Main.UpdateData();
        flag = false;
        Coordinate current = new Coordinate(coordinate_i, coordinate_j);
        Main.mazeData[current.getI()][current.getJ()] = true;
        int count=0;
        while (true) {
            //right-down-left-up (1,4,2,3)
            if (current.getJ() <= player.getCoordinate_j() && current.getI() <= player.getCoordinate_i()) {
                boolean check = priority(1, 4, 2, 3, current);
                if (!check) {
                    try {
                        if (elseCondition(path, current,mark)) {
                            break;
                        }
                    } catch (Exception e) {
                        break;
                    }
                    flag = false;
                }
            }
            //left-down-right-up (2,4,1,3)
            else if (current.getJ() >= player.getCoordinate_j() && current.getI() <= player.getCoordinate_i()) {
                boolean check = priority(2, 4, 1, 3, current);
                if (!check) {
                    try {
                        if (elseCondition(path, current,mark)) {
                            break;
                        }
                    } catch (Exception e) {
                        break;
                    }
                    flag = false;
                }

            }
            //left-up-right-down (2,3,1,4)
            else if (current.getJ() >= player.getCoordinate_j() && current.getI() >= player.getCoordinate_i()) {
                boolean check = priority(2, 3, 1, 4, current);
                if (!check) {
                    try {
                        if (elseCondition(path, current,mark)) {
                            break;
                        }
                    } catch (Exception e) {
                        break;
                    }
                    flag = false;
                }

            }
            //right-up-left-down (1,3,2,4)
            else if (current.getJ() <= player.getCoordinate_j() && current.getI() >= player.getCoordinate_i()) {
                boolean check = priority(1, 3, 2, 4, current);
                if (!check) {
                    try {
                        if (elseCondition(path, current,mark)) {
                            break;
                        }
                    } catch (Exception e) {
                        break;
                    }
                    flag = false;
                }
            }
            //down right left up---or //up right left down
            else if (current.getJ() == player.getCoordinate_j()) {
                if (current.getI() < player.getCoordinate_i()) { //down right left up (4,1,2,3)
                    boolean check = priority(4, 1, 2, 3, current);
                    if (!check) {
                        try {
                            if (elseCondition(path, current,mark)) {
                                break;
                            }
                        } catch (Exception e) {
                            break;
                        }
                        flag = false;
                    }
                } else if (current.getI() > player.getCoordinate_i()) { //up right left down (3,1,2,4)
                    boolean check = priority(3, 1, 2, 4, current);
                    if (!check) {
                        try {
                            if (elseCondition(path, current,mark)) {
                                break;
                            }
                        } catch (Exception e) {
                            break;
                        }
                        flag = false;
                    }
                }
            }
            //left up down right---or---right down up left
            else if (current.getI() == player.getCoordinate_i()) {
                if (current.getJ() > player.getCoordinate_i()) { //left up down right (2,3,4,1)
                    boolean check = priority(2, 3, 4, 1, current);
                    if (!check) {
                        try {
                            if (elseCondition(path, current,mark)) {
                                break;
                            }
                        } catch (Exception e) {
                            break;
                        }
                        flag = false;
                    }


                } else if (current.getI() > player.getCoordinate_i()) { //right down up left (1,4,3,2)
                    boolean check = priority(1, 4, 3, 2, current);
                    if (!check) {
                        try {
                            if (elseCondition(path, current,mark)) {
                                break;
                            }
                        } catch (Exception e) {

                            break;
                        }
                        flag = false;
                    }
                }
            }
            if (flag) {
                path.push(new Coordinate(current.getI(), current.getJ()));
                mark[current.getI()][current.getJ()] = -1;
            }
            if (((current.getI() == player.getCoordinate_i() + 1 || current.getI() == player.getCoordinate_i() - 1) && current.getJ() == player.getCoordinate_j()) ||
                    (current.getI() == player.getCoordinate_i()) && (current.getJ() == player.getCoordinate_j() + 1 || current.getJ() == player.getCoordinate_j() - 1)) {
                mark[player.getCoordinate_i()][player.getCoordinate_j()] = 1;
                Coordinate coordinate = new Coordinate(player.getCoordinate_i(), player.getCoordinate_j());
                path.push(coordinate);
                finalPath = reverseStack(createCopyStack());
                Coordinate location;
                do {
                    location = (Coordinate) finalPath.peek();
                    if ((location.getI() == coordinate_i && location.getJ() == coordinate_j))
                        finalPath.pop();
                } while ((location.getI() == coordinate_i && location.getJ() == coordinate_j));

                break;
            }



        }}


    public Stack createCopyStack() { // create copy stack to avoid losing main stack
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

    public Stack reverseStack(Stack s) { // for reverse stack
        Stack temp = new Stack(s.size());
        while (!s.isEmpty()) {
            temp.push(s.pop());
        }
        return temp;
    }

    public Stack getFinalPath() {
        return finalPath;
    }

    public int getScoreFactor() {
        return scoreFactor;
    }

    public char getColor() {
        return color;
    }

    public int getValue() {
        return value;
    }

    public int[] getNumbers() {
        return numbers;
    }

    public void setScoreFactor(int scoreFactor) {
        this.scoreFactor = scoreFactor;
    }

    public void setColor(char color) {
        this.color = color;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public void setNumbers(int[] numbers) {
        this.numbers = numbers;
    }

    public int getCoordinate_i() {
        return coordinate_i;
    }

    public void setCoordinate_i(int coordinate_i) {
        this.coordinate_i = coordinate_i;
    }

    public int getCoordinate_j() {
        return coordinate_j;
    }

    public void setCoordinate_j(int coordinate_j) {
        this.coordinate_j = coordinate_j;
    }

    public int getDirection() {
        return direction;
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }

    public boolean isContact_with_bariier() {
        return contact_with_bariier;
    }

    public void setContact_with_bariier(boolean contact_with_bariier) {
        this.contact_with_bariier = contact_with_bariier;
    }

    public TextAttributes getPathColor() {
        return pathColor;
    }

    public void setPathColor(TextAttributes pathColor) {
        this.pathColor = pathColor;
    }
}

