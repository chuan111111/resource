package lab3;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


interface MyPredicate<T> {
    boolean test(T t);
}

// Higher-order function to filter a list based on a predicate
public class FilterProgram {
    public static <T> List<T> filter(List<T> list, MyPredicate<T> p) {
        List<T> result = new ArrayList<>();
        for (T item : list) {
            if (p.test(item)) {
                result.add(item);
            }
        }
        return result;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("Please input the function no:");
            System.out.println("1 - Get even numbers");
            System.out.println("2 - Get odd numbers");
            System.out.println("3 - Get prime numbers");
            System.out.println("0 - Quit");

            int choice = scanner.nextInt();
            if (choice == 0) {
                break;
            }

            System.out.println("Input the integer list:");
            scanner.nextLine();
            String line = scanner.nextLine();
            List<Integer> numbers = new ArrayList<>();
            for (String s : line.split(" ")) {
                numbers.add(Integer.parseInt(s));
            }

            List<Integer> filterResults;

            switch (choice) {
                case 1:
                    filterResults = filter(numbers, n -> n % 2 == 0);
                    break;
                case 2:
                    filterResults = filter(numbers, n -> n % 2 != 0);
                    break;
                case 3:
                    filterResults = filter(numbers, FilterProgram::isPrime);
                    break;
                default:
                    filterResults = new ArrayList<>();
                    break;
            }

            System.out.println("Filter results: " + filterResults);
        }

        scanner.close();
    }

    // Helper method to check if a number is prime
    private static boolean isPrime(int number) {
        if (number <= 1) return false;
        for (int i = 2; i <= Math.sqrt(number); i++) {
            if (number % i == 0) return false;
        }
        return true;
    }
}