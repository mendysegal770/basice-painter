package paint;

import org.json.JSONArray;
import org.json.JSONObject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Random;
import javax.imageio.ImageIO;

public class ChooserImage extends JFrame {


    String description = "";
    Lode_Save_picture lodeSavePicture;


    public ChooserImage(String description, Lode_Save_picture lodeSavePicture) throws IOException {
        this.description = description;
        this.lodeSavePicture = lodeSavePicture;
        setTitle("Chooser favorite Image");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 600);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(3, 3, 0, 0));

    }

    public void showImagesMenu() throws IOException {
        JSONArray results = getImageUrlByDescription(description);
        for (int i = 0; i < 10 - 1; i++) {
            JSONObject result = results.getJSONObject(i);
            String imageUrl = result.getJSONObject("urls").getString("full");
            URL url = new URL(imageUrl);
            Image originalImage = ImageIO.read(url);

            int buttonWidth = 300;
            int buttonHeight = 200;
            Image scaledImage = originalImage.getScaledInstance(buttonWidth, buttonHeight, Image.SCALE_SMOOTH);

            ImageIcon icon = new ImageIcon(scaledImage);
            JButton iconButton = new JButton();
            iconButton.setIcon(icon);
            iconButton.setPreferredSize(new Dimension(300, 200));
            add(iconButton);
            iconButton.addActionListener(e -> {
                try {
                    lodeSavePicture.getImageByDescription(imageUrl);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                dispose();
            });

        }
        setVisible(true);
    }

    private JSONArray getImageUrlByDescription(String description) throws IOException {
        String accessKey = ""; // Replace with your Unsplash API access key

        URL url = new URL("https://api.unsplash.com/search/photos?query=" + description + "&client_id=" + accessKey);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String line;
        StringBuilder response = new StringBuilder();
        while ((line = reader.readLine()) != null) {
            response.append(line);
        }
        reader.close();

        JSONObject jsonResponse = new JSONObject(response.toString());
        JSONArray results = jsonResponse.getJSONArray("results");

        return results;
    }


}
