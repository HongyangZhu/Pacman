package pojo;

/**
 * 节点链表类
 *
 * @author zhuhy
 */
public class Node {
    public int X;
    public int Y;
    public int F;
    /**
     * H = 从指定的方格移动到终点 B 的估算成本。
     */
    public int H;
    /**
     * G = 从起点 A 移动到指定方格的移动代价，沿着到达该方格而生成的路径
     */
    public int G;
    public Node parent;

    public boolean canSearch = false;  //用于判断F H G值是否给定

    public void updateFG(int g) {
        G = g;
        F = G + H;
    }


    public Node(int x, int y, int f, int h, int g, Node parent) {
        super();
        X = x;
        Y = y;
        F = f;
        H = h;
        G = g;
        this.parent = parent;
        canSearch = true;
    }

    public Node() {
        super();
    }

    public Node(int x, int y, int h, int g, Node parent) {
        super();
        X = x;
        Y = y;
        H = h;
        G = g;
        F = g + h;
        this.parent = parent;
        canSearch = true;
    }


    @Override
    public String toString() {
        return "pojo.Node [X=" + X + ", Y=" + Y + ", F=" + F + ", H=" + H + ", G=" + G + ", parent=" + parent
                + ", canSearch=" + canSearch + "]";
    }


}
