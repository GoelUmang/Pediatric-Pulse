package application;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.sql.*;

public class SendMessage
{
    private String username;
    //private String userType;

    public SendMessage(String username, String userType)
    {
        this.username = username;
        //this.userType = userType;
    }

    public Scene createScene() {
        Stage primaryStage = new Stage();
        primaryStage.setTitle("Message Box");

        // Text area to display messages
        TextArea messageArea = new TextArea();
        messageArea.setEditable(false);
        messageArea.setPrefHeight(300);

        // Text field for entering the recipient's first name
        TextField recipientNameField = new TextField();
        recipientNameField.setPromptText("Recipient's first name");

        // ComboBox for selecting the recipient's role
        ComboBox<String> roleComboBox = new ComboBox<>();
        roleComboBox.getItems().addAll("Doctor", "Nurse", "Patient");
        roleComboBox.setPromptText("Select Recipient's Role");

        // Text field for entering messages
        TextField inputField = new TextField();
        inputField.setPromptText("Type your message here...");

        // Send button
        Button sendButton = new Button("Send");
        sendButton.setOnAction(e -> {
            String recipientFirstName = recipientNameField.getText();
            String recipientRole = roleComboBox.getValue();
            String message = inputField.getText();

            if (!recipientFirstName.isEmpty() && recipientRole != null && !message.isEmpty())
            {
                // Fetch the recipient username based on the first name and role
                String recipientUsername = getUsernameFromFirstNameAndRole(recipientFirstName, recipientRole);

                if (recipientUsername != null)
                {
                    messageArea.appendText("You: " + message + "\n");
                    sendMessage(username, recipientUsername, message);
                    inputField.clear();
                }
                else
                {
                    messageArea.appendText("Recipient not found.\n");
                }
            }
        });

        VBox layout = new VBox(10);
        layout.setPadding(new Insets(10));
        layout.getChildren().addAll(recipientNameField, roleComboBox, inputField, sendButton, messageArea);

        return new Scene(layout, 770, 500);
    }

    private String getUsernameFromFirstNameAndRole(String firstName, String role) {
        String query = "SELECT username FROM users WHERE first_name = ? AND role = ?";
        
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, firstName);
            pstmt.setString(2, role);
            
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return rs.getString("username");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error fetching username from first name and role");
        }

        return null;
    }

    private void sendMessage(String sender, String recipient, String message) {
        String query = "INSERT INTO messages (sender_username, receiver_username, message_text) VALUES (?, ?, ?)";

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, sender);
            pstmt.setString(2, recipient);
            pstmt.setString(3, message);

            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Failed to send message");
        }
    }
}

