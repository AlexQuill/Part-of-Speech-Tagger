import java.io.*;
import static java.lang.System.out;

import java.util.*;
import java.math.*;

public class System {
	static ArrayList<Airport> allAirports;
	static ArrayList<City> allCities;
	double airportListParam = 100.00;
	double cityListParam = 100.00; // If an airport can be considered close enough to a city to be delivered from, then it should be able to be reached from the city for start/end
	// If airport within 100 miles of a city (1.6 latitude degrees), that city can be accessed by that airport and that airport can be used to exit that city. 
	
	public System() {
		allAirports = new ArrayList<Airport>();
		allCities = new ArrayList<City>();
	}
	
	public void cityParse(String filename) {
		try {
			BufferedReader input = new BufferedReader(new FileReader(filename));
			String read = input.readLine();
			
			while (read != null) {
				String [] cityArray = read.split(",");
				
				String continent = cityArray[0];
				String name = cityArray[1];
				String population = cityArray[3];
				String xco = cityArray[4];
				String yco = cityArray[5];
				
				City city = new City(Integer.parseInt(population), Integer.parseInt(xco), Integer.parseInt(yco), name, continent);
				allCities.add(city);
				read = input.readLine();
			}
			input.close();
		}
		catch (FileNotFoundException e) {e.printStackTrace();}
		catch (IOException e) {e.printStackTrace();}
	}
	
	public void airportParse(String filename) {

		try {
			BufferedReader input = new BufferedReader(new FileReader(filename));
			String read = input.readLine();
			
			while (read != null) {
				String [] airportArray = read.split(",");
				
				int finalType = 0;
				String type = airportArray[2];
				if(type.equals("small_airport")) finalType = 1;
				if(type.equals("medium_airport")) finalType = 2;
				if(type.equals("large_airport")) finalType = 3;

				String xco = airportArray[4];
				String yco = airportArray[5];
				String name = airportArray[3];
				String continent = airportArray[7];
				String country = airportArray[8];
				String region = airportArray[9];
				
				if (finalType != 0) {
					Airport airport = new Airport(finalType, Integer.parseInt(xco), Integer.parseInt(yco), name, continent, country, region);
					allAirports.add(airport);
				}
				read = input.readLine();
			}
			input.close();
		}
		catch (FileNotFoundException e) {e.printStackTrace();}
		catch (IOException e) {e.printStackTrace();}
		// excludes closed airports, heliports, and seabases.
		
	}
	
	public void assign (ArrayList<Airport> airports, ArrayList<City> cities) {
		for(Airport airport: allAirports) {
			City closest = allCities.get(0);
			double cDist = Double.POSITIVE_INFINITY;
			
			// Not every airport should necessarily have a closest city. Intuitively, yes, but isolated airports should not necessarily be delivered from. 
			for (City city: allCities) {
				double distVar = EuclidDist(city, airport);
				if (distVar < cDist) {
					cDist = distVar;
					closest = city;
				}
				if (distVar < airportListParam) city.addAirport(airport);
				if (distVar < cityListParam) airport.addClosest(closest);
			}
		}
	}
	
	public void buildNetwork(ArrayList<Airport> airportList) throws IOException{		
	// Graph building comments
	// for every airport1 in all airports
	// for every other airport airport2
		// if airport 1 is international 
			// if airport1.getLevel().equals(airport2.getLevel()) AND not hasEdge(1,2)
				// insert an undirected edge between them
			// if airport 2 is continental AND their continents match AND not hasEdge(1,2)
				// insert undirected 
		// if airport 1 is continental
			// if airport1.getLevel().equals(airport2.getLevel()) AND their regions match AND not hasEdge(1,2)
				// insert undirected edge between them
			// if airport 2 is local AND they are within distance X AND not hasEdge(1,2)
				// insert an undirected edge 
		// if airport 1 is local
			// if airport1.getLevel().equals(airport2.getLevel()) AND they are within distance Y AND not hasEdge(1,2)
				// insert an undirected edge 

		Graph<Airport,Integer> network = new AdjacencyMapGraph<Airport,Integer>();
		for(Airport airport1: allAirports) network.insertVertex(airport1);

		for(Airport airport1: allAirports) {
			for (Airport airport2: allAirports) {
				if (airport1.getLevel() == 1) {
					if (airport1.getLevel() == (airport2.getLevel()) && !network.hasEdge(airport1,airport2)) network.insertUndirected(airport1, airport2, (int) EuclidDist(airport1, airport2));
					if (airport2.getLevel() == 2 && airport1.getContinent().equals(airport2.getContinent()) && !network.hasEdge(airport1,airport2))  network.insertUndirected(airport1, airport2, (int) EuclidDist(airport1, airport2));
				}
				if (airport1.getLevel() == 2) {
					 if (airport1.getLevel() == (airport2.getLevel()) && airport1.getCountry().equals(airport2.getCountry()) && !network.hasEdge(airport1,airport2)) network.insertUndirected(airport1, airport2, (int) EuclidDist(airport1, airport2));
					 if (airport2.getLevel() == 3 && airport1.getRegion().equals(airport2.getRegion()) && !network.hasEdge(airport1,airport2)) network.insertUndirected(airport1, airport2, (int) EuclidDist(airport1, airport2));
				}
				if (airport2.getLevel() == 3) {
					if (airport1.getLevel() == (airport2.getLevel()) && airport1.getRegion().equals(airport2.getRegion())) network.insertUndirected(airport1, airport2, (int) EuclidDist(airport1, airport2));
				}
			}
		}
	}
	
	public static <V,E> Set<V> missingAirports(Graph<V,E> graph, Graph<V,E> subgraph) {
		Set<V> set = new HashSet<V>();
		
	// find items in whole graph that aren't in subgraph
		for (V vertex: graph.vertices()) {
			if (!subgraph.hasVertex(vertex)) set.add(vertex);
		}
		return set;
	}
	
