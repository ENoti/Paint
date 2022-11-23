import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import static java.awt.image.BufferedImage.*;

public class ImageEdit{
    int k = 5;
    public Stroke stroke = new BasicStroke(k);
    // Режим рисования
    int  rezhim = 0;
    int  xPad;
    int  xf;
    int  yf;
    int  yPad;
    boolean pressed = false;
    // текущий цвет
    private BufferedImage slate;
    Color mainColor;
    MyFrame frame;
    MyPanel panel;
    JButton colorButton;
    JColorChooser tcc;
    // поверхность рисования
    private BufferedImage imag;
    Image oldImage;
    // если мы загружаем картинку
    boolean loading = false;
    String fileName;
    public ImageEdit() {
        frame = new MyFrame("Paint");
        frame.setSize(500,500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainColor = Color.black;
        JMenuBar menuBar = new  JMenuBar();
        frame.setJMenuBar(menuBar);
        menuBar.setBounds(0,0,400,40);
        JMenu fileMenu = new  JMenu("Файл");
        JMenu fillMenu = new  JMenu("Заливка");
        menuBar.add(fileMenu);
        menuBar.add(fillMenu);
        Action savesAction = new  AbstractAction("Сохранить как...") {
            public void actionPerformed(ActionEvent event) {
                try {
                    JFileChooser jf =  new  JFileChooser();
                    // Создаем фильтры для файлов
                    TextFileFilter bmpFilter = new TextFileFilter(".bmp");
                    // Добавляем фильтры
                    jf.addChoosableFileFilter(bmpFilter);
                    int  result = jf.showSaveDialog(null);
                    if(result == JFileChooser.APPROVE_OPTION) {
                        fileName = jf.getSelectedFile().getAbsolutePath();
                    }
                    // Смотрим какой фильтр выбран
                    if(jf.getFileFilter() == bmpFilter) {
                        ImageIO.write(imag, "bmp", new  File(fileName+".bmp"));
                    }
                }
                catch(IOException ex) {
                    JOptionPane.showMessageDialog(frame, "Ошибка ввода-вывода");
                }
            }
        };

        JMenuItem savesMenu = new  JMenuItem(savesAction);
        fileMenu.add(savesMenu);
        Action fillBoyAction = new  AbstractAction("Мальчик") {
            public void actionPerformed(ActionEvent event) {
                try {
                    slate = ImageIO.read(new File("Untitled 01-21-2022 11-43-30.png"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        Action fillEraserAction = new  AbstractAction("Ластик") {
            public void actionPerformed(ActionEvent event) {
                try {
                    slate = ImageIO.read(new File("eraser.png"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        Action fillLinerAction = new  AbstractAction("Линии") {
            public void actionPerformed(ActionEvent event) {
                try {
                    slate = ImageIO.read(new File("lines.png"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        JMenuItem fillBoy = new JMenuItem(fillBoyAction);
        fillMenu.add(fillBoy);
        JMenuItem fillEraser = new JMenuItem(fillEraserAction);
        fillMenu.add(fillEraser);
        JMenuItem LinerAction = new JMenuItem(fillLinerAction);
        fillMenu.add(LinerAction);
        panel = new  MyPanel();
        panel.setBounds(40,40,400,400);
        panel.setBackground(Color.white);
        panel.setOpaque(true);
        frame.add(panel);

        //Вертикальный Бар
        JToolBar toolbar = new  JToolBar("Toolbar", JToolBar.VERTICAL);

        Icon pencil = new ImageIcon("pencil.PNG");
        JButton penButton = new JButton(pencil);
        penButton.addActionListener(event -> rezhim = 0);
        toolbar.add(penButton);

        Icon brush = new ImageIcon("paint.PNG");
        JButton brushButton = new JButton(brush);
        brushButton.addActionListener(event -> rezhim = 1);
        toolbar.add(brushButton);

        Icon lastic = new ImageIcon("eraser.PNG");
        JButton lasticButton = new JButton(lastic);
        lasticButton.addActionListener(event -> rezhim = 2);
        toolbar.add(lasticButton);

        Icon circle_null = new ImageIcon("circle_null.PNG");
        JButton elipsButton = new JButton(circle_null);
        elipsButton.addActionListener(event -> rezhim = 3);
        toolbar.add(elipsButton);

        Icon circle_fill_color = new ImageIcon("circle_fill_color.PNG");
        JButton elipsFillColorButton = new JButton(circle_fill_color);
        elipsFillColorButton.addActionListener(event -> rezhim = 4);
        toolbar.add(elipsFillColorButton);

        Icon circle_fill = new ImageIcon("circle_fill.PNG");
        JButton elipsFillButton = new JButton(circle_fill);
        elipsFillButton.addActionListener(event -> rezhim = 5);
        toolbar.add(elipsFillButton);

        Icon rectangul_null = new ImageIcon("rectangul_null.PNG");
        JButton rectButton = new JButton(rectangul_null);
        rectButton.addActionListener(event -> rezhim = 6);
        toolbar.add(rectButton);

        Icon rectangul_fill_color = new ImageIcon("rectangul_fill_color.PNG");
        JButton rectFillColorButton = new JButton(rectangul_fill_color);
        rectFillColorButton.addActionListener(event -> rezhim = 7);
        toolbar.add(rectFillColorButton);

        Icon rectangul_fill = new ImageIcon("rectangul_fill.PNG");
        JButton rectFillButton = new JButton(rectangul_fill);
        rectFillButton.addActionListener(event -> rezhim = 8);
        toolbar.add(rectFillButton);

        Icon size = new ImageIcon("size.PNG");
        JButton IncButton = new JButton(size);
        IncButton.addActionListener(event -> rezhim = 9);
        toolbar.add(IncButton);

        toolbar.setBounds(0, 0, 40, 400);
        frame.add(toolbar);

        // Горизонтальный бар
        JToolBar color = new  JToolBar("Color bar", JToolBar.HORIZONTAL);
        color.setBounds(40, 0, 260, 30);

        JButton sizeDownButton = new JButton("<-");
        sizeDownButton.addActionListener(event -> {
            k-=2;
            stroke = new BasicStroke(k);
        });
        sizeDownButton.setBounds(20, 0, 25, 25);
        color.add(sizeDownButton);

        JButton sizeUpButton = new JButton("->");
        sizeUpButton.addActionListener(event -> {
            k+=2;
            stroke = new BasicStroke(k);
        });

        sizeUpButton.setBounds(40, 0, 25, 25);
        color.add(sizeUpButton);

        String[] items = {
                "Заливка человечек",
                "Заливка ластик",
                "Выбор цвета"
        };
        JComboBox<String> comboBox = new JComboBox<>(items);
        comboBox.setEditable(true);
        colorButton = new JButton();
        colorButton.setBackground(mainColor);
        colorButton.setBounds(60, 0, 25, 25);
        colorButton.addActionListener(event -> {
            ColorDialog coldi = new ColorDialog(frame,"Выбор цвета");
            coldi.setVisible(true);
        });
        color.add(colorButton);
        color.setLayout(null);
        frame.add(color);
        // Выбор цвета
        tcc = new JColorChooser(mainColor);
        tcc.getSelectionModel().addChangeListener(e -> {
            mainColor = tcc.getColor();
            colorButton.setBackground(mainColor);
        });
        panel.addMouseMotionListener(new  MouseMotionAdapter() {
            public void mouseDragged(MouseEvent e) {
                int  x1 = xf, x2 = xPad, y1 = yf, y2 = yPad;
                if(xf>xPad) {
                    x2 = xf; x1 = xPad;
                }
                if(yf>yPad) {
                    y2 = yf; y1 = yPad;
                }
                if (pressed) {
                    Graphics g = imag.getGraphics();
                    Graphics2D g2 = (Graphics2D)g;
                    // установка цвета
                    g2.setColor(mainColor);
                    switch (rezhim) {
                        // карандаш
                        case 0 -> {
                            g2.setStroke(stroke);
                            g2.drawLine(xPad, yPad, e.getX(), e.getY());
                        }
                        // кисть
                        case 1 -> {
                            var slateTp = new TexturePaint(slate, new Rectangle(0, 0, 90, 60));
                            g2.setPaint(slateTp);
                            g2.setStroke(stroke);
                            g2.drawLine(xPad, yPad, e.getX(), e.getY());
                        }
                        // ластик
                        case 2 -> {
                            g2.setStroke(stroke);
                            g2.setColor(Color.WHITE);
                            g2.drawLine(xPad, yPad, e.getX(), e.getY());
                        }
                        case 3, 4, 5 -> {
                            g2.drawImage(oldImage, 0, 0, null);
                            Ellipse2D oval = new Ellipse2D.Double(x1, y1, (x2 - x1), (y2 - y1));
                            g2.draw(oval);
                        }
                        case 6, 7, 8 -> {
                            g2.drawImage(oldImage, 0, 0, null);
                            Rectangle2D rect = new Rectangle2D.Double(x1, y1, (x2 - x1), (y2 - y1));
                            g2.draw(rect);
                        }
                    }
                    xPad = e.getX();
                    yPad = e.getY();
                }
                panel.repaint();
            }
        });
        panel.addMouseListener(new  MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                Graphics g = imag.getGraphics();
                Graphics2D g2 = (Graphics2D)g;
                // установка цвета
                g2.setColor(mainColor);
                switch (rezhim) {
                    // карандаш
                    case 0 -> {
                        g2.setStroke(new BasicStroke(1.0f));
                        g2.drawLine(xPad, yPad, xPad + 1, yPad + 1);
                    }
                    // кисть
                    case 1 -> {
                        var slateTp = new TexturePaint(slate, new Rectangle(0, 0, 90, 60));
                        g2.setPaint(slateTp);
                        g2.setColor(mainColor);
                        g2.drawLine(xPad, yPad, xPad + 1, yPad + 1);
                    }
                    // ластик
                    case 2 -> {
                        g2.setStroke(new BasicStroke(3.0f));
                        g2.setColor(Color.WHITE);
                        g2.drawLine(xPad, yPad, xPad + 1, yPad + 1);
                    }

                }
                xPad = e.getX();
                yPad = e.getY();

                pressed = true;
                panel.repaint();
            }
            public void mousePressed(MouseEvent e) {
                xPad = e.getX();
                yPad = e.getY();
                xf = e.getX();
                yf = e.getY();
                switch (rezhim){
                    case 3, 4, 5, 6, 7, 8 -> {
                        oldImage = panel.createImage(imag.getWidth(), imag.getHeight());
                        Graphics2D gg2 = (Graphics2D) oldImage.getGraphics();
                        gg2.drawImage(imag, 0, 0, null);
                    }
                }

                pressed = true;
            }
            public void mouseReleased(MouseEvent e) {
                Graphics g = imag.getGraphics();
                Graphics2D g2 = (Graphics2D)g;
                // установка цвета
                g2.setColor(mainColor);
                // Общие рассчеты для овала и прямоугольника
                int  x1 = xf, x2 = xPad, y1 = yf, y2 = yPad;
                if(xf>xPad) {
                    x2 = xf; x1 = xPad;
                }
                if(yf>yPad) {
                    y2 = yf; y1 = yPad;
                }
                switch (rezhim) {
                    // круг
                    case 3 -> {
                        Ellipse2D oval = new Ellipse2D.Double(x1, y1, (x2 - x1), (y2 - y1));
                        g2.draw(oval);
                    }
                    case 4 -> {
                        Ellipse2D oval = new Ellipse2D.Double(x1, y1, (x2 - x1), (y2 - y1));
                        g2.fill(oval);
                    }
                    case 5 -> {
                        var slateTp = new TexturePaint(slate, new Rectangle(0, 0, 90, 60));
                        g2.setPaint(slateTp);
                        Ellipse2D oval = new Ellipse2D.Double(x1, y1, (x2 - x1), (y2 - y1));
                        g2.fill(oval);
                    }
                    // прямоугольник
                    case 6 -> g.drawRect(x1, y1, (x2 - x1), (y2 - y1));
                    case 7 ->{
                        Rectangle2D rect = new Rectangle2D.Double(x1, y1, (x2 - x1), (y2 - y1));
                        g2.fill(rect);
                    }
                    case 8 -> {
                        var slateTp = new TexturePaint(slate, new Rectangle(0, 0, 90, 60));
                        g2.setPaint(slateTp);
                        Rectangle2D rect = new Rectangle2D.Double(x1, y1, (x2 - x1), (y2 - y1));
                        g2.fill(rect);
                    }
                    case 9 -> {
                        var saveImage = imag.getSubimage(x1, y1, (x2 - x1), (y2 - y1));
                        int oldWidth = saveImage.getWidth();
                        int oldHeight = saveImage.getHeight();
                        Image scaledSaveImage = saveImage.getScaledInstance((int) (oldWidth * 2.5), (int) (oldHeight * 2.5), Image.SCALE_SMOOTH);
                        g.drawImage(scaledSaveImage, x1 - oldWidth, y1 - oldHeight, null);
                    }
                }
                xf = 0; yf = 0;
                pressed = false;
                panel.repaint();
            }
        });
        frame.addComponentListener(new  ComponentAdapter() {
            public void componentResized(java.awt.event.ComponentEvent evt) {
                // если делаем загрузку, то изменение размеров формы
                // отрабатываем в коде загрузки
                if(!loading) {
                    panel.setSize(frame.getWidth()-40, frame.getHeight()-80);
                    var tempImage = new BufferedImage(panel.getWidth(), panel.getHeight(), TYPE_INT_RGB);
                    Graphics2D d2 = tempImage.createGraphics();
                    d2.setColor(Color.white);
                    d2.fillRect(0, 0, panel.getWidth(), panel.getHeight());
                    tempImage.setData(imag.getRaster());
                    imag = tempImage;
                    panel.repaint();
                }
                loading = false;
            }
        });
        frame.setLayout(null);
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(ImageEdit::new);
    }

    class ColorDialog extends JDialog {
        public ColorDialog(JFrame owner, String title)
        {
            super(owner, title, true);
            add(tcc);
            setSize(200, 200);
        }
    }

    static class MyFrame extends JFrame {
        public void paint(Graphics g)
        {
            super.paint(g);
        }
        public MyFrame(String title)
        {
            super(title);
        }
    }

    class MyPanel extends JPanel {
        public MyPanel(){}
        public void paintComponent (Graphics g) {
            if(imag == null) {
                imag = new  BufferedImage(this.getWidth(), this.getHeight(), TYPE_INT_RGB);
                Graphics2D d2 = imag.createGraphics();
                d2.setColor(Color.white);
                d2.fillRect(0, 0, this.getWidth(), this.getHeight());
            }
            super.paintComponent(g);
            g.drawImage(imag, 0, 0,this);
        }
    }

    // Фильтр картинок
    static class TextFileFilter extends FileFilter {
        private final String ext;
        public TextFileFilter(String ext) {
            this.ext = ext;
        }
        public boolean accept(java.io.File file) {
            if (file.isDirectory()) return true;
            return (file.getName().endsWith(ext));
        }
        public String getDescription() {
            return "*"+ext;
        }
    }
}