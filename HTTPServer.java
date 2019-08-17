package httpserver;


import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class HTTPServer {
    protected ServerSocket listenSocket;
    protected Socket connection;
    protected BufferedReader in;
    protected BufferedOutputStream out;

    public HTTPServer() throws Exception{
        this.listenSocket = new ServerSocket(2022);
        this.connection = listenSocket.accept();
        this.in  = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        this.out = new BufferedOutputStream(new DataOutputStream(connection.getOutputStream()));
    }

    public String parseRequest() throws Exception{
        // return requested file path from request head
        String status = in.readLine();
        System.out.println(status);
        String s = status.split(" ")[1];
        if(s.startsWith("/")){s = s.substring(1);}
        System.out.println(s);
        return s;
    }

    public byte[] response(String file_name) throws Exception {
        // return the byte[] of the file specified by file_name

        File f = new File(file_name);
        byte[] filebytes = new byte[(int) f.length()];
        BufferedInputStream file_in = new BufferedInputStream(new FileInputStream(file_name));
        file_in.read(filebytes);
        file_in.close();
        return filebytes;
    }

    public static void main(String[] args) throws Exception {
        // initialize HTTPServer
        HTTPServer webserver = new HTTPServer();

            String file_name = webserver.parseRequest();
            System.out.println(file_name);

            webserver.out.write(new String("HTTP/1.1 200 have a nice day\r\n\r\n").getBytes());
            if (new File(file_name).exists()) {
                webserver.out.write(webserver.response(file_name));
                System.out.println("specified file sended");
            } else {
                webserver.out.write(webserver.response("404.html"));
                System.out.println("404");
            }
            webserver.out.flush();

        webserver.connection.close();
    }

}
