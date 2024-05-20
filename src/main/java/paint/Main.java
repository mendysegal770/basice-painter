package paint;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        startPaint();


    }

    private static void startPaint() throws IOException {
        EditManager editManager = new EditManager();
        PaintManager paintManager = new PaintManager(editManager);
        editManager.setPaintManager(paintManager);
        Lode_Save_picture lodeSavePicture = new Lode_Save_picture(editManager, paintManager);
        new PaintUI(paintManager, editManager, lodeSavePicture);
    }

}