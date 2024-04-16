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

public class imageController {
    private final List<Image> images = new ArrayList<>();
    private final List<ImageView> imageViews = new ArrayList<>();
    private volatile boolean continueSlideshow = true;
    private int currentImageIndex = 0;
    private Timeline timeline;

    @FXML
    public Label filenameLabel;
    @FXML
    Parent root;

    @FXML
    private ImageView imageView1;

    @FXML
    private ImageView imageView2;

    @FXML
    private ImageView imageView3;

    @FXML
    private void initialize() {
        imageViews.add(imageView1);
        imageViews.add(imageView2);
        imageViews.add(imageView3);

        timeline = new Timeline(new KeyFrame(Duration.seconds(3), this::handleAutoSlideshow));
        timeline.setCycleCount(Timeline.INDEFINITE);
    }

    private void handleAutoSlideshow(ActionEvent event) {
        Platform.runLater(() -> {
            updateDisplayedImages();
            currentImageIndex = (currentImageIndex + 1) % images.size();
            displayImages();
        });
    }

    @FXML
    private void handleBtnLoadAction() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select image files");
        fileChooser.getExtensionFilters().add(new ExtensionFilter("Images",
                "*.png", "*.jpg", "*.gif", "*.tif", "*.bmp"));
        List<File> files = fileChooser.showOpenMultipleDialog(new Stage());

        if (files != null && !files.isEmpty()) {
            files.forEach((File f) -> {
                images.add(new Image(f.toURI().toString()));
            });
            displayImages();
        }
    }

    @FXML
    private void handleBtnPreviousAction() {
        if (!images.isEmpty()) {
            currentImageIndex = (currentImageIndex - 1 + images.size()) % images.size();
            displayImages();
        }
    }

    @FXML
    private void handleBtnNextAction() {
        if (!images.isEmpty()) {
            currentImageIndex = (currentImageIndex + 1) % images.size();
            displayImages();
        }
    }

    private void updateDisplayedImages() {
        int startIndex = currentImageIndex;
        for (int i = 0; i < 3; i++) {
            Image image = images.get((startIndex + i) % images.size());
            imageViews.get(i).setImage(image);
        }
    }

    private void displayImages() {
        updateDisplayedImages();
        String filename = new File(images.get(currentImageIndex).getUrl()).getName();
        filenameLabel.setText("Filename: " + filename);
    }

    @FXML
    private void handleBtnStartSlideshowAction() {
        timeline.play();
    }

    @FXML
    private void handleBtnStopSlideshowAction() {
        timeline.stop();
    }
}
