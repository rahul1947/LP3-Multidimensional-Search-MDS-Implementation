package rsn170330.lp3;

/**
 * CS 5V81.001: Implementation of Data Structures and Algorithms 
 * Long Project LP3: Multidimensional search (MDS) Implementation
 * Team: LP101
 * @author Rahul Nalawade (rsn170330)
 * @author Prateek Sarna (pxs180012)
 * @author Bhavish Khanna Narayanan (bxn170002)
 * 
 */

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

// Main class containing required implementation of MDS
public class MDS {
	
	// pTree: maps id to it's product
	private TreeMap<Long, Item> pTree;
	
	// dMap: maps a description long integer to all such products 
	// having that description number
	private HashMap<Long, HashSet<Item>> dMap;
	
	private Money zeroMoney = new Money(); // zero valued price
	
	private long size; // total no of items
	
	// Constructors
	public MDS() {
		pTree = new TreeMap<>();
		dMap = new HashMap<>();
		size = 0;
	}
	
	// Information about each Item
	static class Item {
		// using wrapper class for inbuilt-comparison
		private Long id;  
		private HashSet<Long> description;
		private Money price;
		
		public Item() {
			id = (long) 0;
			price = new Money();
			description = new HashSet<Long>(); 
		}
		
		// Parameterized Constructor:
		public Item(long id, Money p, List<Long> list) {
			this.id = id;
			this.price = p;
			this.description = new HashSet<Long>();
			
			for (Long e: list) {
				// NOTE: always distinct descriptions
				this.description.add(e); 
			}
		}
		
		// Returns the id of the item
		public long getId() {
			return this.id;
		}
		
		// Compares two Items on their id
		public int compareTo(Item other) {
			// comparison of id
			return this.id.compareTo(other.id);
		}
		
		// an item: {id, price, [description]}
		public String toString() {
			return "{" + id + ",\t" + price.toString() + ",\t" 
					+ description.toString() + "}";
		}
	}

	// Public methods of MDS. Do not change their signatures.
	// -----------------------------------------------------------------------
	
	/**
	 * insert(id, price, list): 
	 * Insert a new item whose description is given in the list. 
	 * 
	 * If an entry with the same id already exists, then its 
	 * description and price are replaced by the new values, unless list 
	 * is null or empty, in which case, just the price is updated. 
	 * 
	 * @param id the id of Item to be inserted
	 * @param newPrice the price of the Item to be inserted 
	 * @param list description(s) of the Item to be inserted
	 * @return Returns 1 if the item is new, and 0 otherwise.
	 */
	public int insert(long id, Money newPrice, java.util.List<Long> list) {
		
		// Check if Item with the same id already exits
		if (pTree.containsKey(id)) {
			
			// When list is not null AND the list is not empty ~(A v B) = ~A ^ ~B 
			if (!(list == null || list.isEmpty())) {
				
				// 1a. removing the connections of old descriptions from dMap
				deleteDMap(id);
				
				// 1b. removing the old descriptions of that Item from pTree 
				pTree.get(id).description.clear();
				
				// 2. Inserting new descriptions of that Item in pTree
				for (Long e: list) { 
					pTree.get(id).description.add(e); 
				}
				
				// 3. and inserting this item for it's new descriptions into dMap 
				insertDMap(id);
				
				// While creating new TreeSet, we'll define our own ordering
				/*
				TreeSet<Item> newTreeSet = new TreeSet<Item>(
					// ordering that is on Item.price (Money) and different id's
					new Comparator<Item>() {

						@Override
						public int compare(Item o1, Item o2) {
							// comparison of prices
							int result = o1.price.compareTo(o2.price);

							// if same priced items, we'll compare id's
							if (result == 0)
								return o1.id.compareTo(o2.id);

							return result;
						}
				});
				*/
			}
			// Now, updating the price with newPrice in pTree
			pTree.get(id).price = newPrice;
			
			return 0;
		}

		// Create new Item
		Item newItem = new Item(id, newPrice, list);
		
		// Update pTree
		pTree.put(id, newItem);
		
		// insert the whole new item into the dMap
		insertDMap(id);
		
		size++;
		return 1;
	}
	
