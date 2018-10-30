package rsn170330.lp3;

import java.util.Iterator;
import java.util.TreeSet;

public class Tester {

	public static void main(String[] args) {
		
		TreeSet<Integer> t = new TreeSet<>();
		
		for (int i=0; i<10; i++) {
			t.add(i*i);
		}
		
		for (Integer e: t) {
			System.out.print(e+" ");
		}
		
		System.out.println();
		
		Iterator<Integer> it = t.iterator();
		int count = 0;
		
		while (it.hasNext()) {
			if (it.next().compareTo(25) < 0)
				continue;
			break;
		}
		count = 1;
		
		while(it.hasNext()) {
			if (it.next().compareTo(64) <= 0) {
				count++;
				continue;
			}
			break;
			//System.out.print(it.next()+" ");
		}
		
		System.out.println();
		System.out.println(count);
	}

}
