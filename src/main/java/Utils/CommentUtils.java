package Utils;

import pojo.Position;

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
    public static Position Find(int target, int[][] array) {
        int row = array.length;//行数
        int col = array[0].length;//列数
        int i = 0;
        int j = col - 1;
        while (i < row && j >= 0) {
            if (array[i][j] == target)
                return new Position(i, j);
            else if (array[i][j] < target)
                i++;
            else
                j--;
        }
        return new Position(0, 0);
    }

    /**
     * 寻找豆子的坐标列表
     *
     * @param target 豆子
     * @param array  地图
     * @return 豆子的坐标列表
     */
    public static List<Position> FindPac(int target, int[][] array) {
        List<Position> positionList = new ArrayList<>();
        int row = array.length;//行数
        int col = array[0].length;//列数
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                if (array[i][j] == target) positionList.add(new Position(i, j));
            }
        }
        return positionList;
    }

    /**
     * 寻找离自己最近的豆子
     *
     * @param myPosition   我的当前位置
     * @param positionList 豆子的位置列表
     * @return 最近的豆子位置
     */
    public static Position FindNearestPac(Position myPosition, List<Position> positionList) {
        Position resultPosition = new Position(0, 0);
        Map<Position, Integer> resultMap = new HashMap<>();
        int sum = 0;
        if (positionList != null) {
            for (Position p : positionList) {
                sum += Math.abs(p.getX() - myPosition.getX());
                sum += Math.abs(p.getY() - myPosition.getY());
                resultMap.put(p, sum);
                sum = 0;
            }
            // 将HashMap按照距离排序
            Map<Position, Integer> sorted = resultMap
                    .entrySet()
                    .stream()
                    .sorted(comparingByValue())
                    .collect(toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2, LinkedHashMap::new));
            // 取出距离最短的对象位置
            resultPosition = sorted.keySet().stream().findFirst().orElse(null);
        }
        return resultPosition;
    }
}