	/**
	 * Helper method: To remove all the descriptions from dMap 
	 * whatever existing descriptions the Item(id) has.
	 * 
	 * Postcondition: need to perform descriptions.clear() from pTree
	 * 
	 * @param id the id of the Item whose description is to be removed
	 */
	private void deleteDMap(Long id) {
		
		// removing the connections of old descriptions from dMap
		Item r = pTree.get(id); // the item
		
		for (Long d: r.description) {
			
			// remove r from the HashSet corresponding to d
			dMap.get(d).remove(r);
			
			// When that HashSet is emptied
			if (dMap.get(d).isEmpty())
				dMap.remove(d); // we don't need mapping for d anymore
		}
	}
	
	/**
	 * Helper method: Inserts descriptions of the Item(id) in dMap and 
	 * adds mapping from that description to the HashSet 
	 * updated with the Item(id), if needed.
	 * 
	 * Precondition: Item(id) must be updated in pTree
	 * 
	 * @param id the id of the Item whose description is to be inserted
	 */
	private void insertDMap(Long id) {
		
		Item inItem = pTree.get(id);
		
		// for every description d in inItem.description
		for (Long d: inItem.description) {
			
			// When we already have that description d
			if (dMap.containsKey(d)) {
				//dMap.get(d).remove(inItem); // replacing that item
				dMap.get(d).add(inItem); // adding/ replacing that item
			}
			
			// We need a new HashSet for a mapping from d 
			else {
				HashSet<Item> newHSet = new HashSet<Item>();
				newHSet.add(inItem); // adding the first item in TreeSet
				dMap.put(d, newHSet); // inserting the map from d to newTreeSet 
			}
		}
	}
	
	/**
	 * NEVER USED ;)
	 * Helper method: Replaces all the mapping to an Item(id) in DMap,
	 * for which only it's price is changed.
	 * 
	 * Precondition: the price in the Item(id) must be updated in pTree
	 * 
	 * @param id the id of the Item whose price is changed
	 */ 
	private void replaceDMap(Long id) {
		
		// NOTE: may p look updated with newPrice, 
		// but id of item remains unchanged.
		Item p = pTree.get(id);
		
		for (Long d: p.description) {
			
			// remove(p) will actually remove old version of that item
			dMap.get(d).remove(p); // old_p
			dMap.get(d).add(p); // and store new version(p)
		}
	}
	
	
	/**
	 * find(id): return price of item with given id (or 0, if not found).
	 * @param id the id of the item
	 * @return price of the item, if exists, else 0
	 */
	public Money find(long id) {
		
		// When there is no such item (id) with us
		if (pTree.containsKey(id)) {
			return pTree.get(id).price; // returning price of that item
		}
		// return 0
		return zeroMoney;
	}

	/**
	 * delete(id): delete item from storage. 
	 * 
	 * @param id the id of the Item to be deleted
	 * @return Returns the sum of the long integers that are in the 
	 * description of the Item(id), or 0 if such an id does not exist.
	 */
	public long delete(long id) { 
		
		// When there is no such item (id)
		if (!pTree.containsKey(id)) {
			return 0;
		}
		// r: the Item to be removed
		Item r = pTree.get(id);
		
		long sum = 0; // sum of descriptions of r to be returned
		
		// For each description d, of that Item r
		for (Long d: r.description) {
			
			// remove r from the HashSet corresponding to that description d
			dMap.get(d).remove(r); 
			
			// When tempSet is emptied
			if (dMap.get(d).isEmpty()) 
				dMap.remove(d); // we don't need map for d anymore
			
			sum += d; // updating the sum
		}
		// Now, our dMap should be correctly updated.
		
		// So, removing from pTree
		pTree.remove(id);
		
		return sum;
	}

	
	/**
	 * findMinPrice(d): given a long integer, find items whose description 
	 * contains that number (exact match with one of the long integers in 
	 * the item's description), and return lowest price of those items.
	 * 
	 * @param d the description
	 * @return minimum price if that description is present, else 0 (Money).
	 */
	public Money findMinPrice(long d) {
		
		// When there no such description 
		if (!dMap.containsKey(d)) {
			return zeroMoney; // return 0
		}
		
		HashSet<Item> hs = dMap.get(d); // temporary HashSet
		Money min = toMoney(Long.MAX_VALUE);
		
		for (Item i: hs) {
			if (i.price.compareTo(min) < 0) 
				min = i.price;
		}
		return min;
	}
	
