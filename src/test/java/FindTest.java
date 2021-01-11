import utils.AStar;
import utils.CommentUtils;
import constant.Constants;
import pojo.MapInfo;
import pojo.Node;
import pojo.locationInfo;

import java.util.List;

/**
 * 通过A*算法规划到达最近豆子的路线
 */
public class FindTest {
    public static void main(String[] args) {
        // 地图
        int[] array = {
                -2, -2, -5, -5, -5, -2, -5, -5, -5, -2, -2, -2, -5, -5, -5, -2, -5, -5, -5, -2, -2,
                -5, -2, -2, -2, -2, -2, -2, -2, -2, -2, -2, -2, -2, -2, -2, -2, -2, -2, -2, -2, -5,
                -5, -2, -2, -2, -5, -2, -2, -2, -5, -2, -2, -2, -5, -2, -2, -2, -5, -2, -2, -2, -5,
                -5, -2, -2, -2, -5, -2, 0, -5, -2, -2, -2, -2, -2, -5, 0, -2, -5, -2, -2, -2, -5,
                -5, -2, -2, -2, -5, -2, -2, -2, -5, -2, -2, -2, -5, -2, -2, -2, -5, -2, -2, -2, -5,
                -5, -2, -2, -2, -2, -2, -2, -2, -2, -2, -2, -2, -2, -2, -2, -2, -2, -2, -2, -2, -5,
                -5, -2, -5, -5, -5, -5, -5, -5, -5, -2, -2, -2, -5, -5, -5, -5, -5, -5, -5, -2, -5,
                -2, -2, -2, -2, -2, -2, -2, -2, -2, -2, -2, -2, -2, -2, -2, -2, -4, -2, -2, -2, -2,
                -2, -2, -2, -2, -2, -2, -2, -2, -4, -2, -2, -2, -2, -2, -2, -2, -2, -2, -2, -2, -2,
                -2, -5, -5, -5, -5, -5, -5, -2, -2, -2, -2, -2, -2, -2, -5, -5, -5, -5, -5, -5, -2,
                -2, -2, -2, -2, -2, -2, -2, -2, -2, -2, -2, -2, -2, -2, -2, -2, -2, -2, -2, -2, -2,
                -2, -5, -5, -5, -5, -5, -5, -2, -2, -2, -2, -2, -2, -2, -5, -5, -5, -5, -5, -5, -2,
                -2, -2, -2, -2, -2, -2, -2, -2, -2, -2, -2, -2, -2, -2, -2, -2, -2, -2, -2, -2, -2,
                -2, -2, -2, -4, -2, -2, -2, -2, -2, -2, -2, -2, -2, -2, -2, -2, -2, -2, -2, -2, -2,
                -5, -2, -5, -5, -5, -5, -5, -5, -5, -2, -2, -2, -5, -5, -5, -5, -5, -5, -5, -2, -5,
                -5, -2, -2, -2, -2, -2, -2, -2, -2, -2, -2, -2, -2, -2, -2, -2, -2, -4, -2, -2, -5,
                -5, -2, -2, -2, -5, -2, -2, -2, -5, -2, -2, -2, -5, -2, -2, -2, -5, -2, -2, -2, -5,
                -5, -2, -2, -2, -5, -2, 0, -5, -2, -2, -2, -2, -2, -5, 0, -2, -5, -2, -2, -2, -5,
                -5, -2, -2, -2, -5, -2, -2, -2, -5, -2, -2, -2, -5, -2, -2, -2, -5, -2, -2, -2, -5,
                -5, -2, -2, -2, -2, -2, -2, -2, -2, -2, -2, -2, -2, -2, -2, -2, -2, -2, -2, -2, -5,
                -2, -2, -5, -5, -5, -2, -5, -5, -5, -2, -2, -2, -5, -5, -5, -2, -5, -5, -5, -2, -2
        };
        long time1 = System.currentTimeMillis();
        int[][] maps = CommentUtils.switchArray(array, 21, 21);
        System.out.println("-----------------------------地图-----------------------------");
        printMap(maps);
        // 我的位置
        locationInfo myLocationInfo = new locationInfo(6, 17);
        // 找到所有的小豆子的位置
        List<locationInfo> SmallPacList = CommentUtils.FindPac(Constants.SMALLPAC, maps);
        System.out.println("小豆子的位置：" + SmallPacList.toString());
        // 找到距离自己最近的豆子
        locationInfo targetLocationInfo = CommentUtils.FindNearestPac(myLocationInfo, SmallPacList);
        System.out.println("最近的豆子的位置：" + targetLocationInfo);
        // 规划路线
        MapInfo info = new MapInfo(maps, maps[0].length, maps.length, new Node(myLocationInfo), new Node(targetLocationInfo));
        List<Integer> list = new AStar().start(info);
        System.out.println("移动路线：" + list);
        System.out.println("吃到豆子需要的回合数：" + list.size());
        System.out.println("-----------------------------路线图-----------------------------");
        printMap(maps);
        long time2 = System.currentTimeMillis();
        System.out.println("-----------------------------耗时:" + (time2 - time1) + "ms-----------------------------");
    }

    /**
     * 打印地图
     */
    public static void printMap(int[][] maps) {
        for (int i = 0; i < maps.length; i++) {
            for (int j = 0; j < maps[i].length; j++) {
                System.out.print(maps[i][j] + " ");
            }
            System.out.println();
        }
    }
}
