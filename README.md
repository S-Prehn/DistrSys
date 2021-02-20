# Distributed Systems - Assignment 2 - Group 12

* Deepshi Garg : S4199456
* Shubham Jinde : S3993914
* Sebastian Prehn : S3013472

## Run Instructions:

* The java scripts can be compiled executing the attached bash script [compile.sh](./compile.sh) with: `bash compile.sh`

### Part a: Socket Implementation:

* The `SocketInvertServer` takes as input the port number it should connect to, like for instance: `java SocketInvertServer <port-number>`
* The `SocketInvertClient` Module takes as input the name of the machine that hosts the `SocketInvertServer` and the server's port id. For instance, assuming that server and client run on the same machine the compiled script can be run with: `java SocketInvertClient <server-host> <server-port>`
* Enter strings to invert in the client terminal

___

### Part b: RMI Implementation:

* Start the RMI registry in background with the command: `rmiregistry &`
* Start the RMI server using the command: `java RMIInvertServer`
* Start the RMI client using the command: `java RMIInvertClient <server-host>`
* Enter strings to invert in the client terminal

## Content Source Files

The code from the files (without the copyright notes that are in the scripts) are:

### SocketInvertClient:

```java
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
```

### SocketInvertServer:

```java
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
```

### RMIStringInvertInterface

```java
import java.rmi.*;

public interface RMIStringInvertInterface extends Remote {
    public String invert_str(String str) throws RemoteException;
}
```

### RMIStringInvert

```java
import java.rmi.*;
import java.rmi.server.*;

public class RMIStringInvert extends UnicastRemoteObject implements RMIStringInvertInterface {
    public RMIStringInvert() throws RemoteException {}

    public String invert_str(String str) throws RemoteException {
        System.out.println("RMI Server Received: " + str);
        return new StringBuffer(str).reverse().toString();
    }
}

```

### RMIInvertServer

```java

import java.rmi.*;
import java.rmi.server.*;

public class RMIInvertServer {
    public static void main(String[] args) {
        try {
            Naming.rebind("rmi-string-invert", new RMIStringInvert());
            System.out.println("RMI Server is running");
        } catch(Exception e) {
            System.out.println("RMI Server failed: " + e);
        }
    }
}

```

### RMIInvertClient

```java
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.rmi.*;

public class RMIInvertClient {
    public static void main(String[] args) {
        try {
            if (args.length < 0) {
                System.err.println("usage: java RMIInvertClient <host> …\n");
                System.exit(1);
            }
            String url = "//" + args[0] + "/rmi-string-invert";
            RMIStringInvertInterface strInvert = (RMIStringInvertInterface) Naming.lookup(url);

            BufferedReader stdIn =  new BufferedReader(new InputStreamReader(System.in));

            String userInput;
            while ((userInput = stdIn.readLine()) != null) {
                System.out.println("RMI Invert Client Received: " + strInvert.invert_str(userInput));
            }
        } catch (Exception e) {
            System.out.println("RMIInvertClient exception: " + e);
        }
    }
}
```

###  Part c: Is it possible to achieve “Exactly-Once” semantics for RMI method invocations?

* `RPC(Remote Procedure Call)` allows client programs to call procedure in server programs running in different processes/system

* `RPI(Remote Procedure Invocation)` allows an object living in one process to invoke the methods of an object living in another process.

* `RPI(Remote Procedure Invocation)` involves invocations between objects in different processes, whether in the same computer or not. Any remote invocation may fail for reason related to the invoked object being in different computer or different process other than invoker. 

* The invocation procedure is implemented by using request-reply protocol to provide different delivery guarantees. 

* Main Design Choices for Implementing RMI - 
   
    Retry request message - Whether to retransmit request message when server has failed or reply is not received.

    Duplicate Filtering   - In case of retransmission whether to filter out duplicate requests or not.

    Retransmission of results - Whether to keep history of results to enable lost results are retransmitted with re-execution on the server. 

* Combination of these choices can lead to following semantics
    
    Maybe - The remote procedure may be executed excatly once or not at all. This semantics arises when there is no fault tolerance measure applied. 

    At Least Once - This is achieved by retransmission of request messages. The retranmission will take place if there is exception and the invoker  did not receive any results. The exception may happen if the server crashes  

    At-Most-Once - This is achieved by using the combination of fault tolerance measures(retransmission and duplicate filtering). The duplicate filtering will be done by the server. 

* Thus under failures/exception and how the system handles failure/exceptions it is very difficult(not possible) to achieve excatly once semantics. This is because the server may not execute the function in the following cases

    * Request message is dropped
    * Reply message is dropped 
    * Called process fails before executing request
    * Called process fails after executing request
    * Also it is hard for the server to know where excatly failure has taken place in the last two conditions

* Also the function may be executed multiple times if
    * If request message is transmitted multiple times.  

* Thus it is not possible to achieve excatly once semantics for RMI method invocation.      








