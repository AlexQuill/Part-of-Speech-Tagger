import java.util.*;
import java.io.*;

public class City {
	int population;// population
	int xc;// x coordinate
	int yc;// y coordinate
	String name;
	String country;
	Boolean visited = false;
	ArrayList<Airport> airportList;// airport list
	
	public City(int population, int xc, int yc, String name, String country) {
		this.population = population;
		this.xc = xc;
		this.yc = yc;
		this.name = name;
		this.country = country;
		airportList = new ArrayList<Airport>();
	}
	
	public int getPop() {
		return this.population;
	}
	
	public void setPop(int population) {
		this.population = population;
	}
	
	public int getX() {
		return this.xc;	
	}	
	
	public void setX(int xc) {
		this.xc = xc;
	}	
	
	public int getY() {
		return this.yc;
	}
	
	public void setY(int yc) {
		this.yc = yc;
	}
	
	public String getCountry() {
		return this.country;
	}
	
	public void setCountry(String country) {
		this.country = country;
	}
	
	public void addAirport(Airport airport) {
		this.airportList.add(airport);
	}
	public void addAirports(ArrayList<Airport> airportList) {
		for(Airport airport: airportList) this.airportList.add(airport);
	}
	
	public ArrayList<Airport> getAirports() {
		return this.airportList;
	}
	
	public void visit() {
		this.visited = true;
	}

}
