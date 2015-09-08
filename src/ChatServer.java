import java.io.IOException;
import java.net.ServerSocket;

/**
 * Created by id14llm on 2015-09-08.
 */
public class ChatServer {

    public ChatServer() throws IOException {
        ServerSocket serverSocket = new ServerSocket(1337);
    }
}
