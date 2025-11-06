package server;

import java.io.*;
import java.net.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Main {

    static final String ADDRESS = "127.0.0.1";
    static final int PORT = 23456;

    private static volatile boolean running = true;

    public static void main(String[] args) {
        System.out.println("Server started!");
        FileIds.init();
        try{
            ServerSocket server = new ServerSocket(PORT, 50, InetAddress.getByName(ADDRESS));
            while(running) {
                Session session = new Session(server.accept(),server);
                session.start();
            }
        }catch(SocketException e){
            try{
                SerializationUtils.serialize(FileIds.synchronizedNumbers,"filelist.data");
            }catch (Exception ex){
                ex.printStackTrace();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}