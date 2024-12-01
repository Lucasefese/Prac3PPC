package serverTCP;

import java.net.ServerSocket;
import java.net.Socket;

public class servidorHTTP extends Thread{


    private static final int			NO_SEGURO		= 0;
    private ServerSocket s;
    private int puertoSeguro;

    public servidorHTTP(int puertoSeguro) {
        this.puertoSeguro=puertoSeguro;
    }


    @Override
    public void run(){
        try {
            s = new ServerSocket(puertoSeguro);
            while(true) {
                Socket socket = s.accept();
                new GestorPeticion(socket, NO_SEGURO).start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}