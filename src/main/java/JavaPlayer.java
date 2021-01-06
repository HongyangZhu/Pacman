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
     * 保持不动
     */
    public static final int STAY = 0;
    /**
     * 上
     */
    public static final int UP = 1;
    /**
     * 下
     */
    public static final int DOWN = 2;
    /**
     * 左
     */
    public static final int LEFT = 3;
    /**
     * 右
     */
    public static final int RIGHT = 4;

    /**
     * 鬼
     */
    public static final int GHOST = -1;
    /**
     * 空地
     */
    public static final int SPACE = -2;
    /**
     * 大豆子
     */
    public static final int BIGPAC = -3;
    /**
     * 小豆子
     */
    public static final int SMALLPAC = -4;
    /**
     * 障碍物
     */
    public static final int[] BARRIER = {-5, -6, -7};
    /**
     * 选手的初始位置
     */
    public int position;
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
        this.position = position;
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

        return UP;
    }


}
