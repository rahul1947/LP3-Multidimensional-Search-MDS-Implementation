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

# PROBLEM STATEMENT: 

   Multi-Dimensional Search: Consider the web site of a seller like Amazon. 
   They carry tens of thousands of products, and each product has many 
   attributes (Name, Size, Description, Keywords, Manufacturer, Price, etc.). 

   The search engine allows users to specify attributes of products that they 
   are seeking and shows products that have most of those attributes. To make 
   the search efficient, the data is organized using appropriate data structures, 
   such as balanced trees. 

   But, if products are organized by Name, how can search by price implemented 
   efficiently? 

   The solution, called indexing in databases, is to create a new set of 
   references to the objects for each search field and organize them to 
   implement search operations on that field efficiently. 

   As the objects change, these access structures have to be kept consistent. 

   In this project, each object has 3 attributes: id (long int), description 
   (one or more long ints), and price (dollars and cents). 

   The following operations are supported:

   a. insert(id, price, list): 
      Insert a new item whose description is given in the list. 
      If an entry with the same id already exists, then its description and 
      price are replaced by the new values, unless the list is null or empty, 
      in which case, just the price is updated. Returns 1 if the item is new, 
      and 0 otherwise.

   b. find(id): 
      Return price of item with given id (or 0, if not found).

   c. delete(id): 
      Delete an item from storage. Returns the sum of the long ints that are 
      in the description of the item deleted (or 0, if such an id did not exist).

   d. findMinPrice(n): 
      Given a long int, find items whose description contains that number 
      (exact match with one of the long ints in the item's description), and 
      return the lowest price of those items. Return 0 if there is no such item.

   e. findMaxPrice(n): 
      Given a long int, find items whose description contains that number, and 
      return the highest price of those items. Return 0 if there is no such item.

   f. findPriceRange(n, low, high): 
      Given a long int n, find the number of items whose description contains 
      n, and in addition, their prices fall within the given range, [low, high].

   g. priceHike(l, h, r): 
      Increase the price of every product, whose id is in the range [l,h] by r%. 
      Discard any fractional pennies in the new prices of items. Note that you are 
      truncating, not rounding. Returns the sum of the net increases in the prices.

   h. removeNames(id, list): 
      Remove elements of the list from the description of id. It is possible 
      that some of the items in the list are not in the id's description. 
      Return the sum of the numbers that are actually deleted from the 
      description of id. Return 0 if there is no such id.

Implement the operations using data structures that are best suited for the problem.

INPUT SPECIFICATION:
   
   Initially, the store is empty, and there are no items. The input contains a 
   sequence of lines (use test sets with millions of lines). 

   Lines starting with "#" are comments. 

   Other lines have one operation per line: name of the operation, followed by 
   parameters needed for that operation (separated by spaces). 

   Lines with Insert operation will have a "0" at the end, that is not part of 
   the name. The output is a single number, which is the sum of the following 
   values obtained by the algorithm as it processes the input.


SAMPLE INPUT:

Insert 22 19.97 475 1238 9742 0
# New item with id=22, price="$19.97", name="475 1238 9742"
# Return: 1
#
Insert 12 96.92 44 109 0
# Second item with id=12, price="96.92", name="44 109"
# Return: 1
#
Insert 37 47.44 109 475 694 88 0
# Another item with id=37, price="47.44", name="109 475 694 88"
# Return: 1
#
PriceHike 10 22 10
# 10% price increase for id=12 and id=22
# New price of 12: 106.61, Old price = 96.92.  Net increase = 9.69
# New price of 22: 21.96.  Old price = 19.97.  Net increase = 1.99
# Return: 11.68  (sum of 9.69 and 1.99).  Added to total: 11
#
FindMaxPrice 475     
# Return: 47.44 (id of items considered: 22, 37).  Added to total: 47
#
Delete 37
# Return: 1366 (=109+475+694+88)
#
FindMaxPrice 475     
# Return: 21.96 (id of items considered: 22).  Added to total: 21
#
_______________________________________________________________________________

# DESCRIPTION: 

1. For each product/ item, we've made a class called Item consisting - 
   class Item { Long id, Money price, HashSet<Long> description }
   Why HashSet? - to avoid duplicates and fast modifications

2. We've used a TreeMap<Long, <Item>> which maps an id to its Item.
   
   We could've also used TreeSet, but wanted to go with TreeMap, as it does 
   not make a huge difference.
   
   TreeMap<Long, <Item>> pTree;

3. We've used a HashMap<Long, HashSet<Item>> which maps a description to a set 
   of all such Items containing that description. 
   
   Why HashSet? - we initially used TreeSet having our own natural ordering on 
   price and id, but it turned out to be less efficient because the price was 
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
- Time and Memory might change, as you run the test the program on a different 
  system, but they could be comparable to the above values.
  
  Existing Processor: Intel® Core™ i5-8250U CPU @ 1.60GHz × 8
  Memory: 7.5 GiB
  
  Refer lp3-script-results.txt as obtained from 
  $ ./lp3-script.sh > lp3-script-results.txt

- You might need to allocate sufficient memory for the test programs like for 
  lp3-t17 (3 GiB) and lp3-t18 (4 GiB).
_______________________________________________________________________________

# HOW TO RUN:

1. Extract the rsn170330.zip 

2. Compile: 
  $javac rsn170330/*.java

3. Run:
  $java [memory: optional] rsn170330.LP3Driver [arg1] [arg2] 
  $java rsn170330.LP3Driver lp3-test/lp3-t16.txt false
  
  OR 
  $java [memory: optional] rsn170330.TLP3Driver [arg1] [arg2] [arg3: optional]
  $java -Xms4g rsn170330.LP3Driver lp3-test/lp3-t17.txt false x
    
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
   
   *We were missing a pair of parenthesis in toMoney() which caused int to 
   overflow and store incorrect dollar values.
   toString() in Money didn't consider single digit cent values.

3. Version 3: correct results for all given inputs, but took too long to run
   
   Optimized by changing from TreeSet to HashSet. 
   
   As we were unsure of Items in Hash stored as references, we used to 
   redundantly update the same Item with a helper method.
   Used subMap() for priceHike().
_______________________________________________________________________________