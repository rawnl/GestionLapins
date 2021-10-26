package controller;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.Map;
/*
import java.net.URLConnection;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
*/
import java.util.ResourceBundle;

import javafx.collections.FXCollections;

//import java.util.function.Predicate;

//import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
//import javafx.collections.transformation.FilteredList;
//import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
//import javafx.geometry.Insets;
//import javafx.scene.Node;
//import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
/*
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Pagination;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableView;
*/
import javafx.scene.control.TextField;
//import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
//import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
//import javafx.stage.Modality;
import javafx.stage.StageStyle;
/*
import javafx.stage.WindowEvent;
import javafx.util.Callback;
*/
import db.DataManager;
import model.Animal;
import model.User;

public class StatsController implements Initializable{
	 
	private double xOffset;
	private double yOffset;
	
	private User user;
	
	@FXML private StackPane stackPane;
	@FXML private AnchorPane mainAnchorPane;
	@FXML private ImageView brandImageView;

	@FXML private AnchorPane dashboardPane;

	@FXML private Button closeBtn;
	@FXML private ImageView closeIcon;

	@FXML private Button maximizeBtn;
	@FXML private ImageView maximizeIcon;
	
	@FXML private Button minimizeBtn;
	@FXML private ImageView minimizeIcon;
	
	@FXML private Label currentTab;

	@FXML private Circle circle;
	@FXML private Label username;
	
	@FXML private Button homeBtn;
	@FXML private ImageView homeIcon;
	
	@FXML private Button statBtn;
	@FXML private ImageView statIcon;
	
	@FXML private Button confBtn;
	@FXML private ImageView confIcon;

	@FXML private Button addBtn;
	@FXML private ImageView addIcon;

	@FXML private TextField search; 

	@FXML CategoryAxis xAxis;
	@FXML NumberAxis yAxis;
	@FXML private LineChart<Number, Number> lineChart;

	@FXML private PieChart pieChart;

	private ObservableList<Animal> obsList;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1){
		initIcons();
		fillPieChart();
		drawLineChart();
		currentTab.setText("Lappins EL BENNA / Statistiques");
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
		
		File homeIconFile =  new File("images/home-white.png");
		Image homeImage = new Image(homeIconFile.toURI().toString());
		homeIcon.setImage(homeImage);

		File statIconFile =  new File("images/analytics-white.png");
		Image statImage = new Image(statIconFile.toURI().toString());
		statIcon.setImage(statImage);

		File confIconFile =  new File("images/settings-white.png");
		Image confImage = new Image(confIconFile.toURI().toString());
		confIcon.setImage(confImage);

	}

	public void setupUserInfo(User user) {
		this.user = user ;

		File userIconFile = new File("user.png");

		DataManager dataManager = new DataManager();
		dataManager.loadImage(userIconFile, user.getId());

		Image userImage = new Image(userIconFile.toURI().toString());
		
		circle.setFill(new ImagePattern(userImage));
		username.setText(user.getUsername());
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

	public void setUser(User user) {
		this.user = user;
	}
	

	public void fillPieChart(){
		DataManager dataManager = new DataManager();
	
		int maleCount = dataManager.getMaleFemelleCount("Lapereau");
		int femelleCount = dataManager.getMaleFemelleCount("Lapine");

		ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList(
        	new PieChart.Data("Lapereau(x)", maleCount), 
        	new PieChart.Data("Lapine(s)", femelleCount));
		
		pieChart.setData(pieChartData);
		pieChart.setClockwise(true); 
		pieChart.setLabelLineLength(25); 
		pieChart.setLabelsVisible(true); 
		pieChart.setStartAngle(90);     

	}

	public void drawLineChart(){
		XYChart.Series series = new XYChart.Series<>();

		DataManager dataManager = new DataManager();
		String today = LocalDate.now().toString();
		Map<String, Integer> myMap = dataManager.countGroupBy(today);
		
		for (Map.Entry<String, Integer> entry : myMap.entrySet()) {
			series.getData().add(new XYChart.Data<>(entry.getKey(),entry.getValue()));
		}

		lineChart.getData().addAll(series);
		lineChart.lookup(".chart-plot-background").setStyle("-fx-background-color: transparent;");

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

	@FXML
	public void toDashboard() throws IOException {
		AnchorPane statPane = FXMLLoader.load(getClass().getResource("../ui/dashboard.fxml"));
		mainAnchorPane.getChildren().setAll(statPane);	
	}
	
	@FXML
	public void toStats() throws IOException {
		AnchorPane statPane = FXMLLoader.load(getClass().getResource("../ui/stats.fxml"));
		mainAnchorPane.getChildren().setAll(statPane);	
	}

	@FXML
	public void toSettings() throws IOException {
		AnchorPane statPane = FXMLLoader.load(getClass().getResource("../ui/settings.fxml"));
		mainAnchorPane.getChildren().setAll(statPane);
		
	}

}
