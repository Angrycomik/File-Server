package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.Arrays;

import static server.RequestHandler.*;


public class Session extends Thread {
    private final Socket socket;
    private ServerSocket server;
    public Session(Socket socketForClient, ServerSocket server) {
        this.socket = socketForClient;
        this.server = server;
    }


    public void run() {
        try(
                DataInputStream input = new DataInputStream(socket.getInputStream());
                DataOutputStream output  = new DataOutputStream(socket.getOutputStream())
        ){
            String data = input.readUTF();
//                    System.out.println(data);

            if(data.equals("exit")){server.close();socket.close();return;}

            String[] arr = data.split(" ");
//            System.out.println(Arrays.toString(arr));
            String status = switch (arr[0]) {
                case "GET" -> handleGet(socket,arr[1],arr[2]);
                case "PUT" -> handlePut(socket, arr);
                case "DELETE" -> handleDelete(arr[1],arr[2]);
                default -> throw new IllegalStateException("Unexpected value: " + arr[0]);
            };

//                    System.out.println("Sent: " + status);
            output.writeUTF(status);

            socket.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
