package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

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
    
	public User Login(String username, String password){
		User user = null ;
		getConnection();
		try {
			PreStat = connection.prepareStatement("select * from Users where username = ? and password = ? ; ");
			PreStat.setString(1,username);
			PreStat.setString(2,password);
			res = PreStat.executeQuery();
			if(res.next()){
				user = new User ();
				user.setId(res.getInt("ID"));
				user.setName(res.getString("Name"));
				user.setUsername(res.getString("Username"));
				user.setPassword(res.getString("Password"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return user;
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
}
