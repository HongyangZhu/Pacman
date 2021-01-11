package utils;

import constant.Constants;
import pojo.LocationInfo;

import java.util.*;

import static java.util.Map.Entry.comparingByValue;
import static java.util.stream.Collectors.toMap;

/**
 * 共通方法类
 *
 * @author zhuhy
 */
public class CommentUtils {

    /**
     * 一维数组转化为二维数组
     *
     * @param array 一维数组
     * @param line 　行
     * @param row  列
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
     * 在二维数组查找指定元素 返回X，Y轴坐标
     *
     * @param target 指定元素
     * @param array  二维数组
     * @return X，Y轴坐标
     */
    public static LocationInfo Find(int target, int[][] array) {
        int row = array.length;//行数
        int col = array[0].length;//列数
        int i = 0;
        int j = col - 1;
        while (i < row && j >= 0) {
            if (array[i][j] == target)
                return new LocationInfo(j, i);
            else if (array[i][j] < target)
                i++;
            else
                j--;
        }
        return new LocationInfo(0, 0);
    }

    /**
     * 寻找豆子的坐标列表
     *
     * @param target 豆子
     * @param array  地图
     * @return 豆子的坐标列表
     */
    public static List<LocationInfo> FindPac(int target, int[][] array) {
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
        int sumDistance;
        if (locationInfoList != null) {
            // 遍历豆子的位置列表 计算每个豆子到自己的距离
            for (LocationInfo l : locationInfoList) {
                sumDistance = 0;
                sumDistance += Math.abs(l.getX() - myLocationInfo.getX());
                sumDistance += Math.abs(l.getY() - myLocationInfo.getY());
                resultMap.put(l, sumDistance);
            }
        }
        return resultMap;
    }

    /**
     * 寻找离自己最近的豆子
     *
     * @param myLocationInfo   我的当前位置
     * @param pacLocationInfoList 豆子的位置列表
     * @return 最近的豆子位置
     */
    public static LocationInfo FindNearestPac(LocationInfo myLocationInfo, List<LocationInfo> pacLocationInfoList) {
        LocationInfo resultLocationInfo = new LocationInfo(0, 0);
        Map<LocationInfo, Integer> resultMap = new HashMap<>();
        int sum = 0;
        if (pacLocationInfoList != null) {
            for (LocationInfo p : pacLocationInfoList) {
                sum += Math.abs(p.getX() - myLocationInfo.getX());
                sum += Math.abs(p.getY() - myLocationInfo.getY());
                resultMap.put(p, sum);
                sum = 0;
            }
            // 将HashMap按照距离排序
            Map<LocationInfo, Integer> sorted = resultMap
                    .entrySet()
                    .stream()
                    .sorted(comparingByValue())
                    .collect(toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2, LinkedHashMap::new));
            // 取出距离最短的对象位置
            resultLocationInfo = sorted.keySet().stream().findFirst().orElse(null);
        }
        return resultLocationInfo;
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
}