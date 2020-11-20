package edu.neu.coe.info6205.union_find;

import java.io.FileWriter;
import java.io.IOException;
import java.util.function.Consumer;
import java.util.function.UnaryOperator;

import edu.neu.coe.info6205.util.Benchmark_Timer;

public class Assgn4 {
	private static final int N = 8;
	private static final int runs_of_N = 100;
	private static final int initial_N = 5;
	
	public Assgn4() {
		
	}	
    
	private static int count(int n, int runs,int pathCompression) {
		int size = n;
		double av = 0;
		for(int i=0;i<runs;i++) {
			UF h = null;
			if (pathCompression==0) h = new UF_HWQUPC(size,false);
			if (pathCompression==1) h = new UF_HWQUPC(size,true);
			if (pathCompression==2) h = new WQUPC(size);
			int pairs=0;
			while(h.components()>1) {
				int[] temp = computeRandomPair(size);
				h.connect(temp[0], temp[1]);
				pairs++;
			}
			av+= pairs;
		}
		return (int)(av/runs);
	}
	
    
	private static double[] computeUnionFind(int n,int pathCompression) {
		int size = n;
		double[] ans = new double[2];
		double depth = 0;
		double pairs = 0;
		for(int i=0;i<runs_of_N;i++) {
			UF h = null;
			if (pathCompression==0) h = new UF_HWQUPC(size,false);
			if (pathCompression==1) h = new UF_HWQUPC(size,true);
			if (pathCompression==2) h = new WQUPC(size);
			while(h.components()>1) {
				int[] temp = computeRandomPair(size);
				h.connect(temp[0], temp[1]);
				pairs++;
			}
			if(pathCompression==2) depth +=((WQUPC) h).depthAverage();
			else depth +=((UF_HWQUPC) h).depthAverage();
		}
		ans[0] = depth/runs_of_N;
		ans[1] = pairs/runs_of_N;
		return ans;
	}
	
	
	 
		private static int[] computeRandomPair(int n) {
			int[] ans = new int[2];
			ans[0] = (int)(Math.random()*n);
			ans[1] = (int)(Math.random()*n);
			//if(ans[0]==ans[1]) ans = computeRandomPair(n);
			return ans;
		}
	
	public static void main (String[] args) {
		
		//pre function unused
    	UnaryOperator<Integer> pre = inp -> inp;
    	
    	//function to be benchmarked
    	Consumer<Integer> func1 = inp -> count(inp,1,0);
    	Consumer<Integer> func2 = inp -> count(inp,1,1);
    	Consumer<Integer> func3 = inp -> count(inp,1,2);
    	
    	//post function unused
    	Consumer<Integer> post = inp -> System.out.print("");
    	
    	Benchmark_Timer<Integer> t1 = new Benchmark_Timer<>("WQU Benchmark",pre,func1,post);   //no compression
    	Benchmark_Timer<Integer> t2 = new Benchmark_Timer<>("WQUPH Benchmark",pre,func2,post); //path halving
    	Benchmark_Timer<Integer> t3 = new Benchmark_Timer<>("WQUPC Benchmark",pre,func3,post); //path compression

    	try {
			FileWriter writer = new FileWriter("resultsAssignment4/union_find/assignment4.csv");
			writer.write("n,uncompressedtime,uncompressedavdepth,uncompressedpairs, pathhalvedtime,pathhalvedavdepth,pathhalvedpairs,compressedtime,compressedavdepth,compressedpairs,\n");

			for(int i=0;i<N;i++) {
				System.out.println(i);
				int k = (int)Math.pow(2, i);
				k = k*initial_N;
				double[] temp;
				writer.write(k+",");
				writer.write(t1.run(k,runs_of_N)+",");
				temp = computeUnionFind(k,0);
				writer.write(temp[0]+",");
				writer.write(temp[1]+",");
				writer.write(t2.run(k,runs_of_N)+",");
				temp = computeUnionFind(k,1);
				writer.write(temp[0]+",");
				writer.write(temp[1]+",");
				writer.write(t3.run(k,runs_of_N)+",");
				temp = computeUnionFind(k,2);
				writer.write(temp[0]+",");
				writer.write(temp[1]+"\n");
			}
			writer.close();
    	}
    	catch (IOException e) {
    		e.printStackTrace();
    	}
	}
}
