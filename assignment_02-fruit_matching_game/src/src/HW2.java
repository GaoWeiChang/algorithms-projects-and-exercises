 
/* HW2. Fruits and hash tables
 * This file contains 7 classes:
 * 		- Row represents a row of fruits,
 * 		- CountConfigurationsNaive counts stable configurations naively,
 * 		- Quadruple manipulates quadruplets,
 * 		- HashTable builds a hash table,
 * 		- CountConfigurationsHashTable counts stable configurations using our hash table,
 * 		- Triple manipulates triplets,
 * 		- CountConfigurationsHashMap counts stable configurations using the HashMap of java.
 */


import java.util.HashMap;
import java.util.LinkedList;
import java.util.Vector;

class Row { // represent a row of fruits
	private final int[] fruits;

	// empty row constructor
	Row() {
		this.fruits = new int[0];
	}

	// constructor from the field fruits
	Row(int[] fruits) {
		this.fruits = fruits;
	}

	// equals method to compare the row to an object o
	@Override
	public boolean equals(Object o) {
		// we start by transforming the object o into an object of the class Row
		// here we suppose that o will always be of the class Row
		Row that = (Row) o;
		// we check if the two rows have the same length
		if (this.fruits.length != that.fruits.length)
			return false;
		// we check if the i-th fruits of the two rows coincide
		for (int i = 0; i < this.fruits.length; ++i) {
			if (this.fruits[i] != that.fruits[i])
				return false;
		}
		// we have the equality of the two rows
		return true;
	}

	// hash code of the row
	@Override
	public int hashCode() {
		int hash = 0;
		for (int i = 0; i < fruits.length; ++i) {
			hash = 2 * hash + fruits[i];
		}
		return hash;
	}

	// string representing the row
	@Override
	public String toString() {
		StringBuffer s = new StringBuffer();
		for (int i = 0; i < fruits.length; ++i)
			s.append(fruits[i]);
		return s.toString();
	}

	// Question 1

	// returns a new row by adding fruit to the end of the row
	Row extendedWith(int fruit) {
		// throw new Error("method extendedWith(int fruit) to be completed (Question 1)");
		int[] new_row = new int[fruits.length+1];
		for(int i=0; i<fruits.length; i++){
			new_row[i] = fruits[i];
		}
		new_row[fruits.length]=fruit;
		return new Row(new_row);
	}

	// helper function that check whether row is stable or not when we add fruit
	private static boolean isStableRow(Row row, int fruit) {
		int[] fruits = row.fruits;
		int len = fruits.length;
		
		if (len<2){ return true; }
		
		return !(fruits[len-1]==fruit && fruits[len-2]==fruit);
	}

	// return the list of all stable rows of width width
	static LinkedList<Row> allStableRows(int width) {
		// throw new Error("method allStableRows(int width) to be completed (Question 1)");
		LinkedList<Row> res = new LinkedList<>();

		if (width == 0){
			res.add(new Row());
			return res;
		}

		LinkedList<Row> prevRows = allStableRows(width-1);

		// add 0 or 1 in each previous row
		for(Row prev_row: prevRows){
			if(isStableRow(prev_row, 0)){
				res.add(prev_row.extendedWith(0));
			}

			if(isStableRow(prev_row, 1)){
				res.add(prev_row.extendedWith(1));
			}
		}

		return res;
	}

	// check if the row can be stacked with rows r1 and r2
	// without having three fruits of the same type adjacent
	boolean areStackable(Row r1, Row r2) {
		// throw new Error("method areStackable(Row r1, Row r2) to be completed (Question 1)");
		// check size are equal or not
		if((this.fruits.length != r1.fruits.length) || (this.fruits.length != r2.fruits.length)){
			return false;
		}
		
		for(int i=0; i<this.fruits.length; i++){
			// three fruit in column are the same
			if(this.fruits[i]==r1.fruits[i] && this.fruits[i]==r2.fruits[i]){ 
				return false; 
			}
		}

		return true;
	}
}

// Naive counting
class CountConfigurationsNaive {  // counting of stable configurations

	// Question 2

	// returning the number of grids whose first lines are r1 and r2,
	// whose lines are lines of rows and whose height is height
	static long count(Row r1, Row r2, LinkedList<Row> rows, int height) {
		// throw new Error("method count(Row r1, Row r2, LinkedList<Row> rows, int height) of the class CountConfigurationsHashNaive to be completed (Question 2)");
		if(height <= 1){
			return 0;
		}
		if(height==2){
			return 1;
		}
		
		long res = 0;
		for(Row r3: rows){
			if(r3.areStackable(r1, r2)){
				res += count(r2, r3, rows, height-1);
			}
		}

		return res;
	}

	// returning the number of grids with n lines and n columns
	static long count(int n) {
		// throw new Error("method count(int n) of the class CountConfigurationsHashNaive to be completed (Question 2)");
		if (n == 0) {
			return 1;
		}
		if (n==1){
			return 2;
		}

		LinkedList<Row> stable_rows = Row.allStableRows(n);
	
		long res = 0;
		for (Row r1 : stable_rows) {
			for (Row r2 : stable_rows) {
				if (r1.areStackable(r1, r2)) {  // Correct call
					res += count(r1, r2, stable_rows, n);
				}
			}
		}
	
		return res;
	}
}

// Construction and use of a hash table

class Quadruple { // quadruplet (r1, r2, height, result)
	Row r1;
	Row r2;
	int height;
	long result;

	Quadruple(Row r1, Row r2, int height, long result) {
		this.r1 = r1;
		this.r2 = r2;
		this.height = height;
		this.result = result;
	}
}

class HashTable { // hash table
	final static int M = 50000;
	Vector<LinkedList<Quadruple>> buckets;

