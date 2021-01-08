package Controller;

import Utils.AStar;
import Utils.CommentUtils;
import constant.Constants;
import pojo.MapInfo;
import pojo.Node;
import pojo.locationInfo;

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
    public locationInfo myLocationInfo = new locationInfo(6, 17);

    /**
     * 大豆子的位置
     */
    public List<locationInfo> BigPacList;

    /**
     * 小豆子的位置
     */
    public List<locationInfo> SmallPacList;
    /**
     * 力量值比自己高的玩家的位置
     */
    public List<locationInfo> powerHigherPlayerList = new ArrayList<>();
    /**
     * 所有玩家的情报
     * K：位置 V：力量值
     */
    public Map<locationInfo, Integer> allPlayerMap;

    /**
     * 获取地图上的豆子情报
     *
     * @param map 地图情报
     */
    public void getPacInformation(int[][] map) {
        // 找到所有的小豆子的位置
        SmallPacList = CommentUtils.FindPac(Constants.SMALLPAC, map);
        System.out.println("所有小豆子的位置：" + SmallPacList.toString());
        BigPacList = CommentUtils.FindPac(Constants.BIGPAC, map);
        System.out.println("所有大豆子的位置：" + BigPacList.toString());
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
        for (Map.Entry<locationInfo, Integer> entry : allPlayerMap.entrySet()) {
            if (entry.getKey() == myLocationInfo) {
                myPowerValue = entry.getValue();
            }
        }
        // 力量值比自己高的玩家的位置
        for (Map.Entry<locationInfo, Integer> entry : allPlayerMap.entrySet()) {
            if (entry.getValue() > myPowerValue) {
                powerHigherPlayerList.add(entry.getKey());
            }
        }
        System.out.println("所有玩家的情报：" + allPlayerMap.toString());
        System.out.println("力量值比自己高的玩家的位置：" + powerHigherPlayerList.toString());

    }

    /**
     * 能否有吃大豆子的可能性
     * 根据场上形势，判断自己能否有去抢夺大豆子的机会
     */
    public boolean hasBigPacChance(int[][] map) {
        long time1 = System.currentTimeMillis();
        // 计算自己到所有大豆子的距离
        Map<locationInfo, Integer> allPacDistance = CommentUtils.getAllDistance(myLocationInfo, BigPacList);
        System.out.println("自己到所有大豆子的距离：" + allPacDistance);
        // 将HashMap按照距离排序
        Map<locationInfo, Integer> sortedPac = allPacDistance
                .entrySet()
                .stream()
                .sorted(comparingByValue())
                .collect(toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2, LinkedHashMap::new));
        System.out.println("排序：" + sortedPac);
        // 找到距离自己最近的豆子
        locationInfo resultlocationInfo = null;
        // 按照距离排序后，遍历豆子的列表，避开其他玩家选择豆子
        if (powerHigherPlayerList.size() > 1) {
            for (Map.Entry<locationInfo, Integer> entry : sortedPac.entrySet()) {
                // 这个豆子到其他力量值比自己高的玩家的距离
                Map<locationInfo, Integer> allPlayerDistance = CommentUtils.getAllDistance(entry.getKey(), powerHigherPlayerList);
                System.out.println("当前豆子到其他玩家的距离：" + allPlayerDistance);
                for (Map.Entry<locationInfo, Integer> allPlayerEntry : allPlayerDistance.entrySet()) {
                    //当前豆子相比其他玩家来说，距离我最近时，结束循环。
                    if (entry.getValue() < allPlayerEntry.getValue()) {
                        resultlocationInfo = entry.getKey();
                        break;
                    }
                }
                if (resultlocationInfo != null) {
                    break;
                }
            }
        }
        // 我当前的力量值场上最高时,无需考虑其他玩家
        else {
            resultlocationInfo = sortedPac.keySet().stream().findFirst().orElse(null);
        }
        //确实找到了一个豆子 相比其他玩家距离我最近时。
        if (resultlocationInfo != null) {
            System.out.println("--------------------有机会去吃大豆子--------------------");
            // 规划路线
            MapInfo info = new MapInfo(map, map[0].length, map.length, new Node(myLocationInfo), new Node(resultlocationInfo));
            List<Integer> list = new AStar().start(info);
            System.out.println("移动路线：" + list);
            System.out.println("吃到大豆子需要的回合数：" + list.size());
            long time2 = System.currentTimeMillis();
            System.out.println("-----------------------------耗时:" + (time2 - time1) + "ms-----------------------------");
            return true;
        } else {
            System.out.println("--------------------没有机会去吃大豆子--------------------");
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
        long time1 = System.currentTimeMillis();
        boolean flg = false;
        // 获取地图上的其他玩家的位置
        getPacInformation(map);
        // 获取地图上的其他玩家的位置
        getPlayerInformation(map);
        // 当场上存在大豆子时，进行可能性判断
        if (BigPacList.size() > 0) {
            flg = hasBigPacChance(map);
        }
        if (!flg) {
            // 计算自己到所有小豆子的距离
            Map<locationInfo, Integer> allPacDistance = CommentUtils.getAllDistance(myLocationInfo, SmallPacList);
            System.out.println("自己到所有小豆子的距离：" + allPacDistance);
            // 将HashMap按照距离排序
            Map<locationInfo, Integer> sortedPac = allPacDistance
                    .entrySet()
                    .stream()
                    .sorted(comparingByValue())
                    .collect(toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2, LinkedHashMap::new));
            System.out.println("排序：" + sortedPac);
            // 找到距离自己最近的豆子
            locationInfo resultlocationInfo = null;
            // 按照距离排序后，遍历豆子的列表，避开其他玩家选择豆子
            if (powerHigherPlayerList.size() > 1) {
                for (Map.Entry<locationInfo, Integer> entry : sortedPac.entrySet()) {
                    // 这个豆子到其他力量值比自己高的玩家的距离
                    Map<locationInfo, Integer> allPlayerDistance = CommentUtils.getAllDistance(entry.getKey(), powerHigherPlayerList);
                    System.out.println("当前豆子到其他玩家的距离：" + allPlayerDistance);
                    for (Map.Entry<locationInfo, Integer> allPlayerEntry : allPlayerDistance.entrySet()) {
                        //当前豆子相比其他玩家来说，距离我最近时，结束循环。
                        if (entry.getValue() < allPlayerEntry.getValue()) {
                            resultlocationInfo = entry.getKey();
                            break;
                        }
                    }
                    if (resultlocationInfo != null) {
                        break;
                    }
                }
            }
            // 我当前的力量值场上最高时,无需考虑其他玩家
            else {
                resultlocationInfo = sortedPac.keySet().stream().findFirst().orElse(null);
            }
            //确实找到了一个豆子 相比其他玩家距离我最近时。
            if (resultlocationInfo != null) {
                // 规划路线
                MapInfo info = new MapInfo(map, map[0].length, map.length, new Node(myLocationInfo), new Node(resultlocationInfo));
                List<Integer> list = new AStar().start(info);
                System.out.println("移动路线：" + list);
                System.out.println("吃到豆子需要的回合数：" + list.size());
            }
            // TODO 当场上没有距离我最近的豆子时
            else {

            }

            long time2 = System.currentTimeMillis();
            System.out.println("-----------------------------耗时:" + (time2 - time1) + "ms-----------------------------");
        }


    }

    /**
     * TODO planB
     * 当自己能力值最高时（吃了大豆子），无须考虑场上其他玩家的位置。
     * 直接去距离自己最近的豆子
     */
    public void planB() {

    }
}
