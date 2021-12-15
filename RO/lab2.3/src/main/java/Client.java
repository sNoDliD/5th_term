import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

public class Client {
    private Socket socket;
    private DataOutputStream out;
    private DataInputStream in;

    public Client(String ip, int port) throws IOException {
        socket = null;

        while (true) {
            try {
                System.out.println("Connecting to server...");
                socket = new Socket(ip, port);
                System.out.println("Connected");
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
        }

        in = new DataInputStream(socket.getInputStream());
        out = new DataOutputStream(socket.getOutputStream());
    }

    public boolean addKnifeType(KnifeType knifeType) throws IOException {
        return sendQuery(QueryType.ADD_KNIFE_TYPE, List.of(knifeType.getName()));
    }

    public boolean deleteKnifeType(KnifeType knifeType) throws IOException {
        return sendQuery(QueryType.DELETE_KNIFE_TYPE, List.of(knifeType.getName()));
    }

    public boolean addKnife(Knife knife) throws IOException {
        return sendQuery(QueryType.ADD_KNIFE, knife.toList());
    }

    public boolean deleteKnife(Knife knife) throws IOException {
        return sendQuery(QueryType.ADD_KNIFE, knife.toList());
    }

    public boolean updateKnife(Knife oldKnife, Knife newKnife) throws IOException {
        List<String> arguments = oldKnife.toList();
        arguments.addAll(newKnife.toList());

        return sendQuery(QueryType.UPDATE_KNIFE, arguments);
    }

    public int countKnivesByKnifeType(KnifeType knifeType) throws IOException {
        if (sendQuery(QueryType.COUNT_KNIVES_BY_KNIFE_TYPE, List.of(knifeType.getName()))) {
            return in.readInt();
        }

        throw new RuntimeException("Error while counting knifeTypes");
    }

    public Knife getKnifeByName(Knife knife) throws IOException {
        if (sendQuery(QueryType.GET_KNIFE_BY_NAME, knife.toList())) {
            Knife resultKnife = Knife.parseKnife(in);
            return resultKnife;
        }

        throw new RuntimeException("Error while getting knife");
    }

    public List<Knife> getKnivesByKnifeType(KnifeType knifeType) throws IOException {
        if (sendQuery(QueryType.GET_KNIVES_BY_KNIFE_TYPE, knifeType.toList())) {
            int count = in.readInt();

            List<Knife> knives = new ArrayList<>();

            for (int i = 0; i < count; i++) {
                knives.add(Knife.parseKnife(in));
            }

            return knives;
        }

        throw new RuntimeException("Error while getting knives by knifeType");
    }

    public List<KnifeType> getKnifeTypes() throws IOException {
        if (sendQuery(QueryType.GET_KNIFE_TYPES, List.of())) {

            int count = in.readInt();

            List<KnifeType> knifeTypes = new ArrayList<>();

            for (int i = 0; i < count; i++) {
                knifeTypes.add(KnifeType.parseKnifeType(in));
            }

            return knifeTypes;
        }

        throw new RuntimeException("Error while getting knifeTypes");
    }

    public void disconnect() throws IOException {
        System.out.println("Disconnected from server");
        socket.close();
    }

    private boolean sendQuery(QueryType type, List<String> arguments) throws IOException {
        out.writeUTF(type.name());

        for (String argument : arguments) {
            out.writeUTF(argument);
        }

        return in.readInt() == 0;
    }

    public static void main(String[] args) throws IOException {
        Client client = null;
        try {
            client = new Client("localhost", 2710);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Scanner sc = new Scanner(System.in);
        String knifeTypeName;
        String knifeName;
        float length;



        while (true) {
            for (KnifeType knifeType : client.getKnifeTypes()) {
                System.out.println(knifeType);

                for(Knife knife : client.getKnivesByKnifeType(knifeType)) {
                    System.out.println(knife);
                }
            }
            System.out.print("Enter a knifeType: ");
            knifeTypeName = sc.nextLine();

            if (Objects.equals(knifeTypeName, "stop")) {
                break;
            }

            client.addKnifeType(new KnifeType(knifeTypeName));

            for (KnifeType knifeType : client.getKnifeTypes()) {
                System.out.println(knifeType);
            }

            System.out.print("Enter a knife name: ");
            knifeName = sc.nextLine();

            if (knifeName == "stop")
                break;

            System.out.print("Enter a knife length: ");
            length = Float.parseFloat(sc.nextLine());
            System.out.print("Enter a knife knifeType: ");
            knifeTypeName = sc.nextLine();

            client.addKnife(new Knife(knifeName, length, knifeTypeName));
        }

        try {
            client.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
