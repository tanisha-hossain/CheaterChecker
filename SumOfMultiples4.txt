import java.util.Scanner;

public class SumOfMultiples4 {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        sc.close();
        
        int sum = 0;
        
        for (int i = 1; i < n; i++) {
            if (i % 5 == 0 || i % 3 == 0) {
                sum += i;
            }
        }
        
        System.out.println(sum);
    }
}
