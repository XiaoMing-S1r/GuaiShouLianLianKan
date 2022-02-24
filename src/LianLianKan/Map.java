package LianLianKan;

import java.util.ArrayList;

public class Map {

    private final int[][] map;//地图
    private int types;//一共有types种不同的图案
    private int n;//地图有n行n列

    //构造函数
    public Map(int types, int n) {
        this.types = types;
        this.n = n;
        map = new int[n][n];//生成n*n地图
        for (int i = 0; i < n; i++) {//初始化地图信息为空(-1代表是空,这个方格中没有图案)
            for (int j = 0; j < n; j++) {
                map[i][j] = -1;
            }
        }
    }

    //获取刚初始化好的地图
    public int[][] getMap() {

        ArrayList<Integer> list = new ArrayList<>();//将图片编号（0-15）添加到list

        for (int i = 0; i < n * n / types; i++) {
            for (int j = 0; j < types; j++) {
                list.add(j);
            }
        }

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                int index = (int) (Math.random() * list.size());//从list中随机取一个图片ID，并将其添加到数组中，再从list中删除掉它
                map[i][j] = list.get(index);
                list.remove(index);
            }
        }

        return map;//返回一个图片随机生成的地图数组
    }

    //获取重置后的地图
    public int[][] getResetMap(int[][] map) {

        ArrayList<Integer> list = new ArrayList<>();//list用来存储原先的地图信息
        ArrayList<Integer> list2 = new ArrayList<>();

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (map[i][j] != -1)//如果(x,y)处有图片，那么将该图片id添加到list
                    list.add(map[i][j]);
            }
        }

        while (!list.isEmpty()) {//打乱它
            int index = (int) (Math.random() * list.size());//从list中随机取一个图片ID，并将其添加到list2中，再从list中删除掉它
            list2.add(list.get(index));
            list.remove(index);
        }

        for (int i = 0; i < n; i++) {//输出打乱后的地图
            for (int j = 0; j < n; j++) {
                if (map[i][j] != -1) {
                    map[i][j] = list2.get(0);
                    list2.remove(0);
                }
            }
        }

        return map;
    }

    public int getCount() {
        return types;
    }

    public void setCount(int count) {
        this.types = count;
    }

    public int getN() {
        return n;
    }

    public void setN(int n) {
        this.n = n;
    }
}
