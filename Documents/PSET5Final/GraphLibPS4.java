import java.util.*;

// authors: Alex Quill and Chris Cheng

public class GraphLibPS4 {
	
	public static <V,E> Graph<V,E> bfs(Graph<V,E> g, V source) {
		Graph<V,E> backTrack = new AdjacencyMapGraph<V,E>();
		backTrack.insertVertex(source);
		LinkedList<V> queue = new LinkedList<V>(); //queue to implement BFS
		
		queue.add(source); //enqueue start vertex
		while (queue.size() > 0) { //loop until no more vertices
			
			V u = queue.remove(); //dequeue
			
			for (V v : g.outNeighbors(u)) { //loop over out neighbors
				if (!backTrack.hasVertex(v)) { //if neighbor not visited, then neighbor is discovered from this vertex
					queue.add(v); //enqueue neighbor
					backTrack.insertVertex(v);
					backTrack.insertDirected(u, v, g.getLabel(u, v)); //save that this vertex was discovered from prior vertex
				}
			}
		}
		return backTrack;
	}
	
	public static <V,E> List<V> getPath(Graph<V,E> tree, V v) {
		List<V> list = new ArrayList<V>();
		list.add(v); // add the path to be reverse-found
		
		// find the shortest path from the source node to the center of the tree 
		while (tree.inDegree(v) > 0) {
			v = tree.inNeighbors(v).iterator().next();
			list.add(v);
		}
		
		return list;
	}
	
	public static <V,E> Set<V> missingVertices(Graph<V,E> graph, Graph<V,E> subgraph) {
		Set<V> set = new HashSet<V>();
		
		// find items in whole graph that aren't in subgraph
		for (V vertex: graph.vertices()) {
			if (!subgraph.hasVertex(vertex)) set.add(vertex);
		}
		return set;
	}
	
	public static <V, E> double averageSeparation(Graph <V,E> tree, V root) {
		// average separation
		// out of all the actors that are connected to the center of the universe
		// what is the average distance from each to the center of the universe?
		// divide the sum of all differences by the total number of connected nodes 
		double totalSum = 0;
		totalSum = separationHelper(tree, root, totalSum);
		return totalSum/(tree.numVertices()-1);

		}

	public static <V, E> double separationHelper(Graph <V,E> tree, V root, double total) {
	// for each time we recurse, add to the total sum the distance that we are from the source
	// for each time we recurse, add one to the total number of nodes 

		double result = total;
		result ++;
		
		// recurse for each neighbor
		for (V v: tree.outNeighbors(root)) {
			total += separationHelper(tree, v, result);
		}
		
		return total;
	}
}
