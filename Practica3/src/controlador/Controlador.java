package controlador;

import cliente.Cliente;
import serverUDP.ServerPrincipal;


public class Controlador {

    public static void main(String[] args) {
        try {
            new ServerPrincipal().start();
            new Cliente().start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
