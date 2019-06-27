/*
 *  IoKog
 *  target3
 */
import java.rmi.*;
import java.rmi.RemoteException;
import java.util.List;

public interface DataInterf extends Remote{
    public  List<Data> showdata(Data d1)throws RemoteException;
    public String IDfinder(String id, String pass) throws RemoteException;
}
