package controller;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.function.Predicate;

import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.Modality;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;
import javafx.util.Callback;
import db.DataManager;
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
	
	@FXML private ImageView userIcon;
	@FXML private Label username;
	
	@FXML private Button homeBtn;
	@FXML private ImageView homeIcon;
	
	@FXML private Button statBtn;
	@FXML private ImageView statIcon;
	
	@FXML private Button confBtn;
	@FXML private ImageView confIcon;

	@FXML private Button addBtn;
	@FXML private ImageView addIcon;

	@FXML private Button updateBtn;
	@FXML private ImageView updateIcon;

	@FXML private Button deleteBtn;
	@FXML private ImageView deleteIcon;

	@FXML private TextField search; 
//	@FXML private ImageView searchIcon;

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
	@FXML private TableColumn<Animal, String> actions;

	@FXML private TextField nbElements;
	
	private ObservableList<Animal> obsList;
	private String displayType = "TOUS";
	private Animal animal ;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1){
		initIcons();
		addMenuItems();
		initTableView();
		initActionIcons();
		UpdateTableView();
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

		File userIconFile =  new File("images/user-white.png");
		Image userImage = new Image(userIconFile.toURI().toString());
		userIcon.setImage(userImage);

		File homeIconFile =  new File("images/home-white.png");
		Image homeImage = new Image(homeIconFile.toURI().toString());
		homeIcon.setImage(homeImage);

		File statIconFile =  new File("images/analytics-white.png");
		Image statImage = new Image(statIconFile.toURI().toString());
		statIcon.setImage(statImage);

		File confIconFile =  new File("images/settings-white.png");
		Image confImage = new Image(confIconFile.toURI().toString());
		confIcon.setImage(confImage);

		File addIconFile =  new File("images/plus-white.png");
		Image addImage = new Image(addIconFile.toURI().toString());
		addIcon.setImage(addImage);

		File updateIconFile =  new File("images/update-white.png");
		Image updateImage = new Image(updateIconFile.toURI().toString());
		updateIcon.setImage(updateImage);

		File deleteIconFile =  new File("images/delete-white.png");
		Image deleteImage = new Image(deleteIconFile.toURI().toString());
		deleteIcon.setImage(deleteImage);
/*
		File searchIconFile =  new File("images/search-red.png");
		Image searchImage = new Image(searchIconFile.toURI().toString());
		searchIcon.setImage(searchImage);
*/
	}
	
	public void addMenuItems(){
		MenuItem defaultItem = new MenuItem("TOUS");
		MenuItem lapineItem = new MenuItem("LAPINE");
		MenuItem lapereauItem = new MenuItem("LAPEREAU");

		choicesBtn.getItems().addAll(defaultItem, lapineItem, lapereauItem);
		
		defaultItem.setOnAction(e ->{
			displayType = "TOUS";
			UpdateTableView();
			System.out.println("called updateTableView(TOUS)");
		});

		lapineItem.setOnAction(e ->{
			displayType = "LAPINE";			
			UpdateTableView();
			System.out.println("called updateTableView(LAPINE)");
		});

		lapereauItem.setOnAction(e ->{
			displayType = "LAPEREAU" ;
			UpdateTableView();
			System.out.println("called updateTableView(LAPEREAU)");
		});
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

	public void initActionIcons(){
        //add cell of button edit 
        Callback<TableColumn<Animal, String>, TableCell<Animal, String>> cellFoctory = (TableColumn<Animal, String> param) -> {
			// make cell containing buttons
			final TableCell<Animal, String> cell = new TableCell<Animal, String>() {
				@Override
				public void updateItem(String item, boolean empty) {
					super.updateItem(item, empty);
					//that cell created only on non-empty rows
					if (empty) {
						//add button
						setGraphic(null);
						setText(null);
					} else {
						ImageView iconDelete = new ImageView();
						File FileDeleteIcon =  new File("images/delete-red.png");
						Image deleteImg = new Image(FileDeleteIcon.toURI().toString(),25,25, false,true);

						iconDelete.setImage(deleteImg);		
						iconDelete.setStyle(" -fx-cursor: hand ; -fx-fill:#C90202;");
						iconDelete.setOnMouseClicked((MouseEvent event) -> {       
							animal = tableView.getSelectionModel().getSelectedItem();
							displayDeleteDialog();
						});
					
						ImageView iconEdit = new ImageView();
						File FileEditIcon =  new File("images/edit-red.png");
						Image editImg = new Image(FileEditIcon.toURI().toString(), 25, 25, false, true);
									
						iconEdit.setImage(editImg);
						iconEdit.setStyle(" -fx-cursor: hand ; -fx-fill:#C90202; ");
						iconEdit.setOnMouseClicked((MouseEvent event) -> {
							animal = tableView.getSelectionModel().getSelectedItem();
							displayEditForm(animal);
						});

						HBox managebtn = new HBox(iconEdit, iconDelete); // editIcon
						managebtn.setStyle("-fx-alignment:center");
						HBox.setMargin(iconDelete, new Insets(2, 2, 0, 3));
						HBox.setMargin(iconEdit, new Insets(2, 2, 0, 3));
						setGraphic(managebtn);
						setText(null);
					}
				}
			};
            return cell;
    	};
        actions.setCellFactory(cellFoctory);
        tableView.setItems(obsList);
    }

	public void enableSearch(){
		FilteredList<Animal> filteredList = new FilteredList<>(obsList, e -> true);
		search.setOnKeyReleased(e -> {
			search.textProperty().addListener((observableValue, oldValue, newValue) -> {
				filteredList.setPredicate((Predicate<? super Animal>) a ->{
					if(newValue == null || newValue.isEmpty()){
						return true;
					}
					String lowerCaseFilter = newValue.toLowerCase();
					if(Integer.toString(a.getId()).contains(newValue) ){
						return true;
					}
					else if (Integer.toString(a.getCage_number()).contains(newValue)){
						return true;
					}else if(a.getBirth_date() != null && a.getBirth_date().toString().contains(newValue)){
						return true;
					}else if(Integer.toString(a.getAge()).contains(newValue) ){
						return true;
					}else if(a.getDI() != null && a.getDI().toString().contains(newValue)){
						return true;
					}else if (a.getDMB() != null && a.getDMB().toString().contains(newValue)){
						return true;
					}else if (a.getDI_next() != null && a.getDI_next().toString().contains(newValue)){
						return true ;
					}else if(a.getDMB_next() != null && a.getDMB_next().toString().contains(newValue)){
						return true;
					}else if (Integer.toString(a.getMB()).contains(newValue)){
						return true;
					}else if ((a.getType().toLowerCase()).contains(lowerCaseFilter)){
						return true;
					}
					return false;
				});
			});
			SortedList<Animal> sortedList = new SortedList<>(filteredList);
			sortedList.comparatorProperty().bind(tableView.comparatorProperty());
			tableView.setItems(sortedList);
		});
	}
	//fix success/error msgs
	public void displayDeleteDialog(){
		System.out.println("icon delete clicked");
		FXMLLoader loader = new FXMLLoader(getClass().getResource("../ui/deleteDialog.fxml"));
		Parent root;
		try {
			root = (Parent) loader.load();
			Stage stage = new Stage();
			stage.setScene(new Scene(root));
		
			DeleteController deleteController = loader.getController();
			deleteController.configureBackground();
													
			stage.initModality(Modality.WINDOW_MODAL); 
			Stage primaryStage = (Stage)(mainAnchorPane.getScene().getWindow()); 
													
			stage.initOwner(primaryStage);
			deleteController.setDynamic();		
			stage.show();

			stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
				public void handle(WindowEvent we) {
					if(deleteController.getAnswer()){
						DataManager dataManager = new DataManager();
						if(dataManager.DeleteAnimal(animal.getId())){
							System.out.println("deleted succesfully");
							UpdateTableView();
						}else{
							System.out.println("error");
						}
					}
				}
			});
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void displayEditForm(Animal animal){
		System.out.println("icon edit clicked");
		FXMLLoader loader = new FXMLLoader(getClass().getResource("../ui/editForm.fxml"));
		Parent root;
		try {
			root = (Parent) loader.load();
			Stage stage = new Stage();
			stage.setScene(new Scene(root));
			EditController editController = loader.getController();
			editController.configureBackground();
			editController.setAnimal(animal);

													
			stage.initModality(Modality.WINDOW_MODAL); // APPLICATION_MODAL
			Stage primaryStage = (Stage)(mainAnchorPane.getScene().getWindow()); 
													
			stage.initOwner(primaryStage);
			editController.fillForm();	
			editController.setDynamic();		
			stage.show();
						
			stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
				public void handle(WindowEvent we) {
					UpdateTableView();
				}
			});
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void UpdateTableView() {
		DataManager dataManager = new DataManager();
		ArrayList<Animal> animals = new ArrayList<Animal>();
		animals = dataManager.getAnimalsByType(displayType);
		obsList = FXCollections.observableList(animals);
		tableView.setItems(obsList);
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
		
		stage.initModality(Modality.WINDOW_MODAL); // APPLICATION_MODAL
		Stage primaryStage = (Stage)(mainAnchorPane.getScene().getWindow()); 
		
		stage.initOwner(primaryStage);
		
		addFormController.setDynamic();		
		stage.show();

		stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			public void handle(WindowEvent we) {
				UpdateTableView();
			}
		});
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
