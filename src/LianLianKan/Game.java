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

    private Image[] pics;//ͼƬ����
    private Map mapUtil;//һ��Map���ʵ��
    private Point z1, z2;//�洢���߹սǵ����Ϣ
    private int[][] map;//��ͼ
    private int clickId, clickX, clickY;//��¼��һ��ѡ��ͼƬ��id,�Լ������±�
    private int linkMethod;//����ͼ�������ӷ�ʽ
    private int count;//����ȥͼ���ĸ���
    private boolean isClick = false;//����Ƿ��һ��ѡ��ͼƬ
    private final int n;//��������n*n��ͼ����
    private final int types;//ͼ������
    private final int X;//UI���Ͻǿ�ʼλ�õĺ�����
    private final int Y;//UI���Ͻǿ�ʼλ�õ�������
    private static final int LINK_BY_HORIZONTAL = 1, LINK_BY_VERTICAL = 2, LINK_BY_ONECORNER = 3, LINK_BY_TWOCORNER = 4;
    private static final int BLANK_STATE = -1;

    public Game(int types, int n) {
        this.n = n;
        this.types = types;
        this.count = 0;
        this.X = 25;
        this.Y = 25;
        mapUtil = new Map(this.types, this.n);//��ȡ��ʼʱ��ͼƬ����Ϊtypes,������Ϊn�ĵ�ͼ��Ϣ
        map = mapUtil.getMap();
        Play.textField.setText(n * n - count + "");
        this.setSize(610, 610);
        this.setVisible(true);
        this.setFocusable(true);
        this.addMouseListener(this);
        getPics();
        repaint();
    }

    //��ʼ��ͼƬ����
    public void getPics() {
        pics = new Image[types];
        for (int i = 0; i < types; i++) {
            pics[i] = Toolkit.getDefaultToolkit().getImage(".\\GameImages\\" + (i + 1) + ".jpg");
        }
    }

    //��ʼ��������ͼ������
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

    //�ж��Ƿ����ˮƽ����
    private boolean horizontalLink(int clickX1, int clickY1, int clickX2, int clickY2) {

        if (clickY1 > clickY2) {//��֤������y1<y2
            int temp1 = clickX1;
            int temp2 = clickY1;
            clickX1 = clickX2;
            clickY1 = clickY2;
            clickX2 = temp1;
            clickY2 = temp2;
        }

        if (clickX1 == clickX2) {//�������ѡ��ͼƬ������������ͬ
            for (int i = clickY1 + 1; i < clickY2; i++) {
                if (map[clickX1][i] != BLANK_STATE) {//�����ͼƬ�м仹������ͼƬ��˵������ֱ��ˮƽ����
                    return false;
                }
            }
            linkMethod = LINK_BY_HORIZONTAL;
            return true;
        }

        return false;
    }

    //�ж��Ƿ���Դ�ֱ����
    private boolean verticalLink(int clickX1, int clickY1, int clickX2, int clickY2) {

        if (clickX1 > clickX2) {//��֤x1<x2
            int temp1 = clickX1;
            int temp2 = clickY1;
            clickX1 = clickX2;
            clickY1 = clickY2;
            clickX2 = temp1;
            clickY2 = temp2;
        }

        if (clickY1 == clickY2) {//�������ѡ��ͼƬ������������ͬ
            for (int i = clickX1 + 1; i < clickX2; i++) {
                if (map[i][clickY1] != BLANK_STATE) {//�����ͼƬ�м仹������ͼƬ��˵������ֱ�Ӵ�ֱ����
                    return false;
                }
            }
            linkMethod = LINK_BY_VERTICAL;
            return true;
        }

        return false;
    }

    //�ж��Ƿ����ͨ��һ���յ�����
    private boolean oneCornerLink(int clickX1, int clickY1, int clickX2, int clickY2) {

        if (clickY1 > clickY2) {//��֤(x1,y1)�Ǿ��ε����Ͻǻ������½�
            int temp1 = clickX1;
            int temp2 = clickY1;
            clickX1 = clickX2;
            clickY1 = clickY2;
            clickX2 = temp1;
            clickY2 = temp2;
        }

        if (clickX1 < clickX2) {//���(x1,y1)λ�ھ������Ͻ�
            //�ж����Ͻ��Ƿ�Ϊ�ղ��ҿ���ֱ����(x1,y1)��(x2,y2)������,(clickX1, clickY2)�����Ͻǹյ��±�
            if (map[clickX1][clickY2] == BLANK_STATE && horizontalLink(clickX1, clickY1, clickX1, clickY2) && verticalLink(clickX2, clickY2, clickX1, clickY2)) {
                linkMethod = LINK_BY_ONECORNER;
                z1 = new Point(clickX1, clickY2);
                return true;
            }
            //�ж����½��Ƿ�Ϊ�ղ��ҿ���ֱ����(x1,y1)��(x2,y2)������,(clickX2, clickY1)�����½ǹյ��±�
            if (map[clickX2][clickY1] == BLANK_STATE && horizontalLink(clickX2, clickY2, clickX2, clickY1) && verticalLink(clickX1, clickY1, clickX2, clickY1)) {
                linkMethod = LINK_BY_ONECORNER;
                z1 = new Point(clickX2, clickY1);
                return true;
            }
        } else {//���(x1,y1)λ�ھ������½�
            //�ж����Ͻ��Ƿ�Ϊ�ղ��ҿ���ֱ����(x1,y1)��(x2,y2)������,(clickX2, clickY1)�����Ͻǹյ��±�
            if (map[clickX2][clickY1] == BLANK_STATE && horizontalLink(clickX2, clickY2, clickX2, clickY1) && verticalLink(clickX1, clickY1, clickX2, clickY1)) {
                linkMethod = LINK_BY_ONECORNER;
                z1 = new Point(clickX2, clickY1);
                return true;
            }
            //�ж����½��Ƿ�Ϊ�ղ��ҿ���ֱ����(x1,y1)��(x2,y2)������,(clickX1, clickY2)�����½ǹյ��±�
            if (map[clickX1][clickY2] == BLANK_STATE && horizontalLink(clickX1, clickY1, clickX1, clickY2) && verticalLink(clickX2, clickY2, clickX1, clickY2)) {
                linkMethod = LINK_BY_ONECORNER;
                z1 = new Point(clickX1, clickY2);
                return true;
            }
        }

        return false;
    }

    //�ж��Ƿ����ͨ�������յ�����
    private boolean twoCornerLink(int clickX1, int clickY1, int clickX2, int clickY2) {

        //���ϲ���
        for (int i = clickX1 - 1; i >= -1; i--) {
            //�����յ���ѡ��ͼ�����ϲ࣬���������յ��ڵ�ͼ����֮��
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

        //���²���
        for (int i = clickX1 + 1; i <= n; i++) {
            //�����յ���ѡ��ͼ�����²࣬���������յ��ڵ�ͼ����֮��
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

        //�������
        for (int i = clickY1 - 1; i >= -1; i--) {
            //�����յ���ѡ��ͼ������࣬���������յ��ڵ�ͼ����֮��
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

        //���Ҳ���
        for (int i = clickY1 + 1; i <= n; i++) {
            //�����յ���ѡ��ͼ�����Ҳ࣬���������յ��ڵ�ͼ����֮��
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

    //����flag,�ж�(x1,y1)���������е�һ���Ƿ�������ͼƬ�����û�У���������
    private boolean throughHorizontalLink(int clickX, int clickY, boolean flag) {

        if (flag) {//�������
            for (int i = clickY - 1; i >= 0; i--) {
                if (map[clickX][i] != BLANK_STATE) {
                    return false;
                }
            }
        } else {//���Ҳ���
            for (int i = clickY + 1; i < n; i++) {
                if (map[clickX][i] != BLANK_STATE) {
                    return false;
                }
            }
        }

        return true;
    }

    //����flag,�ж�(x1,y1)���������е�һ���Ƿ�������ͼƬ�����û�У���������
    private boolean throughVerticalLink(int clickX, int clickY, boolean flag) {

        if (flag) {//���ϲ���
            for (int i = clickX - 1; i >= 0; i--) {
                if (map[i][clickY] != BLANK_STATE) {
                    return false;
                }
            }
        } else {//���²���
            for (int i = clickX + 1; i < n; i++) {
                if (map[i][clickY] != BLANK_STATE) {
                    return false;
                }
            }
        }

        return true;
    }

    //��ѡ�п�
    public void drawSelectedBlock(int x, int y, Graphics g) {
        Graphics2D g2 = (Graphics2D) g;//ת������Graphics2D����
        g2.setStroke(new BasicStroke(2, BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND));
        g2.setColor(Color.ORANGE);
        g.drawRect(x + 2, y + 2, 37, 37);
    }

    //���߲��������˴���x1,y1,x2,y2������ͼ�����±�
    private void drawLink_And_deleteBlocks(int x1, int y1, int x2, int y2) {

        Graphics2D g2 = (Graphics2D) this.getGraphics();//ת������Graphics2D����
        g2.setStroke(new BasicStroke(2, BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND));
        g2.setColor(Color.ORANGE);
        Point p1 = new Point(y1 * 40 + X + 20, x1 * 40 + Y + 20);
        Point p2 = new Point(y2 * 40 + X + 20, x2 * 40 + Y + 20);

        if (linkMethod == LINK_BY_HORIZONTAL || linkMethod == LINK_BY_VERTICAL) {
            g2.drawLine(p1.x, p1.y, p2.x, p2.y);
            System.out.println("�޹յ���������");
        } else if (linkMethod == LINK_BY_ONECORNER) {
            Point pp1 = new Point(z1.y * 40 + X + 20, z1.x * 40 + Y + 20);//���յ�ת������������
            g2.drawLine(p1.x, p1.y, pp1.x, pp1.y);
            g2.drawLine(p2.x, p2.y, pp1.x, pp1.y);
            System.out.println("���յ���������");
        } else {
            Point pp1 = new Point(z1.y * 40 + X + 20, z1.x * 40 + Y + 20);
            Point pp2 = new Point(z2.y * 40 + X + 20, z2.x * 40 + Y + 20);
            Point temp;
            if (p1.x != pp1.x && p1.y != pp1.y) {//��֤(x1,y1)��յ�z1��ͬһ�л���ͬһ��
                temp = pp1;
                pp1 = pp2;
                pp2 = temp;
            }
            g2.drawLine(p1.x, p1.y, pp1.x, pp1.y);
            g2.drawLine(pp1.x, pp1.y, pp2.x, pp2.y);
            g2.drawLine(p2.x, p2.y, pp2.x, pp2.y);
            System.out.println("˫�յ���������");
        }

        try {
            Thread.sleep(120);//��ʱ120ms
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        map[x1][y1] = BLANK_STATE;//���ͼ����ȥ�󣬸ø�������Ϊ��
        map[x2][y2] = BLANK_STATE;
        count += 2;//��ȥ��ͼ����Ŀ+2
        Play.textField.setText(n * n - count + "");//��ʾʣ��ͼ������
        Play.currentProgress += 2;//���ȼ�2
        Play.progressBar.setValue(Play.currentProgress);//���½���������
        repaint();
        isWin();//�ж���Ϸ�Ƿ����
    }

    public void clearSelectBlock(int i, int j, Graphics g) {
        g.clearRect(j * 40 + X + 1, i * 40 + Y + 1, 38, 38);
        g.drawImage(pics[map[i][j]], X + j * 40, Y + i * 40, 40, 40, this);
        System.out.println("��ո�����ͼ���ѡ����" + i + "," + j);
    }

    //��ʾ
    public void find2Block() {

        Graphics2D g2 = (Graphics2D) this.getGraphics();//ת������Graphics2D����
        if (isClick) {//���֮ǰ���ѡ����һ�����飬��ո�ѡ�п�
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
                        if (map[p][q] != map[i][j] || (p == i && q == j)) {//���ͼ�������
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
                                Point pp1 = new Point(z1.y * 40 + X + 20, z1.x * 40 + Y + 20);//���յ�ת������������
                                g2.drawLine(p1.x, p1.y, pp1.x, pp1.y);
                                g2.drawLine(p2.x, p2.y, pp1.x, pp1.y);
                            } else {
                                Point pp1 = new Point(z1.y * 40 + X + 20, z1.x * 40 + Y + 20);
                                Point pp2 = new Point(z2.y * 40 + X + 20, z2.x * 40 + Y + 20);
                                Point temp;
                                if (p1.x != pp1.x && p1.y != pp1.y) {//��֤(x1,y1)��յ�z1��ͬһ�л���ͬһ��
                                    temp = pp1;
                                    pp1 = pp2;
                                    pp2 = temp;
                                }
                                g2.drawLine(p1.x, p1.y, pp1.x, pp1.y);
                                g2.drawLine(pp1.x, pp1.y, pp2.x, pp2.y);
                                g2.drawLine(p2.x, p2.y, pp2.x, pp2.y);
                            }

                            try {
                                Thread.sleep(450);//��ʱ450ms
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

    //����
    public void resetMap() {
        map = mapUtil.getResetMap(map);
        repaint();
    }

    public void isWin() {
        if (count == n * n) {
            String title = "����";
            String msg = "��ϲ��ͨ�سɹ�������һ�֣�";
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

    //��ʼ������ʼ�µ�һ����Ϸ
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
        Play.currentProgress = 0;//��ʼ����ǰ��ɳ̶�
        Play.progressBar.setValue(Play.currentProgress);//��ʼ��������
        repaint();
    }

    @Override
    public void mousePressed(MouseEvent e) {

        Graphics g = this.getGraphics();//�õ�Graphics����g
        int x = e.getX() - X;//���λ��x-ƫ����x
        int y = e.getY() - Y;//���λ��y-ƫ����y
        int i = y / 40;//������������ת��������������
        int j = x / 40;//������������ת��������������

        if (x < 0 || y < 0 || x > 610 || y > 610)//������ͼ��Χ
            return;

        if (isClick) {//�ڶ��ε��
            if (map[i][j] != BLANK_STATE) {
                if (map[i][j] == clickId) {//���������ͬͼƬId
                    //�������ͬһ��ͼƬ
                    if (i == clickX && j == clickY)
                        return;
                    //������ͨ���������ӣ�Ȼ����ȥѡ��ͼƬ�����õ�һ��ѡ�б�ʶ
                    if (verticalLink(clickX, clickY, i, j) || horizontalLink(clickX, clickY, i, j) || oneCornerLink(clickX, clickY, i, j) || twoCornerLink(clickX, clickY, i, j)) {
                        drawSelectedBlock(j * 40 + X, i * 40 + Y, g);
                        drawLink_And_deleteBlocks(clickX, clickY, i, j);//��������
                        isClick = false;
                    } else {
                        clickId = map[i][j];//����ѡ��ͼƬ������
                        clearSelectBlock(clickX, clickY, g);
                        clickX = i;
                        clickY = j;
                        drawSelectedBlock(j * 40 + X, i * 40 + Y, g);
                    }
                } else {
                    clickId = map[i][j];//����ѡ��ͼƬ������
                    clearSelectBlock(clickX, clickY, g);
                    clickX = i;
                    clickY = j;
                    drawSelectedBlock(j * 40 + X, i * 40 + Y, g);
                }

            }
        } else {//��һ�ε��
            if (map[i][j] != BLANK_STATE) {
                //ѡ��ͼƬ������
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
