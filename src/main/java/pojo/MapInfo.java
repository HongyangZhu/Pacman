package pojo;

/**
 * ClassName: MapInfo
 *
 * @author zhuhy
 * @Description: 包含地图所需的所有输入数据
 */
public class MapInfo {
    public int[][] maps;
    public int width;
    public int hight;
    public Node start;
    public Node end;

    /**
     * @param maps  二维数组的地图
     * @param width 地图的宽
     * @param hight 地图的高
     * @param start 起始结点
     * @param end   最终结点
     */
    public MapInfo(int[][] maps, int width, int hight, Node start, Node end) {
        this.maps = maps;
        this.width = width;
        this.hight = hight;
        this.start = start;
        this.end = end;
    }
}
