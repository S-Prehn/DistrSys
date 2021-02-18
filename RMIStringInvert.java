import java.rmi.*;
import java.rmi.server.*;

/*
This code has been referred from : https://www.pk.org/rutgers/notes/content/rb-rmi.pdf
*/

public class RMIStringInvert extends UnicastRemoteObject implements RMIStringInvertInterface {
    public RMIStringInvert() throws RemoteException {
    }

    public String invert_str(String str) throws RemoteException {
        System.out.println("RMI Server Received: " + str);
        return new StringBuffer(str).reverse().toString();
    }
}
