
class ChessAdvancedLocation{
	int x=0;
	int y=0;
	int stepNumber=0;
	int promotedNumber=-1;
	// int previousX=0;
	// int previousY=0;
	
	ChessLocation previousLocation=null;
	Chess.ChessPiece pieceKilled=null;
	Chess.ChessPiece locatedBy=null;
	public ChessAdvancedLocation(int sn, int xLoc, int yLoc, Chess.ChessPiece toBeLocatedBy, Chess.ChessPiece pKilled){
		stepNumber=sn;
		x=xLoc;
		y=yLoc;
		pieceKilled=pKilled;
		locatedBy=toBeLocatedBy;
		// promotedNumber=promotedValue;
		
	}
	
	public String toString(){
		return "("+x+","+y+")";
	}
	
}