
import java.util.*;

import static java.util.Map.Entry.comparingByValue;
import static java.util.stream.Collectors.toMap;

/**
 * Pacman
 * 1. 如果玩家执行异常，或超时，或撞到障碍物，该玩家判定为死亡
 * 2. 如果玩家在没有无敌光环情况下，碰到ghost，判定为死亡
 * 3. 如果玩家在战斗中牺牲，判定为死亡
 * 4. 游戏比赛运行到只剩最后一个玩家，或者超过预定回合（500）结束比赛，根据力量值判定输赢
 *
 * @author zhuhy
 * @version 1.0
 */

public class JavaPlayer {

    /**
     * 选手的初始位置
     */
    public LocationInfo myLocationInfo;
    /**
     * 每回合执行的时间约束，单位毫秒
     */
    public int timeLimit;

    /* 该接口在游戏中只会被调用一次,用于告知选手程序游戏的初始信息。
     *
     * @param position 选手的位置
     * @param timeLimit 每回合执行的时间约束，单位毫秒
     * @throws Exception
     */
    public void ready(int position, int timeLimit) throws Exception {
        this.myLocationInfo = new LocationInfo(position / 21, position % 21);
        this.timeLimit = timeLimit;
    }

    /**
     * 该接口在游戏中每回合都会被调用一次，用于告知选手程序当前局面，选手程序自行决定行动。
     * 注意：如果board数组中有值超过10000，代表它有无敌光环，strength也是。
     *
     * @param board    局面的位置列表，假设a行b列(0<=a,b<21)，那么对应的值是21*a+b，当前位置为ghost为-1，为空表示-2
     *                 ，为-3表示大豆子,为-4表示小豆子,为-5、-6、-7表示不同的障碍，
     *                 其余>=0 为该位置的pacman所代表的力量（包括一个自己）
     * @param strength 当前的力量，由服务器传回来
     * @return 方向0代表不动，1,2,3,4 分别代表左、上、右、下，其他输入皆非法
     * @throws Exception
     */
    public int run(int[] board, int strength) throws Exception {
        // 一维转换二维地图
        int[][] maps = switchArray(board, 21, 21);
        // 根据地图信息 选择一条最合适的路线
        StrategyController controller = new StrategyController(myLocationInfo);
        List<Integer> pathList = controller.planA(maps);
        int result = pathList.size() > 0 ? pathList.get(0) : Constants.STAY;
        System.out.println(myLocationInfo);
        // 更新我的位置信息
        myLocationUpdate(result);
        System.out.println(myLocationInfo);
        // 返回行动结果
        return result;
    }

    /**
     * 更新我的位置信息
     *
     * @param actionResult 行动结果
     */
    private void myLocationUpdate(int actionResult) {
        switch (actionResult) {
            case Constants.UP:
                myLocationInfo.setY(myLocationInfo.getY() - 1);
                break;
            case Constants.DOWN:
                myLocationInfo.setY(myLocationInfo.getY() + 1);
                break;
            case Constants.LEFT:
                myLocationInfo.setX(myLocationInfo.getX() - 1);
                break;
            case Constants.RIGHT:
                myLocationInfo.setX(myLocationInfo.getX() + 1);
                break;
        }


    }

    /**
     * 一维数组转化为二维数组
     *
     * @param array 一维数组
     * @param line  　行
     * @param row   列
     * @return 二维数组
     */
    public static int[][] switchArray(int[] array, int line, int row) {
        int[][] resultArray = new int[line][row];
        int length = array.length;
        for (int index = 0; index < length; index++) {
            int num = array[index];
            //一维数组中的第index个除三取余数为二维数组的行
            int a = index / line;
            //一维数组中的第index个除三取模为二维数组的列
            int b = index % row;
            resultArray[a][b] = num;
        }
        return resultArray;
    }

