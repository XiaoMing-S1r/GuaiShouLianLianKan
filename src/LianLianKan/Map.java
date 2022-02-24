package LianLianKan;

import java.util.ArrayList;

public class Map {

    private final int[][] map;//��ͼ
    private int types;//һ����types�ֲ�ͬ��ͼ��
    private int n;//��ͼ��n��n��

    //���캯��
    public Map(int types, int n) {
        this.types = types;
        this.n = n;
        map = new int[n][n];//����n*n��ͼ
        for (int i = 0; i < n; i++) {//��ʼ����ͼ��ϢΪ��(-1�����ǿ�,���������û��ͼ��)
            for (int j = 0; j < n; j++) {
                map[i][j] = -1;
            }
        }
    }

    //��ȡ�ճ�ʼ���õĵ�ͼ
    public int[][] getMap() {

        ArrayList<Integer> list = new ArrayList<>();//��ͼƬ��ţ�0-15����ӵ�list

        for (int i = 0; i < n * n / types; i++) {
            for (int j = 0; j < types; j++) {
                list.add(j);
            }
        }

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                int index = (int) (Math.random() * list.size());//��list�����ȡһ��ͼƬID����������ӵ������У��ٴ�list��ɾ������
                map[i][j] = list.get(index);
                list.remove(index);
            }
        }

        return map;//����һ��ͼƬ������ɵĵ�ͼ����
    }

    //��ȡ���ú�ĵ�ͼ
    public int[][] getResetMap(int[][] map) {

        ArrayList<Integer> list = new ArrayList<>();//list�����洢ԭ�ȵĵ�ͼ��Ϣ
        ArrayList<Integer> list2 = new ArrayList<>();

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (map[i][j] != -1)//���(x,y)����ͼƬ����ô����ͼƬid��ӵ�list
                    list.add(map[i][j]);
            }
        }

        while (!list.isEmpty()) {//������
            int index = (int) (Math.random() * list.size());//��list�����ȡһ��ͼƬID����������ӵ�list2�У��ٴ�list��ɾ������
            list2.add(list.get(index));
            list.remove(index);
        }

        for (int i = 0; i < n; i++) {//������Һ�ĵ�ͼ
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
