package edu.neu.coe.info6205.sort.par;

import java.util.Random;
import java.io.FileWriter;
import java.io.IOException;

public class bottomUpSortDriver {

	static final int td_count = 8;
	static final int min_size = 1048576;
	static final int max_size = 16777216;
	static final int min_cf = 16384;
	static final int max_cf = 134217728;

	public static void main(String[] args) {
		Random random = new Random();
		
		try {

			FileWriter wr = new FileWriter("resultsAssign5/parsort/data3.csv",false);

			wr.write("array_size,thread_count,cutoff,time_taken\n");

			for(int size = min_size; size<= max_size; size*=2) {

				for(int cutoff = min_cf; cutoff<= max_cf; cutoff*=2) {

					if(cutoff>size) continue;
					System.out.println("sorting a array size of "+size+" with cutoff "+cutoff);
					int[] arr1 = new int[size];
					for(int i=0;i<arr1.length;i++) arr1[i] = random.nextInt(100000);
					bottomUpPar str = new bottomUpPar(cutoff,arr1, td_count);
					long startTime = System.currentTimeMillis();
					str.sort();
					long elapsedTime = System.currentTimeMillis()-startTime;

					wr.write(size+","+ td_count +","+cutoff+","+elapsedTime+"\n");
				}
			}
			System.out.println("completed");
			wr.close();
		} catch (InterruptedException e) {

			e.printStackTrace();
		} catch (IOException e) {

			e.printStackTrace();
		}
		
	}

}