package org.gitools.ui.panels.matrix;

import java.util.Arrays;

public class SelectionUtils {

	public static int[] mergeWithoutRepetitions(int[] a1, int[] a2) {
		Arrays.sort(a1);
		Arrays.sort(a2);
		
		int[] a3 = new int[a1.length + a2.length];
		
		int i1 = 0;
		int i2 = 0;
		int i3 = 0;
		
		while (i1 < a1.length && i2 < a2.length) {
			int v1 = a1[i1];
			int v2 = a2[i2];
			int v3;
			
			if (v1 == v2) {
				v3 = v1; i1++; i2++;
			}
			else if (v1 < v2) {
				v3 = v1; i1++;
			}
			else {
				v3 = v2; i2++;
			}
			
			if (i3 != 0 && a3[i3] != v3)
				a3[i3++] = v3;
		}
		
		while (i1 < a1.length)
			a3[i3++] = a1[i1++];
		
		while (i2 < a2.length)
			a3[i3++] = a2[i2++];

		int[] a4 = new int[i3];
		System.arraycopy(a3, 0, a4, 0, i3);
		
		return a4;
	}
}
