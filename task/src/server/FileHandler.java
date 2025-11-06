package server;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class FileHandler {
//    static Path root = Path.of(
//            System.getProperty("user.dir"),
//            "File Server","task","src", "server", "data"
//    );
    static Path root = Path.of(
            System.getProperty("user.dir"),
            "src", "server", "data"
    );

    public static boolean isFileExist(String filename) {
        return Files.exists(root.resolve(filename));
    }
    public static String getFileContent(String filename) throws IOException {
        return new String(Files.readAllBytes(root.resolve(filename)));
    }
    public static byte[] getFileBytes(String filename) throws IOException {
        return Files.readAllBytes(root.resolve(filename));
    }
    public static void deleteFile(String filename) throws IOException {
        Files.delete(root.resolve(filename));
    }
    public static void createFile(String filename,String content) throws IOException {
        Files.write(root.resolve(filename), content.getBytes());
    }
    public static void createFile(String filename,byte[] bytes) throws IOException {
        Files.write(root.resolve(filename), bytes);
    }
}
