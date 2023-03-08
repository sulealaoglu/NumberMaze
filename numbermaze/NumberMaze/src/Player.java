
public class Player {

    private char color;
    private int value;
    private int coordinate_i;
    private int coordinate_j;

    public Player(int coordinate_i, int coordinate_j) {
        color = 'b';
        value = 5;
        this.coordinate_i = coordinate_i;
        this.coordinate_j = coordinate_j;
    }

    public boolean increaseValue() {
        if(value != 9) {
            value++;
            return false;
        }
        else {
            value=1;
            return true;
        }
    }
    public char getColor() {
        return color;
    }
    public void setColor(char color) {
        this.color = color;
    }
    public int getValue() {
        return value;
    }
    public void setValue(int value) {
        this.value = value;
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
}
