import java.rmi.*;
import java.rmi.server.*;

/*
This code has been referred from : https://www.pk.org/rutgers/notes/content/rb-rmi.pdf
*/

public class RMIInvertServer {
    public static void main(String[] args) {
        try {
            Naming.rebind("rmi-string-invert", new RMIStringInvert());
            System.out.println("RMI Server is running");
        } catch (Exception e) {
            System.out.println("RMI Server failed: " + e);
        }
    }
}
