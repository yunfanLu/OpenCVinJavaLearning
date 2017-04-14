package xyz.ds.base.groups;

/**
 * @author onion
 * @param <Key>
 */
@SuppressWarnings("rawtypes")
public class MaxPQ<Key extends Comparable> {
	private Key[] pq;
	private int N = 0;

	@SuppressWarnings("unchecked")
	public MaxPQ(int max_N) {
		pq = (Key[]) new Comparable[max_N + 1];
	}

	public boolean isEmpty() {
		return N == 0;
	}

	public int size() {
		return N;
	}

	@SuppressWarnings("unchecked")
	private void resize(int leng) {
		Key[] t = (Key[]) new Comparable[leng + 1];
		for (int i = 1; i <= N; i++) {
			t[i] = pq[i];
		}
		pq = t;
	}

	public void insert(Key V) {
		if ((N + 1) == pq.length)
			resize(2 * pq.length);
		pq[++N] = V;
		swim(N);
	}

	private void swim(int k) {
		while (k > 1 && less(k / 2, k)) {
			exch(k / 2, k);
			k = k / 2;
		}
	}

	private void exch(int i, int j) {
		Key t = pq[i];
		pq[i] = pq[j];
		pq[j] = t;
	}

	@SuppressWarnings("unchecked")
	private boolean less(int i, int j) {
		return pq[i].compareTo(pq[j]) < 0;
	}

	public Key delMax() {
		if (N > 0 && N == pq.length / 4)
			resize(pq.length / 2);
		Key max = pq[1];
		exch(1, N--);
		pq[N + 1] = null;
		sink(1);
		return max;
	}

	private void sink(int k) {
		while (2 * k <= N) {
			int j = 2 * k;
			if (j < N && less(j, j + 1))
				j++;
			if (less(k, j) == false)
				break;
			exch(k, j);
			k = j;
		}
	}

}
