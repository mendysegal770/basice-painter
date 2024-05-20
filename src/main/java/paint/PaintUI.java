package paint;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;


public class PaintUI extends JFrame implements ActionListener {
    private PaintManager paintManager;
    private EditManager editManager;
    private Lode_Save_picture lodeSavePicture;
    static JLabel widthLabel;
    static JLabel heightLabel;
    private JButton selectedColor;


    public PaintUI(PaintManager paintManager, EditManager editManager, Lode_Save_picture lodeSavePicture) throws IOException {
        this.paintManager = paintManager;
        this.editManager = editManager;
        this.lodeSavePicture = lodeSavePicture;
        setTitle("Painter");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        changeLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");

        createMenu();
        westComponent();
        eastComponent();
        southComponent();

        JScrollPane scrollPane = new JScrollPane(paintManager.getMainPanel());
        add(scrollPane);

        setVisible(true);
    }

    private void createMenu() throws IOException {
        JMenuBar menuBar = new JMenuBar();
        JMenu optionsMenu = new JMenu("file");
        optionsMenu.setFont(new Font("Arial", Font.BOLD, 20));


        Image broom = ImageIO.read(getClass().getResourceAsStream("/icons/broom.png"));
        broom = broom.getScaledInstance(40, 40, Image.SCALE_SMOOTH);
        JMenuItem newItem = new JMenuItem("new", new ImageIcon(broom));

        newItem.addActionListener(this);
        newItem.setFont(new Font("Arial", Font.BOLD, 20));

        Image open = ImageIO.read(getClass().getResourceAsStream("/icons/open.png"));
        open = open.getScaledInstance(40, 40, Image.SCALE_SMOOTH);
        JMenu openItem = createMenuItem("open", open);


        Image save = ImageIO.read(getClass().getResourceAsStream("/icons/save.png"));
        save = save.getScaledInstance(40, 40, Image.SCALE_SMOOTH);

        JMenu saveItem = createMenuItem("save", save);

        Image exit = ImageIO.read(getClass().getResourceAsStream("/icons/exit.png"));
        exit = exit.getScaledInstance(40, 40, Image.SCALE_SMOOTH);

        JMenuItem exitItem = new JMenuItem("exit", new ImageIcon(exit));
        exitItem.addActionListener(this);
        exitItem.setFont(new Font("Arial", Font.BOLD, 20));

        optionsMenu.add(newItem);
        optionsMenu.add(openItem);
        optionsMenu.add(saveItem);
        optionsMenu.add(exitItem);
        optionsMenu.setPreferredSize(new Dimension(50, 40));

        Image undoImage = ImageIO.read(getClass().getResourceAsStream("/icons/undo.png"));
        undoImage = undoImage.getScaledInstance(40, 40, Image.SCALE_SMOOTH);
        JButton undoButton = new JButton(new ImageIcon(undoImage));
        undoButton.setFont(new Font("Arial", Font.BOLD, 20));
        undoButton.setToolTipText("undo");
        undoButton.addActionListener(e -> editManager.undo());

        Image redoImage = ImageIO.read(getClass().getResourceAsStream("/icons/redo.png"));
        redoImage = redoImage.getScaledInstance(40, 40, Image.SCALE_SMOOTH);
        JButton redoButton = new JButton(new ImageIcon(redoImage));
        redoButton.setFont(new Font("Arial", Font.BOLD, 20));
        redoButton.setToolTipText("redo");
        redoButton.addActionListener(e -> editManager.redo());


        menuBar.add(optionsMenu);
        menuBar.add(undoButton);
        menuBar.add(redoButton);
        add(menuBar, BorderLayout.PAGE_START);
    }

