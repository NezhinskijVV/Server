import lombok.SneakyThrows;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientEntity implements Runnable, Observer {
    private Socket socket;
    private MyServer server;
    private Client client;

    public ClientEntity(Socket socket, MyServer server) {
        this.socket = socket;
        this.server = server;
    }

    @SneakyThrows
    @Override
    public void run() {
        BufferedReader clientReader = new BufferedReader(new InputStreamReader(
                socket.getInputStream()));
        while (true) {
            String clientMessage = clientReader.readLine();
            if (clientMessage.startsWith("REGISTRATION")) {
                String[] logPass = clientMessage.substring(12).split(":");
                client = new Client(logPass[0], logPass[1]);
                System.out.println("New client connected" +
                        logPass[0] + " " + logPass[1]);
                server.addObserver(this);
            } else {
                System.out.println(clientMessage);
                server.notifyObservers(client.getLogin() + ": " + clientMessage);
            }
        }
    }

    @SneakyThrows
    @Override
    public void notifyObserver(String message) {
        PrintWriter writer = new PrintWriter(socket.getOutputStream());
        writer.println(message);
        writer.flush();
    }

//    public boolean registration(){
//        while ()
//    }
}
