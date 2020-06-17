
class ChessLocation{
	int x=0;
	int y=0;
	ChessLocation previousLocation=null;
	ChessLocation nextLocation=null;
	int promotedNumber=-1;
	// Chess.ChessPiece locatedBy=null;
	public ChessLocation(int xLoc, int yLoc){
		x=xLoc;
		y=yLoc;
		// promotedNumber=promotedValue;
		
	}
	
	public String toString(){
		if(promotedNumber>=0){
		
			return "("+x+","+y+")"+promotedNumber;
		}else{
			return "("+x+","+y+")";
		}
	}
	
}