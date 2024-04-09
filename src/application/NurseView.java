package application;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class NurseView extends Application {

    @Override
    public void start(Stage primaryStage) {
        // Header setup
        BorderPane headerPane = new BorderPane();
        headerPane.setPadding(new Insets(20, 10, 10, 10)); // top, right, bottom, left

        Label lblPediatricPulse = new Label("PEDIATRIC PULSE");
        lblPediatricPulse.setStyle("-fx-text-fill: white; -fx-font-size: 20px;");
        Label lblWelcome = new Label("Welcome, Nurse Saket"); // TODO: link with the sql
        lblWelcome.setStyle("-fx-text-fill: white; -fx-font-size: 16px;");
        Button btnLogout = new Button("Logout");

        HBox leftHeader = new HBox(lblPediatricPulse);
        HBox rightHeader = new HBox(lblWelcome, btnLogout);
        rightHeader.setSpacing(20);

        headerPane.setLeft(leftHeader);
        headerPane.setRight(rightHeader);
        headerPane.setStyle("-fx-background-color: #336699;");

        // Main content layout
        HBox contentBox = new HBox(20); // Spacing between children
        contentBox.setAlignment(Pos.CENTER_LEFT);
        contentBox.setPadding(new Insets(10, 10, 10, 10)); // Padding around the HBox

        // Left side - Image
        Image image = new Image("file:/Users/umanggoel/Desktop/Trash/image.png");
        ImageView imageView = new ImageView(image);
        imageView.setFitHeight(300); // Setting the height of the image
        imageView.setFitWidth(300); // Setting the width of the image
        VBox leftContentBox = new VBox(imageView);
        leftContentBox.setAlignment(Pos.TOP_CENTER);

        // Right side - Patient Info
        VBox rightContentBox = new VBox(10); // Spacing between children
        rightContentBox.setAlignment(Pos.TOP_CENTER);
        
        Label lblPatientName = new Label("Mr. Saket_Change_this"); // TODO: Replace with name in database
        lblPatientName.setStyle("-fx-font-weight: bold; -fx-font-size: 16px;");
        
        GridPane patientInfoGrid = new GridPane();
        patientInfoGrid.setAlignment(Pos.CENTER);
        patientInfoGrid.setHgap(20);
        patientInfoGrid.setVgap(10);

        // Add patient info fields to the grid
        addFormField(patientInfoGrid, "Patient Name:", 4, 2);
        addFormField(patientInfoGrid, "Age:", 4, 3);
        addFormField(patientInfoGrid, "Height:", 4, 4);
        addFormField(patientInfoGrid, "Body Weight:", 4, 5);
        addFormField(patientInfoGrid, "Body Temperature:", 4, 6);
        addFormField(patientInfoGrid, "Blood Pressure:",4, 7);

        // Buttons for record entry and doctor assignment
        Button enterRecordsBtn = new Button("Save Records");
        Button pastVisitInfo= new Button("Past Medical Records");

        HBox buttonBox = new HBox(109,enterRecordsBtn,pastVisitInfo);
        rightContentBox.getChildren().addAll(lblPatientName, patientInfoGrid, buttonBox);

        // Add left and right content to the main content layout
        contentBox.getChildren().addAll(leftContentBox, rightContentBox);

        // Assemble the main layout
        BorderPane mainLayout = new BorderPane();
        mainLayout.setTop(headerPane);
        mainLayout.setCenter(contentBox);

        // Create the scene and place it in the stage
        Scene scene = new Scene(mainLayout, 800, 675);
        primaryStage.setTitle("Nurse_View");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void addFormField(GridPane gridPane, String labelText, int colIndex, int rowIndex) {
        Label label = new Label(labelText);
        TextField textField = new TextField();
        gridPane.add(label, colIndex, rowIndex); // Place the label in the specified column (colIndex)
        gridPane.add(textField, colIndex + 1, rowIndex); // Place the text field right next to the label (colIndex + 1)
    }


    private void showAssignDoctorDialog() {
        // TODO: Implement the logic to show a dialog for doctor assignment
        System.out.println("Assign Doctor button clicked");
    }

    public static void main(String[] args) {
        launch(args);
    }
}
