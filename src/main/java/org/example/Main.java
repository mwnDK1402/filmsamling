import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        System.out.println("This program echoes your input, in order to test whether IO works.");

        var in = new Scanner(System.in);
        while (true) {
            var line = in.nextLine();
            if (line.isBlank()) break;
            System.out.println(line);
        }

        System.out.println("Goodbye!");
    }
}
