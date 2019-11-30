package Resurset;

public class objPawn extends objChessPieces {

	public void objPawn() {
	}

	public boolean legalMove(int startRow, int startColumn, int desRow, int desColumn, int[][] playerMatrix,
			int currentPlayer) {

		boolean legalMove = true;
		int[] playerPawnStart = { 6, 1 };

		if ((currentPlayer == 1 && desRow >= startRow) || (currentPlayer == 2 && desRow <= startRow)) {

			strErrorMsg = "Can not move in that direction";
			legalMove = false;

		} else if (desColumn != startColumn) { // If moving sideways

			if ((desColumn > startColumn && desColumn == (startColumn + 1))
					|| (desColumn < startColumn && desColumn == (startColumn - 1))) // If only moving one place side

			{

				if ((desRow == (startRow + 1) && currentPlayer == 2)
						|| (desRow == (startRow - 1) && currentPlayer == 1)) {

					if (playerMatrix[desRow][desColumn] == 0) { // If cell is empty

						strErrorMsg = "Can only move diagonal when taking an enemy piece";
						legalMove = false;

					}

				} else {

					strErrorMsg = "Can not move that far";
					legalMove = false;

				}

			} else {

				strErrorMsg = "Can not move that far";
				legalMove = false;

			}

		} else if ((currentPlayer == 1 && desRow < (startRow - 1)) || (currentPlayer == 2 && desRow > (startRow + 1))) {

			if ((currentPlayer == 1 && desRow == (startRow - 2)) || (currentPlayer == 2 && desRow == (startRow + 2))) {

				if (playerPawnStart[currentPlayer - 1] != startRow) { // If not at pawn starting place

					strErrorMsg = "Can not move that far";
					legalMove = false;

				}

			} else {

				strErrorMsg = "Can not move that far";
				legalMove = false;

			}

		}

		if (legalMove) {

			finalDesRow = desRow;
			finalDesColumn = desColumn;

		}

		return legalMove;

	}

}