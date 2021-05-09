import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

public class Controller {

    @FXML
    TextField username, recipientName, recipientAddress;

    @FXML
    Label pdfContents, recipientNameLbl;

    @FXML
    PasswordField password;

    @FXML
    Button loginButton, submitNotice, logout, clearLogin, clearNotice, viewPdfContents, generateNotice;

    @FXML
    DatePicker noticeSetDate, noticeExpiryDate;

    @FXML
    // Creates the notice page
    public void launchNoticePage(ActionEvent event) throws Exception {
        Parent parent = FXMLLoader.load(getClass().getResource("notice-page.fxml"));
        Scene noticeScene = new Scene(parent);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(noticeScene);
        window.show();
    }

    @FXML
    public void confirmLogin(ActionEvent event) {

        // Retrieve user input from all fields
        String usernameInput = username.getText();
        String passwordInput = password.getText();
        String errorInformation = "Invalid Login!";
        String confirmInformation = "Successful Login!";

        JdbcDao jdbcDao = new JdbcDao();
        boolean flag = jdbcDao.validate(usernameInput, passwordInput);
        try {
            if (flag) {
                confirmAlertBoxes(confirmInformation);
                launchNoticePage(event);
            } else {
                errorAlertBoxes(errorInformation);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            clearLoginContent();
        }
    }

    @FXML
    public void disableLoginButton() {
        // Prevent users from logging in until both fields are filled in
        String usernameInput = username.getText();
        String passwordInput = password.getText();
        boolean isDisabled = usernameInput.isEmpty() || usernameInput.trim().isEmpty()
                || passwordInput.isEmpty() || passwordInput.trim().isEmpty();
        loginButton.setDisable(isDisabled);
    }

    @FXML
    public void disableNoticePageButtons() {
        // Prevent users from submitting their inputs until all fields are filled in
        String recipientNameInput = recipientName.getText();
        String recipientAddressInput = recipientAddress.getText();
        boolean fieldsNotEmpty = recipientNameInput.isEmpty() || recipientNameInput.trim().isEmpty() ||
                recipientAddressInput.isEmpty() || recipientAddressInput.trim().isEmpty() ||
                noticeSetDate.getValue() == null || noticeExpiryDate.getValue() == null;
        submitNotice.setDisable(fieldsNotEmpty);
    }

    public void confirmAlertBoxes(String alertInformation) {
        // Creates an information alert box
        Alert confirmAlert = new Alert(Alert.AlertType.INFORMATION, alertInformation);
        confirmAlert.showAndWait();
    }

    public void errorAlertBoxes(String alertInformation) {
        // Creates an error alert box
        Alert errorAlert = new Alert(Alert.AlertType.ERROR, alertInformation);
        errorAlert.showAndWait();
    }

    public boolean confirmInputsAlertBoxes(String alertInformation) {
        // Creates a confirmation alert box
        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION, alertInformation);
        Optional<ButtonType> result = confirmation.showAndWait();
        if (result.get() == ButtonType.OK) {
            return true;
        } else
            confirmation.close();
            return false;
    }

    public void clearNoticeContent() {
        // Clear all fields on the notice page
        recipientName.clear();
        recipientAddress.clear();
        noticeSetDate.setValue(null);
        noticeExpiryDate.setValue(null);
    }

    public void clearLoginContent() {
        // Clear all fields on the login page
        username.clear();
        password.clear();
        loginButton.setDisable(true);
    }

    public void logoutOfApplication(ActionEvent event) throws Exception {
        // Go back to the login page
        Parent parent = FXMLLoader.load(getClass().getResource("sample.fxml"));
        Scene noticeScene = new Scene(parent);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        String confirmInformation = "Logout?";
        String loggedOut = "You have been successfully logged out!";
        boolean submittedInputs = confirmInputsAlertBoxes(confirmInformation);
        if (submittedInputs) {
            window.setScene(noticeScene);
            window.show();
            confirmAlertBoxes(loggedOut);
        }
    }

    public void submitInputs() {
        // Prevent users from viewing or generating notice until the inputs are submitted
        String confirmInformation = "Submit inputs?";
        boolean submittedInputs = confirmInputsAlertBoxes(confirmInformation);
        if (submittedInputs) {
            String submitInputsSaved = "Inputs saved successfully to the PDF preview page!";
            confirmAlertBoxes(submitInputsSaved);
            viewPdfContents.setDisable(false);
            generateNotice.setDisable(false);
        }
    }

    public AutoInfillKeys viewPdfContents() {
        // Sends the data to be displayed on the preview
        String recipientNameInput = recipientName.getText();
        String recipientAddressInput = recipientAddress.getText();
        LocalDate dateOfExpDate = noticeExpiryDate.getValue();
        LocalDate dateOfSetDate = noticeSetDate.getValue();
        LocalDate localDate = LocalDate.now();
        String formatExpDate = formatDate(dateOfExpDate);
        String formatSetDate = formatDate(dateOfSetDate);
        String formatTodayDate = formatDate(localDate);
        AutoInfillKeys autoInfillKeys = new AutoInfillKeys(
                "07840 654563", "17 Union Way", formatTodayDate,
                recipientNameInput, recipientAddressInput, formatExpDate, formatSetDate);
        pdfContents.setText(new FileHandling().displayNoticeDetails(autoInfillKeys));
        pdfContents.setWrapText(true);
        return autoInfillKeys;
    }

    public String formatDate(LocalDate date) {
        // Converts all dates whilst following a specific pattern
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd MMMM yyyy");
        return dateTimeFormatter.format(date);
    }

    public void generateNotice() {
        // Generates the notice
        AutoInfillKeys autoInfillKeys = viewPdfContents();
        String confirmInformation = "Generate notice?";
        String newNoticeCreated = "Notice successfully generated!";
        boolean generatedNotice = confirmInputsAlertBoxes(confirmInformation);
        if (generatedNotice) {
            confirmAlertBoxes(newNoticeCreated);
            clearNoticeContent();
            pdfContents.setText("");
            new FileHandling().createNewNotice(autoInfillKeys);
        }
    }
}
