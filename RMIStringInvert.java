import java.rmi.*;
import java.rmi.server.*;

public class RMIStringInvert extends UnicastRemoteObject implements RMIStringInvertInterface {
    public RMIStringInvert() throws RemoteException {}

    public String invert_str(String str) throws RemoteException {
        System.out.println("RMI Server Received: " + str);
        return new StringBuffer(str).reverse().toString();
    }
}
