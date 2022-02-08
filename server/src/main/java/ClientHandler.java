import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Date;

public class ClientHandler {

    private ServerEngine serverEngine;
    private ClientHandlerMsg messager;
    private String name;
    private Date timeToConnect;

    public ClientHandler(ServerEngine serverEngine, Socket socket, int clientNumber) {
        try {
            timeToConnect = new Date();
            System.out.printf("[SERVER | %s] Client handler starting...%n", timeToConnect);

            this.serverEngine = serverEngine;
            messager = new ClientHandlerMsg(
                    new DataInputStream(socket.getInputStream()),
                    new DataOutputStream(socket.getOutputStream())
            );

            doAuthentication(clientNumber);
            clientNumber++;
            doListen();

        } catch (IOException e) {
            throw new RuntimeException("SWW during the client connection.", e);
        }
    }

    public ClientHandlerMsg getMessager() {
        return messager;
    }

    public void doAuthentication(int clientNumber) {
        name = "Client" + clientNumber;
        System.out.printf("[SERVER | %s] Attempt of client authentication ...%n", new Date());
        System.out.printf("[SERVER | %s] Authentication payload: %s%n", new Date(), name);

        if (serverEngine.isNotLoggedIn(name)) {
            serverEngine.broadcastMsg(String.format("[%s] logged in chat.", name));
            serverEngine.register(name, this);
            messager.sendOutboundMsg("You successfully logged in chat.");
            this.name = name;

            System.out.printf("[SERVER | %s] Client with name [%s] authenticated successfully.%n", new Date(), name);
            return;
        }

        System.out.printf("[SERVER | %s] Client authentication was not successful.%n", new Date());
    }

    public void doListen() {
        System.out.printf("[SERVER | %s] Client with name [%s] has started to listen inbound message.%n", new Date(), name);
        while (true) {
            String inboundMsg = messager.listenInboundMsg();
            System.out.printf("[SERVER | %s] Client with name [%s] initiates message [%s] broadcast.%n", new Date(), name, messager);
            serverEngine.broadcastMsg(String.format("%s: %s", name, inboundMsg));
        }
    }

}
