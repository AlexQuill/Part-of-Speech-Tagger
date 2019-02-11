import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

// authors: Alex Quill and Chris Cheng

public class BaconGameFinal {
	// needs three maps
	Map<String, String> actorID; // IDs to actors
	Map<String, String> movieID; // Movie numbers to names
	Map<String, List<String>> movieActor; // Actor numbers to movie numbers
	
	Graph <String, Set<String>> relationships; // COSTAR GRAPH: graph of the relationships between actors by their movie edges
	
	public BaconGameFinal() { // constructor
		actorID = new HashMap<String, String>();
		movieID = new HashMap<String, String>();
		movieActor = new HashMap<String, List<String>>();
		relationships = new AdjacencyMapGraph<String,Set<String>>();
	}
	
	// construct actor and movie maps by inserting number and name into map
	public void parseFile(String filename, Map<String, String> map) throws IOException {
		BufferedReader input = new BufferedReader(new FileReader(filename));
		String c = input.readLine();
		while (c != null) { // read each line
			String [] lineArray = c.split("\\|");
			map.put(lineArray[0], lineArray[1]);
			c = input.readLine();
		}
		
		input.close();
	}
	
	// parse movie-actor file and create movie
	
	public void parseFileTwo(String filename, Map<String, List<String>> map) throws IOException {
		BufferedReader input = new BufferedReader(new FileReader(filename));
		String read = input.readLine();
		
		while (read != null) { // read each line
			String [] lineArray = read.split("\\|");
			if (map.containsKey(lineArray[0])) {
				map.get(lineArray[0]).add(lineArray[1]);
			}	
			
			// if line's stuff is not already in map, make a new list
			else {
				List<String> newList = new ArrayList<String>();
				newList.add(lineArray[1]);
				map.put(lineArray[0], newList);
			}
			
			read = input.readLine();
		}
		
		input.close();
	}
	
	// construct final graph by creating vertices as actors and inserting movie titles  to empty set as edge
	public void constructGraph() {
		Set<String> allMovies = movieActor.keySet();
		
		for(String key1: allMovies) { // add actors from each movie
			for (String key2: movieActor.get(key1)) {
				if (!relationships.hasVertex(key2)) {
					relationships.insertVertex(actorID.get(key2));
				}
			}
		}
		
		// for each movie, look at all actors and add a new set as the edge in between each of the actors
		for (String currentMovie: allMovies) {
				for (String vertex1: movieActor.get(currentMovie)) {
					for (String vertex2: movieActor.get(currentMovie)) {
						if (!vertex1.equals(vertex2)) {
							
								if (relationships.hasEdge(actorID.get(vertex1), actorID.get(vertex2))) {
									relationships.getLabel(actorID.get(vertex1), actorID.get(vertex2)).add(movieID.get(currentMovie));
								}
								
								else {
									relationships.insertUndirected(actorID.get(vertex1), actorID.get(vertex2), new HashSet<String>());
									relationships.getLabel(actorID.get(vertex1), actorID.get(vertex2)).add(movieID.get(currentMovie));
								}
						}
					}
				}
		}
	}
	
