package application;

import java.sql.*;
import java.util.Optional;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Patient
{
	private String username;
	private String userType;
	
	public Patient(String username, String userType)
	{
        this.username = username;
        this.userType = userType;
    }

    public Scene createScene()
    {
    	Stage primaryStage = new Stage();
    	
    	String patientName = getPatientFirstName(username);
        PatientOldRecords oldRecords = new PatientOldRecords(patientName);
    	
        // Header section
        BorderPane headerPane = new BorderPane();
        headerPane.setPadding(new Insets(10, 20, 10, 20));

        Label lblPediatricPulse = new Label("PEDIATRIC PULSE");
        lblPediatricPulse.setStyle("-fx-text-fill: white; -fx-font-size: 40px;");
        Label lblWelcome = new Label("Welcome, "+ patientName);
        lblWelcome.setStyle("-fx-text-fill: white; -fx-font-size: 26px;");
        Button btnLogout = new Button("Logout");
        btnLogout.setOnAction(event -> {
            Login login = new Login(primaryStage);
            primaryStage.setScene(login.createScene());
            primaryStage.show();
        });

        HBox leftHeader = new HBox(lblPediatricPulse);
        HBox rightHeader = new HBox(lblWelcome, btnLogout);
        rightHeader.setSpacing(10);

        headerPane.setLeft(leftHeader);
        headerPane.setRight(rightHeader);
        headerPane.setStyle("-fx-background-color: #0e70ab;");

        Image image = new Image("file:/Users/sakethkoyi/Downloads/WhatsApp Image 2024-04-07 at 18.30.17.jpeg"); // Replace with the correct path to your image
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(300); // Set the width of the image
        imageView.setFitHeight(300); // Set the height of the image
        imageView.setPreserveRatio(true);

        HBox imageBox = new HBox(imageView);
        imageBox.setAlignment(Pos.CENTER);

        // Buttons Section
        HBox buttonRow1 = new HBox(10);
        buttonRow1.setAlignment(Pos.CENTER);
        Button EditContactInfo = new Button("Edit Contact Info");
        EditContactInfo.setOnAction(event -> {
            TextInputDialog dialog = new TextInputDialog();
            dialog.setTitle("Edit Contact Info");
            dialog.setHeaderText("Update your contact number");
            dialog.setContentText("Please enter your new contact number:");

            Optional<String> result = dialog.showAndWait();
            result.ifPresent(phoneNumber -> {
                updatePhoneNumber(username, phoneNumber);
            });
        });
        
        
        
        Button viewRecordsButton = new Button("View Past Medical Records");
        
        buttonRow1.getChildren().addAll(EditContactInfo, viewRecordsButton);

        HBox buttonRow2 = new HBox(10);
        buttonRow2.setAlignment(Pos.CENTER);
        Button messageDoctorButton = new Button("Messages");
        
        buttonRow2.getChildren().addAll(messageDoctorButton);

        // Apply the same size to all buttons, if desired
        buttonRow1.getChildren().forEach(button -> {
            ((Button) button).setPrefSize(600, 70); // Set preferred width and height
        });
        buttonRow2.getChildren().forEach(button -> {
            ((Button) button).setPrefSize(600, 70); // Set preferred width and height
        });

        // Style buttons
        styleButton(EditContactInfo);
        styleButton(viewRecordsButton);
        styleButton(messageDoctorButton);
        

        VBox buttonLayout = new VBox(10);
        buttonLayout.setAlignment(Pos.CENTER);
        buttonLayout.getChildren().addAll(imageBox, buttonRow1, buttonRow2);

        // Main Layout
        VBox mainLayout = new VBox(10);
        mainLayout.setAlignment(Pos.TOP_CENTER);
        mainLayout.getChildren().addAll(headerPane, buttonLayout);
        mainLayout.setPadding(new Insets(20));
        mainLayout.setStyle("-fx-background-color: #DCDCDC; " +
                "-fx-font-family: 'Times New Roman'; " +     // Set the font family (optional)
                "-fx-font-size: 16px; " +          // Set the font size (optional)
                "-fx-font-weight: bold;");         // Set the font weight to bold
        
        // View Past Medical Records button action
        viewRecordsButton.setOnAction(event -> {
            Scene oldRecordsscene = oldRecords.createScene();
            primaryStage.setScene(oldRecordsscene);
            primaryStage.show();
        });
       
        // Message Your Doctor button action
        messageDoctorButton.setOnAction(event -> {
            ChoiceBox choiceBox = new ChoiceBox(username, userType);
            Scene choiceBoxScene = choiceBox.createScene();
            primaryStage.setScene(choiceBoxScene);
            primaryStage.show();
        });
        
        // Set up the scene
        Scene scene = new Scene(mainLayout, 1380, 668); // Set window size
        primaryStage.setTitle("Patient View");
        primaryStage.setScene(scene);
        return scene;
       
    }
    
    private void updatePhoneNumber(String username, String newPhoneNumber) {
        String updateQuery = "UPDATE users SET phone_number = ? WHERE username = ?";
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(updateQuery)) {
            
            pstmt.setString(1, newPhoneNumber);
            pstmt.setString(2, username);
            
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Update Successful");
                alert.setHeaderText(null);
                alert.setContentText("Your contact information has been updated successfully.");
                alert.showAndWait();
            } else {
                System.out.println("Failed to update phone number");
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Update Failed");
                alert.setHeaderText(null);
                alert.setContentText("There was an error updating your contact information. Please try again.");
                alert.showAndWait();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Update Failed");
            alert.setHeaderText(null);
            alert.setContentText("There was an error updating your contact information. Please try again.");
            alert.showAndWait();
        }
    }
    
    private void styleButton(Button button)
    {
        button.setStyle("-fx-background-color: #F5DEB3; " + // Background color
                "-fx-border-color: black; " +       // Border color
                "-fx-border-width: 2px; " +         // Border width
                "-fx-border-radius: 5px; " +        // Border radius for rounded corners
                "-fx-background-radius: 5px;");    // Background radius for rounded corners
        button.setPrefSize(600, 70); // Set preferred width and height
    }
    
    private String getPatientFirstName(String username)
    {
    	String firstName = "";
        String query = "SELECT first_name FROM users WHERE username = ?";

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                firstName = rs.getString("first_name");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Failed to retrieve the patient's first name");
        }

        return firstName;
    }
}