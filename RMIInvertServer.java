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
