package client;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileHandler {
        static Path root = Path.of(
            System.getProperty("user.dir"),
            "src", "client", "data"
    );
    public static byte[] getFileContent(String filename) throws IOException {
        return Files.readAllBytes(root.resolve(filename));
    }
    public static void createFile(String filename,byte[] bytes) throws IOException {
        Files.write(root.resolve(filename), bytes);
    }
}
