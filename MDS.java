package rsn170330.lp3;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.TreeMap;
import java.util.TreeSet;


// If you want to create additional classes, place them in this file 
// as subclasses of MDS
public class MDS {
	// Add fields of MDS here
	
	// pTree: maps id to it's product
	TreeMap<Long, Item> pTree;
	
	// priceMap: maps a price to the product(s) having that price 
	//HashMap<Money, LinkedList<Item>> priceMap;
	
	// dMap: maps a description long integer to all such products 
	// having that description number
	HashMap<Long, TreeSet<Item>> dMap;
	
	private int size;
	private Money zeroMoney = new Money();
	
	// Constructors
	public MDS() {
		pTree = new TreeMap<>();
		dMap = new HashMap<>();
		size = 0;
	}
	
	static class Item {
		
		long id;
		List<Long> description;
		Money price;
		
		public Item() {
			id = 0;
			description = new LinkedList<Long>(); 
			price = new Money();
		}
		
		// Parameterized Constructor:
		public Item(long id, Money p, List<Long> list) {
			this.id = id;
			this.price = p;
			this.description = new LinkedList<Long>();
			
			for (Long e: list) {
				this.description.add(e);
			}
		}
		
		// Returns the id of the item
		public long getId() {
			return this.id;
		}
	}

	// Public methods of MDS. Do not change their signatures.
	// -----------------------------------------------------------------
	
	/**
	 * insert(id, price, list): 
	 * Insert a new item whose description is given in the list. 
	 * 
	 * If an entry with the same id already exists, then its 
	 * description and price are replaced by the new values, unless list 
	 * is null or empty, in which case, just the price is updated. 
	 * 
	 * @param id
	 * @param price
	 * @param list
	 * @return Returns 1 if the item is new, and 0 otherwise.
	 */
	public int insert(long id, Money price, java.util.List<Long> list) {
		return 0;
	}
	
	
	/**
	 * find(id): return price of item with given id (or 0, if not found).
	 * @param id
	 * @return price of the item, if any, else 0
	 */
	public Money find(long id) {
		
		// When there is no such item (id) with us
		if (pTree.containsKey(id)) 
			return pTree.get(id).price; // returning price of that item
		
		// return 0
		return zeroMoney;
	}

	/**
	 * delete(id): delete item from storage. 
	 * 
	 * @param id
	 * @return Returns the sum of the long integers that are in the 
	 * description of the item deleted, or 0, if such an id did not exist.
	 */
	public long delete(long id) {
		
		// When there is no such item (id) with us
		if (!pTree.containsKey(id)) 
			return 0;
		
		// r: the Item to be removed
		Item r = pTree.get(id);
		
		long sum = 0; // sum of descriptions of r to be returned
		
		// For each description d, of that Item r
		for (Long d: r.description) {
			
			// tempSet: TreeSet corresponding to that description d
			TreeSet<Item> tempSet = dMap.get(d);
			tempSet.remove(r); // remove r from the set
			
			// When tempSet is emptied
			if (tempSet.size() == 0) 
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
	 * @param d
	 * @return Return 0 if there is no such item.
	 */
	public Money findMinPrice(long d) {
		
		// When there no such description 
		if (!dMap.containsKey(d)) 
			return zeroMoney; // return 0
		
		// NOTE: need the natural ordering of TreeSet<Item> on price***
		return dMap.get(d).first().price;
	}
	
	/**
	 * findMaxPrice(d): given a long integer, find items whose description 
	 * contains that number, and return highest price of those items.
	 * 
	 * @param d
	 * @return Return 0 if there is no such item.
	 */
	public Money findMaxPrice(long d) {
		// When there no such description 
		if (!dMap.containsKey(d)) 
			return zeroMoney; // return 0
		
		// NOTE: need the natural ordering of TreeSet<Item> on price***
		return dMap.get(d).last().price;
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
		if (low.compareTo(high) > 0)
			return 0;
		
		TreeSet<Item> tempSet = dMap.get(d);
		
		if (tempSet.first().price.compareTo(high) > 0)
			return 0;
		
		if (tempSet.last().price.compareTo(low) < 0)
			return 0;
		
		int count = 0;
		
		if (low.compareTo(tempSet.first().price) > 0) {
			
			Iterator<Item> itr = tempSet.iterator();
			
			// while price < low
			while (itr.hasNext()) {
				
				// starting from ceiling(low)
				if (itr.next().price.compareTo(low) < 0) 
					continue;
				
				break;
			}
			count = 1; // as itr is on the first Item in tempSet
			
			if (high.compareTo(tempSet.last().price) < 0) {
				
				while (itr.hasNext()) {
					// until you reach floor(high)
					if (itr.next().price.compareTo(high) <= 0) {
						count++;
						continue;
					}
					break;
				}
				return count;
			}
			
			else {
				// until you reach the last()
				while (itr.hasNext()) {
					count++;
					itr.next();
				}
				return count;
			}
			
		}
		
		else {
			if (high.compareTo(tempSet.last().price) < 0) {
				// start from first()
				Iterator<Item> itr = tempSet.iterator();
				
				while (itr.hasNext()) {
					count++;
					
					// until you reach floor(high)
					if (itr.next().price.compareTo(high) > 0) 
						break;
				}
				return count;
			}
			
			// for all elements in tempSet
			return tempSet.size();
		}
			
		
		
		return 0;
	}

	/**
	 * priceHike(l, h, r): increase the price of every product, 
	 * whose id is in the range [l,h] by r%. 
	 * 
	 * Discard any fractional pennies in the new prices of items. 
	 * 
	 * @param l lower limit
	 * @param h higher limit
	 * @param rate 
	 * @return Returns the sum of the net increases of the prices.
	 */
	public Money priceHike(long l, long h, double rate) { 
		
		return new Money();
	}
	
	/**
	 * removeNames(id, list): Remove elements of list from the 
	 * description of id. It is possible that some of the items in 
	 * the list are not in the id's description. 
	 * 
	 * @param id 
	 * @param list
	 * @return Return the sum of the numbers that are actually deleted 
	 * from the description of id. 
	 * Return 0 if there is no such id.
	 */
	public long removeNames(long id, java.util.List<Long> list) {
		return 0;
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
		
		public int compareTo(Money other) {
			// Complete this, if needed: Yeah for sure!
			
			if (this.d < other.d) {
				return -1;
			}
			else if (this.d > other.d) {
				return 1;
			}
			else {
				if (this.c < other.c) {
					return -1;
				}
				else if (this.c > other.c) {
					return 1;
				}
				else {
					return 0;
				}
			}
		}
	
		public String toString() { 
			return d + "." + c; 
		}
		
	}
}