    /**
     * 寻找目标的坐标列表
     *
     * @param target 目标的代表值
     * @param array  地图
     * @return 目标的坐标列表
     */
    public static List<LocationInfo> FindTargetLocation(int target, int[][] array) {
        List<LocationInfo> locationInfoList = new ArrayList<>();
        int row = array.length;//行数
        int col = array[0].length;//列数
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                if (array[i][j] == target) locationInfoList.add(new LocationInfo(j, i));
            }
        }
        return locationInfoList;
    }

    /**
     * 寻找所有玩家的坐标列表
     *
     * @param array 地图
     * @return 所有玩家的坐标列表
     * K:玩家位置，V:力量值
     */
    public static Map<LocationInfo, Integer> FindPlayer(int[][] array) {
        Map<LocationInfo, Integer> resultMap = new HashMap<>();
        int row = array.length;//行数
        int col = array[0].length;//列数
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                if (array[i][j] >= 0) {
                    resultMap.put(new LocationInfo(j, i), array[i][j]);
                }
            }
        }
        return resultMap;
    }

    /**
     * 计算自己到所有豆子的距离
     * 计算豆子到所有玩家的距离
     *
     * @param myLocationInfo   我的当前位置
     * @param locationInfoList 豆子的位置列表
     * @return K：位置列表 V：距离
     */
    public static Map<LocationInfo, Integer> getAllDistance(LocationInfo myLocationInfo, List<LocationInfo> locationInfoList) {
        Map<LocationInfo, Integer> resultMap = new HashMap<>();
        if (locationInfoList != null) {
            // 遍历豆子的位置列表 计算每个豆子到自己的距离
            for (LocationInfo l : locationInfoList) {
                resultMap.put(l, getDistance(l, myLocationInfo));
            }
        }
        return resultMap;
    }

    /**
     * 通过XY轴的坐标来判断方向
     *
     * @param current 当前位置
     * @param target  目标位置
     * @return 上下左右
     */
    public static int getDirectionByLocation(LocationInfo current, LocationInfo target) {
        if (current.getX() < target.getX()) {
            return Constants.LEFT;
        } else if (current.getX() > target.getX()) {
            return Constants.RIGHT;
        }
        if (current.getY() < target.getY()) {
            return Constants.UP;
        } else if (current.getY() > target.getY()) {
            return Constants.DOWN;
        }
        return Constants.STAY;
    }

    /**
     * 计算两点间的距离：“曼哈顿”法，坐标分别取差值相加
     *
     * @param endLocation   终点
     * @param startLocation 起点
     * @return 两点之间距离
     */
    public static int getDistance(LocationInfo endLocation, LocationInfo startLocation) {
        return Math.abs(endLocation.x - startLocation.x)
                + Math.abs(endLocation.y - startLocation.y);
    }


}


/**
 * 共通定数
 * @author zhuhy
 */
class Constants{
    /**
     * 保持不动
     */
    public static final int STAY = 0;
    /**
     * 上
     */
    public static final int UP = 1;
    /**
     * 下
     */
    public static final int DOWN = 2;
    /**
     * 左
     */
    public static final int LEFT = 3;
    /**
     * 右
     */
    public static final int RIGHT = 4;

    /**
     * 鬼
     */
    public static final int GHOST = -1;
    /**
     * 空地
     */
    public static final int SPACE = -2;
    /**
     * 大豆子
     */
    public static final int BIGPAC = -3;
    /**
     * 小豆子
     */
    public static final int SMALLPAC = -4;
    /**
     * 障碍物
     */
    public static final int[] BARRIER = {-5, -6, -7};

}

// Pojo
/**
 * ClassName: LocationInfo
 * 按照二维数组的特点，坐标原点在左上角，所以y是高，x是宽，y向下递增，x向右递增，
 *
 * @author zhuhy
 * @Description: 位置信息
 */
class LocationInfo {

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

/**
 * ClassName: MapInfo
 *
 * @author zhuhy
 * @Description: 包含地图所需的所有输入数据
 */
class MapInfo {
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

/**
 * ClassName: Node
 *
 * @author zhuhy
 * @Description: 路径结点
 */
class Node implements Comparable<Node> {

    public LocationInfo locationInfo; // 坐标
    public Node parent; // 父结点
    public int G; // G：是个准确的值，是起点到当前结点的代价
    public int H; // H：是个估值，当前结点到目的结点的估计代价

    public Node(int x, int y) {
        this.locationInfo = new LocationInfo(x, y);
    }

    public Node(LocationInfo locationInfo) {
        this.locationInfo = locationInfo;
    }

    public Node(LocationInfo locationInfo, Node parent, int g, int h) {
        this.locationInfo = locationInfo;
        this.parent = parent;
        G = g;
        H = h;
    }

