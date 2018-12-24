// Driver code for testing LP3
package rsn170330.lp3;

import java.io.File;
import java.util.List;
import java.util.LinkedList;
import java.util.Scanner;

public class LP3Driver {
	public static void main(String[] args) throws Exception {
		Scanner in;
		
		if (args.length > 0 && !args[0].equals("-")) {
			File file = new File(args[0]);
			in = new Scanner(file);
		} 
		else {
			in = new Scanner(System.in);
		}
		
		boolean VERBOSE = false;
		if (args.length > 1) { VERBOSE = Boolean.parseBoolean(args[1]); }

		String operation = "";
		long lineNo = 0;

		MDS mds = new MDS();
		Timer timer = new Timer();
		long id, result, total = 0;
		MDS.Money price;
		List<Long> name = new LinkedList<>();

		whileloop:
		while (in.hasNext()) {
			lineNo++;
			result = 0;
			operation = in.next();
			
			if(operation.charAt(0) == '#') {
				in.nextLine();
				continue;
			}
			
			switch (operation) {
				case "End":
					break whileloop;
				
				case "Insert":
					id = in.nextLong();
					price = new MDS.Money(in.next());
					name.clear();
					
					while(true) {
						long val = in.nextLong();
						if(val == 0) { break; }
						else { name.add(val); }
					}
					
					result = mds.insert(id, price, name);
					break;
				
				case "Find":
					id = in.nextLong();
					result = mds.find(id).dollars();
					break;
				
				case "Delete":
					id = in.nextLong();
					result = mds.delete(id);
					break;
				
				case "FindMinPrice":
					result = mds.findMinPrice(in.nextLong()).dollars();
					break;
				
				case "FindMaxPrice":
					result = mds.findMaxPrice(in.nextLong()).dollars();
					break;
				
				case "FindPriceRange":
					result = mds.findPriceRange(in.nextLong(), new MDS.Money(in.next()), new MDS.Money(in.next())); 
					break;
				
				case "PriceHike":
					result = mds.priceHike(in.nextLong(), in.nextLong(), in.nextDouble()).dollars();
					break;
				
				case "RemoveNames":
					id = in.nextLong();
					name.clear();
					
					while(true) {
						long val = in.nextLong();
						if(val == 0) { break; }
						else { name.add(val); }
					}
				
					result = mds.removeNames(id, name);
					break;
				
				default:
					System.out.println("Unknown operation: " + operation);
			}
			total += result;
			if(VERBOSE) { 
				System.out.println(lineNo + "\t" + operation + "\t" + result + "\t" + total); 
			}
		}
		System.out.println(total);
		System.out.println(timer.end());
		
	}
	
	public static class Timer {
		long startTime, endTime, elapsedTime, memAvailable, memUsed;

		public Timer() {
			startTime = System.currentTimeMillis();
		}

		public void start() {
			startTime = System.currentTimeMillis();
		}

		public Timer end() {
			endTime = System.currentTimeMillis();
			elapsedTime = endTime-startTime;
			memAvailable = Runtime.getRuntime().totalMemory();
			memUsed = memAvailable - Runtime.getRuntime().freeMemory();
			return this;
		}

		public String toString() {
			return "Time: " + elapsedTime + " msec.\n" + "Memory: " + (memUsed/1048576) + " MB / " + (memAvailable/1048576) + " MB.";
		}
	}
}
