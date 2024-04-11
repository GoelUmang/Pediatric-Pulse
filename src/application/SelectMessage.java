package application;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class SelectMessage
{
	
	private String username;
	private String userType;
    
    public SelectMessage(String username, String userType)
	{
        this.username = username;
        this.userType = userType;
    }

    public Scene createScene()
    {
        // Create buttons
        Button doctorButton = new Button("Message to Doctor");
        Button nurseButton = new Button("Message to Nurse");

        // Set button styles
        doctorButton.setStyle("-fx-font-weight: bold;");
        nurseButton.setStyle("-fx-font-weight: bold;");

        // Set button sizes
        doctorButton.setPrefWidth(200);
        doctorButton.setPrefHeight(59);
        nurseButton.setPrefWidth(200);
        nurseButton.setPrefHeight(59);

        // Set action for "Message to Doctor" button
        doctorButton.setOnAction(e -> {
            SendMessage sendMessage = new SendMessage(username, userType);
            Stage stage = new Stage();
            stage.setScene(sendMessage.createScene());
            stage.show();
        });

        // Set action for "Message to Nurse" button
        nurseButton.setOnAction(e -> {
        	 SendMessage sendMessage = new SendMessage(username, userType);
             Stage stage = new Stage();
             stage.setScene(sendMessage.createScene());
             stage.show();
         });

        // Create layout
        VBox root = new VBox(20);
        root.setAlignment(Pos.CENTER);
        root.setBackground(new Background(new BackgroundFill(Color.DARKGRAY, CornerRadii.EMPTY, Insets.EMPTY)));
        root.getChildren().addAll(doctorButton, nurseButton);
        root.setPadding(new Insets(10));

        // Create a scene
        return new Scene(root, 540, 440);
    }
}