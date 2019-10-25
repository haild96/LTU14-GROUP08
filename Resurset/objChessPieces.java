package Resurset;
public class objChessPieces
{
	
	protected int finalDesRow = 0;
	protected int finalDesColumn = 0;
	protected String strErrorMsg = "";
	
	public objChessPieces ()
	{
	}
	
	private boolean checkAxisMove (int newRow, int newColumn, int[][] playerMatrix)
	{
		
		if (playerMatrix[newRow][newColumn] != 0) //If not empty
		{

			strErrorMsg = "A piece is blocking the route"; //Error message
			return false;
			
		}
		
		return true;
		
	}
	
}