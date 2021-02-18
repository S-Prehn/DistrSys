import java.rmi.*;

public interface RMIStringInvertInterface extends Remote {
    public String invert_str(String str) throws RemoteException;
}
