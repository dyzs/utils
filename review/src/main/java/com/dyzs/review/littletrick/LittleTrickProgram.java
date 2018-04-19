package com.dyzs.review.littletrick;


/**
 * @author dyzs, created on 2018/4/9.
 */

public class LittleTrickProgram {
    public static void main(String[] args) {

        int[] arrs = new int[]{1,9,15,6,8};
        bubbleSortAsc(arrs);

        bubbleSortDesc(arrs);

    }
    public static int binarySearch(int[] nums, int key) {
        int start = 0, end = nums.length - 1, mid = -1;
        while (start <= end) {
            mid = (start + end) / 2;
            if (nums[mid] == key) {
                return mid;
            } else if (nums[mid] > key) {
                end = mid - 1;
            } else if (nums[mid] < key) {
                start = mid + 1;
            }
        }
        return -1;
    }

    /**
     * 冒泡降序
     * @param arr
     */
    public static void bubbleSortDesc(int[] arr) {
        for (int i = 0; i < arr.length - 1; i++) {
            for (int j = i + 1; j < arr.length; j++) {
                if (arr[i] < arr[j]) {
                    int temp = arr[i];
                    arr[i] = arr[j];
                    arr[j] = temp;
                }
            }
        }
        System.out.print("{");
        for (int k = 0; k < arr.length; k++) {
            System.out.print(arr[k] + ",");
        }
        System.out.print("}");
    }

    /**
     * 冒泡升序
     * @param arr
     */
    public static void bubbleSortAsc(int[] arr) {
        for (int i = 0; i < arr.length - 1; i++) {
            for (int j = i + 1; j < arr.length; j++) {
                if (arr[i] > arr[j]) {
                    int temp = arr[i];
                    arr[i] = arr[j];
                    arr[j] = temp;
                }
            }
        }
        System.out.print("{");
        for (int k = 0; k < arr.length; k++) {
            System.out.print(arr[k] + ",");
        }
        System.out.print("}");
    }

}
