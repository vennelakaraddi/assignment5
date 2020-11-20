package edu.neu.coe.info6205.sort.par;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

class bottomUpPar{

	public int cutoff;
	public int[] arr;
	public ExecutorService pool;

	public bottomUpPar(int cutoff, int[] arr, int poolSize) {
		this.cutoff = cutoff;
		this.arr = arr;
		pool = Executors.newFixedThreadPool(poolSize);
	}

	private void merge(int from,int mid,int to) {
		if(mid>=to) return;
		if(arr[mid]>=arr[mid-1]) return;
		int[] temp = new int[to-from+1];
		for(int i=from;i<=to;i++) {
			temp[i-from] = arr[i];
		}

		int l = 0;
		int m = mid-from;
		int r = m;
		int counter=from;
		while(l<m || r<temp.length) {
			if(l>=m) arr[counter++] = temp[r++];
			else if(r>=temp.length) arr[counter++] = temp[l++];
			else if(temp[l]<=temp[r]) arr[counter++] = temp[l++];
			else arr[counter++] = temp[r++];
		}
	}

	private void sort(int from,int to) {
		Arrays.sort(arr, from, to);
	}


	public void sort() throws InterruptedException {
		int sort_segment_start = 0;
		List<Callable<Void>> tasks = new ArrayList<>();
		while(sort_segment_start<arr.length) {
			final int start = sort_segment_start;
			final int end = Math.min(sort_segment_start+cutoff,arr.length);
			tasks.add(new Callable<Void>() {
				public Void call() {
					sort(start,end);
					return null;
				}
			});
			sort_segment_start+=cutoff;
		}
		pool.invokeAll(tasks);


		int size = cutoff;
		while(size<=arr.length) {
			int merge_segment_start = 0;
			while(merge_segment_start<arr.length) {
				final int start = merge_segment_start;
				final int mid = Math.min(start+size,arr.length-1);
				final int end = Math.min(start+(size*2)-1,arr.length-1);
				tasks.add(new Callable<Void>() {
					public Void call() {
						merge(start,mid,end);
						return null;
					}
				});
				merge_segment_start+=size*2;
			}
			pool.invokeAll(tasks);
			size*=2;
		}
		pool.shutdown();

	}
}