    @Override
    public int compareTo(Node o) {
        if (o == null) return -1;
        if (G + H > o.G + o.H)
            return 1;
        else if (G + H < o.G + o.H) return -1;
        return 0;
    }
}

// 共同方法
/**
 * ClassName: AStar
 *
 * @author zhuhy
 * @Description: A星算法
 */
class AStar {
    // 障碍值
    public final static List<Integer> BAR = new ArrayList<Integer>() {{
        // 障碍物
        add(-5);
        add(-6);
        add(-7);
        // ghost
        add(-1);
    }};
    public final static int PATH = 2; // 路径
    public final static int DIRECT_VALUE = 10; // 横竖移动代价
//	public final static int OBLIQUE_VALUE = 14; // 斜移动代价

    Queue<Node> openList = new PriorityQueue<>(); // 优先队列(升序)
    List<Node> closeList = new ArrayList<>();
    List<Integer> pathList = new ArrayList<>();

    /**
     * 开始算法
     */
    public List<Integer> start(MapInfo mapInfo) {
        if (mapInfo == null) return pathList;
        // clean
        openList.clear();
        closeList.clear();
        // 开始搜索
        openList.add(mapInfo.start);
        moveNodes(mapInfo);
        return pathList;
    }

    /**
     * 移动当前结点
     */
    private void moveNodes(MapInfo mapInfo) {
        while (!openList.isEmpty()) {
            Node current = openList.poll();
            closeList.add(current);
            addNeighborNodeInOpen(mapInfo, current);
            if (isInClose(mapInfo.end.locationInfo)) {
                drawPath(mapInfo.maps, mapInfo.end);
                break;
            }
        }
    }

    /**
     * 回溯法绘制路径
     * 在二维数组中绘制路径
     * 路径是倒叙
     */
    private void drawPath(int[][] maps, Node end) {
        if (end == null || maps == null) return;
        System.out.println("总代价：" + end.G);
        // 记录上次的位置
        LocationInfo tmp = null;
        while (end != null) {
            LocationInfo c = end.locationInfo;
            if (tmp != null) pathList.add(JavaPlayer.getDirectionByLocation(tmp, c));
            tmp = c;
            maps[c.y][c.x] = PATH;
            end = end.parent;
        }
        //因为是回溯法，所以需要颠倒顺序
        Collections.reverse(pathList); // 倒序排列
    }

    /**
     * 添加所有邻结点到open表
     */
    private void addNeighborNodeInOpen(MapInfo mapInfo, Node current) {
        int x = current.locationInfo.x;
        int y = current.locationInfo.y;
        // 左
        addNeighborNodeInOpen(mapInfo, current, x - 1, y, DIRECT_VALUE);
        // 上
        addNeighborNodeInOpen(mapInfo, current, x, y - 1, DIRECT_VALUE);
        // 右
        addNeighborNodeInOpen(mapInfo, current, x + 1, y, DIRECT_VALUE);
        // 下
        addNeighborNodeInOpen(mapInfo, current, x, y + 1, DIRECT_VALUE);

    }

    /**
     * 添加一个邻结点到open表
     */
    private void addNeighborNodeInOpen(MapInfo mapInfo, Node current, int x, int y, int value) {
        if (canAddNodeToOpen(mapInfo, x, y)) {
            Node end = mapInfo.end;
            LocationInfo locationInfo = new LocationInfo(x, y);
            int G = current.G + value; // 计算邻结点的G值
            Node child = findNodeInOpen(locationInfo);
            if (child == null) {
                int H = calcH(end.locationInfo, locationInfo); // 计算H值
                if (isEndNode(end.locationInfo, locationInfo)) {
                    child = end;
                    child.parent = current;
                    child.G = G;
                    child.H = H;
                } else {
                    child = new Node(locationInfo, current, G, H);
                }
                openList.add(child);
            } else if (child.G > G) {
                child.G = G;
                child.parent = current;
                openList.add(child);
            }
        }
    }

    /**
     * 从Open列表中查找结点
     */
    private Node findNodeInOpen(LocationInfo locationInfo) {
        if (locationInfo == null || openList.isEmpty()) return null;
        for (Node node : openList) {
            if (node.locationInfo.equals(locationInfo)) {
                return node;
            }
        }
        return null;
    }


    /**
     * 计算H的估值：“曼哈顿”法，坐标分别取差值相加
     */
    private int calcH(LocationInfo end, LocationInfo locationInfo) {
        return Math.abs(end.x - locationInfo.x)
                + Math.abs(end.y - locationInfo.y);
    }

    /**
     * 判断结点是否是最终结点
     */
    private boolean isEndNode(LocationInfo end, LocationInfo locationInfo) {
        return end != null && end.equals(locationInfo);
    }

