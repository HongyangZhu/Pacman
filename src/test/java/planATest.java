import Controller.StrategyController;
import Utils.CommentUtils;

public class planATest {
    public static void main(String[] args) {
        // 地图
        int[] array = {
                -2,-2,-5,-5,-5,-2,-5,-5,-5,-2,-2,-2,-5,-5,-5,-2,-5,-5,-5,-2,-2,
                -5,-2,-2,-2,-2,-2,-2,-2,-2,-2,-2,-2,-2,-2,-2,-2,-2,-2,-2,-2,-5,
                -5,-2,-2,-2,-5,-2,-2,-2,-5,-2,-2,-2,-5,-2,-2,-2,-5,-2,-2,-2,-5,
                -5,-2,-2,-2,-5,-2,1,-5,-2,-2,-2,-2,-2,-5,2,-2,-5,-2,-2,-2,-5,
                -5,-2,-2,-2,-5,-2,-2,-2,-5,-2,-2,-2,-5,-2,-2,-2,-5,-2,-2,-2,-5,
                -5,-2,-2,-2,-2,-2,-2,-2,-2,-2,-2,-2,-2,-2,-2,-2,-2,-2,-2,-2,-5,
                -5,-2,-5,-5,-5,-5,-5,-5,-5,-2,-2,-2,-5,-5,-5,-5,-5,-5,-5,-2,-5,
                -2,-2,-2,-2,-2,-2,-2,-2,-2,-2,-2,-2,-2,-2,-2,-2,-4,-2,-2,-2,-2,
                -2,-2,-2,-2,-2,-2,-2,-2,-4,-2,-2,-2,-2,-2,-2,-2,-2,-2,-2,-2,-2,
                -2,-5,-5,-5,-5,-5,-5,-2,-2,-2,-2,-2,-2,-2,-5,-5,-5,-5,-5,-5,-2,
                -2,-2,-2,-2,-2,-2,-2,-2,-2,-2,-2,-2,-2,-2,-2,-2,-2,-2,-2,-2,-2,
                -2,-5,-5,-5,-5,-5,-5,-2,-2,-2,-2,-2,-2,-2,-5,-5,-5,-5,-5,-5,-2,
                -2,-2,-2,-2,-2,-2,-2,-2,-2,-2,-2,-2,-2,-2,-2,-2,-2,-2,-2,-2,-2,
                -2,-2,-2,-4,-2,-2,-2,-2,-2,-2,-2,-2,-2,-2,-2,-2,-2,-2,-2,-2,-2,
                -5,-2,-5,-5,-5,-5,-5,-5,-5,-2,-2,-2,-5,-5,-5,-5,-5,-5,-5,-2,-5,
                -5,-2,-2,-2,-2,-2,-2,-2,-2,-2,-2,-2,-2,-2,-2,-2,-2,-4,-2,-2,-5,
                -5,-2,-2,-2,-5,-2,-2,-2,-5,-2,-2,-2,-5,-2,-2,-2,-5,-2,-2,-2,-5,
                -5,-2,-2,-2,-5,-2,0,-5,-2,-2,-2,-2,-2,-5,0,-2,-5,-2,-2,-2,-5,
                -5,-2,-2,-2,-5,-2,-2,-2,-5,-2,-2,-2,-5,-2,-2,-2,-5,-2,-2,-2,-5,
                -5,-2,-2,-2,-2,-2,-2,-2,-2,-2,-2,-2,-2,-2,-2,-2,-2,-2,-2,-2,-5,
                -2,-2,-5,-5,-5,-2,-5,-5,-5,-2,-2,-2,-5,-5,-5,-2,-5,-5,-5,-2,-2
        };
        long time1 = System.currentTimeMillis();
        int[][] maps = CommentUtils.switchArray(array, 21, 21);

        StrategyController controller = new StrategyController();
        controller.planA(maps);

    }
}