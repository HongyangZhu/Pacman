package Utils;

import pojo.Position;

import java.util.Arrays;

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
        int row = (int) array.length;//行数
        int col = (int) array[0].length;//列数
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
}