    /**
     * 判断结点能否放入Open列表
     */
    private boolean canAddNodeToOpen(MapInfo mapInfo, int x, int y) {
        // 是否在地图中
        if (x < 0 || x >= mapInfo.width || y < 0 || y >= mapInfo.hight) return false;
        // 判断是否是不可通过的结点
        if (BAR.contains(mapInfo.maps[y][x])) return false;
        // 判断结点是否存在close表
        return !isInClose(x, y);
    }

    /**
     * 判断坐标是否在close表中
     */
    private boolean isInClose(LocationInfo locationInfo) {
        return locationInfo != null && isInClose(locationInfo.x, locationInfo.y);
    }

    /**
     * 判断坐标是否在close表中
     */
    private boolean isInClose(int x, int y) {
        if (closeList.isEmpty()) return false;
        for (Node node : closeList) {
            if (node.locationInfo.x == x && node.locationInfo.y == y) {
                return true;
            }
        }
        return false;
    }
}

/**
 * 行进路线控制器
 *
 * @author zhuhy
 * <p>
 * 整体策略：
 * 考虑其他玩家的位置情报，找到距离自己最近的豆子
 * 考虑其他玩家的力量值，避免被其他玩家杀掉以及杀掉比自己力量低的玩家
 * 考虑GHOST的位置情报，避免距离GHOST最近防止被追杀
 * 考虑地图特性（边界互通）
 */
class StrategyController {

    /**
     * 我的位置
     */
    public LocationInfo myLocationInfo;

    /**
     * 大豆子的位置
     */
    public List<LocationInfo> bigPacList;

    /**
     * 小豆子的位置
     */
    public List<LocationInfo> smallPacList;
    /**
     * 力量值比自己高的玩家的位置
     */
    public List<LocationInfo> powerHigherPlayerList = new ArrayList<>();
    /**
     * 所有玩家的情报
     * K：位置 V：力量值
     */
    public Map<LocationInfo, Integer> allPlayerMap;

    /**
     * 我的位置初始化
     *
     * @param myLocationInfo 我的位置
     */
    public StrategyController(LocationInfo myLocationInfo) {
        this.myLocationInfo = myLocationInfo;
    }

    /**
     * 获取地图上的豆子情报
     *
     * @param map 地图情报
     */
    public void getPacInformation(int[][] map) {
        // 找到所有的小豆子的位置
        smallPacList = JavaPlayer.FindTargetLocation(Constants.SMALLPAC, map);
        System.out.println("所有小豆子的位置：" + smallPacList.toString());
        bigPacList = JavaPlayer.FindTargetLocation(Constants.BIGPAC, map);
        System.out.println("所有大豆子的位置：" + bigPacList.toString());
    }

    /**
     * 获取地图上的其他玩家的位置
     *
     * @param map 地图情报
     */
    public void getPlayerInformation(int[][] map) {
        // 找到所有玩家的位置
        allPlayerMap = JavaPlayer.FindPlayer(map);
        // 我的力量值
        int myPowerValue = 0;
        for (Map.Entry<LocationInfo, Integer> entry : allPlayerMap.entrySet()) {
            if (entry.getKey() == myLocationInfo) {
                myPowerValue = entry.getValue();
            }
        }
        // 力量值比自己高的玩家的位置
        for (Map.Entry<LocationInfo, Integer> entry : allPlayerMap.entrySet()) {
            if (entry.getValue() > myPowerValue) {
                powerHigherPlayerList.add(entry.getKey());
            }
        }
        System.out.println("所有玩家的情报：" + allPlayerMap.toString());
        System.out.println("力量值比自己高的玩家的位置：" + powerHigherPlayerList.toString());

    }

