import java.util.Scanner;

public class SumOfMultiples5 {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        sc.close();
        
        int result = 0;
        
        for (int i = 1; i < n; i++) {
            if (i % 3 == 0 || i % 5 == 0) {
                result += i;
            }
        }
        
        System.out.println(result);
    }
}
