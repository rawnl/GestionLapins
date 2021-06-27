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
import com.google.api.services.sheets.v4.model.ValueRange;

public class SheetsAndJava {

    private static Sheets sheetsService;
    private static String APPLICATION_NAME; // ="Google Sheets Example";
    private static String SPREAD_SHEET_ID; //= "10ihw60G_JNyXbnl9Kvi9LSNorr23tsg49zRyYYShTfQ";
    private static String CREDENTIAL_FILE_PATH ;

    public static Credential authorize() throws IOException, GeneralSecurityException {
        
        InputStream in = SheetsAndJava.class.getResourceAsStream(CREDENTIAL_FILE_PATH); //"../resources/credentials.json"
        
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

    public static void main(String [] args) throws IOException, GeneralSecurityException{
        
        FileInputStream config = new FileInputStream("src/config.properties");
        Properties properties = new Properties();
        properties.load(config);

        APPLICATION_NAME = properties.getProperty("APPLICATION_NAME");
        SPREAD_SHEET_ID = properties.getProperty("SPREAD_SHEET_ID");
        CREDENTIAL_FILE_PATH = properties.getProperty("CREDENTIAL_FILE_PATH");

        sheetsService = getSheetsService();
        String range = "ScheduledEmails!A1:D3";
        
        if(checkConnection()){
            ValueRange response = sheetsService.spreadsheets()
                                                .values()
                                                .get(SPREAD_SHEET_ID, range)
                                                .execute();
        
            List<List<Object>> values = response.getValues();
            
            if(values == null || values.isEmpty()){
                System.out.println("NO DATA FOUND");
            }else{
                for(List row: values){
                    System.out.printf("Email: %s Subject: %s Body: %s Status: %s \n", row.get(0), row.get(1), row.get(2), row.get(3));
                }
            }    
        }else{
            System.out.println("No connection");
        }
    }
}
