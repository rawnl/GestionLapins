package controller;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;

import model.User;
import db.DataManager;

public class LoginController implements Initializable{
	
	@FXML private AnchorPane mainAnchorPane;
	
	@FXML private Button login;
	@FXML private Label error_msg;
	@FXML private ImageView brandImageView;
	@FXML private TextField username;
	@FXML private PasswordField password;
	
	@FXML private Button closeBtn;
	@FXML private ImageView closeIcon;

	@FXML private Button maximizeBtn;
	@FXML private ImageView maximizeIcon;
	
	@FXML private Button minimizeBtn;
	@FXML private ImageView minimizeIcon;
	
	@Override
	public void initialize(URL url, ResourceBundle resourceBundle) {
		File brandingFile =  new File("images/logo-red.png");
		Image brandingImage = new Image(brandingFile.toURI().toString());
		brandImageView.setImage(brandingImage);
		
		File closeIconFile =  new File("images/close-red.png");
		Image closeImage = new Image(closeIconFile.toURI().toString());
		closeIcon.setImage(closeImage);
		
		File maximizeIconFile =  new File("images/maximize-red.png");
		Image maximizeImage = new Image(maximizeIconFile.toURI().toString());
		maximizeIcon.setImage(maximizeImage);
		
		File minimizeIconFile =  new File("images/minimize-red.png");
		Image minimizeImage = new Image(minimizeIconFile.toURI().toString());
		minimizeIcon.setImage(minimizeImage);
	}
	
	public void loginOnAction(ActionEvent event) throws IOException {
		if((username.getText().isBlank() == false ) &&  (password.getText().isBlank() == false ) ){
			DataManager dataManager = new DataManager();
			User user = dataManager.Login(username.getText(), password.getText());
			if (user != null) {
				toDashboard(user);
			}else {
				//#ff5042
				username.setStyle("-jfx-unfocus-color: #1b1717; ");
				password.setStyle("-jfx-unfocus-color: #1b1717; ");
				error_msg.setText("Tentative de connexion invalide !");
				error_msg.textFillProperty().setValue(Paint.valueOf("#1b1717"));
			}
		}else {
			username.setStyle("-jfx-unfocus-color: #1b1717; ");
			password.setStyle("-jfx-unfocus-color: #1b1717; ");
			error_msg.setText("Veuillez remplir les champs");
			error_msg.textFillProperty().setValue(Paint.valueOf("#1b1717"));
		}
	}
	
	@FXML
	public void closeBtnOnAction(ActionEvent event) {
		Stage stage = (Stage)(((Button)event.getSource()).getScene().getWindow());
		stage.close();
	}
	
	@FXML
	public void maximizeBtnOnAction(ActionEvent event) {
		Stage stage = (Stage) mainAnchorPane.getScene().getWindow();
		if(stage.isMaximized()) {
			stage.setMaximized(false);
		}else {
			stage.setMaximized(true);
		}
	}
	
	@FXML
	public void minimizeBtnOnAction(ActionEvent event) {
		Stage stage = (Stage) mainAnchorPane.getScene().getWindow();
		stage = (Stage)(((Button)event.getSource()).getScene().getWindow());
		stage.setIconified(true);
	}
	
	public void toDashboard(User user) throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("../ui/dashboard.fxml"));
		Parent root = (Parent) loader.load();
		Stage stage = new Stage();
		stage.setScene(new Scene(root));
		DashboardController dashboardController = loader.getController();
		
		dashboardController.setupUserInfo(user);
		dashboardController.configureBackground();
		dashboardController.enableSearch();
		stage.show();
		dashboardController.setDynamic();
		login.getScene().getWindow().hide();
	}
}
