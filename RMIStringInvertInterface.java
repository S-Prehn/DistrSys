import java.rmi.*;

/*
This code has been referred from : https://www.pk.org/rutgers/notes/content/rb-rmi.pdf
*/

public interface RMIStringInvertInterface extends Remote {
    public String invert_str(String str) throws RemoteException;
}
