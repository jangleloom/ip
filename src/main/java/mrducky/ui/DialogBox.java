package mrducky.ui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

/**
 * Represents a dialog box consisting of text and an image.
 */
public class DialogBox extends HBox {

    private final Label text;
    private final ImageView displayPicture;

    /**
     * Creates a dialog box with the given text and image.
     *
     * @param text Dialog text.
     * @param image Dialog image.
     */
    private DialogBox(String text, Image image) {
        assert text != null : "Text cannot be null";
        assert image != null : "Image cannot be null";
        
        this.text = new Label(text);
        displayPicture = new ImageView(image);

        this.text.setWrapText(true);
        this.text.setMaxWidth(250.0);
        this.text.setStyle("-fx-background-color: #E6E6E6; -fx-padding: 6; -fx-background-radius: 6;");
        displayPicture.setFitWidth(100.0);
        displayPicture.setFitHeight(100.0);
        this.setAlignment(Pos.TOP_RIGHT);
        this.setSpacing(8);
        this.setPadding(new Insets(5, 10, 5, 10));

        this.getChildren().addAll(this.text, this.displayPicture);
    }

    /**
     * Flips the dialog box so the image is on the left and text on the right.
     */
    private void flip() {
        this.setAlignment(Pos.TOP_LEFT);
        ObservableList<Node> tmp = FXCollections.observableArrayList(this.getChildren());
        FXCollections.reverse(tmp);
        this.getChildren().setAll(tmp);
    }

    /**
     * Creates a dialog box for user input (image on the right).
     *
     * @param text Dialog text.
     * @param image Dialog image.
     * @return DialogBox aligned to the right.
     */
    public static DialogBox getUserDialog(String text, Image image) {
        return new DialogBox(text, image);
    }

    /**
     * Creates a dialog box for MrDucky responses (image on the left).
     *
     * @param text Dialog text.
     * @param image Dialog image.
     * @return DialogBox aligned to the left.
     */
    public static DialogBox getMrDuckyDialog(String text, Image image) {
        DialogBox dialog = new DialogBox(text, image);
        dialog.flip();
        return dialog;
    }
}
