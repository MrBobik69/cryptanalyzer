import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CaesarCipher {
    // это единый алфавит для проекта здесь строчные буквы, знаки препинания и пробел
    private static final List<Character> ALPHABET = Arrays.asList(
            'а', 'б', 'в', 'г', 'д', 'е', 'ё', 'ж', 'з', 'и', 'й', 'к', 'л', 'м', 'н', 'о',
            'п', 'р', 'с', 'т', 'у', 'ф', 'х', 'ц', 'ч', 'ш', 'щ', 'ъ', 'ы', 'ь', 'э', 'ю', 'я',
            '.', ',', '«', '»', '"', '\'', ':', '!', '?', ' '
    );

    // Первое это шифрование текста через сдвиг вправо
    public String encrypt(String text, int key) {
        return runCipher(text, key);
    }

    // второе расшифровка текста через сдвиг влево
    public String decrypt(String text, int key) {
        return runCipher(text, -key);
    }

    // здесь у нас универсальный движок шифрования
    private String runCipher(String text, int key) {
        StringBuilder result = new StringBuilder();
        int alphabetSize = ALPHABET.size();

        for (char character : text.toCharArray()) {
            char lowerChar = Character.toLowerCase(character);
            int index = ALPHABET.indexOf(lowerChar);

            if (index != -1) {
                int newIndex = (index + key) % alphabetSize;
                if (newIndex < 0) {
                    newIndex += alphabetSize; // защита от отрицательного остатка
                }
                result.append(ALPHABET.get(newIndex));
            } else {
                result.append(character); // те символы что вне алфавита оставляем без изменений
            }
        }
        return result.toString();
    }

    // Третье режим Brute Force (перебор ключей по маркерам слов)
    public String bruteForce(String encryptedText) {
        int bestKey = 0;
        int maxMatches = -1;
        String bestResult = "";

        // самые популярные маркеры в живой речи
        List<String> markers = Arrays.asList(" и ", " в ", " не ", " на ", " что ", " как ", " я ", " с ", " то ");

        for (int key = 1; key < ALPHABET.size(); key++) {
            String decryptedTry = decrypt(encryptedText, key);

            int currentMatches = 0;
            for (String marker : markers) {
                if (decryptedTry.contains(marker)) {
                    currentMatches++;
                }
            }

            if (currentMatches > maxMatches) {
                maxMatches = currentMatches;
                bestKey = key;
                bestResult = decryptedTry;
            }
        }
        System.out.println("   [Анализ] Brute Force подобрал ключ: " + bestKey);
        return bestResult;
    }

    // Четвертое режим Статистического анализа (по частоте самого популярного символа — пробела)
    public String statisticalAnalysis(String encryptedText) {
        if (encryptedText.isEmpty()) return encryptedText;

        Map<Character, Integer> frequencies = new HashMap<>();
        for (char c : encryptedText.toCharArray()) {
            char lowerChar = Character.toLowerCase(c);
            if (ALPHABET.contains(lowerChar)) {
                frequencies.put(lowerChar, frequencies.getOrDefault(lowerChar, 0) + 1);
            }
        }

        // здесь находим самый частый символ в зашифрованном тексте
        char mostFrequentChar = ' ';
        int maxCount = -1;
        for (Map.Entry<Character, Integer> entry : frequencies.entrySet()) {
            if (entry.getValue() > maxCount) {
                maxCount = entry.getValue();
                mostFrequentChar = entry.getKey();
            }
        }

        // самый частый символ — это пробел ' '
        int encryptedIndex = ALPHABET.indexOf(mostFrequentChar);
        int spaceIndex = ALPHABET.indexOf(' ');

        // вычисляем ключ на основе разницы позиций
        int calculatedKey = (encryptedIndex - spaceIndex + ALPHABET.size()) % ALPHABET.size();
        System.out.println("   [Анализ] Статанализ определил ключ: " + calculatedKey + " (основан на символе '" + mostFrequentChar + "')");

        return decrypt(encryptedText, calculatedKey);
    }

    public int getAlphabetSize() {
        return ALPHABET.size();
    }
}
