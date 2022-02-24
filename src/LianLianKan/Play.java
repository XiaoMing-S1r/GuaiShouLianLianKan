package LianLianKan;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Play {

    Game game = new Game(14, 14);
    JFrame frame = new JFrame("怪兽连连看");
    JPanel panel = new JPanel(null);
    JButton button1 = new JButton("提示");
    JButton button2 = new JButton("重置");
    JButton button3 = new JButton("重来");
    JLabel label = new JLabel("剩余图案个数");
    JLabel image_label = new JLabel();
    JLabel developer_information_label1 = new JLabel("By: SZU 小明先森");
    JLabel developer_information_label2 = new JLabel("Email: william87668@outlook.com");
    static JTextField textField = new JTextField(4);
    static int currentProgress = 0;
    static JProgressBar progressBar = new JProgressBar(0, 0, 14 * 14);

    public Play() {
        Image logo = Toolkit.getDefaultToolkit().getImage(".\\GameImages\\logo.png");
        frame.setSize(860, 645);
        frame.setResizable(false);
        frame.setIconImage(logo);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        panel.setSize(860, 645);
        panel.setBackground(Color.WHITE);
        panel.add(game);

        ImageIcon ii = new ImageIcon(".\\GameImages\\logo.png");
        ii.setImage(ii.getImage().getScaledInstance(120, 120, Image.SCALE_DEFAULT));
        image_label.setIcon(ii);
        image_label.setLocation(662, 60);
        image_label.setSize(120, 120);
        panel.add(image_label);

        progressBar.setLocation(620, 210);
        progressBar.setSize(205, 18);
        progressBar.setForeground(Color.MAGENTA);
        progressBar.setStringPainted(true);
        panel.add(progressBar);

        label.setLocation(635, 230);
        label.setSize(150, 40);
        label.setFont(new Font("幼圆", Font.BOLD, 18));
        label.setForeground(Color.MAGENTA);
        panel.add(label);

        textField.setLocation(760, 238);
        textField.setSize(36, 25);
        textField.setEditable(false);
        textField.setFont(new Font("幼圆", Font.BOLD, 18));
        textField.setForeground(Color.MAGENTA);
        textField.setBackground(Color.WHITE);
        panel.add(textField);

        developer_information_label1.setLocation(678, 565);
        developer_information_label1.setSize(200, 12);
        developer_information_label1.setFont(new Font("幼圆", Font.PLAIN, 12));
        developer_information_label1.setForeground(Color.BLACK);
        panel.add(developer_information_label1);

        developer_information_label2.setLocation(635, 578);
        developer_information_label2.setSize(200, 12);
        developer_information_label2.setFont(new Font("幼圆", Font.PLAIN, 12));
        developer_information_label2.setForeground(Color.BLACK);
        panel.add(developer_information_label2);

        button1.setBounds(665, 300, 118, 30);
        button1.setFont(new Font("幼圆", Font.BOLD, 15));
        panel.add(button1);

        button2.setBounds(665, 400, 118, 30);
        button2.setFont(new Font("幼圆", Font.BOLD, 15));
        panel.add(button2);

        button3.setBounds(665, 500, 118, 30);
        button3.setFont(new Font("幼圆", Font.BOLD, 15));
        panel.add(button3);

        button1.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                game.find2Block();
            }
        });
        button2.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                game.resetMap();
            }
        });
        button3.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                game.startNewGame();
            }
        });

        frame.setContentPane(panel);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public static void main(String[] args) { new Play(); }

}
