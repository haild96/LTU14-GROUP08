package Resurset;

import java.awt.*;
import java.awt.event.*;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

import javax.swing.JOptionPane;
import javax.swing.JDialog;

import javax.swing.Timer;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.MalformedURLException;

import ServerSide.ChessInterface;

public class windowChessBoard extends objChessBoard implements MouseListener, MouseMotionListener {

	private final int refreshRate = 5;

	private Image[][] imgPlayer = new Image[2][6];
	public static String[] strPlayerName = { "Player 1", "Player 2" };
	private String strStatusMsg = "";
	private objCellMatrix cellMatrix = new objCellMatrix();
	private int currentPlayer = 1, startRow = 0, startColumn = 0, pieceBeingDragged = 0;
	private int startingX = 0, startingY = 0, currentX = 0, currentY = 0, refreshCounter = 0;
	private boolean firstTime = true, hasWon = false, isDragging = false;
	private boolean isMoveSuccess = true;
	public static String localName = "";

	private objPawn pawnObject = new objPawn();
	private objRock rockObject = new objRock();
	private objKnight knightObject = new objKnight();
	private objBishop bishopObject = new objBishop();
	private objQueen queenObject = new objQueen();
	private objKing kingObject = new objKing();

	public int tempSX = 0; // mousePressed(x)
	public int tempSY = 0; // mousePressed(y)
	public int tempDesColumn = 0; // mouseReleased(desColumn)
	public int tempDesRow = 0; // mouseReleased(desRow)
	public int tempCurrentY = 0; // mouseRelease(currentY)
	public int tempCurrentX = 0; // mouseReleased(currentX)
	public String myType = "none";
	public boolean isMove = false;

	public int[] vlerat = { 0, 0, 0, 0 };
	public int[] vleratTemp = vlerat;

	Timer timer;

	public windowChessBoard() {

		this.addMouseListener(this);
		this.addMouseMotionListener(this);

	}

	private String getPlayerMsg() {
		if (hasWon) {
			if (this.localName.equals(strPlayerName[currentPlayer - 1])) {
				return "Congrats " + strPlayerName[currentPlayer - 1] + ", you are the winner!";
			} else {
				return "You are the lose!";
			}
		} else if (firstTime) {
			return "Press new game to start";
		} else {
			return "" + strPlayerName[currentPlayer - 1] + " move";
		}

	}

	private void resetBoard() {
		hasWon = false;
		currentPlayer = 1;
		strStatusMsg = getPlayerMsg();
		cellMatrix.resetMatrix();
		repaint();
	}

	public void setupImages(Image[] imgRed, Image[] imgBlue) {

		imgPlayer[0] = imgRed;
		imgPlayer[1] = imgBlue;
		resetBoard();

	}

	public void setNames(String strPlayer1Name, String strPlayer2Name) {

		strPlayerName[0] = strPlayer1Name;
		strPlayerName[1] = strPlayer2Name;
		strStatusMsg = getPlayerMsg();
		repaint();

	}

	protected void drawExtra(Graphics g) {

		for (int i = 0; i < vecPaintInstructions.size(); i++) {

			currentInstruction = (objPaintInstruction) vecPaintInstructions.elementAt(i);
			int paintStartRow = currentInstruction.getStartRow();
			int paintStartColumn = currentInstruction.getStartColumn();
			int rowCells = currentInstruction.getRowCells();
			int columnCells = currentInstruction.getColumnCells();

			for (int row = paintStartRow; row < (paintStartRow + rowCells); row++) {

				for (int column = paintStartColumn; column < (paintStartColumn + columnCells); column++) {

					int playerCell = cellMatrix.getPlayerCell(row, column);
					int pieceCell = cellMatrix.getPieceCell(row, column);

					if (playerCell != 0) {

						try {
							g.drawImage(imgPlayer[playerCell - 1][pieceCell], ((column + 1) * 50), ((row + 1) * 50),
									this);
						} catch (ArrayIndexOutOfBoundsException e) {
						}

					}

				}

			}

		}

		if (isDragging) {
			g.drawImage(imgPlayer[currentPlayer - 1][pieceBeingDragged], (currentX - 25), (currentY - 25), this);
		}

		g.setColor(new Color(0, 0, 0));
		g.drawString(strStatusMsg, 50, 475);

		vecPaintInstructions.clear(); // clear all paint instructions
	}

