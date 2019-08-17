package httpserver;

import java.net.ServerSocket;
import java.net.Socket;

public class HTTPServerMT {
    public static void main(String[] args) throws Exception{
        ServerSocket listensocket = new ServerSocket(2022);
        while (true){
            System.out.println(Thread.currentThread());
            Socket connection = listensocket.accept();
            new Thread(new HTTPThread(connection)).start();

        }
    }
}
