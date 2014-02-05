import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

//Класс главного окна приложения
public class GeoFrame extends JFrame {
    private static GeoFrame gFrame = new GeoFrame();

    private final int DEFAULT_POSITION_X; //координата X верхнего левого угла окна
    private final int DEFAULT_POSITION_Y; //координата Y верхнего левого угла окна
    private final int DEFAULT_WIDTH;      //ширина окна
    private final int DEFAULT_HEIGHT;     //высота окна

    //Компоненты
    private JPanel gPanel1;

    private JLabel jLabel1;
    private JTextArea jText1;
    private JPanel gPanel2;

    private JButton jButton1;
    private JButton jButton2;

    //Дополнительные компоненты
    private BufferedImage image1;
    private ImageIcon iIcon1;
    private Graphics g;

    //Коструктор окна
    private GeoFrame() {
        Toolkit t = Toolkit.getDefaultToolkit();
        DEFAULT_POSITION_X = t.getScreenSize().width / 4;
        DEFAULT_POSITION_Y = t.getScreenSize().height / 4;
        DEFAULT_WIDTH = t.getScreenSize().width / 2;
        DEFAULT_HEIGHT = t.getScreenSize().height / 2;

        JFrame.setDefaultLookAndFeelDecorated(true);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setResizable(false);
        this.setBounds(DEFAULT_POSITION_X, DEFAULT_POSITION_Y, DEFAULT_WIDTH, DEFAULT_HEIGHT);
        this.setIconImage(Toolkit.getDefaultToolkit().createImage("C:\\Users\\Игорь\\Desktop\\Документы\\X7l5SlpT1Uc.jpg"));
        this.setTitle("Геометрия 2.1. Задание 2");

        gPanel1 = new JPanel(new BorderLayout());
        jLabel1 = new JLabel();
        jLabel1.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        jText1 = new JTextArea("Правила:" +
                "\nЧтобы добавить точку, щелкните на \nсвободную область." +
                "\nНачало координат слева вверху." +
                "\nПри щелчке на точку, она удалится." +
                "\nТочки можно перемещать." +
                "\nВо время выполнения алгоритма запрещены" +
                "\nлюбые изменения."
        );
        jText1.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        jText1.setEditable(false);
        gPanel2 = new JPanel(new GridLayout(12, 1));
        gPanel1.add(this.jLabel1, BorderLayout.EAST);
        gPanel1.add(this.gPanel2, BorderLayout.WEST);
        gPanel1.add(this.jText1, BorderLayout.CENTER);

        jButton1 = new JButton();
        jButton1.setText("Clear");
        jButton2 = new JButton();
        jButton2.setEnabled(false);
        jButton2.setText("Go!");
        gPanel2.add(jButton1);
        gPanel2.add(jButton2);

        this.setContentPane(this.gPanel1);
        this.setVisible(true);

        this.image1 = new BufferedImage(this.getWidth() / 2, this.jLabel1.getHeight(), BufferedImage.TYPE_INT_RGB);
        this.image1.getGraphics().fillRect(0, 0, 480, 640);
        this.image1.getGraphics().setColor(Color.BLACK);

        iIcon1 = new ImageIcon();
        iIcon1.setImage(this.image1);
        this.jLabel1.setIcon(iIcon1);
        g = this.image1.getGraphics();
    }

    public static GeoFrame getGeoFrame() {
        return gFrame;
    }

    //Глобальные переменные
    ArrayList<Point> points = new ArrayList<Point>();
    ArrayList<Point> bestPoints = new ArrayList<Point>();
    //Нажата ли кнопка
    boolean isClicked = false;
    /*
     * Если isDragged > 0, значит, было нажатие на точку
     * -1 и 0 - отсутствие нажатия
     * -2 - до события было нажатие на точку
     */
    int isDragged = -1;
    //Координаты точки нажатия
    int X0, Y0;

