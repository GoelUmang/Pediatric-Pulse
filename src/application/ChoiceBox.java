package application;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class ChoiceBox
{
    private VBox root;
    private String username;
	private String userType;
    
    public ChoiceBox(String username, String userType)
	{
        this.username = username;
        this.userType = userType;
    }

    public Scene createScene()
    {
        // Create buttons
        Button sendMessageButton = new Button("Send Message");
        sendMessageButton.setStyle("-fx-font-weight: bold;");
        Button inboxButton = new Button("Inbox");
        inboxButton.setStyle("-fx-font-weight: bold;");

        // Set the size for buttons
        sendMessageButton.setPrefWidth(200);
        sendMessageButton.setPrefHeight(59);
        inboxButton.setPrefWidth(200);
        inboxButton.setPrefHeight(59);

        // Set action for SendMessage button
        sendMessageButton.setOnAction(e -> {
            SendMessage sendMessage = new SendMessage(username, userType);
            Scene sendMessageScene = sendMessage.createScene();
            Stage primaryStage = (Stage) sendMessageButton.getScene().getWindow();
            primaryStage.setScene(sendMessageScene);
            primaryStage.show();
        });

        // Set action for Inbox button
        inboxButton.setOnAction(e -> {
            MessageInbox messageInbox = new MessageInbox(username, userType);
            Scene inboxScene = messageInbox.createScene();
            Stage primaryStage = (Stage) inboxButton.getScene().getWindow();
            primaryStage.setScene(inboxScene);
            primaryStage.show();
        });

        // Create layout
        root = new VBox(20);
        root.setAlignment(Pos.CENTER); // Center align the buttons by default
        root.setBackground(new Background(new BackgroundFill(Color.DARKGRAY, CornerRadii.EMPTY, javafx.geometry.Insets.EMPTY)));
        root.getChildren().addAll(sendMessageButton, inboxButton);

        // Set up the scene
        return new Scene(root, 540, 440);
    }

    // Method to set the alignment of the buttons
    public void setButtonAlignment(Pos alignment)
    {
        root.setAlignment(alignment);
    }

    // Method to set the spacing between the buttons
    public void setButtonSpacing(double spacing)
    {
        root.setSpacing(spacing);
    }
}