	public void newGame() {

		firstTime = false;
		resetBoard();

		try {
			ChessInterface stubChess = (ChessInterface) Naming
					.lookup("rmi://" + chess.IP + ":" + chess.PORT + "/Chess");

			if (!stubChess.getCheckExistWhoPlay()) {
				myType = "0";
				stubChess.tellWhoHasToPlay(myType);
				stubChess.setCheckExistWhoPlay();
			} else {
				myType = "1";
				stubChess.tellWhoHasToPlay(myType);
				startTimer();
			}
		} catch (Exception re) {
			re.printStackTrace();
		}
	}

	public void startTimer() {
		ActionListener actionListener = new ActionListener() {
			public void actionPerformed(ActionEvent actionEvent) {
				try {
					ChessInterface stubChess = (ChessInterface) Naming
							.lookup("rmi://" + chess.IP + ":" + chess.PORT + "/Chess");

					vlerat = stubChess.getMoveLocation();
					if (!myType.equals(stubChess.getTellWhoHasToPlay())) {
						moveServerPieces(vlerat[0], vlerat[1], vlerat[2], vlerat[3]);
						if (myType.equals("0") || myType.equals("1")) {
							timer.stop();
						}
					}
				} catch (Exception re) {
					re.printStackTrace();
				}
			}
		};
		timer = new Timer(500, actionListener);
		timer.start();

	}

	public void moveServerPieces(int tSX, int tSY, int tDesColumn, int tDesRow) {
		if (!hasWon && !firstTime) {
			int x = tSX;
			int y = tSY;

			if ((x > 60 && x < 430) && (y > 60 && y < 430)) // in the correct bounds
			{

				startRow = findWhichTileSelected(y);
				startColumn = findWhichTileSelected(x);

				if (cellMatrix.getPlayerCell(startRow, startColumn) == currentPlayer) {
					pieceBeingDragged = cellMatrix.getPieceCell(startRow, startColumn);
					cellMatrix.setPieceCell(startRow, startColumn, 6);
					cellMatrix.setPlayerCell(startRow, startColumn, 0);
					isDragging = true;

				} else {
					isDragging = false;
				}

			}
		}

		if (isDragging) {
			isDragging = false;

			int desRow = tDesRow;
			int desColumn = tDesColumn;
			checkMove(desRow, desColumn);
			isMove = false;
			repaint();

		}
	}

