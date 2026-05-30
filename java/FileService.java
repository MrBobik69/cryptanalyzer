import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileService {

    // чтение файла с проверкой на его существование
    public String readFile(String filePath) throws IOException {
        Path path = Path.of(filePath);
        if (!Files.exists(path)) {
            throw new FileNotFoundException("Файл не найден по пути: " + path.toAbsolutePath());
        }
        if (Files.isDirectory(path)) {
            throw new IllegalArgumentException("Указанный путь является папкой, а не файлом: " + path.toAbsolutePath());
        }
        return Files.readString(path);
    }

    // запись результата в файл
    public void writeFile(String filePath, String content) throws IOException {
        Path path = Path.of(filePath);
        // если папки для файла не существует, создаем её
        if (path.getParent() != null) {
            Files.createDirectories(path.getParent());
        }
        Files.writeString(path, content);
    }
}
