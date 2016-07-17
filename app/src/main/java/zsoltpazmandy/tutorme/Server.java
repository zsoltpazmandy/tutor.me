package zsoltpazmandy.tutorme;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server extends Thread {

    public static final int port = 60001;
    static int clientCounter = 1;
    static ArrayList<Socket> users = new ArrayList<>();


    // this class needs a thread per client
    // make it constantly wait for clients & instantiate a connection
    // each time, so testing from two clients can begin

    public static void main(String[] args) {

        ServerSocket serverSocket = null;
        Socket clientSocket = null;

        try {
            serverSocket = new ServerSocket(port);
            System.out.println("Server running.");

            while (true) {
                clientSocket = serverSocket.accept();
                users.add(clientSocket);
                ServerThread serverThread = new ServerThread(clientSocket);
                serverThread.start();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static class ServerThread extends Thread {

        Socket clientSocket = null;
        BufferedWriter output;
        BufferedReader input;

        public ServerThread(Socket clientSocket) throws IOException {
            this.clientSocket = clientSocket;
            output = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
            input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        }

        public void run() {


            System.out.println("Client #" + clientCounter + " connected.");
            clientCounter++;

            try {

                String line;
                while ((line = input.readLine()) != null) {
                    for (Socket client : users) {
                        updateClientMessageBox(client, line);
                    }

                }

            } catch (
                    Exception e
                    )

            {
                e.printStackTrace();
            }

        }

        public void updateClientMessageBox(Socket client, String line) {
            try {

                output = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));

                output.write(line);
                output.newLine();
                output.flush();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}

