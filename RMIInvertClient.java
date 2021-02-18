import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.rmi.*;

/*
This code has been referred from : https://www.pk.org/rutgers/notes/content/rb-rmi.pdf
*/

public class RMIInvertClient {
    public static void main(String[] args) {
        try {
            if (args.length < 0) {
                System.err.println("usage: java RMIInvertClient <host> …\n");
                System.exit(1);
            }
            String url = "//" + args[0] + "/rmi-string-invert";
            RMIStringInvertInterface strInvert = (RMIStringInvertInterface) Naming.lookup(url);

            BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));

            String userInput;
            while ((userInput = stdIn.readLine()) != null) {
                System.out.println("RMI Invert Client Received: " + strInvert.invert_str(userInput));
            }
        } catch (Exception e) {
            System.out.println("RMIInvertClient exception: " + e);
        }
    }
}
