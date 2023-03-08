
import java.util.Random;

public class Bonus {
    String sign;
    int type;
    public Bonus() {
        bonusGenerator();
    }

    public void bonusGenerator() {
        Random rnd = new Random();
        int a = rnd.nextInt(6);
        if (a == 0 && Main.hearts < 4)//+1 heart
        {
            sign = "+";
            type = 0;
        } else //+10 point
        {
            sign = "$";
            type = 3;
        }
        if (a == 1)//+1 value
        {
            sign = "^";
            type = 1;
        } else if (a < 4)//slower enemy
        {
            sign = "-";
            type = 2;
        } else //+10 point
        {
            sign = "$";
            type = 3;
        }

    }

    public void increaseHearts() {
        Main.hearts++;
    }

    public void slowerEnemy() {
        Main.difficulty += 10;
    }

    public void increaseValue() {
        Main.player.increaseValue();
    }

    public void increaseScore() {
        Main.score += 10;
    }

    public int getType() {
        return type;
    }

    public String getSign() {
        return sign;
    }
}
