package network.tcp;

import command.Command;
import command.CommandCreator;
import command.CommandExecutor;
import command.queries.InsertQuery;
import exceptions.MissingExtractedDataException;
import exceptions.NoCommandProvidedException;
import exceptions.MissingCommandArgumentsException;
import exceptions.UnknownCommandException;
import network.https.properties.Properties;
import storage.databases.ibm_db2.DataExchanger;


import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.InetSocketAddress;
import java.net.URISyntaxException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class Server {
    private static final int BUFFER_SIZE = 32768;
    private final CommandExecutor commandExecutor;
    private DataExchanger dataExchanger;
    private InsertQuery insertQuery;
    private final int port;
    private boolean isServerWorking;

    private ByteBuffer buffer;
    private Selector selector;
    private ExecutorService executorService = Executors.newCachedThreadPool();

    private Server(int port, CommandExecutor commandExecutor) throws SQLException, IOException {
        this.port = port;
        this.commandExecutor = commandExecutor;
        dataExchanger = DataExchanger.of(new InsertQuery());
    }

    public static Server of(int port, CommandExecutor commandExecutor) throws SQLException, IOException {
        return new Server(port, commandExecutor);
    }

    /**
     * Configure the server attributes.
     * Available to connect with new clients.
     * Sends and receives data to each client.
     * Extracts data from REST API or a file when a client provide a appropriate commands.
     */
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
                                Command inputCommand = CommandCreator.newCommand(dataExchanger, clientInput);
                                commandExecutor.takeCommand(inputCommand);
                                List<String> output = commandExecutor.placeCommand();

                                writeClientOutput(clientChannel, output.get(0));
                            } catch (NoCommandProvidedException e) {
                                clientErrorMessage = "No command is provided.";
                                writeClientOutput(clientChannel, clientErrorMessage);
                                keyIterator.remove();
                                break;
                            } catch (UnknownCommandException e) {
                                clientErrorMessage = "Unknown or invalid command is provided.";
                                writeClientOutput(clientChannel, clientErrorMessage);
                                keyIterator.remove();
                                break;
                            } catch (MissingCommandArgumentsException e) {
                                clientErrorMessage = "Command not completed. Missing arguments.";
                                writeClientOutput(clientChannel, clientErrorMessage);
                                keyIterator.remove();
                                break;
                            } catch (MissingExtractedDataException e) {
                                if (e.getMessage().equals("No such item in the REST API server")) {
                                    writeClientOutput(clientChannel, "Unable to find food. Try again with another key");
                                } else {
                                    writeClientOutput(clientChannel, e.getMessage());
                                }
                                keyIterator.remove();
                                break;
                            } catch (SQLException e) {
                                clientErrorMessage = "Problem occurred while storing data. Try again, or contact an administrator";
                                writeClientOutput(clientChannel, clientErrorMessage);
                                keyIterator.remove();
                                break;
                            }catch (URISyntaxException e) {
                                writeClientOutput(clientChannel, "Unexpected error occurred. Unable to provide food");
                                keyIterator.remove();
                                break;
                            } catch (InterruptedException e) {
                                writeClientOutput(clientChannel, "Unexpected error occurred. Unable to provide food");
                                keyIterator.remove();
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

    /**
     * Reads the client's input from buffer.
     * @return String
     */
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


    /**
     * Writes the output in the buffer.
     */
    private synchronized void writeClientOutput(SocketChannel clientChannel, String output) throws IOException {
        buffer.clear();
        buffer.put(output.getBytes());
        buffer.flip();

        clientChannel.write(buffer);
    }

    /**
     * Accepts the incoming client connections.
     *
     */
    private void accept(Selector selector, SelectionKey key) throws IOException {
        ServerSocketChannel sockChannel = (ServerSocketChannel) key.channel();
        SocketChannel accept = sockChannel.accept();

        accept.configureBlocking(false);
        accept.register(selector, SelectionKey.OP_READ);
        System.out.println("New client connected.");
    }
}
