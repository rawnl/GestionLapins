package controller;


import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.SheetsScopes;
import com.google.api.services.sheets.v4.model.AppendValuesResponse;
import com.google.api.services.sheets.v4.model.ValueRange;








import java.io.File;
import java.net.URL;
import java.net.URLConnection;
import java.time.LocalDate;
import java.util.Date;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDatePicker;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.DateCell;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;
import javafx.util.converter.LocalDateStringConverter;
import model.Animal;
import model.User;
import javafx.scene.layout.StackPane;
import javafx.scene.input.MouseEvent;
import javafx.event.EventHandler;


public class NotificationController implements Initializable {
   
    double xOffset;
	double yOffset;

    @FXML StackPane stackPane;
    @FXML private ImageView icon;
    @FXML private JFXDatePicker notificationDate;
    @FXML private Label notificationDateMsg;
    @FXML private JFXButton confirmBtn;
    @FXML private JFXButton cancelBtn;

    private Animal animal;

    private boolean answer;
    private LocalDate minDate;
    private LocalDate maxDate;
    private LocalDate selectedDate;

    private static Sheets sheetsService;
    private static String APPLICATION_NAME; // ="Google Sheets Example";
    private static String SPREAD_SHEET_ID; //= "10ihw60G_JNyXbnl9Kvi9LSNorr23tsg49zRyYYShTfQ";
    private static String CREDENTIAL_FILE_PATH ;

    private User user;

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        File imageFile =  new File("images/bell.png");
		Image alertImage = new Image(imageFile.toURI().toString());
		icon.setImage(alertImage);
    }
    
    public void setAnswer(boolean answer) {
        this.answer = answer;
    }

    public boolean getAnswer(){
        return this.answer;
    }

    public LocalDate getMinDate(){
        return minDate;
    }

    public LocalDate getMaxDate(){
        return maxDate;
    }

    public void setScheduledDate(LocalDate localDate) {
        this.selectedDate = localDate;
    }

    public LocalDate getScheduledDate(){
        return this.selectedDate;
    }

    public void setAnimal(Animal animal){
        this.animal = animal;
    }

    public Animal getAnimal(){
        return animal;
    }

    public User getUser(){
        return user;
    }

    public void setUser(User user){
        this.user = user;
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

    public void setMinMaxDate(Date min, Date max) {
        
        minDate = LocalDate.parse(min.toString()).plusDays(1);
        maxDate = LocalDate.parse(max.toString());

        notificationDate.setDayCellFactory(d ->
           new DateCell() {
               @Override public void updateItem(LocalDate item, boolean empty) {
                   super.updateItem(item, empty);
                   System.out.println(item); 
                   setDisable(item.compareTo(maxDate) > 0 || item.compareTo(minDate) < 0 );
                   //setDisable(item.isAfter(minDate) || item.isBefore(maxDate));
               }});
    }

    @FXML
	public void cancelBtnOnAction(ActionEvent event) {
        setAnswer(false);
		cancelBtn.getScene().getWindow().hide();
	}
    
    @FXML
	public void confirmBtnOnAction(ActionEvent event) throws IOException, GeneralSecurityException {
        setScheduledDate(notificationDate.getValue());
        if(getScheduledDate().compareTo(maxDate) <= 0 && getScheduledDate().compareTo(minDate) >= 0 ){
            notificationDateMsg.setVisible(false);
            //call the google sheets API	
            setAnswer(scheduleEmail());

            confirmBtn.getScene().getWindow().hide();	
            Stage stage = (Stage) confirmBtn.getScene().getWindow();
            stage.getOnCloseRequest().handle(new WindowEvent(stage, WindowEvent.WINDOW_CLOSE_REQUEST));	    

        }else{
            notificationDateMsg.setVisible(true);
        }
    }	
    
    public static Credential authorize() throws IOException, GeneralSecurityException {
        
        InputStream in = NotificationController.class.getResourceAsStream(CREDENTIAL_FILE_PATH); //"../resources/credentials.json"

        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JacksonFactory.getDefaultInstance(), new InputStreamReader(in));
        
        List<String> SCOPES = Arrays.asList(SheetsScopes.SPREADSHEETS);
        
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                GoogleNetHttpTransport.newTrustedTransport(),
                JacksonFactory.getDefaultInstance(), 
                clientSecrets, 
                SCOPES)
            .setDataStoreFactory(new FileDataStoreFactory(new java.io.File("tokens")))
            .setAccessType("offline")
            .build();

        Credential credential = new AuthorizationCodeInstalledApp(flow, new LocalServerReceiver()).authorize("user");

        return credential ;
    }

    public static Sheets getSheetsService() throws IOException, GeneralSecurityException {
        Credential credential = authorize();
        return new Sheets.Builder(
            GoogleNetHttpTransport.newTrustedTransport(),
            JacksonFactory.getDefaultInstance(),
            credential)
        .setApplicationName(APPLICATION_NAME)
        .build();
    }

    public static boolean checkConnection(){
        try {
            URL u = new URL("https://www.google.com");
            URLConnection cnx = u.openConnection();
            cnx.connect();       
            System.out.println("Internet connection established"); 
            return true;

        }catch (Exception e){
            System.out.println("No Internet Connection available, please connect with internet");
            return false ;                                            
        } 
    }

    // Fix E-mail format 
    public boolean scheduleEmail()  throws IOException, GeneralSecurityException {
        boolean result = false;
        
        FileInputStream config = new FileInputStream("src/config.properties");
        Properties properties = new Properties();
        properties.load(config);

        APPLICATION_NAME = properties.getProperty("APPLICATION_NAME");
        SPREAD_SHEET_ID = properties.getProperty("SPREAD_SHEET_ID");
        CREDENTIAL_FILE_PATH = properties.getProperty("CREDENTIAL_FILE_PATH");

        sheetsService = getSheetsService();
        String range = "ScheduledEmails!A1:D3";
        
        if(checkConnection()){
            System.out.println(getScheduledDate());
            String date = Integer.toString(getScheduledDate().getDayOfMonth())+"-"
                            +Integer.toString(getScheduledDate().getMonthValue())+"-"
                            +Integer.toString(getScheduledDate().getYear());
            ValueRange appendBody = new ValueRange()
                                        .setValues(Arrays.asList(
                                            Arrays.asList(  user.getName(),
                                                            user.getEmail(),
                                                            "Notification de mise bas",
                                                            Integer.toString(animal.getId()),
                                                            animal.getDMB_next().toString(),
                                                            "UNSENT", 
                                                            date)
                                            ));
                                            
            AppendValuesResponse appendResult = sheetsService.spreadsheets()
                                                                .values()
                                                                .append(SPREAD_SHEET_ID, range, appendBody)
                                                                .setValueInputOption("USER_ENTERED")
                                                                .setInsertDataOption("INSERT_ROWS")
                                                                .setIncludeValuesInResponse(true)
                                                                .execute();
            result = true;
            System.out.println("done");
        }else{
            System.out.println("No connection");
        }
        
        return result;
    }

}
