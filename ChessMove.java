public class ChessMove{
	ChessLocation from;
	ChessLocation to;
	ChessMove previousMove=null;
	ChessMove nextMove=null;
	Chess.ChessPiece pieceEaten=null;
	public ChessMove(ChessLocation f, ChessLocation t){
		from=f;
		to=t;
	}
	
	public String toString(){
		return from+" to "+to;
	}

}