	// for airports and airports
	public static double EuclidDist(Airport a1, Airport a2) {
	// 1 degree latitude = 69 miles
	// 1 degree longitude = 
	// cosine(lat1) * 69.172 (miles)
		
		 double lat1 = a1.getX();
		 double lat2 = a2.getX();
		
		 double latDist = (lat1-lat2)*69;
		 double longDist = 0.0;
		 for( int i = 0; i < Math.abs(lat1 - lat2); i++) {
			 if(lat1 <= lat2) longDist += Math.cos(lat1 + i) * 69.172;
			 else longDist += Math.cos(lat1 - i) * 69.172;
		 }
	
		 return Math.sqrt(Math.pow((latDist), 2) + Math.pow((longDist), 2));
	}
	
	// For cities and airports 
	public static double EuclidDist(City a1, Airport a2) {
	// 1 degree latitude = 69 miles
	// 1 degree longitude = 
	// cosine(lat1) * 69.172 (miles)
		
		 double lat1 = a1.getX();
		 double lat2 = a2.getX();
		
		 double latDist = (lat1-lat2)*69;
		 double longDist = 0.0;
		 for( int i = 0; i < Math.abs(lat1 - lat2); i++) {
			 if(lat1 <= lat2) longDist += Math.cos(lat1 + i) * 69.172;
			 else longDist += Math.cos(lat1 - i) * 69.172;
		 }
	
		 return Math.sqrt(Math.pow((latDist), 2) + Math.pow((longDist), 2));
	}
	
	public static ArrayList<City> AStar(Graph<Airport,Integer> airportMap, City start, City goal){
		// open set 
		 Set<Airport> explored = new HashSet<Airport>();
		
		// create priority queue
		PriorityQueue<Airport> pq = new PriorityQueue<Airport>(new fComparator());
		// Create closed list 
		
		// choose biggest port from starting city
		Airport startAirport = getBestAirport(start);
		
		//add start airport
		pq.add(startAirport);
		
		 while(!pq.isEmpty()) {
			 Airport current = pq.poll();
			 explored.add(current);
		
			 for(Airport neighbor: airportMap.outNeighbors(current)){
				if(neighbor.getCities().contains(goal)) {
					 neighbor.setPred(current);
					 ArrayList<City> finalList = backtrace(start, goal);
					 return finalList;
				}
				
				double neighborTempG = (current.getG() + (double)airportMap.getLabel(current, neighbor));
				double neighborTempH = (EuclidDist(goal, neighbor));
				double neighborTempF = (neighborTempG + neighborTempH);
					 
				if((explored.contains(neighbor) && neighbor.getF() < neighborTempF)) continue;
				
				// If neighbor seen for first time or seen but score was previously worse:
				// Note: If neighbor not in explored AND not in queue, it's our first time seeing it. neighbor.F starts as infinity to avoid breaking when getF called on neighbor not yet seen)
				 else if (!pq.contains(neighbor) || neighborTempF < neighbor.getF()) {
					 neighbor.setPred(current); // set backtrace to current if the fastest way to neighbor has been shown to be through current (so far)
					 neighbor.setG(neighborTempG);
					 neighbor.setH(neighborTempH);
					 neighbor.setF(neighborTempF);
		
					 if(pq.contains(neighbor)) pq.remove(neighbor);
					 pq.add(neighbor);
				}
				 
			} // for each neighbor end
		
		}// while queue not empty end
//		System.out.println("No destination");
		return (new ArrayList<City>());
	}
	
	public static ArrayList<City> backtrace(City start, City goal){
		City currentNode = goal;
		Airport startPort = getBestAirport(goal);
		// startPort is airport from end city with lowest G score. Begin backtrace from here. 
		
		 ArrayList<City> cityPath = new ArrayList<City>();
		 ArrayList<Airport> airportPath = new ArrayList<Airport>();

		 Airport currentPort = startPort;
		 while(!currentNode.equals(start)) {
				currentNode = getClosestCity(currentPort.getPred()); // get city closest to predecessor
		 		cityPath.add(currentNode);
				currentPort = getBestAirport(currentNode); // get best Airport from this non-start city
		 		airportPath.add(currentPort);
//		 short circuits when currentNode = start;
		 }
		 Collections.reverse(cityPath);
		 Collections.reverse(airportPath);
		
		 returnAirportPath(airportPath);
		 return (cityPath);
	}
	
	public static ArrayList<Airport> returnAirportPath(ArrayList<Airport> list) {
		return list;
	}
	
	public static City getClosestCity(Airport airport) {
		double closestDist = Double.POSITIVE_INFINITY;
		City closest = null;
		for(City eachCity: airport.getCities()) {
			if(EuclidDist(eachCity, airport) < closestDist) closest = eachCity;
		}
		return closest;
	}
	
	// get port adjacent to city parameter that is closest to start
	public static Airport getBestAirport(City city) {
		double bestG = Double.POSITIVE_INFINITY; // will update to any airport 
		Airport bestPort = null;
		for( Airport airport: city.getAirports()) {
				if (airport.getG() < bestG) bestPort = airport;
		}
		return bestPort;
	}
	
	public static class fComparator implements Comparator<Airport> { 
		public int compare (Airport A1, Airport A2) {
			return (int)A1.getF() - (int)A2.getF();
		}
	}
	

	public static void main(String[] args) throws IOException {
		System testSystem = new System();
		testSystem.cityParse("Inputs/world_cities.txt");
		testSystem.airportParse("Inputs/airports.txt");
		testSystem.assign(allAirports, allCities);
		testSystem.buildNetwork(allAirports);
	}
	
}
