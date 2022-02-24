package LianLianKan;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.*;

public class Game extends JPanel implements ActionListener, MouseListener {

    private Image[] pics;//图片数组
    private Map mapUtil;//一个Map类的实例
    private Point z1, z2;//存储连线拐角点的信息
    private int[][] map;//地图
    private int clickId, clickX, clickY;//记录第一次选中图片的id,以及数组下标
    private int linkMethod;//两个图案的连接方式
    private int count;//已消去图案的个数
    private boolean isClick = false;//标记是否第一次选中图片
    private final int n;//行列数（n*n个图案）
    private final int types;//图案种数
    private final int X;//UI左上角开始位置的横坐标
    private final int Y;//UI左上角开始位置的纵坐标
    private static final int LINK_BY_HORIZONTAL = 1, LINK_BY_VERTICAL = 2, LINK_BY_ONECORNER = 3, LINK_BY_TWOCORNER = 4;
    private static final int BLANK_STATE = -1;

    public Game(int types, int n) {
        this.n = n;
        this.types = types;
        this.count = 0;
        this.X = 25;
        this.Y = 25;
        mapUtil = new Map(this.types, this.n);//获取初始时，图片种类为types,行列数为n的地图信息
        map = mapUtil.getMap();
        Play.textField.setText(n * n - count + "");
        this.setSize(610, 610);
        this.setVisible(true);
        this.setFocusable(true);
        this.addMouseListener(this);
        getPics();
        repaint();
    }

    //初始化图片数组
    public void getPics() {
        pics = new Image[types];
        for (int i = 0; i < types; i++) {
            pics[i] = Toolkit.getDefaultToolkit().getImage(".\\GameImages\\" + (i + 1) + ".jpg");
        }
    }

