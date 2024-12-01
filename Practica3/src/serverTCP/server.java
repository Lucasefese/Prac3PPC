package serverTCP;
import cliente.Cliente;


public class server{

    public static int puertoSeguro = 4430;
    public static int puertoNoSeguro = 3754;

    public static void main(String args[]){
        try {
            new SSLServer(puertoSeguro).start();
            new servidorHTTP(puertoNoSeguro).start();
            new Cliente().start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}

