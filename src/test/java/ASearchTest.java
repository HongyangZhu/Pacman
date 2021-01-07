import Utils.ASearch;
import pojo.Node;

public class ASearchTest {
    public static void main(String[] args) {
        Node start = new Node(2, 1, 40, 0, null);
        Node end = new Node(2, 5, 0, 0, null);
        // 绘制地图
        int[][] map = new int[9][9];
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[0].length; j++) {
                if (i == 2 && j == 1) {
                    map[i][j] = 1;
                    continue;
                }

                if (i == 2 && j == 5) {
                    map[i][j] = 2;
                    continue;
                }
                map[i][j] = 0;
            }
        }
        // 设置障碍物
        map[1][3] = 6;
        map[2][3] = 6;
        map[3][3] = 6;
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[0].length; j++) {
                System.out.print(map[i][j] + " ");
            }
            System.out.println();
        }
        System.out.println("-----------------开始寻路-----------------");
        long time1 = System.currentTimeMillis();
        ASearch aSearch = new ASearch(start, end);
        Node node = aSearch.findPath(map);
        while (node.parent != null) {
            System.out.println(node);

            node = node.parent;
        }
        long time2 = System.currentTimeMillis();
        System.out.println("-----------------寻路结束-----------------");
        System.out.println("-----------------寻路耗时：" + (time2 - time1) + "ms-----------------");
    }
}