    /**
     * 计算最有可能吃到的豆子的位置
     *
     * @param allPacDistance 所有豆子的位置
     * @return 最有可能吃到的豆子的位置
     */
    public LocationInfo getMostPossiblePacLocation(Map<LocationInfo, Integer> allPacDistance) {
        // 将HashMap按照距离排序
        Map<LocationInfo, Integer> sortedPac = allPacDistance
                .entrySet()
                .stream()
                .sorted(comparingByValue())
                .collect(toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2, LinkedHashMap::new));
        System.out.println("排序：" + sortedPac);
        // 找到距离自己最近的豆子
        LocationInfo resultLocationInfo = null;
        // 按照距离排序后，遍历豆子的列表，避开其他玩家选择豆子
        if (powerHigherPlayerList.size() > 1) {
            for (Map.Entry<LocationInfo, Integer> entry : sortedPac.entrySet()) {
                // 这个豆子到其他力量值比自己高的玩家的距离
                Map<LocationInfo, Integer> allPlayerDistance = JavaPlayer.getAllDistance(entry.getKey(), powerHigherPlayerList);
                System.out.println("当前豆子到其他玩家的距离：" + allPlayerDistance);
                for (Map.Entry<LocationInfo, Integer> allPlayerEntry : allPlayerDistance.entrySet()) {
                    //当前豆子相比其他玩家来说，距离我最近时，结束循环。
                    if (entry.getValue() < allPlayerEntry.getValue()) {
                        resultLocationInfo = entry.getKey();
                        break;
                    }
                }
                if (resultLocationInfo != null) {
                    break;
                }
            }
        }
        // 我当前的力量值场上最高时,无需考虑其他玩家
        else {
            resultLocationInfo = sortedPac.keySet().stream().findFirst().orElse(null);
        }
        return resultLocationInfo;
    }

    /**
     * 能否有吃大豆子的可能性
     * 根据场上形势，判断自己能否有去抢夺大豆子的机会
     *
     * @param map 地图
     * @return 移动路线
     * 当有可能吃到大豆子时，返回行动路线
     * 反之，返回[]
     */
    public List<Integer> hasBigPacChance(int[][] map) {
        List<Integer> pathList = new ArrayList<>();
        // 计算自己到所有大豆子的距离
        Map<LocationInfo, Integer> allBigPacDistance = JavaPlayer.getAllDistance(myLocationInfo, bigPacList);
        System.out.println("自己到所有大豆子的距离：" + allBigPacDistance);
        // 计算最有可能吃到的豆子的位置
        LocationInfo resultLocationInfo = getMostPossiblePacLocation(allBigPacDistance);
        //确实找到了一个豆子 相比其他玩家距离我最近时。
        if (resultLocationInfo != null) {
            System.out.println("--------------------有机会去吃大豆子--------------------");
            // 规划路线
            MapInfo info = new MapInfo(map, map[0].length, map.length, new Node(myLocationInfo), new Node(resultLocationInfo));
            pathList = new AStar().start(info);
            System.out.println("移动路线：" + pathList);
            System.out.println("吃到大豆子需要的回合数：" + pathList.size());
        } else {
            System.out.println("--------------------没有机会去吃大豆子--------------------");
        }
        return pathList;
    }

    /**
     * 能否有吃小豆子的可能性
     * 根据场上形势，判断自己能否有去抢夺小豆子的机会
     *
     * @param map 地图
     * @return 移动路线
     * 当有可能吃到小豆子时，返回行动路线
     * 反之，返回[]
     */
    public List<Integer> hasSmallPacChance(int[][] map) {
        List<Integer> pathList = new ArrayList<>();
        Map<LocationInfo, Integer> allSmallPacDistance = JavaPlayer.getAllDistance(myLocationInfo, smallPacList);
        System.out.println("自己到所有小豆子的距离：" + allSmallPacDistance);
        // 计算最有可能吃到的豆子的位置
        LocationInfo resultLocationInfo = getMostPossiblePacLocation(allSmallPacDistance);
        //确实找到了一个豆子 相比其他玩家距离我最近时。
        if (resultLocationInfo != null) {
            System.out.println("--------------------有机会去吃小豆子--------------------");
            // 规划路线
            MapInfo info = new MapInfo(map, map[0].length, map.length, new Node(myLocationInfo), new Node(resultLocationInfo));
            pathList = new AStar().start(info);
            System.out.println("移动路线：" + pathList);
            System.out.println("吃到豆子需要的回合数：" + pathList.size());
        } else {
            System.out.println("--------------------没有机会去吃小豆子--------------------");
        }
        return pathList;
    }

    /**
     * 当自己能力值不是最高时，应避开其他玩家。
     * 获取全部豆子的位置以及玩家的位置
     * 分别计算出最短路径
     * 选择一条最优解
     */
    public List<Integer> planA(int[][] map) {
        List<Integer> pathList = new ArrayList<>();
        // 获取地图上的豆子情报
        getPacInformation(map);
        // 获取地图上的其他玩家的位置
        getPlayerInformation(map);
        // 当场上存在大豆子时，进行可能性判断
        if (bigPacList.size() > 0) {
            pathList = hasBigPacChance(map);
        }
        // 当没有机会去吃大豆子时
        if (pathList.size() == 0) {
            pathList = hasSmallPacChance(map);
        }
        return pathList;
    }

}


