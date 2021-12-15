import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server {
    private GunShopDAO gunShop;

    private ServerSocket server;
    private Socket clientSocket;
    private DataOutputStream out = null;
    private DataInputStream in = null;

    public Server(int port, GunShopDAO gunShop) {
        this.server = null;
        this.out = null;
        this.in = null;
        this.gunShop = gunShop;

        try {
            server = new ServerSocket(port);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void start() throws IOException {
        while (true) {
            acceptClient();

            in = new DataInputStream(clientSocket.getInputStream());
            out = new DataOutputStream(clientSocket.getOutputStream());

            while (processQuery()) ;
        }
    }

    private boolean processQuery() throws IOException {
        try {
            QueryType type = QueryType.valueOf(in.readUTF());

            switch (type) {
                case ADD_KNIFE_TYPE -> gunShop.addKnifeType(new KnifeType(in.readUTF()));
                case DELETE_KNIFE_TYPE -> gunShop.deleteKnifeType(in.readUTF());
                case ADD_KNIFE -> addKnife();
                case DELETE_KNIFE -> deleteKnife();
                case UPDATE_KNIFE -> updateKnife();
                case COUNT_KNIVES_BY_KNIFE_TYPE -> countKnivesByKnifeType();
                case GET_KNIFE_BY_NAME -> getKnifeByName();
                case GET_KNIVES_BY_KNIFE_TYPE -> getKnivesByKnifeType();
                case GET_KNIFE_TYPES -> getKnifeTypes();
                default -> throw new IllegalStateException("Unexpected value: " + type);
            }

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            out.writeInt(-1);
            return false;
        }
    }

    private void getKnifeTypes() throws IOException {
        List<KnifeType> knifeTypes = gunShop.getKnifeTypes();

        out.writeInt(0);
        out.writeInt(knifeTypes.size());

        for (KnifeType knifeType : knifeTypes) {
            for (String argument : knifeType.toList())
                out.writeUTF(argument);
        }
    }

    private void getKnivesByKnifeType() throws IOException {
        List<Knife> knives = gunShop.getKnivesByKnifeType(KnifeType.parseKnifeType(in).getName());

        out.writeInt(0);
        out.writeInt(knives.size());

        for (Knife knife : knives) {
            for (String argument : knife.toList())
                out.writeUTF(argument);
        }
    }

    private void getKnifeByName() throws IOException {
        List<String> list = new ArrayList<>();

        out.writeInt(0);

        for (int i = 0, n = Knife.listSize(); i < n; i++)
            list.add(in.readUTF());

        gunShop.getKnifeByName(new Knife(list).getName());
    }

    private void countKnivesByKnifeType() throws IOException {
        out.writeInt(0);
        out.writeInt(gunShop.getNumberOfKnivesByKnifeType(KnifeType.parseKnifeType(in).getName()));
    }

    private void updateKnife() throws Exception {
        out.writeInt(0);
        gunShop.updateKnife(Knife.parseKnife(in), Knife.parseKnife(in));
    }

    private void deleteKnife() throws IOException {
        out.writeInt(0);
        gunShop.deleteKnife(Knife.parseKnife(in).getName());
    }

    private void addKnife() throws IOException {
        out.writeInt(0);
        gunShop.addKnife(Knife.parseKnife(in));
    }

    private void acceptClient() {
        clientSocket = null;
        int attempts = 5;

        while (attempts > 0) {
            try {
                System.out.println("Waiting for a client...");
                clientSocket = server.accept();
                System.out.println("Client connected");
                break;
            } catch (IOException e) {
                int timeout = 5000;
                System.out.printf("Failed. Waiting %d ms...%n", timeout);
                try {
                    Thread.sleep(timeout);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                    Thread.currentThread().interrupt();
                }
            }
            --attempts;
        }
    }

    public static void main(String[] args) throws Exception {
        GunShopDAO gunShop = new GunShopDAO();
        Server server = new Server(2710, gunShop);

        server.start();

        gunShop.stop();
    }
}
