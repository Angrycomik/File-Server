package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Random;

public class RequestHandler {
    static String handleDelete(String method, String nameOrId) {
        try{
            switch(method) {
                case "BY_NAME" -> {
                    FileHandler.deleteFile(nameOrId);
                    FileIds.removeFileByName(nameOrId);
                }
                case "BY_ID" -> {
                    System.out.println(nameOrId);
                    FileHandler.deleteFile(FileIds.getFileById(nameOrId));
                    FileIds.removeFileById(Integer.parseInt(nameOrId));
                }
                default -> throw new IllegalStateException("Unexpected value: " + method);
            }
            return "200";
        }catch (Exception e){
            e.printStackTrace();
            return "404";
        }
    }

    static String handlePut(Socket socket, String[] arr) {
        String name = arr.length == 1 ? randomName() : arr[1];
        if(FileHandler.isFileExist(name)){return "403";}

        try{
            FileHandler.createFile(name, getBytes(socket));
            int id = FileIds.addFileID(name);
            return "200 " + id;
        }catch (Exception e) {
            e.printStackTrace();
            return "403";
        }
    }

    static String handleGet(Socket socket,String method, String nameOrId){
        try{
            String data = switch(method){
                case "BY_NAME" -> getByName(nameOrId, socket);
                case "BY_ID" -> getById(nameOrId, socket);
                default -> throw new IllegalStateException("Unexpected value: " + method);
            };

            return "200 " + data;
        }catch (Exception e) {
            e.printStackTrace();
            return "404";
        }
    }

    static String getByName(String name,Socket socket) throws IOException {
        sendBytes(FileHandler.getFileBytes(name),socket);
        return FileHandler.getFileContent(name);
    }

    static String getById(String id, Socket socket) throws  IOException{
        String name = FileIds.getFileName(Integer.parseInt(id));
        sendBytes(FileHandler.getFileBytes(name),socket);
        return FileHandler.getFileContent(FileIds.getFileById(id));
    }

    private static void sendBytes(byte[] bytes, Socket socket) throws IOException {
        DataOutputStream output= new DataOutputStream(socket.getOutputStream());

        output.writeInt(bytes.length);
        output.write(bytes);
    }

    static byte[] getBytes(Socket socket) throws IOException {
        DataInputStream input = new DataInputStream(socket.getInputStream());

        int length = input.readInt();
        byte[] data = new byte[length];
        input.readFully(data,0, data.length);

        return data;
    }

    private static String randomName() {
        String ALLOWEDCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder filename = new StringBuilder();
        Random rnd = new Random();
        while (filename.length() < 10) {
            int index = (int) (rnd.nextFloat() * ALLOWEDCHARS.length());
            filename.append(ALLOWEDCHARS.charAt(index));
        }
        return filename.toString();
    }
}
