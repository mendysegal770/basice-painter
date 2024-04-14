package org.example;


import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.util.ArrayList;
import java.util.List;

public class Paint extends JFrame {

    private Color currentColor = Color.MAGENTA;
    private BufferedImage canvasImage;
    private Graphics2D graphics;
    private List<BufferedImage> images;
    int number;
    boolean isNotPainting = false;
    private JPanel canvas;
    int x = 0;
    int y = 0;
    int width = 786;
    int height = 561;


    public Paint() {
        setTitle("Painter");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);

        canvasImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

        graphics = canvasImage.createGraphics();
        graphics.setColor(Color.WHITE);

        graphics.fillRect(x, y, width, height);
        images = new ArrayList<>();
        BufferedImage startCanvas = deepCopy(canvasImage);

        images.add(startCanvas);
        number = images.size() - 1;


        canvas = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                BufferedImage canvasToPaint;
                if (!isNotPainting) {
                    canvasToPaint = canvasImage;
                } else {
                    canvasToPaint = images.get(number);

                }
                g.drawImage(canvasToPaint, x, y, width, height, x - 100,
                        y - 50, width + 200, height + 150, this);

                // g.drawImage(canvasToPaint, 0, 0, this);
            }
        };


        canvas.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (isNotPainting) {
                    canvasImage = deepCopy(images.get(number));
                    graphics = canvasImage.createGraphics();
                }
                isNotPainting = false;
                draw(e.getX(), e.getY());

            }

            public void mouseReleased(MouseEvent e) {
                BufferedImage copiedImage = deepCopy(canvasImage); // Create a deep copy
                images.add(copiedImage);
                number = images.size() - 1;
            }
        });

        canvas.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                draw(e.getX(), e.getY());
            }
        });

        JButton colorButton = new JButton("Choose Color");
        colorButton.addActionListener(e -> chooseColor());

        JButton undoButton = new JButton("Undo");
        undoButton.addActionListener(e -> undo());

        JButton redoButton = new JButton("Redo");
        redoButton.addActionListener(e -> redo());
        JButton reduceButton = new JButton("reduce");
        reduceButton.addActionListener(e -> {
            reduce();
        });

        JPanel controlPanel = new JPanel();
        JPanel controlPanel1 = new JPanel();
        JPanel controlPanel2 = new JPanel();


        controlPanel.add(undoButton);
        controlPanel.add(redoButton);
        add(canvas);
        controlPanel1.add(colorButton);
        controlPanel2.add(reduceButton);
//        add(controlPanel, BorderLayout.SOUTH);
//        add(controlPanel1, BorderLayout.NORTH);
//        add(controlPanel2, BorderLayout.EAST);


    }

    private void draw(int x, int y) {
        graphics.setColor(currentColor);
        graphics.fillRect(x, y, 15, 15);
        repaint();
    }

    private void reduce() {
        x += 100;
        y += 50;
        width -= 200;
        height -= 150;
        repaint();
    }


    private BufferedImage deepCopy(BufferedImage bi) {
        ColorModel cm = bi.getColorModel();
        boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
        WritableRaster raster = bi.copyData(null);
        return new BufferedImage(cm, raster, isAlphaPremultiplied, null);
    }


    private void redo() {
        if (number < images.size() - 1) {
            number++;
        }
        System.out.println(number);
        isNotPainting = true;
        canvas.repaint();
    }

    private void undo() {
        if (number > 0) {
            number--;
        }
        System.out.println(number);
        isNotPainting = true;
        canvas.repaint();
    }


    private void chooseColor() {
        Color selectedColor = JColorChooser.showDialog(this, "Choose a Color", currentColor);
        if (selectedColor != null) {
            currentColor = selectedColor;
        }
    }
}
