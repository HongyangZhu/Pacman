package pojo;

/**
 * ClassName: LocationInfo
 * 按照二维数组的特点，坐标原点在左上角，所以y是高，x是宽，y向下递增，x向右递增，
 *
 * @author zhuhy
 * @Description: 位置信息
 */
public class LocationInfo {

    public int x;
    public int y;

    public LocationInfo(int x, int y) {
        this.x = x;
        this.y = y;
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
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (obj instanceof LocationInfo) {
            LocationInfo c = (LocationInfo) obj;
            return x == c.x && y == c.y;
        }
        return false;
    }

    @Override
    public String toString() {
        return "locationInfo{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }
}
