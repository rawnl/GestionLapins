package db;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import javafx.print.PrintColor;
import model.Animal;
import model.User;

public class DataManager {
	
	private static Connection connection;//3306
    private static String url="jdbc:mysql://localhost:3306/rabbits_db?useLegacyDatetimeCode=false&serverTimezone=CET";
    
    private static Statement Stat ;
    private static PreparedStatement PreStat;
    private static ResultSet res ;
    
    public static void  getConnection(){ 
		String user="root";
		String password = "mysqlpassword";
		
    	try{ 
			Class.forName("com.mysql.cj.jdbc.Driver");//.newInstance(); 
			connection= DriverManager.getConnection(url,user,password ); 
			
			System.out.println("Database connected .");
		 }
		 catch(SQLException | ClassNotFoundException e){ //| InstantiationException | IllegalAccessException | ClassNotFoundException
			 System.out.println("Driver is missing !");
		 }
	 }
    
	public User Login(String email, String password){
		User user = null ;
		getConnection();
		try {
			PreStat = connection.prepareStatement("select * from Users where email = ? and password = ? ; ");
			PreStat.setString(1,email);
			PreStat.setString(2,password);
			res = PreStat.executeQuery();
			if(res.next()){
				user = new User ();
				user.setId(res.getInt("ID"));
				user.setName(res.getString("Name"));
				user.setUsername(res.getString("Username"));
				user.setEmail(res.getString("Email"));
				user.setPassword(res.getString("Password"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return user;
	}
    
	// test if inserted 
	public static void insertImage(String url, int id){
		getConnection();

		File myFile = new File(url); //"images/logo-black.png"

		try (FileInputStream fin = new FileInputStream(myFile)) {
			
			PreStat = connection.prepareStatement("update users set image=? where id=?");	
            PreStat.setBinaryStream(1, fin, (int) myFile.length());
			PreStat.setInt(2, id);
            PreStat.executeUpdate();

        }catch (IOException ex) {
            
			Logger lgr = Logger.getLogger(DataManager.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
        
		}catch (SQLException ex) {
        
			Logger lgr = Logger.getLogger(DataManager.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
        
		}
    }
    
	public FileOutputStream loadImage(File fileName, int id){
		String query = "SELECT Image FROM users where id = ?";
		FileOutputStream file = null;
		getConnection();
        try {
			PreStat = connection.prepareStatement(query);
			PreStat.setInt(1, id);
			ResultSet result = PreStat.executeQuery();

			if (result.next()) {

				try (FileOutputStream fos = new FileOutputStream(fileName)) {

					Blob blob = result.getBlob("Image");
					int len = (int) blob.length();

					byte[] buf = blob.getBytes(1, len);

					fos.write(buf, 0, len);
					file = fos;

				} catch (IOException ex) {

					Logger lgr = Logger.getLogger(DataManager.class.getName());
					lgr.log(Level.SEVERE, ex.getMessage(), ex);
				}
			}
		} catch (SQLException ex) {
			
			Logger lgr = Logger.getLogger(DataManager.class.getName());
			lgr.log(Level.SEVERE, ex.getMessage(), ex);
		}
	
		return file;
	
	}
	
	public boolean addAnimal(Animal animal) {
		boolean result = false;
		getConnection();
		try {
			if(animal.getType().equals("LAPINE")) {
				PreStat = connection.prepareStatement("insert into animals (cage_number, birth_date, age, Type, DI, DMB, DI_next, DMB_next, MB) values (?, ?, ?, ?, ?, ?, ?, ?, ?) ; ");
				PreStat.setInt(1,animal.getCage_number());
				PreStat.setDate(2,animal.getBirth_date());
				PreStat.setInt(3,animal.getAge());
				PreStat.setString(4,animal.getType());
				PreStat.setDate(5,animal.getDI());
				PreStat.setDate(6,animal.getDMB());
				PreStat.setDate(7,animal.getDI_next());
				PreStat.setDate(8,animal.getDMB_next());
				PreStat.setInt(9, animal.getMB());
			}else {
				PreStat = connection.prepareStatement("insert into animals (cage_number, birth_date, age, Type) values (?, ?, ?, ?) ; ");
				PreStat.setInt(1,animal.getCage_number());
				PreStat.setDate(2,animal.getBirth_date());
				PreStat.setInt(3,animal.getAge());
				PreStat.setString(4,animal.getType());
			}		
			//res = PreStat.executeUpdate();
			if(PreStat.executeUpdate() >= 1){
				result = true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}
	//to test 
	public Animal getAnimal(int id) {
		Animal animal = null ;
		getConnection();
		try {
			PreStat = connection.prepareStatement("select * from Animals where ID = ? ; ");
			PreStat.setInt(1,id);
			res = PreStat.executeQuery();
			if(res.next()){
				animal = new Animal ();
				animal.setId(res.getInt("id"));
				animal.setCage_number(res.getInt("cage_number"));
				animal.setBirth_date(res.getDate("birth_date"));
				animal.setAge(res.getInt("age"));
				animal.setType(res.getString("Type"));
				animal.setDI(res.getDate("DI"));
				animal.setDMB(res.getDate("DMB"));
				animal.setDI_next(res.getDate("DI_next"));
				animal.setDMB_next(res.getDate("DMB_next"));
				animal.setMB(res.getInt("Mb"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return animal;
	}
	
	public boolean DeleteAnimal(int id) {
		boolean result = false ;
		getConnection();
		try {
			PreStat = connection.prepareStatement("delete from Animals where ID = ? ; ");
			PreStat.setInt(1,id);
			if(PreStat.executeUpdate() >= 1){
				result = true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}

	public boolean EditAnimal(Animal animal) {
		boolean result = false;
		getConnection();
		try {
			if(animal.getType().equals("LAPINE")) {
				PreStat = connection.prepareStatement(" UPDATE animals SET cage_number = ?, birth_date = ?, age = ?, Type = ?, DI = ?, DMB = ?, MB = ? WHERE Id =  ? ; ");
				
				PreStat.setInt(1,animal.getCage_number());
				PreStat.setDate(2,animal.getBirth_date());
				PreStat.setInt(3,animal.getAge());
				PreStat.setString(4,animal.getType());
				PreStat.setDate(5,animal.getDI());
				PreStat.setDate(6,animal.getDMB());
				PreStat.setInt(7, animal.getMB());
				
				PreStat.setInt(8,animal.getId());
				
			}else {
				PreStat = connection.prepareStatement(" UPDATE animals SET cage_number = ?, birth_date = ?, age = ?, Type = ?, DI = Null, DMB = Null, DI_next = Null, DMB_next = Null, MB = Null WHERE Id =  ? ; ");
				
				PreStat.setInt(1,animal.getCage_number());
				PreStat.setDate(2,animal.getBirth_date());
				PreStat.setInt(3,animal.getAge());
				PreStat.setString(4,animal.getType());

				PreStat.setInt(5,animal.getId());
			}	
			System.out.println(PreStat);	
			if(PreStat.executeUpdate() >= 1){
				result = true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public ArrayList<Animal> getAnimals() {
		ArrayList<Animal> animals = new ArrayList<Animal>() ;
		getConnection();
		try {
			Stat = connection.createStatement();
			res = Stat.executeQuery("select * from animals ; ");
			while(res.next()){
				Animal animal = new Animal ();
				
				animal.setId(res.getInt("id"));
				animal.setCage_number(res.getInt("cage_number"));
				animal.setBirth_date(res.getDate("birth_date"));
				animal.setAge(res.getInt("age"));
				animal.setType(res.getString("type"));
					
				animal.setDI(res.getDate("DI"));
				animal.setDMB(res.getDate("DMB"));
				animal.setDI_next(res.getDate("DI_next"));
				animal.setDMB_next(res.getDate("DMB_next"));
				animal.setMB(res.getInt("MB"));
				
				animals.add(animal);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return animals;
	}

	public ArrayList<Animal> getAnimalsByType(String type) {
		ArrayList<Animal> animals = new ArrayList<Animal>() ;
		getConnection();
		try {
			Stat = connection.createStatement();
			
			if(type.equals("TOUS")){
				return getAnimals();
			}else{
				PreStat = connection.prepareStatement("select * from Animals where Type = ? ; ");
				PreStat.setString(1,type);
				res = PreStat.executeQuery();
				while(res.next()){
					Animal animal = new Animal ();
					
					animal.setId(res.getInt("id"));
					animal.setCage_number(res.getInt("cage_number"));
					animal.setBirth_date(res.getDate("birth_date"));
					animal.setAge(res.getInt("age"));
					animal.setType(res.getString("type"));
						
					animal.setDI(res.getDate("DI"));
					animal.setDMB(res.getDate("DMB"));
					animal.setDI_next(res.getDate("DI_next"));
					animal.setDMB_next(res.getDate("DMB_next"));
					animal.setMB(res.getInt("MB"));
					
					animals.add(animal);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return animals;
	}

	public int getMaleFemelleCount(String type){
		int count = 0 ;
		try {
			PreStat = connection.prepareStatement("select count(*) AS rowcount from Animals where Type = ? ; ");
			PreStat.setString(1,type);
			res = PreStat.executeQuery();
			
			res.next();
			count = res.getInt("rowcount");
			System.out.println(count);
			PreStat.close();
			res.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return count;
	}

	public static Map<String, Integer> countGroupBy(String date){
		Map<String, Integer> myMap = null;
		getConnection();
		try {
			PreStat = connection.prepareStatement("SELECT DMB_next, count(DMB_next) as rowcount FROM Animals where DMB_next >= ? group by DMB_next");
			PreStat.setString(1, date);

			res = PreStat.executeQuery();
			
			myMap = new HashMap<String, Integer>();
			
			while(res.next()){
				myMap.put(res.getString(1), res.getInt(2));
			}			
			
		}catch (SQLException e) {
			e.printStackTrace();
		}
		return myMap;
	}
/*	
	public static void main(String [] args){
		/*FileOutputStream file = loadImage(1);
		if( file != null){
			System.out.println(file.toString());
		}else{
			System.out.println("error");
		}*/
/*
		LocalDate now = LocalDate.now();  
		//System.out.println(now);
		

		Map<String, Integer> myMap = countGroupBy();
		
		for (Map.Entry<String, Integer> entry : myMap.entrySet()) {
			System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());
		}

	}*/
	
}
