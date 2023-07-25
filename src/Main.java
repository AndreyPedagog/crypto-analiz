import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {

    private static final ArrayList<Character> ukrainianAlphabet = new ArrayList<>(List.of('А', 'Б', 'В', 'Г', 'Ґ', 'Д', 'Е', 'Є', 'Ж', 'З',
            'И', 'І', 'Ї', 'Й', 'К', 'Л', 'М', 'Н', 'О', 'П', 'Р', 'С', 'Т', 'У', 'Ф', 'Х', 'Ц', 'Ч', 'Ш', 'Щ', 'Ь',
            'Ю', 'Я', 'а', 'б', 'в', 'г', 'ґ', 'д', 'е', 'є', 'ж', 'з', 'и', 'і', 'ї', 'й', 'к', 'л', 'м', 'н', 'о',
            'п', 'р', 'с', 'т', 'у', 'ф', 'х', 'ц', 'ч', 'ш', 'щ', 'ь', 'ю', 'я', ' ', '.', ',', '"', '?', ':', '-'));

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        System.out.println("Виберіть дію, яку ви хочете зробити: ");
        System.out.println("1 - Зашифрувати текст");
        System.out.println("2 - Розшифрувати текст");
        System.out.println("3 - Криптоаналіз методом brute force");

        int choice = scanner.nextInt();
        scanner.nextLine();


        switch (choice) {
            case 1 -> encryptFile(scanner);
            case 2 -> decryptFile(scanner);
            case 3 -> bruteForceDecrypt(scanner);
            default -> System.out.println("Такої функції не інснує, виберіть 1, 2 або 3");
        }
    }
        public static void encryptFile (Scanner scanner) {
            while (true) {

                System.out.println("Введіть шлях до текстового файлу, який треба зашифрувати: ");

                try {

                    Path path = Path.of(scanner.nextLine());
                    String file = Files.readString(path, StandardCharsets.UTF_8);

                    System.out.println("Введіть ключ шифрування: ");
                    int key = scanner.nextInt();

                    System.out.println("Зашифрований текст з файлу: " + encrypt(file, key));


                    System.out.println("Введіть шлях куди хочете зберегти зашифрований файл: ");
                    scanner.nextLine();
                    Path encryptFile = Paths.get(scanner.nextLine());
                    Files.writeString(encryptFile, encrypt(file, key), StandardCharsets.UTF_8);

                    break;

                }
                catch (NoSuchFileException e) {
                    System.out.println("Такого файлу не існує.");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

            public static void decryptFile (Scanner scanner) {
        while (true) {

            System.out.println("Введіть шлях до текстового файлу, який треба розшифрувати: ");

            try {

                Path encryptPath = Path.of(scanner.nextLine());
                String encryptFile = Files.readString(encryptPath, StandardCharsets.UTF_8);

                System.out.println("Введіть ключ розшифрування: ");
                int key = scanner.nextInt();
                String decryptMassage = decrypt(encryptFile, key);
                System.out.println("Розшифрований текст: " + decryptMassage);

                break;

            } catch (NoSuchFileException e) {
                System.out.println("Такого файлу не існує.");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

            public static void bruteForceDecrypt (Scanner scanner) {
        while (true) {
            System.out.println("Введіть шлях до файлу, який треба розшифрувати методом brute force: ");
            try {

                Path encryptFile = Path.of(scanner.nextLine());
                String encryptFileText = Files.readString(encryptFile, StandardCharsets.UTF_8);

                System.out.println("Напишіть шлях до файлу перевірки, який ви завантажили: ");

                Path dictionaryPath = Path.of(scanner.nextLine());
                List<String> dictionaryWords = Files.readAllLines(dictionaryPath, StandardCharsets.UTF_8);

                boolean found = false;

                for (int key = 1; key < ukrainianAlphabet.size(); key++) {
                    String decryptedFile = decrypt(encryptFileText, key);

                    String[] words = decryptedFile.split("\\s+");

                    for (String word : words) {
                        if (dictionaryWords.contains(word.toLowerCase())) {
                            System.out.println("Знайдено можливий ключ: " + key);
                            System.out.println("Розшифрований текст файлу: " + decryptedFile);
                            found = true;
                            break;
                        }
                    }

                    if (found) {
                        break;
                    }
                }

                if (!found) {
                    System.out.println("Слово не знайдено в словнику.");
                }


                bruteForceDecrypt(encryptFileText);

                break;

            } catch (NoSuchFileException e) {
                System.out.println("Такого файлу не існує.");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


        public static String encrypt(String path, int key) {


            StringBuilder encryptMassage = new StringBuilder();

            for (char l : path.toCharArray()) {

                int index = ukrainianAlphabet.indexOf(l);
                if (index != -1) {
                    int encryptWord = (index + key) % ukrainianAlphabet.size();
                    encryptMassage.append(ukrainianAlphabet.get(encryptWord));
                }
                else {
                    encryptMassage.append(l);
                }
            }
            return encryptMassage.toString();
        }

        public static String decrypt(String encryptMassage, int key) {
            StringBuilder decryptMessage = new StringBuilder();
            for (char l : encryptMassage.toCharArray()) {
                int index = ukrainianAlphabet.indexOf(l);
                if (index != -1) {
                    int decryptWord = (index - key + ukrainianAlphabet.size()) % ukrainianAlphabet.size();
                    decryptMessage.append(ukrainianAlphabet.get(decryptWord));
            } else {
                    decryptMessage.append(l);
                }
        }
            return decryptMessage.toString();
    }
        public static void bruteForceDecrypt (String decryptFile) {
        for (int key = 1; key < ukrainianAlphabet.size(); key++) {
                String decryptedFile = decrypt(decryptFile, key);
                if (decryptedFile.contains(" ")) {
                    System.out.println("Знайдено другий можливий ключ: " + key);
                    System.out.println("Розшифрований текст файлу: " + decryptedFile);
                    break;
                }
            }
        }
    }
