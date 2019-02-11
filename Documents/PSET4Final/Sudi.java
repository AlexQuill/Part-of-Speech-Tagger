// class for running through text input and constructing training algorithm
import java.io.*;
import java.util.*;

public class Sudi {
	Map<String, Map<String, Double>> observationMap; // Map (POS | Map(Word, #word/#POS)) used in viterbi
	Map<String, Map<String, Double>> transitionMap;  // Map (POS1 | Map(POS2, #1->2/#1->)) used in viterbi
	Map<String, Integer> totalTransitions;			// Map (POS | #POS->) used in parsing of POS file to construct transition probability map
	Map<String, Integer> wordFrequency;				// Map (Word | #Word) used in parsing of word file to construct observation probability map
	Map<String, Integer> partFrequency;				// Map (POS | #POS) used in parsing of files to construct both maps
	
	Map<String, Double> currentScores;				// Map (current POS | POS score)
	Set<String> currentStates; 
	String start = "start"; // Start state
	
	List<String> inputwords = new ArrayList<String>(); // List (All words in text file) 
	List<String> inputtags = new ArrayList<String>(); // List (all tags in tag file)
	List<String> trainwords = new ArrayList<String>(); // List (All words in text file) 
	List<String> traintags = new ArrayList<String>(); // List (all tags in tag file)
	List<Map<String, Double>> wordIndextoStates; // List that, for each index i, maps all the possible POS and all their scores for word i within a sentence. 
	
	Double U = -100.0; // unknown value
	
	public Sudi() {
		observationMap = new HashMap<String, Map<String, Double>>();
		transitionMap = new HashMap<String, Map<String, Double>>();
		currentScores = new HashMap<String, Double>();
		currentStates = new HashSet<String>();
		totalTransitions = new HashMap<String, Integer>();
		wordFrequency = new HashMap<String, Integer>();
		partFrequency = new HashMap<String, Integer>();
		wordIndextoStates = new ArrayList<Map<String, Double>>();
	}
	
	public void parseTextFile(String filename1, String filename2, String filename3, String filename4) throws IOException {
		// split the file by spaces and add each word to the arrayList instance variable
		BufferedReader words = new BufferedReader(new FileReader(filename1));
		BufferedReader tags = new BufferedReader(new FileReader(filename2));
		BufferedReader realwords = new BufferedReader(new FileReader(filename3));
		BufferedReader realtags = new BufferedReader(new FileReader(filename4));
		// parse words on its own
		try { 
			String w = words.readLine();
			String x = realwords.readLine();
			while (w != null) {
				String[] lineArray = w.split(" ");
					for (String s: lineArray) {
						s.toLowerCase();
						// add each word to input words list and increment frequency by one if necessary
						trainwords.add(s);
						if (wordFrequency.containsKey(s)) wordFrequency.put(s, wordFrequency.get(s) + 1);
						else wordFrequency.put(s, 1);
					
				}
				w = words.readLine();
			}
			while (x != null) {
				String[] lineArray = x.split(" ");
				for (String s: lineArray) {
					inputwords.add(s);
				}
				x = realwords.readLine();
			}
		}
		finally {
			words.close();
			realwords.close();
		}
		// parse tags on its own
		try { 
			String t = tags.readLine();
			String u = realtags.readLine();
			while (t != null) {
				String[] lineArray = t.split(" ");
				// add each tag to tags list and increment if necessary
				for (String s: lineArray) {
					traintags.add(s);
					if (partFrequency.containsKey(s)) partFrequency.put(s, partFrequency.get(s) + 1);
					else partFrequency.put(s, 1);
				}
				t = tags.readLine();
			}

			while (u != null) {
				String[] lineArray = u.split(" ");
				// add each tag to tags list and increment if necessary
				for (String s: lineArray) {
					inputtags.add(s);
				}
				u = realtags.readLine();
			}
		}
		finally {
			tags.close();
			realtags.close();
		}
	}
	
	public void parseTransitionFile(String filename) throws Exception {
		// split the file by spaces and add each word to the arrayList instance variable
		BufferedReader input = new BufferedReader(new FileReader(filename));
		
		String c = input.readLine();
				
		while (c != null) {
			String [] states = c.split(" ");
			for(int i = 0; i < states.length-1; i++) {
				
				// if not transitioned away from yet, put in the transitions map. If so, increment 
				if (!totalTransitions.containsKey(states[i])) totalTransitions.put(states[i], 1);
				else totalTransitions.put(states[i], totalTransitions.get(states[i])   +   1);
				
				if (!transitionMap.containsKey(states[i])) transitionMap.put(states[i], new HashMap<String, Double>());
				
				if (!transitionMap.get(states[i]).containsKey(states[i + 1])) transitionMap.get(states[i]).put(states[i + 1], Math.log(1.0/totalTransitions.get(states[i])));
				// if we have seen this transition before, update numerator and denominator of the frequency 
				else {
					double denom = totalTransitions.get(states[i]);
					double numer = Math.exp(transitionMap.get(states[i]).get(states[i + 1]));
					transitionMap.get(states[i]).put(states[i + 1], Math.log((numer + 1) / denom + 1));
				}
			}
			c = input.readLine();
		}	
		input.close();
		
		transitionMap.put(start, new HashMap<String, Double>());
		transitionMap.get(start).put(inputtags.get(0), 0.0);
	}
	
