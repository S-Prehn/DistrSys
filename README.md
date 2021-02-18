# Distributed Systems - Assignment 2 - Group 12

* Deepshi Garg : S4199456
* Shubham Jinde : S3993914
* Sebastian Prehn : S3013472

## General Information

The java scripts can be compiled executing the attached bash script [compile.sh](./compile.sh) with: `bash compile.sh`

The SocketInvertServer takes as input the port number it should connect to, like for instance: `java SocketInvertServer 8000`

The SocketInvertClient Module takes as input the name of the machine that hosts the SocketInvertServer and the server's port id. For instance, assuming that server and client run on the same machine the compiled script can be run with: `java SocketInvertClient $HOSTNAME 8000`

## Content Source Files

The code from the files (without the copyright notes that are in the scripts) are:

### SocketInvertClient:

import java.io.*;
import java.net.*;

public class SocketInvertClient {
    public static void main(String[] args) throws IOException {

        if (args.length != 2) {
            System.err.println(
                "Usage: java SocketInvertClient <host name> <port number>");
            System.exit(1);
        }

        String hostName = args[0];
        int portNumber = Integer.parseInt(args[1]);

        try (
            Socket invertSocket = new Socket(hostName, portNumber);
            PrintWriter out =
                new PrintWriter(invertSocket.getOutputStream(), true);
            BufferedReader in =
                new BufferedReader(
                    new InputStreamReader(invertSocket.getInputStream()));
            BufferedReader stdIn =
                new BufferedReader(
                    new InputStreamReader(System.in))
        ) {
            String userInput;
            while ((userInput = stdIn.readLine()) != null) {
                out.println(userInput);
                System.out.println("Client received: " + in.readLine());
            }
        } catch (UnknownHostException e) {
            System.err.println("Don't know about host " + hostName);
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to " +
                hostName);
            System.exit(1);
        }
    }
}

### SocketInvertServer:

import java.net.*;
import java.io.*;

public class SocketInvertServer {

    public static String invert_str(String str) {
        char ch[] = str.toCharArray();
        String invert="";
        for (int i= ch.length-1; i>=0; i--) {
            invert += ch[i];
        }
        return invert;
    }

    public static void main(String[] args) throws IOException {

        if (args.length != 1) {
            System.err.println("Usage: java SocketInvertServer <port number>");
            System.exit(1);
        }

        int portNumber = Integer.parseInt(args[0]);

        try (
            ServerSocket serverSocket =
                new ServerSocket(Integer.parseInt(args[0]));
            Socket clientSocket = serverSocket.accept();
            PrintWriter out =
                new PrintWriter(clientSocket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(
                new InputStreamReader(clientSocket.getInputStream()));
        ) {
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                System.out.println("Invert Server received: " + inputLine);
                out.println(invert_str(inputLine));
            }
        } catch (IOException e) {
            System.out.println("Exception caught when trying to listen on port "
                + portNumber + " or listening for a connection");
            System.out.println(e.getMessage());
        }
    }
}
