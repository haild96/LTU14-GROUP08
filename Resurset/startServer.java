package Resurset;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

import javax.swing.JFrame;

import ServerSide.ChessImpl;

public class startServer {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		 startSrv();
		 startClient();
	}
	
	public static void startSrv(){
		try {
    		Integer convertPort = Integer.valueOf(chess.PORT);
			LocateRegistry.createRegistry(convertPort);
			ChessImpl impl= new ChessImpl();
			Naming.rebind("rmi://"+chess.IP+":"+chess.PORT+"/Chess", impl);
		} catch (RemoteException e1) {
			e1.printStackTrace();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		System.out.println("Started Server");
	}
	
	public static void startClient() {
		try {
			JFrame.setDefaultLookAndFeelDecorated(true); //Make it look nice
	        JFrame frame = new JFrame("Chess Game"); //Title
	        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);	        
	        chessGUI chessWindow = new chessGUI();
	        frame.setContentPane(chessWindow.createGUI(frame));

	        frame.setSize(550,650);
	        frame.setResizable(false);
	        frame.setVisible(true);  
	        frame.pack();
		} catch (Exception re) {
			System.out.println("Connect Fail");
		}
	}
}
