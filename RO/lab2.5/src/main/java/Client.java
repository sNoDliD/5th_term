import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeoutException;

public class Client {
    private static final String splitter = "#";
    private Connection connection;
    private Socket socket;
    private DataOutputStream out;
    private DataInputStream in;
    Channel channelFromClient;
    Channel channelToClient;
    private String responseString;

    public Client() throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.newConnection();
        factory.setHost("localhost");
        connection = factory.newConnection();
        channelFromClient = connection.createChannel();
        channelToClient = connection.createChannel();
        channelFromClient.queueDeclare("fromClient", false, false, false, null);
        channelToClient.queueDeclare("toClient", false, false, false, null);
    }

    public boolean addKnifeType(KnifeType knifeType) throws IOException {
        List<String> response = new ArrayList<>(sendQuery(QueryType.ADD_KNIFE_TYPE, List.of(knifeType.getName())));
        String status = popArgument(response);

        return status.equals("Nothing");
    }

    public boolean deleteKnifeType(KnifeType knifeType) throws IOException {
        List<String> response = new ArrayList<>(sendQuery(QueryType.DELETE_KNIFE_TYPE, List.of(knifeType.getName())));
        String status = popArgument(response);

        return status.equals("Nothing");
    }

    public boolean addKnife(Knife knife) throws IOException {
        List<String> response = new ArrayList<>(sendQuery(QueryType.ADD_KNIFE, knife.toList()));
        String status = popArgument(response);

        return status.equals("Nothing");
    }

    public boolean deleteKnife(Knife knife) throws IOException {
        List<String> response = new ArrayList<>(sendQuery(QueryType.DELETE_KNIFE, knife.toList()));
        String status = popArgument(response);

        return status.equals("Nothing");
    }

    public boolean updateKnife(Knife oldKnife, Knife newKnife) throws IOException {
        List<String> arguments = oldKnife.toList();
        arguments.addAll(newKnife.toList());

        List<String> response = new ArrayList<>(sendQuery(QueryType.UPDATE_KNIFE, arguments));
        String status = popArgument(response);

        return status.equals("Nothing");
    }

    public int countKnivesByKnifeType(KnifeType knifeType) throws IOException {
        List<String> response = new ArrayList<>(sendQuery(QueryType.COUNT_KNIVES_BY_KNIFE_TYPE, knifeType.toList()));
        String status = popArgument(response);

        if (status.equals("Nothing")) {
            return Integer.parseInt(popArgument(response));
        }

        throw new RuntimeException("Error while counting knives");
    }

    public Knife getKnifeByName(Knife knife) throws IOException {
        List<String> response = new ArrayList<>(sendQuery(QueryType.GET_KNIFE_BY_NAME, knife.toList()));
        String status = popArgument(response);

        if (status.equals("Nothing")) {
            return new Knife(popArguments(response, Knife.listSize()));
        }

        throw new RuntimeException("Error while getting knife");
    }

    public List<Knife> getKnivesByKnifeType(KnifeType knifeType) throws IOException {
        List<String> response = new ArrayList<>(sendQuery(QueryType.GET_KNIVES_BY_KNIFE_TYPE, knifeType.toList()));
        String status = popArgument(response);

        if (status.equals("Nothing")) {
            int count = Integer.parseInt(popArgument(response));

            List<Knife> knives = new ArrayList<>();

            for (int i = 0; i < count; i++) {
                knives.add(new Knife(popArguments(response, Knife.listSize())));
            }

            return knives;
        }

        throw new RuntimeException("Error while getting knives by knifeType");
    }

    public List<KnifeType> getKnifeTypes() throws IOException {
        List<String> response = new ArrayList<>(sendQuery(QueryType.GET_KNIFE_TYPES, List.of()));
        String status = popArgument(response);

        if (status.equals("Nothing")) {
            List<KnifeType> knifeTypes = new ArrayList<>();
            int count = Integer.parseInt(popArgument(response));

            for (int i = 0; i < count; i++) {
                knifeTypes.add(new KnifeType(popArguments(response, KnifeType.listSize())));
            }
            return knifeTypes;
        }

        throw new RuntimeException("Error while getting knifeTypes. Error code: " + response);
    }

    private String popArgument(List<String> args) {
        String argument = args.get(0);
        args.remove(0);

        return argument;
    }

    private List<String> popArguments(List<String> args, int size) {
        List<String> arguments = new ArrayList<>(args.subList(0, size));
        for (int i = 0; i < size; i++) {
            args.remove(0);
        }

        return arguments;
    }

    private List<String> sendQuery(QueryType type, List<String> arguments) throws IOException {
        responseString = "";
        String query = type.name();

        if (arguments.size() > 0) {
            query += splitter + String.join(splitter, arguments);
        }

        try {
            channelFromClient.basicPublish("", "fromClient", null, query.getBytes(StandardCharsets.UTF_8));
            System.out.println(" [Client] Sent '" + query + "'\n");

            DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                responseString = new String(delivery.getBody(), StandardCharsets.UTF_8);
            };

            while (responseString.equals("")) {
                channelToClient.basicConsume("toClient", true, deliverCallback, consumerTag -> {
                });
            }

            return Arrays.stream(responseString.split(splitter)).toList();
        } catch (Exception e) {
            System.out.println(">>     " + e.getMessage());
        }

        throw new RuntimeException("Error while sending query");
    }

    public static void main(String[] args) throws IOException, TimeoutException {
        Client client = new Client();

        for (KnifeType knifeType : client.getKnifeTypes()) {
            System.out.println(knifeType);
            System.out.printf("Count of knives: %s%n", client.countKnivesByKnifeType(knifeType));

            for (Knife knife : client.getKnivesByKnifeType(knifeType)) {
                System.out.println(knife);
                System.out.printf("Got by name: %s%n", client.getKnifeByName(knife));
            }
        }

        client.addKnifeType(new KnifeType("New KnifeType"));
        client.addKnife(new Knife("EveryBody", 2.3f, "New KnifeType"));
        client.updateKnife(new Knife("EveryBody"), new Knife("None", 2.6f, "Comedy"));
        System.out.println("\n");

        for (KnifeType knifeType : client.getKnifeTypes()) {
            System.out.println(knifeType);
            System.out.printf("Count of knives: %s%n", client.countKnivesByKnifeType(knifeType));

            for (Knife knife : client.getKnivesByKnifeType(knifeType)) {
                System.out.println(knife);
                System.out.printf("Got by name: %s%n", client.getKnifeByName(knife));
            }
        }
    }
}
