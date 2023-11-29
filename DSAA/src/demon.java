import java.io.*;
import java.util.*;

public class demon {
    static int n;
    static int m;
    static char[][] graph;
    static int[][] isVisited;


    public static void main(String[] args) {
        QReader in = new QReader();
        QWriter out = new QWriter();
        while (true){

            n= in.nextInt();
            m= in.nextInt();
            if(n==-1&&m==-1){break;}
            graph= new char[n][m];
            isVisited= new int[n][m];
            Queue<int[]> queue= new LinkedList<>();
            boolean finals=true;
            for(int i=0;i<n;i++){
                String str= in.next();
                for(int j=0;j<m;j++){
                    char k=str.charAt(j);
                    graph[i][j]= k;
                    if (k=='S'){
                        queue.add(new int[]{i,j});
                        isVisited[i][j]= 1;
                    }
                }
            }

            while (!queue.isEmpty()){
                int[] node= queue.poll();
                int a=node[0];
                int b=node[1];
                if (a-1>0){
                    int vX=a-1;
                    int vY=b;
                    int x= vX%n;
                    int y= vY%m;
                    if(graph[x][y]!='#'&&isVisited[x][y]==0){
                        isVisited[x][y]= 1;
                        queue.add(new int[]{vX,vY});
                    }}
                if (b-1>0){
                    int vX=a;
                    int vY=b-1;
                    int x= vX%n;
                    int y= vY%m;
                    if(graph[x][y]!='#'&&isVisited[x][y]==0){
                        isVisited[x][y]= 1;
                        queue.add(new int[]{vX,vY});
                    }
                }
                if (a+1>0){
                    int vX=a+1;
                    int vY=b;
                    int x= vX%n;
                    int y= vY%m;
                    if(graph[x][y]!='#'&&isVisited[x][y]==0){
                        isVisited[x][y]= 1;
                        queue.add(new int[]{vX,vY});
                    }
                }
                if (b+1>0){
                    int vX=a;
                    int vY=b+1;
                    int x= vX%n;
                    int y= vY%m;
                    if(graph[x][y]!='#'&&isVisited[x][y]==0){
                        isVisited[x][y]= 1;
                        queue.add(new int[]{vX,vY});
                    }
                }

            }
            loop:
            for(int i=0;i<n;i++){
                for(int j=0;j<m;j++){
                    if(isVisited[i][j]==0){
                      finals=false;
                      break loop;
                    }
                }
            }
            if (finals){
                out.println("Yes");
            }else {
                out.println("No");
            }

        }
        out.close();
    }

    static void bfs(){

    }
    static   class QReader {
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

        public String nextLine() {
            tokenizer = new StringTokenizer("");
            return innerNextLine();
        }

        public String next() {
            hasNext();
            return tokenizer.nextToken();
        }

        public int nextInt() {
            return Integer.parseInt(next());
        }

        public long nextLong() {
            return Long.parseLong(next());
        }
    }

    static class QWriter implements Closeable {
        private BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(System.out));

        public void print(Object object) {
            try {
                writer.write(object.toString());
            } catch (IOException e) {
                return;
            }
        }

        public void println(Object object) {
            try {
                writer.write(object.toString());
                writer.write("\n");
            } catch (IOException e) {
                return;
            }
        }

        @Override
        public void close() {
            try {
                writer.close();
            } catch (IOException e) {
                return;
            }
        }
    }
}