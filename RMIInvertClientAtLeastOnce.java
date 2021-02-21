import java.rmi.*;

/*
This code has been referred from : https://www.pk.org/rutgers/notes/content/rb-rmi.pdf
*/

public class RMIInvertClientAtLeastOnce {
    public static void main(String[] args) {
        while (true) {
            try {
                if (args.length != 2) {
                    System.err.println("usage: java RMIInvertClientAtLeastOnce <host> <input> …\n");
                    System.exit(1);
                }
                String url = "//" + args[0] + "/rmi-string-invert";
                RMIStringInvertInterface strInvert = (RMIStringInvertInterface) Naming.lookup(url);
                System.out.println("RMI Invert Client Received: " + strInvert.invert_str(args[1]));
                break;
            } catch (Exception e) {
                System.out.println("RMIInvertClient exception: " + e);
            }
        }
    }
}
