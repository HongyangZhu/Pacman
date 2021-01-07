package pojo;

/**
 * 位置信息
 *
 * @author zhuhy
 */
public class Position {
    private int x;
    private int y;

    public Position(int i, int j) {
        this.x = i;
        this.y = j;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    @Override
    public String toString() {
        return "Position{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }
}
