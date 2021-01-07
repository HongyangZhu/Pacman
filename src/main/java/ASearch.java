import java.util.ArrayList;
import java.util.LinkedList;

/**
 * A*搜索算法
 * <p>
 * 规定：
 * 数字大于5的表示为障碍物
 * 数字为1表示起点
 * 数字2表示终点
 * 小于0的数用作标记处理
 *
 * @author zhuhy
 */
public class ASearch {

    public LinkedList<Node> closelist = new LinkedList<Node>();
    public LinkedList<Node> openlist = new LinkedList<Node>();
    private Node start;
    private Node end;


    public ASearch(Node start, Node end) {
        super();
        this.start = start;
        this.end = end;
    }


    public Node getStart() {
        return start;
    }


    public void setStart(Node start) {
        this.start = start;
    }


    public Node getEnd() {
        return end;
    }


    public void setEnd(Node end) {
        this.end = end;
    }


    /**
     * 检查start end 若G H F值不存在则初始化
     */
    public void checkGHF() {
        if (!start.canSearch || !end.canSearch) {
            start.G = 0;
            start.H = Math.abs(end.X - start.X) + Math.abs(end.Y - start.Y);
            start.F = start.G + start.H;

            end.G = 0;
            end.H = 0;
            end.F = 0;
        }
    }

    public Node findPath(int[][] map) {  //搜索区域划分为二维数组
        checkGHF();

        Node correntNode = null;  //当前节点

        //1.将start加入openlist
        openlist.add(start);
        map[start.X][start.Y] = -1; //标记为已加入openlist

        //退出条件 openlist为空 或者 openlist将终点包含进来
        while (!openlist.isEmpty()) {
            //获得openlist中F最小的节点 并设为当前节点
            correntNode = openlist.getFirst();

            System.out.println(correntNode.toString());
            //将当前节点加入closelist
            closelist.add(correntNode);
            openlist.remove(correntNode);
            map[correntNode.X][correntNode.Y] = -2;  //标记为已加入closelist

            //更新openlist
            if (updateOpenlist(correntNode, map)) {
                break;
            }
            System.out.println(openlist.size());
        }
        return correntNode;
    }


    //根据F值插入链表  维护链表
    public void insert(Node node) {
        if (openlist.size() == 0) {
            openlist.addFirst(node);
            return;
        }
        int i = 0;
        while (node.F > openlist.get(i).F) {
            i++;
            if (i >= openlist.size()) {
                openlist.addLast(node);
                return;
            }
        }
        openlist.add(i, node);
    }

    //更新openlist
    public boolean updateOpenlist(Node node, int[][] map) {
//		openlist.clear();
        //更新当前节点的openlist与原来openlist交集中数据的FG值
        ArrayList<Integer> indexList = new ArrayList<Integer>();
        for (int i = 0; i < openlist.size(); i++) {
            if (Math.abs(openlist.get(i).X - node.X) == 1 && Math.abs(openlist.get(i).Y - node.Y) == 1) {
                openlist.get(i).updateFG(14);
                indexList.add(i);
            }

            if ((Math.abs(openlist.get(i).X - node.X) == 0 && Math.abs(openlist.get(i).Y - node.Y) == 1) || Math.abs(openlist.get(i).X - node.X) == 1 && Math.abs(openlist.get(i).Y - node.Y) == 0) {
                openlist.get(i).updateFG(10);
                indexList.add(i);
            }

        }

        //重排openlist 维持有序
        sortOpenlist(indexList);

        if (node.X >= 1 && node.X < map.length - 1 && node.Y >= 1 && node.Y < map[0].length - 1) {//a.正常情况下

            int x = node.X;
            int y = node.Y;
            for (int i = x - 1; i <= x + 1; i++) {
                for (int j = y - 1; j <= y + 1; j++) {

                    if (map[i][j] == 2) {
                        System.out.println(2);
                        return true;
                    }
                    if ((i == x && j == y) || map[i][j] == -2 || map[i][j] > 4) {
                        continue;
                    }
                    //对点不可达情况
                    if (i != x && j != y && (map[i][y] > 4 || map[x][j] > 4)) {
                        continue;
                    }

                    //当前点不存在openlist中
                    if (map[i][j] != -1) {
                        int g = 0;
                        if (i != x && j != y) {
                            g = 14;
                        } else {
                            g = 10;
                        }
                        Node newnode = new Node(i, j, getH(i, j), g, node);

                        insert(newnode);
                    }

                }
            }
        }

        /**
         * 接下来我们就要考虑如果当前点处于边界的情况了
         * 在处于边界的时候，我们就检查的零接点个数就变得不确定了
         */
        return false;
    }

    /*
     * 重排openlist维持有序  基于此特殊情况的排序方式
     * 排序原则:先将变动的node复制一份,在openlist去除变动过得node
     * 在根据大小重新插入
     */
    public void sortOpenlist(ArrayList<Integer> list) {
        LinkedList<Node> temp = new LinkedList<Node>();
        for (int i = list.size() - 1; i >= 0; i--) {
            temp.add(openlist.get(list.get(i)));
            openlist.remove(list.get(i));
        }
        openlist.clear();  //将不包含在新节点的零接节点范围的点清除
        for (int i = 0; i < temp.size(); i++) {
            insert(temp.get(i));
        }
    }

    //计算H值
    public int getH(int x, int y) {
        int h = Math.abs(end.X - x) + Math.abs(end.Y - y);
        return h * 10;
    }

}
