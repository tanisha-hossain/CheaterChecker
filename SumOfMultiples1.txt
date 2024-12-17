import java.util.Scanner;

public class SumOfMultiples1 {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        sc.close();
        
        int sum = 0;
        
        for (int i = 1; i < n; i++) {
            if (i % 3 == 0 || i % 5 == 0) {
                sum += i;
            }
        }
        
        System.out.println(sum);
    }
}
