package serverTCP;

/**
 * Created on 06-oct-2021
 *
 * @author Humberto Martinez Barbera
 */

import java.io.*;
import java.net.*;
import javax.net.ssl.*;
import java.security.*;

public class SSLServer extends Thread
{
    static public final boolean			DEBUG		= true;

    static public final String			SV_STORE	= "./sources/certificados/cliente.ks";
    static public final String			SV_PWD		= "galleta";
    static public final String			SV_CERT_PWD	= "galleta";
    static public final String			CA_STORE	= "./sources/certificados/ca.ks";
    static public final String			CA_PWD		= "galleta";
    private static final int			SEGURO		= 1;
    private int puertoSeguro;




    public SSLServer(int s) {
        this.puertoSeguro=s;
    }

    public void run() {
        try {
            while(true) {
                instanceEchoServerCert(puertoSeguro);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    static public void instanceEchoServerCert (int port)
    {
        SSLContext				ctx;
        SSLServerSocketFactory	fac;
        SSLServerSocket			s;
        Socket					s1;
        KeyStore 				ksm, kst;
        KeyManagerFactory 		kmf;
        TrustManagerFactory 	tmf;

        try {

            ksm = KeyStore.getInstance("JKS");
            ksm.load(new FileInputStream(SV_STORE), SV_PWD.toCharArray());
            kmf = KeyManagerFactory.getInstance("SunX509");
            kmf.init(ksm, SV_CERT_PWD.toCharArray());

            kst = KeyStore.getInstance("JKS");
            kst.load(new FileInputStream(CA_STORE), CA_PWD.toCharArray());
            tmf = TrustManagerFactory.getInstance("SunX509");
            tmf.init(kst);

            ctx = SSLContext.getInstance("TLSv1.2");
            ctx.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);
            fac = ctx.getServerSocketFactory();
            s =  (SSLServerSocket) fac.createServerSocket(port);
            s.setNeedClientAuth(true);

            while (s != null)
            {
                s1 = s.accept();
                new GestorPeticion(s1, SEGURO).start();
            }

        } catch (Exception e) { e.printStackTrace(); }
    }
}