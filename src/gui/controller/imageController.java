package gui.controller;

import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import javafx.util.Duration;

public class imageController
{
    private final List<Image> images = new ArrayList<>();
    private volatile boolean continueSlideshow = true;
    private int currentImageIndex = 0;
    private Thread slideshowThread;
    @FXML
    public Label filenameLabel;
    @FXML
    Parent root;

    @FXML
    private ImageView imageView;


    private void handleAutoSlideshow(ActionEvent event) {
        Platform.runLater(() -> {
            currentImageIndex = (currentImageIndex + 1) % images.size();
            displayImage();
        });
    }

    @FXML
    private void handleBtnLoadAction()
    {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select image files");
        fileChooser.getExtensionFilters().add(new ExtensionFilter("Images",
                "*.png", "*.jpg", "*.gif", "*.tif", "*.bmp"));
        List<File> files = fileChooser.showOpenMultipleDialog(new Stage());

        if (!files.isEmpty())
        {
            files.forEach((File f) ->
            {
                images.add(new Image(f.toURI().toString()));
            });
            displayImage();
        }
    }

    @FXML
    private void handleBtnPreviousAction()
    {
        if (!images.isEmpty())
        {
            currentImageIndex =
                    (currentImageIndex - 1 + images.size()) % images.size();
            displayImage();
        }
    }

    @FXML
    private void handleBtnNextAction()
    {
        if (!images.isEmpty())
        {
            currentImageIndex = (currentImageIndex + 1) % images.size();
            displayImage();
        }
    }

    private void displayImage()
    {

        if (!images.isEmpty())
        {
            imageView.setImage(images.get(currentImageIndex));
            String filename = new File(images.get(currentImageIndex).getUrl()).getName();
            filenameLabel.setText("Filename: " + filename);
        }
    }
    @FXML
    private void handleBtnStartSlideshowAction() {
        slideshowThread = new Thread(() -> {
            while (continueSlideshow) {
                try {
                    Thread.sleep(3000);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                handleAutoSlideshow(null);
            }
        });
        slideshowThread.setDaemon(true);
        slideshowThread.start();
    }

    @FXML
    private void handleBtnStopSlideshowAction() {
        continueSlideshow=false;
        if (slideshowThread != null) {
            slideshowThread.interrupt();
        }
    }


}