
package ModelView;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;


public class Cliente implements Chat {
    private DatagramSocket socket;
    private InetAddress address;
    private int port;

    public Cliente(String address, int port) throws IOException {
        socket = new DatagramSocket();
        this.address = InetAddress.getByName(address);
        this.port = port;
    }

    @Override
    public void listen() throws IOException {
        // Escuchar mensajes de los clientes
        while (true) {
            byte[] receiveData = new byte[1024];
            DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
            socket.receive(receivePacket);

            // Leer el mensaje
            String message = new String(receiveData, 0, receivePacket.getLength());

            // Mostrar el mensaje en la consola
            System.out.println("Cliente: " + message);
        }
    }

    @Override
    public void send(String message) throws IOException {
        // Enviar mensaje al servidor
        byte[] sendData = message.getBytes();
        DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, address, port);
        socket.send(sendPacket);
    }
    
    @Override
    public void sendFile(File file) throws IOException {
        // Obtener el contenido del archivo
        byte[] fileBytes = new byte[(int) file.length()];
        FileInputStream inputStream = new FileInputStream(file);
        inputStream.read(fileBytes);
        inputStream.close();

        // Enviar el archivo al servidor
        byte[] sendData = fileBytes;
        DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, address, port);
        socket.send(sendPacket);
    }


}

