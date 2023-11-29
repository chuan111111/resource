import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;


public class student {
        public static void main(String[] args) {
            QReader in = new QReader();
            int t= in.nextInt();
            int k= in.nextInt();
            int count=0;
            int[] arr=new int[t];
            while (count<t) {
                arr[count]=in.nextInt();
                count++;
            }
            int length = arr.length;
            for(int i=0;i<length;i++){
                int iRandNum = (int)(Math.random() * length);
                int temp = arr[iRandNum];
                arr[iRandNum] = arr[i];
                arr[i] = temp;
            }
          int result=  findKthLargest(arr,k);
            System.out.print(result);
        }
        public static int findKthLargest(int[] nums, int k) {
            if (k < 1 || nums == null) {
                return 0;
            }

            return getKth(nums.length - k + 1, nums, 0, nums.length - 1);
        }

        public static int getKth(int k, int[] nums, int start, int end) {

            int pivot = nums[end];

            int left = start;
            int right = end;

            while (true) {

                while (nums[left] < pivot && left < right) {
                    left++;
                }

                while (nums[right] >= pivot && right > left) {
                    right--;
                }

                if (left == right) {
                    break;
                }

                swap(nums, left, right);
            }

            swap(nums, left, end);

            if (k == left + 1) {
                return pivot;
            } else if (k < left + 1) {
                return getKth(k, nums, start, left - 1);
            } else {
                return getKth(k, nums, left + 1, end);
            }
        }

        public static void swap(int[] nums, int n1, int n2) {
            int tmp = nums[n1];
            nums[n1] = nums[n2];
            nums[n2] = tmp;
        }

    static class QReader {
        private BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        private StringTokenizer tokenizer = new StringTokenizer("");

        private String innerNextLine() {
            try {
                return reader.readLine();
            } catch (IOException e) {
                return null;
            }
        }
        public boolean hasNext() {
            while (!tokenizer.hasMoreTokens()) {
                String nextLine = innerNextLine();
                if (nextLine == null) {
                    return false;
                }
                tokenizer = new StringTokenizer(nextLine);
            }
            return true;
        }
        public String next() {
            hasNext();
            return tokenizer.nextToken();
        }
        public int nextInt() {
            return Integer.parseInt(next());
        }

    }

}