    public static void main(String[] args) {
        final GeoFrame mainFrame = GeoFrame.getGeoFrame();

        mainFrame.jLabel1.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);
                mainFrame.isClicked = true;
                //Нажатие по точке или нет.
                for (Point point : mainFrame.points) {
                    if (point.isContained(new Point(e.getX(), e.getY()))) {
                        mainFrame.isDragged = mainFrame.points.indexOf(point);
                        mainFrame.X0 = e.getX();
                        mainFrame.Y0 = e.getY();
                        break;
                    }
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                super.mouseReleased(e);
                boolean isDelete = false;
                if (mainFrame.isClicked) {
                    boolean isContained = false;
                    //Проверка, отпускаем ли над областью точки
                    for (Point point : mainFrame.points) {
                        if (point.equals(new Point(e.getX(), e.getY()), 40)) {
                            isContained = true;
                            if (point.equals(new Point(e.getX(), e.getY()), 30) && mainFrame.isDragged != -2) {
                                isDelete = true;
                            }
                            break;
                        }
                    }
                    if (!isContained && mainFrame.isDragged != -2) {
                        mainFrame.points.add(new Point(e.getX(), e.getY()));
                        mainFrame.g.setColor(Color.RED);
                        mainFrame.g.setPaintMode();
                        mainFrame.g.drawOval(e.getX() - 3, e.getY() - 3, 3, 3);
                        mainFrame.jLabel1.repaint();
                    } else {
                        if (mainFrame.isDragged == -2 && !isDelete) {
                            mainFrame.points.add(new Point(mainFrame.X0, mainFrame.Y0));
                            mainFrame.g.setColor(Color.RED);
                            mainFrame.g.setPaintMode();
                            mainFrame.g.drawOval(mainFrame.X0 - 3, mainFrame.Y0 - 3, 3, 3);
                            mainFrame.jLabel1.repaint();
                        } else if (mainFrame.isDragged > -1 && isDelete) {
                            mainFrame.points.remove(mainFrame.isDragged);
                        }
                    }
                    isContained = false;
                    isDelete = false;
                    mainFrame.isDragged = -1;
                    mainFrame.isClicked = false;
                    mainFrame.clearImage();
                    mainFrame.showPoints();
                    mainFrame.jLabel1.repaint();
                }
                if (mainFrame.points.size() > 2) {
                    mainFrame.jButton2.setEnabled(true);
                } else {
                    mainFrame.jButton2.setEnabled(false);
                }
            }
        }
        );

        mainFrame.jLabel1.addMouseMotionListener(new MouseMotionListener() {
            @Override
            public void mouseDragged(MouseEvent e) {
                boolean isSoClose = false;
                if (mainFrame.isClicked) {
                    if (mainFrame.isDragged > -1) {
                        mainFrame.g.setColor(Color.WHITE);
                        mainFrame.g.setPaintMode();
                        mainFrame.g.drawOval(mainFrame.points.get(mainFrame.isDragged).getX() - 3,
                                mainFrame.points.get(mainFrame.isDragged).getY() - 3, 3, 3);
                        mainFrame.points.remove(mainFrame.isDragged);
                        mainFrame.clearImage();
                        mainFrame.showPoints();
                        mainFrame.jLabel1.repaint();
                        mainFrame.isDragged = -2;
                    } else if (mainFrame.isDragged == -2) {
                        for (Point point : mainFrame.points) {
                            if (point.equals(new Point(e.getX(), e.getY()), 40)) {
                                isSoClose = true;
                                break;
                            }
                        }
                        if (!isSoClose && e.getX() > 3 && e.getX() < mainFrame.getWidth() - 3
                                && e.getY() > 3 && e.getY() < mainFrame.getHeight() - 3) {
                            mainFrame.g.setColor(Color.WHITE);
                            mainFrame.g.drawOval(mainFrame.X0 - 3,
                                    mainFrame.Y0 - 3, 3, 3);
                            mainFrame.g.setPaintMode();
                            mainFrame.g.setColor(Color.RED);
                            mainFrame.g.drawOval(e.getX() - 3,
                                    e.getY() - 3, 3, 3);
                            mainFrame.X0 = e.getX();
                            mainFrame.Y0 = e.getY();
                            mainFrame.showPoints();
                            mainFrame.jLabel1.repaint();
                        }
                    }
                }
            }

            @Override
            public void mouseMoved(MouseEvent e) {
                //To change body of implemented methods use File | Settings | File Templates.
            }
        });


        mainFrame.jButton1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainFrame.image1.getGraphics().setColor(Color.WHITE);
                mainFrame.image1.getGraphics().fillRect(0, 0, 480, 640);
                mainFrame.jLabel1.repaint();
                mainFrame.points.clear();
                mainFrame.jText1.setText("Правила:" +
                        "\nЧтобы добавить точку, щелкните на \nсвободную область." +
                        "\nНачало координат слева вверху." +
                        "\nПри щелчке на точку, она удалится." +
                        "\nТочки можно перемещать." +
                        "\nВо время выполнения алгоритма запрещены" +
                        "\nлюбые изменения."
                );
                mainFrame.jButton2.setEnabled(false);
            }
        }
        );

        mainFrame.jButton2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainFrame.graham();
            }
        }
        );
    }

    public void clearImage() {
        this.image1.getGraphics().setColor(Color.WHITE);
        this.image1.getGraphics().fillRect(0, 0, this.jLabel1.getWidth(), this.jLabel1.getHeight());
        this.image1.getGraphics().setColor(Color.RED);
    }

    public void showPoints() {
        for (Point point : this.points) {
            this.g.setColor(Color.RED);
            this.g.setPaintMode();
            this.g.drawOval(point.getX() - 3, point.getY() - 3, 3, 3);
            this.jLabel1.repaint();
        }
    }

    public int closestPoint() {
        double min = Double.MAX_VALUE;
        int index = 0;
        for (Point point : this.points) {
            if ((new Vector(new Point(0, 0), point)).getSize() < min) {
                min = (new Vector(new Point(0, 0), point)).getSize();
                index = this.points.indexOf(point);
            }
        }
        return index;

    }

    public int myCompare(Point p, Point p1, Point p2) {
        int X0 = p.getX();
        int X1 = p1.getX() - X0;
        int X2 = p2.getX() - X0;
        int Y0 = p.getY();
        if(p.getY()==p1.getY()){
            p1 = new Point(p1.getX(),p1.getY()+1);
        }
        if(p.getY()==p2.getY()){
            p2 = new Point(p2.getX(),p2.getY()+1);
        }
        int Y1 = p1.getY() - Y0;
        int Y2 = p2.getY() - Y0;

        double atan1 = Math.atan((double) Y1 / X1);
        if (atan1 < 0) {
            atan1 += 2 * Math.PI;
        }
        double atan2 = Math.atan((double) Y2 / X2);
        if (atan2 < 0) {
            atan2 += 2 * Math.PI;
        }
        if (atan1 == 0) {
            atan1 += 2 * Math.PI - 0.00001;
        }
        if (atan2 == 0) {
            atan2 += 2 * Math.PI - 0.00001;
        }
        ;
        if (atan1 == atan2) {
            return 0;
        } else if (Y1 >= 0 && Y2 >= 0) {
            return atan1 > atan2 ? 1 : -1;
        } else if (Y1 < 0 && Y2 > 0) {
            return -1;
        } else if (Y1 > 0 && Y2 < 0) {
            return 1;
        } else {
            return atan1 > atan2 ? 1 : -1;
        }
    }

    public void sort(final Point p) {
        Collections.sort(this.points, new Comparator<Point>() {
            @Override
            public int compare(Point p1, Point p2) {
                return myCompare(p, p1, p2);
            }
        });
    }

    public void graham() {
        this.jButton1.setEnabled(false);
        this.jButton2.setEnabled(false);
        this.bestPoints = new ArrayList<Point>();
        Point tP;
        tP = this.points.get(this.closestPoint());
        this.points.remove(this.closestPoint());
        this.sort(tP);
        this.points.add(0, tP);
        for (int i = 1; i < points.size(); i++) {
            g.setColor(Color.BLUE);
            drawP(i);
            jLabel1.update(jLabel1.getGraphics());
            g.setColor(Color.RED);
        }
        Point p0 = points.get(0);
        Point p1 = points.get(1);
        this.bestPoints.add(p0);
        this.bestPoints.add(p1);
        this.clearImage();
        this.showPoints();
        this.g.setColor(Color.GREEN);

        for (int i = 2; i < points.size(); i++) {
            if (points.size() > i) {
                while (Vector.mulVectors(new Vector(this.bestPoints.get(this.bestPoints.size() - 1), this.bestPoints.get(this.bestPoints.size() - 2)),
                        new Vector(this.bestPoints.get(this.bestPoints.size() - 1), points.get(i))) > 0) {
                    this.bestPoints.remove(this.bestPoints.size() - 1);
                    this.g.setColor(Color.BLUE);
                    this.clearImage();
                    this.showPoints();
                    this.g.setColor(Color.GREEN);
                    this.drawB();
                }
                this.bestPoints.add(this.points.get(i));
                this.drawB();
                jLabel1.update(jLabel1.getGraphics());
            }
        }
        g.drawLine(this.bestPoints.get(this.bestPoints.size() - 1).getX(), this.bestPoints.get(this.bestPoints.size() - 1).getY(),
                this.bestPoints.get(0).getX(), this.bestPoints.get(0).getY());
        this.jButton1.setEnabled(true);
    }

    public synchronized void drawB() {
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
            }
        });
        t.start();
        synchronized (t) {
            try {
                t.wait();
                t.sleep(10);
                for (int i = 1; i < bestPoints.size(); i++) {
                    g.drawLine(bestPoints.get(i - 1).getX(), bestPoints.get(i - 1).getY(),
                            bestPoints.get(i).getX(), bestPoints.get(i).getY());
                }
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
            }
        }
    }

    public synchronized void drawP(final int i) {
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
            }
        });
        t.start();
        synchronized (t) {
            try {
                t.wait();
                t.sleep(10);
                g.drawLine(points.get(0).getX(), points.get(0).getY(),
                        points.get(i).getX(), points.get(i).getY());
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
            }
        }
    }
}
