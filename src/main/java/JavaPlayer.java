import constant.Constants;
import controller.StrategyController;
import pojo.LocationInfo;
import utils.CommentUtils;

import java.util.List;

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
        int[][] maps = CommentUtils.switchArray(board, 21, 21);
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

}
