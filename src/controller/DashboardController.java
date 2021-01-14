package controller;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.util.ArrayList;
import java.util.ResourceBundle;

import db.DataManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import model.Animal;
import model.User;

public class DashboardController implements Initializable{
	 
	private double xOffset;
	private double yOffset;
	
	private User user;
	
	@FXML private AnchorPane mainAnchorPane;
	@FXML private ImageView brandImageView;
	
	@FXML private Button closeBtn;
	@FXML private ImageView closeIcon;

	@FXML private Button maximizeBtn;
	@FXML private ImageView maximizeIcon;
	
	@FXML private Button minimizeBtn;
	@FXML private ImageView minimizeIcon;
	
	@FXML private Label username;
	
	@FXML private Button addBtn;
	@FXML private MenuButton choicesBtn;
		
	@FXML private TableView<Animal> tableView;
	@FXML private TableColumn <Animal, Integer> id;
	@FXML private TableColumn <Animal, Integer> cage_number;
	@FXML private TableColumn <Animal, Date> birth_date;
	@FXML private TableColumn <Animal, Integer> age; 
	@FXML private TableColumn <Animal, String> type;
	@FXML private TableColumn <Animal, Integer> MB;
	@FXML private TableColumn <Animal, Date> DI;
	@FXML private TableColumn <Animal, Date> DMB;
	@FXML private TableColumn <Animal, Date> DI_next;
	@FXML private TableColumn <Animal, Date> DMB_next;
		
	@FXML private TextField nbElements;
	
	private ObservableList<Animal> obsList;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1){
		initIcons();
		initTableView();
		updateTableView();
		//leftPane.setEffect(new GaussianBlur());
	}
	
	public void initIcons() {
		File brandingFile =  new File("images/logo-red.png");
		Image brandingImage = new Image(brandingFile.toURI().toString());
		brandImageView.setImage(brandingImage);
		
		File closeIconFile =  new File("images/close-white.png");
		Image closeImage = new Image(closeIconFile.toURI().toString());
		closeIcon.setImage(closeImage);
		
		File maximizeIconFile =  new File("images/maximize-white.png");
		Image maximizeImage = new Image(maximizeIconFile.toURI().toString());
		maximizeIcon.setImage(maximizeImage);
		
		File minimizeIconFile =  new File("images/minimize-white.png");
		Image minimizeImage = new Image(minimizeIconFile.toURI().toString());
		minimizeIcon.setImage(minimizeImage);
	}
	
	public void initTableView() {
		id.setCellValueFactory(new PropertyValueFactory<Animal,Integer>("id"));
		cage_number.setCellValueFactory(new PropertyValueFactory<Animal,Integer>("cage_number"));
		birth_date.setCellValueFactory(new PropertyValueFactory<Animal,Date>("birth_date"));
		age.setCellValueFactory(new PropertyValueFactory<Animal,Integer>("age"));
		type.setCellValueFactory(new PropertyValueFactory<Animal,String>("type"));
		
		MB.setCellValueFactory(new PropertyValueFactory<Animal,Integer>("MB"));
		DI.setCellValueFactory(new PropertyValueFactory<Animal,Date>("dI"));
		DMB.setCellValueFactory(new PropertyValueFactory<Animal,Date>("dMB"));
		DI_next.setCellValueFactory(new PropertyValueFactory<Animal,Date>("dI_next"));
		DMB_next.setCellValueFactory(new PropertyValueFactory<Animal,Date>("dMB_next"));
	}
	
	public void updateTableView() {
		DataManager dataManager = new DataManager();
		ArrayList<Animal> animals = new ArrayList<Animal>();
		animals = dataManager.getAnimals();
		obsList = FXCollections.observableList(animals);
		tableView.getItems().addAll(obsList); // works with animals as well 
		nbElements.setText(((Integer)(tableView.getItems().size())).toString());
	}
	
	public void setupUserInfo(User user) {
		this.user = user ;
		username.setText(user.getUsername());
		//set up the image
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
	
	public User getUser() {
		return user;
	}
	
	public void AddOnAction(ActionEvent event) throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("../ui/addForm.fxml"));
		Parent root = (Parent) loader.load();
		Stage stage = new Stage();
		stage.setScene(new Scene(root));
		AddFormController addFormController = loader.getController();
		addFormController.configureBackground();
		stage.show();
		addFormController.setDynamic();
		tableView.refresh();
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
	
}
