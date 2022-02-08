import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ClientEngineTest {
    ClientEngine clientEngine = new ClientEngine();

    @Test
    public void prepareValidTest() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, IOException {
        Method method = ClientEngine.class.getDeclaredMethod("prepare");
        method.setAccessible(true);
        method.invoke(clientEngine);
        Assertions.assertNotNull(clientEngine.getIn());
        Assertions.assertNotNull(clientEngine.getOut());
        Assertions.assertNotNull(clientEngine.getSocket());
    }

    @Test
    public void startExceptionTest() {
        Assertions.assertThrows(
                RuntimeException.class,
                new Executable() {
                    @Override
                    public void execute() throws Throwable {
                        clientEngine.start();
                    }
                });
    }

    @Test
    public void doListenExceptionTest() {
        Assertions.assertThrows(
                InvocationTargetException.class,
                new Executable() {
                    @Override
                    public void execute() throws Throwable {
                        Method method = ClientEngine.class.getDeclaredMethod("doListen");
                        method.setAccessible(true);
                        method.invoke(clientEngine);
                    }
                });
    }

    @Test
    public void clientDisconnectedExceptionTest() {
        Assertions.assertThrows(
                InvocationTargetException.class,
                new Executable() {
                    @Override
                    public void execute() throws Throwable {
                        Method method = ClientEngine.class.getDeclaredMethod("clientDisconnected");
                        method.setAccessible(true);
                        method.invoke(clientEngine);
                    }
                });
    }

}