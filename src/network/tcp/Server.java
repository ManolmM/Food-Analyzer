package network.tcp;

import command.Command;
import command.CommandCreator;
import command.CommandExecutor;
import exceptions.NoCommandProvidedException;
import exceptions.MissingCommandArgumentsException;
import exceptions.UnknownCommandException;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;

public class Server {
    private static final int BUFFER_SIZE = 1024;
    private static final String HOST = "localhost";

    private final CommandExecutor commandExecutor;

    private final int port;
    private boolean isServerWorking;

    private ByteBuffer buffer;
    private Selector selector;

    public Server(int port, CommandExecutor commandExecutor) {
        this.port = port;
        this.commandExecutor = commandExecutor;
    }

    public void start() {
        try (ServerSocketChannel serverSocketChannel = ServerSocketChannel.open()) {
            selector = Selector.open();
            configureServerSocketChannel(serverSocketChannel, selector);
            this.buffer = ByteBuffer.allocate(BUFFER_SIZE);
            isServerWorking = true;
            while (isServerWorking) {
                try {
                    int readyChannels = selector.select();
                    if (readyChannels == 0) {
                        continue;
                    }

                    Iterator<SelectionKey> keyIterator = selector.selectedKeys().iterator();
                    while (keyIterator.hasNext()) {
                        SelectionKey key = keyIterator.next();
                        if (key.isReadable()) {

                            SocketChannel clientChannel = (SocketChannel) key.channel();
                            String clientErrorMessage;
                            try {
                                String clientInput = getClientInput(clientChannel);
                                System.out.println("Client request: " + clientInput);
                                Command inputCommand = CommandCreator.newCommand(clientInput);
                                commandExecutor.takeCommand(inputCommand);
                                String output = commandExecutor.placeCommand();
                                writeClientOutput(clientChannel, output);

                            } catch (NoCommandProvidedException e) {
                                clientErrorMessage = "No command is provided.";
                                writeClientOutput(clientChannel, clientErrorMessage);
                                break;

                            } catch (UnknownCommandException e) {
                                clientErrorMessage = "Unknown or invalid command is provided.";
                                writeClientOutput(clientChannel, clientErrorMessage);
                                break;

                            } catch (MissingCommandArgumentsException e) {
                                clientErrorMessage = "Command not completed. Missing arguments.";
                                writeClientOutput(clientChannel, clientErrorMessage);
                                break;

                            } catch (IOException e) {
                                if (e.getMessage().equals("Unable to write new data to the dataset file.")) {
                                    //TODO Add method that stores this exception to a file
                                }
                                if (e.getMessage().equals("Unable to open dataset file.")) {
                                    //TODO Add method that stores this exception to a file
                                }
                            }
                        } else if (key.isAcceptable()) {
                            accept(selector, key);
                        }

                        keyIterator.remove();
                    }
                } catch (IOException e) {
                    System.out.println("Error occurred while processing client request: " + e.getMessage());
                }
            }
        } catch (IOException e) {
            throw new UncheckedIOException("failed to start server", e);
        }
    }

    public void stop() {
        this.isServerWorking = false;
        if (selector.isOpen()) {
            selector.wakeup();
        }
    }

    private void configureServerSocketChannel(ServerSocketChannel channel, Selector selector) throws IOException {
        channel.bind(new InetSocketAddress(HOST, this.port));
        channel.configureBlocking(false);
        channel.register(selector, SelectionKey.OP_ACCEPT);
    }

    private String getClientInput(SocketChannel clientChannel) throws IOException, NoCommandProvidedException {
        buffer.clear();

        int readBytes = clientChannel.read(buffer);
        if (readBytes < 0) {
            clientChannel.close();
            throw new NoCommandProvidedException("No command is provided from the client");
        }

        buffer.flip();

        byte[] clientInputBytes = new byte[buffer.remaining()];
        buffer.get(clientInputBytes);

        return new String(clientInputBytes, StandardCharsets.UTF_8);
    }

    private void writeClientOutput(SocketChannel clientChannel, String output) throws IOException {
        buffer.clear();
        buffer.put(output.getBytes());
        buffer.flip();

        clientChannel.write(buffer);
    }

    private void accept(Selector selector, SelectionKey key) throws IOException {
        ServerSocketChannel sockChannel = (ServerSocketChannel) key.channel();
        SocketChannel accept = sockChannel.accept();

        accept.configureBlocking(false);
        accept.register(selector, SelectionKey.OP_READ);
        System.out.println("New client connected.");
    }

    public static void main(String[] args) {
        CommandExecutor executor = CommandExecutor.newInstance();
        Server server = new Server(7777, executor);
        server.start();
    }
}