	public void train() throws Exception {
		parseTextFile("Inputs/brown-train-sentences.txt", "Inputs/brown-train-tags.txt", "Inputs/brown-test-sentences.txt", "Inputs/brown-test-tags.txt");
		parseTransitionFile("Inputs/brown-train-tags.txt");
		
		for (String word: trainwords) {
			observationMap.put(word, new HashMap<String, Double>());
		}
		
		for (int i = 0; i < trainwords.size(); i++) {
			// match each word to its corresponding part of speech
			String currentWord = trainwords.get(i);
			String currentPart = traintags.get(i);
			
			if (observationMap.get(currentWord).containsKey(currentPart)) {
				// update the frequency
				double numer = (observationMap.get(currentWord).get(currentPart) * wordFrequency.get(currentWord)) + 1;// number of total times this word has occurred as the specific part + 1
				double denom = wordFrequency.get(currentWord);// number of total times this part has occurred 
				observationMap.get(currentWord).put(currentPart, numer/denom); // divide the two
			}
			// insert with frequency 1/partfrequency
			else {
				observationMap.get(currentWord).put(currentPart, 1.0/wordFrequency.get(currentWord));			
			}
		}
		
	}
	
	public List<String> Viterbi() {
		currentStates.add(start);
		currentScores.put(start, 0.0);
		
		// loop through input words
		for (int i = 0; i < inputwords.size(); i++) {
			Set<String> nextStates = new HashSet<String>();
			Map<String, Double> nextScores = new HashMap<String, Double>();
			
			for (String state: currentStates) {
				 // as long as a transition exists for this
				 if (transitionMap.get(state)!=null) { 
					 for (String destination: transitionMap.get(state).keySet()) {
						 Double nextScore;
						 nextStates.add(destination);
						 // calculate nextscore
						 if (!observationMap.containsKey(inputwords.get(i))) nextScore = currentScores.get(state) + transitionMap.get(state).get(destination) + U;
						 
						 else if (observationMap.get(inputwords.get(i)).get(destination) != null) nextScore = currentScores.get(state) + transitionMap.get(state).get(destination) + observationMap.get(inputwords.get(i)).get(destination);

						 else nextScore = currentScores.get(state) + transitionMap.get(state).get(destination) + U;
						 
						 // choose winner if multiple routes to one state
						 if (nextScores.containsKey(destination) && nextScores.get(destination) < nextScore) nextScores.put(destination, nextScore);
						 else if (!nextScores.containsKey(destination)) nextScores.put(destination, nextScore);
						 
					 }	
				 }
			 }
		// add next states and scores to list of maps 
		wordIndextoStates.add(nextScores);
		// transition states and scores set/map
		currentStates = nextStates; 
		currentScores = nextScores; 
		}
		
		// list to hold back trace stuff
		ArrayList<String> finalList = new ArrayList<String>();		
		
		for (int i = wordIndextoStates.size() -1; i >= 0; i--) { // work backwards
			Map<String, Double> currentWordStates = wordIndextoStates.get(i);
			String currentHighestState = "placeholder";
			double currentHighestProb = 0; // probability will never be zero 
			
			// run through all possible states of a word and choose the highest probability, adding it to the front of finalList
			for (String possibleState: currentWordStates.keySet()) {
				if (currentHighestProb == 0) {
					currentHighestProb = currentWordStates.get(possibleState);
					currentHighestState = possibleState;
				}
				if (currentWordStates.get(possibleState) > currentHighestProb) {
					currentHighestProb = currentWordStates.get(possibleState);
					currentHighestState = possibleState;
				}
			}
			finalList.add(0, currentHighestState);
		}
		return finalList;
	}
	
	
	public String testTags(String filename) throws Exception {
		List<String> comparetags = new ArrayList<String>(); // List (all tags in tag file)

		// comparison method
		// train 
		try {
			this.train();
			BufferedReader tags = new BufferedReader(new FileReader(filename));
			String w = tags.readLine();
			while (w != null) {
				String[] lineArray = w.split(" ");
				for (String s: lineArray) {
					s.toLowerCase();
					// add each word to input words list and increment frequency by one if necessary
					comparetags.add(s);
				}
				w = tags.readLine();
			}
			tags.close();
		}
		
		catch (Exception e) {
			System.out.println("no files found for training");
		}
		
		// print out list of parts of speech for text file
		List<String> viterbi = this.Viterbi();
		
		int i = 0;
		double correct = 0;
		// run through above list and compare to inputtags
		for (String word: viterbi) {
			if (i<comparetags.size()) if (word.equals(comparetags.get(i))) correct++;
			i++;
		}
		// return comparison
		double result = correct/i * 100;
		return result + "% correct Viterbi; " + (int) correct + " out of " + i + " total words";
		
	}
	
	public void input() {
		// make new Sudi object 
		Sudi koff = new Sudi();
		// console input method
		Scanner read = new Scanner(System.in);
		// append commands to phrase variable
		String phrase = "";
		System.out.println("Enter 'end' to terminate");
		
		while (!phrase.equals("end")) {
			System.out.println("Enter tags: \n");
			phrase = read.nextLine();
			// "teach" the sudi object by assigning tags list and probability instance variables
			if (!phrase.equals("end")) {
				System.out.println(phrase);
				koff.inputwords = Arrays.asList(phrase.split(" "));
				koff.inputtags = this.inputtags;
				koff.observationMap = this.observationMap;
				koff.transitionMap = this.transitionMap;
				System.out.println(koff.Viterbi());
			}
		}
		read.close();
	}
	
	public static void main (String[] args) throws Exception{
		System.out.println("starting...");
		
		Sudi Sudi = new Sudi();
		System.out.println(Sudi.testTags("Inputs/brown-test-tags.txt"));
		Sudi.input();
	}
}