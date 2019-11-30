package ServerSide;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ChessInterface extends Remote{
	public void sendMoveAndReciveServer(int tempSX,int tempSY,int a, int b) throws RemoteException;
	
	public int[] getMoveLocation() throws RemoteException;
	public void tellWhoHasToPlay(String s) throws RemoteException;
	public String getTellWhoHasToPlay() throws RemoteException;
	public boolean getCheckExistWhoPlay() throws RemoteException;
	public void setCheckExistWhoPlay() throws RemoteException;

	public void setWhoPlayChange(String s) throws RemoteException;
    public String getWhoPlayChange() throws RemoteException;
    public int[] getPieceChange() throws RemoteException;
    public void setPieceChange(int row, int col, int value) throws RemoteException;
    public String[] getNamePlayers() throws RemoteException;
    public void setNamePlayer(String name, int index)  throws RemoteException;
}
