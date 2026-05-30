import java.io.IOException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        CaesarCipher cipher = new CaesarCipher();
        FileService fileService = new FileService();

        System.out.println("=== МЕНЮ CAESAR CIPHER TOOL ===");
        System.out.println("1. Зашифровать файл (нужен ключ)");
        System.out.println("2. Расшифровать файл (нужен ключ)");
        System.out.println("3. Взломать файл перебором (Brute Force)");
        System.out.println("4. Взломать файл статанализом (Statistical Analysis)");
        System.out.println("0. Выход");
        System.out.print("Выберите режим работы: ");

        int choice = scanner.nextInt();
        scanner.nextLine(); // очистка сканера после считывания числа

        if (choice == 0) {
            System.out.println("Программа завершена.");
            return;
        }

        System.out.print("Введите путь к исходному файлу: ");
        String sourcePath = scanner.nextLine();

        System.out.print("Введите путь для сохранения результата: ");
        String destPath = scanner.nextLine();

        try {
            // читаем входной текст
            String text = fileService.readFile(sourcePath);
            String resultText;

            // обрабатываем выбранный режим с помощью конструкции switch
            switch (choice) {
                case 1 -> {
                    System.out.print("Введите ключ шифрования (число от 1 до " + (cipher.getAlphabetSize() - 1) + "): ");
                    int key = scanner.nextInt();
                    resultText = cipher.encrypt(text, key);
                    System.out.println("✔ Текст успешно зашифрован!");
                }
                case 2 -> {
                    System.out.print("Введите ключ для расшифровки: ");
                    int key = scanner.nextInt();
                    resultText = cipher.decrypt(text, key);
                    System.out.println("Текст успешно расшифрован!");
                }
                case 3 -> {
                    resultText = cipher.bruteForce(text);
                    System.out.println("Взлом методом Brute Force успешно завершен!");
                }
                case 4 -> {
                    resultText = cipher.statisticalAnalysis(text);
                    System.out.println("Взлом методом статанализа успешно завершен!");
                }
                default -> {
                    System.out.println("Неверный режим работы.");
                    return;
                }
            }

            // записываем полученный текст в выходной файл
            fileService.writeFile(destPath, resultText);
            System.out.println("Результат успешно записан в файл: " + destPath);

        } catch (IOException | IllegalArgumentException e) {
            System.err.println("Ошибка выполнения: " + e.getMessage());
        }
    }
}
