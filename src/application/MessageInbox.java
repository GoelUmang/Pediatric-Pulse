package application;

import java.sql.*;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class MessageInbox
{
    
    private String username;
    private String userType;
    
    public MessageInbox(String username, String userType)
    {
        this.username = username;
        this.userType = userType;
    }

    public Scene createScene()
    {
        Stage primaryStage = new Stage();
        primaryStage.setTitle("Inbox");

        TextArea messageArea = new TextArea();
        messageArea.setEditable(false);
        messageArea.setPrefHeight(300);

        Button refreshButton = new Button("Refresh");
        refreshButton.setOnAction(e -> loadMessagesInto(messageArea));
        
        Button replyButton = new Button("Reply");
        replyButton.setOnAction(e -> {
            // Assuming you have username and userType available in this context
            SendMessage sendMessage = new SendMessage(this.username, this.userType);
            Scene sendMessageScene = sendMessage.createScene();
            Stage currentStage = (Stage) replyButton.getScene().getWindow();
            currentStage.setScene(sendMessageScene);
        });


        // Load messages on scene display
        loadMessagesInto(messageArea);

        VBox layout = new VBox(10);
        layout.setPadding(new Insets(10));
        layout.getChildren().addAll(refreshButton, messageArea, replyButton);

        return new Scene(layout, 540, 440);
    }

    private void loadMessagesInto(TextArea messageArea)
    {
        String messages = retrieveMessages();
        if (!messages.isEmpty())
        {
            messageArea.setText(messages);
        }
        else
        {
            messageArea.setText("No messages found.");
        }
    }

    private String retrieveMessages()
    {
        StringBuilder messages = new StringBuilder();
        // Adjust the query to order the results in ascending order by created_at
        String query = "SELECT u.first_name, u.role, m.message_text, m.created_at " +
                       "FROM messages m " +
                       "JOIN users u ON m.sender_username = u.username " +
                       "WHERE m.receiver_username = ? " +
                       "ORDER BY m.created_at ASC";  // Change to ASC for ascending order
        
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query))
        {
            
            pstmt.setString(1, this.username);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next())
            {
                String senderFirstName = rs.getString("first_name");
                String senderRole = rs.getString("role");
                Date date = rs.getDate("created_at");
                messages.append("[").append(date).append("] ")
                        .append(senderRole).append(" ")
                        .append(senderFirstName).append(" : ")
                        .append(rs.getString("message_text")).append("\n\n");
            }
        }
        catch (SQLException e)
        {
            System.err.println("Error retrieving messages: " + e.getMessage());
            return "Error retrieving messages.";
        }

        return messages.toString();
    }
}