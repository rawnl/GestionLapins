package controller;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.scene.layout.StackPane;
import javafx.scene.input.MouseEvent;
import javafx.event.EventHandler;


public class AlertController implements Initializable {
   
    double xOffset;
	double yOffset;

    @FXML StackPane stackPane;
    @FXML private ImageView alertIcon;
	@FXML private Label label;
	@FXML private Text text;
    @FXML private JFXButton okBtn;


    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        File imageFile =  new File("images/alert-red.png");
		Image alertImage = new Image(imageFile.toURI().toString());
		alertIcon.setImage(alertImage);
    }

	public void changeIcon() {
			File imageFile =  new File("images/ok.png");
			Image alertImage = new Image(imageFile.toURI().toString());
			alertIcon.setImage(alertImage);
			label.setText("Succes");
    }

	public void setMessage(String msg){
		text.setText(msg);
	}
    public void configureBackground() {
		Stage stage = (Stage) stackPane.getScene().getWindow();
		stage.initStyle(StageStyle.TRANSPARENT);
		Scene scene = stackPane.getScene();
		scene.setFill(Color.TRANSPARENT);
    }
    // to fix 
    public void setDynamic() {
		Scene scene = stackPane.getScene();
		scene.setOnMousePressed(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				xOffset = event.getSceneX();
				yOffset = event.getSceneY();
			}
		});
    }
    
    @FXML
	public void okBtnOnAction(ActionEvent event) {
        okBtn.getScene().getWindow().hide();
	}	

}

