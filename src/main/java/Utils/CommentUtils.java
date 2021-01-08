package Utils;

import constant.Constants;
import pojo.locationInfo;

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
     * @param nums 一维数组
     * @param line 　行
     * @param row  列
     * @return 二维数组
     */
    public static int[][] switchArray(int[] nums, int line, int row) {
        int[][] numArray = new int[line][row];
        int length = nums.length;
        for (int index = 0; index < length; index++) {
            int num = nums[index];
            //一维数组nums中的第index个除三取余数为二维数组的行
            int a = index / line;
            //一维数组nums中的第index个除三取模为二维数组的列
            int b = index % row;
            numArray[a][b] = num;
        }
        return numArray;
    }

    /**
     * 在二维数组查找指定元素 返回X，Y轴坐标
     *
     * @param target 指定元素
     * @param array  二维数组
     * @return X，Y轴坐标
     */
    public static locationInfo Find(int target, int[][] array) {
        int row = array.length;//行数
        int col = array[0].length;//列数
        int i = 0;
        int j = col - 1;
        while (i < row && j >= 0) {
            if (array[i][j] == target)
                return new locationInfo(j, i);
            else if (array[i][j] < target)
                i++;
            else
                j--;
        }
        return new locationInfo(0, 0);
    }

    /**
     * 寻找豆子的坐标列表
     *
     * @param target 豆子
     * @param array  地图
     * @return 豆子的坐标列表
     */
    public static List<locationInfo> FindPac(int target, int[][] array) {
        List<locationInfo> locationInfoList = new ArrayList<>();
        int row = array.length;//行数
        int col = array[0].length;//列数
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                if (array[i][j] == target) locationInfoList.add(new locationInfo(j, i));
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
    public static Map<locationInfo, Integer> FindPlayer(int[][] array) {
        Map<locationInfo, Integer> resultMap = new HashMap<>();
        int row = array.length;//行数
        int col = array[0].length;//列数
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                if (array[i][j] >= 0) {
                    resultMap.put(new locationInfo(j, i), array[i][j]);
                }
            }
        }
        return resultMap;
    }

    /**
     * 计算自己到所有豆子的距离
     * 计算豆子到所有玩家的距离
     *
     * @param mylocationInfo   我的当前位置
     * @param locationInfoList 豆子的位置列表
     * @return K：位置列表 V：距离
     */
    public static Map<locationInfo, Integer> getAllDistance(locationInfo mylocationInfo, List<locationInfo> locationInfoList) {
        Map<locationInfo, Integer> resultMap = new HashMap<>();
        int sum = 0;
        if (locationInfoList != null) {
            // 遍历豆子的位置列表 计算每个豆子到自己的距离
            for (locationInfo p : locationInfoList) {
                sum = 0;
                sum += Math.abs(p.getX() - mylocationInfo.getX());
                sum += Math.abs(p.getY() - mylocationInfo.getY());
                resultMap.put(p, sum);
            }
        }
        return resultMap;
    }

    /**
     * 寻找离自己最近的豆子
     *
     * @param mylocationInfo   我的当前位置
     * @param locationInfoList 豆子的位置列表
     * @return 最近的豆子位置
     */
    public static locationInfo FindNearestPac(locationInfo mylocationInfo, List<locationInfo> locationInfoList) {
        locationInfo resultlocationInfo = new locationInfo(0, 0);
        Map<locationInfo, Integer> resultMap = new HashMap<>();
        int sum = 0;
        if (locationInfoList != null) {
            for (locationInfo p : locationInfoList) {
                sum += Math.abs(p.getX() - mylocationInfo.getX());
                sum += Math.abs(p.getY() - mylocationInfo.getY());
                resultMap.put(p, sum);
                sum = 0;
            }
            // 将HashMap按照距离排序
            Map<locationInfo, Integer> sorted = resultMap
                    .entrySet()
                    .stream()
                    .sorted(comparingByValue())
                    .collect(toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2, LinkedHashMap::new));
            // 取出距离最短的对象位置
            resultlocationInfo = sorted.keySet().stream().findFirst().orElse(null);
        }
        return resultlocationInfo;
    }

    /**
     * 通过XY轴的坐标来判断方向
     *
     * @param current 当前位置
     * @param target  目标位置
     * @return 上下左右
     */
    public static int getDirectionByLocation(locationInfo current, locationInfo target) {
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
