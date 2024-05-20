package paint;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class EditManager {


    private PaintManager paintManager;

    private final List<BufferedImage> images;

    private int index;


    private int percent;

    public void setPaintManager(PaintManager paintManager) {
        this.paintManager = paintManager;
        saveCurrentImage();
    }

    public EditManager() {
        images = new ArrayList<>();
    }

    public void redo() {
        if (index < images.size() - 1) {
            index++;
        }
        paintManager.updatedSizeCanvas(images.get(index), paintManager.resizeWidth, paintManager.resizeHeight);

    }

    public void undo() {
        if (index > 0) {
            index--;
        }
        paintManager.updatedSizeCanvas(images.get(index), paintManager.resizeWidth, paintManager.resizeHeight);
    }

    public void addImage(BufferedImage image) {
        images.add(image);
    }


    public void topIndex() {
        this.index = images.size() - 1;
    }

    public void newCanvas() {
        BufferedImage startCanvas = images.get(0);
        images.clear();
        paintManager.setCanvasImage(startCanvas);
        images.add(startCanvas);
        topIndex();
        paintManager.setMouseStatus("");
        paintManager.setSubCanvasImage(null);
        paintManager.updatedSizeCanvas(paintManager.getCanvasImage(), paintManager.resizeWidth, paintManager.resizeHeight);
    }

    private void saveCurrentImage() {
        paintManager.getCanvasPanel().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                if (paintManager.tool.equals("cut") ) {
                    paintManager.zero();
                } else if (paintManager.tool.startsWith("shape")) {
                    paintManager.paintShape();
                }
                if (!paintManager.tool.equals("cut") && !paintManager.tool.equals("text")) {
                    saveInMemory();
                }
                paintManager.mouseStatus = "released";
                paintManager.getCanvasPanel().repaint();
            }
        });
    }


    public void zoom(int value) {
        percent = value;
        paintManager.setPercent(1 + 0.5 * percent);
        paintManager.resizeWidth = (int) (paintManager.canvasWidth * paintManager.percent);
        paintManager.resizeHeight = (int) (paintManager.canvasHeight * paintManager.percent);
        paintManager.updatedSizeCanvas(paintManager.getCanvasImage(), paintManager.resizeWidth, paintManager.resizeHeight);
    }

    public void saveInMemory() {
        BufferedImage copiedImage = Utils.deepCopy(paintManager.getCanvasImage());
        images.add(copiedImage);
        topIndex();
    }


}