    private void westComponent() throws IOException {

        Image pen = ImageIO.read(getClass().getResourceAsStream("/icons/pen.png"));
        pen = pen.getScaledInstance(40, 40, Image.SCALE_SMOOTH);
        JButton penButton = new JButton(new ImageIcon(pen));
        penButton.setFont(new Font("Arial", Font.BOLD, 20));
        penButton.setToolTipText("pen");
        penButton.addActionListener(e -> paintManager.pen());


        Image eraser = ImageIO.read(getClass().getResourceAsStream("/icons/eraser.png"));
        eraser = eraser.getScaledInstance(40, 40, Image.SCALE_SMOOTH);

        JButton eraserButton = new JButton(new ImageIcon(eraser));
        eraserButton.setFont(new Font("Arial", Font.BOLD, 20));
        eraserButton.setToolTipText("eraser");
        eraserButton.addActionListener(e -> paintManager.eraser());


        JButton textButton = new JButton("T");
        textButton.setFont(new Font("Arial", Font.BOLD, 35));
        textButton.setPreferredSize(new Dimension(65, 45));
        textButton.setToolTipText("write text");
        textButton.addActionListener(e -> {
            paintManager.text();
        });

        Image cut = ImageIO.read(getClass().getResourceAsStream("/icons/Scissors.png"));
        cut = cut.getScaledInstance(40, 40, Image.SCALE_SMOOTH);
        JButton cutButton = new JButton(new ImageIcon(cut));
        cutButton.setFont(new Font("Arial", Font.BOLD, 20));
        cutButton.setToolTipText("cut");
        cutButton.addActionListener(e -> {
            paintManager.cut();
        });


        Object[] numbers = {5, 9, 13, 17, 21, 25, 29, 33, 37, 41, 45};
        JComboBox<Object> comboBoxInk = new JComboBox<>(numbers);


        comboBoxInk.setFont(new Font("Arial", Font.BOLD, 20));
        comboBoxInk.setToolTipText("Change the ink or eraser thickness");

        comboBoxInk.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selected = String.valueOf(comboBoxInk.getSelectedItem());
                paintManager.resizeInkByComboBox(Integer.parseInt(selected));
            }
        });

        Object[] shapeThickness = {0.5f, 1f, 2f, 3f, 4f, 5f};
        JComboBox<Object> comboBoxShape = new JComboBox<>(shapeThickness);

        comboBoxShape.setFont(new Font("Arial", Font.BOLD, 20));
        comboBoxShape.setToolTipText("Change the shape thickness");

        comboBoxShape.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selected = String.valueOf(comboBoxShape.getSelectedItem());
                paintManager.resizeShapeThicknessByComboBox(Float.parseFloat(selected));
            }
        });

        JPanel tools = new JPanel(new GridLayout(4, 1, 0, 30));
        tools.setBorder(new LineBorder(Color.BLACK, 1));
        tools.add(penButton);
        tools.add(eraserButton);
        tools.add(textButton);
        tools.add(cutButton);

        JPanel boxes = new JPanel(new GridLayout(2, 1, 0, 30));
        boxes.add(comboBoxInk);
        boxes.add(comboBoxShape);
        boxes.setPreferredSize(new Dimension(50, 150));
        boxes.setBorder(new LineBorder(Color.BLACK, 1));

        JPanel editPanel = new JPanel(new BorderLayout());
        editPanel.add(tools, BorderLayout.NORTH);
        editPanel.add(boxes, BorderLayout.SOUTH);


        add(editPanel, BorderLayout.WEST);

    }


    private void eastComponent() throws IOException {
        JPanel paintPanel = new JPanel(new GridLayout(4, 3, 5, 5));
        Color[] colors = {Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW, Color.ORANGE,
                Color.CYAN, Color.MAGENTA, Color.PINK, Color.LIGHT_GRAY, Color.DARK_GRAY, Color.BLACK, new Color(220, 100, 0)};

        for (int i = 0; i < colors.length; i++) {
            JButton button = new JButton();
            button.setBackground(colors[i]);
            int finalI = i;
            button.addActionListener(e -> {
                paintManager.setCurrentColor(colors[finalI]);
                selectedColor.setBackground(colors[finalI]);
            });
            paintPanel.add(button);
            button.setPreferredSize(new Dimension(100, 35));
        }
        paintPanel.setPreferredSize(new Dimension(100, 200));
        paintPanel.setBorder(new LineBorder(Color.BLACK, 1));

        selectedColor = new JButton();
        selectedColor.setBackground(Color.BLACK);
        selectedColor.setPreferredSize(new Dimension(100, 50));
        selectedColor.setToolTipText("the selected color");

        JPanel colorsPanel = new JPanel(new BorderLayout());
        colorsPanel.setPreferredSize(new Dimension(100, 300));
        colorsPanel.add(paintPanel, BorderLayout.NORTH);
        colorsPanel.add(selectedColor, BorderLayout.SOUTH);


        Image circle = ImageIO.read(getClass().getResourceAsStream("/icons/circle.png"));
        circle = circle.getScaledInstance(80, 60, Image.SCALE_SMOOTH);
        JButton circleButton = new JButton(new ImageIcon(circle));
        circleButton.setFont(new Font("Arial", Font.BOLD, 20));
        circleButton.addActionListener(e -> paintManager.shapes("circle"));
        circleButton.setPreferredSize(new Dimension(100, 90));


        Image rect = ImageIO.read(getClass().getResourceAsStream("/icons/rect.png"));
        rect = rect.getScaledInstance(80, 60, Image.SCALE_SMOOTH);
        JButton rectButton = new JButton(new ImageIcon(rect));
        rectButton.setFont(new Font("Arial", Font.BOLD, 20));
        rectButton.addActionListener(e -> paintManager.shapes("rect"));
        rectButton.setPreferredSize(new Dimension(100, 90));

        Image line = ImageIO.read(getClass().getResourceAsStream("/icons/line.png"));
        line = line.getScaledInstance(80, 60, Image.SCALE_SMOOTH);
        JButton lineButton = new JButton(new ImageIcon(line));
        lineButton.setFont(new Font("Arial", Font.BOLD, 20));
        lineButton.addActionListener(e -> paintManager.shapes("line"));
        lineButton.setPreferredSize(new Dimension(100, 90));

        JPanel shapesPanel = new JPanel(new GridLayout(3, 1, 0, 40));
        shapesPanel.setBorder(new LineBorder(Color.BLACK, 1));

        shapesPanel.add(circleButton);
        shapesPanel.add(rectButton);
        shapesPanel.add(lineButton);
        shapesPanel.setPreferredSize(new Dimension(100, 300));


        JPanel eastPanel = new JPanel(new BorderLayout());
        eastPanel.add(colorsPanel, BorderLayout.NORTH);
        eastPanel.add(shapesPanel, BorderLayout.SOUTH);
        eastPanel.setPreferredSize(new Dimension(100, 700));

        add(eastPanel, BorderLayout.EAST);
    }

    private void southComponent() {

        paintManager.getPixel().setPreferredSize(new Dimension(950, 50));


        JLabel zoomLabel = new JLabel("zoom: ");
        Font fontZoom = new Font("Arial", Font.BOLD, 18);
        zoomLabel.setFont(fontZoom);
        zoomLabel.setPreferredSize(new Dimension(75, 50));

        JSlider zoomSlider = new JSlider(JSlider.HORIZONTAL, 0, 10, 7);
        zoomSlider.setMajorTickSpacing(1);
        zoomSlider.setMinorTickSpacing(0);
        zoomSlider.setPaintTicks(true);
        zoomSlider.setPaintLabels(true);
        editManager.zoom(zoomSlider.getValue());
        zoomSlider.addChangeListener(e -> {
            editManager.zoom(zoomSlider.getValue());
        });

        JPanel zoomPanel = new JPanel();
        zoomPanel.setLayout(new BorderLayout());
        zoomPanel.add(zoomLabel, BorderLayout.WEST);
        zoomPanel.add(zoomSlider, BorderLayout.CENTER);
        JPanel space = new JPanel();
        space.setPreferredSize(new Dimension(15, 50));
        zoomPanel.add(space, BorderLayout.EAST);
        zoomPanel.setPreferredSize(new Dimension(320, 50));


        widthLabel = new JLabel();
        heightLabel = new JLabel();
        widthLabel.setFont(new Font("Arial", Font.BOLD, 20));
        heightLabel.setFont(new Font("Arial", Font.BOLD, 20));


        widthLabel.setText(String.valueOf(paintManager.resizeWidth));
        widthLabel.setPreferredSize(new Dimension(50, 50));
        heightLabel.setText(String.valueOf(paintManager.resizeHeight));


        widthLabel.setToolTipText("width");
        heightLabel.setToolTipText("height");

        JPanel widthHeightPanel = new JPanel();
        widthHeightPanel.setPreferredSize(new Dimension(100, 50));
        widthHeightPanel.setLayout(new BorderLayout());
        widthHeightPanel.add(widthLabel, BorderLayout.WEST);
        widthHeightPanel.add(heightLabel, BorderLayout.CENTER);


        JPanel pixel_zoom_panel = new JPanel(new BorderLayout());
        pixel_zoom_panel.add(paintManager.getPixel(), BorderLayout.WEST);
        pixel_zoom_panel.add(zoomPanel, BorderLayout.EAST);
        pixel_zoom_panel.setPreferredSize(new Dimension(1440, 50));

        JPanel southPanel = new JPanel();
        southPanel.setLayout(new BorderLayout());
        southPanel.add(pixel_zoom_panel, BorderLayout.WEST);
        southPanel.add(widthHeightPanel, BorderLayout.CENTER);


        add(southPanel, BorderLayout.SOUTH);
    }

    private void changeLookAndFeel(String className) {
        try {
            UIManager.setLookAndFeel(className);
            SwingUtilities.updateComponentTreeUI(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        String actionCommand = e.getActionCommand();

        if (actionCommand.equals("new")) {
            editManager.newCanvas();
        } else if (actionCommand.equals("open file")) {
            lodeSavePicture.openImageFromComputer();

        } else if (actionCommand.equals("open by description")) {

            try {
                imageByDescription();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }


        } else if (actionCommand.equals("png") || actionCommand.equals("jpg")) {
            lodeSavePicture.saveImageToFile(actionCommand);

        } else if (actionCommand.equals("exit")) {
            System.exit(0);
        }
    }


    private JMenu createMenuItem(String typeAction, Image image) throws IOException {
        Image itemImage1 = null;
        String item1Str = "";
        Image itemImage2 = null;
        String item2Str = "";
        if (typeAction.equals("open")) {
            itemImage1 = ImageIO.read(getClass().getResourceAsStream("/icons/new folder.png"));
            item1Str = "open file";
            itemImage2 = ImageIO.read(getClass().getResourceAsStream("/icons/cloud.png"));
            item2Str = "open by description";

        } else {
            item1Str = "png";
            itemImage1 = ImageIO.read(getClass().getResourceAsStream("/icons/png.png"));
            item2Str = "jpg";
            itemImage2 = ImageIO.read(getClass().getResourceAsStream("/icons/jpg.png"));
        }
        itemImage1 = itemImage1.getScaledInstance(40, 40, Image.SCALE_SMOOTH);
        itemImage2 = itemImage2.getScaledInstance(40, 40, Image.SCALE_SMOOTH);
        JMenuItem Item1 = new JMenuItem(item1Str, new ImageIcon(itemImage1));
        Item1.addActionListener(this);
        Item1.setFont(new Font("Arial", Font.BOLD, 20));

        JMenuItem Item2 = new JMenuItem(item2Str, new ImageIcon(itemImage2));
        Item2.addActionListener(this);
        Item2.setFont(new Font("Arial", Font.BOLD, 20));


        JMenu menu = new JMenu(typeAction);
        menu.setIcon(new ImageIcon(image));
        menu.setFont(new Font("Arial", Font.BOLD, 20));
        menu.add(Item1);
        menu.add(Item2);

        return menu;
    }

    private void imageByDescription() throws IOException {
        JTextField imageByDescription = new JTextField();
        Font fontDesc = new Font("Arial", Font.BOLD, 15);
        imageByDescription.setFont(fontDesc);

        imageByDescription.setText("Enter a picture name");

        imageByDescription.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                if (imageByDescription.getText().equals("Enter a picture name"))
                    imageByDescription.setText("");
            }
        });

        int option = JOptionPane.showConfirmDialog(
                this, imageByDescription, "Enter Text", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        // Handle user input from dialog
        if (option == JOptionPane.OK_OPTION) {
            String inputText = imageByDescription.getText();
            JOptionPane.showMessageDialog(null, "The maximum time for uploading " + inputText + " is up to 2 minutes", "upload", JOptionPane.INFORMATION_MESSAGE);
            ChooserImage chooserImage = new ChooserImage(inputText, lodeSavePicture);
            chooserImage.showImagesMenu();
        }
    }

    public static void setTextField(int textW, int textH) {
        if (widthLabel != null && heightLabel != null) {
            widthLabel.setText(String.valueOf(textW));
            heightLabel.setText(String.valueOf(textH));
        }
    }

}
