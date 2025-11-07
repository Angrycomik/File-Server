package client;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;

public class FileClientService {
    private final String address;
    private final int port;

    public FileClientService(String address, int port) {
        this.address = address;
        this.port = port;
    }

    public String addFile(String localFilename, String serverFilename) throws IOException {
        try (Socket socket = new Socket(InetAddress.getByName(address), port);
             DataInputStream input = new DataInputStream(socket.getInputStream());
             DataOutputStream output = new DataOutputStream(socket.getOutputStream()))
        {
            output.writeUTF(Main.Request.PUT.name() + " " + serverFilename);

            byte[] bytes = FileHandler.getFileContent(localFilename);
            output.writeInt(bytes.length);
            output.write(bytes);

            String response = input.readUTF();
            if (!response.startsWith("200")) {
                throw new IOException("Error: " + response);
            }
            return response.split(" ")[1];
        }
    }
    public void sendExitRequest() {
        try (Socket socket = new Socket(InetAddress.getByName(address), port);
             DataOutputStream output = new DataOutputStream(socket.getOutputStream())
        ) {
            System.out.println("The request was sent");
            output.writeUTF("exit");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}