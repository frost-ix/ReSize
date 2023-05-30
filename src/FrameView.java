import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

public class FrameView extends JFrame {
    Container c =  getContentPane();
    public FrameView () {
        setTitle("reSize");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1350, 850);
        c.add(new View(), BorderLayout.NORTH);
        setVisible(true);
    }

    class View extends Panel {
        private String formatSet = "";
        private BufferedImage extractImage, ogImage;
        private JButton uploadBtn = new JButton("버튼을 눌러서 이미지를 업로드");
        private JButton confirmBtn = new JButton("실행");
        private JFileChooser fileChooser = new JFileChooser();
        private JTextField width = new JTextField("width", 10);
        private JTextField height = new JTextField("height", 10);
        private JMenuBar scaleMenuBar = new JMenuBar();
        private JMenuItem[] scaleMenuItems = new JMenuItem[3];
        private String[] scaleLabels = {"HD", "FHD", "QHD"};
        private JMenu scale = new JMenu("해상도");
        private JMenuBar formatBar = new JMenuBar();
        private JMenuItem[] formatItems = new JMenuItem[2];
        private String[] formatLabels = {"jpg", "png"};
        private JMenu format = new JMenu("포맷");
        private MenuActionListener listner = new MenuActionListener();
        private JLabel image = new JLabel("대기중");
        private String filePath = "";
        private int n = 0;
        InputStream inputStream;
        public View() {
            setBackground(Color.lightGray);
            for (int i = 0; i < scaleMenuItems.length; i++) {
                scaleMenuItems[i] = new JMenuItem(scaleLabels[i]);
                scaleMenuItems[i].addActionListener(listner);
                scale.add(scaleMenuItems[i]);
            }
            scaleMenuBar.add(scale);
            setJMenuBar(scaleMenuBar);

            for (int i = 0; i < formatItems.length; i++) {
                formatItems[i] = new JMenuItem(formatLabels[i]);
                formatItems[i].addActionListener(listner);
                format.add(formatItems[i]);
            }
            formatBar.add(format);
            setJMenuBar(formatBar);

            add(uploadBtn); add(scaleMenuBar); add(formatBar); add(width); add(height); add(confirmBtn);
            c.add(image);

            uploadBtn.addActionListener(e -> {
                fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
                fileChooser.setDialogTitle("사진 열기");
                int result = fileChooser.showOpenDialog(null);
                if (result == JFileChooser.APPROVE_OPTION) {
                    File getFile = fileChooser.getSelectedFile();
                    filePath = getFile.getPath();
                    try {
                        inputStream = Files.newInputStream(Paths.get(filePath));
//                        inputStream = new FileInputStream(filePath);
                        ogImage = ImageIO.read(getFile);
                        System.out.println(filePath);
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            });
            confirmBtn.addActionListener(e -> {
                if (confirmBtn.getText().equals("실행")) {
                    try {
                        c.remove(image);
                        int exchangeWidth = Integer.parseInt(String.valueOf(width.getText()));
                        int exchangeHeight = Integer.parseInt(String.valueOf(height.getText()));
                        image = new JLabel(new ImageIcon(ResizeFunc.reSize(ogImage, exchangeWidth, exchangeHeight)));
                        confirmBtn.setText("리셋");
                        c.add(image, BorderLayout.CENTER);
                        extractImage = ResizeFunc.reSize(ogImage, exchangeWidth, exchangeHeight);
                        File resized = new File("/Users/sung/Desktop/Java/Report/_2ndReport/img/result/export"+n+"."+formatSet);
                        if (resized.exists()) {
                            n++;
                            resized = new File("/Users/sung/Desktop/Java/Report/_2ndReport/img/result/export"+n+"."+formatSet);
                        }
                        ImageIO.write(extractImage, formatSet, resized);
                        setSize(exchangeWidth, exchangeHeight);
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                }
                else if (confirmBtn.getText().equals("리셋")){
                    confirmBtn.setText("실행");
                    filePath = "";
                    c.remove(image);
                    this.width.setText("");
                    this.height.setText("");
                    this.image = new JLabel("대기중");
                    setSize(800, 500);
                    c.add(image, FlowLayout.CENTER);
                }
            });
        }
        class MenuActionListener implements ActionListener {
            @Override
            public void actionPerformed(ActionEvent e) {
                String command = e.getActionCommand();
                switch (command) {
                    case "HD": {
                        width.setText("1280"); height.setText("720");
                        break;
                    }
                    case "FHD": {
                        width.setText("1920"); height.setText("1080");
                        break;
                    }
                    case "QHD": {
                        width.setText("2560"); height.setText("1440");
                        break;
                    }
                    case "png": {
                        formatSet = "png";
                        break;
                    }
                    case "jpg": {
                        formatSet = "jpg";
                        break;
                    }
                }
            }
        }
    }
}
