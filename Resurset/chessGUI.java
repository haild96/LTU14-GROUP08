package Resurset;

import java.awt.*;
import java.awt.event.*;
import java.rmi.Naming;

import javax.swing.*;
import javax.swing.JOptionPane;

import ServerSide.ChessInterface;

public class chessGUI implements ActionListener, KeyListener, WindowFocusListener {

	private windowChessBoard mainChessBoard;
	private objCreateAppletImage createImage;
	private JButton cmdNewGame, cmdSetNames;
	private JTextField txtPlayerOne, txtPlayerTwo;
	private JLabel lblPlayerOne, lblPlayerTwo;
	private String[] strRedPieces = { "bPawn.gif", "bRook.gif", "bKnight.gif", "bBishop.gif", "bQueen.gif",
			"bKing.gif" };
	private String[] strBluePieces = { "pawn.gif", "Rook.gif", "Knight.gif", "Bishop.gif", "Queen.gif", "King.gif" };
	private Color clrBlue = new Color(184, 184, 184);
	private MediaTracker mt;

	public void chessGUI() {
	}

	public Container createGUI(JFrame mainApp) {

		JPanel panRoot = new JPanel(new BorderLayout());
		panRoot.setOpaque(true);
		panRoot.setPreferredSize(new Dimension(550, 600));

		mainChessBoard = new windowChessBoard();
		createImage = new objCreateAppletImage();

		mainChessBoard.setSize(new Dimension(500, 500));

		cmdNewGame = new JButton("New Game");
		cmdSetNames = new JButton("Set Names");

		cmdNewGame.addActionListener(this);
		cmdSetNames.addActionListener(this);

		txtPlayerOne = new JTextField("Player 1", 10);
		txtPlayerTwo = new JTextField("Player 2", 10);

		txtPlayerOne.addKeyListener(this);
		txtPlayerTwo.addKeyListener(this);

		lblPlayerOne = new JLabel("    ", JLabel.RIGHT);
		lblPlayerTwo = new JLabel("    ", JLabel.RIGHT);

		try {

			Image[] imgRed = new Image[6];
			Image[] imgBlue = new Image[6];
			mt = new MediaTracker(mainApp);

			for (int i = 0; i < 6; i++) {
				imgRed[i] = createImage.getImage(this, "images/" + strRedPieces[i], 5000);
				imgBlue[i] = createImage.getImage(this, "images/" + strBluePieces[i], 5000);
				mt.addImage(imgRed[i], 0);
				mt.addImage(imgBlue[i], 0);

			}

			try {
				mt.waitForID(0);
			} catch (InterruptedException e) {
			}

			mainChessBoard.setupImages(imgRed, imgBlue);

		} catch (NullPointerException e) {

			JOptionPane.showMessageDialog(null,
					"Unable to load images. There should be a folder called images with all the chess pieces in it. Try downloading this programme again",
					"Unable to load images", JOptionPane.WARNING_MESSAGE);
			cmdNewGame.setEnabled(false);
			cmdSetNames.setEnabled(false);

		}

		JPanel panBottomHalf = new JPanel(new BorderLayout());
		JPanel panNameArea = new JPanel(new GridLayout(3, 1, 2, 2));
		JPanel panPlayerOne = new JPanel();
		JPanel panPlayerTwo = new JPanel();
		JPanel panNameButton = new JPanel();
		JPanel panNewGame = new JPanel();

		panRoot.add(mainChessBoard, BorderLayout.NORTH);
		panRoot.add(panNewGame, BorderLayout.CENTER);
		panNewGame.add(cmdNewGame);

//		panBottomHalf.add(panNameArea, BorderLayout.WEST);
//		panNameArea.add(panPlayerOne);
//		panPlayerOne.add(lblPlayerOne);
//		panPlayerOne.add(txtPlayerOne);
//		panNameArea.add(panPlayerTwo);
//		panPlayerTwo.add(lblPlayerTwo);
//		panPlayerTwo.add(txtPlayerTwo);
//		panNameArea.add(panNameButton);
//		panNameButton.add(cmdSetNames);
//		panBottomHalf.add(panNewGame, BorderLayout.SOUTH);
//		panNewGame.add(cmdNewGame);

		panRoot.setBackground(clrBlue);
		panBottomHalf.setBackground(clrBlue);
		panNameArea.setBackground(clrBlue);
		panPlayerOne.setBackground(clrBlue);
		panPlayerTwo.setBackground(clrBlue);
		panNameButton.setBackground(clrBlue);
		panNewGame.setBackground(clrBlue);

		lblPlayerOne.setBackground(new Color(0, 0, 0));
		lblPlayerTwo.setBackground(new Color(255, 255, 255));

		cmdNewGame.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

		return panRoot;

	}

	public void actionPerformed(ActionEvent e) {

		if (e.getSource() == cmdSetNames) {

			if (txtPlayerOne.getText().equals("")) {
				txtPlayerOne.setText("Player 1");
			}

			if (txtPlayerTwo.getText().equals("")) {
				txtPlayerTwo.setText("Player 2");
			}

			mainChessBoard.setNames(txtPlayerOne.getText(), txtPlayerTwo.getText());

		} else if (e.getSource() == cmdNewGame) {
			String name = JOptionPane.showInputDialog("Your name");
			if (name != null && name.length() > 0) {
				try {
					windowChessBoard.localName = name;
					ChessInterface stubChess = (ChessInterface) Naming
							.lookup("rmi://" + chess.IP + ":" + chess.PORT + "/Chess");
					String[] names = stubChess.getNamePlayers();
					if (!stubChess.getCheckExistWhoPlay() && names[0].equals("")) {
						stubChess.setNamePlayer(name, 0);
						JOptionPane.showMessageDialog(null, "You are black");
					} else {
						stubChess.setNamePlayer(name, 1);
						JOptionPane.showMessageDialog(null, "You are white");
					}
					names = stubChess.getNamePlayers();
					mainChessBoard.setNames(names[0], names[1]);
					cmdNewGame.setVisible(false);
					mainChessBoard.newGame();
				} catch (Exception re) {
					re.printStackTrace();
				}
			}
		}

	}

	public void keyTyped(KeyEvent e) {

		String strBuffer = "";
		char c = e.getKeyChar();

		if (e.getSource() == txtPlayerOne) {
			strBuffer = txtPlayerOne.getText();
		} else {
			strBuffer = txtPlayerTwo.getText();
		}

		if (strBuffer.length() > 10 && !((c == KeyEvent.VK_BACK_SPACE) || (c == KeyEvent.VK_DELETE))) {
			e.consume();
		}

	}

	public void keyPressed(KeyEvent e) {
	}

	public void keyReleased(KeyEvent e) {
	}

	public void windowGainedFocus(WindowEvent e) {
		mainChessBoard.gotFocus();
	}

	public void windowLostFocus(WindowEvent e) {
	}

}