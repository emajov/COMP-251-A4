import java.util.*;
import java.io.File;
import java.io.FileNotFoundException;

public class US_elections {

	public static int solution(int num_states, int[] delegates, int[] votes_Biden, int[] votes_Trump, int[] votes_Undecided){	
		// creating variables to store
		int curr_Biden = 0;
		int curr_Trump = 0;
		// variable to get total num delegates
		int total_del = 0;
		// variable to get total of flippable delegates (in delegates_und)
		int del_swing = 0;
		// variable to get total of flippable votes (in min_votes)
		int vote_swing = 0;
		// keeping track of min votes needed
		ArrayList<Integer> min_votes = new ArrayList<>();
		// keeping track of delegates that match swing states
		ArrayList<Integer> delegates_und = new ArrayList<>();
		
		
		// !!! FOR PRACTICE !!! 
		ArrayList<Integer> flippable_indices = new ArrayList<>();
		
		
		
		// iterating over all states
		for (int i=0; i<num_states; i++) {
			// add to total delegate count
			total_del += delegates[i];
			// if no undecided votes in that state
			if (votes_Undecided[i] == 0) {
				if (votes_Biden[i] <= votes_Trump[i])
					curr_Trump += delegates[i];
				else
					curr_Biden += delegates[i];
			}
			// if not enough votes to swing results
			else if (votes_Undecided[i] < Math.abs(votes_Trump[i] - votes_Biden[i])) {
				if (votes_Biden[i] < votes_Trump[i])
					curr_Trump += delegates[i];
				else
					curr_Biden += delegates[i];
			}
			// if there's a tie after adding undecided
			else if (votes_Undecided[i] == Math.abs(votes_Trump[i] - votes_Biden[i])) {
				// biden loses if there's a tie
				if (votes_Biden[i] < votes_Trump[i])
					curr_Trump += delegates[i];
				else {
					int minimum = min_needed(votes_Biden[i], votes_Trump[i], votes_Undecided[i]);
					min_votes.add(minimum);
					delegates_und.add(delegates[i]);
					del_swing += delegates[i];
					vote_swing += minimum;
					
					// for practice
					flippable_indices.add(i);
				}
			}
			// otherwise the state can swing for biden
			else {
				int minimum = min_needed(votes_Biden[i], votes_Trump[i], votes_Undecided[i]);
				min_votes.add(minimum);
				delegates_und.add(delegates[i]);
				del_swing += delegates[i];
				vote_swing += minimum;
				
				// for practice
				flippable_indices.add(i);
			}	
		}
		
		// REMOVE BEFORE SUBMITTING!!!
		System.out.println(min_votes.toString());
		System.out.println(delegates_und.toString());
		System.out.println(flippable_indices.toString());
		System.out.println("Total delegates is: " + total_del);
		
		
		// outside of loop
		int threshold = total_del/2+ 1;
		int total_biden = del_swing + curr_Biden;
		int capacity = total_biden - threshold;
		
		System.out.println("Threshold is: " + threshold);
		
		if (!(total_biden >= threshold)) 
			return -1;
		else {
			if (min_votes.size() == 1) {
				return min_votes.get(min_votes.size()-1);
			}
			else {
				int temp = total_min_needed(min_votes.size(), capacity, delegates_und, min_votes);
				return vote_swing - temp;
			}
		}
	}

	// to find min num voters for a single state that biden needs to convice
	private static int min_needed(int votes_Biden, int votes_Trump, int votes_Undecided) {
		int total = votes_Biden + votes_Trump + votes_Undecided;
		int for_biden = total/2 + 1;
		int addBiden = for_biden - votes_Biden;
		return addBiden;
	}
	
	// to find min number voters biden needs to convince for all states
	private static int total_min_needed(int n, int capacity, ArrayList<Integer> del, ArrayList<Integer> votes) {
		
		int i,w;
		int dp[][] = new int[n+1][capacity+1];
		
		ArrayList<Integer> states_removed = new ArrayList<>();
		ArrayList<Integer> indices_removed = new ArrayList<>();
		
		for (i=0; i<=n; i++) {
			for (w=0; w<=capacity; w++) {
				if (i==0 || w==0) 
					dp[i][w]=0;
				else if (del.get(i-1) <= w) {
					dp[i][w] = Math.max(votes.get(i-1)+dp[i-1][w-del.get(i-1)],
							dp[i-1][w]);
				}
				else 
					dp[i][w] = dp[i-1][w];
			}
		}
	
		// CHANGE BACK LATER !!!
		//int res = dp[n][capacity];
		//System.out.println(res);
		
		
		int res = dp[n][capacity];
		System.out.println("Removed: "+res);
		w = capacity;
		for (i=n; i>0 && res>0; i--) {
			if (res == dp[i-1][w])
				continue;
			else {
				
				states_removed.add(del.get(i-1));
				indices_removed.add(i-1);
				//System.out.print(del.get(i-1) + " ");
				res = res - votes.get(i-1);
				w = w - del.get(i-1);
			}
		}
		System.out.println("State: "+states_removed.toString());
		System.out.println("Index: "+indices_removed.toString());
		return dp[n][capacity];
	}
	
	public static void main(String[] args) {
	 try {
			String path = args[0];
      File myFile = new File(path);
      Scanner sc = new Scanner(myFile);
      int num_states = sc.nextInt();
      int[] delegates = new int[num_states];
      int[] votes_Biden = new int[num_states];
      int[] votes_Trump = new int[num_states];
 			int[] votes_Undecided = new int[num_states];	
      for (int state = 0; state<num_states; state++){
			  delegates[state] =sc.nextInt();
				votes_Biden[state] = sc.nextInt();
				votes_Trump[state] = sc.nextInt();
				votes_Undecided[state] = sc.nextInt();
      }
      sc.close();
      int answer = solution(num_states, delegates, votes_Biden, votes_Trump, votes_Undecided);
      	System.out.println(answer);
    	} catch (FileNotFoundException e) {
      	System.out.println("An error occurred.");
      	e.printStackTrace();
    	}
				
  	}

}