package controller;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.input.MouseEvent;
import javafx.event.EventHandler;


public class DeleteController implements Initializable {
   
    double xOffset;
	double yOffset;

    @FXML StackPane stackPane;
    @FXML private ImageView alertIcon;
    @FXML private JFXButton yesBtn;
    @FXML private JFXButton noBtn;

    private boolean answer;

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        File imageFile =  new File("images/alert-red.png");
		Image alertImage = new Image(imageFile.toURI().toString());
		alertIcon.setImage(alertImage);
    }
    
    public void setAnswer(boolean answer) {
        this.answer = answer;
    }

    public boolean getAnswer(){
        return this.answer;
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
	public void yesBtnOnAction(ActionEvent event) {
        setAnswer(true);
        yesBtn.getScene().getWindow().hide();	
		Stage stage = (Stage) yesBtn.getScene().getWindow();
		stage.getOnCloseRequest().handle(new WindowEvent(stage, WindowEvent.WINDOW_CLOSE_REQUEST));	
    }	
    
    @FXML
	public void noBtnOnAction(ActionEvent event) {
        setAnswer(false);
		noBtn.getScene().getWindow().hide();
	}	

}
