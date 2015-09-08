import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

/**
 * Created by id14llm on 2015-09-08.
 */
public class ChatServer {
    Integer IP;
    Integer port;

    public ChatServer(Integer IP, Integer port) throws IOException {
        this.IP = IP;
        this.port = port;

        ServerSocket serverSocket = new ServerSocket(IP, port);
        Socket socket = serverSocket.accept();

        InputStream in = socket.getInputStream();

        Scanner scanner = new Scanner(in);

    }

    public void sendHeartBeat(){

    }
}
