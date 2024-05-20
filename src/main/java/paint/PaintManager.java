package paint;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;

import static java.awt.Color.*;

public class PaintManager {

    private BufferedImage canvasImage;

    public void setSubCanvasImage(BufferedImage subCanvasImage) {
        this.subCanvasImage = subCanvasImage;
    }

    private BufferedImage subCanvasImage;
    private Graphics2D graphics;
    private final JPanel canvasPanel;
    private JPanel mainPanel = new JPanel();
    int canvasWidth = 300;
    int canvasHeight = 250;
    int resizeWidth = 300;
    int resizeHeight = 250;
    double percent = 1;
    int drawWidth = 5;
    int drawHeight = 5;
    int redrawWidth = 5;
    int redrawHeight = 5;
    float shapeWidth = 0.5f;
    float firstShapeWidth = 0.5f;
    Color currentColor = BLACK;
    private int mouseX, mouseY;
    JLabel pixel = new JLabel("");
    private EditManager editManager;
    boolean isEntered;
    JTextArea textArea = new JTextArea();
    String tool = "pen";
    String mouseStatus = "";
    int xPressed, yPressed, xCurrent, yCurrent, xLocation, yLocation, widthCut, heightCut;




    public PaintManager(EditManager editManager) {
        this.editManager = editManager;
        canvasImage = new BufferedImage(canvasWidth, canvasHeight, BufferedImage.TYPE_INT_ARGB);
        graphics = canvasImage.createGraphics();

        mainPanel.setBackground(GRAY);

        textArea.setBackground(lightGray);
        textArea.setFont(new Font("Arial", Font.BOLD, 25));



        BufferedImage startCanvas = Utils.deepCopy(canvasImage);
        editManager.addImage(startCanvas);
        editManager.topIndex();

        pixel.setFont(new Font("Arial", Font.BOLD, 20));


        canvasPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.setColor(WHITE);
                g.fillRect(0, 0, resizeWidth, resizeHeight);
                g.drawImage(canvasImage, 0, 0, null);
                g.setColor(currentColor);
                Graphics2D g2d = (Graphics2D) g;


                if (tool.startsWith("shape") && mouseStatus.equals("pressed")) {
                    int widthShape = Math.abs(xCurrent - xPressed);
                    int heightShape = Math.abs(yCurrent - yPressed);
                    g2d.setStroke(new BasicStroke(shapeWidth));
                    if (tool.contains("circle")) {
                        g.drawOval(xLocation, yLocation, widthShape, heightShape);
                    } else if (tool.contains("rect")) {
                        g.drawRect(xLocation, yLocation, widthShape, heightShape);
                    } else {
                        g.drawLine(xPressed, yPressed, xCurrent, yCurrent);
                    }
                } else if (tool.equals("cut")) {
                    g2d.setStroke(new BasicStroke(3.0f));
                    g.setColor(GRAY);
                    if (mouseStatus.equals("pressed")) {
                        widthCut = Math.abs(xCurrent - xPressed);
                        heightCut = Math.abs(yCurrent - yPressed);
                        g.drawRect(xLocation, yLocation, widthCut, heightCut);
                    } else if (mouseStatus.equals("released")) {
                        g.setColor(RED);
                        g.drawRect(xLocation + (mouseX - xPressed), yLocation + (mouseY - yPressed), widthCut, heightCut);
                        xCurrent = xLocation + (mouseX - xPressed);
                        yCurrent = yLocation + (mouseY - yPressed);
                        if (subCanvasImage != null) {
                            g.drawImage(subCanvasImage, xCurrent, yCurrent, null);
                        }
                    }
                } else if (tool.equals("text")) {
                    textArea.setForeground(currentColor);
                    if (mouseStatus.equals("pressed")) {
                        widthCut = Math.abs(xCurrent - xPressed);
                        heightCut = Math.abs(yCurrent - yPressed);
                        textArea.setBounds(xLocation, yLocation, widthCut, heightCut);
                        canvasPanel.add(textArea);

                    }
                } else if (tool.equals("pen") || tool.equals("eraser")) {
                    if (isEntered) {
                        if (tool.equals("pen")) {
                            g.fillOval(mouseX - drawWidth / 2, mouseY - drawHeight / 2, drawWidth, drawHeight);
                        } else {
                            g.setColor(LIGHT_GRAY);
                            g.fillRect((mouseX - drawWidth / 2) - 3, (mouseY - drawHeight / 2) - 3, drawWidth + 6, drawHeight + 6);
                            g.setColor(WHITE);
                            g.fillRect(mouseX - drawWidth / 2, mouseY - drawHeight / 2, drawWidth, drawHeight);
                        }
                    }
                }
            }
        };
        canvasPanel.setPreferredSize(new Dimension(resizeWidth, resizeHeight));
        canvasPanel.setLayout(null);

        canvasPanel.addMouseListener(new MouseAdapter() {


            @Override
            public void mouseEntered(MouseEvent e) {
                if (tool.equals("pen") || tool.equals("eraser")) {
                    isEntered = true;
                    updateMousePosition(e);
                    canvasPanel.repaint();
                }
                setLabelText(e.getX(), e.getY());
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (tool.equals("pen") || tool.equals("eraser")) {
                    isEntered = false;
                    canvasPanel.repaint();
                }
                pixel.setText("");
            }

            @Override
            public void mousePressed(MouseEvent e) {
                if (tool.equals("cut") || tool.startsWith("shape") || tool.startsWith("text")) {
                    if (!mouseStatus.equals("released") || isOutOfBorders(e)) {
                        if (widthCut > 0 && heightCut > 0) {
                            if (tool.equals("cut")) {
                                paintTheCut();
                            } else if (tool.equals("text")) {
                                paintTheText();
                            }
                        }
                        mouseStatus = "pressed";
                        xLocation = xCurrent = xPressed = e.getX();
                        yLocation = yCurrent = yPressed = e.getY();
                        canvasPanel.repaint();
                        subCanvasImage = null;
                    } else {
                        xPressed = e.getX();
                        yPressed = e.getY();
                    }
                } else if (tool.equals("pen") || tool.equals("eraser")) {
                    draw(e.getX(), e.getY());

                }
                updateMousePosition(e);

            }
        });

        canvasPanel.addMouseMotionListener(new MouseMotionAdapter() {

            @Override
            public void mouseDragged(MouseEvent e) {
                int x, y;
                x = e.getX();
                y = e.getY();

                if ((tool.startsWith("shape") || tool.equals("cut") || tool.equals("text")) && mouseStatus.equals("pressed")) {
                    if (x < xPressed)
                        xLocation = x;
                    if (y < yPressed)
                        yLocation = y;
                    xCurrent = x;
                    yCurrent = y;

                } else if (tool.equals("pen") || tool.equals("eraser")) {
                    draw(x, y);
                }
                updateMousePosition(e);
                canvasPanel.repaint();

                setLabelText(x, y);
            }

            @Override
            public void mouseMoved(MouseEvent e) {
                if (tool.equals("pen") || tool.equals("eraser")) {
                    updateMousePosition(e);
                    canvasPanel.repaint();
                }
                setLabelText(e.getX(), e.getY());
            }
        });

        mainPanel.add(canvasPanel);
    }

    public void zero() {
        this.xPressed = yPressed = mouseX = mouseY = 0;
        if (mouseStatus.equals("released")) {
            xLocation = xCurrent;
            yLocation = yCurrent;
        } else if (widthCut > 0 && heightCut > 0) {
            subCanvasImage = Utils.deepCopy(canvasImage);
            subCanvasImage = subCanvasImage.getSubimage(xLocation, yLocation, widthCut, heightCut);
            deleteBackend();
        }
    }

    private void paintTheCut() {
        graphics.drawImage(subCanvasImage, xLocation, yLocation, null);
        editManager.saveInMemory();
    }

    private void paintTheText() {
        graphics.setColor(currentColor);
        graphics.setFont(new Font("Arial", Font.BOLD, 20));

        paintWithEnters();
        editManager.saveInMemory();
        canvasPanel.remove(textArea);
        textArea = new JTextArea();
        textArea.setBackground(lightGray);
        textArea.setFont(new Font("Arial", Font.BOLD, 25));
        canvasPanel.revalidate();
        canvasPanel.repaint();
    }


    private void draw(int x, int y) {
        if (tool.equals("pen")) {
            graphics.setColor(currentColor);
            graphics.fillOval(x - drawWidth / 2, y - drawHeight / 2, drawWidth, drawHeight);
        } else {
            graphics.setColor(WHITE);
            graphics.fillRect(x - drawWidth / 2, y - drawHeight / 2, drawWidth, drawHeight);
        }
        canvasPanel.repaint();
    }


    public JPanel getCanvasPanel() {
        return canvasPanel;
    }


    public JLabel getPixel() {
        return pixel;
    }

    public void setLabelText(int x, int y) {
        if (x >= 0 && x <= resizeWidth && y >= 0 && y <= resizeHeight) {
            pixel.setText(x + "," + y + ": px");
        } else {
            pixel.setText("");
        }
    }

    public BufferedImage getCanvasImage() {
        return canvasImage;
    }

    public void resizeInkByComboBox(int value) {
        redrawWidth = value;
        redrawHeight = value;
        resizeInkBySizeCanvas(redrawWidth * percent, redrawHeight * percent);
    }

    public void resizeShapeThicknessByComboBox(float value) {
        this.firstShapeWidth = value;
        resizeShapeWidth((float) (firstShapeWidth * percent));
    }

    public void resizeInkBySizeCanvas(double updWidth, double updHeight) {
        drawWidth = (int) (updWidth);
        drawHeight = (int) (updHeight);
    }


    public void setCurrentColor(Color currentColor) {
        this.currentColor = currentColor;

        canvasPanel.revalidate();
        canvasPanel.repaint();
    }


    public JPanel getMainPanel() {
        return mainPanel;
    }

    public void updatedSizeCanvas(BufferedImage toDraw, int updWi, int updHe) {
        BufferedImage resizedImage = new BufferedImage(updWi, updHe, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2dResized = resizedImage.createGraphics();
        g2dResized.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2dResized.drawImage(toDraw, 0, 0, updWi, updHe,0,0,toDraw.getWidth(),toDraw.getHeight(), null);
        g2dResized.dispose();

        canvasPanel.setPreferredSize(new Dimension(resizeWidth, resizeHeight));
        canvasImage = (resizedImage);
        graphics = canvasImage.createGraphics();
        resizeInkBySizeCanvas(redrawWidth * percent, redrawHeight * percent);
        resizeShapeWidth((int) (firstShapeWidth * percent));
        canvasPanel.revalidate();
        canvasPanel.repaint();
        PaintUI.setTextField(resizeWidth, resizeHeight);
    }

    private void resizeShapeWidth(float newWidth) {
        this.shapeWidth = newWidth;
    }

    public void setPercent(double percent) {
        this.percent = percent;
    }


    public void setCanvasImage(BufferedImage canvasImage) {
        this.canvasImage = canvasImage;
    }

    private void updateMousePosition(MouseEvent e) {
        mouseX = e.getX();
        mouseY = e.getY();
    }

    public void paintShape() {
        graphics.setStroke(new BasicStroke(shapeWidth));
        graphics.setColor(currentColor);
        if (tool.contains("circle")) {
            graphics.drawOval(xLocation, yLocation, Math.abs(xCurrent - xPressed), Math.abs(yCurrent - yPressed));
        } else if (tool.contains("rect")) {
            graphics.drawRect(xLocation, yLocation, Math.abs(xCurrent - xPressed), Math.abs(yCurrent - yPressed));
        } else {
            graphics.drawLine(xPressed, yPressed, xCurrent, yCurrent);
        }
        canvasPanel.repaint();
    }

    public void eraser() {
        tool = "eraser";
    }

    public void pen() {
        tool = "pen";

    }

    public void shapes(String type) {
        tool = "shape" + type;
    }

    public void text() {
        tool = "text";
    }

    public void cut() {
        this.tool = "cut";
        this.mouseStatus = "";
    }


    private boolean isOutOfBorders(MouseEvent e) {
        return (e.getX() < xCurrent || e.getX() > xCurrent + widthCut || e.getY() < yCurrent || e.getY() > yCurrent + heightCut);
    }

    private void deleteBackend() {
        graphics.setColor(WHITE);
        graphics.fillRect(xLocation, yLocation, widthCut, heightCut);
    }

    public void setMouseStatus(String mouseStatus) {
        this.mouseStatus = mouseStatus;
    }

    private void paintWithEnters() {
        String input = textArea.getText();

        String enter = "[\n\\s]";
        String[] strings = input.split(enter);
        for (String string : strings) {
            graphics.drawString(string, xLocation, yLocation);
            yLocation += 25;
        }

    }


}
