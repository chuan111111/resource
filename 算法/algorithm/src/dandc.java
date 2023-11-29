import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;

public class dandc {
   static int[] result=new int[2];
   static HashMap<Integer,Integer> map=new HashMap<>();
    public static void main(String[] args) {
        Scanner in=new Scanner(System.in);

            String s1=in.nextLine();
            String[] s2=s1.split(" ");
            int[] t= Arrays.stream(s2).mapToInt(Integer::parseInt).toArray();

        for (int i = 0; i < t.length; i++) {
            map.put(t[i],i);
        }
        int[] tmp = new int[t.length];    //新建一个临时数组存放
        dandc(t,0,t.length-1,tmp);
        int r=result[1]-result[0];
        System.out.println(map.get(result[0])+" "+map.get(result[1])+" "+r);
        }
        public static void divideandc(int[] arr,int low,int mid,int high,int[] tmp ){

            int i = 0;
            int j = low,k = mid+1;
            if (map.get(arr[mid])>map.get(arr[k])){
                if (arr[mid]-arr[k]>result[1]-result[0]){
                    result[1]=arr[mid];
                    result[0]=arr[k];
                }
            }
            if (map.get(arr[high])>map.get(arr[j])){
                if (arr[high]-arr[j]>result[1]-result[0]){
                    result[1]=arr[high];
                    result[0]=arr[j];
                }
            }

                //左边序列和右边序列起始索引
            while(j <= mid && k <= high){
                if(arr[j] < arr[k]){
                    tmp[i++] = arr[j++];
                }else{
                    tmp[i++] = arr[k++];
                }
            }
            //若左边序列还有剩余，则将其全部拷贝进tmp[]中
            while(j <= mid){
                tmp[i++] = arr[j++];
            }

            while(k <= high){
                tmp[i++] = arr[k++];
            }

            for(int t=0;t<i;t++){
                arr[low+t] = tmp[t];
            }


        }
        public static void dandc(int[] arr,int low,int high,int[] tmp){
            if(low<high){
                int mid = (low+high)/2;
                dandc(arr,low,mid,tmp); //对左边序列进行归并排序
                dandc(arr,mid+1,high,tmp);  //对右边序列进行归并排序
                divideandc(arr,low,mid,high,tmp);    //合并两个有序序列
            }

        }
    }

