package serverTCP;

import cliente.Cliente;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.util.StringTokenizer;


public class GestorPeticion extends Thread{

    private static Socket s;
    private int clienteID;
    static public final int			TIMEOUT	= 20000; //timeout de 20 segunds (20000 milisegundos)


    public GestorPeticion(Socket socket,int clienteID) {
        s=socket;
        this.clienteID=clienteID;
    }


    @Override
    public void run(){
        try {
            manejaPeticion(clienteID);
        } catch (IOException e) {
            System.out.println("Cliente desconectado");
        }
    }

    public static void manejaPeticion(int id) throws IOException{
        long startTime = System.currentTimeMillis();
        BufferedReader sIn = new BufferedReader(new InputStreamReader(s.getInputStream()));
        PrintStream sOut = new PrintStream(s.getOutputStream());
        while(true) {
            float newTime = System.currentTimeMillis() - startTime;
            if (newTime > TIMEOUT) {
                System.out.println("Tiempo total de 20 segundos alcanzado. Cerrando conexión.");
                break; // Salir del bucle después de 20 segundos
            }
            String newCookie="";
            StringBuilder cookie = new StringBuilder();
            StringBuilder mensaje = new StringBuilder();
            String linea="";
            String contenido="";
            while((linea = sIn.readLine()) != null) {
                if(linea.isEmpty()) {
                    break;
                }
                //tener en cuenta posibles huecos en blanco por basura en el buffer, solo por precaución
                String sinBlancoslinea=linea.strip();
                mensaje.append(sinBlancoslinea).append('\n');
                StringTokenizer cadena = new StringTokenizer(linea," ");
                String primerCampo = cadena.nextToken().strip();
                if(primerCampo.equals("GET")){
                    String segundoCampo = cadena.nextToken();
                    if(segundoCampo.charAt(0) == '/' && segundoCampo.length()==1) {
                        cookie.append('#').append("index");
                        contenido = Cliente.mostrarContenido();
                    }else {
                        if(segundoCampo.substring(1).equals("infoS1.html")){
                            contenido = Cliente.mostrarInfo("S1");
                        }else if(segundoCampo.substring(1).equals("infoS2.html")){
                            contenido = Cliente.mostrarInfo("S2");
                        }else if(segundoCampo.substring(1).equals("infoS3.html")){
                            contenido = Cliente.mostrarInfo("S3");
                        }
                        cookie.append('#').append(segundoCampo.substring(1));
                    }
                }
                if(primerCampo.equals("Cookie:")) {
                    if(!cadena.hasMoreElements()) {
                        cookie.deleteCharAt(0);
                    }else {
                        newCookie = cadena.nextToken().toString();
                    }
                }

            }
            String cookieDef="";
            if(newCookie != ""){
                int posicionPuntoYComa = newCookie.indexOf(';');

                if (posicionPuntoYComa == -1) {
                    newCookie = newCookie + ";";
                    posicionPuntoYComa = newCookie.indexOf(';');
                }

                cookieDef= newCookie.substring(0, posicionPuntoYComa) + cookie.toString() + newCookie.substring(posicionPuntoYComa);

            }else {
                String cabecera = "PPC=";
                int posicionPuntoYComa = cabecera.indexOf(';');

                if (posicionPuntoYComa == -1) {
                    cabecera = cabecera + ";";
                    posicionPuntoYComa = cabecera.indexOf(';');
                }

                cookieDef= cabecera.substring(0, posicionPuntoYComa) + cookie.toString() + cabecera.substring(posicionPuntoYComa);

            }

            System.out.println(mensaje);
            newTime = TIMEOUT - newTime;
            String respuesta = procesarRespuesta(contenido,mensaje, cookieDef, id, newTime);
            sOut.println(respuesta);
            sOut.flush();
        }
        sIn.close();
        sOut.close();
        s.close();
    }


    public static String procesarRespuesta(String contenido, StringBuilder mensaje, String cookie, int id, float time) {
        String respuesta;

        String archivoHTML ="<html><head>\n"
                + "<title> Fichero HTML de prueba</title>\n"
                + "</head><body>\n"
                + "<h1>Este fichero muestra la informacion pedida por el cliente</h1>\n"
                + "<p>"+ contenido +"</p>"
                + "</body></html>";
        int bytes = tamBytes(archivoHTML);
        StringBuilder newCookie = new StringBuilder(cookie);
        respuesta = "HTTP/1." + id + " 200 OK\n" + "Connection: keep-alive\n" + "Content-Type: text/html\n" + "Set-Cookie: " + newCookie+ "\r\n" +  "Content-Length: " + bytes +"\n" + "Keep-Alive: timeout=" + (int)time/1000  + "\n" + "\r\n" + archivoHTML + "\r\n";


        //System.out.println(respuesta);

        return respuesta;

    }

    public static int tamBytes(String archivo) {
        byte[] bytes = archivo.getBytes();
        return bytes.length;
    }
}