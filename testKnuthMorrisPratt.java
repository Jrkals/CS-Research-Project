import java.util.ArrayList;

public class testKnuthMorrisPratt {

	public static void main(String[] args) {
		String T = "hello w";
		String P = "llo w";
		KMPMatcher(T, P);
		System.out.println("done");
	}
	
	// Knuth Morris Pratt implementation
	static void KMPMatcher(String T, String P){
		int n = T.length();
		int m = P.length();
		int[] pi = ComputePrefixFunction(P);
		int q = 0;
		ArrayList<Integer> matches = new ArrayList<>();
		for(int i = 0; i < n; i++) {
			System.out.println("i is "+i+ " q is "+q);
			while(q > 0 && P.charAt(q+1) != T.charAt(i)) {
				System.out.print("pi[q] is "+pi[q]+ ", P[q+1] is "+P.charAt(q+1));
				System.out.println(", T[i] is "+ T.charAt(i));
				q = pi[q];
			}
			if(P.charAt(q+1) == T.charAt(i)) {
				System.out.print("T[i] is "+T.charAt(i) + " and ");
				System.out.println("P[q+1] is "+P.charAt(q+1));
				q++;
			}
			if(q == m-1) {
				System.out.println("*******************************");
				System.out.println("Patter occurs with shift "+ (i-m+1));
				System.out.println("*******************************");
				matches.add(i);
				q = pi[q];
			}
		}
	}
	
	//Prefix function for Knuth Morris Pratt
	static int[] ComputePrefixFunction(String P) {
		int m = P.length();
		int[] pi = new int[m];
		pi[0] = -1;
		int k = -1;
		for(int q = 1; q < m; q++) {
			while(k >= 0 && P.charAt(k+1) !=P.charAt(q)) {
				k = pi[k];
			}
			if(P.charAt(k+1) == P.charAt(q)) {
				k++;
			}
			pi[q] = k;
		}
		System.out.println("done with prefixing, array is :");
		printArray(pi);
		return pi;
	}
	
	static void printArray(int[] a) {
		for (int i = 0; i < a.length; i++) {
			System.out.print(a[i]+ " ");
		}
		System.out.println();
	}


}
