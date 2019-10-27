package ServerSide;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class ChessImpl extends UnicastRemoteObject implements ChessInterface{
	
	public int[] n = new int[4];
	public String myType = "none"; //ne fillim asgje
	public ChessImpl() throws RemoteException {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public void sendMoveAndReciveServer(int mySX,int mySY,int myDesColumn, int myDesRow) 
												throws RemoteException{
		n[0] = mySX;
		n[1] = mySY;
		n[2] = myDesColumn;
		n[3] = myDesRow;
 	}
	
	//Test
	public int[] getMoveLocation() throws RemoteException{
		return n;
	}
	
	
	
	public void tellWhoHasToPlay(String s) throws RemoteException{
		myType = s;
	}
	
	public String getTellWhoHasToPlay() throws RemoteException{
		return myType;
	}

}