	/**
	 * findMaxPrice(d): given a long integer, find items whose description 
	 * contains that number, and return highest price of those items.
	 * 
	 * @param d the description
	 * @return maximum price if that description is present, else 0 (Money).
	 */
	public Money findMaxPrice(long d) {
		
		// When there no such description 
		if (!dMap.containsKey(d)) {
			return zeroMoney; // return 0
		}
		
		HashSet<Item> hs = dMap.get(d); // temporary HashSet
		Money max = zeroMoney;
		
		for (Item i: hs) {
			if (i.price.compareTo(max) > 0) 
				max = i.price;
		}
		return max;
	}

	
	/**
	 * findPriceRange(d, low, high): given a long integer d, find the 
	 * number of items whose description contains d, and in addition, 
	 * their prices fall within the given range, [low, high]. 
	 * 
	 * @param d the description
	 * @param low the lower limit of price
	 * @param high the upper limit of price
	 * @return count of Items whose prices are in [low, high] 
	 *    and has description d
	 */
	public int findPriceRange(long d, Money low, Money high) {
		
		// When low > high
		if (low.compareTo(high) > 0) {
			return 0;
		}
		
		HashSet<Item> tempSet = dMap.get(d);
		int count = 0; // Now, there would be at-least one Item		
		
		for (Item i: tempSet) {
			// ~ (i < low or high < i) *
			if (! (i.price.compareTo(low) < 0 || i.price.compareTo(high) > 0))
				count++;
		}
		
		return count;
		/**
		 * NOTE: interval [low, high] is assumed to be smaller as compared to 
		 * [lowest, highest] interval. Hence, using ~(cond1 || cond2) 
		 * rather than (cond1 && cond2).
		 */
	}

	/**
	 * priceHike(low, high, rate): increases the price of every product, 
	 * whose id is in the range [low, high] by rate%. 
	 * 
	 * Discard any fractional pennies in the new prices of items. 
	 * 
	 * @param low lower limit
	 * @param high higher limit
	 * @param rate the rate of change
	 * @return Returns the sum of the net increases of the prices.
	 */
	public Money priceHike(long low, long high, double rate) {
		
		if (low > high) 
			return zeroMoney; // When low > high
			
		if (pTree.firstKey().compareTo((Long) high) > 0)
			return zeroMoney;
			
		if (pTree.lastKey().compareTo((Long) low) < 0) 
			return zeroMoney;
		
		long netIncrement = 0; // Now, there would be at-least one Item		
		
		// startId: ceiling(low) and endId: floor(high) keys in pTree TreeMap
		Long startId = pTree.ceilingKey(low);
		Long endId = pTree.floorKey(high);
		
		// When both startId and endId points to the same Item
		if (startId.equals(endId)) {
			netIncrement += itemPriceHike(startId, rate);
			return toMoney(netIncrement); 
		}
		// sortedSubMap: subMap of pTree with id in the interval [startId, endId] 
		Map<Long, Item> sortedSubMap = pTree.subMap(startId, true, endId, true);
		
		for (Long i: sortedSubMap.keySet()) {
			// increment the price of each Item(i) with same rate
			netIncrement += itemPriceHike(i, rate);
		}
		return toMoney(netIncrement);
	}
	
	/**
	 * Increments price of item(id) by rate%.
	 * @param id the id of the item
	 * @param rate the rate by which price increases
	 * @return the incremented price in long format
	 */
	private long itemPriceHike(Long id, double rate) {
		
		long currentPrice = toLong(pTree.get(id).price);
		
		long increment = (long) Math.floor((currentPrice * rate) / 100);
		long newPrice = currentPrice + increment;
		
		// only to change price with newPrice
		insert(id, toMoney(newPrice), null); 
		
		return increment;
	}
	
