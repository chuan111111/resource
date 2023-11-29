import java.util.Arrays;
import java.util.Scanner;



public class t {
  static   class Fruit implements Comparable<Fruit> {
        int l, r, v, slot;

        @Override
        public int compareTo(Fruit other) {
            return Integer.compare(this.l, other.l);
        }
    }
    static final int N = 5000 + 5;
    static Fruit[] fruit = new Fruit[N];
    static int[] ActiveTimeslot = new int[N];
    static int[] OccupyTimeslot = new int[N];
    static int n;

    static boolean cmp1(Fruit x, Fruit y) {
        return x.l < y.l;
    }

    static boolean cmp2(Fruit x, Fruit y) {
        return x.v > y.v;
    }

    static int linearMatch(int i, int timeslotId) {
        if (ActiveTimeslot[timeslotId] > fruit[i].r) return 0;
        if (OccupyTimeslot[timeslotId] == 0) {
            OccupyTimeslot[timeslotId] = i;
            return 1;
        }
        if (fruit[i].r > fruit[OccupyTimeslot[timeslotId]].r)
            return linearMatch(i, timeslotId + 1);
        if (linearMatch(OccupyTimeslot[timeslotId], timeslotId + 1) == 1) {
            OccupyTimeslot[timeslotId] = i;
            return 1;
        }
        return 0;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        n = scanner.nextInt();
        for (int i = 1; i <= n; i++) {
            fruit[i] = new Fruit();
            fruit[i].l = scanner.nextInt();
            fruit[i].r = scanner.nextInt();
            fruit[i].v = scanner.nextInt();
        }

        Arrays.sort(fruit, 1, n + 1);

        for (int i = 1; i <= n; i++) {
            ActiveTimeslot[i] = Math.max(ActiveTimeslot[i - 1] + 1, fruit[i].l);
        }

        for (int i = 1, j = 1; i <= n; fruit[i].slot = j, i++) {
            while (ActiveTimeslot[j] < fruit[i].l && j <= n) j++;
        }

        long ans = 0;

        Arrays.sort(fruit, 1, n + 1, (x, y) -> Integer.compare(y.v, x.v));

        for (int i = 1; i <= n; i++) {
            ans += fruit[i].v * linearMatch(i, fruit[i].slot);
        }

        System.out.println(ans);
    }
}
