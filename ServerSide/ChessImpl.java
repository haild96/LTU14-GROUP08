package ServerSide;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class ChessImpl extends UnicastRemoteObject implements ChessInterface{
	
	public int[] n = new int[4];
	public String myType = "none"; //ne fillim asgje
	public boolean isExistPlayer = false;
	public String userChange = "none";

	public int[] pieceChange = new int [3];
	
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

	public boolean getCheckExistWhoPlay() throws RemoteException {
		return isExistPlayer;
	}

	public void setCheckExistWhoPlay() throws RemoteException {
		isExistPlayer = !isExistPlayer;
	}

	public void setWhoPlayChange(String s) throws RemoteException {
    		userChange = s;
    	}

    	public String getWhoPlayChange() throws RemoteException {
    		return userChange;
    	}

    	public void setPieceChange(int row, int col, int value) throws RemoteException  {
    		pieceChange[0] = row;
    		pieceChange[1] = col;
    		pieceChange[2] = value;
    	}

    	public int [] getPieceChange() throws RemoteException {
    	    return pieceChange;
    	}
}