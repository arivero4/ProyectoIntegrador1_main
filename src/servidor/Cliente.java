package servidor;

import java.io.*;
import java.net.*;

public class Cliente {
    public static void main(String[] args) {
        try (Socket socket = new Socket("localhost", 5000);
             ObjectOutputStream salida = new ObjectOutputStream(socket.getOutputStream());
             ObjectInputStream entrada = new ObjectInputStream(socket.getInputStream())) {

            salida.writeObject("Registrar nueva inspección fitosanitaria");
            System.out.println("Respuesta del servidor: " + entrada.readObject());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