    //初始化连连看图案界面
    @Override
    public void paint(Graphics g) {
        g.clearRect(0, 0, 610, 610);
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (map[i][j] != BLANK_STATE) {
                    g.drawImage(pics[map[i][j]], X + j * 40, Y + i * 40, 40, 40, this);
                } else {
                    g.clearRect(X + j * 40, Y + i * 40, 40, 40);
                }
            }
        }
    }

    //判断是否可以水平相连
    private boolean horizontalLink(int clickX1, int clickY1, int clickX2, int clickY2) {

        if (clickY1 > clickY2) {//保证两个点y1<y2
            int temp1 = clickX1;
            int temp2 = clickY1;
            clickX1 = clickX2;
            clickY1 = clickY2;
            clickX2 = temp1;
            clickY2 = temp2;
        }

        if (clickX1 == clickX2) {//如果两个选中图片的所在行数相同
            for (int i = clickY1 + 1; i < clickY2; i++) {
                if (map[clickX1][i] != BLANK_STATE) {//如果两图片中间还有其他图片，说明不能直接水平相连
                    return false;
                }
            }
            linkMethod = LINK_BY_HORIZONTAL;
            return true;
        }

        return false;
    }

    //判断是否可以垂直连接
    private boolean verticalLink(int clickX1, int clickY1, int clickX2, int clickY2) {

        if (clickX1 > clickX2) {//保证x1<x2
            int temp1 = clickX1;
            int temp2 = clickY1;
            clickX1 = clickX2;
            clickY1 = clickY2;
            clickX2 = temp1;
            clickY2 = temp2;
        }

        if (clickY1 == clickY2) {//如果两个选中图片的所在列数相同
            for (int i = clickX1 + 1; i < clickX2; i++) {
                if (map[i][clickY1] != BLANK_STATE) {//如果两图片中间还有其他图片，说明不能直接垂直相连
                    return false;
                }
            }
            linkMethod = LINK_BY_VERTICAL;
            return true;
        }

        return false;
    }

    //判断是否可以通过一个拐点相连
    private boolean oneCornerLink(int clickX1, int clickY1, int clickX2, int clickY2) {

        if (clickY1 > clickY2) {//保证(x1,y1)是矩形的左上角或者左下角
            int temp1 = clickX1;
            int temp2 = clickY1;
            clickX1 = clickX2;
            clickY1 = clickY2;
            clickX2 = temp1;
            clickY2 = temp2;
        }

        if (clickX1 < clickX2) {//如果(x1,y1)位于矩形左上角
            //判断右上角是否为空并且可以直接与(x1,y1)和(x2,y2)相连接,(clickX1, clickY2)是右上角拐点下标
            if (map[clickX1][clickY2] == BLANK_STATE && horizontalLink(clickX1, clickY1, clickX1, clickY2) && verticalLink(clickX2, clickY2, clickX1, clickY2)) {
                linkMethod = LINK_BY_ONECORNER;
                z1 = new Point(clickX1, clickY2);
                return true;
            }
            //判断左下角是否为空并且可以直接与(x1,y1)和(x2,y2)相连接,(clickX2, clickY1)是左下角拐点下标
            if (map[clickX2][clickY1] == BLANK_STATE && horizontalLink(clickX2, clickY2, clickX2, clickY1) && verticalLink(clickX1, clickY1, clickX2, clickY1)) {
                linkMethod = LINK_BY_ONECORNER;
                z1 = new Point(clickX2, clickY1);
                return true;
            }
        } else {//如果(x1,y1)位于矩形左下角
            //判断左上角是否为空并且可以直接与(x1,y1)和(x2,y2)相连接,(clickX2, clickY1)是左上角拐点下标
            if (map[clickX2][clickY1] == BLANK_STATE && horizontalLink(clickX2, clickY2, clickX2, clickY1) && verticalLink(clickX1, clickY1, clickX2, clickY1)) {
                linkMethod = LINK_BY_ONECORNER;
                z1 = new Point(clickX2, clickY1);
                return true;
            }
            //判断右下角是否为空并且可以直接与(x1,y1)和(x2,y2)相连接,(clickX1, clickY2)是右下角拐点下标
            if (map[clickX1][clickY2] == BLANK_STATE && horizontalLink(clickX1, clickY1, clickX1, clickY2) && verticalLink(clickX2, clickY2, clickX1, clickY2)) {
                linkMethod = LINK_BY_ONECORNER;
                z1 = new Point(clickX1, clickY2);
                return true;
            }
        }

        return false;
    }

    //判断是否可以通过两个拐点相连
    private boolean twoCornerLink(int clickX1, int clickY1, int clickX2, int clickY2) {

        //向上查找
        for (int i = clickX1 - 1; i >= -1; i--) {
            //两个拐点在选中图案的上侧，并且两个拐点在地图区域之外
            if (i == -1 && throughVerticalLink(clickX2, clickY2, true)) {
                z1 = new Point(-1, clickY1);
                z2 = new Point(-1, clickY2);
                linkMethod = LINK_BY_TWOCORNER;
                return true;
            }

            if (i >= 0 && map[i][clickY1] == BLANK_STATE) {
                if (oneCornerLink(i, clickY1, clickX2, clickY2)) {
                    linkMethod = LINK_BY_TWOCORNER;
                    z1 = new Point(i, clickY1);
                    z2 = new Point(i, clickY2);
                    return true;
                }
            } else {
                break;
            }
        }

        //向下查找
        for (int i = clickX1 + 1; i <= n; i++) {
            //两个拐点在选中图案的下侧，并且两个拐点在地图区域之外
            if (i == n && throughVerticalLink(clickX2, clickY2, false)) {
                z1 = new Point(n, clickY1);
                z2 = new Point(n, clickY2);
                linkMethod = LINK_BY_TWOCORNER;
                return true;
            }

            if (i < n && map[i][clickY1] == BLANK_STATE) {
                if (oneCornerLink(i, clickY1, clickX2, clickY2)) {
                    linkMethod = LINK_BY_TWOCORNER;
                    z1 = new Point(i, clickY1);
                    z2 = new Point(i, clickY2);
                    return true;
                }
            } else {
                break;
            }
        }

        //向左查找
        for (int i = clickY1 - 1; i >= -1; i--) {
            //两个拐点在选中图案的左侧，并且两个拐点在地图区域之外
            if (i == -1 && throughHorizontalLink(clickX2, clickY2, true)) {
                linkMethod = LINK_BY_TWOCORNER;
                z1 = new Point(clickX1, -1);
                z2 = new Point(clickX2, -1);
                return true;
            }

            if (i >= 0 && map[clickX1][i] == BLANK_STATE) {
                if (oneCornerLink(clickX1, i, clickX2, clickY2)) {
                    linkMethod = LINK_BY_TWOCORNER;
                    z1 = new Point(clickX1, i);
                    z2 = new Point(clickX2, i);
                    return true;
                }
            } else {
                break;
            }
        }

        //向右查找
        for (int i = clickY1 + 1; i <= n; i++) {
            //两个拐点在选中图案的右侧，并且两个拐点在地图区域之外
            if (i == n && throughHorizontalLink(clickX2, clickY2, false)) {
                z1 = new Point(clickX1, n);
                z2 = new Point(clickX2, n);
                linkMethod = LINK_BY_TWOCORNER;
                return true;
            }

            if (i < n && map[clickX1][i] == BLANK_STATE) {
                if (oneCornerLink(clickX1, i, clickX2, clickY2)) {
                    linkMethod = LINK_BY_TWOCORNER;
                    z1 = new Point(clickX1, i);
                    z2 = new Point(clickX2, i);
                    return true;
                }
            } else {
                break;
            }
        }

        return false;
    }

    //根据flag,判断(x1,y1)左右两侧中的一侧是否还有其他图片，如果没有，可以相连
    private boolean throughHorizontalLink(int clickX, int clickY, boolean flag) {

        if (flag) {//向左查找
            for (int i = clickY - 1; i >= 0; i--) {
                if (map[clickX][i] != BLANK_STATE) {
                    return false;
                }
            }
        } else {//向右查找
            for (int i = clickY + 1; i < n; i++) {
                if (map[clickX][i] != BLANK_STATE) {
                    return false;
                }
            }
        }

        return true;
    }

    //根据flag,判断(x1,y1)上下两侧中的一侧是否还有其他图片，如果没有，可以相连
    private boolean throughVerticalLink(int clickX, int clickY, boolean flag) {

        if (flag) {//向上查找
            for (int i = clickX - 1; i >= 0; i--) {
                if (map[i][clickY] != BLANK_STATE) {
                    return false;
                }
            }
        } else {//向下查找
            for (int i = clickX + 1; i < n; i++) {
                if (map[i][clickY] != BLANK_STATE) {
                    return false;
                }
            }
        }

        return true;
    }

    //画选中框
    public void drawSelectedBlock(int x, int y, Graphics g) {
        Graphics2D g2 = (Graphics2D) g;//转换生成Graphics2D对象
        g2.setStroke(new BasicStroke(2, BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND));
        g2.setColor(Color.ORANGE);
        g.drawRect(x + 2, y + 2, 37, 37);
    }

    //画线并消除，此处的x1,y1,x2,y2是两个图案的下标
    private void drawLink_And_deleteBlocks(int x1, int y1, int x2, int y2) {

        Graphics2D g2 = (Graphics2D) this.getGraphics();//转换生成Graphics2D对象
        g2.setStroke(new BasicStroke(2, BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND));
        g2.setColor(Color.ORANGE);
        Point p1 = new Point(y1 * 40 + X + 20, x1 * 40 + Y + 20);
        Point p2 = new Point(y2 * 40 + X + 20, x2 * 40 + Y + 20);

        if (linkMethod == LINK_BY_HORIZONTAL || linkMethod == LINK_BY_VERTICAL) {
            g2.drawLine(p1.x, p1.y, p2.x, p2.y);
            System.out.println("无拐点连线消除");
        } else if (linkMethod == LINK_BY_ONECORNER) {
            Point pp1 = new Point(z1.y * 40 + X + 20, z1.x * 40 + Y + 20);//将拐点转换成像素坐标
            g2.drawLine(p1.x, p1.y, pp1.x, pp1.y);
            g2.drawLine(p2.x, p2.y, pp1.x, pp1.y);
            System.out.println("单拐点连线消除");
        } else {
            Point pp1 = new Point(z1.y * 40 + X + 20, z1.x * 40 + Y + 20);
            Point pp2 = new Point(z2.y * 40 + X + 20, z2.x * 40 + Y + 20);
            Point temp;
            if (p1.x != pp1.x && p1.y != pp1.y) {//保证(x1,y1)与拐点z1在同一列或者同一行
                temp = pp1;
                pp1 = pp2;
                pp2 = temp;
            }
            g2.drawLine(p1.x, p1.y, pp1.x, pp1.y);
            g2.drawLine(pp1.x, pp1.y, pp2.x, pp2.y);
            g2.drawLine(p2.x, p2.y, pp2.x, pp2.y);
            System.out.println("双拐点连线消除");
        }

        try {
            Thread.sleep(120);//延时120ms
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        map[x1][y1] = BLANK_STATE;//这对图案消去后，该格子设置为空
        map[x2][y2] = BLANK_STATE;
        count += 2;//消去的图案数目+2
        Play.textField.setText(n * n - count + "");//显示剩余图案个数
        Play.currentProgress += 2;//进度加2
        Play.progressBar.setValue(Play.currentProgress);//更新进度条进度
        repaint();
        isWin();//判断游戏是否结束
    }

    public void clearSelectBlock(int i, int j, Graphics g) {
        g.clearRect(j * 40 + X + 1, i * 40 + Y + 1, 38, 38);
        g.drawImage(pics[map[i][j]], X + j * 40, Y + i * 40, 40, 40, this);
        System.out.println("清空该坐标图标的选定框：" + i + "," + j);
    }

    //提示
    public void find2Block() {

        Graphics2D g2 = (Graphics2D) this.getGraphics();//转换生成Graphics2D对象
        if (isClick) {//如果之前玩家选中了一个方块，清空该选中框
            clearSelectBlock(clickX, clickY, g2);
            isClick = false;
        }

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (map[i][j] == BLANK_STATE) {
                    continue;
                }

                for (int p = i; p < n; p++) {
                    for (int q = 0; q < n; q++) {
                        if (map[p][q] != map[i][j] || (p == i && q == j)) {//如果图案不相等
                            continue;
                        }

                        if (verticalLink(p, q, i, j) || horizontalLink(p, q, i, j)
                                || oneCornerLink(p, q, i, j) || twoCornerLink(p, q, i, j)) {

                            drawSelectedBlock(j * 40 + X, i * 40 + Y, g2);
                            drawSelectedBlock(q * 40 + X, p * 40 + Y, g2);

                            g2.setStroke(new BasicStroke(2, BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND));
                            g2.setColor(Color.ORANGE);
                            Point p1 = new Point(q * 40 + X + 20, p * 40 + Y + 20);
                            Point p2 = new Point(j * 40 + X + 20, i * 40 + Y + 20);

                            if (linkMethod == LINK_BY_HORIZONTAL || linkMethod == LINK_BY_VERTICAL) {
                                g2.drawLine(p1.x, p1.y, p2.x, p2.y);
                            } else if (linkMethod == LINK_BY_ONECORNER) {
                                Point pp1 = new Point(z1.y * 40 + X + 20, z1.x * 40 + Y + 20);//将拐点转换成像素坐标
                                g2.drawLine(p1.x, p1.y, pp1.x, pp1.y);
                                g2.drawLine(p2.x, p2.y, pp1.x, pp1.y);
                            } else {
                                Point pp1 = new Point(z1.y * 40 + X + 20, z1.x * 40 + Y + 20);
                                Point pp2 = new Point(z2.y * 40 + X + 20, z2.x * 40 + Y + 20);
                                Point temp;
                                if (p1.x != pp1.x && p1.y != pp1.y) {//保证(x1,y1)与拐点z1在同一列或者同一行
                                    temp = pp1;
                                    pp1 = pp2;
                                    pp2 = temp;
                                }
                                g2.drawLine(p1.x, p1.y, pp1.x, pp1.y);
                                g2.drawLine(pp1.x, pp1.y, pp2.x, pp2.y);
                                g2.drawLine(p2.x, p2.y, pp2.x, pp2.y);
                            }

                            try {
                                Thread.sleep(450);//延时450ms
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            repaint();
                            return;
                        }

                    }
                }
            }
        }
    }

    //重置
    public void resetMap() {
        map = mapUtil.getResetMap(map);
        repaint();
    }

    public void isWin() {
        if (count == n * n) {
            String title = "过关";
            String msg = "恭喜你通关成功，再来一局？";
            int type = JOptionPane.YES_NO_OPTION;
            int choice;
            choice = JOptionPane.showConfirmDialog(null, msg, title, type);
            if (choice == 1) {
                System.exit(0);
            } else if (choice == 0) {
                startNewGame();
            }
        }
    }

    //初始化，开始新的一局游戏
    public void startNewGame() {
        count = 0;
        mapUtil = new Map(types, n);
        map = mapUtil.getMap();
        isClick = false;
        clickId = -1;
        clickX = -1;
        clickY = -1;
        linkMethod = -1;
        Play.textField.setText(n * n - count + "");
        Play.currentProgress = 0;//初始化当前完成程度
        Play.progressBar.setValue(Play.currentProgress);//初始化进度条
        repaint();
    }

    @Override
    public void mousePressed(MouseEvent e) {

        Graphics g = this.getGraphics();//得到Graphics对象g
        int x = e.getX() - X;//点击位置x-偏移量x
        int y = e.getY() - Y;//点击位置y-偏移量y
        int i = y / 40;//根据像素坐标转换成数组行坐标
        int j = x / 40;//根据像素坐标转换成数组行坐标

        if (x < 0 || y < 0 || x > 610 || y > 610)//超出地图范围
            return;

        if (isClick) {//第二次点击
            if (map[i][j] != BLANK_STATE) {
                if (map[i][j] == clickId) {//点击的是相同图片Id
                    //点击的是同一个图片
                    if (i == clickX && j == clickY)
                        return;
                    //可以连通，画线连接，然后消去选中图片并重置第一次选中标识
                    if (verticalLink(clickX, clickY, i, j) || horizontalLink(clickX, clickY, i, j) || oneCornerLink(clickX, clickY, i, j) || twoCornerLink(clickX, clickY, i, j)) {
                        drawSelectedBlock(j * 40 + X, i * 40 + Y, g);
                        drawLink_And_deleteBlocks(clickX, clickY, i, j);//画线连接
                        isClick = false;
                    } else {
                        clickId = map[i][j];//重新选中图片并画框
                        clearSelectBlock(clickX, clickY, g);
                        clickX = i;
                        clickY = j;
                        drawSelectedBlock(j * 40 + X, i * 40 + Y, g);
                    }
                } else {
                    clickId = map[i][j];//重新选中图片并画框
                    clearSelectBlock(clickX, clickY, g);
                    clickX = i;
                    clickY = j;
                    drawSelectedBlock(j * 40 + X, i * 40 + Y, g);
                }

            }
        } else {//第一次点击
            if (map[i][j] != BLANK_STATE) {
                //选中图片并画框
                clickId = map[i][j];
                isClick = true;
                clickX = i;
                clickY = j;
                drawSelectedBlock(j * 40 + X, i * 40 + Y, g);
            }
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        // TODO Auto-generated method stub

    }

    @Override
    public void mouseReleased(MouseEvent e) {
        // TODO Auto-generated method stub
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        // TODO Auto-generated method stub

    }

    @Override
    public void mouseExited(MouseEvent e) {
        // TODO Auto-generated method stub

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // TODO Auto-generated method stub

    }
}
