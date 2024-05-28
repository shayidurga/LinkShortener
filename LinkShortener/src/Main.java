import java.util.Scanner;
import java.util.InputMismatchException;

public class Main {
    public static void main(String[] args) {
        LinkShortener linkShortener = new LinkShortener();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("1. Shorten URL");
            System.out.println("2. Shorten URL with Custom Alias");
            System.out.println("3. Expand URL");
            System.out.println("4. URL Access Count");
            System.out.println("5. Exit");
            System.out.print("Choose an option: ");
            
            int choice = 0;
            boolean validInput = false;
            while (!validInput) {
                try {
                    choice = scanner.nextInt();
                    scanner.nextLine();  // consume newline
                    validInput = true;
                } catch (InputMismatchException e) {
                    System.out.print("Invalid input. Please enter a number between 1 and 5: ");
                    scanner.next(); // consume the invalid input
                }
            }

            switch (choice) {
                case 1:
                    System.out.print("Enter long URL: ");
                    String longURL = scanner.nextLine();
                    String shortURL = linkShortener.shortenURL(longURL, null);
                    System.out.println("Short URL: " + shortURL);
                    break;
                case 2:
                    System.out.print("Enter long URL: ");
                    longURL = scanner.nextLine();
                    System.out.print("Enter custom alias: ");
                    String customAlias = scanner.nextLine();
                    shortURL = linkShortener.shortenURL(longURL, customAlias);
                    System.out.println("Short URL: " + shortURL);
                    break;
                case 3:
                    System.out.print("Enter short URL: ");
                    String inputShortURL = scanner.nextLine();
                    String originalURL = linkShortener.expandURL(inputShortURL);
                    System.out.println("Original URL: " + originalURL);
                    break;
                case 4:
                    System.out.print("Enter short URL: ");
                    inputShortURL = scanner.nextLine();
                    int accessCount = linkShortener.getAccessCount(inputShortURL);
                    System.out.println("Access count: " + accessCount);
                    break;
                case 5:
                    System.out.println("Exiting...");
                    scanner.close();
                    return;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }
}