	/**
	 * Converts money from long format to Money. COULD BE PART of MONEY
	 * @param m the input money in long
	 * @return equivalent Money
	 */
	private Money toMoney(long m) {
		
		int cents = (int) (m % 100); // IMPORTANT* [lp3-big-100k.txt]
		long dollars = m / 100;
		
		return new Money(dollars, cents);
	}
	
	/**
	 * Converts money from Money to long format. COULD BE PART of MONEY
	 * @param m input money in Money
	 * @return equivalent money in long
	 */
	private long toLong(Money m) {
		
		long money = m.dollars();
		money = money * 100;
		money = money + m.cents();
		
		return money;
	}
	
	/**
	 * removeNames(id, list): Remove elements of list from the 
	 * description of id. It is possible that some of the items in 
	 * the list are not in the id's description. 
	 * 
	 * @param id the Item(id) whose Names is to be removed
	 * @param list the Names to be removed
	 * @return Return the sum of the numbers that are actually deleted 
	 * from the description of id. 
	 * Return 0 if there is no such id.
	 */
	public long removeNames(long id, java.util.List<Long> list) {
		
		if (list == null || list.isEmpty())
			return 0; // Edge case when nothing is to be removed
		
		// r: the Item whose description is to be cleared
		Item r = pTree.get(id);
		
		long sum = 0;
		
		// For each description d from the list
		for (Long d: list) {
			
			// If d is present, removing it from the original Item(id)
			if (r.description.contains(d)) {
				
				sum += d; // updating the sum
				
				// remove r from the HashSet corresponding to d
				dMap.get(d).remove(r); 
				
				// When tempSet is emptied
				if (dMap.get(d).isEmpty()) 
					dMap.remove(d); // we don't need map for d anymore
				
			}
			pTree.get(id).description.remove(d);
		}
		// Thus, our dMap and pTree should be correctly updated.***
		
		return sum;
	}
	
	/**
	 * Private method used for debugging the code.
	 * Prints the existing pTree. MAY NOT BE USED.
	 */
	private void printTreeMap() {
		
		System.out.println("Current TreeMap: ");
		System.out.println("ID\tItem");
		System.out.println();
		
		for (Long i: pTree.keySet()) {
			
			System.out.print(i);
			System.out.print("\t"+pTree.get(i).toString());
			System.out.println();
		}
	}
	
	/**
	 * Private method used for debugging the code.
	 * Prints the existing dMap. MAY NOT BE USED.
	 */
	private void printDMap() {
		
		System.out.println("Current HashMap: ");
		System.out.println("Description\tTreeSet");
		System.out.println();
		
		for (Long i: dMap.keySet()) {
			
			System.out.print(i);
			System.out.print("\t"+dMap.get(i).toString());
			System.out.println();
		}
	}
	
	public long size() {
		return size;
	}

	// Do not modify the Money class in a way that breaks LP3Driver.java
	public static class Money implements Comparable<Money> { 
		
		long d; // dollars
		int c; // cents
		
		public Money() { 
			d = 0; 
			c = 0; 
		}
		
		public Money(long d, int c) { 
			this.d = d; 
			this.c = c; 
		}
		
		public Money(String s) {
			String[] part = s.split("\\.");
			int len = part.length;
			
			if(len < 1) { 
				d = 0; 
				c = 0; 
			}
			
			else if(part.length == 1) { 
				d = Long.parseLong(s); 
				c = 0; 
			}
			
			else { 
				d = Long.parseLong(part[0]); 
				c = Integer.parseInt(part[1]); 
			}
		}
		
		public long dollars() { 
			return d; 
		}
		
		public int cents() { 
			return c; 
		}
		
		public String toString() { 
			//if (c < 10)
			//	return d + ".0" + c;
			return d + "." + c; 
		}

		@Override
		public int compareTo(Money other) {
			// Complete this, if needed: Yeah for sure!

			if (this.d < other.d) 
				return -1;
			
			else if (this.d > other.d) 
				return 1;
			
			// When we have exact same dollar values
			else {
				
				if (this.c < other.c) 
					return -1;
				
				else if (this.c > other.c) 
					return 1;
				
				// same exact Money values 
				else 
					return 0;
			}
		}
	}
}
