public class RabinKarp {
	private long patHash; // pattern hash
	private int M; // pattern length
	private long Q; // large prime number
	private int R = 256; // alphabet size
	private long RM; // R^{M-1} % Q (used in removing the leading digit)
	public RabinKarp(String pattern) {
		this.M = pattern.length();
		this.Q = randomLongPrime();
		this.RM = 1;

		// R^{M-1} % Q = R * R * ... * R (M-1 times) % Q
		// but "% Q" can be applied at each multiplication (does not change the result)
		for (int i = 1; i <= pattern.length() - 1; i++) {
			RM = (R * RM) % Q;
		}

		this.patHash = hash(pattern, M);
	}

	/*
	pattern: a pattern to search in a string
	M: length of the pattern
	=> value of the pattern beginning at position i: x_i = \sum_{k=i}{M-1} t_k \cdot R^{(M-1) - k}
	=> hash: x_i % Q
	@return the hash of the pattern: O(M)
	 */
	private long hash(String pattern, int M) {
		// using Horner rule
		long hash = 0;
		for (int k = 0; k <= M-1; k++) {
			hash = (pattern.charAt(k) + R * hash) % Q;
		}

		return hash;
	}

	/*
	txt: a long string
	@return the position of the first char of the pattern in the string txt
	 */
	private int search(String txt) {
		int N = txt.length();

		// hash the first M char of the long string
		long txtHash = hash(txt, M);

		// same hash => pattern is at position 0 (i.e. beginning of the string)
		if (patHash == txtHash) {
			return 0;
		}

		// otherwise compute the next window hash
		for (int i = M; i < N; i++) {
			// remove leading digit
			// add an extra Q to make sure everything is positive
			txtHash = (txtHash + Q - txt.charAt(i - M) * RM % Q) % Q;
			// add trailing digit
			txtHash = (txtHash * R + txt.charAt(i)) % Q;

			if (patHash == txtHash) {
				return i - M + 1;
			}
		}

		// not found
		return N;
	}

	public static void main(String[] args) {
		System.out.println("Hello world!");
	}
}