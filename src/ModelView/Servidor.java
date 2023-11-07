
package ModelView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;


public class Servidor {
    private DatagramSocket socket;
    private List<InetAddress> clientAddresses;
    private List<Integer> clientPorts;

    public Servidor(int serverPort) throws SocketException {
        socket = new DatagramSocket(serverPort);
        clientAddresses = new ArrayList<>();
        clientPorts = new ArrayList<>();
    }

    public void startServer() {
        try {
            while (true) {
                byte[] receiveData = new byte[1024];
                DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                socket.receive(receivePacket);

                InetAddress clientAddress = receivePacket.getAddress();
                int clientPort = receivePacket.getPort();

                // Obtener el mensaje o archivo recibido
                byte[] data = receivePacket.getData();
                String message = new String(data, 0, receivePacket.getLength());

                // Procesar el mensaje o archivo según tus necesidades
                if (message.startsWith("MSG:")) {
                    // Es un mensaje de texto, puedes procesarlo y reenviarlo a otros clientes
                    String messageContent = message.substring(4); // Elimina "MSG:"
                    sendMessageToOtherClients(messageContent, clientAddress, clientPort);
                } else if (message.startsWith("FILE:")) {
                    // Es un archivo, puedes procesarlo y guardar o reenviar según tu lógica
                    processReceivedFile(data, receivePacket.getLength(), clientAddress, clientPort);
                }

                // Agregar la dirección y puerto del cliente si no están en la lista
                if (!clientAddresses.contains(clientAddress) || !clientPorts.contains(clientPort)) {
                    clientAddresses.add(clientAddress);
                    clientPorts.add(clientPort);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sendMessageToOtherClients(String message, InetAddress senderAddress, int senderPort) {
        for (int i = 0; i < clientAddresses.size(); i++) {
            InetAddress clientAddress = clientAddresses.get(i);
            int clientPort = clientPorts.get(i);

            if (!clientAddress.equals(senderAddress) || clientPort != senderPort) {
                // Reenviar el mensaje a otros clientes
                String responseMessage = "MSG:" + message;
                DatagramPacket sendPacket = new DatagramPacket(responseMessage.getBytes(), responseMessage.length(), clientAddress, clientPort);
                try {
                    socket.send(sendPacket);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void processReceivedFile(byte[] data, int length, InetAddress senderAddress, int senderPort) {
        try {
            // El mensaje recibido es de la forma "FILE:NombreDelArchivo"
            String message = new String(data, 0, length);
            String fileName = message.substring(5); // Elimina "FILE:"

            // Directorio donde se guardarán los archivos recibidos (ajusta la ruta según tus necesidades)
            String saveDirectory = "archivos_recibidos/";

            // Crea el directorio si no existe
            File directory = new File(saveDirectory);
            if (!directory.exists()) {
                directory.mkdirs();
            }

            // Ruta completa para guardar el archivo
            String filePath = saveDirectory + fileName;

            // Crea un nuevo archivo y escribe los datos recibidos en él
            FileOutputStream fileOutputStream = new FileOutputStream(filePath);
            fileOutputStream.write(data, 5, length - 5); // El 5 corresponde a "FILE:"
            fileOutputStream.close();

            // Puedes realizar otras acciones, como notificar a otros clientes o procesar el archivo de alguna manera
            System.out.println("Archivo recibido y guardado en: " + filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
}

    public static void main(String[] args) {
        int serverPort = 12345;
        try {
            Servidor servidor = new Servidor(serverPort);
            servidor.startServer();
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }
}


