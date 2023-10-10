package network.tcp;

import command.Command;
import command.CommandCreator;
import command.CommandExecutor;
import exceptions.NoCommandProvidedException;
import exceptions.MissingCommandArgumentsException;
import exceptions.UnknownCommandException;
import network.https.properties.Properties;
import storage.foods.DataExchanger;
import storage.foods.FileFoodHandler;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.Iterator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
    private static final int BUFFER_SIZE = 32768;
    private final CommandExecutor commandExecutor;

    private Path filePath;
    private final int port;
    private boolean isServerWorking;

    private ByteBuffer buffer;
    private Selector selector;
    private ExecutorService executorService = Executors.newCachedThreadPool();

    public Server(Path filePath, int port, CommandExecutor commandExecutor) {
        this.filePath = filePath;
        this.port = port;
        this.commandExecutor = commandExecutor;
    }

    public void start() {
        try (ServerSocketChannel serverSocketChannel = ServerSocketChannel.open()) {
            selector = Selector.open();
            configureServerSocketChannel(serverSocketChannel, selector);
            this.buffer = ByteBuffer.allocate(BUFFER_SIZE);
            isServerWorking = true;

            FileFoodHandler fileFoodHandler = FileFoodHandler.newInstance(filePath);
            DataExchanger dataExchanger = DataExchanger.of(fileFoodHandler);
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
                                Command inputCommand = CommandCreator.newCommand(dataExchanger, clientInput);
                                commandExecutor.takeCommand(inputCommand);
                                String output = commandExecutor.placeCommand();
                                executorService.submit(() -> {
                                    try {
                                        writeClientOutput(clientChannel, output);
                                    } catch (IOException e) {
                                        throw new RuntimeException(e);
                                    }
                                });

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
        channel.bind(new InetSocketAddress(Properties.HOST, this.port));
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
}
