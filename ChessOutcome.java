class ChessOutcome{
	ChessMove chessMove=null;
	ChessMove nextMove=null;
	ChessMove previousMove=null;
	
	java.util.ArrayList<ChessOutcome> nextOutcomes=null;
	int winningCount=0;
	int drawingCount=0;
	int unfinishedCount=0;
	int losingCount=0;
	int averageMilitaryPoints=0;
	double score=0;
	ChessMove moveHistory=null;
	
	boolean hasBeenTerminated=false;
	public ChessOutcome(ChessMove cm, int wc, int dc, int uf, int lc, int mp,ChessMove mh){
		chessMove=cm;
		winningCount=wc;
		drawingCount=dc;
		losingCount=lc;
		averageMilitaryPoints=mp;
		moveHistory=mh;
	}
}