	private void checkMove(int desRow, int desColumn) {

		boolean legalMove = false;

		if (cellMatrix.getPlayerCell(desRow, desColumn) == currentPlayer) {
			strStatusMsg = "Can not move onto a piece that is yours";
		} else {

			switch (pieceBeingDragged) {

			case 0:
				legalMove = pawnObject.legalMove(startRow, startColumn, desRow, desColumn, cellMatrix.getPlayerMatrix(),
						currentPlayer);
				break;
			case 1:
				legalMove = rockObject.legalMove(startRow, startColumn, desRow, desColumn,
						cellMatrix.getPlayerMatrix());
				break;
			case 2:
				legalMove = knightObject.legalMove(startRow, startColumn, desRow, desColumn,
						cellMatrix.getPlayerMatrix());
				break;
			case 3:
				legalMove = bishopObject.legalMove(startRow, startColumn, desRow, desColumn,
						cellMatrix.getPlayerMatrix());
				break;
			case 4:
				legalMove = queenObject.legalMove(startRow, startColumn, desRow, desColumn,
						cellMatrix.getPlayerMatrix());
				break;
			case 5:
				legalMove = kingObject.legalMove(startRow, startColumn, desRow, desColumn,
						cellMatrix.getPlayerMatrix());
				break;
			}
		}

		if (legalMove) {
			isMoveSuccess = true;
			int newDesRow = 0;
			int newDesColumn = 0;
			switch (pieceBeingDragged) {
			case 0:
				newDesRow = pawnObject.getDesRow();
				newDesColumn = pawnObject.getDesColumn();
				break;
			case 1:
				newDesRow = rockObject.getDesRow();
				newDesColumn = rockObject.getDesColumn();
				break;
			case 2:
				newDesRow = knightObject.getDesRow();
				newDesColumn = knightObject.getDesColumn();
				break;
			case 3:
				newDesRow = bishopObject.getDesRow();
				newDesColumn = bishopObject.getDesColumn();
				break;
			case 4:
				newDesRow = queenObject.getDesRow();
				newDesColumn = queenObject.getDesColumn();
				break;
			case 5:
				newDesRow = kingObject.getDesRow();
				newDesColumn = kingObject.getDesColumn();
				break;
			}

			cellMatrix.setPlayerCell(newDesRow, newDesColumn, currentPlayer);

			if (!cellMatrix.checkWinner(currentPlayer) && pieceBeingDragged == 0 && (newDesRow == 0 || newDesRow == 7)) { // If pawn has got to the end row

				try {
					ChessInterface stubChess;
					stubChess = (ChessInterface) Naming.lookup("rmi://" + chess.IP + ":" + chess.PORT + "/Chess");
					String flag = stubChess.getWhoPlayChange();
					if (flag.equals("none")) {
						stubChess.setWhoPlayChange(myType);
					} else {
						stubChess.setWhoPlayChange("none");
					}

					flag = stubChess.getWhoPlayChange();
					if (flag.equals(myType)) {
						boolean canPass = false;
						int newPiece = 2;
						String strNewPiece = "Rock";
						String[] strPieces = { "Rock", "Knight", "Bishop", "Queen" };
						JOptionPane digBox = new JOptionPane("Choose the piece to change your pawn into",
								JOptionPane.QUESTION_MESSAGE, JOptionPane.OK_CANCEL_OPTION, null, strPieces, "Rock");
						JDialog dig = digBox.createDialog(null, "pawn at end of board");
						do {

							dig.setVisible(true);

							try {

								strNewPiece = digBox.getValue().toString();

								for (int i = 0; i < strPieces.length; i++) {

									if (strNewPiece.equalsIgnoreCase(strPieces[i])) {

										canPass = true;
										newPiece = i + 1;

									}

								}

							} catch (NullPointerException e) {
								canPass = false;
							}

						} while (canPass == false);
						cellMatrix.setPieceCell(newDesRow, newDesColumn, newPiece);
						stubChess.setPieceChange(newDesRow, newDesColumn, newPiece);
					} else {
						int[] pieceChange = stubChess.getPieceChange();
						cellMatrix.setPieceCell(pieceChange[0], pieceChange[1], pieceChange[2]);
					}

				} catch (MalformedURLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (RemoteException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (NotBoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

			} else {
				cellMatrix.setPieceCell(newDesRow, newDesColumn, pieceBeingDragged);
			}

			if (cellMatrix.checkWinner(currentPlayer)) {
				hasWon = true;
				strStatusMsg = getPlayerMsg();
			} else {
				if (currentPlayer == 1) {
					currentPlayer = 2;
				} else {
					currentPlayer = 1;
				}
				strStatusMsg = getPlayerMsg();
			}
		} else {
			switch (pieceBeingDragged) {
			case 0:
				strStatusMsg = pawnObject.getErrorMsg();
				break;
			case 1:
				strStatusMsg = rockObject.getErrorMsg();
				break;
			case 2:
				strStatusMsg = knightObject.getErrorMsg();
				break;
			case 3:
				strStatusMsg = bishopObject.getErrorMsg();
				break;
			case 4:
				strStatusMsg = queenObject.getErrorMsg();
				break;
			case 5:
				strStatusMsg = kingObject.getErrorMsg();
				break;
			}
			isMoveSuccess = false;
			unsucessfullDrag(desRow, desColumn);
		}
	}

	private void unsucessfullDrag(int desRow, int desColumn) {
		isMove = false;
		cellMatrix.setPieceCell(startRow, startColumn, pieceBeingDragged);
		cellMatrix.setPlayerCell(startRow, startColumn, currentPlayer);
	}

	private void updatePaintInstructions(int desRow, int desColumn) {
		currentInstruction = new objPaintInstruction(startRow, startColumn, 1);
		vecPaintInstructions.addElement(currentInstruction);

		currentInstruction = new objPaintInstruction(desRow, desColumn);
		vecPaintInstructions.addElement(currentInstruction);
	}

	public void mouseClicked(MouseEvent e) {
	}

	public void mouseEntered(MouseEvent e) {
	}

	public void mouseExited(MouseEvent e) {
		mouseReleased(e);
	}

	public void mousePressed(MouseEvent e) {
		if ((!hasWon && !firstTime) && (myType.equals("0") || myType.equals("1"))) {
			int x = e.getX();
			int y = e.getY();

			// Vlere e shtuar
			tempSX = x;
			tempSY = y;

			if ((x > 60 && x < 430) && (y > 60 && y < 430)) // in the correct bounds
			{
				startRow = findWhichTileSelected(y);
				startColumn = findWhichTileSelected(x);

				if (myType.equals("0")) {
					if (cellMatrix.getPlayerCell(startRow, startColumn) == 1 && !isMove) {

						pieceBeingDragged = cellMatrix.getPieceCell(startRow, startColumn);
						cellMatrix.setPieceCell(startRow, startColumn, 6);
						cellMatrix.setPlayerCell(startRow, startColumn, 0);
						isDragging = true;
						isMove = true;
					} else {
						/* Nese nuk e keni rendin dhe pretendoni te levizni */
						isDragging = false;
					}
				} else if (myType.equals("1")) {
					if (cellMatrix.getPlayerCell(startRow, startColumn) == 2 && !isMove) {

						pieceBeingDragged = cellMatrix.getPieceCell(startRow, startColumn);
						cellMatrix.setPieceCell(startRow, startColumn, 6);
						cellMatrix.setPlayerCell(startRow, startColumn, 0);
						isDragging = true;
						isMove = true;
					} else {
						/* Nese nuk e keni rendin dhe pretendoni te levizni */
						isDragging = false;
					}
				}

			}
		}
	}

	public void mouseReleased(MouseEvent e) {
		if (isDragging) {// Vlera e dragging eshte true
			isDragging = false;

			int desRow = findWhichTileSelected(currentY);
			int desColumn = findWhichTileSelected(currentX);

			// Vlere e shtuar
			tempDesColumn = desColumn;
			tempDesRow = desRow;
			tempCurrentY = currentY;
			tempCurrentX = currentX;
			if (myType.equals("0") || myType.equals("1")) {
				checkMove(desRow, desColumn);
				repaint();
		
				if (isMoveSuccess) {
					try {
						ChessInterface stubChess = (ChessInterface) Naming
								.lookup("rmi://" + chess.IP + ":" + chess.PORT + "/Chess");

						stubChess.sendMoveAndReciveServer(tempSX, tempSY, tempDesColumn, tempDesRow);
						stubChess.tellWhoHasToPlay(myType);
						String[] names = stubChess.getNamePlayers();
						this.setNames(names[0], names[1]);
					} catch (Exception re) {
					}
					startTimer();
				}
			}
		}
	}

	public void mouseDragged(MouseEvent e) {
		if (isDragging) {
			int x = e.getX();
			int y = e.getY();

			if ((x > 60 && x < 430) && (y > 60 && y < 430)) {
				if (refreshCounter >= refreshRate) {
					currentX = x;
					currentY = y;
					refreshCounter = 0;
					int desRow = findWhichTileSelected(currentY);
					int desColumn = findWhichTileSelected(currentX);

					updatePaintInstructions(desRow, desColumn);
					repaint();
				} else {
					refreshCounter++;
				}
			}
		}
	}

	public void mouseMoved(MouseEvent e) {
	}

	public void gotFocus() {
		repaint();
	}

}
