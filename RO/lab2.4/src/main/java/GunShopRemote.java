import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface GunShopRemote extends Remote {
    void addKnifeType(KnifeType knifeType) throws RemoteException;

    void deleteKnifeType(String name) throws RemoteException;

    void addKnife(Knife knife) throws RemoteException;

    void deleteKnife(String name) throws RemoteException;

    void updateKnife(Knife old, Knife other) throws RemoteException;

    int countKnivesByKnifeType(String name) throws RemoteException;

    Knife getKnifeByName(String name) throws RemoteException;

    List<Knife> getKnivesByKnifeType(String name) throws RemoteException;

    List<KnifeType> getKnifeTypes() throws RemoteException;
}