	public void playBaconGame(String center) throws IOException{
		Scanner scan = new Scanner(System.in); // to take user input
		String command = "u " + center; // default command
		BaconGameFinal baconOne = new BaconGameFinal(); // initialize object
		
		// run through the text files to update instance variable graphs
		try {
			baconOne.parseFile("Inputs/actors.txt", baconOne.actorID);
			baconOne.parseFile("Inputs/movies.txt", baconOne.movieID);
			baconOne.parseFileTwo("Inputs/movie-actors.txt", baconOne.movieActor);
		}
		
		// boundary case of no file
		catch (Exception e){
			System.out.println("No file found");
		}
		
		baconOne.constructGraph();
		Graph<String, Set<String>> bfs = GraphLibPS4.bfs(baconOne.relationships, center); // run breadth-first search
		
		// interface instructions
	    System.out.println("Commands:");
		System.out.println("c <#>: list top (positive number) or bottom (negative) <#> centers of the universe, sorted by average separation"); //if user puts in 10, top 10, -10, bottom 10
		System.out.println("d <low> <high>: list actors sorted by degree, with degree between low and high");
		System.out.println("i: list actors with infinite separation from the current center");
		System.out.println("p <name>: find path from <name> to current center of the universe");
		System.out.println("s <low> <high>: list actors sorted by non-infinite separation from the current center, with separation between low and high");
		System.out.println("u <name>: make <name> the center of the universe");
		System.out.println("q: quit game \n");
		
	    ////////////////////////////////// Commands separated by slashes like so /////////////////////////////////////////////
		
		// command line interface allows us to select key and run custom command
		while (command.charAt(0) != 'q') {
			char c = command.charAt(0); // get the command

			if (c == 'u') {
				if (command.length() > 1) {
		    		center = command.substring(2);
		    		
		    		// run BFS to center universe around new center
		    		bfs = GraphLibPS4.bfs(baconOne.relationships, center);
		    		Graph <String, Set<String>> newGraph = bfs;
		    		double degrees = GraphLibPS4.averageSeparation(newGraph, center);
		    		
					System.out.println(center + " is now the center of the acting universe, connected to " + (newGraph.numVertices()-1)
					+ "/" + baconOne.relationships.numVertices() + " actors with average separation " + degrees);
					System.out.println(newGraph);
				}
				
				else System.out.println("Read how to properly format your command above");
					
			}
		    
		    ///////////////////////////////////////////////////////////////////////////////

		    else if (c == 'c') {
		    	if (command.split("\\ ").length > 1) {
					// make a new list that will be returned to represent the top/bottom x centers
					List<String> topList = new ArrayList<String>();
					topList = Arrays.asList(command.split("\\ "));
					int X = Integer.parseInt(topList.get(1)); // parse the variable input after the c command
					
					List<String> finalList = new ArrayList<String>(); // list to store the output
					Map<String, Double> separationMap = new HashMap<String, Double>(); // make a map to store all average separations
					
					for (String actor: baconOne.relationships.vertices()) { // add each vertex to the separation map
						double av = GraphLibPS4.averageSeparation((GraphLibPS4.bfs(baconOne.relationships, actor)), actor);
						separationMap.put(actor, av);
					}
					
					
					// write a custom comparator to compare the average degrees of separation for graphs formed around to sources of type string
					if (X > 0) {
						class averageSeparationComparator implements Comparator<String> {
							public int compare(String source1, String source2) {
									if (separationMap.get(source1) > separationMap.get(source2)) {
										return -1;
									}
									else if ( separationMap.get(source1) < separationMap.get(source2)) {
										return 1;
									}
									else return 0;
							}
						}
						
					Comparator<String> comparator = new averageSeparationComparator();
						
					// make a new priority queue to hold all actors 
					PriorityQueue<String> pq = new PriorityQueue<String>(comparator);
					
					// add each actor to the list and finally add them to the finalList that we return
					for (String actor: baconOne.actorID.values()) pq.add(actor);
						
					for (int i = 0; i < X; i++) finalList.add(pq.peek() + ": " + separationMap.get(pq.remove()));
	
				 }
					else if (X < 0) {
						// same but for negative case 
						
						class averageSeparationComparator implements Comparator<String> {
							public int compare(String source1, String source2) {
									if (separationMap.get(source1) > separationMap.get(source2)) {
										return 1;
									}
									else if (separationMap.get(source1) < separationMap.get(source2)) {
										return -1;
									}
									else return 0;
							}
						}
						
						Comparator<String> comparator = new averageSeparationComparator();
						
						// make a new priority queue
						PriorityQueue<String> pq = new PriorityQueue<String>(comparator);
					
						// add each actor to the list
						for (String actor: GraphLibPS4.bfs(baconOne.relationships, center).vertices()) pq.add(actor);
						
			
						// remove the bottom X actors, sorted by their average degrees of separation
						for(int i = 0; i > X; i--) finalList.add(pq.peek() + ": " + separationMap.get(pq.remove()));
						
						
					}
					
					else System.out.println("You didn't ask for any items");
					
					System.out.println(finalList);
		    	}
		    	
		    	else System.out.println("Read how to properly format your command above");
		    }
				
		    ///////////////////////////////////////////////////////////////////////////////

		    else if (c == 'd') {
		    	if (command.split("\\ ").length > 2) {
			    	// parse input to sort by degree
			    	List<String> topList = new ArrayList<String>();
					topList = Arrays.asList(command.split("\\ "));
					int min = Integer.parseInt(topList.get(1)); // first number
					int max = Integer.parseInt(topList.get(2)); // second number
					
					
					// Map with each node and the amount of vertices that are connected to it by inDegree
					Map<String, Integer> map = new HashMap<String, Integer>();
					for (String actor: GraphLibPS4.bfs(baconOne.relationships, center).vertices()) {
						map.put(actor,baconOne.relationships.inDegree(actor));
					}
					
					List<String> finalList = new ArrayList<String>(); // to store output
					
					// make a new priority queue
					PriorityQueue<String> pq = new PriorityQueue<String>((String node1, String node2) -> (map.get(node1)-map.get(node2)));
				
					// Add all vertices that have inDegree within min and max 
					for (String actor: map.keySet()) if (map.get(actor) >= min && map.get(actor) < max) pq.add(actor);
	
					while (pq.peek() != null) {
						String top = pq.poll();
						finalList.add(top + ": " + map.get(top));
					}
					
					System.out.println(finalList);
		    	}
		    	
		    	else System.out.println("Read how to properly format your command above");
		    	
		    }
			
		    ///////////////////////////////////////////////////////////////////////////////

		    else if (c=='i') {
		    	// compare a subgraph to a full graph
				Graph <String, Set<String>> currentGraph = bfs; // subgraph
				Graph <String, Set<String>> wholeGraph = baconOne.relationships;
				
				List<String> finalList = new ArrayList<String>(); // to store output
				
				// Use the missingVertices method to find all vertices not connected to the current center 
				for (String node: wholeGraph.vertices()) {
					if (GraphLibPS4.missingVertices(wholeGraph, currentGraph).contains(node)) finalList.add(node); // run missing vertices
				}
				
				System.out.println(finalList);
				
		    	}
		    
		    ///////////////////////////////////////////////////////////////////////////////
		    
		    else if (c == 's') {
		    	if (command.split("\\ ").length > 2) {
			    	// initialize input
			    	List<String> topList = new ArrayList<String>();
					topList = Arrays.asList(command.split("\\ "));
					int min = Integer.parseInt(topList.get(1)); // parse first input
					int max = Integer.parseInt(topList.get(2)); // parse second input
					
					// Map holds all the sizes of the paths 
					Map<String, Integer> map = new HashMap<String, Integer>();
					
					// for each actor, find all non-connected actors and sort by the distance they are from the center  
					for (String actor: baconOne.relationships.vertices()) {
						Graph <String, Set<String>> bfsGraph = GraphLibPS4.bfs(baconOne.relationships, center);
						Set<String> mV = GraphLibPS4.missingVertices(baconOne.relationships, bfsGraph); // mV = missing vertices
						
						// add it to the finalList if it is not an infinite separation from the center 
						if (!mV.contains(actor) && actor != center){
							map.put(actor, GraphLibPS4.getPath(bfsGraph, actor).size());
						}
					}
					
					List<String> finalList = new ArrayList<String>();
					
					// make a new priority queue to sort the items based on path size
					PriorityQueue<String> pq = new PriorityQueue<String>((String node1, String node2) -> (map.get(node1)-map.get(node2)));
					
					// anonymous comparator to make sure distance between node and center is less than max and greater than min
					for (String actor: map.keySet()) {
						if (map.get(actor) >= min && map.get(actor) < max) pq.add(actor);
					}
					
					// until queue is empty, add to final List
					while (pq.peek()!=null) {
						String top = pq.poll();
						finalList.add(top + ": " + map.get(top));
					}
					
					System.out.println(finalList);
		    	}
		    	
		    	else System.out.println("Read how to properly format your command above");
				
			}
			
		    ///////////////////////////////////////////////////////////////////////////////
		    
		    
		    else if (c == 'p') {
		    	// when p is pressed:
		    	if (command.length() > 1) {
					String name = command.substring(2);
					
					Graph <String, Set<String>> subGraph = GraphLibPS4.bfs(baconOne.relationships, center); // creating subgraph to find distance from input variable
					
					// make a new list to hold all the vertices
					List<String> backChainList = new ArrayList<String>();
					
					// name is the current vertex
					String currentVertex = name;
					
					try { // try block to catch boundary cases
						backChainList = GraphLibPS4.getPath(subGraph, currentVertex);
						System.out.println(name + "'s number is " + (backChainList.size()-1));
					}
					catch (NullPointerException e) {
						System.out.println(name + " is infinitely separated from " + center);
					}
					
					// process each item in the back chain list and spit it out in nice formatting
					for (int i = 0; i < backChainList.size()-1; i ++) {
						ArrayList<String> connectionList = new ArrayList<String>();
						for (String setContent: baconOne.relationships.getLabel(backChainList.get(i), backChainList.get(i + 1))) {
							connectionList.add(setContent);
						}
						
						System.out.println(backChainList.get(i) + " appeared in " + connectionList + " with " + backChainList.get(i + 1));
					}
		    	}
		    	
		    	else System.out.println("Read how to properly format your command above");
		    }
			
		    System.out.println("\n"+ center +" game >");
		    command = scan.nextLine();
		}
		scan.close();
	}
	
	public static void main (String[] args) throws IOException {
		BaconGameFinal TestGame = new BaconGameFinal();
		TestGame.playBaconGame("Kevin Bacon");	
	}
	
}
