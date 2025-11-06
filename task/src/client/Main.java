package client;

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Main {
    static final String ADDRESS = "127.0.0.1";
    static final int PORT = 23456;

    enum Request{GET, PUT, DELETE}

    static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("Enter action (1 - get a file, 2 - create a file, 3 - delete a file): ");

        String action = scanner.nextLine();

        handleAction(action);
    }

    private static void handleAction(String action) {
        switch (action) {
            case "1" -> get(idOrName());
            case "2" -> add();
            case "3" -> remove(idOrName());
            case "exit" -> sendExitRequest();
            default -> System.out.println("Invalid input");
        }
    }

    static String sendToServer(String method, String name) {
        try (Socket socket = new Socket(InetAddress.getByName(ADDRESS), PORT);
             DataInputStream input = new DataInputStream(socket.getInputStream());
             DataOutputStream output = new DataOutputStream(socket.getOutputStream())
        ){
            output.writeUTF(method + " " + name);
            System.out.println("The request was sent");

            int length = input.readInt();
            byte[] data = new byte[length];
            input.readFully(data,0, data.length);
            askForANameAndSave(data);

            return input.readUTF();
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
    static String sendFileToServer(String method, String localFilename,String serverFilename) {
        try (Socket socket = new Socket(InetAddress.getByName(ADDRESS), PORT);
             DataInputStream input = new DataInputStream(socket.getInputStream());
             DataOutputStream output = new DataOutputStream(socket.getOutputStream())
        ){
            output.writeUTF(method + " " + serverFilename);
            System.out.println("The request was sent");
            switch (method) {
                case "PUT"->{
                    try{
                        byte[] bytes = FileHandler.getFileContent(localFilename);
                        output.writeInt(bytes.length);
                        output.write(bytes);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                case "GET"-> {

                }
            }

            return input.readUTF();
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    private static void askForANameAndSave(byte[] bytes) throws IOException {
        System.out.println("The file was downloaded! Specify a name for it: ");
        String name = scanner.nextLine();
        FileHandler.createFile(name, bytes);
        System.out.println("File saved on the hard drive!");
    }

    static void sendExitRequest(){
        try (Socket socket = new Socket(InetAddress.getByName(ADDRESS), PORT);
             DataOutputStream output = new DataOutputStream(socket.getOutputStream())
        ){
            System.out.println("The request was sent");
            output.writeUTF("exit");
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    static void add(){
        System.out.println("Enter name of the file: ");
        String localFilename = scanner.nextLine();
        System.out.println("Enter name of the file to be saved on server: ");
        String serverFilename = scanner.nextLine();

        String response = sendFileToServer(Request.PUT.name(), localFilename, serverFilename);
        if(response != null && response.startsWith("200")) {
            System.out.println("Response says that file is saved! ID = " + response.split(" ")[1] );
        }else{
            System.out.println("The response says that creating the file was forbidden!");
        }
    }

    static void get(String request){
        String response = sendToServer(Request.GET.name(),request);

        if(response != null &&  response.startsWith("200")) {
//            System.out.println("The content of the file is: " + response.split(" ")[1]);
        }else{
            System.out.println("The response says that this file is not found!");
        }
    }

    static void remove(String name){
        String response = sendToServer(Request.DELETE.name(), name);
        if(response != null &&  response.startsWith("200")) {
            System.out.println("The response says that the file was successfully deleted!");
        }else{
            System.out.println("The response says that the file was not found!");
        }
    }

    static boolean verify(String input){
        if(input.matches("\\+")){
            System.out.printf("Invalid input: %s\n", input);
            return false;
        }
        return true;
    }
    static String idOrName(){
        System.out.println("Do you want to get the file by name or by id (1 - name, 2 - id): ");
        int action = scanner.nextInt();
        scanner.nextLine(); // consume \n
        switch (action) {
            case 1->{
                System.out.println("Enter name of the file to be saved on server: ");
                String name = scanner.nextLine();
                if(!verify(name)){
                    System.out.println("Invalid input!");
                    return null;
                }
                return "BY_NAME " +  name;
            }
            case 2->{
                System.out.println("Enter id: ");
                String id = scanner.nextLine();
                return "BY_ID " +  id;
            }
            default -> System.out.println("Invalid input");
        }
        return "";
    }
}
