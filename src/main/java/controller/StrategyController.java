package controller;

import utils.AStar;
import utils.CommentUtils;
import constant.Constants;
import pojo.MapInfo;
import pojo.Node;
import pojo.LocationInfo;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static java.util.Map.Entry.comparingByValue;
import static java.util.stream.Collectors.toMap;

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
public class StrategyController {

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
        smallPacList = CommentUtils.FindPac(Constants.SMALLPAC, map);
        System.out.println("所有小豆子的位置：" + smallPacList.toString());
        bigPacList = CommentUtils.FindPac(Constants.BIGPAC, map);
        System.out.println("所有大豆子的位置：" + bigPacList.toString());
    }

    /**
     * 获取地图上的其他玩家的位置
     *
     * @param map 地图情报
     */
    public void getPlayerInformation(int[][] map) {
        // 找到所有玩家的位置
        allPlayerMap = CommentUtils.FindPlayer(map);
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
                Map<LocationInfo, Integer> allPlayerDistance = CommentUtils.getAllDistance(entry.getKey(), powerHigherPlayerList);
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
     */
    public boolean hasBigPacChance(int[][] map) {
        // 计算自己到所有大豆子的距离
        Map<LocationInfo, Integer> allBigPacDistance = CommentUtils.getAllDistance(myLocationInfo, bigPacList);
        System.out.println("自己到所有大豆子的距离：" + allBigPacDistance);
        // 计算最有可能吃到的豆子的位置
        LocationInfo resultLocationInfo = getMostPossiblePacLocation(allBigPacDistance);
        //确实找到了一个豆子 相比其他玩家距离我最近时。
        if (resultLocationInfo != null) {
            System.out.println("--------------------有机会去吃大豆子--------------------");
            // 规划路线
            MapInfo info = new MapInfo(map, map[0].length, map.length, new Node(myLocationInfo), new Node(resultLocationInfo));
            List<Integer> list = new AStar().start(info);
            System.out.println("移动路线：" + list);
            System.out.println("吃到大豆子需要的回合数：" + list.size());
            return true;
        } else {
            System.out.println("--------------------没有机会去吃大豆子--------------------");
            return false;
        }
    }

    /**
     * 能否有吃小豆子的可能性
     * 根据场上形势，判断自己能否有去抢夺小豆子的机会
     */
    public boolean hasSmallPacChance(int[][] map) {
        Map<LocationInfo, Integer> allSmallPacDistance = CommentUtils.getAllDistance(myLocationInfo, smallPacList);
        System.out.println("自己到所有小豆子的距离：" + allSmallPacDistance);
        // 计算最有可能吃到的豆子的位置
        LocationInfo resultLocationInfo = getMostPossiblePacLocation(allSmallPacDistance);
        //确实找到了一个豆子 相比其他玩家距离我最近时。
        if (resultLocationInfo != null) {
            System.out.println("--------------------有机会去吃小豆子--------------------");
            // 规划路线
            MapInfo info = new MapInfo(map, map[0].length, map.length, new Node(myLocationInfo), new Node(resultLocationInfo));
            List<Integer> list = new AStar().start(info);
            System.out.println("移动路线：" + list);
            System.out.println("吃到豆子需要的回合数：" + list.size());
            return true;
        } else {
            System.out.println("--------------------没有机会去吃小豆子--------------------");
            return false;
        }
    }

    /**
     * 当自己能力值不是最高时，应避开其他玩家。
     * 获取全部豆子的位置以及玩家的位置
     * 分别计算出最短路径
     * 选择一条最优解
     */
    public void planA(int[][] map) {
        boolean hasBigPacChanceFlg = false;
        boolean hasSmallPacChanceFlg = false;
        // 获取地图上的其他玩家的位置
        getPacInformation(map);
        // 获取地图上的其他玩家的位置
        getPlayerInformation(map);
        // 当场上存在大豆子时，进行可能性判断
        if (bigPacList.size() > 0) {
            hasBigPacChanceFlg = hasBigPacChance(map);
        }
        // 当没有机会去吃大豆子时
        if (!hasBigPacChanceFlg) {
            hasSmallPacChanceFlg = hasSmallPacChance(map);
            // TODO 当没有机会去吃小豆子时
            if (!hasSmallPacChanceFlg) {

            }
        }
    }

    /**
     * TODO planB
     */
    public void planB() {

    }
}
