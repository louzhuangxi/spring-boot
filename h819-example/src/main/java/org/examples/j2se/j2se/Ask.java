package org.examples.j2se.j2se;

/**
 * @ClassName: Ask
 * @Description: TODO(记录一些疑问问题)
 * @author h819
 * @date Sep 2, 2009 3:14:04 PM
 * @version V1.0
 */
public class Ask {

	public void test() {

		// 存放返回结果的结构
		int[] r = new int[2];
		int i = 0;
		while (i < 10) {
			r[0] = i;
			r[1] = i + 1;

			System.out.print("i="+i+"  "+"r[0] :" + r[0] + "  ");
			System.out.println("r[1] :" + r[1]);
			i++;
		}

	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		Ask as = new Ask();
		as.test();

	}

}
