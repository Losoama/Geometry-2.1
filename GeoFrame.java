import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Random;

//Класс главного окна приложения
public class GeoFrame extends JFrame {
    private static GeoFrame gFrame = new GeoFrame();

    private final int DEFAULT_POSITION_X; //координата X верхнего левого угла окна
    private final int DEFAULT_POSITION_Y; //координата Y верхнего левого угла окна
    private final int DEFAULT_WIDTH;      //ширина окна
    private final int DEFAULT_HEIGHT;     //высота окна

    //Компоненты
    private static JPanel gPanel1;

    private static JLabel jLabel1;
    private static JTextArea jText1;
    private static JPanel gPanel2;

    private static JButton jButton1;
    private static JButton jButton2;
    private static JButton jButton3;

    //Дополнительные компоненты
    private static BufferedImage image1;
    private static ImageIcon iIcon1;
    private static Graphics g;

    private static final String rules = "Правила:" +
            "\nЧтобы добавить точку, щелкните на \nсвободную область." +
            "\nНачало координат слева вверху." +
            "\nПри щелчке на точку, она удалится." +
            "\nТочки можно перемещать." +
            "\nВо время выполнения алгоритма запрещены" +
            "\nлюбые изменения.";

    //Коструктор окна
    private GeoFrame() {
        Toolkit t = Toolkit.getDefaultToolkit();
        DEFAULT_POSITION_X = t.getScreenSize().width / 4;
        DEFAULT_POSITION_Y = t.getScreenSize().height / 4;
        DEFAULT_WIDTH = t.getScreenSize().width / 2;
        DEFAULT_HEIGHT = t.getScreenSize().height / 2;

        JFrame.setDefaultLookAndFeelDecorated(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        setBounds(DEFAULT_POSITION_X, DEFAULT_POSITION_Y, DEFAULT_WIDTH, DEFAULT_HEIGHT);
        setIconImage(Toolkit.getDefaultToolkit().createImage("C:\\Users\\Игорь\\Desktop\\Документы\\X7l5SlpT1Uc.jpg"));
        setTitle("Геометрия 2.1. Задание 2");

        gPanel1 = new JPanel(new BorderLayout());
        jLabel1 = new JLabel();
        jLabel1.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        jText1 = new JTextArea(rules);
        jText1.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        jText1.setEditable(false);
        gPanel2 = new JPanel(new GridLayout(12, 1));
        gPanel1.add(jLabel1, BorderLayout.EAST);
        gPanel1.add(gPanel2, BorderLayout.WEST);
        gPanel1.add(jText1, BorderLayout.CENTER);

        jButton1 = new JButton();
        jButton1.setText("Clear");
        jButton3 = new JButton();
        jButton3.setText("Random");
        jButton2 = new JButton();
        jButton2.setEnabled(false);
        jButton2.setText("Go!");
        gPanel2.add(jButton1);
        gPanel2.add(jButton2);
        gPanel2.add(jButton3);

        setContentPane(gPanel1);
        setVisible(true);

        image1 = new BufferedImage(getWidth() / 2, jLabel1.getHeight(), BufferedImage.TYPE_INT_RGB);
        image1.getGraphics().fillRect(0, 0, 480, 640);
        image1.getGraphics().setColor(Color.BLACK);

        iIcon1 = new ImageIcon();
        iIcon1.setImage(image1);
        jLabel1.setIcon(iIcon1);
        g = image1.getGraphics();
    }

    public static GeoFrame getGeoFrame() {
        return gFrame;
    }

    //Глобальные переменные
    static ArrayList<Point> points = new ArrayList<Point>();
    static ArrayList<Point> bestPoints = new ArrayList<Point>();
    //Нажата ли кнопка
    static boolean isClicked = false;
    /*
     * Если isDragged > 0, значит, было нажатие на точку
     * -1 и 0 - отсутствие нажатия
     * -2 - до события было нажатие на точку
     */
    static int isDragged = -1;
    //Координаты точки нажатия
    static int X0, Y0;

    public static void main(String[] args) {
        final GeoFrame mainFrame = GeoFrame.getGeoFrame();

        jLabel1.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);
                isClicked = true;
                //Нажатие по точке или нет.
                for (Point point : points) {
                    if (point.isContained(new Point(e.getX(), e.getY()))) {
                        isDragged = points.indexOf(point);
                        X0 = e.getX();
                        Y0 = e.getY();
                        break;
                    }
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                super.mouseReleased(e);
                boolean isDelete = false;
                if (isClicked) {
                    boolean isContained = false;
                    //Проверка, отпускаем ли над областью точки
                    for (Point point : points) {
                        isContained = point.equals(new Point(e.getX(), e.getY()), 40);
                        isDelete = isContained &&
                                point.equals(new Point(e.getX(), e.getY()), 30) && isDragged != -2;
                        if (isContained) break;
                    }
                    if (isDragged != -2) {
                        if (!isContained) {
                            points.add(new Point(e.getX(), e.getY()));
                            g.drawOval(e.getX() - 3, e.getY() - 3, 3, 3);
                        } else if (isDragged > -1 && isDelete) {
                            points.remove(isDragged);
                        }
                    } else {
                        points.add(new Point(X0, Y0));
                        g.drawOval(X0 - 3, Y0 - 3, 3, 3);
                    }

                    isDragged = -1;
                    isClicked = false;
                    clearImage();
                    showPoints();
                    jLabel1.repaint();
                }
                jButton2.setEnabled(points.size() > 2);
            }
        }
        );

        jLabel1.addMouseMotionListener(new MouseMotionListener() {
            @Override
            public void mouseDragged(MouseEvent e) {
                boolean isSoClose = false;
                if (isClicked) {
                    if (isDragged > -1) {
                        g.drawOval(points.get(isDragged).getX() - 3,
                                points.get(isDragged).getY() - 3, 3, 3);
                        points.remove(isDragged);
                        isDragged = -2;
                    } else if (isDragged == -2) {
                        for (Point point : points) {
                            isSoClose = point.equals(new Point(e.getX(), e.getY()), 40);
                            if (isSoClose) break;
                        }
                        if (!isSoClose && e.getX() > 3 && e.getX() < jLabel1.getWidth() - 3
                                && e.getY() > 3 && e.getY() < jLabel1.getHeight() - 3) {
                            clearImage();
                            g.drawOval(e.getX() - 3,
                                    e.getY() - 3, 3, 3);
                            X0 = e.getX();
                            Y0 = e.getY();

                        }
                    }
                    showPoints();
                }
            }

            @Override
            public void mouseMoved(MouseEvent e) {
            }
        });


        jButton1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                image1.getGraphics().setColor(Color.WHITE);
                image1.getGraphics().fillRect(0, 0, 480, 640);
                jLabel1.repaint();
                points.clear();
                jText1.setText(rules);
                jButton2.setEnabled(false);
            }
        }
        );

        jButton2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                graham();
            }
        }
        );

        jButton3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Random r = new Random();
                for (int i = 0; i < 10; i++) {
                    boolean isCons = false;
                    int X = 10 + r.nextInt(jLabel1.getWidth() - 20);
                    int Y = 10 + r.nextInt(jLabel1.getHeight() - 20);
                    for (Point point : points) {
                        isCons = point.isContained(new Point(X, Y));
                        if (isCons) break;
                    }
                    if (!isCons) {
                        points.add(new Point(X, Y));
                        clearImage();
                        showPoints();
                    }
                    jLabel1.repaint();
                }
                jButton2.setEnabled(points.size() > 2);
            }
        }
        );
    }

    public static void clearImage() {
        image1.getGraphics().setColor(Color.WHITE);
        image1.getGraphics().fillRect(0, 0, jLabel1.getWidth(), jLabel1.getHeight());
        image1.getGraphics().setColor(Color.RED);
    }

    public static void showPoints() {
        for (Point point : points) {
            g.setColor(Color.RED);
            g.setPaintMode();
            g.drawOval(point.getX() - 3, point.getY() - 3, 3, 3);
            jLabel1.repaint();
        }
    }

    public static int closestPoint() {
        Point min = new Point(Integer.MAX_VALUE, Integer.MAX_VALUE);
        int index = 0;
        for (Point point : points) {
            if (point.getY() < min.getY()) {
                min = point;
                index = points.indexOf(point);
            } else if (point.getY() == min.getY()) {
                if (point.getX() < min.getX()) {
                    min = point;
                    index = points.indexOf(point);
                }
            }
        }
        return index;

    }

    public static int myCompare(Point p, Point p1, Point p2) {
        Vector v1 = new Vector(p, p1);
        Vector v2 = new Vector(p, p2);
        return (byte) Math.signum(Vector.mulVectors(v2, v1));
    }

    public static void sort(final Point p) {
        Collections.sort(points, new Comparator<Point>() {
            @Override
            public int compare(Point p1, Point p2) {
                return myCompare(p, p1, p2);
            }
        });
    }

    public static void graham() {
        jButton1.setEnabled(false);
        jButton2.setEnabled(false);
        bestPoints = new ArrayList<Point>();
        Point tP = points.get(closestPoint());
        points.remove(closestPoint());
        sort(tP);
        points.add(0, tP);
        for (int i = 1; i < points.size(); i++) {
            g.setColor(Color.BLUE);
            drawP(i);
            jLabel1.update(jLabel1.getGraphics());
            g.setColor(Color.RED);
        }
        ArrayList<Point> uniqArray = new ArrayList<Point>();
        for (Point p : points) {
            uniqArray.add(p);
        }
        int index = 2;
        while (index < uniqArray.size()) {
            if (myCompare(uniqArray.get(0), uniqArray.get(index), uniqArray.get(index - 1)) == 0) {
                if ((new Vector(uniqArray.get(0), uniqArray.get(index))).getSize() >
                        (new Vector(uniqArray.get(0), uniqArray.get(index - 1))).getSize()) {
                    uniqArray.remove(index - 1);
                } else {
                    uniqArray.remove(index);
                }
            } else {
                index++;
            }
        }
        bestPoints.add(uniqArray.get(0));
        bestPoints.add(uniqArray.get(1));
        clearImage();
        showPoints();
        g.setColor(Color.GREEN);

        for (int i = 2; i < uniqArray.size(); i++) {
            while (Vector.mulVectors(new Vector(bestPoints.get(bestPoints.size() - 1), bestPoints.get(bestPoints.size() - 2)),
                    new Vector(bestPoints.get(bestPoints.size() - 1), uniqArray.get(i))) > 0) {
                bestPoints.remove(bestPoints.size() - 1);
                g.setColor(Color.BLUE);
                clearImage();
                showPoints();
                g.setColor(Color.GREEN);
                drawB();
            }
            bestPoints.add(uniqArray.get(i));
            drawB();
            jLabel1.update(jLabel1.getGraphics());
        }
        g.drawLine(bestPoints.get(bestPoints.size() - 1).getX(), bestPoints.get(bestPoints.size() - 1).getY(),
                bestPoints.get(0).getX(), bestPoints.get(0).getY());
        jButton1.setEnabled(true);
    }

    public static synchronized void drawB() {
        try {
            Thread.sleep(10);
            for (int i = 1; i < bestPoints.size(); i++) {
                g.drawLine(bestPoints.get(i - 1).getX(), bestPoints.get(i - 1).getY(),
                        bestPoints.get(i).getX(), bestPoints.get(i).getY());
            }
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
    }

    public static synchronized void drawP(final int i) {
        try {
            Thread.sleep(10);
            g.drawLine(points.get(0).getX(), points.get(0).getY(),
                    points.get(i).getX(), points.get(i).getY());
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
    }
}
