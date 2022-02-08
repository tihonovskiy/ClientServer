import org.junit.jupiter.api.*;
import org.junit.jupiter.api.function.Executable;

import java.io.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ClientHandlerMsgTest {
    ClientHandlerMsg clientHandlerMsg;

    @BeforeAll
    public void setUp() throws FileNotFoundException {
        clientHandlerMsg = new ClientHandlerMsg(
                new DataInputStream(new FileInputStream("src/test/test.txt")),
                new DataOutputStream(new FileOutputStream("src/test/test.txt"))
        );
    }

    @Test
    public void sendOutboundMsgTest() throws IOException {
        clientHandlerMsg.sendOutboundMsg("This is test message.");
        Assertions.assertEquals("This is test message.", clientHandlerMsg.getIn().readUTF());
    }

    @Test
    public void listenInboundMsgTest() throws IOException {
        clientHandlerMsg.getOut().writeUTF("This is test message.");
        Assertions.assertEquals("This is test message.", clientHandlerMsg.listenInboundMsg());
    }

    @Test
    public void listenInboundMsgExceptionTest() {
        Assertions.assertThrows(
                RuntimeException.class,
                new Executable() {
                    @Override
                    public void execute() throws Throwable {
                        clientHandlerMsg.listenInboundMsg();
                    }
                });
    }

}
