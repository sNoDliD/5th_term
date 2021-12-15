import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public class GunShopClient {
    public static void main(String[] args) throws RemoteException {
        GunShopRemote gunShop = null;
        try {
            gunShop = (GunShopRemote) Naming.lookup("//" + "localhost" + "/GunShopServer");
        } catch (NotBoundException | MalformedURLException | RemoteException e) {
            e.printStackTrace();
        }

        for(var knifeType : gunShop.getKnifeTypes()) {
            System.out.println(knifeType);

            for(var knife : gunShop.getKnivesByKnifeType(knifeType.getName())) {
                System.out.println(knife);
            }
        }

        gunShop.addKnifeType(new KnifeType("New KnifeType"));
        gunShop.addKnife(new Knife("EveryBody", 2.3f, "New KnifeType"));
        gunShop.updateKnife(new Knife("EveryBody"), new Knife("None", 2.6f, "Comedy"));

        System.out.println("\n");

        for(var knifeType : gunShop.getKnifeTypes()) {
            System.out.println(knifeType);

            for(var knife : gunShop.getKnivesByKnifeType(knifeType.getName())) {
                System.out.println(knife);
            }
        }
    }
}
