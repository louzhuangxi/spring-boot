package org.examples.j2se.collection;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;

import java.util.*;

public class CollectionUtilsSample {

	/**
	 * @param args
	 */
	@SuppressWarnings({ "unchecked"})
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String[] arrayA = new String[] { "1", "2", "3", "3", "4", "5" };
		String[] arrayB = new String[] { "3", "4", "4", "5", "6", "7" };

		List<String> a = Arrays.asList(arrayA);
		List<String> b = Arrays.asList(arrayB);

		// === list
		//并集    
		Collection<String> l_union = CollectionUtils.union(a, b);
		//交集    
		Collection<String> l_intersection = CollectionUtils.intersection(a, b);
		//交集的补集    
		Collection<String> l_disjunction = CollectionUtils.disjunction(a, b);
		//集合相减
		Collection<String> l_subtract = CollectionUtils.subtract(a, b);

		Collections.sort((List<String>) l_union);
		Collections.sort((List<String>) l_intersection);
		Collections.sort((List<String>) l_disjunction);
		Collections.sort((List<String>) l_subtract);

		// === set

		String[] arrayC = new String[] { "1", "2", "3", "4", "5" };
		String[] arrayD = new String[] { "3", "4", "5", "6", "7" };

		TreeSet<String> c = new TreeSet<String>();
		CollectionUtils.addAll(c, arrayC);
		TreeSet<String> d = new TreeSet<String>();
		CollectionUtils.addAll(d, arrayD);
		
		//并集
		Collection<String> s_union = CollectionUtils.union(c, d);
		//交集
		Collection<String> s_intersection = CollectionUtils.intersection(c, d);
		//交集的补集
		Collection<String> s_disjunction = CollectionUtils.disjunction(c, d);
		//集合相减
		Collection<String> s_subtract = CollectionUtils.subtract(c, d);

//		Collections.sort((List<String>) s_union);
//		Collections.sort((List<String>) s_intersection);
//		Collections.sort((List<String>) s_disjunction);
//		Collections.sort((List<String>) s_subtract);

		System.out.println("List =========");
		System.out.println("A: " + ArrayUtils.toString(a.toArray()));
		System.out.println("B: " + ArrayUtils.toString(b.toArray()));
		System.out.println("--------------------------------------------");
		System.out.println("List: Union(A, B) 并集 : "
				+ ArrayUtils.toString(l_union.toArray()));
		System.out.println("List: Intersection(A, B) 交集 : "
				+ ArrayUtils.toString(l_intersection.toArray()));
		System.out.println("List: Disjunction(A, B) 交集的补集: "
				+ ArrayUtils.toString(l_disjunction.toArray()));
		System.out.println("List: Subtract(A, B) 集合相减  : "
				+ ArrayUtils.toString(l_subtract.toArray()));

		System.out.println("Set =========");
		System.out.println("C: " + ArrayUtils.toString(c.toArray()));
		System.out.println("D: " + ArrayUtils.toString(d.toArray()));
		System.out.println("--------------------------------------------");
		System.out.println("Set: Union(C, D) 并集 : "
				+ ArrayUtils.toString(s_union.toArray()));
		System.out.println("Set: Intersection(C, D) 交集 : "
				+ ArrayUtils.toString(s_intersection.toArray()));
		System.out.println("Set: Disjunction(C, D) 交集的补集: "
				+ ArrayUtils.toString(s_disjunction.toArray()));
		System.out.println("Set: Subtract(C, D) 集合相减  : "
				+ ArrayUtils.toString(s_subtract.toArray()));
	}
}