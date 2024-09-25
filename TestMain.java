package com.example.demo;

import java.util.*;

public class TestMain {


    private static List<List<Integer>> kSum(List<Integer> nums, int target, int k, int start) {
        List<List<Integer>> result = new ArrayList<>();
        int len = nums.size();
        
        if (k == 1) {
        	// if it is reduced to 1-sum problem. then just find the 
        	// element in the list and add it to result after creating a list.
        	nums.subList(start, nums.size() - 1)
            	.stream()
            	.filter(x -> x == target)
            	.forEach(p -> result.add(Arrays.asList(p)));

        } else { 
            // Recursive case: reduce k-sum to (k-1)-sum
            for (int i = start; i < len - (k - 1); i++) {
                
                int newTarget = target - nums.get(i);
                List<List<Integer>> subResult = kSum(nums, newTarget, k - 1, i + 1);
                for (List<Integer> subList : subResult) {
                    List<Integer> newList = new ArrayList<>(subList);
                    newList.add(0, nums.get(i));
                    result.add(newList);
                }
            }
        }

        return result;
    }

    public static void main(String[] args) {
        List<Integer> nums = Arrays.asList(3,9,12,6,8,18,28,38);
        int target = 44;
        int n = 4;  // Example: 4-sum

        List<List<Integer>> result = kSum(nums, target, n, 0);
        result.forEach(System.out::println);
    }
}
