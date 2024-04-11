package application;

import java.sql.*;
import java.time.LocalDate;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class Signup
{
	private Stage primaryStage;
	
	public Signup(Stage primaryStage)
	{
        this.primaryStage = primaryStage;
    }
	
    public Scene createScene()
    {
        primaryStage.setTitle("Sign-up page");

        // GridPane layout
        GridPane gridPane = new GridPane();
        gridPane.setStyle("-fx-background-color: #A9A9A9;");
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(10));
        
        // Sign-up Page
        Label loginPageLabel = new Label("               Sign up");
        loginPageLabel.setAlignment(Pos.CENTER);
        loginPageLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");
        GridPane.setConstraints(loginPageLabel, 1, 0);

        // User type combo box
        ComboBox<String> userTypeCombo = new ComboBox<>();
        userTypeCombo.getItems().addAll("Doctor", "Nurse", "Patient");
        userTypeCombo.setPromptText("Select User type");
        GridPane.setConstraints(userTypeCombo, 1, 1); // column=1, row=0
        
        // First name
        Label firstNameLabel = new Label("First Name");
        firstNameLabel.setStyle("-fx-font-size: 10px; -fx-font-weight: bold;");
        GridPane.setConstraints(firstNameLabel, 1, 2);
        
        TextField firstNameInput = new TextField();
        firstNameInput.setPromptText("Enter first name");
        GridPane.setConstraints(firstNameInput, 1, 3);
        
        // Last name
        Label lastNameLabel = new Label("Last Name");
        lastNameLabel.setStyle("-fx-font-size: 10px; -fx-font-weight: bold;");
        GridPane.setConstraints(lastNameLabel, 1, 4);
        
        TextField lastNameInput = new TextField();
        lastNameInput.setPromptText("Enter last name");
        GridPane.setConstraints(lastNameInput, 1, 5);
        
        // Phone number
        Label phoneNumberLabel = new Label("Phone Number");
        phoneNumberLabel.setStyle("-fx-font-size: 10px; -fx-font-weight: bold;");
        GridPane.setConstraints(phoneNumberLabel, 1, 6);
        
        TextField phoneNumberInput = new TextField();
        phoneNumberInput.setPromptText("Enter phone number");
        GridPane.setConstraints(phoneNumberInput, 1, 7);

        // Email
        Label emailLabel = new Label("Email");
        emailLabel.setStyle("-fx-font-size: 10px; -fx-font-weight: bold;");
        GridPane.setConstraints(emailLabel, 1, 8);
        
        TextField emailInput = new TextField();
        emailInput.setPromptText("Enter email id");
        GridPane.setConstraints(emailInput, 1, 9);

        // Password
        Label passwordLabel = new Label("Password");
        passwordLabel.setStyle("-fx-font-size: 10px; -fx-font-weight: bold;");
        GridPane.setConstraints(passwordLabel, 1, 10);
        
        PasswordField passwordInput = new PasswordField();
        passwordInput.setPromptText("Create Password");
        GridPane.setConstraints(passwordInput, 1, 11);

        // Date picker
        Label dobLabel = new Label("Date of birth");
        dobLabel.setStyle("-fx-font-size: 10px; -fx-font-weight: bold;");
        GridPane.setConstraints(dobLabel, 1, 12);
        
        DatePicker dobPicker = new DatePicker();
        dobPicker.setPromptText("DD/MM/YYYY");
        GridPane.setConstraints(dobPicker, 1, 13);

        // Sign up button
        Button signUpButton = new Button("Sign Up");
        HBox buttonBox = new HBox(signUpButton);
        buttonBox.setAlignment(Pos.CENTER);
        GridPane.setConstraints(buttonBox, 1, 15);
        
        /*// Back button
        Button backButton = new Button("Back");
        gridPane.add(backButton, 1, 15);

        backButton.setOnAction(event -> {
            Login login = new Login(primaryStage);
            Scene loginScene = login.createScene();
            primaryStage.setScene(loginScene);
            // The following line is optional if primaryStage is already shown
            primaryStage.show(); 
        });*/

        
        // Button actions
        signUpButton.setOnAction(event -> {
            String userType = userTypeCombo.getValue();
            String firstName = firstNameInput.getText();
            String lastName = lastNameInput.getText();
            String phoneNumber = phoneNumberInput.getText();
            String email = emailInput.getText();
            String password = passwordInput.getText();
            LocalDate dob = dobPicker.getValue();

            if (userType == null || firstName.isEmpty() || lastName.isEmpty() || phoneNumber.isEmpty() || email.isEmpty() || password.isEmpty() || dob == null)
            {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("Please fill out all fields.");
                alert.showAndWait();
                return;
            }

            // Call the method to save user data to the database
            String result = saveUserData(userType, firstName, lastName, phoneNumber, email, password, dob);

            switch (result) {
                case "Success":
                    Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                    successAlert.setTitle("Success");
                    successAlert.setHeaderText(null);
                    successAlert.setContentText("User registered successfully.");
                    successAlert.setOnHidden(e -> {
                        Login login = new Login(primaryStage);
                        primaryStage.setScene(login.createScene());
                        primaryStage.show();
                    });
                    successAlert.showAndWait();
                    break;
                case "Username Taken":
                    Alert usernameTakenAlert = new Alert(Alert.AlertType.ERROR);
                    usernameTakenAlert.setTitle("Registration Error");
                    usernameTakenAlert.setHeaderText("Username Taken");
                    usernameTakenAlert.setContentText("This username is already taken. Please choose another one.");
                    usernameTakenAlert.showAndWait();
                    break;
                case "Error":
                    Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                    errorAlert.setTitle("Error");
                    errorAlert.setHeaderText(null);
                    errorAlert.setContentText("Registration failed.");
                    errorAlert.showAndWait();
                    break;
            }
        });
        

        // Adding all nodes to the gridPane
        gridPane.getChildren().addAll(loginPageLabel, userTypeCombo, firstNameLabel, firstNameInput, lastNameLabel, lastNameInput, phoneNumberLabel, phoneNumberInput,
        							  emailLabel, emailInput, passwordLabel, passwordInput, dobLabel, dobPicker, buttonBox);
        
        // Company Label
        Label companyName = new Label("Pediatric Pulse");
        companyName.setStyle("-fx-font-size: 36px; -fx-font-weight: bold;");
        AnchorPane.setTopAnchor(companyName, 20.0);
        AnchorPane.setLeftAnchor(companyName, 600.0);
        
        // AnchorPane layout
        AnchorPane rootPane = new AnchorPane();
        rootPane.setStyle("-fx-background-color: #D3D3D3;");
        rootPane.getChildren().addAll(companyName, gridPane); // place the company name and the GridPane on the AnchorPane

        // Use AnchorPane to position GridPane
        AnchorPane.setTopAnchor(gridPane, 85.0);
        AnchorPane.setBottomAnchor(gridPane, 90.0);
        AnchorPane.setLeftAnchor(gridPane, 580.0);
        AnchorPane.setRightAnchor(gridPane, 580.0);

        // return scene
        return new Scene(rootPane, 1400, 700);
    }
    
    private String saveUserData(String userType, String firstName, String lastName, String phoneNumber, String email, String password, LocalDate dob)
    {
        String query = "INSERT INTO users (username, password, dob, role, first_name, phone_number) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setString(1, email);
            pstmt.setString(2, password);
            pstmt.setDate(3, java.sql.Date.valueOf(dob));
            pstmt.setString(4, userType);
            pstmt.setString(5, firstName);
            pstmt.setString(6, phoneNumber);

            pstmt.executeUpdate();
            return "Success";
        } catch (SQLException e) {
            if (e.getErrorCode() == 1062) {
                return "Username Taken";
            } else {
                System.err.println("Error saving user data to database: " + e.getMessage());
                e.printStackTrace();
                return "Error";
            }
        }
    }

}
