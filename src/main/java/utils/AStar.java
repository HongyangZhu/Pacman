package utils;

import pojo.LocationInfo;
import pojo.MapInfo;
import pojo.Node;

import java.util.*;

/**
 * ClassName: AStar
 *
 * @author zhuhy
 * @Description: A星算法
 */
public class AStar {
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
            if (tmp != null) pathList.add(CommentUtils.getDirectionByLocation(tmp, c));
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