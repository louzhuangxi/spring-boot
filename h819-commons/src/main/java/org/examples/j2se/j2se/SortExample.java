package org.examples.j2se.j2se;

/**
 * @ClassName: SortExample
 * @Description: TODO(演示各种 java 中的排序)
 * @author h819
 * @date May 8, 2009 10:59:34 AM
 * @version V1.0
 */
public class SortExample {

	// JAVA四种基本排序,包括冒泡法,插入法,选择法,SHELL排序法.其中选择法是冒泡法的改进,SHELL排序法是
	// 插入法的改进.所以从根本上来说可以归纳为两种不同的排序方法:即:插入法＆冒泡法
	//
	// 一
	// 插入法:遍历排序集合，每到一个元素时，都要将这个元素与所有它之前的元素遍历比较一遍，
	// 让符合排序顺序的元素挨个移动到当前范围内它最应该出现的位置。
	// 交换是相邻遍历移动，双重循环控制实现.
	// 这种排序法属于地头蛇类型,在我的地牌上我要把所有的东西按一定的顺序规整,过来一个,规整一个.
	// 处理代码如下:
	
	public void sort(int[] data) {
		int temp;
		for (int i = 1; i < data.length; i++) {
			for (int j = i; (j > 0) && (data[j] < data[j - 1]); j--) {
				temp = data[j];
				data[j] = data[j - 1];
				data[j - 1] = temp;
			}
		}
	}

	// 二冒泡法:比较容易，它的内层循环保证遍历一次后，集合中最小（大）元素出现在它的正确位置，下一次就是次小元素。。。
	// 该方法在集合分布的各种情况下交换移动的次数基本不变，属于最慢的一种排序。实现也是双重循环控制。
	// 这种排序法属于过江龙,就是要找到极端,但是过奖龙也有大哥,二哥等,所以他们只能是大哥挑了二哥挑.
	// 处理代码如下:
	public static int[] maopao(int[] data) {
		int temp;
		for (int i = 0; i < data.length - 1; i++) {
			for (int j = i + 1; j < data.length; j++) {
				if (data[i] < data[j]) {
					temp = data[i];
					data[i] = data[j];
					data[j] = temp;
				}
			}
		}

		return data;
	}

	// 三选择法:该方法只是通过遍历集合记录最小（大）元素的位置，一次遍历完后，再进行交换位置操作，类似冒泡，
	// 但在比较过程中，不进行交换操作，只记录元素位置。一次遍历只进行一次交换操作。
	// 这个对与交换次序比较费时的元素比较适合。这种排序法比冒泡法要城府要深的多,我先记住极端数据,
	// 待遍历数据完了之后,我再处理,不像冒泡法那样只要比自己极端一点的就要处理,选择法只处理本身范围内的最极端数据.
	public static void xuanze(int[] data) {
		int temp;
		for (int i = 0; i < data.length; i++) {
			int lowIndex = i;
			for (int j = data.length - 1; j > i; j--) {
				if (data[j] > data[lowIndex]) {
					lowIndex = j;
				}
			}
			temp = data[i];
			data[i] = data[lowIndex];
			data[lowIndex] = temp;
		}
	}

	// 四 Shell排序：
	// 它是对插入排序的一种改进，是考虑将集合元素按照一定的基数划分成组去排序，让每一组在局部范围内先排成基本有序，最后在进行一次所有元素的插入排序。
	public void sort2(int[] data) {
		for (int i = data.length / 2; i > 2; i /= 2) {
			for (int j = 0; j < i; j++) {
				insertSort(data, j, i);
			}
		}
		insertSort(data, 0, 1);
	}

	private void insertSort(int[] data, int start, int inc) {
		int temp;
		for (int i = start + inc; i < data.length; i += inc) {
			for (int j = i; (j >= inc) && (data[j] < data[j - inc]); j -= inc) {
				temp = data[j];
				data[j] = data[j - inc];
				data[j - inc] = temp;
			}
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		SortExample se = new SortExample();

		int[] a = { 1, 4, 0, 5, 8, 10,5 };

		se.sort(a);
		for (int i : a)
			System.out.println(i);

	}

}
