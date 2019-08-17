package httpserver;

import java.io.*;
import java.net.Socket;

public class HTTPThread implements Runnable {
    private final Socket connection;
    private BufferedReader in;
    private BufferedOutputStream out;

    public HTTPThread(Socket s) throws Exception{
        this.connection = s;
        this.in  = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        this.out = new BufferedOutputStream(new DataOutputStream(connection.getOutputStream()));
    }

    public String parseRequest(String Status) throws Exception{
        // return requested file path from request head
        String status = Status;
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

    public void run(){
        try {
            String status = in.readLine();
            String file_name = parseRequest(status);
            System.out.println(file_name);

            out.write(new String("HTTP/1.1 200 have a nice day\r\n\r\n").getBytes());
            if (new File(file_name).exists()) {
                out.write(response(file_name));
                System.out.println("specified file sended");
            } else {
                out.write(response("404.html"));
                System.out.println("404");
            }
            out.flush();
        }catch (Exception e){e.printStackTrace();}

        try{connection.close();}catch (Exception e2){e2.printStackTrace();
            System.out.println("connection won't close properly");}
    }
}
