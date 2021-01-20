package controller;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXRadioButton;
import com.jfoenix.controls.JFXTextField;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;
import model.Animal;

public class EditController implements Initializable {

    double xOffset;
	double yOffset;
	
	@FXML StackPane stackPane;
	@FXML private AnchorPane mainAnchorPane;	
	
	@FXML private ImageView closeIcon;
    @FXML private Button closeBtn;
    
    @FXML private JFXTextField ID_field;
    
	@FXML private JFXTextField cage_number_field;
	@FXML private Label cage_number_msg;
	
	@FXML private JFXDatePicker birth_date_field;
	@FXML private Label birth_date_msg;
	
	@FXML private JFXRadioButton lapineBtn;
	@FXML private JFXRadioButton lapereauBtn;
	
	@FXML private ToggleGroup TypeToggleGroup;
	
	@FXML private JFXTextField MB_field;
	@FXML private Label MB_msg;
	
	@FXML private JFXDatePicker DI_field;
	@FXML private Label DI_msg;
	
	@FXML private JFXDatePicker DMB_field;
    @FXML private Label DMB_msg;
    
    private Animal animal ; 

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        File imageFile =  new File("images/close-white.png");
		Image closeImage = new Image(imageFile.toURI().toString());
		closeIcon.setImage(closeImage);

		TypeToggleGroup.selectedToggleProperty().addListener(
		   (observable, oldToggle, newToggle) -> {
		       if (newToggle == lapineBtn) {
		    	   MB_field.setDisable(false);
		    	   DI_field.setDisable(false);
		    	   DMB_field.setDisable(false);
		       } else if (newToggle == lapereauBtn) { 
		    	   //MB_field.clear();
		    	   DI_field.setValue(null);
		    	   DMB_field.setValue(null);
		    	   
		    	   DI_field.setDisable(true);
		    	   MB_field.setDisable(true);
		    	   DMB_field.setDisable(true);
		    	   
		    	   //MB_msg.setText("");
		    	   //DI_msg.setText("");
		    	   //DMB_msg.setText("");
		       }
		    }
		);
		
		cage_number_field.setTextFormatter(new TextFormatter<>(change ->
        (change.getControlNewText().matches("([1-9][0-9]*)?")) ? change : null));
		
		MB_field.setTextFormatter(new TextFormatter<>(change ->
        (change.getControlNewText().matches("([0-9]*)?")) ? change : null));
    }

    public void setAnimal(Animal animal){
        this.animal = animal ;
    }

    public Animal getAnimal(){
        return animal ;
    }

    public void fillForm(){
        ID_field.setDisable(true);

        ID_field.setText(Integer.toString(animal.getId()));
        cage_number_field.setText(Integer.toString(animal.getCage_number()));

        birth_date_field.setValue(animal.getBirth_date().toLocalDate());

        if(animal.getType().equals("LAPEREAU")){
            lapereauBtn.setSelected(true);
        }else{
            lapineBtn.setSelected(true);
            MB_field.setText(Integer.toString(animal.getMB()));
            DI_field.setValue(animal.getDI().toLocalDate());
            DMB_field.setValue(animal.getDMB().toLocalDate());
        }
    }

	public void configureBackground() {
		Stage stage = (Stage) mainAnchorPane.getScene().getWindow();
		stage.initStyle(StageStyle.TRANSPARENT);
		Scene scene = mainAnchorPane.getScene();
		scene.setFill(Color.TRANSPARENT);
	}
	
	public void setDynamic() {
		Scene scene = mainAnchorPane.getScene();
		scene.setOnMousePressed(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				xOffset = event.getSceneX();
				yOffset = event.getSceneY();
			}
		});
		
		Stage stage = (Stage) mainAnchorPane.getScene().getWindow();
		scene.setOnMouseDragged(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				stage.setX(event.getScreenX() - xOffset);
				stage.setY(event.getScreenY() - yOffset);
			}
		});
	}
	
    @FXML
    public void editOnAction(ActionEvent event){
        System.out.println("edit clicked");
    }

    @FXML
	public void closeBtnOnAction(ActionEvent event) {
		closeBtn.getScene().getWindow().hide();	
		Stage stage = (Stage)closeBtn.getScene().getWindow();
		stage.getOnCloseRequest().handle(new WindowEvent(stage, WindowEvent.WINDOW_CLOSE_REQUEST));
		//closeBtn.getScene().getWindow().hide();
	}	
}