/**
 * Original code:
 * Copyright ©️ 2000–2017, Robert Sedgewick and Kevin Wayne.
 * <p>
 * Modifications:
 * Copyright (c) 2017. Phasmid Software
 */
package edu.neu.coe.info6205.union_find;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

/**
 * Height-weighted Quick Union with Path Compression
 */
public class UF_HWQUPC implements UF {
	
	 private static final Random random = new Random();
    /**
     * Ensure that site p is connected to site q,
     *
     * @param p the integer representing one site
     * @param q the integer representing the other site
     */
    public void connect(int p, int q) {
        if (!isConnected(p, q)) union(p, q);
    }

    /**
     * Initializes an empty union–find data structure with {@code n} sites
     * {@code 0} through {@code n-1}. Each site is initially in its own
     * component.
     *
     * @param n               the number of sites
     * @param pathCompression whether to use path compression
     * @throws IllegalArgumentException if {@code n < 0}
     */
    public UF_HWQUPC(int n, boolean pathCompression) {
        count = n;
        parent = new int[n];
        height = new int[n];
        for (int i = 0; i < n; i++) {
            parent[i] = i;
            height[i] = 1;
        }
        this.pathCompression = pathCompression;
    }

    /**
     * Initializes an empty union–find data structure with {@code n} sites
     * {@code 0} through {@code n-1}. Each site is initially in its own
     * component.
     * This data structure uses path compression
     *
     * @param n the number of sites
     * @throws IllegalArgumentException if {@code n < 0}
     */
    public UF_HWQUPC(int n) {
        this(n, true);
    }

    public void show() {
        for (int i = 0; i < parent.length; i++) {
            System.out.printf("%d: %d, %d\n", i, parent[i], height[i]);
        }
    }
    
    public double depthAverage() {
    	double result = 0.0;
    	for(int i =0;i<parent.length;i++) {
    		int temp = i;
    		while(parent[temp]!=temp) {
    			result+=1;
    			temp = parent[temp];
    		}
    	}
    	return result/parent.length;
    }

    /**
     * Returns the number of components.
     *
     * @return the number of components (between {@code 1} and {@code n})
     */
    public int components() {
        return count;
    }

    /**
     * Returns the component identifier for the component containing site {@code p}.
     *
     * @param p the integer representing one site
     * @return the component identifier for the component containing site {@code p}
     * @throws IllegalArgumentException unless {@code 0 <= p < n}
     */
    public int find(int p) {
    	validate(p);
        int root = p;
        while (root != parent[root]) {
        	if(pathCompression) {
        		parent[root] = parent[parent[root]];
        	}
            root = parent[root];
        }
        if (!pathCompression) {
            return root;
        }
        
        return root;
    }

    /**
     * Returns true if the the two sites are in the same component.
     *
     * @param p the integer representing one site
     * @param q the integer representing the other site
     * @return {@code true} if the two sites {@code p} and {@code q} are in the same component;
     * {@code false} otherwise
     * @throws IllegalArgumentException unless
     *                                  both {@code 0 <= p < n} and {@code 0 <= q < n}
     */
    public boolean connected(int p, int q) {
        return find(p) == find(q);
    }

    /**
     * Merges the component containing site {@code p} with the
     * the component containing site {@code q}.
     *
     * @param p the integer representing one site
     * @param q the integer representing the other site
     * @throws IllegalArgumentException unless
     *                                  both {@code 0 <= p < n} and {@code 0 <= q < n}
     */
    public void union(int p, int q) {
        // CONSIDER can we avoid doing find again?
        mergeComponents(find(p), find(q));
        count--;
    }

    @Override
    public int size() {
        return parent.length;
    }

    /**
     * Used only by testing code
     *
     * @param pathCompression true if you want path compression
     */
    public void setPathCompression(boolean pathCompression) {
        this.pathCompression = pathCompression;
    }

    @Override
    public String toString() {
        return "UF_HWQUPC:" + "\n  count: " + count +
                "\n  path compression? " + pathCompression +
                "\n  parents: " + Arrays.toString(parent) +
                "\n  heights: " + Arrays.toString(height);
    }

    // validate that p is a valid index
    private void validate(int p) {
        int n = parent.length;
        if (p < 0 || p >= n) {
            throw new IllegalArgumentException("index " + p + " is not between 0 and " + (n - 1));
        }
    }

    private void updateParent(int p, int x) {
        parent[p] = x;
    }

    private void updateHeight(int p, int x) {
        height[p] += height[x];
    }

    /**
     * Used only by testing code
     *
     * @param i the component
     * @return the parent of the component
     */
    private int getParent(int i) {
        return parent[i];
    }

    private final int[] parent;   // parent[i] = parent of i
    private final int[] height;   // height[i] = height of subtree rooted at i
    private int count;  // number of components
    private boolean pathCompression;

    private void mergeComponents(int i, int j) {
        // TO BE IMPLEMENTED make shorter root point to taller one
    	System.out.println("before merge - "+ this.toString());
    	if (height[i] < height[j]) {
    		parent[i] = j;
            height[j] += height[i];
        } else {
        	parent[j] = i;
            height[i] += height[j];
        }
    	System.out.println("After merge - "+ this.toString());
    }

    /**
     * This implements the single-pass path-halving mechanism of path compression
     */
    private void doPathCompression(int i) {
        // TO BE IMPLEMENTED update parent to value of grandparent
    	int root = find(i);
    	
    	parent[root] = parent[parent[root]];
    }
    
    static void doPathCompression(int p, int root, int[] parent) {
        // TODO update parent if appropriate
        //throw new RuntimeException("not implemented");

        while (p != root) {
            int newp = parent[p];
            parent[p] = root;
            p = newp;
        }
    }
    
//    public static void main(String args[]) {
//
//    	try {
//			FileWriter writer = new FileWriter("resultsAssignment4/union_find/assignment4.csv");
//			writer.write("n,pairs\n");
//
//			for(int i=0;i<8;i++) {
//				System.out.println(i);
//				int t = (int)Math.pow(2, i);
//				t = t*5;
//				writer.write(t+",");
//				writer.write(count(t,2000)+"\n");
//			}
//			writer.close();
//    	}
//    	catch (IOException e) {
//    		e.printStackTrace();
//    	}
//    }
    
    private static int[] randomPair(int n) {
		int[] ans = new int[2];
		
		ans[0] = (int)(Math.random()*n);
		ans[1] = (int)(Math.random()*n);
		//if(ans[0]==ans[1]) ans = randomPair(n);
		return ans;
	}
	
	private static int count(int n, int runs) {
		int size = n;
		double av = 0;
		for(int i=0;i<runs;i++) {
			UF h = new UF_HWQUPC(size, true);
			int pairs=0;
			while(h.components()>1) {
				int[] temp = randomPair(size);
				h.connect(temp[0], temp[1]);
				System.out.println(temp[0]+" "+temp[1]+" "+pairs+" "+h.components());
				pairs++;
			}
			av+= pairs;
			System.out.println("Average : : "+av);
		}
		return (int)(av/runs);
	}
}