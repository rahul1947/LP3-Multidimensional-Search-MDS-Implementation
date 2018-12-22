<<<<<<< HEAD
# Long Project LP3: Multidimensional search (MDS) Implementation

# Team: LP101 
 * Rahul Nalawade (rsn170330)
 * Prateek Sarna (pxs180012)
 * Bhavish Khanna Narayanan (bxn170002)

# End Date: 
 * Sunday, November 04, 2018
___________________________________________________________________________

# DESCRIPTION: 

1. For each product/ item, we've made a class called Item consisting - 
   class Item { Long id, Money price, HashSet<Long> description }
   Why HashSet - to avoid duplicates and fast modifications

2. We've used a TreeMap<Long, <Item>> which maps an id to it's Item.
   
   We could've also used TreeSet, but wanted to go with TreeMap, as it 
   would not make a huge difference.
   
   TreeMap<Long, <Item>> pTree;

3. We've used a HashMap<Long, HashSet<Item>> which maps a description to a 
   set of all such Items containing that description. 
   
   Why HashSet? - we initially used TreeSet having our own natural ordering 
   on price and id, but it turned out to be less efficient. We changed to 
   HashSet as we decided to go without ordering. 

   HashMap<Long, HashSet<Item>> dMap;

4. Note that, each Item is stored only in TreeMap (pTree). HashMap (dMap)
   contains only the references of the Items which are mapped by the 
   descriptions.
___________________________________________________________________________

# RESULTS: 

+-------------------------------------------------------------------------+
| File         | Output          |   Time (mSec)     | Memory (used/avail)|
|-------------------------------------------------------------------------|
| 401          | 1448            | 13                | 1 MB / 117 MB      |
|-------------------------------------------------------------------------|
| 402          | 4142            | 6                 | 1 MB / 117 MB      |
|-------------------------------------------------------------------------|
| 403          | 11132           | 21                | 2 MB / 117 MB      |
|-------------------------------------------------------------------------|
| 404          | 52018           | 50                | 5 MB / 117 MB      |
|-------------------------------------------------------------------------|
| 405          | 16494           | 58                | 10 MB / 117 MB     |
|-------------------------------------------------------------------------|
| 406          | 19005           | 77                | 21 MB / 117 MB     |
|-------------------------------------------------------------------------|
| 407          | 489174          | 89                | 20 MB / 117 MB     |
|-------------------------------------------------------------------------|
| 420          | 1016105100      | 17653             | 1095 MB / 1407 MB  |
|-------------------------------------------------------------------------|
| lp3-big-100k | 565883014879    | 3795              | 200 MB / 770 MB    |
|-------------------------------------------------------------------------|
| lp3-big-200k | 12957774        | 14013             | 632 MB / 1071 MB   |
|-------------------------------------------------------------------------|
| lp3-big-300k | 29000824        | 19709             | 1015 MB / 1325 MB  |
|-------------------------------------------------------------------------|
| lp3-big-500k | 303607214       | 25720             | 2075 MB / 3723 MB  |
+-------------------------------------------------------------------------+

NOTE: 
- Time and Memory might change, as you run the test the program on a different 
  system, but they could be comparable to the above values.
  
  Existing Processor: Intel® Core™ i5-8250U CPU @ 1.60GHz × 8
  Memory: 7.5 GiB
  
- A screenshot has been included for reference for the above table.

- You might need to allocate sufficient memory for the test programs 
  like for lp3-big-500k.txt, we allocated 4 GBs for the run.

___________________________________________________________________________

# How to Run:

1. Extract the rsn170330.zip 

2. Compile: 
	$javac rsn170330/*.java

3. Run: 
	$java -Xss512m -Xms4g rsn170330.LP3Driver test-lp3/lp3-big-500k.txt
___________________________________________________________________________

# DEBUGGING:

1. Version 1: 402, 404, 407 were giving incorrect results.
   
   Need to correct logic for priceHike() findMaxPrice() and findMinPrice()

2. Version 2: correct Output for 401-420, but incorrect for lp3-big-100k
   
   Changed description from LinkedList<> to HashSet<>.
   Used long for manipulating money values internally.
   *We were missing a pair of parenthesis in toMoney() which caused int 
   to overflow and store incorrect dollar values.
   toString() in Money didn't considered single digit cent values.

3. Version 3: correct results for all given inputs, but took long to run
   
   Optimized by changing from TreeSet to HashSet. 
   toString() in Money didnt considered single digit cent values.

3. Version 3: correct results for all given inputs, but took long to run
   
   Optimised by changing from TreeSet to HashSet. 
   As we were unsure of Items in Hash stored as references, we used to 
   redundantly update the same Item with a helper method.
   Used subMap() for priceHike().
___________________________________________________________________________