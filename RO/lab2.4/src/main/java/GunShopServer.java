import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

public class GunShopServer extends UnicastRemoteObject implements GunShopRemote {
    private final GunShopDAO gunShop;

    protected GunShopServer(GunShopDAO gunShop) throws Exception {
        this.gunShop = gunShop;
    }

    @Override
    public void addKnifeType(KnifeType knifeType) throws RemoteException {
        synchronized (gunShop){
            gunShop.addKnifeType(knifeType);
        }
    }

    @Override
    public void deleteKnifeType(String knifeType) throws RemoteException {
        synchronized (gunShop){
            gunShop.deleteKnifeType(knifeType);
        }
    }

    @Override
    public void addKnife(Knife knife) throws RemoteException {
        synchronized (gunShop){
            gunShop.addKnife(knife);
        }
    }

    @Override
    public void deleteKnife(String name) throws RemoteException {
        synchronized (gunShop){
            gunShop.deleteKnife(name);
        }
    }

    @Override
    public void updateKnife(Knife old, Knife other) throws RemoteException {
        synchronized (gunShop){
            try{
                gunShop.updateKnife(old, other);
            }
            catch (Exception exception){;}
        }
    }

    @Override
    public int countKnivesByKnifeType(String name) throws RemoteException {
        synchronized (gunShop){
            return gunShop.getNumberOfKnivesByKnifeType(name);
        }
    }

    @Override
    public Knife getKnifeByName(String name) throws RemoteException {
        synchronized (gunShop){
            return gunShop.getKnifeByName(name);
        }
    }

    @Override
    public List<Knife> getKnivesByKnifeType(String name) throws RemoteException {
        synchronized (gunShop){
            return gunShop.getKnivesByKnifeType(name);
        }
    }

    @Override
    public List<KnifeType> getKnifeTypes() throws RemoteException {
        synchronized (gunShop){
            return gunShop.getKnifeTypes();
        }
    }

    public static void main(String[] args) throws Exception {
        try {
            Registry reg = LocateRegistry.createRegistry(1099);

            reg.rebind("GunShopServer", new GunShopServer(new GunShopDAO()));
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}