	// Question 3.1

	// constructor
	HashTable() {
		// throw new Error("Constructor HashTable() to be completed (Question 3.1)");
		buckets = new Vector<LinkedList<Quadruple>>(M);

		for (int i=0;i<M;i++){
			buckets.add(new LinkedList<Quadruple>());
		}
	}

	// Question 3.2

	// return the hash code of the triplet (r1, r2, height)
	static int hashCode(Row r1, Row r2, int height) {
		// throw new Error("method hashCode(Row r1, Row r2, int height) to be completed (Question 3.2)");
		return r1.hashCode()*31 + r2.hashCode()*31*31+height; // Combining field hashcodes with prime numbers to avoid collision
	}

	// return the bucket of the triplet (r1, r2, height)
	int bucket(Row r1, Row r2, int height) {
		// throw new Error("method bucket(Row r1, Row r2, int height) to be completed (Question 3.2)");
		int hash_code = hashCode(r1, r2, height);

		return hash_code % M;
	}

	// Question 3.3

	// add the quadruplet (r1, r2, height, result) in the bucket indicated by the
	// method bucket
	void add(Row r1, Row r2, int height, long result) {
		// throw new Error("method add(Row r1, Row r2, int height, long result) to be completed (Question 3.3)");
		int bucket_idx = bucket(r1, r2, height);
		Quadruple q_ruple = new Quadruple(r1, r2, height, result);

		buckets.get(bucket_idx).add(q_ruple);
	}

	// Question 3.4

	// search in the table an entry for the triplet (r1, r2, height)
	Long find(Row r1, Row r2, int height) {
		// throw new Error("method Quadruple find(Row r1, Row r2, int height) to be completed (Question 3.4)");
		int bucket_idx = bucket(r1, r2, height);
		LinkedList<Quadruple> bucket = buckets.get(bucket_idx);

		for(Quadruple quad: bucket){
			if(quad.r1.equals(r1) && quad.r2.equals(r2) && quad.height==height){
				return Long.valueOf(quad.result);
			}
		}

		return null;
	}
}

class CountConfigurationsHashTable { // counting of stable configurations using our hash table
	static HashTable memo = new HashTable();

	// Question 4

	// return the number of grids whose first lines are r1 and r2,
	// whose lines are lines of rows and whose height is height
	// using our hash table
	static long count(Row r1, Row r2, LinkedList<Row> rows, int height) {
		// throw new Error("method count(Row r1, Row r2, LinkedList<Row> rows, int height) of the class CountConfigurationsHashTable to be completed (Question 4)");
		// base case
		if(height<=1){ return 0;}
		if(height==2){ return 1;}

		Long result = memo.find(r1, r2, height);
		if(result != null){
			return result;
		}

		// if cannot get result from memo, calculate it
		long sum=0;
		for(Row r3:rows){
			if(r3.areStackable(r1, r2)){
				sum += count(r2,r3,rows,height-1);
			}
		}

		// add the result in memo for future needed
		memo.add(r1,r2,height,sum);

		return sum;
	}

	// return the number of grids with n lines and n columns
	static long count(int n) {
		// throw new Error("method count(int n) of the class CountConfigurationsHashTable to be completed (Question 4)");
		LinkedList<Row> rows = Row.allStableRows(n);
		memo = new HashTable(); // clear hashtable for current calculation

		long total = 0;
		for(Row r1:rows){
			for(Row r2:rows){
				if(r1.areStackable(r1, r2)){
					total += count(r1,r2,rows,n);
				}
			}
		}

		return total;
	}
}

//Use of HashMap

class Triple { // triplet (r1, r2, height)
	// to be completed
	Row r1;
	Row r2;
	int height;

	Triple(Row r1, Row r2, int height){
		this.r1 = r1;
		this.r2 = r2;
		this.height = height;
	}

	@Override
	public boolean equals(Object o){
		if(this == o){
			return true;
		}
		// if(o == null || getClass()!=o.getClass()){
		// 	return false;
		// }

		Triple _obj = (Triple) o; // cast type to Triple
		return (this.height==_obj.height) && (this.r1.equals(_obj.r1)) && (this.r2.equals(_obj.r2));
	}

	@Override
	public int hashCode(){
		return HashTable.hashCode(r1, r2, height);
	}
}

class CountConfigurationsHashMap { // counting of stable configurations using the HashMap of java
	static HashMap<Triple, Long> memo = new HashMap<Triple, Long>();

	// Question 5

	// returning the number of grids whose first lines are r1 and r2,
	// whose lines are lines of rows and whose height is height
	// using the HashMap of java
	static long count(Row r1, Row r2, LinkedList<Row> rows, int height) {
		// throw new Error("method count(Row r1, Row r2, LinkedList<Row> rows, int height) of the class CountConfigurationsHashMap to be completed (Question 5)");
		
		// base case
		if(height<=1){
			return 0;
		}
		if(height==2){
			return 1;
		}

		Triple triple = new Triple(r1, r2, height);
		Long result = memo.get(triple);

		// if already has result, return true
		if(result != null){
			return result;
		}

		long sum = 0;
		for(Row r3: rows){
			if(r3.areStackable(r1, r2)){
				sum += count(r1, r2, rows, height-1);
			}
		}

		memo.put(triple, sum);
		return sum;
	}

	// return the number of grids with n lines and n columns
	static long count(int n) {
		// throw new Error("method count(int n) of the class CountConfigurationsHashMap to be completed (Question 5)");
		LinkedList<Row> rows = Row.allStableRows(n);

		long result = 0;
		for(Row r1: rows){
			for(Row r2: rows){
				if(r1.areStackable(r1, r2)){
					result += count(r1,r2,rows,n);
				}
			}
		}

		return result;
	}
}
