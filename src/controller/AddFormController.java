package controller;

import java.io.File;
import java.net.URL;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import com.jfoenix.controls.JFXRadioButton;
import com.jfoenix.controls.JFXTextField;

import db.DataManager;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;
import model.Animal;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

public class AddFormController implements Initializable{

	double xOffset;
	double yOffset;
	
	@FXML StackPane stackPane;
	@FXML private AnchorPane mainAnchorPane;	
	
	@FXML private ImageView closeIcon;

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
	
	@FXML private JFXButton addBtn;
	@FXML private Button closeBtn;
	
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
		    	   MB_field.clear();
		    	   DI_field.setValue(null);
		    	   DMB_field.setValue(null);
		    	   
		    	   DI_field.setDisable(true);
		    	   MB_field.setDisable(true);
		    	   DMB_field.setDisable(true);
		    	   
		    	   MB_msg.setText("");
		    	   DI_msg.setText("");
		    	   DMB_msg.setText("");
		       }
		    }
		);
		
		cage_number_field.setTextFormatter(new TextFormatter<>(change ->
        (change.getControlNewText().matches("([1-9][0-9]*)?")) ? change : null));
		
		MB_field.setTextFormatter(new TextFormatter<>(change ->
        (change.getControlNewText().matches("([0-9]*)?")) ? change : null));
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

	public Date ConvertDate(LocalDate date) {
		int year = date.getYear();
		int month = date.getMonthValue();
		int day = date.getDayOfMonth();
		return new Date(year-1900,month-1, day);
	}

	public int ConvertToAge(Date date) {
		LocalDate today = LocalDate.now();
		int yearsInBetween = (today.getYear() - date.getYear()) - 1900; 
		int monthsDiff = today.getMonthValue() - date.getMonth();
		int ageInMonths = yearsInBetween*12 + monthsDiff; 
		return ageInMonths-1;
	}
	
	//ajouter les autres champs
	@FXML 
	public void addOnAction(ActionEvent event) {
		if(validateForm()) {
			int cage = Integer.parseInt(cage_number_field.getText());
			LocalDate birthDateField = birth_date_field.getValue();
			
			Date birth_date = ConvertDate(birthDateField);
			int age = ConvertToAge(birth_date);
			
			Animal animal = new Animal();
			
			animal.setCage_number(cage);
			animal.setBirth_date(birth_date);
			animal.setAge(age);
			
			if(TypeToggleGroup.getSelectedToggle() == lapereauBtn) {
				animal.setType("LAPEREAU");
			}else {
				animal.setType("LAPINE");
				animal.setMB(Integer.parseInt(MB_field.getText()));
				animal.setDI(ConvertDate(DI_field.getValue()));
				animal.setDMB(ConvertDate(DMB_field.getValue()));
			}
			
			DataManager dataManager = new DataManager();
			
			if(dataManager.addAnimal(animal)) {
				displayMessage("success","Opération effectuée avec succès.");
			}else {			
				displayMessage("echec","Echec d'ajout !\nVeuillez vérifier que les informations fournies sont correctes et non pas dupliquées.");
			}
			
		}else {
			addListeners();
			if(validTextField(cage_number_field) == false ) {
				cage_number_msg.setText("Ce champs est obligatoire. Veuillez le remplir");
			}
			if(validDateField(birth_date_field) == false) {
				birth_date_msg.setText("Ce champs est obligatoire. Veuillez le remplir");					
			}
			if(TypeToggleGroup.getSelectedToggle() == lapineBtn) {
				if(validTextField(MB_field) == false ) {
					MB_msg.setText("Ce champs est obligatoire. Veuillez le remplir");
				}
				if(validDateField(DI_field) == false) {
					DI_msg.setText("Ce champs est obligatoire. Veuillez le remplir");					
				}
				if(validDateField(DMB_field) == false) {
					DMB_msg.setText("Ce champs est obligatoire. Veuillez le remplir");					
				}
			}
		}
	}

	public void displayMessage(String msgType, String msg) {
		JFXDialogLayout dialogLayout = new JFXDialogLayout();
		
		JFXDialog dialog = new JFXDialog(stackPane, dialogLayout, JFXDialog.DialogTransition.TOP);
		dialog.setOverlayClose(false);
		
		JFXButton okBtn = new JFXButton("OK");
		okBtn.setStyle("-fx-background-color: #C90202; -fx-border-radius: 1em; -fx-text-fill: white ;");
		okBtn.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent mouseEvent)->{
			dialog.close();
			if(msgType == "success") {
				closeBtn.fire();
			}
		});
		
		dialogLayout.setBody(new Text(msg));
		dialogLayout.setActions(okBtn);
		dialog.show();
	}

	public boolean validateForm() {
		boolean result = false; 
		if(TypeToggleGroup.getSelectedToggle() == lapereauBtn ) {
			if(validTextField(cage_number_field) && validDateField(birth_date_field)) {
				result = true ;
			}else {
				result = false;
			}
		}else {
			if(validTextField(cage_number_field) && validTextField(MB_field) && validDateField(birth_date_field) && validDateField(DI_field) && validDateField(DMB_field)) {
				result = true ;
			}else {
				result = false ;
			}
		}
		return result;
	}

	public boolean validTextField(JFXTextField textField) {
		boolean result = false ;
		if(textField.getText() != null && textField.getText().isEmpty() == false) {
			result = true;
		}
		return result;
	}

	public boolean validDateField(JFXDatePicker dateField) {
		boolean result = false ;
		if(dateField.getValue() != null && dateField.getValue().toString().isEmpty() == false) {
			result = true;
		}
		return result;
	}

	public void addListeners() {
		ChangeListener<Boolean> changeEvent = new ChangeListener<Boolean>() {
			@Override
		    public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue, Boolean newPropertyValue)
		    {
		        cage_number_msg.setText("");
				birth_date_msg.setText("");
				MB_msg.setText("");
				DI_msg.setText("");
				DMB_msg.setText("");	        
		    }
		};
		
		cage_number_field.focusedProperty().addListener(changeEvent);
		birth_date_field.focusedProperty().addListener(changeEvent);
		MB_field.focusedProperty().addListener(changeEvent);
		DI_field.focusedProperty().addListener(changeEvent);
		DMB_field.focusedProperty().addListener(changeEvent);
	}

	@FXML
	public void closeBtnOnAction(ActionEvent event) {
		closeBtn.getScene().getWindow().hide();	
		Stage stage = (Stage)closeBtn.getScene().getWindow();
		stage.getOnCloseRequest().handle(new WindowEvent(stage, WindowEvent.WINDOW_CLOSE_REQUEST));
		//closeBtn.getScene().getWindow().hide();
	}	

}




