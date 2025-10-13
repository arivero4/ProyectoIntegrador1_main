package servidor;

import java.io.*;
import java.net.*;

public class Servidor {
    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(5000)) {
            System.out.println("Servidor en ejecución. Esperando clientes...");
            while (true) {
                Socket socket = serverSocket.accept();
                new Thread(new ManejadorCliente(socket)).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

class ManejadorCliente implements Runnable {
    private Socket socket;
    
    public ManejadorCliente(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try (ObjectInputStream entrada = new ObjectInputStream(socket.getInputStream());
             ObjectOutputStream salida = new ObjectOutputStream(socket.getOutputStream())) {

            Object solicitud = entrada.readObject();
            System.out.println("Solicitud recibida: " + solicitud);

            // Aquí podrías instanciar los gestores de negocio
            salida.writeObject("Servidor recibió: " + solicitud);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
