# Long Project LP3: Multidimensional search (MDS) Implementation

# Team: LP101 
 * Rahul Nalawade (https://github.com/rahul1947) 
   rsn170330@utdallas.edu 
 * Prateek Sarna (https://github.com/prateek5795)  
   pxs180012@utdallas.edu 
 * Bhavish Khanna Narayanan (https://github.com/bhavish14) 
   bxn170002@utdallas.edu 

# End Date: 
 * Sunday, November 04, 2018
_______________________________________________________________________________

# DESCRIPTION: 

1. For each product/ item, we've made a class called Item consisting - 
   class Item { Long id, Money price, HashSet<Long> description }
   Why HashSet? - to avoid duplicates and fast modifications

2. We've used a TreeMap<Long, <Item>> which maps an id to it's Item.
   
   We could've also used TreeSet, but wanted to go with TreeMap, as it 
   does not make a huge difference.
   
   TreeMap<Long, <Item>> pTree;

3. We've used a HashMap<Long, HashSet<Item>> which maps a description to a 
   set of all such Items containing that description. 
   
   Why HashSet? - we initially used TreeSet having our own natural ordering 
   on price and id, but it turned out to be less efficient, because price was 
   changing a lot of times. We changed to HashSet, ordered only on id, as we 
   decided to go without ordering. 

   HashMap<Long, HashSet<Item>> dMap;

4. Note that, each Item is stored only in TreeMap (pTree). HashMap (dMap)
   contains only the references of the Items which are mapped by the 
   descriptions.
_______________________________________________________________________________

# RESULTS: 

+-------------------------------------------------------------------------+
| File    | # Operations |   Output*  | Time (mSec) | Memory (used/avail) |
|-------------------------------------------------------------------------|
| lp3-t01 |            7 |        135 |          10 |       1 MB / 117 MB |
|-------------------------------------------------------------------------|
| lp3-t02 |           10 |         32 |          11 |       1 MB / 117 MB |
|-------------------------------------------------------------------------|
| lp3-t03 |           12 |        203 |           5 |       1 MB / 117 MB |
|-------------------------------------------------------------------------|
| lp3-t04 |           57 |        426 |          19 |       2 MB / 117 MB |
|-------------------------------------------------------------------------|
| lp3-t05 |          100 |        340 |          30 |       4 MB / 117 MB |
|-------------------------------------------------------------------------|
| lp3-t06 |          607 |       6192 |          44 |      11 MB / 117 MB |
|-------------------------------------------------------------------------|
| lp3-t07 |         1000 |       7982 |          49 |       5 MB / 117 MB |
|-------------------------------------------------------------------------|
| lp3-t08 |         1000 |       4335 |          94 |      29 MB / 117 MB |
|-------------------------------------------------------------------------|
| lp3-t09 |         1203 |     10 218 |          84 |      20 MB / 117 MB |
|-------------------------------------------------------------------------|
| lp3-t10 |         5000 |     56 894 |          85 |      18 MB / 117 MB |
|-------------------------------------------------------------------------|
| lp3-t11 |       10 000 |     39 776 |         396 |      99 MB / 208 MB |
|-------------------------------------------------------------------------|
| lp3-t12 |      100 000 |    909 627 |        4826 |     403 MB / 747 MB |
|-------------------------------------------------------------------------|
| lp3-t13 |      100 000 |  1 890 671 |        3675 |     200 MB / 769 MB |
|-------------------------------------------------------------------------|
| lp3-t14 |      200 000 |  1 286 719 |       11512 |    754 MB / 1112 MB |
|-------------------------------------------------------------------------|
| lp3-t15 |      200 201 |    101 256 |       16614 |    826 MB / 1395 MB |
|-------------------------------------------------------------------------|
| lp3-t16 |      300 000 |  2 062 306 |       19551 |    914 MB / 1364 MB |
|-------------------------------------------------------------------------|
| lp3-t17 |      500 000 |  4 913 652 |       26277 |   2092 MB / 2841 MB |
|-------------------------------------------------------------------------|
| lp3-t18 |      750 000 | 14 070 859 |       86304 |   2552 MB / 3873 MB |
+-------------------------------------------------------------------------+

* These are the results obtained using TLP3Driver as the Driver code. 

NOTE: 
- Time and Memory might change, as you run the test the program on a 
  different system, but they could be comparable to the above values.
  
  Existing Processor: Intel® Core™ i5-8250U CPU @ 1.60GHz × 8
  Memory: 7.5 GiB
  
  Refer lp3-script-results.txt as obtained from 
  $ ./lp3-script.sh > lp3-script-results.txt

- You might need to allocate sufficient memory for the test programs like 
  for lp3-t17 (3 GiB) and lp3-t18 (4 GiB).
_______________________________________________________________________________

# How to Run:

1. Extract the rsn170330.zip 

2. Compile: 
  $javac rsn170330/*.java

3. Run:
  $java [memory: optional] rsn170330.LP3Driver [arg1] [arg2] 
  $java rsn170330.LP3Driver test-lp3/lp3-t16.txt false
  
  OR 
  $java [memory: optional] rsn170330.TLP3Driver [arg1] [arg2] [arg3: optional]
  $java -Xms4g rsn170330.LP3Driver test-lp3/lp3-t17.txt false x
	
Note:
[memory: optional] -Xms3g and -Xms4g can be used only for files 
  lp3-t17.txt and lp3-t18.txt

[arg1] is the input test file

[arg2] keep true when you need output in verbose, otherwise false

[arg3: optional] if arg3 present prints '.' after every 10k operations and '+' 
  after every 100k lines, otherwise no '.' and '+'. Can be used for debugging. 
_______________________________________________________________________________

# DEBUGGING:

1. Version 1: t03, t07, t10 were giving incorrect results. 
   Need to correct logic for priceHike() findMaxPrice() and findMinPrice()

2. Version 2: correct Output for files lp3-t01 to lp3-t11, but incorrect for 
   lp3-t12.txt
   
   Changed description from LinkedList<> to HashSet<>.
   Used long for manipulating money values internally.
   
   *We were missing a pair of parenthesis in toMoney() which caused int 
   to overflow and store incorrect dollar values.
   toString() in Money didn't considered single digit cent values.

3. Version 3: correct results for all given inputs, but took too long to run
   
   Optimized by changing from TreeSet to HashSet. 
   
   As we were unsure of Items in Hash stored as references, we used to 
   redundantly update the same Item with a helper method.
   Used subMap() for priceHike().
_______________________________________________________________________________