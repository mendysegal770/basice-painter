package paint;



import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;

public class Lode_Save_picture {
    PaintManager paintManager;
    EditManager editManager;

    public Lode_Save_picture(EditManager editManager, PaintManager paintManager) {
        this.paintManager = paintManager;
        this.editManager = editManager;
    }

    public void openImageFromComputer() {
        JFileChooser fileChooser = new JFileChooser();


        String userHome = System.getProperty("user.home");
        String screenshotsPath = userHome + File.separator + "Pictures" + File.separator + "Saved Pictures";
        File screenshotsDir = new File(screenshotsPath);

        if (screenshotsDir.exists() && screenshotsDir.isDirectory()) {
            fileChooser.setCurrentDirectory(screenshotsDir);
        }


        FileNameExtensionFilter filter = new FileNameExtensionFilter(
                "Image Files", "jpg", "jpeg", "png", "gif");
        fileChooser.setFileFilter(filter);

        int result = fileChooser.showOpenDialog(null);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            try {

                BufferedImage convertedImage = convertToARGB(ImageIO.read(selectedFile));

                paintManager.setCanvasImage(convertedImage);
                BufferedImage copiedImage = Utils.deepCopy(paintManager.getCanvasImage()); // Create a deep copy
                editManager.addImage(copiedImage);
                editManager.topIndex();
                paintManager.updatedSizeCanvas(paintManager.getCanvasImage(), paintManager.resizeWidth, paintManager.resizeHeight);

            } catch (IOException ex) {
                JOptionPane.showMessageDialog(null,
                        "Error loading image: " + ex.getMessage(),
                        "Image Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public void getImageByDescription(String imageFavorite) throws IOException {

        URL url = new URL(imageFavorite);
        BufferedImage convertedImage = convertToARGB(ImageIO.read(url));
        paintManager.setCanvasImage(convertedImage);
        BufferedImage copiedImage = Utils.deepCopy(paintManager.getCanvasImage()); // Create a deep copy
        editManager.addImage(copiedImage);
        editManager.topIndex();
        paintManager.updatedSizeCanvas(paintManager.getCanvasImage(), paintManager.resizeWidth, paintManager.resizeHeight);

    }


    public void saveImageToFile(String typeFile) {

        JFileChooser fileChooser = new JFileChooser();
        String userHome = System.getProperty("user.home");
        String screenshotsPath = userHome + File.separator + "Pictures" + File.separator + "Saved Pictures";
        File screenshotsDir = new File(screenshotsPath);

        if (screenshotsDir.exists() && screenshotsDir.isDirectory()) {
            fileChooser.setCurrentDirectory(screenshotsDir);
        }
        fileChooser.setDialogTitle("Save Image");
        int userSelection = fileChooser.showSaveDialog(null);
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();
            String filePath = fileToSave.getAbsolutePath();

            String format;
            String extension;

            if (typeFile.equals("png")) {
                format = "png";
                extension = ".png";
            } else {
                format = "jpg";
                extension = ".jpg";
            }

            if (!filePath.toLowerCase().endsWith(extension)) {
                fileToSave = new File(filePath + extension);
            }

            try {
                ImageIO.write(paintManager.getCanvasImage(), format, fileToSave);
                JOptionPane.showMessageDialog(null, "Image saved successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null, "Error saving image: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }


    private BufferedImage convertToARGB(BufferedImage image) {
        if (image.getType() == BufferedImage.TYPE_INT_ARGB) {
            return image;
        }

        BufferedImage argbImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = argbImage.createGraphics();
        g2d.drawImage(image, 0, 0, null);

        return argbImage;
    }


}

