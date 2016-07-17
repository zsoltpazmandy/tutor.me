package zsoltpazmandy.tutorme;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Server extends Thread {

    public static final int portNumber = 60001;


    public static void main(String[] args) {

        ServerSocket serverSocket = null;

        try {
            serverSocket = new ServerSocket(portNumber);
            System.out.println("Server running.");

            // waiting for a client to connect here:
            Socket clientSocket = serverSocket.accept();

            System.out.println("Client " + clientSocket.getChannel() + " connected.");

            BufferedWriter output = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
            output.write("You are now connected to the server.");
            output.newLine();
            output.flush();

            BufferedReader input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            String line;
            while ((line = input.readLine()) != null) {

                System.out.println(line);
                output.write(line);
                output.newLine();
                output.flush();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

