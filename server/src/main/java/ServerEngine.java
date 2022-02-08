import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;

public class ServerEngine {

    private final int PORT = 8080;
    private int ClientNumber = 0;
    private ServerSocket serverSocket;
    private ClientHandlerRegistry clientHandlerRegistry;
    private Socket socket;
    private LinkedList<ClientHandler> clientList = new LinkedList<>();

    public void start() throws IOException {
        try {
            clientHandlerRegistry = new ClientHandlerRegistry();
            serverSocket = new ServerSocket(PORT);
            while (true) {
                socket = serverSocket.accept();

                new Thread(() -> {
                    ClientHandler clientHandler = new ClientHandler(this, socket, ClientNumber);
                    clientList.add(clientHandler);
                }).start();
                ClientNumber++;
            }
        } catch (IOException e) {
            throw new RuntimeException("SWW during server start-up.", e);
        } finally {
            serverSocket.close();
        }
    }

    public void register(String name, ClientHandler clientHandler) {
        clientHandlerRegistry.register(name, clientHandler);
    }

    public boolean isNotLoggedIn(String name) {
        return clientHandlerRegistry.isNotLoggedIn(name);
    }

    public void broadcastMsg(String message) {
        System.out.println(message);
        if(message.split(" ")[1].equals("-exit")) {
            removeClient();
        }
        for (ClientHandler clientHandler : clientHandlerRegistry.getHandlers().values()) {
            clientHandler.getMessager().sendOutboundMsg(message);
        }
    }

    private void removeClient() {
        try {
            if(!socket.isClosed()) {
                socket.close();

                for (ClientHandler ch : clientList) {
                    if(ch.equals(this)) {
                        clientList.remove(this);
                    }
                }
            }
        } catch (IOException ignored) {}
    }

}
