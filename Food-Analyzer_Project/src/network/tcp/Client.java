package network.tcp;

import network.https.properties.Properties;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Scanner;

public class Client {
    private static final int BUFFER_SIZE = 32768;
    private static ByteBuffer buffer = ByteBuffer.allocateDirect(BUFFER_SIZE);

    public static void main(String[] args) {

        try (SocketChannel socketChannel = SocketChannel.open();
             Scanner scanner = new Scanner(System.in)) {

            socketChannel.connect(new InetSocketAddress(Properties.SERVER_HOST, Properties.SERVER_PORT));

            System.out.println("Connected to the server.");

            while (true) {
                System.out.print("Enter command: ");
                String message = scanner.nextLine(); // read a line from the console

                if ("quit".equals(message)) {
                    break;
                }

                System.out.println("Sending message " + message);

                buffer.clear(); // switch to writing mode
                buffer.put(message.getBytes()); // buffer fill
                buffer.flip(); // switch to reading mode
                socketChannel.write(buffer); // buffer drain

                buffer.clear(); // switch to writing mode
                socketChannel.read(buffer); // buffer fill
                buffer.flip(); // switch to reading mode

                byte[] byteArray = new byte[buffer.remaining()];
                buffer.get(byteArray);
                String reply = new String(byteArray, "UTF-8"); // buffer drain

                System.out.println("The server replied:\n" + reply);
            }

        } catch (IOException e) {
            throw new RuntimeException("There is a problem with the network communication", e);
        }
    }
}