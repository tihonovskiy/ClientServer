import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class ClientHandlerMsg {

    private DataInputStream in;
    private DataOutputStream out;

    public ClientHandlerMsg(DataInputStream in, DataOutputStream out) {
        this.in = in;
        this.out = out;
    }

    public String listenInboundMsg() {
        try {
            return in.readUTF();
        } catch (IOException e) {
            throw new RuntimeException("SWW during inbound message listening.", e);
        }
    }

    public void sendOutboundMsg(String message) {
        try {
            out.writeUTF(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public DataInputStream getIn() {
        return in;
    }

    public DataOutputStream getOut() {
        return out;
    }
}
