import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;
import java.util.Scanner;

public class ClientEngine {

    private final int PORT = 8080;
    private final String IP = "127.0.0.1";
    private DataInputStream in;
    private DataOutputStream out;
    private Socket socket;

    public void start() {
        try {
            prepare();

            new Thread(this::doListen).start();
            doWrite();

            System.out.println("[CLIENT] Application is shutting down...");
            System.out.println("[CLIENT] Thank you! Bey!");
        } catch (IOException e) {
            throw new RuntimeException("SWW during establishing a connection with the server.", e);
        }
    }

    private void prepare() throws IOException {
        socket = new Socket(IP, PORT);
        in = new DataInputStream(socket.getInputStream());
        out = new DataOutputStream(socket.getOutputStream());
    }

    private void doListen() {
        while (true) {
            try {
                String str = in.readUTF();
                System.out.println(str);
                if (str.split(" ")[1].equals("-exit")) {
                    clientDisconnected();
                    break;
                }
            } catch (EOFException e) {
                System.out.println("[CLIENT] Something went wrong during a listening inbound messages.");
                System.out.println("[CLIENT] Probably, the connection is lost and client is not reachable anymore.");
                System.out.println("[CLIENT] Connection is about to be closed.");
                System.out.println("[CLIENT] Please double press enter-button...");
                break;
            } catch (IOException e) {
                throw new RuntimeException("Something went wrong during the communication.");
            }
        }
    }

    private void doWrite() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            String message = scanner.nextLine();
            try {
                out.writeUTF(message);
            } catch (SocketException e) {
                System.out.println("[CLIENT] The writing operation finished.");
                System.out.println("[CLIENT] The connection closed.");
                break;
            } catch (IOException e) {
                throw new RuntimeException("SWW during a sending the message to the server.", e);
            }
        }
    }

    private void clientDisconnected() {
        try {
            if (!socket.isClosed()) {
                socket.close();
                in.close();
                out.close();
                System.out.println("Client disconnected");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public DataInputStream getIn() {
        return in;
    }

    public DataOutputStream getOut() {
        return out;
    }

    public Socket getSocket() {
        return socket;
    }
}
