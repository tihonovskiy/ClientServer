import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;
import java.util.Scanner;

public class Client {

    private DataInputStream in;
    private DataOutputStream out;

    public void start() {
        try{
            prepare();
            new Thread(this::doListen).start();
            doWrite();

            System.out.println("[CLIENT] Client is shutting down...");
            System.out.println("[CLIENT] Thank you! Bey!");

        } catch (IOException e) {
            throw new RuntimeException("SWW during establishing a connection with the server", e);
        }
    }

    public void prepare() throws IOException {
        Socket socket = new Socket("127.0.0.1", 8080);
        in = new DataInputStream(socket.getInputStream());
        out = new DataOutputStream(socket.getOutputStream());
    }

    public void doListen() {
        while (true) {
            try{
                System.out.println("Inbound message: " + in.readUTF());
            } catch (EOFException | SocketException e) {
                System.out.println("[CLIENT] Something went wrong during a listening inbound messages.");
                System.out.println("[CLIENT] Probably, the connection is lost and client is not reachable anymore.");
                System.out.println("[CLIENT] Connection is about to be closed.");
                System.out.println("[CLIENT] Please double press enter-button...");
                break;
            } catch (IOException e) {
                throw new RuntimeException("Something went wrong during the communication.", e);
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
                throw new RuntimeException("SWW during a sending the message to the server", e);
            }
        }
    }

}
