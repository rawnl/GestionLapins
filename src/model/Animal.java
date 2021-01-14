package model;

import java.sql.Date;

public class Animal {
	
	private int id;
	private int cage_number;
	private Date birth_date ;
	private int age; // in months
	private String type;
	
	private Date DI ;
	private Date DMB;
	private Date DI_next;
	private Date DMB_next;
	
	private int MB;
	
	//public enum TYPE {LAPERAU, LAPINE}
	
	public Animal() {
		super();
	}
	
	public Animal(int id,int cage_number, Date birth_date, int age, String type) {
		this.id = id;
		this.cage_number = cage_number;
		this.birth_date = birth_date;
		this.age = age;	
		this.type = type;
	}
	
	public Animal(int id,int cage_number, Date birth_date, int age, String type, Date DI, Date DMB, Date DI_next, Date DMB_next, int MB) {
		
		this.id = id;
		this.cage_number = cage_number;
		this.birth_date = birth_date;
		this.age = age;
		
		this.DI = DI;
		this.DMB = DMB;
		this.DI_next = DI_next;
		this.DMB_next = DMB_next;
		this.MB = MB;

		this.type = type;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	public int getCage_number() {
		return cage_number;
	}

	public void setCage_number(int cage_number) {
		this.cage_number = cage_number;
	}

	public Date getBirth_date() {
		return birth_date;
	}

	public void setBirth_date(Date birth_date) {
		this.birth_date = birth_date;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
	public Date getDI() {
		return DI;
	}

	public void setDI(Date dI) {
		DI = dI;
	}

	public Date getDMB() {
		return DMB;
	}

	public void setDMB(Date dMB) {
		DMB = dMB;
	}

	public Date getDI_next() {
		return DI_next;
	}

	public void setDI_next(Date dI_next) {
		DI_next = dI_next;
	}

	public Date getDMB_next() {
		return DMB_next;
	}

	public void setDMB_next(Date dMB_next) {
		DMB_next = dMB_next;
	}

	public int getMB() {
		return MB;
	}

	public void setMB(int mB) {
		MB = mB;
	}
	
}
