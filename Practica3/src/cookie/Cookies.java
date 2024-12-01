package Cookie;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.StringTokenizer;

public class Cookies {



    public static void escribirGalleta(String cookie) {
        StringBuilder contenidoArchivo = new StringBuilder();
        cookie.substring(4);
        String rutaArchivo = "./sources/galletas/galletas.txt";
        try (BufferedReader reader = new BufferedReader(new FileReader(rutaArchivo))) {
            String linea;
            while ((linea = reader.readLine()) != null) {
                int inicioContenido = linea.indexOf('=') + 1;
                linea = linea.substring(0, inicioContenido) + cookie;
                contenidoArchivo.append(linea).append(System.lineSeparator());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(rutaArchivo))) {
            writer.write(contenidoArchivo.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String leerGalleta() {

        String rutaArchivo = "./sources/galletas/galletas.txt";  // Nombre o ruta del archivo
        String linea="";
        try (BufferedReader reader = new BufferedReader(new FileReader(rutaArchivo))) {
            while ((linea = reader.readLine()) != null) {
                if (linea.startsWith("http")) {
                    linea = linea.substring(9);
                    return linea;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


        return linea;
    }

    public static String leerGalletaCompleta() {
        String rutaArchivo = "./sources/galletas/galletas.txt";
        String linea="";
        try (BufferedReader reader = new BufferedReader(new FileReader(rutaArchivo))) {
            while ((linea = reader.readLine()) != null) {
                if (linea.startsWith("http")) {
                    StringTokenizer cadena = new StringTokenizer(linea," ");
                    cadena.nextToken();
                    return cadena.nextToken().toString();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


        return linea;
    }
}