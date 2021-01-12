package utils;

import constant.Constants;
import pojo.LocationInfo;

import java.util.*;

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
