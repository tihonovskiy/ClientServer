import java.util.HashMap;
import java.util.Map;

public class ClientHandlerRegistry {
    private Map<String, ClientHandler> handlers = new HashMap<>();

    public Map<String, ClientHandler> getHandlers() {
        return handlers;
    }

    public void register(String name, ClientHandler clientHandler) {
        handlers.put(name, clientHandler);
    }

    public boolean isLoggedIn(String name) {
        for (String handlerName : handlers.keySet()) {
            if (handlerName.equals(name)) {
                return true;
            }
        }
        return false;
    }

    public boolean isNotLoggedIn(String name) {
        return !isLoggedIn(name);
    }

}
