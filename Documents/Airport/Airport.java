import java.util.*;
import java.io.*;

public class Airport {
	int level; // class
	double xc;
	double yc;
	String name;
	String continent;
	String country;
	String region;
	ArrayList<City> closestCities;
	Airport pred = null;
	double G = Double.POSITIVE_INFINITY;
	double H = Double.POSITIVE_INFINITY;
	double F = Double.POSITIVE_INFINITY;

	
	public Airport(int level, int xc, int yc, String name, String continent, String country, String region) {
		this.level = level;
		this.xc = xc;
		this.yc = yc;
		this.name = name;
		this.continent = continent;
		this.country = country;
		this.region = region;
		this.closestCities = new ArrayList<City>();
	}
	
	public double getX() {
		return this.xc;	
	}	
	
	public void setX(int xc) {
		this.xc = xc;
	}	
	
	public double getY() {
		return this.yc;
	}
	
	public void setY(int yc) {
		this.yc = yc;
	}
	
	public int getLevel() {
		return this.level;
	}
	
	public String getContinent() {
		return this.continent;
	}
	
	public String getName() {
		return this.name;
	}
	
	public String getCountry() {
		return this.country;
	}
	
	public String getRegion() {
		return this.region;
	}
	
	public void setPred(Airport pred) {
		this.pred = pred;
	}
	
	public Airport getPred() {
		return this.pred;
	}
	
	public void setG(double G) {
		this.G = G;
	}		
	
	public double getG() {
		return this.G;	
	}	

	public void setH(double H) {
		this.H = H;	
	}	
	
	public double getH() {
		return this.H;
	}	
	
	public double getF() {
		return this.F;	
	}	
	
	public void setF(double F) {
		this.F = F;
	}	

	public ArrayList<City> getCities() {
		return this.closestCities;
	}
	
	public void addClosest(City city) {
		this.closestCities.add(city);
	}
	
	
	
}
