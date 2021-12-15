import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeoutException;

public class Server {
    private static final String splitter = "#";
    private final GunShopDAO gunShop;
    private Connection connection;
    private ServerSocket server;
    private Socket clientSocket;
    private List<String> currentArguments;
    private String response;

    public Server(GunShopDAO gunShop) {
        this.gunShop = gunShop;
    }

    public void start() throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        connection = factory.newConnection();

        Channel channelFromClient = connection.createChannel();
        Channel channelToClient = connection.createChannel();
        channelFromClient.queueDeclare("fromClient", false, false, false, null);
        channelToClient.queueDeclare("toClient", false, false, false, null);

        System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String query = new String(delivery.getBody(), StandardCharsets.UTF_8);

            System.out.println(" [Server] Received '" + query + "'\n");
            String response = processQuery(query);

            channelToClient.basicPublish("", "toClient", null, response.getBytes(StandardCharsets.UTF_8));
        };
        channelFromClient.basicConsume("fromClient", true, deliverCallback, consumerTag -> {
        });

    }

    private String processQuery(String query) throws IOException {
        response = "Nothing";
        try {
            currentArguments = new ArrayList<>(Arrays.stream(query.split(splitter)).toList());

            QueryType type = QueryType.valueOf(getFirstElement());

            switch (type) {
                case ADD_KNIFE_TYPE -> gunShop.addKnifeType(new KnifeType(getFirstElement()));
                case DELETE_KNIFE_TYPE -> gunShop.deleteKnifeType(getFirstElement());
                case ADD_KNIFE -> addKnife();
                case UPDATE_KNIFE -> updateKnife();
                case DELETE_KNIFE -> deleteKnife();
                case COUNT_KNIVES_BY_KNIFE_TYPE -> countKnivesByKnifeType(response);
                case GET_KNIFE_BY_NAME -> getKnifeByName(response);
                case GET_KNIVES_BY_KNIFE_TYPE -> getKnivesByKnifeType(response);
                case GET_KNIFE_TYPES -> getKnifeTypes(response);
                default -> throw new IllegalStateException("Unexpected value: " + type);
            }

            return response;
        } catch (Exception e) {
            e.printStackTrace();
            return Integer.toString(1);
        }
    }

    private String getFirstElement() {
        String element = currentArguments.get(0);
        currentArguments.remove(0);

        return element;
    }

    private List<String> getAllElements(int size) {
        List<String> list = new ArrayList<>(currentArguments.subList(0, size));
        for (int i = 0; i < size; i++) {
            currentArguments.remove(0);
        }

        return list;
    }

    private void addToResponse(String argument) {
        response += splitter + argument;
    }

    private void getKnifeTypes(String response) throws IOException {
        List<KnifeType> knifeTypes = gunShop.getKnifeTypes();

        addToResponse(Integer.toString(knifeTypes.size()));

        for (KnifeType knifeType : knifeTypes) {
            for (String argument : knifeType.toList())
                addToResponse(argument);
        }
    }

    private void getKnivesByKnifeType(String response) throws IOException {
        List<Knife> knives = gunShop.getKnivesByKnifeType(new KnifeType(getAllElements(KnifeType.listSize())).getName());

        addToResponse(Integer.toString(knives.size()));

        for (Knife knife : knives) {
            for (String argument : knife.toList())
                addToResponse(argument);
        }
    }

    private void getKnifeByName(String response) throws IOException {
        List<String> knifeList = gunShop.getKnifeByName(new Knife(getAllElements(Knife.listSize())).getName()).toList();

        for (int i = 0, n = Knife.listSize(); i < n; i++) {
            addToResponse(knifeList.get(i));
        }
    }

    private void countKnivesByKnifeType(String response) throws IOException {
        addToResponse(Integer.toString(gunShop.getNumberOfKnivesByKnifeType(new KnifeType(currentArguments.subList(0,
                KnifeType.listSize())).getName())));
    }

    private void updateKnife() {
        try{
            gunShop.updateKnife(new Knife(getAllElements(Knife.listSize())),
                    new Knife(getAllElements(Knife.listSize())));
        }
        catch (Exception exception){};
    }

    private void deleteKnife() {
        gunShop.deleteKnife(new Knife(getAllElements(Knife.listSize())).getName());
    }

    private void addKnife() {
        gunShop.addKnife(new Knife(getAllElements(Knife.listSize())));
    }

    public static void main(String[] args) throws Exception {
        Server server = new Server(new GunShopDAO());
        server.start();
    }
}
