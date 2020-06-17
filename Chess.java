import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
// import java.util.*;
import javax.imageio.ImageIO;
import java.io.*;

import java.awt.image.*;
public class Chess extends JPanel implements ActionListener, MouseListener{

	//team 1:
	final int KING=0;
	final int QUEEN=1;
	final int KNIGHT=2;
	final int ROOKIE=3;
	final int BISHOP=4;
	final int PAWN=5;
	
	//team 1:
	final int EKING=6;
	final int EQUEEN=7;
	final int EKNIGHT=8;
	final int EROOKIE=9;
	final int EBISHOP=10;
	final int EPAWN=11;
	
	int teamNumber=0;
	ChessPiece [][]board=new ChessPiece[8][8];
	// ArrayList<ChessPiece> team1;
	// ArrayList<ChessPiece> team2;
	
	ChessPiece kingLocation=null;
	ChessPiece enemyKingLocation=null;
	java.util.ArrayList<ChessLocation> choosersSteps=null;
	int stepNumber=0;
	JFrame frame=null;
	ChessMove moveHistory=null;
	ChessMove predictedMoves=null;
	java.util.ArrayList<ChessOutcome> bestList=null;
	//score: 
	// checkmate opponent king: 1
	//draw: 0
	//loss: -1.
	//collect scores from playing tree.
	//find which branch of chess moves has the most probable winning outcomes.
	//if there are more than one branch with same top probable winning outcomes, find which of those branches has most military points.
	//chess usually comprises of 40 moves
	// public static void main(String []args){
		// new Chess().start();
		// new Chess().experiment();
	
	// }
	
	int xCoordinate=-1;
	int yCoordinate=-1;
	java.util.ArrayList<ChessPiece> blackImageList=null;
	java.util.ArrayList<ChessPiece> whiteImageList=null;
	
	public static void main(String []args){
		new Chess().experiment();
	}
	public void experiment(){
		ChessMove move1=new ChessMove(new ChessLocation(4,6),new ChessLocation(4,4));
		
		ChessMove move2=new ChessMove(new ChessLocation(6,1),new ChessLocation(6,3));
		ChessMove move3=new ChessMove(new ChessLocation(2,7),new ChessLocation(6,3));
		move1.nextMove=move2;
		move2.previousMove=move1;
		move2.nextMove=move3;
		move3.previousMove=move2;
		int step=moveToCurrentStepNumber(0,whiteImageList,blackImageList,move1,board,true);
		printBoard(board);
		for(int j=0;j<blackImageList.size();j++){
			System.out.println(blackImageList.get(j).xLocation+" "+blackImageList.get(j).yLocation);
		}
		System.out.println(blackImageList.size());
		java.util.ArrayList<Integer> list=new java.util.ArrayList<Integer>();
		list.add(4);
		list.add(14);
		list.add(24);
		System.out.println(list);
		list.subList(1,list.size()).clear();
		System.out.println(list);
	}
	
	JButton hintButton=new JButton("Hint");
	JButton undobutton=new JButton("Undo");
	JButton showMoveHistoryButton=new JButton("Show move history");
	JTextField depthText=new JTextField(2);
	public Chess(){
		hintButton.addActionListener(this);
		add(hintButton);
		
		undobutton.addActionListener(this);
		add(undobutton);
		showMoveHistoryButton.addActionListener(this);
		add(showMoveHistoryButton);
		depthText.setText("5");
		depthText.addActionListener(this);
		add(depthText);
		initializeEnemyPieces();
	initializeGoodPieces();
	initializePromotion();
	// printBoard(board);
	// printBoard(board);
		setBackground(Color.WHITE);
		addMouseListener(this);
	}
	
	
	public void initializePromotion(){
		
		okButton=new JButton("OK");
		frame=A3JFrame("Choose your promotion piece:",500,500,200,300);
	}
	public void initializeEnemyPieces(){
		blackImageList=new java.util.ArrayList<ChessPiece>();
		try{
			Image image=null;
			image = ImageIO.read(new File("images/enemy_rookie.png"));
			blackImageList.add(new ChessPiece(EROOKIE,0,0,resize((BufferedImage)image,30,30)));
			image = ImageIO.read(new File("images/enemy_knight.png"));
			blackImageList.add(new ChessPiece(EKNIGHT,1,0,resize((BufferedImage)image,30,30)));
			image = ImageIO.read(new File("images/enemy_bishop.png"));
			blackImageList.add(new ChessPiece(EBISHOP,2,0,resize((BufferedImage)image,30,30)));
			image = ImageIO.read(new File("images/enemy_queen.png"));
			blackImageList.add(new ChessPiece(EQUEEN,3,0,resize((BufferedImage)image,30,30)));
			image = ImageIO.read(new File("images/enemy_king.png"));
			blackImageList.add(new ChessPiece(EKING,4,0,resize((BufferedImage)image,30,30)));
			image = ImageIO.read(new File("images/enemy_bishop.png"));
			blackImageList.add(new ChessPiece(EBISHOP,5,0,resize((BufferedImage)image,30,30)));
			image = ImageIO.read(new File("images/enemy_knight.png"));
			blackImageList.add(new ChessPiece(EKNIGHT,6,0,resize((BufferedImage)image,30,30)));
			image = ImageIO.read(new File("images/enemy_rookie.png"));
			blackImageList.add(new ChessPiece(EROOKIE,7,0,resize((BufferedImage)image,30,30)));
		
			image = ImageIO.read(new File("images/enemy_pawn.png"));
			for(int j=0;j<8;j++){
				blackImageList.add(new ChessPiece(EPAWN,j,1,resize((BufferedImage)image,30,30)));
				blackImageList.get(j).arrayIndexLocation=j;
				blackImageList.get(j+8).arrayIndexLocation=j+8;
			}
			for(int e1=0;e1<16;e1++){
				board[e1%8][e1/8]=blackImageList.get(e1);
			}
			enemyKingLocation=board[4][0];
				
		}catch(Exception e){
		}
	}
	
	public void initializeGoodPieces(){
		choosersSteps=new java.util.ArrayList<ChessLocation>();
		whiteImageList=new java.util.ArrayList<ChessPiece>();
		try{
			Image image=null;
			image = ImageIO.read(new File("images/rookie.png"));
			whiteImageList.add(new ChessPiece(ROOKIE,0,7,resize((BufferedImage)image,30,30)));
			image = ImageIO.read(new File("images/knight.png"));
			whiteImageList.add(new ChessPiece(KNIGHT,1,7,resize((BufferedImage)image,30,30)));
			image = ImageIO.read(new File("images/bishop.png"));
			whiteImageList.add(new ChessPiece(BISHOP,2,7,resize((BufferedImage)image,30,30)));
			image = ImageIO.read(new File("images/queen.png"));
			whiteImageList.add(new ChessPiece(QUEEN,3,7,resize((BufferedImage)image,30,30)));
			image = ImageIO.read(new File("images/king.png"));
			whiteImageList.add(new ChessPiece(KING,4,7,resize((BufferedImage)image,30,30)));
			image = ImageIO.read(new File("images/bishop.png"));
			whiteImageList.add(new ChessPiece(BISHOP,5,7,resize((BufferedImage)image,30,30)));
			image = ImageIO.read(new File("images/knight.png"));
			whiteImageList.add(new ChessPiece(KNIGHT,6,7,resize((BufferedImage)image,30,30)));
			image = ImageIO.read(new File("images/rookie.png"));
			whiteImageList.add(new ChessPiece(ROOKIE,7,7,resize((BufferedImage)image,30,30)));
		
			image = ImageIO.read(new File("images/pawn.png"));
			for(int j=0;j<8;j++){
				whiteImageList.add(new ChessPiece(PAWN,j,6,resize((BufferedImage)image,30,30)));
				whiteImageList.get(j).arrayIndexLocation=j;
				whiteImageList.get(j+8).arrayIndexLocation=j+8;
			}
			// for(int w1=15;w1>=0;w1--){
				// board[w1%8][6+w1/8]=whiteImageList.get(w1);
				// //////System.out.println((int)(w1%8)+" "+(int)(w1/8+6));
			// }
			for(int w1=0;w1<8;w1++){
				board[w1][6]=whiteImageList.get(8+w1);
			}
			for(int w1=0;w1<8;w1++){
				board[w1][7]=whiteImageList.get(w1);
			}
			kingLocation=board[4][7];

		}catch(Exception e){
		}
	}
	
	// public void performMove(
	public void printBoard(ChessPiece [][]b){
		for(int j=0;j<8;j++){
			for(int k=0;k<8;k++){
				if(b[k][j]!=null){
				System.out.print(b[k][j]+" ");
				}else{
					System.out.print("n ");
				}
			}
			System.out.println();
		}
	}
	
	public int chessResult(int teamNumber){
		java.util.ArrayList<ChessPiece> typicalTeam=null;
		java.util.ArrayList<ChessPiece> typicalEnemyTeam=null;
		ChessPiece kingDeterminer=null;
		ChessPiece enemyKingDeterminer=null;
		if(teamNumber<6){
			typicalTeam=whiteImageList;
			typicalEnemyTeam=blackImageList;
			kingDeterminer=kingLocation;
			enemyKingDeterminer=enemyKingLocation;
		}else{
			typicalTeam=blackImageList;
			typicalEnemyTeam=whiteImageList;
			kingDeterminer=enemyKingLocation;
			enemyKingDeterminer=kingLocation;
		}
		int numberOfSteps=0;
		int numberOfEnemySteps=0;
		for(int j=0;j<typicalTeam.size();j++){
			java.util.ArrayList<ChessLocation> moves=typicalTeam.get(j).getListOfMoves();

			numberOfSteps+=moves.size();
		}
		for(int j=0;j<typicalEnemyTeam.size();j++){
			java.util.ArrayList<ChessLocation> moves=typicalEnemyTeam.get(j).getListOfMoves();

			numberOfEnemySteps+=moves.size();
		}
		if(numberOfSteps>0&&numberOfEnemySteps>0){
			return -2;//not finished.
		}
		ChessLocation kingL=new ChessLocation(kingDeterminer.xLocation,kingDeterminer.yLocation);//,kingDeterminer,null);
		ChessLocation enemyKingL=new ChessLocation(enemyKingDeterminer.xLocation,enemyKingDeterminer.yLocation);//,enemyKingDeterminer,null);
		if(numberOfSteps==0&&willNotKillKing(kingDeterminer,kingDeterminer,kingL,board)){
			return 0;
		}else if(!willNotKillKing(kingDeterminer,kingDeterminer,kingL,board)){
			return -1;
		}
		if(numberOfEnemySteps==0&&willNotKillKing(enemyKingDeterminer,enemyKingDeterminer,enemyKingL,board)){
			return 0;
		}
		//check if there's a move that will save king from dying in the next move.
		//if there is, it is false, else it returns true;
		return 1;
	}
	
	public boolean check(int teamNumber){
		java.util.ArrayList<ChessPiece> typicalTeam=null;
		ChessPiece kingDeterminer=null;
		if(teamNumber<6){
			typicalTeam=whiteImageList;
			kingDeterminer=kingLocation;
		}else{
			typicalTeam=blackImageList;
			kingDeterminer=enemyKingLocation;
		}
		// ArrayList<ChessLocation> moves=typicalTeam.get(0).getListOfMoves();
		//check if there's a team that will kill its opposing team in the next turn. If there is, return true, else return false.
		ChessLocation kingL=new ChessLocation(kingDeterminer.xLocation,kingDeterminer.yLocation);
		if(willNotKillKing(kingDeterminer,kingDeterminer,kingL,board)){
			return false;
		}
		return true;
	}
		
	public boolean willCheckMateEnemyKing(ChessPiece enemyKing, ChessPiece beforeMoved, ChessLocation step, ChessPiece [][]afterBoard){
		boolean enemyLoss=false;
		ChessPiece pieceBeforeEaten=null;
		
		if(beforeMoved!=null){
			previousX=beforeMoved.xLocation;
			previousY=beforeMoved.yLocation;
			afterBoard[previousX][previousY]=null;
			pieceBeforeEaten=afterBoard[step.x][step.y];
			afterBoard[step.x][step.y]=beforeMoved;
			
			int movesCount=0;
			if(enemyKing.chessNumber<6){
				for(int j=0;j<whiteImageList.size();j++){
					movesCount+=whiteImageList.get(j).getListOfMoves().size();
				}
			}else{
				for(int j=0;j<blackImageList.size();j++){
					movesCount+=blackImageList.get(j).getListOfMoves().size();
				}
			}
			ChessLocation enemyLoc=new ChessLocation(enemyKing.xLocation,enemyKing.yLocation);//, enemyKing,enemyKing);
			enemyLoss=!willNotKillKing(enemyKing,enemyKing,enemyLoc,afterBoard)&&(movesCount==0);
			
			afterBoard[step.x][step.y]=pieceBeforeEaten;
			afterBoard[previousX][previousY]=beforeMoved;
		}
		return enemyLoss;
	}
	
	public int willCheckMateEnemyKing2(ChessPiece enemyKing, ChessPiece beforeMoved, java.util.ArrayList<ChessPiece> badTeam, ChessLocation step, ChessPiece [][]afterBoard){
		int finalResult=-1;
		ChessPiece pieceBeforeEaten=null;
		int pX=beforeMoved.xLocation;
		int pY=beforeMoved.yLocation;
		// afterBoard[pX][pY]=null;
		// ChessPiece [][]afterBoard=copyBoard(board);
		afterBoard[pX][pY]=null;
		// pieceBeforeEaten=afterBoard[step.x][step.y];
		pieceBeforeEaten=afterBoard[step.x][step.y];
		for(int j=0;pieceBeforeEaten!=null&&j<badTeam.size();j++){
			if(badTeam.get(j)!=null&&badTeam.get(j).xLocation==pieceBeforeEaten.xLocation&&badTeam.get(j).yLocation==pieceBeforeEaten.yLocation){
				badTeam.set(j,null);
				break;
			}
		}
		// afterBoard[step.x][step.y]=beforeMoved;
		afterBoard[step.x][step.y]=beforeMoved;
		beforeMoved.xLocation=step.x;
		beforeMoved.yLocation=step.y;
		if(beforeMoved.chessNumber==KING||beforeMoved.chessNumber==EKING){
			if(step.x-pX==-2){
				ChessPiece rookie=afterBoard[0][beforeMoved.yLocation];
				
				afterBoard[0][beforeMoved.yLocation]=null;
				rookie.xLocation=3;
				rookie.hasNeverMoved=false;
				afterBoard[3][beforeMoved.yLocation]=rookie;
		
						// epiece.yLocation=0;
					
			}
							
							
			if(step.x-pX==2){
				ChessPiece rookie=afterBoard[7][beforeMoved.yLocation];
				
				afterBoard[7][beforeMoved.yLocation]=null;
				rookie.xLocation=5;
				rookie.hasNeverMoved=false;
				afterBoard[5][beforeMoved.yLocation]=rookie;
			}
		}
		int beforePromotedNumber=beforeMoved.chessNumber;
		if(step.promotedNumber>=0){
			beforeMoved.chessNumber=step.promotedNumber;
			if(step.promotedNumber%6==1){
				beforeMoved.chessValue=9;
			}else if(step.promotedNumber%6==3){
				beforeMoved.chessValue=5;
			}else{
				beforeMoved.chessValue=3;
			}
		}
		
		// //System.out.println("Board");
		// printBoard(board);
		// //System.out.println("After Board");
		// printBoard(afterBoard);
		int moveCount=0;
		// if(enemyKing.chessNumber<6){
			// for(ChessPiece p:whiteImageList){
				// moveCount+=p.getListOfMoves2(afterBoard).size();
			// }
		// }else{
			for(ChessPiece p:badTeam){
				// moveCount+=p.getListOfMoves2(afterBoard).size();
				if(p!=null){
				moveCount+=p.getListOfMoves2(afterBoard,beforeMoved.chessNumber).size();
				}
			}
		// }
		//case 1: moveCount==0 and !willnotkillking2==false ==>> DRAW
		//case 2: moveCount==0 and !willnotkillking2==true ==>> WIN
		boolean checksEnemyKing= !willNotKillKing(enemyKing,null,null,afterBoard);
		// if(checksEnemyKing){
			// System.out.println("Checks enemy king at ("+pX+","+pY+") to "+step);
		// }
		
		// afterBoard[step.x][step.y]=pieceBeforeEaten;
		afterBoard[step.x][step.y]=pieceBeforeEaten;
		if(pieceBeforeEaten!=null){
			// badTeam.add(pieceBeforeEaten);
			badTeam.set(pieceBeforeEaten.arrayIndexLocation,pieceBeforeEaten);
		}
		// afterBoard[pX][pY]=beforeMoved;
		afterBoard[pX][pY]=beforeMoved;
		beforeMoved.xLocation=pX;
		beforeMoved.yLocation=pY;
		
		if(beforeMoved.chessNumber==KING||beforeMoved.chessNumber==EKING){
			if(step.x-pX==-2){
				ChessPiece rookie=afterBoard[3][beforeMoved.yLocation];
				
				afterBoard[3][beforeMoved.yLocation]=null;
				rookie.xLocation=0;
				rookie.hasNeverMoved=true;
				afterBoard[0][beforeMoved.yLocation]=rookie;
		
						// epiece.yLocation=0;
					
			}
							
							
			if(step.x-pX==2){
				ChessPiece rookie=afterBoard[5][beforeMoved.yLocation];
				
				afterBoard[5][beforeMoved.yLocation]=null;
				rookie.xLocation=7;
				rookie.hasNeverMoved=true;
				afterBoard[7][beforeMoved.yLocation]=rookie;
			}
		}
		if(step.promotedNumber>=0){
			beforeMoved.chessNumber=beforePromotedNumber;
			beforeMoved.chessValue=1;
		}
		if(moveCount==0&&!checksEnemyKing){
			finalResult=0;
		}
		if(moveCount==0&&checksEnemyKing){
			finalResult=1;
	
			// System.out.println("Checkmates enemy king at ("+pX+","+pY+") to "+step);
			// printBoard(afterBoard);
			// //System.out.println("Why: "+moveCount);
		
		}
		return finalResult;
	}
	
	public int moveToCurrentStepNumber(int currentStepNumber, java.util.ArrayList<ChessPiece> whiteTeam, java.util.ArrayList<ChessPiece> blackTeam, ChessMove step, ChessPiece [][]afterBoard, boolean goingForward){
		
		if(goingForward){
			
			while(step.nextMove!=null){
				//do something here.
				if(currentStepNumber%2==0){// going forward+csn%2==0
					movePiece(step,blackTeam,afterBoard,goingForward);
				}else{// going forward+csn%2==1
					movePiece(step,whiteTeam,afterBoard,goingForward);
				}
				step=step.nextMove;
				currentStepNumber++;
				//end do something here.
			}
			// currentStepNumber++;

		}else{
		// ChessMove typ=copyChessMove(step);
		// printChessSteps(typ);
			// printBoard(afterBoard);
			while(step.previousMove!=null){
				//do something here.
				if(currentStepNumber%2==1){// !going forward+csn%2==1
					movePiece(step,blackTeam,afterBoard,goingForward);
				}else{// !going forward+csn%2==0
					movePiece(step,whiteTeam,afterBoard,goingForward);
				}
				step=step.previousMove;
				currentStepNumber--;
				//end do something here.
			}
			// currentStepNumber--;
		}
		//do something here final time.
		
		
		if(currentStepNumber%2==0&&goingForward||currentStepNumber%2==1&&!goingForward){
			movePiece(step,blackTeam,afterBoard,goingForward);
		}else{
			movePiece(step,whiteTeam,afterBoard,goingForward);
		}
		if(goingForward){
			currentStepNumber++;
		}else{
			currentStepNumber--;
		}
		// if(!goingForward){
			// System.out.println(currentStepNumber+" After");
			// printBoard(afterBoard);
			// System.out.println();
		// }
		//end do something here final time.
		return currentStepNumber;
	
	}
	
	boolean hasCheckedPawnPromote=false;
	public void movePiece(ChessMove step, java.util.ArrayList<ChessPiece> enemyTeam, ChessPiece [][]afterBoard, boolean goingForward){
		ChessPiece pieceToAddOrRemove=null;
		if(goingForward){
			pieceToAddOrRemove=afterBoard[step.to.x][step.to.y];
			// step.pieceEaten=pieceToAddOrRemove;
			afterBoard[step.to.x][step.to.y]=afterBoard[step.from.x][step.from.y];
			afterBoard[step.from.x][step.from.y]=null;
			afterBoard[step.to.x][step.to.y].xLocation=step.to.x;
			afterBoard[step.to.x][step.to.y].yLocation=step.to.y;
			
			if(afterBoard[step.to.x][step.to.y].chessNumber==KING||afterBoard[step.to.x][step.to.y].chessNumber==EKING){
				if(step.from.x-step.to.x==2){
					ChessPiece rookie=afterBoard[0][step.to.y];
					
					afterBoard[0][step.to.y]=null;
					rookie.xLocation=3;
					// rookie.hasNeverMoved=false;
					afterBoard[3][step.to.y]=rookie;
			
							// epiece.yLocation=0;
						
				}
								
								
				if(step.from.x-step.to.x==-2){
					ChessPiece rookie=afterBoard[7][afterBoard[step.to.x][step.to.y].yLocation];
					
					afterBoard[7][step.to.y]=null;
					rookie.xLocation=5;
					// rookie.hasNeverMoved=false;
					afterBoard[5][step.to.y]=rookie;
				}
			}
			
			if(step.to.promotedNumber>=0&&step.to.promotedNumber<11){
				afterBoard[step.to.x][step.to.y].chessNumber=step.to.promotedNumber;
				if(step.to.promotedNumber%6==1){
					afterBoard[step.to.x][step.to.y].chessValue=9;
				}else if(step.to.promotedNumber%6==3){
					afterBoard[step.to.x][step.to.y].chessValue=5;
				}else{
					afterBoard[step.to.x][step.to.y].chessValue=3;
				}
			}
			// if(!hasCheckedPawnPromote){
				// if(afterBoard[step.to.x][step.to.y].chessValue==1&&step.to.y==0){
				// System.out.println("Problem "+step+" "+step.to.promotedNumber);
				// hasCheckedPawnPromote=true;
				// }
			// }
			
		
		}else{
			afterBoard[step.from.x][step.from.y]=afterBoard[step.to.x][step.to.y];
			pieceToAddOrRemove=step.pieceEaten;
			afterBoard[step.to.x][step.to.y]=step.pieceEaten;
			afterBoard[step.from.x][step.from.y].xLocation=step.from.x;
			afterBoard[step.from.x][step.from.y].yLocation=step.from.y;
			
			if(afterBoard[step.from.x][step.from.y].chessNumber==KING||afterBoard[step.from.x][step.from.y].chessNumber==EKING){
				if(step.from.x-step.to.x==2){
					ChessPiece rookie=afterBoard[3][step.to.y];
					
					afterBoard[3][step.from.y]=null;
					rookie.xLocation=0;
					rookie.hasNeverMoved=true;
					afterBoard[0][step.from.y]=rookie;
			
							// epiece.yLocation=0;
						
				}
								
								
				if(step.from.x-step.to.x==-2){
					ChessPiece rookie=afterBoard[5][step.from.y];
					
					afterBoard[5][step.from.y]=null;
					rookie.xLocation=7;
					// rookie.hasNeverMoved=true;
					afterBoard[7][step.from.y]=rookie;
				}
			}
			// System.out.println(step+" "+step.to.promotedNumber);
			if(step.to.promotedNumber>=0&&step.to.promotedNumber<11){
				if(afterBoard[step.from.x][step.from.y].chessNumber<6){
					afterBoard[step.from.x][step.from.y].chessNumber=5;
					
				}else{
					afterBoard[step.from.x][step.from.y].chessNumber=11;
				}
				afterBoard[step.from.x][step.from.y].chessValue=1;
			}
				
		}
		
		if(pieceToAddOrRemove!=null){
			for(int j=0;j<enemyTeam.size()&&goingForward;j++){
				if(enemyTeam.get(j)!=null&&enemyTeam.get(j).xLocation==step.to.x&&enemyTeam.get(j).yLocation==step.to.y){
					// enemyTeam.remove(j);
					enemyTeam.set(j,null);
					// System.out.println("Enemy team removed");
					// System.out.println(enemyTeam.get(j).xLocation+" "+enemyTeam.get(j).yLocation);
					break;
				}
			}
			if(!goingForward){
				// enemyTeam.add(pieceToAddOrRemove);
				// System.out.println(pieceToAddOrRemove.xLocation+" "+pieceToAddOrRemove.yLocation);
				enemyTeam.set(pieceToAddOrRemove.arrayIndexLocation,pieceToAddOrRemove);
			}
		}
	}
	public int numberOfChecks(ChessMove move, java.util.ArrayList<ChessPiece> team, java.util.ArrayList<ChessPiece> enemyTeam, ChessPiece [][]afterBoard){
		int noChecks=0;
		movePiece(move,enemyTeam,afterBoard,true);
		ChessPiece movedPiece=afterBoard[move.to.x][move.to.y];
		int teamNo=movedPiece.chessNumber;
		// java.util.ArrayList<ChessLocation> listOfLocations=
		for(int i=0;i<team.size();i++){
			java.util.ArrayList<ChessLocation> teamSteps=team.get(i).getListOfMoves2(afterBoard,teamNo);
			for(int j=0;j<teamSteps.size();j++){
				ChessPiece testEnemy=afterBoard[teamSteps.get(j).x][teamSteps.get(j).y];
				if(testEnemy!=null&&(testEnemy.chessNumber<6&&teamNo>=6||testEnemy.chessNumber>=6&&teamNo<6)&&(testEnemy.chessValue>1||testEnemy.chessValue==0)){
					noChecks++;
				}
			}
		}
		movePiece(move,enemyTeam,afterBoard,false);
		return noChecks;
	}
		// (ChessMove step, java.util.ArrayList<ChessPiece> enemyTeam, ChessPiece [][]afterBoard, boolean goingForward){
	public boolean willNotKillKing(ChessPiece king, ChessPiece beforeMoved, ChessLocation step, ChessPiece [][]afterBoard){//determines whether the step is an illegal move.
			
			//assumes that beforeMoved is in king's team.
			ChessPiece pieceBeforeEaten=null;
			int previousX=-1;
			int previousY=-1;
			if(beforeMoved!=null){
				previousX=beforeMoved.xLocation;
				previousY=beforeMoved.yLocation;
				afterBoard[previousX][previousY]=null;
				pieceBeforeEaten=afterBoard[step.x][step.y];
				afterBoard[step.x][step.y]=beforeMoved;
			}
			//check status of board after it moved, diagonally from king, horizontally and vertically from king, and 2*3spaces from king.
			// boolean doesntKillKing=true;
			int x=king.xLocation;
			int y=king.yLocation;
			if(beforeMoved!=null&&(beforeMoved.chessNumber==KING||beforeMoved.chessNumber==EKING)){
				x=step.x;
				y=step.y;
			}
			//diagonal: check for bishop and pawns, queens and enemy king.
			ChessPiece []identities=new ChessPiece[4];
			if(x-1>=0&&y-1>=0){
				identities[0]=afterBoard[x-1][y-1];
			}
			if(x-1>=0&&y+1<=7){
				identities[1]=afterBoard[x-1][y+1];
			}
			if(x+1<=7&&y-1>=0){
				identities[2]=afterBoard[x+1][y-1];
			}
			if(x+1<=7&&y+1<=7){
				identities[3]=afterBoard[x+1][y+1];
			}
			
			ChessPiece identity;
			for(int c=0;c<4;c++){
				identity=identities[c];
				
				if(identity!=null&&(king.chessNumber<6&&identity.chessNumber>=6||king.chessNumber>=6&&identity.chessNumber<6)){//if has meet up with enemy
					if(identity.chessNumber!=ROOKIE&&identity.chessNumber!=EROOKIE&&identity.chessNumber!=KNIGHT&identity.chessNumber!=EKNIGHT){//if the enemy isn't rookie or knight...
						// ////System.out.println(king.xLocation+" "+king.yLocation);
						if(identity.chessValue==identity.PAWNVALUE){
							if((identity.chessNumber<6&&y<identity.yLocation||identity.chessNumber>=6&&y>identity.yLocation)&&beforeMoved!=null){
								afterBoard[step.x][step.y]=pieceBeforeEaten;
								afterBoard[previousX][previousY]=beforeMoved;
								return false;
							}
						}else if(beforeMoved!=null){
								afterBoard[step.x][step.y]=pieceBeforeEaten;
								afterBoard[previousX][previousY]=beforeMoved;
								return false;
						}
							////System.out.println(king.xLocation+" "+king.yLocation+" "+king.chessNumber+" "+identity.chessNumber+" "+c+" "+identity);
							////System.out.println("reason here 1");
					}
					
				}
			}
			// //////System.out.println("Yikes");
			//second test.
			if(x-1>=0){
				identities[0]=afterBoard[x-1][y];
			}
			if(x+1<=7){
				identities[1]=afterBoard[x+1][y];
			}
			if(y-1>=0){
				identities[2]=afterBoard[x][y-1];
			}
			if(y+1<=7){
				identities[3]=afterBoard[x][y+1];
			}
			// //////System.out.println("Yikes");
			for(int c=0;c<4;c++){
				identity=identities[c];
				if(identity!=null&&(king.chessNumber<6&&identity.chessNumber>=6||king.chessNumber>=6&&identity.chessNumber<6)){//if has meet up with enemy
					if(identity.chessValue!=identity.BISHOPVALUE&&identity.chessValue!=identity.KNIGHTVALUE&&identity.chessValue!=identity.PAWNVALUE){//if the enemy isn't rookie or knight...
						if(beforeMoved!=null){
							afterBoard[step.x][step.y]=pieceBeforeEaten;
							afterBoard[previousX][previousY]=beforeMoved;
						}
						////System.out.println("reason here 2");
						return false;
					}
				}
			}
			
			// if(x-1>=0&&y-1>=0){
				// ChessPiece identity=board[x+1][y-1];
				// if(identity!=null&&chessNumber<6&&identity.chessNumber>=6){//if has meet up with enemy
					// if(identity.chessValue!=ROOKIEVALUE&&identity.chessValue!=KNIGHTVALUE)
						// return false;
					// }
				// }
			// }
			
			//CHECK FOR STRONG OPPONENTS
			// //////System.out.println("Yikes");
			//Check for enemy knight..
				identities=new ChessPiece[8];
				if(x-1>=0&&y-2>=0){
					identities[0]=afterBoard[x-1][y-2];
				}
				if(x-2>=0&&y-1>=0){
					identities[1]=afterBoard[x-2][y-1];
				}
				// //////System.out.println("Yikes"+x+" "+y);
				if(x+1<=7&&y-2>=0){
					identities[2]=afterBoard[x+1][y-2];
					// //////System.out.println(identities[2]);
				}
				if(x+2<=7&&y-1>=0){
					identities[3]=afterBoard[x+2][y-1];
				}
				if(x-1>=0&&y+2<=7){
					identities[4]=afterBoard[x-1][y+2];
				}
				if(x-2>=0&&y+1<=7){
					identities[5]=afterBoard[x-2][y+1];
				}
				if(x+1<=7&&y+2<=7){
				// //////System.out.println(x+" "+y);
					identities[6]=afterBoard[x+1][y+2];
				}
				if(x+2<=7&&y+1<=7){
					identities[7]=afterBoard[x+2][y+1];
				}
				for(int c=0;c<8;c++){
					identity=identities[c];
					if(identity!=null&&(king.chessNumber<6&&identity.chessNumber==8||king.chessNumber>=6&&identity.chessNumber==2)){//if has meet up with enemy
						// if(identity.chessValue!=BISHOPVALUE&&identity.chessValue!=KNIGHTVALUE&&identity.chessValue!=PAWNVALUE){//if the enemy isn't rookie or knight...
						if(beforeMoved!=null){
						afterBoard[step.x][step.y]=pieceBeforeEaten;
						afterBoard[previousX][previousY]=beforeMoved;
						}
						////System.out.println("reason here 3");
						return false;
						// }
					}
				}
				
				//check for BISHOP.
				int rx=x-1;
				int ry=y-1;
				ChessPiece ep=null;//board[rx][ry];
				while(rx>=0&&ry>=0&&ep==null){
					ep=afterBoard[rx][ry];
					rx--;
					ry--;
				}
				if(ep!=null&&(king.chessNumber<6&&(ep.chessNumber==EBISHOP||ep.chessNumber==EQUEEN)||king.chessNumber>=6&&(ep.chessNumber==BISHOP||ep.chessNumber==QUEEN))){
					if(beforeMoved!=null){
					afterBoard[step.x][step.y]=pieceBeforeEaten;
					afterBoard[previousX][previousY]=beforeMoved;
					}
					////System.out.println("reason here 4");
					return false;
				}
				
				ep=null;
				rx= x+1;
				ry= y+1;
				while(rx<8&&ry<8&&ep==null){
					ep=afterBoard[rx][ry];
					rx++;
					ry++;
					// ep=board[rx][ry];
				}
				if(ep!=null&&(king.chessNumber<6&&(ep.chessNumber==EBISHOP||ep.chessNumber==EQUEEN)||king.chessNumber>=6&&(ep.chessNumber==BISHOP||ep.chessNumber==QUEEN))){
					if(beforeMoved!=null){
					afterBoard[step.x][step.y]=pieceBeforeEaten;
					afterBoard[previousX][previousY]=beforeMoved;
					}
					////System.out.println("reason here 5");
					return false;
				}
				
				ep=null;
				rx= x-1;
				ry= y+1;
				while(rx>=0&&ry<8&&ep==null){
					ep=afterBoard[rx][ry];
					rx--;
					ry++;
					// ep=board[rx][ry];
				}
				if(ep!=null&&(king.chessNumber<6&&(ep.chessNumber==EBISHOP||ep.chessNumber==EQUEEN)||king.chessNumber>=6&&(ep.chessNumber==BISHOP||ep.chessNumber==QUEEN))){
					if(beforeMoved!=null){afterBoard[step.x][step.y]=pieceBeforeEaten;
					afterBoard[previousX][previousY]=beforeMoved;
					}
					////System.out.println("reason here 6");
					return false;
				}
				
				ep=null;
				rx= x+1;
				ry= y-1;
				while(rx<8&&ry>=0&&ep==null){
					ep=afterBoard[rx][ry];
					rx++;
					ry--;
					// ep=board[rx][ry];
				}
				if(ep!=null&&(king.chessNumber<6&&(ep.chessNumber==EBISHOP||ep.chessNumber==EQUEEN)||king.chessNumber>=6&&(ep.chessNumber==BISHOP||ep.chessNumber==QUEEN))){
					if(beforeMoved!=null){
					afterBoard[step.x][step.y]=pieceBeforeEaten;
					afterBoard[previousX][previousY]=beforeMoved;
					// //////System.out.println(ep.chessNumber);
					}
					////System.out.println("reason here 7");
					return false;
				}
			
			//horizontal and vertical: check for rookie, enemy king and queen.
			
			//check for rookie.
				rx=x-1;
				// int ry=y-1;
				ep=null;//board[rx][ry];
				while(rx>=0&&ep==null){
					ep=afterBoard[rx][y];
					rx--;
					// ry--;
				}
				if(ep!=null&&(king.chessNumber<6&&(ep.chessNumber==EROOKIE||ep.chessNumber==EQUEEN)||king.chessNumber>=6&&(ep.chessNumber==ROOKIE||ep.chessNumber==QUEEN))){
					if(beforeMoved!=null){
					afterBoard[step.x][step.y]=pieceBeforeEaten;
					afterBoard[previousX][previousY]=beforeMoved;
					}
					////System.out.println("reason here 8");
					return false;
				}
				
				ep=null;
				rx= x+1;
				// ry= y+1;
				while(rx<8&&ep==null){
					ep=afterBoard[rx][y];
					rx++;
					// ry++;
					// ep=board[rx][ry];
				}
				if(ep!=null&&(king.chessNumber<6&&(ep.chessNumber==EROOKIE||ep.chessNumber==EQUEEN)||king.chessNumber>=6&&(ep.chessNumber==ROOKIE||ep.chessNumber==QUEEN))){
					if(beforeMoved!=null){
					afterBoard[step.x][step.y]=pieceBeforeEaten;
					afterBoard[previousX][previousY]=beforeMoved;
					}
					////System.out.println("reason here 9");
					return false;
				}
				
				ep=null;
				// rx= x-1;
				ry= y-1;
				while(ry>=0&&ep==null){
					ep=afterBoard[x][ry];
					// rx--;
					ry--;
					// ep=board[rx][ry];
				}
				if(ep!=null&&(king.chessNumber<6&&(ep.chessNumber==EROOKIE||ep.chessNumber==EQUEEN)||king.chessNumber>=6&&(ep.chessNumber==ROOKIE||ep.chessNumber==QUEEN))){
					if(beforeMoved!=null){
					afterBoard[step.x][step.y]=pieceBeforeEaten;
					afterBoard[previousX][previousY]=beforeMoved;
					}
					////System.out.println("reason here 10");
					return false;
					
				}
				
				ep=null;
				// rx= x+1;
				ry= y+1;
				while(ry<8&&ep==null){
					ep=afterBoard[x][ry];
					// rx++;
					ry++;
					// ep=board[rx][ry];
				}
				if(ep!=null&&(king.chessNumber<6&&(ep.chessNumber==EROOKIE||ep.chessNumber==EQUEEN)||king.chessNumber>=6&&(ep.chessNumber==ROOKIE||ep.chessNumber==QUEEN))){
					if(beforeMoved!=null){
					afterBoard[step.x][step.y]=pieceBeforeEaten;
					afterBoard[previousX][previousY]=beforeMoved;
					}
					////System.out.println("reason here 11");
					return false;
				}
			//2*3 spaces: check for knights.
			if(beforeMoved!=null){
			afterBoard[step.x][step.y]=pieceBeforeEaten;
			afterBoard[previousX][previousY]=beforeMoved;
			}
						
			return true;
		}
		





 	// public Chess() {
		
		// initializeEnemyPieces();
		// initializeGoodPieces();
		// setBackground(Color.WHITE);
		// addMouseListener(this);
	// }
	
	private static BufferedImage resize(BufferedImage image, int width, int      height) {
        int type = image.getType() == 0? BufferedImage.TYPE_INT_ARGB :     image.getType();
      BufferedImage resizedImage = new BufferedImage(width, height, type);
      Graphics2D g = resizedImage.createGraphics();
      g.setComposite(AlphaComposite.Src);

      g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
      RenderingHints.VALUE_INTERPOLATION_BILINEAR);

      g.setRenderingHint(RenderingHints.KEY_RENDERING,
      RenderingHints.VALUE_RENDER_QUALITY);

      g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
      RenderingHints.VALUE_ANTIALIAS_ON);

      g.drawImage(image, 0, 0, width, height, null);
      g.dispose();
      return resizedImage;
    } 
	// public void actionPerformed(ActionEvent e){

		// repaint();
	// }
	int previousX=-1;
	int previousY=-1;
	int canEnemyLeftCastle=-1;
	int canEnemyRightCastle=-1;
	int canLeftCastle=-1;
	int canRightCastle=-1;
	
	public boolean isInBounds(int x, int y){
		return x>=0&&x<8&&y>=0&&y<8;
	}
	
	public void mousePressed(MouseEvent e){
		
		int x=e.getX();
		int y=e.getY();
		// if(x>=80&&x<=400&&y>=80&&y<=400){
			
			xCoordinate=(x-85)/40;
			yCoordinate=(y-85)/40;
			//System.out.print(stepNumber+": ");
			if(board[xCoordinate][yCoordinate]!=null&&(board[xCoordinate][yCoordinate].chessNumber<6&&stepNumber%2==0||board[xCoordinate][yCoordinate].chessNumber>5&&stepNumber%2==1)){
			// System.out.println("Got here");
				//////System.out.println(board[xCoordinate][yCoordinate]);
				choosersSteps=board[xCoordinate][yCoordinate].getListOfMoves();
				//System.out.println("Possible moves: "+choosersSteps);
				// if(stepNumber%2==0){
					
					// FinalScore bestMove=bestMove3(board[xCoordinate][yCoordinate].chessNumber,0,4);
					// FinalScore bestMove=MinMax(null,blackImageList, whiteImageList, kingLocation, enemyKingLocation, board, 0, 4);//GamePosition game) {

					// System.out.println("Hint put "+bestMove.cMove.from+" to "+bestMove.cMove.to);
				// }
				// if(bestMove.winningCount==1&&bestMove.drawingCount==1&&bestMove.losingCount==0){
					// //System.out.print(bestMove.chessMove+" will make you win.");
				// }
			}
			//System.out.println();
			if(choosersSteps.size()>0&&hasChoosersStep(xCoordinate,yCoordinate)){
				if(moveHistory==null){
					moveHistory=new ChessMove(new ChessLocation(previousX,previousY),new ChessLocation(xCoordinate,yCoordinate));
				}else{
					ChessMove next=new ChessMove(new ChessLocation(previousX,previousY),new ChessLocation(xCoordinate,yCoordinate));
					next.previousMove=moveHistory;
					moveHistory.nextMove=next;
					moveHistory=moveHistory.nextMove;
				}
				
				System.out.println(stepNumber+": "+moveHistory);
				if(stepNumber%2==0){
					if(board[xCoordinate][yCoordinate]!=null){
						for(int l=0;l<blackImageList.size();l++){
							if(blackImageList.get(l)!=null&&blackImageList.get(l).xLocation==xCoordinate&&blackImageList.get(l).yLocation==yCoordinate){
								moveHistory.pieceEaten=blackImageList.get(l);
								// blackImageList.remove(l);
								blackImageList.set(l,null);
								break;
							}
						}
					}
					for(int j=0;j<whiteImageList.size();j++){
						if(whiteImageList.get(j)!=null&&whiteImageList.get(j).xLocation==previousX&&whiteImageList.get(j).yLocation==previousY){
							whiteImageList.get(j).xLocation=xCoordinate;
							whiteImageList.get(j).yLocation=yCoordinate;
							
							/*if(whiteImageList.get(j).chessNumber==KING&&whiteImageList.get(j).xLocation-previousX==-2){
								ChessPiece rookie=null;
								for(ChessPiece epiece:whiteImageList){
									if(epiece.chessNumber==ROOKIE){
										board[epiece.xLocation][epiece.yLocation]=null;
										board[3][epiece.yLocation]=epiece;
										epiece.xLocation=3;
										epiece.hasNeverMoved=false;
										whiteImageList.get(j).hasNeverMoved=false;
										break;
										// epiece.yLocation=0;
									}
								}
							
							
								if(whiteImageList.get(j).chessNumber==KING&&whiteImageList.get(j).xLocation-previousX==2){
									// ChessPiece rookie=null;
									for(ChessPiece epiece:whiteImageList){
										if(epiece.chessNumber==ROOKIE){
											board[epiece.xLocation][epiece.yLocation]=null;
											board[6][epiece.yLocation]=epiece;
											epiece.xLocation=6;
											epiece.hasNeverMoved=false;
											whiteImageList.get(j).hasNeverMoved=false;
											break;
											// epiece.yLocation=0;
										}
									}
								}*/
							// if(whiteImageList.get(j).chessNumber==PAWN&&previousY==6&&previousY-yCoordinate==2){
								// whiteImageList.get(j).wentStraightForward=true;
								// if(xCoordinate>0&&board[xCoordinate-1][yCoordinate]!=null&&&board[xCoordinate-1][yCoordinate].canEnpassant){
									// blackImageList.get(j).canBeEnpassanted=true;
								// }
							// }else if(whiteImageList.get(j).wentStraightForward&&previousX==xCoordinate){
								// whiteImageList.get(j).wentStraightForward=false;
								// whiteImageList.get(j).canEnpassant=true;
							// }else{
								// if(whiteImageList.get(j).canEnpassant&&board[xCoordinate][yCoordinate]==null&&xCoordinate!=previousX){
									// board[xCoordinate][yCoordinate+1]=null;
									// for(int l=0;l<blackImageList.size();l++){
										// if(blackImageList.get(l).xLocation==xCoordinate&&blackImageList.get(l).yLocation==yCoordinate+1){
											// blackImageList.remove(l);
											// break;
										// }
									// }
								// }
								
								// whiteImageList.get(j).wentStraightForward=false;
								// whiteImageList.get(j).canEnpassant=false;
							// }
							// break;
						// }else{
						// //System.out.println("white pawn cannot enpassant ("+blackImageList.get(j).xLocation+","+blackImageList.get(j).yLocation+")");
							// whiteImageList.get(j).canEnpassant=false;
							// }
						}
					}
				}
				if(stepNumber%2==1){
					if(board[xCoordinate][yCoordinate]!=null){
						for(int l=0;l<whiteImageList.size();l++){
							if(whiteImageList.get(l)!=null&&whiteImageList.get(l).xLocation==xCoordinate&&whiteImageList.get(l).yLocation==yCoordinate){
								moveHistory.pieceEaten=whiteImageList.get(l);
								// whiteImageList.remove(l);
								whiteImageList.set(l,null);
								// System.out.println(xCoordinate+" "+yCoordinate);
								break;
							}
						}
					}
					for(int j=0;j<blackImageList.size();j++){
						if(blackImageList.get(j)!=null&&blackImageList.get(j).xLocation==previousX&&blackImageList.get(j).yLocation==previousY){
							blackImageList.get(j).xLocation=xCoordinate;
							blackImageList.get(j).yLocation=yCoordinate;
							/*if(blackImageList.get(j).chessNumber==EKING&&blackImageList.get(j).xLocation-previousX==-2){
								ChessPiece rookie=null;
								for(ChessPiece epiece:blackImageList){
									if(epiece.chessNumber==EROOKIE){
										board[epiece.xLocation][epiece.yLocation]=null;
										board[3][epiece.yLocation]=epiece;
										epiece.xLocation=3;
										epiece.hasNeverMoved=false;
										blackImageList.get(j).hasNeverMoved=false;
										// epiece.yLocation=0;
									}
								}
							}
							
							if(blackImageList.get(j).chessNumber==EKING&&blackImageList.get(j).xLocation-previousX==2){
								ChessPiece rookie=null;
								for(ChessPiece epiece:blackImageList){
									if(epiece.chessNumber==EROOKIE){
										board[epiece.xLocation][epiece.yLocation]=null;
										board[6][epiece.yLocation]=epiece;
										epiece.xLocation=6;
										epiece.hasNeverMoved=false;
										blackImageList.get(j).hasNeverMoved=false;
										break;
										// epiece.yLocation=0;
									}
								}
							}*/
							// if(blackImageList.get(j).chessNumber==EPAWN&&previousY==1&&yCoordinate-previousY==2){
								// blackImageList.get(j).wentStraightForward=true;
								// if(xCoordinate>0&&board[xCoordinate-1][yCoordinate]!=null&&&board[xCoordinate-1][yCoordinate].canEnpassant){
									// blackImageList.get(j).canBeEnpassanted=true;
								// }
							// }else if(blackImageList.get(j).wentStraightForward&&xCoordinate==previousX){
								// blackImageList.get(j).wentStraightForward=false;
								// blackImageList.get(j).canEnpassant=true;
							// }else{
								// if(blackImageList.get(j).canEnpassant&&board[xCoordinate][yCoordinate]==null&&xCoordinate!=previousX){
									// board[xCoordinate][yCoordinate-1]=null;
									// for(int l=0;l<whiteImageList.size();l++){
										// if(whiteImageList.get(l).xLocation==xCoordinate&&whiteImageList.get(l).yLocation==yCoordinate-1){
											// whiteImageList.remove(l);
											// break;
										// }
									// }
								// }
								
								// blackImageList.get(j).wentStraightForward=false;
								// blackImageList.get(j).canEnpassant=false;
							// }
							// break;
						}
					}
				}
				
					
				if((canLeftCastle<0||canRightCastle<0)&&board[previousX][previousY].chessNumber==KING||board[previousX][previousY].chessNumber==EKING&&(canEnemyLeftCastle<0||canEnemyRightCastle<0)){
					if(previousX-xCoordinate==2){
						board[0][yCoordinate].xLocation=3;
						board[3][yCoordinate]=board[0][yCoordinate];
						board[0][yCoordinate]=null;
					
					}else if(previousX-xCoordinate==-2){
						board[7][yCoordinate].xLocation=5;
						board[5][yCoordinate]=board[7][yCoordinate];
						board[7][yCoordinate]=null;
					
					}
					if(previousY==7){
						canLeftCastle=stepNumber;
						canRightCastle=stepNumber;
					}else{
						canEnemyLeftCastle=stepNumber;
						canEnemyRightCastle=stepNumber;
					}
				}
				if((canEnemyLeftCastle<0||canEnemyRightCastle<0)&&previousY==0){
					if(previousX==0){
						canEnemyLeftCastle=stepNumber;
					}else if(previousX==7){
						canEnemyRightCastle=stepNumber;
					}
				}else if((canLeftCastle<0||canRightCastle<0)&&previousY==7){
					if(previousX==0){
						canLeftCastle=stepNumber;
					}else if(previousX==7){
						canRightCastle=stepNumber;
					}
				}
				board[xCoordinate][yCoordinate]=board[previousX][previousY];
				board[previousX][previousY]=null;
				
				if(yCoordinate==0&&board[xCoordinate][yCoordinate].chessNumber==PAWN||yCoordinate==7&&board[xCoordinate][yCoordinate].chessNumber==EPAWN){
					//replace it with whatever strong enemy you want.
					teamNumber=board[xCoordinate][yCoordinate].chessNumber;
					frame.setVisible(true);
				}
				
				stepNumber++;
				choosersSteps.clear();
				// printBoard(board);
			}
							
			previousX=xCoordinate;
			previousY=yCoordinate;
		// }
		repaint();
		//////System.out.println(xCoordinate+" "+yCoordinate);
	}
	
	public String bestMove(ChessLocation from, ChessLocation to,int teamNumber,int searchDepth){
		if(teamNumber<6){
			for(int j=0;j<whiteImageList.size();j++){
				java.util.ArrayList<ChessLocation> listOfMoves=whiteImageList.get(j).getListOfMoves();
			}
		}
		return ""+from;
	}
	// public void reversePromoting(){
		
	public void startPromoting(){
		int newChessValue=-1;
		Image promotedImage=null;
		String goodOrBad="";
		// if(promotionChoice>=0){
		try{
			if(promotionChoice>=6){
				goodOrBad="enemy_";
			}
			if(promotionChoice==QUEEN||promotionChoice==EQUEEN){
				newChessValue=9;//ChessPiece.QUEENVALUE;
				promotedImage=ImageIO.read(new File("images/"+goodOrBad+"queen.png"));
			}
			if(promotionChoice==ROOKIE||promotionChoice==EROOKIE){
				newChessValue=5;//ChessPiece.ROOKIEVALUE;
				promotedImage=ImageIO.read(new File("images/"+goodOrBad+"rookie.png"));
			}
			if(promotionChoice==KNIGHT||promotionChoice==EKNIGHT){
				newChessValue=3;//ChessPiece.KNIGHTVALUE;
				promotedImage=ImageIO.read(new File("images/"+goodOrBad+"knight.png"));
			}
			if(promotionChoice==BISHOP||promotionChoice==EBISHOP){
				newChessValue=3;//ChessPiece.BISHOPVALUE;
				promotedImage=ImageIO.read(new File("images/"+goodOrBad+"bishop.png"));
			}
			if(board[xCoordinate][yCoordinate].chessNumber==PAWN){	
				for(int u=0;u<whiteImageList.size();u++){
					if(whiteImageList.get(u)!=null&&whiteImageList.get(u).xLocation==xCoordinate&&whiteImageList.get(u).yLocation==yCoordinate){
						whiteImageList.get(u).chessNumber=promotionChoice;
						whiteImageList.get(u).chessValue=newChessValue;
						// Image image=ImageIO.read(new File("images/enemy_knight.png"));
						whiteImageList.get(u).imagePiece=resize((BufferedImage)promotedImage,30,30);
						break;
					}
				}
			}else{
				for(int u=0;u<blackImageList.size();u++){
					if(blackImageList.get(u)!=null&&blackImageList.get(u).xLocation==xCoordinate&&blackImageList.get(u).yLocation==yCoordinate){
						blackImageList.get(u).chessNumber=promotionChoice;
						blackImageList.get(u).chessValue=newChessValue;
						// Image image=ImageIO.read(new File("images/enemy_knight.png"));
						blackImageList.get(u).imagePiece=resize((BufferedImage)promotedImage,30,30);
					}
				}
			}
			moveHistory.to.promotedNumber=promotionChoice;
		}catch(IOException eX){}
		promotionChoice=-1;
		
		frame.setVisible(false);
		// }
	}
	JButton okButton=null;
	JRadioButton qOption=null;
	JRadioButton rOption=null;
	JRadioButton kOption=null;
	JRadioButton bOption=null;
	
	int promotionChoice=-1;
	public JPanel userChoice(){
		JPanel panel=new JPanel();
		ButtonGroup groupOfButtons=new ButtonGroup();
		qOption=new JRadioButton("Queen");
		rOption=new JRadioButton("Rookie");
		kOption=new JRadioButton("Knight");
		bOption=new JRadioButton("Bishop");
		ChangeListener listener=
		new ChangeListener() {
			  public void stateChanged(ChangeEvent changeEvent) {
					if(qOption.isSelected()){
						// //////System.out.println("qoption");
						promotionChoice=1;
					}
					if(rOption.isSelected()){
						// //////System.out.println("roption");
						promotionChoice=3;
					}
					if(kOption.isSelected()){
						// //////System.out.println("koption");
						promotionChoice=2;
					}
					if(bOption.isSelected()){
						promotionChoice=4;
						// //////System.out.println("boption");
					}
					if(teamNumber>=6){
						promotionChoice+=6;
					}
					
					// //////System.out.println("Promotin choice: "+promotionChoice);
			  }
			};
		
		qOption.addChangeListener(listener);
		groupOfButtons.add(qOption);
		rOption.addChangeListener(listener);
		groupOfButtons.add(rOption);
		bOption.addChangeListener(listener);
		groupOfButtons.add(bOption);
		kOption.addChangeListener(listener);
		groupOfButtons.add(kOption);
	
		okButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
		//////System.out.println("Here promote: "+promotionChoice);
				// if(qOption.isSelected()){
					// promotionChoice=1;
				// }else if(rOption.isSelected()){
					// promotionChoice=3;
				// }else if(bOption.isSelected()){
					// promotionChoice=4;
				// }else if(kOption.isSelected()){
					// promotionChoice=2;
				// }
				// if(teamNumber>=6){
					// promotionChoice+=5;
				// }
				startPromoting();
				frame.setVisible(false);
				repaint();
			}
		});
		for (java.util.Enumeration<AbstractButton> en = groupOfButtons.getElements(); en.hasMoreElements();) {
			AbstractButton b = en.nextElement();
			panel.add(b);
		}
		// panel.add(groupOfButtons);
		panel.add(okButton);
		return panel;
	}
	public JFrame A3JFrame(String title, int x, int y, int width, int height){

		// Set the title, top left location, and close operation for the 
		frame=new JFrame();
		frame.setTitle(title);
		frame.setLocation(x, y);
		// setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// Create an instance of the JPanel class, and set this to define the
		// content of the window
		JPanel frameContent = userChoice();
		Container visibleArea = frame.getContentPane();
		visibleArea.add(frameContent);

		// Set the size of the content pane of the window, resize and validate the
		// window to suit, obtain keyboard focus, and then make the window visible
		frameContent.setPreferredSize(new Dimension(width, height));
		frame.pack();
		frameContent.requestFocusInWindow();
		frame.setVisible(false);
		frame.setResizable(false);
		return frame;
		// t.start();
		
	}
	
	public void mouseReleased(MouseEvent e){}
	public void mouseClicked(MouseEvent e){}
	public void mouseExited(MouseEvent e){}
	public void mouseEntered(MouseEvent e){}
	public void actionPerformed(ActionEvent e){
		if(e.getSource()==hintButton){
			bestList=new java.util.ArrayList<ChessOutcome>();
			java.util.ArrayList<ChessPiece> whiteTeam=new java.util.ArrayList<ChessPiece>();
			java.util.ArrayList<ChessPiece> blackTeam=new java.util.ArrayList<ChessPiece>();
			ChessPiece [][]afterBoard=copyBoard(board);
			for(int j=0;j<afterBoard.length;j++){
				for(int k=0;k<afterBoard.length;k++){
					if(afterBoard[j][k]!=null){
						if(afterBoard[j][k].chessNumber<6){
							whiteTeam.add(afterBoard[j][k]);
							afterBoard[j][k].arrayIndexLocation=whiteTeam.size()-1;
						}else{
							blackTeam.add(afterBoard[j][k]);
							afterBoard[j][k].arrayIndexLocation=blackTeam.size()-1;
						}
					}
				}
			}
			int depthDeterminer=4;
			// int strongUnitCount=0;
			// int horseCount=0;
			// int strongEnemyUnitCount=0;
			// int enemyHorseCount=0;
			double totalPossibleMoves=0;
			int totalEnemyPossibleMoves=0;
			for(ChessPiece w:whiteTeam){
				if(w.chessNumber==ROOKIE||w.chessNumber==BISHOP){
					totalPossibleMoves+=8;
				}
				if(w.chessNumber==QUEEN){
					totalPossibleMoves+=16;
					// strongUnitCount++;
				}
				if(w.chessNumber==KNIGHT){
					totalPossibleMoves+=4;
				}
				if(w.chessNumber==PAWN){
					totalPossibleMoves+=0.5;
				}
				if(w.chessNumber==KING){
					totalPossibleMoves+=4;
				}
			}
			for(ChessPiece w:blackTeam){
				if(w.chessNumber==EROOKIE||w.chessNumber==EBISHOP){
					totalEnemyPossibleMoves+=8;
				}
				if(w.chessNumber==EQUEEN){
					totalEnemyPossibleMoves+=16;
					// strongUnitCount++;
				}
				if(w.chessNumber==EKNIGHT){
					totalEnemyPossibleMoves+=4;
				}
				if(w.chessNumber==EPAWN){
					totalEnemyPossibleMoves+=0.5;
				}
				if(w.chessNumber==EKING){
					totalEnemyPossibleMoves+=4;
				}
			}
			/*
			int countMaxDepth=1;
			int fStep=0;
			while(countMaxDepth<2000000){
				if(fStep%2==0){
					countMaxDepth*=totalPossibleMoves;
				}else{
					countMaxDepth*=totalEnemyPossibleMoves;
				}
				fStep++;
			}
			fStep-=2;
			depthDeterminer=fStep/2;
			depthDeterminer=depthDeterminer*2;
			// if(depthDeterminer<4){
				// depthDeterminer=4;
			// }
			// FinalScore bestMove=bestMove3(0,500000,4);
			*/
			depthDeterminer=Integer.parseInt(depthText.getText());
			// ChessOutcome bestMove=bestMoveUsingMiniMax(0, depthDeterminer,depthDeterminer, whiteTeam, blackTeam, afterBoard,null,1000,0);
			ChessOutcome cmax=new ChessOutcome(null,0,0,0,0,0,null);
			cmax.score=-1000;
			ChessOutcome cmin=new ChessOutcome(null,0,0,0,0,0,null);
			cmin.score=1000;
			
			ChessOutcome bestMove=alphabeta(0, cmax, cmin, true, 0, depthDeterminer, depthDeterminer, whiteTeam, blackTeam, afterBoard,null, 0,null);

			// System.out.println("Depth: "+depthDeterminer+" Max nodes to visit :"+countMaxDepth);
			System.out.println("Depth: "+depthDeterminer+" Max nodes to visit :"+nodesVisited);
			nodesVisited=0;
			System.out.println("Hint put "+bestMove.chessMove.from+" to "+bestMove.chessMove.to +" Points: "+bestMove.score);
			printMoveHistory(bestMove.moveHistory);
		}
		if(e.getSource()==undobutton&&moveHistory!=null){
			
			ChessMove lastMove=new ChessMove(moveHistory.from,moveHistory.to);
			lastMove.pieceEaten=moveHistory.pieceEaten;
			
			if(lastMove.to.promotedNumber>=0&&lastMove.to.promotedNumber<11){
				String imageName="pawn.png";
				if(stepNumber%2==0){
					imageName="enemy_"+imageName;
				}
				imageName="images/"+imageName;
				try{
					Image chessImage=ImageIO.read(new File(imageName));
				
					board[lastMove.to.x][lastMove.to.y].imagePiece=resize((BufferedImage)chessImage,30,30);
				}catch(IOException ex){}
			}
			if(stepNumber==canLeftCastle||stepNumber==canEnemyLeftCastle||stepNumber==canRightCastle||stepNumber==canEnemyRightCastle){
				if(stepNumber==canLeftCastle){
					canLeftCastle=-1;
				}
				if(stepNumber==canRightCastle){
					canRightCastle=-1;
				}
				if(stepNumber==canEnemyLeftCastle){
					canEnemyLeftCastle=-1;
				}
				if(stepNumber==canEnemyRightCastle){
					canEnemyRightCastle=-1;
				}
			}
			stepNumber=moveToCurrentStepNumber(stepNumber, whiteImageList, blackImageList, lastMove, board, false);
			// if(lastMove.pieceEaten.chessNumber<6){
				// for(ChessPiece pirooece:whiteImageList){
					// System.out.print(piece+" ");
				// }
				// System.out.println();
			// }
			// System.out.println("Last move" +lastMove);
			moveHistory=moveHistory.previousMove;
			if(moveHistory!=null){
				moveHistory.nextMove=null;
			}
		}
		
		if(e.getSource()==showMoveHistoryButton&&moveHistory!=null){
			while(moveHistory.previousMove!=null){
				moveHistory=moveHistory.previousMove;
			}
			int step=0;
			System.out.println("\nHistory: ");
			while(moveHistory.nextMove!=null){
				System.out.println(step+": "+moveHistory);
				moveHistory=moveHistory.nextMove;
				step++;
			}
			System.out.println(step+": "+moveHistory);
			System.out.println();
		}
		repaint();
	}
	
	public void printMoveHistory(ChessMove m){
		if(m==null){
			return;
		}
		while(m.previousMove!=null){
		m=m.previousMove;
		}
		while(m.nextMove!=null){
			System.out.println(m);
			m=m.nextMove;
		}
		System.out.println(m);
	}
   	public void paintComponent(Graphics g){
	
 	    super.paintComponent(g);
		g.setFont(new Font("Serif", Font.ITALIC, 35));
		drawTitle(g);
		g.setFont(new Font("Serif", Font.ITALIC, 20));
		drawBoard(g);
		drawPiecesAndTheirFootsteps(g);
		drawBlackPieces(g);
		drawWhitePieces(g);
	}
	// public void setFont(Graphics g){
		
	// }
	public void drawTitle(Graphics g){
		g.drawString("Chess",40,40);
	}
	
	public void drawBoard(Graphics g){
		
		for(int i=0;i<320;i+=40){
			g.drawString(""+(int)(i/40),50,i+100);
			g.drawString(""+(int)(i/40),i+100,70);
		}
		for(int x=0;x<320;x+=40){
			for(int y=0;y<320;y+=40){
				// if(xCoordinate==x/40&&yCoordinate==y/40){
					// g.setColor(Color.YELLOW);
				// }else 
				// printBoard(board);
				// if(board[x/40][y/40]!=null&&(stepNumber%2==0&&board[x/40][y/40].chessNumber<6||stepNumber%2==1&&board[x/40][y/40].chessNumber>5)&&xCoordinate==x/40&&yCoordinate==y/40||hasChoosersStep(x/40,y/40)){
					// g.setColor(Color.YELLOW);
					
				// }else 
				if((x+y)%80==0){
					g.setColor(Color.GRAY);
				
				}else{
					g.setColor(new Color(200,200,200));
				}
				
				g.fillRect(x+80,y+80,40,40);
			}
		}
		// for(int j=0;j<whiteImageList.size();j++){
			// if(whiteImageList.xLocation=
		// //////System.out.println("("+xCoordinate+","+yCoordinate+")");
	
	}
	
	public void drawPiecesAndTheirFootsteps(Graphics g){
		g.setColor(Color.YELLOW);
		if(stepNumber%2==0){
		
			for(int j=0;j<whiteImageList.size();j++){
				if(whiteImageList.get(j)!=null&&xCoordinate==whiteImageList.get(j).xLocation&&yCoordinate==whiteImageList.get(j).yLocation){
					g.fillRect(whiteImageList.get(j).xLocation*40+80,whiteImageList.get(j).yLocation*40+80,40,40);//Color.YELLOW);
					break;
				}
			}
		}else{
			for(int j=0;j<blackImageList.size();j++){
				if(blackImageList.get(j)!=null&&xCoordinate==blackImageList.get(j).xLocation&&yCoordinate==blackImageList.get(j).yLocation){
					g.fillRect(blackImageList.get(j).xLocation*40+80,blackImageList.get(j).yLocation*40+80,40,40);//Color.YELLOW);
				}
			}
		}
		for(int j=0;j<choosersSteps.size();j++){
			g.fillRect(choosersSteps.get(j).x*40+80,choosersSteps.get(j).y*40+80,40,40);
		}
	}
	boolean hasChoosersStep(int x,int y){
		for(int j=0;j<choosersSteps.size();j++){
			if(choosersSteps.get(j).x==x&&choosersSteps.get(j).y==y){
				return true;
			}
		}
		return false;
	}
	

	
	
	public void drawBlackPieces(Graphics g){
		// for(int j=0;j<8;j++){
			// g.drawImage(blackImageList.get(j),j*40+85,85,null);
		// }
		// for(int j=0;j<8;j++){
			// g.drawImage(blackImageList.get(8),j*40+85,125,null);
		// }
		
		for(int j=0;j<blackImageList.size();j++){
			if(blackImageList.get(j)!=null){
				g.drawImage(blackImageList.get(j).imagePiece,blackImageList.get(j).xLocation*40+85,blackImageList.get(j).yLocation*40+85,null);
			}
		}
	}
	
	public void drawWhitePieces(Graphics g){
		// for(int j=0;j<8;j++){
			// g.drawImage(whiteImageList.get(j).imagePiece,j*40+85,85+7*40,null);
		// }
		// for(int j=0;j<8;j++){
			// g.drawImage(whiteImageList.get(8).imagePiece,j*40+85,45+7*40,null);
		// }
		for(int j=0;j<whiteImageList.size();j++){
			if(whiteImageList.get(j)!=null){
				g.drawImage(whiteImageList.get(j).imagePiece,whiteImageList.get(j).xLocation*40+85,whiteImageList.get(j).yLocation*40+85,null);
			}
		}
	}
	
	/*
	public Object []bestMove(int teamNo){
		Object []resultArray=new Object[5];
		
		// java.util.ArrayList<ChessOutcome> outcomes=new java.util.ArrayList<ChessOutcome>();
		ChessOutcome outcome=null;
		java.util.ArrayList<ChessOutcome> futureSteps=new java.util.ArrayList<ChessOutcome>();
		
		int totalMilitaryPoints=0;
		int militaryPoints=0;
		if(teamNo<6){
			int []promotedValues={KNIGHT,BISHOP,ROOKIE,QUEEN};
			for(int j=0;j<whiteImageList.size();j++){
				java.util.ArrayList<ChessLocation> listOfLocations=whiteImageList.get(j).getListOfMoves();
				ChessLocation firstStep=new ChessLocation(whiteImageList.get(j).xLocation,whiteImageList.get(j).yLocation);
				for(int k=0;k<listOfLocations.size();k++){
					futureSteps=new java.util.ArrayList<ChessOutcome>();
					ChessLocation cl=listOfLocations.get(k);//new ChessLocation( listOfLocations.get(k).x,listOfLocations.get(k).y);
					cl.previousLocation=firstStep;
					cl.locatedBy=whiteImageList.get(j);

					if(board[cl.x][cl.y]!=null){
						militaryPoints=board[cl.x][cl.y].chessValue;
						totalMilitaryPoints+=militaryPoints;
					}
					if(whiteImageList.get(j).chessValue==whiteImageList.get(j).PAWNVALUE&&cl.y==7){
						
						for(int l=0;l<4;l++){
							cl.promotedNumber=promotedValues[l];
							futureSteps.add(new ChessOutcome(null,cl,0,0,0,militaryPoints));
						}
						totalMilitaryPoints+=(whiteImageList.get(j).KNIGHTVALUE+whiteImageList.get(j).QUEENVALUE+whiteImageList.get(j).BISHOPVALUE+whiteImageList.get(j).ROOKIEVALUE-4);
					}else{
						futureSteps.add(new ChessOutcome(null,cl,0,0,0,militaryPoints));
					}
					// outcomes.add(new ChessOutcome(futureSteps,0,0,0,0));
				}
			}
		}else{
			int []promotedValues={EKNIGHT,EBISHOP,EROOKIE,EQUEEN};
			for(int j=0;j<blackImageList.size();j++){
				java.util.ArrayList<ChessLocation> listOfLocations=blackImageList.get(j).getListOfMoves();
				ChessLocation firstStep=new ChessLocation(blackImageList.get(j).xLocation,blackImageList.get(j).yLocation);

				for(int k=0;k<listOfLocations.size();k++){
					futureSteps=new java.util.ArrayList<ChessOutcome>();
					ChessLocation cl=listOfLocations.get(k);//new ChessLocation(stepNumber+1, listOfLocations.get(k).x,listOfLocations.get(k).y);
					cl.previousLocation=firstStep;
					cl.locatedBy=blackImageList.get(j);
					if(board[cl.x][cl.y]!=null){
						militaryPoints=board[cl.x][cl.y].chessValue;
						totalMilitaryPoints+=militaryPoints;
					}
					if(blackImageList.get(j).chessValue==blackImageList.get(j).PAWNVALUE&&cl.y==7){
					
						for(int l=0;l<4;l++){
							cl.promotedNumber=promotedValues[l];
							
							futureSteps.add(new ChessOutcome(null,cl,0,0,0,militaryPoints));
						}
						militaryPoints+=(whiteImageList.get(j).KNIGHTVALUE+whiteImageList.get(j).QUEENVALUE+whiteImageList.get(j).BISHOPVALUE+whiteImageList.get(j).ROOKIEVALUE-4);
						// militaryPoints
					}else{
						futureSteps.add(new ChessOutcome(null,cl,0,0,0,militaryPoints));
					}
					// outcomes.add(new ChessOutcome(futureSteps,0,0,0,0));
				}
			}
		}
		ChessPiece enemyKingDeterminer=null;
		ChessLocation enemyLocation=null;
		if(teamNo<6){
			enemyKingDeterminer=enemyKingLocation;
			enemyLocation=new ChessLocation(enemyKingLocation.xLocation,enemyKingLocation.yLocation);
		}else{
			enemyKingDeterminer=kingLocation;
			enemyLocation=new ChessLocation(kingLocation.xLocation,kingLocation.yLocation);
		}
		int previousX,previousY=0;
		ChessPiece pieceBeforeEaten=null;
		ChessPiece beforeMoved=null;
		for(int j=0;j<futureSteps.size();j++){	//impossible.
			beforeMoved=futureSteps.get(j).location.locatedBy;
			previousX=beforeMoved.xLocation;
			previousY=beforeMoved.yLocation;
			board[previousX][previousY]=null;
			pieceBeforeEaten=board[futureSteps.get(j).location.x][futureSteps.get(j).location.y];
			board[futureSteps.get(j).location.x][futureSteps.get(j).location.y]=beforeMoved;
			
			if(!willNotKillKing(enemyKingDeterminer,enemyKingDeterminer,enemyLocation,board)){
				
				// Object []ra= {1,0,0,(double)(militaryPoints)/(double)(futureSteps.size()),futureSteps.get(j).location};
				resultArray[0]=1;
				resultArray[1]=0;
				resultArray[2]=0;
				resultArray[3]=(double)(militaryPoints)/(double)(futureSteps.size());
				resultArray[4]=futureSteps.get(j).location;
				// ////System.out.println(enemyKingDeterminer.xLocation+" "+enemyKingDeterminer.yLocation+" Yes");
				board[futureSteps.get(j).location.x][futureSteps.get(j).location.y]=pieceBeforeEaten;
				board[previousX][previousY]=beforeMoved;
				return resultArray;
				// return resultArray;
			}
			board[futureSteps.get(j).location.x][futureSteps.get(j).location.y]=pieceBeforeEaten;
			board[previousX][previousY]=beforeMoved;
		}
		return resultArray;
		// outcome=new ChessOutcome(futureSteps,null,0,0,0,(double)(militaryPoints)/(double)(futureSteps.size()));
		// return calculateBestMove(outcome, 500, resultArray);
		
	}
	*/
	public int findIndex(java.util.ArrayList<ChessOutcome> list, ChessMove move){
		
		for(int j=0;j<list.size();j++){
			if(list.get(j).chessMove.from.x==move.from.x&&list.get(j).chessMove.to.x==move.to.x&&list.get(j).chessMove.from.y==move.from.y&&list.get(j).chessMove.to.y==move.to.y){
				return j;
			}
			
		}
		return -1;
	}
	///*
	public int findIndex2(java.util.ArrayList<FinalScore> list, ChessMove move){
		
		for(int j=0;j<list.size();j++){
			if(list.get(j).cMove.from.x==move.from.x&&list.get(j).cMove.to.x==move.to.x&&list.get(j).cMove.from.y==move.from.y&&list.get(j).cMove.to.y==move.to.y){
				// ChessMove secondTest=copyChessMove(move);
				// ChessMove secondListTest=copyChessMove(list.get(j).cMove);
				// secondTest=secondTest.nextMove;
				// secondListTest=secondListTest.nextMove;
				// if(secondTest!=null&&secondListTest!=null&&secondTest.from.x==secondListTest.from.x&&secondTest.to.x==secondListTest.to.x&&secondTest.from.y==secondListTest.from.y&&secondTest.to.y==secondListTest.to.y){
					return j;
				// }
			}
			
		}
		return -1;
	}
	
	//*/
	public boolean atOriginalPosition(ChessPiece [][]afterBoard){
		for(int j=0;j<afterBoard.length;j++){
			
			for(int k=0;k<afterBoard[j].length;k++){
				if(k<2&&(afterBoard[j][k]==null||afterBoard[j][k].chessNumber<6)){
					return false;
				}
				if(k>5&&(afterBoard[j][k]==null||afterBoard[j][k].chessNumber>5)){
					return false;
				}
				if(k>1&&k<6&&afterBoard[j][k]!=null){
					return false;
				}
			}
		}
		return true;
	}
	// ChessLocation moveHistory=null;
	public ChessOutcome bestMove2(int teamNo, int strengthPoints){
		int enemyTeamNo=0;
		if(teamNo<6){
			enemyTeamNo=8;
		}else{
			enemyTeamNo=2;
		}
		ChessOutcome determineBaseCase=null;
		ChessPiece kingDeterminer=null;
		ChessPiece enemyKingDeterminer=null;
		java.util.ArrayList<ChessPiece> teamImageList=null;
		java.util.ArrayList<ChessPiece> enemyTeamImageList=null;
		// Object []bestMoveParameter=new Object[6];
		
		java.util.ArrayList<ChessOutcome> moveRating=new java.util.ArrayList<ChessOutcome>();
		java.util.ArrayList<java.util.ArrayList<Range>> moveRatingIndex=new java.util.ArrayList<java.util.ArrayList<Range>>();
		java.util.ArrayList<ChessOutcome> outcomeHistory=new java.util.ArrayList<ChessOutcome>();
		ChessPiece [][]afterBoard=copyBoard(board);
		java.util.ArrayList<ChessPiece> whiteTempList=new java.util.ArrayList<ChessPiece>();
		java.util.ArrayList<ChessPiece> blackTempList=new java.util.ArrayList<ChessPiece>();
		for(int  j=0;j<afterBoard.length;j++){
			for(int k=0;k<afterBoard[j].length;k++){
				if(afterBoard[j][k]!=null){
					if(afterBoard[j][k].chessNumber<6){
						whiteTempList.add(afterBoard[j][k]);
					}else{
						blackTempList.add(afterBoard[j][k]);
					}
					if(afterBoard[j][k].chessNumber==0){
						if(teamNo<6){
							kingDeterminer=afterBoard[j][k];
						}else{
							enemyKingDeterminer=afterBoard[j][k];
						}
					}else if(afterBoard[j][k].chessNumber==6){
						if(teamNo>=6){
							kingDeterminer=afterBoard[j][k];
						}else{
							enemyKingDeterminer=afterBoard[j][k];
						}
					}
				}
			}
		}
		if(teamNo<6){
			// enemyKingDeterminer=enemyKingLocation;
			teamImageList=whiteTempList;
			enemyTeamImageList=blackTempList;
			
		}else{
			// enemyKingDeterminer=kingLocation;
			teamImageList=blackTempList;
			enemyTeamImageList=whiteTempList;
		}
		// int enemyMoveCount=0;
		// for(int l=0;l<enemyTeamImageList.size();l++){
			// enemyMoveCount+=enemyTeamImageList.get(l).getListOfMoves().size();
		// }

		for(int j=0;j<teamImageList.size();j++){
			java.util.ArrayList<ChessLocation> teamSteps=teamImageList.get(j).getListOfMoves2(afterBoard,teamNo);
			for(int k=0;k<teamSteps.size();k++){
				// ChessLocation firstLocation=teamSt
				ChessMove firstMove=new ChessMove(new ChessLocation(teamImageList.get(j).xLocation,teamImageList.get(j).yLocation),teamSteps.get(k));
				int militaryPoints=0;
				// ChessPiece pieceBeforeEaten=null;
				if(afterBoard[teamSteps.get(k).x][teamSteps.get(k).y]!=null){
					militaryPoints=afterBoard[teamSteps.get(k).x][teamSteps.get(k).y].chessValue;
					firstMove.pieceEaten=afterBoard[teamSteps.get(k).x][teamSteps.get(k).y];
				}
				// System.out.println("First move here 2: "+firstMove);
				ChessOutcome outcome=new ChessOutcome(firstMove,0,0,1,0,militaryPoints,null);
				
				int resultIs=willCheckMateEnemyKing2(enemyKingDeterminer, afterBoard[teamImageList.get(j).xLocation][teamImageList.get(j).yLocation], enemyTeamImageList, teamSteps.get(k), afterBoard);
				// willCheckMateEnemyKing2(ChessPiece enemyKing, ChessPiece beforeMoved, java.util.ArrayList<ChessPiece> goodTeam, ChessLocation step,ChessPiece [][]afterBoard)
				if(resultIs==1){
					System.out.println("("+choosersSteps.get(j).x+","+choosersSteps.get(j).y+") will make you win.");
					// bestMoveParameter[0]=new ChessLocation(teamImageList.get(j).xLocation,teamImageList.get(j).yLocation);
					// bestMoveParameter[1]=teamsSteps.get(k);
					// bestMoveParameter[2]=1;
					// bestMoveParameter[3]=0;
					// bestMoveParameter[4]=0;
					// bestMoveParameter[5]=0;
					outcome.winningCount=1;
					outcome.unfinishedCount=0;
					return outcome;
					
				}else if(resultIs==0){
					outcome.drawingCount=1;
					outcome.unfinishedCount=0;
				}
				moveRating.add(outcome);
				java.util.ArrayList r=new java.util.ArrayList<Range>();
				r.add(new Range(j*teamImageList.size()+k,j*teamImageList.size()+k,j*teamImageList.size()+k+1,1));
				// moveRatingIndex.add(r);
				outcomeHistory.add(outcome);
				strengthPoints--;
			}
		}
		// System.out.println("Got here without a problem");
		// for(ChessOutcome out:outcomeHistory){
			// System.out.println(out.chessMove);
		// }
		int futureStepNumber=stepNumber;
		// for(int j=0;j<outcomeHistory.size();j++){
			//System.out.println(outcomeHistory.get(j).chessMove);
		// }
		// int lastSavePoint=moveRating.size();
		// ChessMove lastSaveMove=moveRating.get(0).chessMove;
		while(strengthPoints>0){
		
		
			ChessOutcome currentOutcome=outcomeHistory.remove(0);
		
			if(currentOutcome.unfinishedCount==1){
				determineBaseCase=currentOutcome;
			}else if(determineBaseCase!=null){
				if(determineBaseCase.equals(currentOutcome)){
					break;
				}else{
					determineBaseCase=currentOutcome;
				}
			}
			if(currentOutcome.winningCount==1||currentOutcome.drawingCount==1||currentOutcome.losingCount==1){
				outcomeHistory.add(currentOutcome);
				
				continue;
			}
			// System.out.println(teamNo+" "+futureStepNumber+" "+outcomeHistory.size());
			// printChessSteps(currentOutcome.chessMove);
			// for(int m=0;m<moveRatingIndex;m++){
				// moveRatingIndex.get(m).x--;
				// moveRatingIndex.get(m).y--;
			// }
			ChessMove firstMove=currentOutcome.chessMove;
			// printChessSteps(firstMove);
			// printBoard(afterBoard);
			//System.out.println("first move here" +firstMove);
			if(afterBoard[firstMove.to.x][firstMove.to.y]!=null){
			// System.out.println(afterBoard[firstMove.to.x][firstMove.to.y]);
				int j=0;
				if(afterBoard[firstMove.to.x][firstMove.to.y].chessNumber>=6 &&teamNo<6||afterBoard[firstMove.to.x][firstMove.to.y].chessNumber<6 &&teamNo>=6){//if(stepNumber%2==0){//
					while(enemyTeamImageList.get(j).xLocation!=firstMove.to.x||enemyTeamImageList.get(j).yLocation!=firstMove.to.y){
						j++;
					}
					enemyTeamImageList.remove(j);
				}else{
					while(teamImageList.get(j).xLocation!=firstMove.to.x||teamImageList.get(j).yLocation!=firstMove.to.y){
						j++;
					}
					teamImageList.remove(j);
				}
			}
			
			//System.out.println("Went here"+futureStepNumber+" "+firstMove.from);
			// printBoard(afterBoard);
			
			afterBoard[firstMove.to.x][firstMove.to.y]=afterBoard[firstMove.from.x][firstMove.from.y];
			afterBoard[firstMove.to.x][firstMove.to.y].xLocation=firstMove.to.x;
			afterBoard[firstMove.to.x][firstMove.to.y].yLocation=firstMove.to.y;
			// System.out.println(teamImageList.get(0).xLocation+" "+teamImageList.get(0).yLocation);
			afterBoard[firstMove.from.x][firstMove.from.y]=null;
			// if(futureStepNumber%2==0&&teamNo<6||futureStepNumber%2==1&&teamNo>=6){
						// for(int t=0;t<teamImageList.size();t++){
							// if(teamImageList.get(t).xLocation==firstMove.from.x&&teamImageList.get(t).yLocation==firstMove.from.y){
								// teamImageList.get(t).xLocation=firstMove.to.x;
								// teamImageList.get(t).yLocation=firstMove.to.y;
								// break;
							// }
						// }
					// }else{
						// for(int t=0;t<enemyTeamImageList.size();t++){
							// if(enemyTeamImageList.get(t).xLocation==firstMove.from.x&&enemyTeamImageList.get(t).yLocation==firstMove.from.y){
								// enemyTeamImageList.get(t).xLocation=firstMove.to.x;
								// enemyTeamImageList.get(t).yLocation=firstMove.to.y;
								// break;
							// }
						// }
					// }
			// System.out.println("got here");
			futureStepNumber++;
			
			// ChessMove saveMove=copyChessMove(firstMove);

			// ChessMove saveMove=copyChessMove(firstMove);
			// firstMove=firstMove.nextMove;
			do{
				if(firstMove.nextMove==null){
					break;
				}
				firstMove=firstMove.nextMove;
				
				
					// saveMove=copyChessMove(firstMove);
					if(afterBoard[firstMove.to.x][firstMove.to.y]!=null){
						int j=0;
						if(afterBoard[firstMove.to.x][firstMove.to.y].chessNumber>=6 &&teamNo<6||afterBoard[firstMove.to.x][firstMove.to.y].chessNumber<6 &&teamNo>=6){//if(stepNumber%2==0){//
							while(enemyTeamImageList.get(j).xLocation!=firstMove.to.x||enemyTeamImageList.get(j).yLocation!=firstMove.to.y){
								j++;
							}
							enemyTeamImageList.remove(j);
						}else{
							while(teamImageList.get(j).xLocation!=firstMove.to.x||teamImageList.get(j).yLocation!=firstMove.to.y){
								j++;
							}
							teamImageList.remove(j);
						}
					}
					// printChessSteps(firstMove);
					// printBoard(afterBoard);
					// System.out.println("From "+firstMove.from+ " to "+firstMove.to);
					afterBoard[firstMove.to.x][firstMove.to.y]=afterBoard[firstMove.from.x][firstMove.from.y];
					afterBoard[firstMove.to.x][firstMove.to.y].xLocation=firstMove.to.x;
					afterBoard[firstMove.to.x][firstMove.to.y].yLocation=firstMove.to.y;//i already did it here though.
					// System.out.println("got here");
					// if(futureStepNumber%2==0&&teamNo<6||futureStepNumber%2==1&&teamNo>=6){
						// for(int t=0;t<teamImageList.size();t++){
							// if(teamImageList.get(t).xLocation==firstMove.from.x&&teamImageList.get(t).yLocation==firstMove.from.y){
								// teamImageList.get(t).xLocation=firstMove.to.x;
								// teamImageList.get(t).yLocation=firstMove.to.y;
								// break;
							// }
						// }
					// }else{
						// for(int t=0;t<enemyTeamImageList.size();t++){
							// if(enemyTeamImageList.get(t).xLocation==firstMove.from.x&&enemyTeamImageList.get(t).yLocation==firstMove.from.y){
								// enemyTeamImageList.get(t).xLocation=firstMove.to.x;
								// enemyTeamImageList.get(t).yLocation=firstMove.to.y;
								// break;
							// }
						// }
					// }
					
					if(firstMove.to.promotedNumber>=0&&firstMove.to.promotedNumber<11){
						// if(afterBoard[firstMove.to.x][firstMove.to.y].chessNumber<6){
						afterBoard[firstMove.to.x][firstMove.to.y].chessNumber=firstMove.to.promotedNumber;
							// afterBoard[firstMove.from.x][firstMove.from.y].chessValue=1;
						// }else{
							// afterBoard[firstMove.from.x][firstMove.from.y].chessNumber=EPAWN;
						// }
						if(firstMove.to.promotedNumber%6==1){
							afterBoard[firstMove.to.x][firstMove.to.y].chessValue=9;
						}else if(firstMove.to.promotedNumber%6==2){
							afterBoard[firstMove.to.x][firstMove.to.y].chessValue=3;
						}else if(firstMove.to.promotedNumber%6==3){
							afterBoard[firstMove.to.x][firstMove.to.y].chessValue=5;
						}else{
							afterBoard[firstMove.to.x][firstMove.to.y].chessValue=3;
						}
							
						// afterBoard[firstMove.from.x][firstMove.from.y].chessValue=1;
					}
					afterBoard[firstMove.from.x][firstMove.from.y]=null;
					
					futureStepNumber++;
					// saveMove=copyChessMove(firstMove);
					// if(firstMove.nextMove==null){
						// break;
					// }
					// firstMove=firstMove.nextMove;
				} while(firstMove.nextMove!=null);
				// for(int j=0;j<teamImageList.size();j++){
					// System.out.println("team name: "+teamImageList.get(j).chessNumber+" Team list locations "+teamImageList.get(j).xLocation+" "+teamImageList.get(j).yLocation);
				// }
				// for(int j=0;j<enemyTeamImageList.size();j++){
					// System.out.println("enemy team name: "+enemyTeamImageList.get(j).chessNumber+" enemy Team list locations "+enemyTeamImageList.get(j).xLocation+" "+enemyTeamImageList.get(j).yLocation);
				// }
			// }
			// firstMove=saveMove;
			//System.out.println("Yikes"+futureStepNumber);
			System.out.println("\nafter\n");
			// printBoard(afterBoard);
			System.out.println();
			//now is time for examination.
			if(futureStepNumber%2==0&&teamNo<6||futureStepNumber%2==1&&teamNo>=6){//if right team's turn
				int upto=outcomeHistory.size();
				for(int m=0;m<teamImageList.size();m++){
					boolean isBreaking=false;
					// System.out.println("Team location and board map: "+teamImageList.get(m).xLocation+" "+teamImageList.get(m).yLocation);
					// printBoard(afterBoard);
					// int xL=teamImageList.get(m).xLocation;
					// int yL=teamImageList.get(m).yLocation;
					// System.out.println("before: "+teamImageList.get(m).xLocation+" "+teamImageList.get(m).yLocation);
					java.util.ArrayList<ChessLocation> teamSteps=teamImageList.get(m).getListOfMoves2(afterBoard,teamNo);
					// if(teamImageList.get(m).xLocation!=xL||teamImageList.get(m).yLocation!=yL){
						// System.out.println("Check here"+teamImageList.get(m).xLocation+" "+teamImageList.get(m).yLocation+" "+xL+" "+yL);
					// }
					// System.out.println("after: "+teamImageList.get(m).xLocation+" "+teamImageList.get(m).yLocation+" "+teamSteps);
					for(int k=0;k<teamSteps.size();k++){
						ChessMove next=new ChessMove(new ChessLocation(teamImageList.get(m).xLocation,teamImageList.get(m).yLocation),teamSteps.get(k));
						int militaryPoints=currentOutcome.averageMilitaryPoints;
						// ChessPiece pieceBeforeEaten=null;
						if(afterBoard[teamSteps.get(k).x][teamSteps.get(k).y]!=null){
							militaryPoints+=afterBoard[teamSteps.get(k).x][teamSteps.get(k).y].chessValue;
							next.pieceEaten=afterBoard[teamSteps.get(k).x][teamSteps.get(k).y];
							for(int e=0;e<enemyTeamImageList.size();e++){
								if(enemyTeamImageList.get(e).xLocation==teamSteps.get(k).x&&enemyTeamImageList.get(e).yLocation==teamSteps.get(k).y){
									enemyTeamImageList.remove(e);
									break;
								}
							}
						}
						
						ChessMove typicalMove=copyChessMove(firstMove,true);
						while(typicalMove.nextMove!=null){
							typicalMove=typicalMove.nextMove;
						}
						typicalMove.nextMove=next;
						next.previousMove=typicalMove;
						while(typicalMove.previousMove!=null){
							typicalMove=typicalMove.previousMove;
						}
						ChessOutcome outcomeL=new ChessOutcome(typicalMove,0,0,1,0,militaryPoints,null);
						
						// int resultIs=willCheckMateEnemyKing2(enemyKingDeterminer, afterBoard[teamImageList.get(m).xLocation][teamImageList.get(m).yLocation], teamSteps.get(k));
						// printBoard(afterBoard);
						afterBoard[teamSteps.get(k).x][teamSteps.get(k).y]=teamImageList.get(m);
						afterBoard[teamImageList.get(m).xLocation][teamImageList.get(m).yLocation]=null;
						teamImageList.get(m).xLocation=teamSteps.get(k).x;
						teamImageList.get(m).yLocation=teamSteps.get(k).y;
						// System.out.println("Error here: "+teamImageList.get(m).xLocation+" "+teamImageList.get(m).yLocation);
						int resultIs= willCheckMateEnemyKing2(enemyKingDeterminer, afterBoard[teamImageList.get(m).xLocation][teamImageList.get(m).yLocation], enemyTeamImageList, teamSteps.get(k), afterBoard);
						if(resultIs==1){
							outcomeL.winningCount=1;
							outcomeL.unfinishedCount=0;
							// return outcome;
							outcomeHistory.subList(upto,outcomeHistory.size()).clear();
							outcomeHistory.add(outcomeL);
							
							teamImageList.get(m).xLocation=next.from.x;
							teamImageList.get(m).yLocation=next.from.y;
							afterBoard[teamSteps.get(k).x][teamSteps.get(k).y]=next.pieceEaten;
							afterBoard[next.from.x][next.from.y]=teamImageList.get(m);
							if(next.pieceEaten!=null){
								enemyTeamImageList.add(next.pieceEaten);
							}
							// moveRatingIndex.get(m).add(new Range(upto,upto+1));
							break;
							
						}else if(resultIs==0){
							outcomeL.drawingCount=1;
							outcomeL.unfinishedCount=0;
						}else{
							outcomeL.unfinishedCount=1;
						}
						teamImageList.get(m).xLocation=next.from.x;
						teamImageList.get(m).yLocation=next.from.y;
						afterBoard[teamSteps.get(k).x][teamSteps.get(k).y]=next.pieceEaten;
						afterBoard[next.from.x][next.from.y]=teamImageList.get(m);
						if(next.pieceEaten!=null){
							enemyTeamImageList.add(next.pieceEaten);
						}
						// moveRating.add(outcome);
						// java.util.ArrayList r=new java.util.ArrayList<Range>();
						// r.add(new Range(j*teamImageList.size()+k,j*teamImageList.size()+k+1));
						// moveRatingIndex.add(r);
						outcomeHistory.add(outcomeL);
						strengthPoints--;
					}
				}
				// futureStepNumber++;
			}else{
				// System.out.println("enemy turn");
				for(int m=0;m<enemyTeamImageList.size();m++){
				// System.out.println("Before: ");
				// printBoard(afterBoard);
					java.util.ArrayList<ChessLocation> teamSteps=enemyTeamImageList.get(m).getListOfMoves2(afterBoard,enemyTeamNo);
					// System.out.println("After: ");
				// printBoard(afterBoard);
				// System.out.println();
					for(int k=0;k<teamSteps.size();k++){
						ChessMove next=new ChessMove(new ChessLocation(enemyTeamImageList.get(m).xLocation,enemyTeamImageList.get(m).yLocation),teamSteps.get(k));
						// System.out.println("Enemy move: "+next);
						int militaryPoints=currentOutcome.averageMilitaryPoints;
						// ChessPiece pieceBeforeEaten=null;
						boolean t=false;
						/*
						if(next.from.y==3){
							// t=false;
							
							for(int u=0;u<8;u++){
								if(afterBoard[u][4]!=null){
									t=true;
									break;
								}
							}
							if(t){
								printBoard(afterBoard);
								System.out.println(teamSteps.get(k));
							}
						}
						
							if(t){
								System.out.println("Military: "+afterBoard[teamSteps.get(k).x][teamSteps.get(k).y]+" "+(afterBoard[teamSteps.get(k).x][teamSteps.get(k).y]==null)+" "+(afterBoard[teamSteps.get(k).x][teamSteps.get(k).y]!=null));
								System.out.println();
							}
							 //*/
						if(afterBoard[teamSteps.get(k).x][teamSteps.get(k).y]!=null){
							militaryPoints-=afterBoard[teamSteps.get(k).x][teamSteps.get(k).y].chessValue;
							// System.out.println("Military: "+militaryPoints);
							next.pieceEaten=afterBoard[teamSteps.get(k).x][teamSteps.get(k).y];
							for(int e=0;e<teamImageList.size();e++){
								if(teamImageList.get(e).xLocation==teamSteps.get(k).x&&teamImageList.get(e).yLocation==teamSteps.get(k).y){
									teamImageList.remove(e);
									break;
								}
							}
						}
						
						ChessMove typicalMove=copyChessMove(firstMove,true);
						while(typicalMove.nextMove!=null){
							typicalMove=typicalMove.nextMove;
						}
						typicalMove.nextMove=next;
						next.previousMove=typicalMove;
						while(typicalMove.previousMove!=null){
							typicalMove=typicalMove.previousMove;
						}
						ChessOutcome outcomeL=new ChessOutcome(typicalMove,0,0,1,0,militaryPoints,null);
						// if(outcomeL.averageMilitaryPoints>0){
							// System.out.println("military: "+outcomeL.averageMilitaryPoints);
						// }
						afterBoard[teamSteps.get(k).x][teamSteps.get(k).y]=enemyTeamImageList.get(m);
						afterBoard[enemyTeamImageList.get(m).xLocation][enemyTeamImageList.get(m).yLocation]=null;
						enemyTeamImageList.get(m).xLocation=teamSteps.get(k).x;
						enemyTeamImageList.get(m).yLocation=teamSteps.get(k).y;
						// int resultIs=willCheckMateEnemyKing2(kingDeterminer, afterBoard[enemyTeamImageList.get(m).xLocation][enemyTeamImageList.get(m).yLocation], teamSteps.get(k));
						//System.out.println("Check here"+enemyTeamImageList.get(m).xLocation+" "+enemyTeamImageList.get(m).yLocation);
						//printBoard(afterBoard);//+" "+afterBoard[enemyTeamImageList.get(m).xLocation][enemyTeamImageList.get(m).yLocation]);
						int resultIs=willCheckMateEnemyKing2(kingDeterminer, afterBoard[enemyTeamImageList.get(m).xLocation][enemyTeamImageList.get(m).yLocation], teamImageList, teamSteps.get(k), afterBoard);
						if(resultIs==1){

							outcomeL.losingCount=1;
							outcomeL.unfinishedCount=0;
							
							// enemyTeamImageList.get(m).xLocation=next.from.x;
							// enemyTeamImageList.get(m).yLocation=next.from.y;
							// afterBoard[teamSteps.get(k).x][teamSteps.get(k).y]=next.pieceEaten;
							// afterBoard[next.from.x][next.from.y]=enemyTeamImageList.get(m);
							// if(next.pieceEaten!=null){
								// teamImageList.add(next.pieceEaten);
							// }
						
						}else if(resultIs==0){
							outcomeL.drawingCount=1;
							outcomeL.unfinishedCount=0;
						}else{
							outcomeL.unfinishedCount=1;
						}
						
						// moveRating.add(outcome);
						// java.util.ArrayList r=new java.util.ArrayList<Range>();
						// r.add(new Range(j*teamImageList.size()+k,j*teamImageList.size()+k+1));
						// moveRatingIndex.add(r);
						enemyTeamImageList.get(m).xLocation=next.from.x;
						enemyTeamImageList.get(m).yLocation=next.from.y;
						afterBoard[teamSteps.get(k).x][teamSteps.get(k).y]=next.pieceEaten;
						afterBoard[next.from.x][next.from.y]=enemyTeamImageList.get(m);
						if(next.pieceEaten!=null){
							teamImageList.add(next.pieceEaten);
						}
						outcomeHistory.add(outcomeL);
						strengthPoints--;

					}
				}
				// futureStepNumber++;
			}
			//after examining, 
			while(firstMove!=null){
				afterBoard[firstMove.from.x][firstMove.from.y]=afterBoard[firstMove.to.x][firstMove.to.y];
				afterBoard[firstMove.from.x][firstMove.from.y].xLocation=firstMove.from.x;
				afterBoard[firstMove.from.x][firstMove.from.y].yLocation=firstMove.from.y;
				afterBoard[firstMove.to.x][firstMove.to.y]=firstMove.pieceEaten;
				/*if(futureStepNumber%2==0){// i already did this above.
					for(int t=0;t<teamImageList.size();t++){
						if(teamImageList.get(t).xLocation==firstMove.to.x&&teamImageList.get(t).yLocation==firstMove.to.y){
							teamImageList.get(t).xLocation=firstMove.from.x;
							teamImageList.get(t).yLocation=firstMove.from.y;
						}
					}
				}else{
					for(int t=0;t<enemyTeamImageList.size();t++){
						if(enemyTeamImageList.get(t).xLocation==firstMove.to.x&&enemyTeamImageList.get(t).yLocation==firstMove.to.y){
							enemyTeamImageList.get(t).xLocation=firstMove.from.x;
							enemyTeamImageList.get(t).yLocation=firstMove.from.y;
						}
					}
				}*/
				
				if(firstMove.pieceEaten!=null){
					if(firstMove.pieceEaten.chessNumber<6&&teamNo<6||firstMove.pieceEaten.chessNumber>=6&&teamNo>=6){
						teamImageList.add(firstMove.pieceEaten);
					}else{
						enemyTeamImageList.add(firstMove.pieceEaten);
					}
				}
				if(firstMove.to.promotedNumber>=0&&firstMove.to.promotedNumber<11){
					if(afterBoard[firstMove.from.x][firstMove.from.y].chessNumber<6){
						afterBoard[firstMove.from.x][firstMove.from.y].chessNumber=PAWN;
						// afterBoard[firstMove.from.x][firstMove.from.y].chessValue=1;
					}else{
						afterBoard[firstMove.from.x][firstMove.from.y].chessNumber=EPAWN;
					}
					afterBoard[firstMove.from.x][firstMove.from.y].chessValue=1;
				}
				firstMove=firstMove.previousMove;
				futureStepNumber--;
				
			}
			// System.out.println("back to normal position?");
			// printBoard(afterBoard);
			// for(int j=0;j<teamImageList.size();j++){
					// System.out.println("team name: "+teamImageList.get(j).chessNumber+" Team list locations "+teamImageList.get(j).xLocation+" "+teamImageList.get(j).yLocation);
				// }
				// for(int j=0;j<enemyTeamImageList.size();j++){
					// System.out.println("enemy team name: "+enemyTeamImageList.get(j).chessNumber+" enemy Team list locations "+enemyTeamImageList.get(j).xLocation+" "+enemyTeamImageList.get(j).yLocation);
				// }
			// System.out.println(futureStepNumber);
			// printChessSteps(outcomeHistory.get(20).chessMove);
			// strengthPoints--;
			
		}
		System.out.println("Finally");
		ChessMove lastMove=moveRating.get(moveRating.size()-1).chessMove;
		ChessMove moveThatWillTerminateForLoop=moveRating.get(moveRating.size()-1).chessMove;
			// ChessOutcome typicalOutcome=null;
		int winningCount=0;
		int drawingCount=0;
		int losingCount=0;
		int unfinishedCount=0;
		int militaryPoints=0;
		int index=moveRating.size()-1;
		// boolean lastRound
		int u=outcomeHistory.size()-1;
		ChessMove firstM=outcomeHistory.get(u).chessMove;
		// while(outcomeHistory.get(u).chessMove.from.x==firstM.from.x&&outcomeHistory.get(u).chessMove.from.y==firstM.from.y){
			// u--;
		// }
		ChessMove depthIdentifier=copyChessMove(outcomeHistory.get(u).chessMove,true);
		int depth=0;
		while(depthIdentifier!=null){
			System.out.println(depthIdentifier);
			depthIdentifier=depthIdentifier.nextMove;
			depth++;
		}
		for(int v=0;v<outcomeHistory.size();v++){
			ChessMove cm=outcomeHistory.get(v).chessMove;
			
			int k=findIndex(moveRating,cm);
			if(outcomeHistory.get(v).winningCount==1){
				moveRating.get(k).winningCount++;
			}else if(outcomeHistory.get(v).drawingCount==1){
				moveRating.get(k).drawingCount++;
			}else if(outcomeHistory.get(v).losingCount==1){
				moveRating.get(k).losingCount++;
			}else{
				moveRating.get(k).unfinishedCount++;
			}
			moveRating.get(k).averageMilitaryPoints+=outcomeHistory.get(u).averageMilitaryPoints;
			
		}
		for(int j=0;j<moveRating.size();j++){
			System.out.println(moveRating.get(j).chessMove+" "+moveRating.get(j).winningCount+" "+moveRating.get(j).drawingCount+" "+moveRating.get(j).losingCount+" "+moveRating.get(j).unfinishedCount+" "+moveRating.get(j).averageMilitaryPoints);
		}
		ChessOutcome bestOutcome=moveRating.get(0);
		double aWinningCount=(double)(moveRating.get(0).winningCount);
		double aDrawingCount=(double)(moveRating.get(0).drawingCount);
		double aLosingCount=(double)(moveRating.get(0).losingCount);
		double aUnfinishedCount=(double)(moveRating.get(0).unfinishedCount);
		double aTotalOutcomes=winningCount+drawingCount+losingCount;
		double aPercentagePoints=(aWinningCount-aLosingCount)/aTotalOutcomes;
		double aMilitary=100*((double)(moveRating.get(0).averageMilitaryPoints))/aTotalOutcomes;
		int aIndex=0;
		for(int j=0;j<moveRating.size();j++){
			double anotherWinningCount=(double)(moveRating.get(j).winningCount);
			double anotherDrawingCount=(double)(moveRating.get(j).drawingCount);
			double anotherLosingCount=(double)(moveRating.get(j).losingCount);
			double anotherUnfinishedCount=(double)(moveRating.get(j).losingCount);
			double anotherTotalOutcomes=winningCount+drawingCount+losingCount;
			double anotherPercentagePoints=(anotherWinningCount-anotherLosingCount)/anotherTotalOutcomes;
			double anotherMilitary=100*((double)(moveRating.get(j).averageMilitaryPoints))/anotherTotalOutcomes;
			if(anotherPercentagePoints>aPercentagePoints){
				aWinningCount=anotherWinningCount;
				aDrawingCount=anotherDrawingCount;
				aUnfinishedCount=anotherUnfinishedCount;
				aLosingCount=anotherLosingCount;
				aTotalOutcomes=anotherTotalOutcomes;
				aPercentagePoints=anotherPercentagePoints;
				aMilitary=anotherMilitary;
				aIndex=j;
			}else if(anotherPercentagePoints==aPercentagePoints&&anotherMilitary>aMilitary){
				aWinningCount=anotherWinningCount;
				aDrawingCount=anotherDrawingCount;
				aUnfinishedCount=anotherUnfinishedCount;
				aLosingCount=anotherLosingCount;
				aTotalOutcomes=anotherTotalOutcomes;
				aPercentagePoints=anotherPercentagePoints;
				aMilitary=anotherMilitary;
				aIndex=j;
			}
		}
		ChessMove bestM=copyChessMove(moveRating.get(aIndex).chessMove,true);

		System.out.println("Depth: "+depth);
		return moveRating.get(aIndex);
		
	}
	
	// public Object []calculateBestMove3(int teamNo, int strengthPoints){
	
	
	
	// }	
	
	public FinalScore bestMove3(int teamNo, int strengthPoints, int fDepth){
		int savedStrengthPoints=strengthPoints;
		int enemyTeamNo=0;
		if(teamNo<6){
			enemyTeamNo=8;
		}else{
			enemyTeamNo=2;
		}
		ChessOutcome determineBaseCase=null;
		ChessPiece kingDeterminer=null;
		ChessPiece enemyKingDeterminer=null;
		java.util.ArrayList<ChessPiece> teamImageList=null;
		java.util.ArrayList<ChessPiece> enemyTeamImageList=null;
		// Object []bestMoveParameter=new Object[6];
		
		java.util.ArrayList<ChessOutcome> moveRating=new java.util.ArrayList<ChessOutcome>();
		java.util.ArrayList<java.util.ArrayList<Range>> moveRatingIndex=new java.util.ArrayList<java.util.ArrayList<Range>>();
		java.util.ArrayList<ChessOutcome> outcomeHistory=new java.util.ArrayList<ChessOutcome>();
		ChessPiece [][]afterBoard=copyBoard(board);
		// ChessPiece afterBoard=board;
		java.util.ArrayList<ChessPiece> whiteTempList=new java.util.ArrayList<ChessPiece>();
		java.util.ArrayList<ChessPiece> blackTempList=new java.util.ArrayList<ChessPiece>();
		for(int  j=0;j<afterBoard.length;j++){
			for(int k=0;k<afterBoard[j].length;k++){
				if(afterBoard[j][k]!=null){
					if(afterBoard[j][k].chessNumber<6){
						whiteTempList.add(afterBoard[j][k]);
					}else{
						blackTempList.add(afterBoard[j][k]);
					}
					if(afterBoard[j][k].chessNumber==0){
						if(teamNo<6){
							kingDeterminer=afterBoard[j][k];
						}else{
							enemyKingDeterminer=afterBoard[j][k];
						}
					}else if(afterBoard[j][k].chessNumber==6){
						if(teamNo>=6){
							kingDeterminer=afterBoard[j][k];
						}else{
							enemyKingDeterminer=afterBoard[j][k];
						}
					}
				}
			}
		}
		if(teamNo<6){
			// enemyKingDeterminer=enemyKingLocation;
			teamImageList=whiteTempList;
			enemyTeamImageList=blackTempList;
			
		}else{
			// enemyKingDeterminer=kingLocation;
			teamImageList=blackTempList;
			enemyTeamImageList=whiteTempList;
		}


		for(int j=0;j<teamImageList.size();j++){
			java.util.ArrayList<ChessLocation> teamSteps=teamImageList.get(j).getListOfMoves2(afterBoard,teamNo);
			for(int k=0;k<teamSteps.size();k++){
				// ChessLocation firstLocation=teamSt
				ChessMove firstMove=new ChessMove(new ChessLocation(teamImageList.get(j).xLocation,teamImageList.get(j).yLocation),teamSteps.get(k));
				int militaryPoints=0;
				// ChessPiece pieceBeforeEaten=null;
				if(afterBoard[teamSteps.get(k).x][teamSteps.get(k).y]!=null){
					militaryPoints=afterBoard[teamSteps.get(k).x][teamSteps.get(k).y].chessValue;
					firstMove.pieceEaten=afterBoard[teamSteps.get(k).x][teamSteps.get(k).y];
				}
				ChessOutcome outcome=new ChessOutcome(firstMove,0,0,1,0,militaryPoints,null);
				
				int resultIs=willCheckMateEnemyKing2(enemyKingDeterminer, afterBoard[teamImageList.get(j).xLocation][teamImageList.get(j).yLocation], enemyTeamImageList, teamSteps.get(k), afterBoard);
				if(resultIs==1){
					// System.out.println("("+choosersSteps.get(j).x+","+choosersSteps.get(j).y+") will make you win.");
	
					outcome.winningCount=1;
					outcome.unfinishedCount=0;
					// return outcome;
					FinalScore finalS=new FinalScore(firstMove,25+militaryPoints);
					return finalS;
				}else if(resultIs==0){
					outcome.drawingCount=1;
					outcome.unfinishedCount=0;
				}
				moveRating.add(outcome);
				// moveRating.add(new FinalScore(new ChessMove(new ChessLocation(firstMove.from.x,firstMove.from.y),new ChessLocation(firstMove.to.x,firstMove.to.y)),0));
				java.util.ArrayList r=new java.util.ArrayList<Range>();
				r.add(new Range(j*teamImageList.size()+k,j*teamImageList.size()+k,j*teamImageList.size()+k+1,1));
				// moveRatingIndex.add(r);
				outcomeHistory.add(outcome);
				strengthPoints--;
			}
		}
		int futureStepNumber=stepNumber;
		// System.out.println("Started");
		boolean done=false;
		int tDepth=1;
		while(strengthPoints>0||tDepth<=fDepth){
		
			//my implementation here.
			ChessMove countMove=copyChessMove(outcomeHistory.get(0).chessMove,true);
			int countDepth=1;
			while(countMove.nextMove!=null){
				countMove=countMove.nextMove;
				countDepth++;
			}
			
			// if(tDepth<countDepth
			tDepth=countDepth;
			if(tDepth==fDepth&&strengthPoints<=0){
				break;
			}
			//end my implementation here.
		
			ChessOutcome currentOutcome=outcomeHistory.remove(0);
		
			if(currentOutcome.unfinishedCount==1){
				determineBaseCase=currentOutcome;
			}else if(determineBaseCase!=null){
				if(determineBaseCase.equals(currentOutcome)){
					break;
				}else{
					determineBaseCase=currentOutcome;
				}
			}
			
			if(currentOutcome.winningCount==1||currentOutcome.drawingCount==1||currentOutcome.losingCount==1){
				outcomeHistory.add(currentOutcome);
				
				continue;
			}
		
			ChessMove firstMove=currentOutcome.chessMove;
			

			
			// ChessMove typical=copyChessMove(firstMove);
			// printChessSteps(typical);
			if(teamNo<6){
				futureStepNumber=moveToCurrentStepNumber(futureStepNumber, teamImageList, enemyTeamImageList, firstMove, afterBoard, true);
			}else{
				futureStepNumber=moveToCurrentStepNumber(futureStepNumber, enemyTeamImageList, teamImageList, firstMove, afterBoard, true);
			}
			
			
			ChessMove next=null;
			int upto=outcomeHistory.size();
			
			if(futureStepNumber%2==0&&teamNo<6||futureStepNumber%2==1&&teamNo>=6){
				boolean isBreaking=false;
				int highestMilitaryPoints=0;
				boolean firstTime=true;
				// int teamIndex=outcomeHistory.size();
				for(int j=0;j<teamImageList.size();j++){
				
					java.util.ArrayList<ChessLocation> teamSteps=teamImageList.get(j).getListOfMoves2(afterBoard,teamNo);
					
				// if(strengthPoints==2890){
					// System.out.println(upto);
				// }
				
					for(int k=0;k<teamSteps.size();k++){
					// if(teamSteps.get(k).y==0&&teamImageList.get(j).chessValue==1){
						// System.out.println("finding error "+teamSteps.get(k).promotedNumber);
					// }
						int militaryPoints=currentOutcome.averageMilitaryPoints;
						
						
						ChessMove savedMove=copyChessMove(firstMove,true);
						while(savedMove.nextMove!=null){
							savedMove=savedMove.nextMove;
						}
						next=new ChessMove(new ChessLocation(teamImageList.get(j).xLocation,teamImageList.get(j).yLocation),teamSteps.get(k));
						savedMove.nextMove=next;
						next.previousMove=savedMove;
						int resultIs=willCheckMateEnemyKing2(enemyKingDeterminer, afterBoard[teamImageList.get(j).xLocation][teamImageList.get(j).yLocation], enemyTeamImageList, teamSteps.get(k), afterBoard);
						// if(resultIs==1){
							// System.out.println("Checks enemy king at "+next);
							// printBoard(afterBoard);
						// }
						if(afterBoard[teamSteps.get(k).x][teamSteps.get(k).y]!=null){
						// System.out.println("enemy : "+afterBoard[teamSteps.get(k).x][teamSteps.get(k).y]);
							next.pieceEaten=afterBoard[teamSteps.get(k).x][teamSteps.get(k).y];
							militaryPoints+=next.pieceEaten.chessValue;
							if(firstTime||highestMilitaryPoints<militaryPoints){
								highestMilitaryPoints=militaryPoints;
								firstTime=false;
							}
						}
						boolean doubleThreat=numberOfChecks(next, teamImageList, enemyTeamImageList,afterBoard)>0&&(tDepth<fDepth-1);
						// if(doubleThreat){
							// next.to.promotedNumber=11;
						// }
						if(!firstTime&&highestMilitaryPoints>militaryPoints&&resultIs!=1&&!doubleThreat){
							
							continue;
						}
						while(next.previousMove!=null){
							next=next.previousMove;
						}
						ChessOutcome outcome=new ChessOutcome(next,0,0,0,0,militaryPoints,null);
						// if(militaryPoints>0){
							// System.out.println(militaryPoints+" "+outcome.averageMilitaryPoints);
						// }
						int index=findIndex(moveRating,next);
						if(resultIs==-1){
							outcome.unfinishedCount=1;
							moveRating.get(index).unfinishedCount++;
						}else if(resultIs==0){
							outcome.drawingCount=1;
							moveRating.get(index).drawingCount++;
						}else if(resultIs==1){
							outcome.winningCount=1;
							moveRating.get(index).winningCount++;
							outcomeHistory.subList(upto,outcomeHistory.size()).clear();
							outcomeHistory.add(outcome);
							isBreaking=true;
							strengthPoints--;
							break;
						}
						
						outcomeHistory.add(outcome);
						strengthPoints--;
					}
					if(isBreaking){
						break;
					}
				}
				
				for(int j=upto;j<outcomeHistory.size()&&!firstTime;j++){
					if(outcomeHistory.get(j).averageMilitaryPoints<highestMilitaryPoints&&outcomeHistory.get(j).chessMove.to.promotedNumber!=11){
						outcomeHistory.remove(j);
						j--;
					}
				}
				
			}else{
				// int enemyTeamIndex=outcomeHistory.size();
				int highestEnemyMilitaryPoints=0;
				boolean firstTime=true;
				for(int j=0;j<enemyTeamImageList.size();j++){
					java.util.ArrayList<ChessLocation> teamSteps=enemyTeamImageList.get(j).getListOfMoves2(afterBoard,enemyTeamNo);
					for(int k=0;k<teamSteps.size();k++){
					int militaryPoints=currentOutcome.averageMilitaryPoints;
						
					
						
						
						ChessMove savedMove=copyChessMove(firstMove,true);
						while(savedMove.nextMove!=null){
							savedMove=savedMove.nextMove;
						}
						next=new ChessMove(new ChessLocation(enemyTeamImageList.get(j).xLocation,enemyTeamImageList.get(j).yLocation),teamSteps.get(k));
						savedMove.nextMove=next;
						next.previousMove=savedMove;
						
						
						int resultIs=willCheckMateEnemyKing2(kingDeterminer, afterBoard[enemyTeamImageList.get(j).xLocation][enemyTeamImageList.get(j).yLocation], teamImageList, teamSteps.get(k), afterBoard);
						
						if(afterBoard[teamSteps.get(k).x][teamSteps.get(k).y]!=null){
							next.pieceEaten=afterBoard[teamSteps.get(k).x][teamSteps.get(k).y];
							militaryPoints-=next.pieceEaten.chessValue;
							if(firstTime||highestEnemyMilitaryPoints>militaryPoints){
								highestEnemyMilitaryPoints=militaryPoints;
								firstTime=false;
							}
						}
						boolean doubleThreat=numberOfChecks(next, enemyTeamImageList, teamImageList,afterBoard)>0&&(tDepth<fDepth-1);
						// if(doubleThreat){
							// next.to.promotedNumber=11;
						// }
						if(!firstTime&&highestEnemyMilitaryPoints<militaryPoints&&resultIs!=1&&!doubleThreat){
							continue;
						}
						while(next.previousMove!=null){
							next=next.previousMove;
						}
						ChessOutcome outcome=new ChessOutcome(next,0,0,0,0,militaryPoints,null);
						int index=findIndex(moveRating,next);
						if(resultIs==-1){
							outcome.unfinishedCount=1;
							moveRating.get(index).unfinishedCount++;
						}else if(resultIs==0){
							moveRating.get(index).drawingCount++;
							outcome.drawingCount=1;
						}else if(resultIs==1){
							moveRating.get(index).losingCount++;
							outcome.losingCount=1;
						}
						
						outcomeHistory.add(outcome);
						strengthPoints--;
					}
				}
				for(int j=upto;j<outcomeHistory.size()&&!firstTime;j++){
					if(outcomeHistory.get(j).averageMilitaryPoints>highestEnemyMilitaryPoints&&outcomeHistory.get(j).chessMove.to.promotedNumber!=11){
						outcomeHistory.remove(j);
						j--;
					}
				}

			}
			
			//after examination
			while(firstMove.nextMove!=null){
				firstMove=firstMove.nextMove;
			}
			if(teamNo<6){
				futureStepNumber=moveToCurrentStepNumber(futureStepNumber, teamImageList, enemyTeamImageList, firstMove, afterBoard, false);
			}else{
				futureStepNumber=moveToCurrentStepNumber(futureStepNumber, enemyTeamImageList, teamImageList, firstMove, afterBoard, false);
			
			}
			// if(strengthPoints>80000){
				// for(ChessPiece p:teamImageList){
					// if(p.chessNumber>=6||teamImageList.size()>16){
						// System.out.println(strengthPoints);
						// for(ChessPiece t:teamImageList){
							// System.out.println(t.chessNumber+" "+t.xLocation+" "+t.yLocation);
						// }
						// ChessMove movecopy=copyChessMove(outcomeHistory.get(outcomeHistory.size()-1).chessMove);
						// printChessSteps(movecopy);
					// }
					// break;
				// }
			// }
			// for(ChessPiece p: enemyTeamImageList){
			// System.out.println();
				// if(p.chessNumber<6){
				// System.out.println("After: ");
				// System.out.println("Team:");
				// for(ChessPiece t:teamImageList){
						// System.out.println(t.chessNumber+" "+t.xLocation+" "+t.yLocation);
					// }
					// System.out.println();
					// System.out.println("enemy Team:");
					// for(ChessPiece t:enemyTeamImageList){
						// System.out.println(t.chessNumber+" "+t.xLocation+" "+t.yLocation);
					// }
					// break;
				// }
			// }
			// if(strengthPoints>=50000&&strengthPoints<60000){
			/*if(strengthPoints==2890){
			
			for(ChessPiece p: teamImageList){
				if(p.chessNumber>=6){
				System.out.println(strengthPoints);
				System.out.println("After: ");
				System.out.println("Team:");
				for(ChessPiece t:teamImageList){
						System.out.println(t.chessNumber+" "+t.xLocation+" "+t.yLocation);
					}
					System.out.println();
					System.out.println("enemy Team:");
					for(ChessPiece t:enemyTeamImageList){
						System.out.println(t.chessNumber+" "+t.xLocation+" "+t.yLocation);
					}
					ChessMove doMove=copyChessMove(outcomeHistory.get(outcomeHistory.size()-1).chessMove);
					while(doMove!=null){
						System.out.print(doMove+" ");
						doMove=doMove.nextMove;
					}
					System.out.println("first move");
					while(firstMove!=null){
						System.out.print(firstMove+" ");
						firstMove=firstMove.previousMove;
					}
					System.out.println();
					break;
				}
			}
			}*/
			
			
		}
		if(savedStrengthPoints>0){
			for(int j=outcomeHistory.size()-1;j>0;j--){
				int cDepth=0;
				ChessMove move=copyChessMove(outcomeHistory.get(j).chessMove,true);
				while(move!=null){
					move=move.nextMove;
					cDepth++;
				}
				if(cDepth<tDepth){
					outcomeHistory.subList(j+1,outcomeHistory.size()).clear();
					break;
				}
			}
			
		}
				
		// if(tDepth>fDepth){
			
			// outcomeHistory.remove(outcomeHistory.size()-1);
		// }
		System.out.println("Finally: "+outcomeHistory.size());
		int winningCount=0;
		int drawingCount=0;
		int losingCount=0;
		int unfinishedCount=0;
		int militaryPoints=0;
		// int index=moveRating.size()-1;
		// boolean lastRound
		int u=outcomeHistory.size()-1;
		
		ChessMove depthIdentifier=copyChessMove(outcomeHistory.get(u).chessMove,true);
		int depth=0;
		while(depthIdentifier!=null){
			System.out.println(depthIdentifier);
			depthIdentifier=depthIdentifier.nextMove;
			depth++;
		}
		System.out.println("Depth :"+depth);
		java.util.ArrayList<FinalScore> finalScore=new java.util.ArrayList<FinalScore>();
		for(int t=0;t<moveRating.size();t++){
			System.out.println("Move rating: "+moveRating.get(t).chessMove);
		}
		for(int v=0;v<outcomeHistory.size();v++){
			
			ChessMove cm=outcomeHistory.get(v).chessMove;
			int l=findIndex(moveRating,cm);
			if(l<0){
				System.out.println("invalid "+cm);
			}
			int k=findIndex2(finalScore,cm);
			// if(k>=0||cm!=null){
				double score=((double)(moveRating.get(l).winningCount-moveRating.get(l).losingCount))/((double)(moveRating.get(l).losingCount+moveRating.get(l).drawingCount+moveRating.get(l).winningCount+moveRating.get(l).unfinishedCount));
				score*=25;
				score+=outcomeHistory.get(v).averageMilitaryPoints;
				if(k<0){
					finalScore.add(new FinalScore(new ChessMove(cm.from,cm.to),score));
				}else if(score>finalScore.get(k).fScore){
					finalScore.get(k).fScore=score;
					// moveRating.get(k).firstTime=false;
				}
			// }
			// System.out.println(v);
			// if(outcomeHistory.get(v).winningCount==1){
				// moveRating.get(k).winningCount++;
			// }else if(outcomeHistory.get(v).drawingCount==1){
				// moveRating.get(k).drawingCount++;
			// }else if(outcomeHistory.get(v).losingCount==1){
				// moveRating.get(k).losingCount++;
			// }else{
				// moveRating.get(k).unfinishedCount++;
			// }
		
	
			// if(moveRating.get(k).averageMilitaryPoints>outcomeHistory.get(v).averageMilitaryPoints){
				// moveRating.get(k).averageMilitaryPoints=outcomeHistory.get(v).averageMilitaryPoints;
			// }
			// if(outcomeHistory.get(v).averageMilitaryPoints>0){
				// System.out.println(k+" Military: "+outcomeHistory.get(v).averageMilitaryPoints+" "+moveRating.get(k).averageMilitaryPoints);
			// }
			
		}
		for(int j=0;j<moveRating.size();j++){
		int f=findIndex2(finalScore,moveRating.get(j).chessMove);
			if(f>=0){
				System.out.println(moveRating.get(j).chessMove+" "+moveRating.get(j).winningCount+" "+moveRating.get(j).drawingCount+" "+moveRating.get(j).unfinishedCount+" "+moveRating.get(j).losingCount+" "+finalScore.get(f).fScore);//winningCount+" "+moveRating.get(j).drawingCount+" "+moveRating.get(j).losingCount+" "+moveRating.get(j).unfinishedCount+" "+moveRating.get(j).averageMilitaryPoints);
			}
		}
		/*ChessOutcome bestOutcome=moveRating.get(0);
		double aWinningCount=(double)(moveRating.get(0).winningCount);
		double aUnfinishedCount=(double)(moveRating.get(0).unfinishedCount);
		double aDrawingCount=(double)(moveRating.get(0).drawingCount);
		double aLosingCount=(double)(moveRating.get(0).losingCount);
		
		double aTotalOutcomes=aWinningCount+aDrawingCount+aLosingCount+aUnfinishedCount;
		double aPercentagePoints=(aWinningCount-aLosingCount)/aTotalOutcomes;
		int aMilitary=moveRating.get(0).averageMilitaryPoints;//((double)(moveRating.get(0).averageMilitaryPoints))/aTotalOutcomes;
		aPercentagePoints*=25;
		aPercentagePoints+=aMilitary;
		*/
		int aIndex=0;
		java.util.ArrayList<FinalScore> listOfEquivalentScores=new java.util.ArrayList<FinalScore>();
		
		for(int j=0;j<finalScore.size();j++){
			if(finalScore.get(aIndex).fScore<finalScore.get(j).fScore){
				aIndex=j;
				listOfEquivalentScores.clear();
				listOfEquivalentScores.add(finalScore.get(aIndex));
			}else if(finalScore.get(aIndex).fScore==finalScore.get(j).fScore){
				listOfEquivalentScores.add(finalScore.get(j));
			}
			/*double anotherWinningCount=(double)(moveRating.get(j).winningCount);
			double anotherDrawingCount=(double)(moveRating.get(j).drawingCount);
			double anotherLosingCount=(double)(moveRating.get(j).losingCount);
			double anotherUnfinishedCount=(double)(moveRating.get(j).unfinishedCount);
			
			double anotherTotalOutcomes=anotherWinningCount+anotherDrawingCount+anotherLosingCount+anotherUnfinishedCount;
			double anotherPercentagePoints=(anotherWinningCount-anotherLosingCount)/anotherTotalOutcomes;
			
			int anotherMilitary=moveRating.get(j).averageMilitaryPoints;//((double)(moveRating.get(j).averageMilitaryPoints))/anotherTotalOutcomes;
			anotherPercentagePoints*=25;
			anotherPercentagePoints+=anotherMilitary;
			System.out.println(moveRating.get(j).chessMove+" "+anotherPercentagePoints+" "+anotherMilitary);
			if(anotherPercentagePoints>aPercentagePoints){
				aWinningCount=anotherWinningCount;
				aDrawingCount=anotherDrawingCount;
				aUnfinishedCount=anotherUnfinishedCount;
				aLosingCount=anotherLosingCount;
				aTotalOutcomes=anotherTotalOutcomes;
				aPercentagePoints=anotherPercentagePoints;
				aMilitary=anotherMilitary;
				aIndex=j;
			}*/
			// else if(anotherPercentagePoints==aPercentagePoints&&anotherMilitary>aMilitary){
				// aWinningCount=anotherWinningCount;
				// aDrawingCount=anotherDrawingCount;
				// aUnfinishedCount=anotherUnfinishedCount;
				// aLosingCount=anotherLosingCount;
				// aTotalOutcomes=anotherTotalOutcomes;
				// aPercentagePoints=anotherPercentagePoints;
				// aMilitary=anotherMilitary;
				// aIndex=j;
			// }
		}
		// ChessMove bestM=copyChessMove(moveRating.get(aIndex).chessMove);

		// System.out.println("Depth: "+depth);
		// printChessSteps(depthIdentifier);
		return listOfEquivalentScores.get((int)(Math.random()*listOfEquivalentScores.size()));
		// return finalScore.get(aIndex);
	}
	public Object []calculateBestMove(ChessOutcome outcome, int strengthPoints, Object []array){
	
		
		int originalStep=stepNumber;
		stepNumber++;
		return array;
	}
	// java.util.ArrayList<ChessPiece> enemyList,java.util.ArrayList<ChessPiece> teamList, ChessPiece king, ChessPiece enemyKing, ChessPiece [][]afterBoard, int militaryKilled, int depth){
	public FinalScore MinMax(ChessMove move,java.util.ArrayList<ChessPiece> enemyList, java.util.ArrayList<ChessPiece> teamList, ChessPiece king, ChessPiece enemyKing, ChessPiece [][]afterBoard, int militaryKilled, int depth){//GamePosition game) {
		// return MaxMove (game);
		return MaxMove(move, enemyList,teamList, king, enemyKing, afterBoard, militaryKilled, depth);
	}
	
	public FinalScore MaxMove(ChessMove move, java.util.ArrayList<ChessPiece> enemyList,java.util.ArrayList<ChessPiece> teamList, ChessPiece king, ChessPiece enemyKing, ChessPiece [][]afterBoard, int militaryKilled, int depth){//GamePosition game) {
		System.out.println(depth);
		int resultIs=-1;
		if(move!=null){
			resultIs=willCheckMateEnemyKing2(enemyKing, afterBoard[move.from.x][move.from.y], enemyList, new ChessLocation(move.to.x,move.to.y), afterBoard);
		// }
			if(afterBoard[move.to.x][move.to.y]!=null){
				militaryKilled+=afterBoard[move.to.x][move.to.y].chessValue;
			}
		}
		if (resultIs==1){//if(GameEnded(game)||DepthLimitReached()) {
			return new FinalScore(move,25+militaryKilled);//EvalGameState(game, MAX);
			// win.military=militaryKilled;
		}
		
		if(resultIs==0||depth==0){
			return new FinalScore(move,militaryKilled);
		}
		
		else {
			// best_move <- {};
			FinalScore bestMove=null;
			
			if(move!=null){
				movePiece(move,enemyList,afterBoard,true);//move <- MinMove(ApplyMove(game));
			}
			for(int j=0;j<teamList.size();j++){
				java.util.ArrayList<ChessLocation> teamSteps=teamList.get(j).getListOfMoves();//moves <- GenerateMoves(game);
				for(int k=0;k<teamSteps.size();k++){ //ForEach moves {
					
					FinalScore typicalMove=MinMove(new ChessMove(new ChessLocation(teamList.get(j).xLocation,teamList.get(j).yLocation),teamSteps.get(k)), teamList, enemyList, enemyKing, king, afterBoard, militaryKilled, depth-1);
					if (bestMove==null||typicalMove.fScore<bestMove.fScore){//>Value(move) > Value(best_move)) {
						bestMove=typicalMove;//best_move <- move;
					}
					// movePiece(teamSteps.get(k),enemyList,false,afterBoard);
				}
			}
			if(move!=null){
				movePiece(move,enemyList,afterBoard,false);
			}
			return bestMove;//best_move;
		}
	}
	
	public FinalScore MinMove(ChessMove move, java.util.ArrayList<ChessPiece> enemyList,java.util.ArrayList<ChessPiece> teamList, ChessPiece king, ChessPiece enemyKing, ChessPiece [][]afterBoard, int militaryKilled, int depth){
		
		
		// if (GameEnded(game) || DepthLimitReached()) {
			// return EvalGameState(game, MIN);
			
		int resultIs=willCheckMateEnemyKing2(enemyKing, afterBoard[move.from.x][move.from.y], enemyList, new ChessLocation(move.to.x,move.to.y), afterBoard);
		if(afterBoard[move.to.x][move.to.y]!=null){
			militaryKilled+=afterBoard[move.to.x][move.to.y].chessValue;
		}
		if (resultIs==1){//if(GameEnded(game)||DepthLimitReached()) {
			return new FinalScore(move,militaryKilled+25);//EvalGameState(game, MAX);
			// win.military=militaryKilled;
		}
		
		if(resultIs==0||depth==0){
			return new FinalScore(move,militaryKilled);
		
		}else {
			FinalScore bestMove=null;
			
			movePiece(move,enemyList,afterBoard,true);//move <- MinMove(ApplyMove(game));
			for(int j=0;j<teamList.size();j++){
				java.util.ArrayList<ChessLocation> teamSteps=teamList.get(j).getListOfMoves();//moves <- GenerateMoves(game);
				for(int k=0;k<teamSteps.size();k++){ //ForEach moves {
					
					// FinalScore move=MaxMove(teamSteps.get(k), enemyList, teamList, king, enemyKing, afterBoard, militaryKilled, depth-1);
					FinalScore finalMove=MaxMove(new ChessMove(new ChessLocation(teamList.get(j).xLocation,teamList.get(j).yLocation),teamSteps.get(k)), teamList, enemyList, enemyKing, king, afterBoard, militaryKilled, depth-1);
					if (bestMove==null||finalMove.fScore>bestMove.fScore){//>Value(move) > Value(best_move)) {
						bestMove=finalMove;//best_move <- move;
					}
					// movePiece(teamSteps.get(k),teamList,false,afterBoard);
				}
			}
			// movePiece(move,teamList,true,afterBoard)
			movePiece(move,enemyList,afterBoard,false);
			return bestMove;//best_move;
			
			// best_move <- {};
			// moves <- GenerateMoves(game);
			// ForEach moves {
				// move <- MaxMove(ApplyMove(game));
				// if (Value(move) > Value(best_move)) {
					// best_move <- move;
				// }
			// }
			// return best_move;
		}
	}
	int nodesVisited=0;
	
	public ChessOutcome evaluateOutcome(ChessPiece attacker,ChessPiece [][]afterBoard,ChessLocation nextLocation, ChessMove currentMove,ChessPiece enemyKing,java.util.ArrayList<ChessPiece> enemyTeam, int originalDepth, int depth){
		ChessOutcome typicalOutcome=new ChessOutcome(null,0,0,0,0,0,null);
		if(afterBoard[nextLocation.x][nextLocation.y]!=null){
			typicalOutcome.averageMilitaryPoints=afterBoard[nextLocation.x][nextLocation.y].chessValue;
			// nextLocation.get(k).pieceEaten=afterBoard[nextLocation.get(k).x][nextLocation.get(k).y];
		}
		if(nextLocation.promotedNumber>=0){
			// currentOutcome.averageMilitaryPoints+
			
			if(nextLocation.promotedNumber%6==1){
				typicalOutcome.averageMilitaryPoints+=8;
			}else if(nextLocation.promotedNumber%6==2){
				typicalOutcome.averageMilitaryPoints+=2;
			}else if(nextLocation.promotedNumber%6==3){
				typicalOutcome.averageMilitaryPoints+=4;
			}else{
				typicalOutcome.averageMilitaryPoints+=2;
			}
			
		}
		// }
		typicalOutcome.score=typicalOutcome.averageMilitaryPoints;
		typicalOutcome.chessMove=new ChessMove(new ChessLocation(attacker.xLocation,attacker.yLocation),nextLocation);
		typicalOutcome.chessMove.pieceEaten=afterBoard[nextLocation.x][nextLocation.y];
				// if(depth==1&&typicalOutcome.chessMove.pieceEaten!=null&&team.get(j).chessValue>typicalOutcome.chessMove.pieceEaten.chessValue&&enemyCanAttackYouThere(typicalOutcome.chessMove,enemyTeamNo,enemyTeam,afterBoard)){
					// typicalOutcome.score=0;
				// }
				
				int resultIs=willCheckMateEnemyKing2(enemyKing, afterBoard[attacker.xLocation][attacker.yLocation], enemyTeam, nextLocation, afterBoard);
				if(resultIs==1){
					// if(predictedMoves.previousMove!=null){
						// predictedMoves=predictedMoves.previousMove;
						// predictedMoves.nextMove=null;
					// }else{
						// predictedMoves=null;
					// }
					// if(scalar==1){
					typicalOutcome.winningCount=1;
					typicalOutcome.score+=25;

					return typicalOutcome;
				}else if(resultIs==0){
					typicalOutcome.drawingCount=1;
					typicalOutcome.score-=16;

				}else{
					typicalOutcome.unfinishedCount=1;
				}
				if(originalDepth-depth>=4&&resultIs!=1){
					ChessMove threeStepsBefore=copyChessMove(currentMove,false);
					ChessMove oneStepBefore=copyChessMove(currentMove,false);
					while(threeStepsBefore.nextMove!=null){
						threeStepsBefore=threeStepsBefore.nextMove;
						oneStepBefore=oneStepBefore.nextMove;
					}
					threeStepsBefore=threeStepsBefore.previousMove;
					threeStepsBefore=threeStepsBefore.previousMove;
					if(threeStepsBefore.from.x==oneStepBefore.from.x&&threeStepsBefore.from.y==oneStepBefore.from.y&&threeStepsBefore.to.x==oneStepBefore.to.x&&threeStepsBefore.to.y==oneStepBefore.to.y){
						if(threeStepsBefore.nextMove.from.x==nextLocation.x&&threeStepsBefore.nextMove.to.y==nextLocation.y){
							typicalOutcome.unfinishedCount=0;
							typicalOutcome.drawingCount=1;
							typicalOutcome.score-=16;
						}
					}
				}
				
		return typicalOutcome;
	}
	// double accumulateScores(ChessMove myMoveHistory, boolean maximizingPlayer){
		
	// }	

	public ChessOutcome alphabeta(int node, ChessOutcome A, ChessOutcome B, boolean maximizingPlayer, int teamNo, int originalDepth, int depth,java.util.ArrayList<ChessPiece> team, java.util.ArrayList<ChessPiece> enemyTeam, ChessPiece [][]afterBoard,ChessMove currentMove, double currentScore,ChessMove beginningMove){
		ChessOutcome A1=new ChessOutcome(null,0,0,0,0,0,null);
		A1.score=A.score;
		
		ChessOutcome B1=new ChessOutcome(null,0,0,0,0,0,null);
		B1.score=B.score;
      
      // if depth = 0 or node is a terminal node
	  // double myCurrentScore=currentScore;
	  // if(Math.abs(myCurrentScore)>500){
		  // myCurrentScore=0;
	  // }
	  nodesVisited++;
// System.out.println("depth: "+depth);
      if(depth==0){
		  // while(currentMove.previousMove!=null){
			  // currentMove=currentMove.previousMove;
		  // }
		  // if(currentMove.from.x==0&&currentMove.from.y==6&&currentMove.to.x==0&&currentMove.to.y==5){
		  // System.out.println("start history");
			  
		  // printMoveHistory(currentMove);
		  // System.out.println("Score: "+currentScore);
		  // System.out.println("end history");
		  // }
		  // while(currentMove.nextMove!=null){
			  // currentMove=currentMove.nextMove;
		  // }
		  // ChessMove c1=new ChessMove(new ChessLocation(0,6),new ChessLocation(0,5));
		  // ChessMove c2=new ChessMove(new ChessLocation(6,4),new ChessLocation(4,4));
		  // ChessMove c3=new ChessMove(new ChessLocation(3,7),new ChessLocation(4,6));
		  // ChessMove c4=new ChessMove(new ChessLocation(4,4),new ChessLocation(4,6));
		  // c1.nextMove=c2;
		  // c2.previousMove=c1;
		  
		  // c2.nextMove=c3;
		  // c3.previousMove=c2;
		  
		  // c3.nextMove=c4;
		  // c4.previousMove=c3;
		  
		  // if(moveHistoriesAreEqual(currentMove,c4)){
			  // System.out.println("my current score: "+currentScore);
		  // }
	  
		  // System.out.println("depth: "+depth);
		  // printMoveHistory(currentMove);
		  // System.out.println("Score: "+currentScore);
		  // System.out.println(beginningMove+" "+currentMove);
         // return the heuristic value of node
		 // return evaluateOutcome(team.get(j), afterBoard, nextLocation.get(k), currentMove, enemyKing, enemyTeam, originalDepth, depth);
		ChessOutcome v=new ChessOutcome(beginningMove,0,0,0,1,0,copyChessMove(currentMove,false));//,copyChessMove(currentMove));//evaluateOutcome(team.get(j), afterBoard, nextLocation.get(k), currentMove, enemyKing, enemyTeam, originalDepth, depth);
		v.score=currentScore;
		// v.score=currentScore;
		// v.moveHistory=copyChessMove(currentMove,false);
		return v;
	 }
      // if maximizingPlayer
	  int enemyTeamNo=0;
	  if(teamNo==0){
		  enemyTeamNo=6;
	  }
	  ChessPiece enemyKing=null;
	  for(int k=0;k<enemyTeam.size();k++){
		if(enemyTeam.get(k)!=null&&(enemyTeam.get(k).chessNumber==KING||enemyTeam.get(k).chessNumber==EKING)){
			enemyKing=enemyTeam.get(k);
			break;
		}
	}

		predictedMoves=currentMove;
      if(maximizingPlayer){
		  boolean isBreaking=false;
          // v := -inf
          ChessOutcome v = new ChessOutcome(null,0,0,0,0,-1000,null);
		  v.score=-1000;
          // for each child of node
		  
		  java.util.ArrayList<ChessOutcome> listOfBestOutcomes=new java.util.ArrayList<ChessOutcome>();
			for(int j=0;j<team.size();j++){
				if(team.get(j)==null){
					continue;
				}
				java.util.ArrayList<ChessLocation> nextLocation=team.get(j).getListOfMoves2(afterBoard,teamNo);
			  
				for(int k=0;k<nextLocation.size();k++){
					double myCurrentScore=currentScore;
					ChessMove move=new ChessMove(new ChessLocation(team.get(j).xLocation,team.get(j).yLocation),nextLocation.get(k));
					move.pieceEaten=afterBoard[nextLocation.get(k).x][nextLocation.get(k).y];
					if(depth==originalDepth){
						beginningMove=new ChessMove(move.from,move.to);
						beginningMove.pieceEaten=afterBoard[nextLocation.get(k).x][nextLocation.get(k).y];
					}
					ChessMove tMove=null;
					if(currentMove==null){
						tMove=new ChessMove(move.from,move.to);
						tMove.pieceEaten=move.pieceEaten;
					}else{
						tMove=copyChessMove(currentMove,false);
						tMove.nextMove=new ChessMove(move.from,move.to);
						tMove.nextMove.pieceEaten=move.pieceEaten;
						
						tMove.nextMove.previousMove=tMove;
						tMove=tMove.nextMove;
					}
						
					// if(depth==originalDepth){
						// currentMove=new ChessMove(move.from,move.to);
						// currentMove.pieceEaten=move.pieceEaten;
					// }else{
						// currentMove.nextMove=new ChessMove(move.from,move.to);
						// currentMove.nextMove.pieceEaten=afterBoard[nextLocation.get(k).x][nextLocation.get(k).y];
						// currentMove.nextMove.previousMove=currentMove;
						// currentMove=currentMove.nextMove;
						
					// }
					// printMoveHistory(currentMove);
					  double fruitValue=0;
					  // if(team.get(j).chessValue==1){
						  // fruitValue+=0.2;
					  // }
					  boolean enemyCanAttackYouAtLastMove=enemyCanAttackYouThere(move, enemyTeamNo, enemyTeam, afterBoard);
					  ChessPiece victim=afterBoard[nextLocation.get(k).x][nextLocation.get(k).y];
				 if(depth>1||depth==1&&(!enemyCanAttackYouAtLastMove||victim!=null&&victim.chessValue>=team.get(j).chessValue)){
				  if(move.pieceEaten!=null){
					 
						fruitValue+=move.pieceEaten.chessValue;
					}
					if(nextLocation.get(k).promotedNumber>0){
						if(nextLocation.get(k).promotedNumber%6==1){
							fruitValue+=8;
						}else if(nextLocation.get(k).promotedNumber%6==2){
							fruitValue+=2;
						}else if(nextLocation.get(k).promotedNumber%6==3){
							fruitValue+=4;
						}else{
							fruitValue+=2;
						}
							// fruitValue+=nextLocation.get(k).promotedNumber;
					}
				 }
					// if(move.pieceEaten!=null){
					if(fruitValue>0){
						// myCurrentScore+=move.pieceEaten.chessValue;
						myCurrentScore+=fruitValue;//move.pieceEaten.chessValue;
					}
					int resultIs=willCheckMateEnemyKing2(enemyKing, afterBoard[team.get(j).xLocation][team.get(j).yLocation], enemyTeam, nextLocation.get(k), afterBoard);
									
					// if(predictedMoves.previousMove!=null){
						// predictedMoves=predictedMoves.previousMove;
						// predictedMoves.nextMove=null;
					// }else{
						// predictedMoves=null;
					// }
					ChessOutcome tempV=null;
					if(resultIs==1){
						v.chessMove=beginningMove;
						v.moveHistory=tMove;
						v.winningCount=1;
						v.score=myCurrentScore+25;

						return v;
					}else if(resultIs==0){
						tempV=new ChessOutcome(beginningMove,0,1,0,0,0,tMove);
						// v.drawingCount=1;
						tempV.score=myCurrentScore-16;

					// }else{
					}
					// myCurrentScore=v.score;
				  // ChessOutcome tempV=evaluateOutcome(team.get(j), afterBoard, nextLocation.get(k), currentMove, enemyKing, enemyTeam, originalDepth, depth);
				  
				  
				  //v=evaluateOutcome(team.get(j), afterBoard, nextLocation.get(k), currentMove, enemyKing, enemyTeam, originalDepth, depth);
              // v := max(v, alphabeta(child, originalDepth, depth-1, A, B, FALSE))
			  						movePiece(move, enemyTeam, afterBoard, true);
			
			  if(tempV==null){
				  tempV=alphabeta(node, A, B, false, enemyTeamNo,originalDepth, depth-1,enemyTeam,team,afterBoard,tMove,myCurrentScore,beginningMove);
			  }
			  // ChessOutcome tempV=alphabeta(node, A, B, false, enemyTeamNo,originalDepth, depth-1,enemyTeam,team,afterBoard,tMove,v.score,beginningMove);
										movePiece(move, enemyTeam, afterBoard, false);
				// System.out.println("Depth="+depth+" move: "+move);
			// if(v==null||tempV.score==v.score){
				// listOfBestOutcomes.push(tempV);
			// }else if(tempV.score>v.score){
				// listOfBestOutcomes.clear();
				// listOfBestOutcomes.push(tempV);
			// }
			if(depth==originalDepth){
					 // printBoard(afterBoard);
						
					  System.out.println("Move "+tempV.chessMove+" Score: "+tempV.score);
					  // System.out.println("Move "+move+" Score: "+tempV.score);
				  }
				if(depth==originalDepth&&tempV.score>v.score){
					listOfBestOutcomes.clear();
					listOfBestOutcomes.add(tempV);
				}else if(depth==originalDepth&&tempV.score==v.score){
					listOfBestOutcomes.add(tempV);
				}
			  v= max(v, tempV);
			  
			  // System.out.println(v.chessMove);
				A1= max(A1, v);
              // if B <= A
				// if(B.score <= A.score){
				if(B1.score < A1.score){
					v.hasBeenTerminated=true;
				// if(B.score < A.score&&depth==originalDepth-1||B.score<=A.score&&depth<originalDepth-1){
					isBreaking=true;
                  // break;
				  
                
				}
					// if(currentMove.previousMove!=null){
				  // currentMove=currentMove.previousMove;
				  // currentMove.nextMove=null;
				 
			  // }
				if(isBreaking){
						  break;
					  }
			  }
			  if(isBreaking){
				  break;
			  }
		  }
		  if(depth==originalDepth){
			  return listOfBestOutcomes.get((int)(Math.random()*listOfBestOutcomes.size()));
		  }
          return v;
      }
          // v := inf
		  boolean isBreaking=false;
           ChessOutcome v = new ChessOutcome(null,0,0,0,0,1000,null);
		   v.score=1000;
          // for each child of node
		  	// printBoard(afterBoard);
			for(int j=0;j<team.size();j++){
				if(team.get(j)==null){
					continue;
				}
			  java.util.ArrayList<ChessLocation> nextLocation=team.get(j).getListOfMoves2(afterBoard,teamNo);
			  // System.out.println("here: "+team.get(j)+" depth="+depth+" "+nextLocation.size());
			  for(int k=0;k<nextLocation.size();k++){
				  
				  double myCurrentScore=currentScore;
				  
				  ChessMove move=new ChessMove(new ChessLocation(team.get(j).xLocation,team.get(j).yLocation),nextLocation.get(k));
				  
				  move.pieceEaten=afterBoard[nextLocation.get(k).x][nextLocation.get(k).y];
				  
				  // if(currentMove==null){
					  
					// currentMove=new ChessMove(move.from,move.to);
					// currentMove.pieceEaten=move.pieceEaten;
					
				  // }else{
					ChessMove tMove=null;
					if(currentMove==null){
						tMove=new ChessMove(move.from,move.to);
						tMove.pieceEaten=move.pieceEaten;
					}else{
						tMove=copyChessMove(currentMove,false);
						tMove.nextMove=new ChessMove(move.from,move.to);
						tMove.nextMove.pieceEaten=move.pieceEaten;
						
						tMove.nextMove.previousMove=tMove;
						tMove=tMove.nextMove;
					}
								   

					  // currentMove.nextMove=new ChessMove(move.from,move.to);
					  // currentMove.nextMove.pieceEaten=afterBoard[nextLocation.get(k).x][nextLocation.get(k).y];
					  // currentMove.nextMove.previousMove=currentMove;
					  // currentMove=currentMove.nextMove;
				  // }
				  // int fruitValue=0;
				 // if(depth==1&&move.pieceEaten!=null&&!enemyCanAttackYouThere(move, enemyTeamNo, enemyTeam, afterBoard)){
					 // fruitValue=move.pieceEaten.chessValue;
				 // }
				  double fruitValue=0;
				  // if(team.get(j).chessValue==1){
						  // fruitValue+=0.2;
					  // }
				  boolean enemyCanAttackYouAtLastMove=enemyCanAttackYouThere(move, enemyTeamNo, enemyTeam, afterBoard);
				  ChessPiece victim=afterBoard[nextLocation.get(k).x][nextLocation.get(k).y];
				 if(depth>1||depth==1&&(!enemyCanAttackYouAtLastMove||victim!=null&&victim.chessValue>=team.get(j).chessValue)){
				  if(move.pieceEaten!=null){
					 
						fruitValue+=move.pieceEaten.chessValue;
					}
					if(nextLocation.get(k).promotedNumber>0){
						if(nextLocation.get(k).promotedNumber%6==1){
							fruitValue+=8;
						}else if(nextLocation.get(k).promotedNumber%6==2){
							fruitValue+=2;
						}else if(nextLocation.get(k).promotedNumber%6==3){
							fruitValue+=4;
						}else{
							fruitValue+=2;
						}
							// fruitValue+=nextLocation.get(k).promotedNumber;
					}
				 }
				 
				  if(fruitValue>0){
					// myCurrentScore-=move.pieceEaten.chessValue;
					myCurrentScore-=fruitValue;
				  }
				  // ChessOutcome tempV=evaluateOutcome(team.get(j), afterBoard, nextLocation.get(k), currentMove, enemyKing, enemyTeam, originalDepth, depth);
					int resultIs=willCheckMateEnemyKing2(enemyKing, afterBoard[team.get(j).xLocation][team.get(j).yLocation], enemyTeam, nextLocation.get(k), afterBoard);
										  
					ChessOutcome tempV=null;
					if(resultIs==1){
						v.losingCount=1;
						v.score=myCurrentScore-25;

						return v;
					}else if(resultIs==0){
						tempV=new ChessOutcome(beginningMove,0,1,0,0,0,tMove);

						tempV.score=myCurrentScore+16;

					}
					// else{
						// v.unfinishedCount=1;
						// v.score=myCurrentScore;
					// }
					
				  // v := min(v, alphabeta(child, originalDepth, depth-1, A,B, TRUE))
				 // if(v.unfinishedCount==1){
					 // myCurrentScore=v.score;
					 
					  movePiece(move, enemyTeam, afterBoard, true);
					 if(tempV==null){
						 tempV=alphabeta(node, A, B, true, enemyTeamNo,originalDepth, depth-1,enemyTeam,team,afterBoard,tMove,myCurrentScore,beginningMove);
					 }
					 movePiece(move, enemyTeam, afterBoard, false);
					 // System.out.println("V: "+v.chessMove+" v score: "+v.score+" tempV: "+tempV.chessMove+" tempV score: "+tempV.score);

					  v= min(v, tempV);
					 // System.out.println("V: "+v.chessMove+" v score: "+v.score+" tempV: "+tempV.chessMove+" tempV score: "+tempV.score);
					  // B := min(B, v)
					  B1= min(B, v);
					  // if B<=A
					  // if(B.score <= A.score){
					  if(B1.score < A1.score){
					// if(B.score < A.score&&depth==originalDepth-1||B.score<=A.score&&depth<originalDepth-1){
							v.hasBeenTerminated=true;
						  isBreaking=true;
						  // break
						  
					  }
				  //}
				  // printMoveHistory(currentMove);
				   // if(currentMove.previousMove!=null){
						  // currentMove=currentMove.previousMove;
						  // currentMove.nextMove=null;
					  // }
					  // System.out.println("reached here "+j+" "+k+" "+isBreaking);
					  if(isBreaking){
						  break;
					  }
			  }
			  if(isBreaking){
				  break;
			  }
			}
				  
          return v;
		}
	
	
	public ChessOutcome max(ChessOutcome outcome1, ChessOutcome outcome2){
		// ChessOutcome moveHist1=outcome1.moveHistory;
		// ChessOutcome moveHist2=outcome2.moveHistory;
		// while(moveHist1.previousMove!=null){
			// moveHist1=moveHist1.previousMove;
		// }
		// while(moveHist2.previousMove!=null){
			// moveHist2=moveHist2.previousMove;
		// }
		// while(moveHist1.nextMove!=null&&moveHist2.nextMove!=null){
			// moveHist1=moveHist1.nextMove;
			// moveHist2=moveHist2.nextMove;
		// }

		if(outcome1.chessMove==null){
			return outcome2;
		}
		if(outcome2.chessMove==null){
			return outcome1;
		}
		ChessOutcome bestOutcome=outcome1;
		if(outcome2.score>outcome1.score){
			bestOutcome=outcome2;
		}
		return bestOutcome;
		
	}
	
	public ChessOutcome min(ChessOutcome outcome1, ChessOutcome outcome2){
				if(outcome1.chessMove==null){
			return outcome2;
		}
		if(outcome2.chessMove==null){
			return outcome1;
		}
		ChessOutcome bestOutcome=outcome1;
		if(outcome2.score<outcome1.score){
			bestOutcome=outcome2;
		}
		return bestOutcome;
		
	}
	
	public ChessOutcome bestMoveUsingMiniMax(int teamNo, int originalDepth, int depth, java.util.ArrayList<ChessPiece> team, java.util.ArrayList<ChessPiece> enemyTeam, ChessPiece [][]afterBoard, ChessMove currentMove,int parentAttackScore,int parentBestScore){
		// int enemy=team.get(0).chessNumber;
		
		// if(numberToPass<-900&&maxOrMin==-1||numberToPass>900&&maxOrMin==1){
			// numberToPass=-numberToPass;
		// }
		
		// ChessOutcome outcome=new ChessOutcome(null,0,0,0,0,0);
		// if(depth==0){
			// return outcome;
		// }
		nodesVisited++;
		int numberToPass=0;
		// int numberToPass=maxOrMinAllowable;
		// if(Math.abs(numberToPass)>900){
			 // numberToPass=-numberToPass;
		 // }
		ChessOutcome typicalOutcome=null;//new ChessOutcome(null,0,0,0,0,0);//new ChessOutcome(ChessMove cm, int wc, int dc, int uf, int lc, int mp){
		ChessOutcome bestOutcome=null;
		ChessPiece enemyKing=null;
		// int scalar=1;
		int enemyTeamNo=0;
		if(teamNo<6){
			enemyTeamNo=6;
		}
		// if(teamNo<6&&enemyTeam.get(0).chessNumber<6||teamNo>=6&&enemyTeam.get(0).chessNumber>=6){
			// scalar=-1;
		// }
		// if(outcome==null){
			
		// }
		//find enemy king.
		// for(int j=0
		for(int k=0;k<enemyTeam.size();k++){
			if(enemyTeam.get(k)!=null&&(enemyTeam.get(k).chessNumber==KING||enemyTeam.get(k).chessNumber==EKING)){
				enemyKing=enemyTeam.get(k);
				break;
			}
		}
		
		java.util.ArrayList<ChessOutcome> listOfBestOutcomes=new java.util.ArrayList<ChessOutcome>();
		boolean isBreaking=false;
		for(int j=0;j<team.size();j++){
			if(team.get(j)==null){
				continue;
			}
			java.util.ArrayList<ChessLocation> nextLocation=team.get(j).getListOfMoves2(afterBoard,teamNo);
			for(int k=0;k<nextLocation.size();k++){
				// if(depth==4&&team.get(j).yLocation==7&&atOriginalPosition(afterBoard)){
				// System.out.println("strange chess number: "+team.get(j).chessNumber+" "+team.get(j).xLocation+" "+team.get(j).yLocation+" "+team.get(j).chessValue+" "+nextLocation.get(k));
					// printBoard(afterBoard);
					// System.out.println();
					
				// }
				// predictedMoves=nextLocation.get(k);
				// for(int d=originalDepth;d>depth;d--){
					// if(predictedMoves==null){
						// predictedMoves=copyChessMove(moveHistory);
						// if(predictedMoves!=null){
							// while(predictedMoves.nextMove!=null){
								// predictedMoves=predictedMoves.nextMove;
							// }
						// }
					// }
					// if(predictedMoves!=null){
						// predictedMoves.nextMove=new ChessMove(new ChessLocation(team.get(j).xLocation,team.get(j).yLocation),nextLocation.get(k));
						// predictedMoves.nextMove.pieceEaten=afterBoard[nextLocation.get(k).x][nextLocation.get(k).y];
						// predictedMoves.nextMove.previousMove=predictedMoves;
						// predictedMoves=predictedMoves.nextMove;
					// }else{
						// predictedMoves=new ChessMove(new ChessLocation(team.get(j).xLocation,team.get(j).yLocation),nextLocation.get(k));
					// }
				//typicalOutcome=new ChessOutcome(null,0,0,0,0,0,null);
				typicalOutcome=evaluateOutcome(team.get(j), afterBoard, nextLocation.get(k), currentMove, enemyKing, enemyTeam, originalDepth, depth);
				/*
				// currentOutcome=new ChessOutcome(new ChessMove(nextLocation.from,nextLocation.to),0,0,1,0,0);
				
				// boolean enemyCanDefendLastNode=!willNotKillKing(enemyKing,null,null,afterBoard);
				// boolean enemyCanDefendLastNode=false;
				// if(depth==1){
					// enemyCanDefendLastNode=!willNotKillKing(team.get(j),team.get(j),nextLocation.get(k),afterBoard);
				// }
				// if(!enemyCanDefendLastNode){
				if(afterBoard[nextLocation.get(k).x][nextLocation.get(k).y]!=null){
					typicalOutcome.averageMilitaryPoints=afterBoard[nextLocation.get(k).x][nextLocation.get(k).y].chessValue;
					// nextLocation.get(k).pieceEaten=afterBoard[nextLocation.get(k).x][nextLocation.get(k).y];
				}
				if(nextLocation.get(k).promotedNumber>=0){
					// currentOutcome.averageMilitaryPoints+
					
					if(nextLocation.get(k).promotedNumber%6==1){
						typicalOutcome.averageMilitaryPoints+=8;
					}else if(nextLocation.get(k).promotedNumber%6==2){
						typicalOutcome.averageMilitaryPoints+=2;
					}else if(nextLocation.get(k).promotedNumber%6==3){
						typicalOutcome.averageMilitaryPoints+=4;
					}else{
						typicalOutcome.averageMilitaryPoints+=2;
					}
					
				}
				// }
				typicalOutcome.score=typicalOutcome.averageMilitaryPoints;
				typicalOutcome.chessMove=new ChessMove(new ChessLocation(team.get(j).xLocation,team.get(j).yLocation),nextLocation.get(k));
				// if(nextLocation.get(k).y==5&&nextLocation.get(k).x==5&&depth==originalDepth){
					// if(team.get(j).chessValue==1){
						// System.out.println("here");
					// }
				// }
				typicalOutcome.chessMove.pieceEaten=afterBoard[nextLocation.get(k).x][nextLocation.get(k).y];
				// if(depth==1&&typicalOutcome.chessMove.pieceEaten!=null&&team.get(j).chessValue>typicalOutcome.chessMove.pieceEaten.chessValue&&enemyCanAttackYouThere(typicalOutcome.chessMove,enemyTeamNo,enemyTeam,afterBoard)){
					// typicalOutcome.score=0;
				// }
				
				int resultIs=willCheckMateEnemyKing2(enemyKing, afterBoard[team.get(j).xLocation][team.get(j).yLocation], enemyTeam, nextLocation.get(k), afterBoard);
				if(resultIs==1){
					// if(predictedMoves.previousMove!=null){
						// predictedMoves=predictedMoves.previousMove;
						// predictedMoves.nextMove=null;
					// }else{
						// predictedMoves=null;
					// }
					// if(scalar==1){
					typicalOutcome.winningCount=1;
					typicalOutcome.score+=25;

					return typicalOutcome;
				}else if(resultIs==0){
					typicalOutcome.drawingCount=1;
					typicalOutcome.score-=16;

				}else{
					typicalOutcome.unfinishedCount=1;
				}
				if(originalDepth>=4&&originalDepth-depth>=4&&resultIs!=1){
					ChessMove threeStepsBefore=copyChessMove(currentMove,false);
					ChessMove oneStepBefore=copyChessMove(currentMove,false);
					while(threeStepsBefore.nextMove!=null){
						threeStepsBefore=threeStepsBefore.nextMove;
						oneStepBefore=oneStepBefore.nextMove;
					}
					threeStepsBefore=threeStepsBefore.previousMove;
					threeStepsBefore=threeStepsBefore.previousMove;
					if(threeStepsBefore.from.x==oneStepBefore.from.x&&threeStepsBefore.from.y==oneStepBefore.from.y&&threeStepsBefore.to.x==oneStepBefore.to.x&&threeStepsBefore.to.y==oneStepBefore.to.y){
						if(threeStepsBefore.nextMove.from.x==nextLocation.get(k).x&&threeStepsBefore.nextMove.to.y==nextLocation.get(k).y){
							typicalOutcome.unfinishedCount=0;
							typicalOutcome.drawingCount=1;
							typicalOutcome.score-=16;
						}
					}
				}
				*/
				ChessOutcome enemyBest=null;

				if(typicalOutcome.unfinishedCount==1){
					ChessMove move=new ChessMove(new ChessLocation(team.get(j).xLocation,team.get(j).yLocation),nextLocation.get(k));

					move.pieceEaten=afterBoard[nextLocation.get(k).x][nextLocation.get(k).y];
					ChessMove tMove=null;
					if(currentMove==null){
						tMove=new ChessMove(move.from,move.to);
						tMove.pieceEaten=move.pieceEaten;
						
					}else{
						tMove=copyChessMove(currentMove,false);
						// while(tMove.nextMove!=null){
							// tMove=tMove.nextMove;
						// }
						tMove.nextMove=new ChessMove(move.from,move.to);
						tMove.nextMove.pieceEaten=move.pieceEaten;
						
						tMove.nextMove.previousMove=tMove;
						tMove=tMove.nextMove;
					}
					
	
					

					// int nextParser=-maxOrMin;
					int scoreBeforeAttacking=(int)(typicalOutcome.score);
					if(depth>1){
						movePiece(move, enemyTeam, afterBoard, true);
						enemyBest=bestMoveUsingMiniMax(enemyTeamNo, originalDepth, depth-1, enemyTeam, team, afterBoard,tMove,scoreBeforeAttacking,numberToPass);
						movePiece(move, enemyTeam, afterBoard,  false);
					}
					
					if(depth==1){
						typicalOutcome.moveHistory=copyChessMove(tMove,true);
					}else{
						
						typicalOutcome.moveHistory=copyChessMove(enemyBest.moveHistory,true);
					}
					

				}
				if(enemyBest!=null){
					typicalOutcome.score-=enemyBest.score;
				}
				// }.
				// double moveScore=
				// moveScore+=typicalOutcome.averageMilitaryPoints;
				
				// if(bestOutcome==null||typicalOutcome.score>bestOutcome.score&&(enemyBest==null||!enemyBest.hasBeenTerminated)){
				if(bestOutcome==null||typicalOutcome.score>bestOutcome.score&&(enemyBest==null||!enemyBest.hasBeenTerminated)){
				// if(bestOutcome==null||typicalOutcome.score>bestOutcome.score){
					// System.out.println("reached here");
					bestOutcome=typicalOutcome;
					// System.out.println("here: "+typicalOutcome.chessMove+" "+enemyBest.score);
					numberToPass=(int)(bestOutcome.score);
					if(originalDepth==depth){
						listOfBestOutcomes.clear();
						listOfBestOutcomes.add(bestOutcome);
					}
					// System.out.println("Depth: "+depth);
					// if(parentAttackScore-numberToPass<=parentBestScore){
						// System.out.println("Depth: "+depth);
					// }
					// if(parentAttackScore-numberToPass<parentBestScore&&depth<originalDepth){
					// if(parentAttackScore-numberToPass<=parentBestScore&&depth<originalDepth-1||parentAttackScore-numberToPass<parentBestScore&&depth==originalDepth-1){
					if((parentAttackScore-numberToPass<=parentBestScore)&&depth<originalDepth){
						bestOutcome.hasBeenTerminated=true;
						isBreaking=true;
						// bestOutcome.score+=1;
					}
					
					// if(parentAttackScore-numberToPass<parentBestScore){
						// isBreaking=true;
						// bestOutcome.score=1;
					// }
					
					// if(maxOrMin==1&&numberToPass<maxOrMinAllowable){
						// isBreaking=true;
						// break;
					// }
					
					// if(maxOrMin==-1&&numberToPass>maxOrMinAllowable){
						// isBreaking=true;
						// break;
					// }
				} else if(originalDepth==depth&&typicalOutcome.score==bestOutcome.score&&!enemyBest.hasBeenTerminated){
					listOfBestOutcomes.add(typicalOutcome);
				}
				if(enemyBest!=null&&enemyBest.hasBeenTerminated&&depth<originalDepth){
					bestOutcome.hasBeenTerminated=true;
					isBreaking=true;
				}
				// if(enemyBest!=null&&enemyBest.hasBeenTerminated&&depth<originalDepth){
					// bestOutcome.hasBeenTerminated=true;
				// }
				if(originalDepth==depth){
					
					double fScore=typicalOutcome.score;
					if(enemyBest.hasBeenTerminated){
						fScore--;
					}
					System.out.println("Move: "+typicalOutcome.chessMove+" Score: "+fScore+" "+!enemyBest.hasBeenTerminated);
				}
					// double bestScore=(bestOutcome.winningCount-bestOutcome.losingCount)*25+bestOutcome.averageMilitaryPoints;
					// if(scalar==1&&bestScore<moveScore||scalar==-1&&bestScore>moveScore){
						// bestOutcome=typicalOutcome;
					// }
				// }
	
				// if(predictedMoves.previousMove!=null){
					// predictedMoves=predictedMoves.previousMove;
					// predictedMoves.nextMove=null;
				// }else{
					// predictedMoves=null;
				// }
				if(isBreaking){
					break;
				}
			}
			if(isBreaking){
				break;
			}
		}
		if(originalDepth!=depth){
			return bestOutcome;
		}
		// else{
			// predictedMoves=null;
		// }
		if(listOfBestOutcomes.size()==1){
			return listOfBestOutcomes.get(0);
		}
		// if(team.size()<5||enemyTeam.size()<5){
			// int index=-1;
			// for(int uv=0;uv<listOfBestOutcomes.size();uv++){
				// if(afterBoard[listOfBestOutcomes.get(uv).chessMove.from.x][listOfBestOutcomes.get(uv).chessMove.from.y].chessValue==1&&(index<0||listOfBestOutcomes.get(index).chessMove.from.y>listOfBestOutcomes.get(uv).chessMove.from.y)){
					// index=uv;
				// }
			// }
			// if(index>=0){
				// return listOfBestOutcomes.get(index);
			// }
		// }
		return listOfBestOutcomes.get((int)(Math.random()*listOfBestOutcomes.size()));
	}
	
	boolean enemyCanAttackYouThere(ChessMove myMove, int enemyTeamNo,java.util.ArrayList<ChessPiece> enemyTeam,ChessPiece [][]afterBoard){
			movePiece(myMove, enemyTeam, afterBoard, true);
			int x=myMove.to.x;
			int y=myMove.to.y;
			boolean isBreaking=false;
			for(int h=0;h<enemyTeam.size();h++){
				// if(enemyTeam[h]!=null){
					if(enemyTeam.get(h)==null){
						continue;
					}
					java.util.ArrayList<ChessLocation> nexLocation=enemyTeam.get(h).getListOfMoves2(afterBoard, enemyTeamNo);
					for(int i=0;i<nexLocation.size();i++){
						if(nexLocation.get(i).x==x&&nexLocation.get(i).y==y){
							isBreaking=true;
							break;
						}
					}
					if(isBreaking){
						break;
					}
				// }
			}
			movePiece(myMove, enemyTeam, afterBoard, false);
			if(isBreaking){
				return true;
			}
			return false;
		}
		
	public ChessOutcome copyChessOutcome(ChessOutcome whatToCopy){
		ChessOutcome outcome=new ChessOutcome(copyChessMove(whatToCopy.chessMove,true),whatToCopy.winningCount,whatToCopy.drawingCount,whatToCopy.unfinishedCount,whatToCopy.losingCount,whatToCopy.averageMilitaryPoints,null);
		outcome.nextOutcomes=copyArray(whatToCopy.nextOutcomes);
		return outcome;
	}
	
	public java.util.ArrayList<ChessOutcome> copyArray(java.util.ArrayList<ChessOutcome> list){
		java.util.ArrayList<ChessOutcome> temp=null;
		if(list!=null){
			temp=new java.util.ArrayList<ChessOutcome>();
			for(ChessOutcome t:list){
				temp.add(copyChessOutcome(t));
			}
			
		}
		return temp;
	}
		
	public boolean moveHistoriesAreEqual(ChessMove mv1,ChessMove mv2){
		int count1=0;
		int count2=0;
		while(mv1.previousMove!=null){
			mv1=mv1.previousMove;
			count1++;
		}
		while(mv2.previousMove!=null){
			mv2=mv2.previousMove;
			count2++;
		}
		boolean isEqual=true;
		// if(count1!=count2){
			// System.out.println("reached here 2");
		// }
		if(count1==count2){
			// if(mv1.from.x==0&&mv1.from.y==6){
				// System.out.println("Reached here");
			// }
					  // ChessMove c1=new ChessMove(new ChessLocation(0,6),new ChessLocation(0,5));
		  // ChessMove c2=new ChessMove(new ChessLocation(6,4),new ChessLocation(4,4));
		  // ChessMove c3=new ChessMove(new ChessLocation(3,7),new ChessLocation(4,6));
		  // ChessMove c4=new ChessMove(new ChessLocation(4,4),new ChessLocation(4,6));
		
			while(mv1.nextMove!=null){
				
				if(mv1.from.x!=mv2.from.x||mv1.from.y!=mv2.from.y||mv1.to.x!=mv2.to.x||mv1.to.y!=mv2.to.y){
					isEqual=false;
				}
				mv1=mv1.nextMove;
				mv2=mv2.nextMove;
			}
			if(mv1.from.x!=mv2.from.x||mv1.from.y!=mv2.from.y||mv1.to.x!=mv2.to.x||mv1.to.y!=mv2.to.y){
					isEqual=false;
				}
			
		}else{
			isEqual=false;
			while(mv1.nextMove!=null){
				mv1=mv1.nextMove;
			}
			while(mv2.nextMove!=null){
				mv2=mv2.nextMove;
			}
		}
		return isEqual;
	}
	
		
			
	/*
	public ChessOutcome startBreadFirstSearch(ChessOutcome outcome, int strengthPoints, int level){
		ChessPiece [][]originalBoard=copyBoard(board);
		int []strength={strengthPoints};
		while (strengthPoints>0){
		
			//check here.
			
			//check left
			
			//check right.
			for(int j=0;j<outcome.futureMoves.size();j++){
				//set board with updated chesspieces' locations.
				calculatePointsByBreadthSearch(outcome.futureMoves.get(j), strength, level);
				strengthPoints=strength[0];
				
			}
			level++;
		}
		return outcome;
	}
	
	public void calculatePointsByBreadthSearch(ChessOutcome outcome, int []strengthPoints,int level){
		
			//check here.
			
			//check left
			
			//check right.
		for(int j=0;j<outcome.futureMoves.size()&&strengthPoints[0]>=0;j++){
			if(level>=0){
				calculatePointsByBreadthSearch(outcome.futureMoves.get(j), strengthPoints,level--);
			}else{
				//do something here.
				// is it your or opponent's team's turn?
				strengthPoints[0]--;
			}
		}
		// return enemyStrengthPoints;
			
	}

	public Object []somethingElse(ChessOutcome outcome){
		int enemyStrengthPoints=0;
		Object []array=new Object[6];
		// int predictingStepNumber=originalStep+1;
		ChessPiece [][]originalBoard=copyBoard(board);
		// ori
		int level=0;
		int startingPoint=0;
		//visit all nodes.
		//visit neighbours of those nodes.
		while(enemyStrengthPoints>=0){
			for(int y=0;y<outcome.futureMoves.size();y++){
			//do something here.
			
				enemyStrengthPoints--;
				if(enemyStrengthPoints<0){
					break;
				}
			}
		}
		return array;
	
	}
	*/
	public ChessPiece [][]copyBoard(ChessPiece [][]board){
		ChessPiece [][]backup=new ChessPiece[8][8];
		for(int l=0;l<board.length;l++){
			for(int m=0;m<board[l].length;m++){
				if(board[l][m]!=null){
				
					backup[l][m]=new ChessPiece(board[l][m].chessNumber,board[l][m].xLocation,board[l][m].yLocation,null);
				}
			}
		}
		return backup;
	}
	public ChessPiece copyChessPiece(ChessPiece x){
		ChessPiece piece=new ChessPiece(x.chessNumber, x.xLocation,x.yLocation, null);
		return piece;
	}
	public ChessMove copyChessMove(ChessMove typicalMove, boolean bringToBeginning){
	if(typicalMove==null){
		return null;
	}
		while(typicalMove.previousMove!=null){
			typicalMove=typicalMove.previousMove;
		}
		ChessLocation from=null;//=new ChessLocation(typicalMove.from.x,typicalMove.from.y);
		ChessLocation to=null;//new ChessLocation(typicalMove.to.x,typicalMove.to.y);
		ChessMove independentMove=null;//new ChessMove(from,to);
		ChessMove backUpMove=null;//independentMove;
		// independentMove.pieceEaten=typicalMove.pieceEaten;
		while(typicalMove.nextMove!=null){
			backUpMove=independentMove;
			
			
			from=new ChessLocation(typicalMove.from.x,typicalMove.from.y);
			to=new ChessLocation(typicalMove.to.x,typicalMove.to.y);
			
			
			
			independentMove=new ChessMove(from,to);
			independentMove.previousMove=backUpMove;
			if(backUpMove!=null){
				backUpMove.nextMove=independentMove;
			}
			// independentMove.previousMove=typicalMove.previousMove;
			independentMove.pieceEaten=typicalMove.pieceEaten;
			to.promotedNumber=typicalMove.to.promotedNumber;
			typicalMove=typicalMove.nextMove;
		}
		
		//execute for final time without losing original version of move.
		backUpMove=independentMove;	
		from=new ChessLocation(typicalMove.from.x,typicalMove.from.y);
		to=new ChessLocation(typicalMove.to.x,typicalMove.to.y);
		independentMove=new ChessMove(from,to);
		independentMove.previousMove=backUpMove;
		if(backUpMove!=null){
			backUpMove.nextMove=independentMove;
		}
		independentMove.pieceEaten=typicalMove.pieceEaten;
		to.promotedNumber=typicalMove.to.promotedNumber;
		// finish executing.
		
		while(bringToBeginning&&independentMove.previousMove!=null){
			independentMove=independentMove.previousMove;
		}
		return independentMove;
	}
	
	public void printChessSteps(ChessMove cm){
	int stepN=1;
	// if(cm.previousMove!=null){
		// System.out.println("Error here");
	// }
		while(cm!=null){
			System.out.print(stepN+": "+cm);
			cm=cm.nextMove;
			stepN++;
			
		}
		System.out.println();
	}
	boolean hasChecked=false;
	class ChessPiece{
		int xLocation=0;
		int yLocation=0;
		int chessNumber=0;
		int chessValue=0;
		Image imagePiece=null;
		final int QUEENVALUE=9;
		final int ROOKIEVALUE=5;
		final int BISHOPVALUE=3;
		final int KNIGHTVALUE=3;
		final int PAWNVALUE=1;
		boolean wentStraightForward=false;
		boolean canEnpassant=false;
		boolean canBeEnpassanted=false;
		boolean hasNeverMoved=false;
		
		int arrayIndexLocation=0;
		public ChessPiece(int number, int x, int y, Image im){
			chessNumber=number;
			xLocation=x;
			yLocation=y;
			imagePiece=im;
			if(number==1||number==7){
				chessValue=QUEENVALUE;
			}
			if(number==2||number==8){
				chessValue=KNIGHTVALUE;
			}
			if(number==3||number==9){
				chessValue=ROOKIEVALUE;
			}
			
			if(number==4||number==10){
				chessValue=BISHOPVALUE;
			}
			
			if(number==5||number==11){
				chessValue=PAWNVALUE;
			}
		}
		
		public java.util.ArrayList<ChessLocation> getListOfMoves(){
			java.util.ArrayList<ChessLocation> listOfLocations=new java.util.ArrayList<ChessLocation>();
			ChessPiece king=null;
			ChessLocation step=null;
			if(chessNumber<6){
				king=kingLocation;
			}else{
				king=enemyKingLocation;
			}
			if(chessNumber==PAWN||chessNumber==EPAWN){
				int teamValue=-1;//enemy team.
				if(chessNumber<6){	//can only go forward. TEAM 1
					teamValue=1;//good team.
				}
				//determine front move.
				if(yLocation>0&&yLocation<7&&board[xLocation][yLocation-1*teamValue]==null){
					step=new ChessLocation( xLocation,yLocation-1*teamValue);
					if(willNotKillKing(king, board[xLocation][yLocation], step, board)){
						// if(yLocation==1&&chessNumber==PAWN){
						// step.promotedNumber=1;
						for(int j=1;yLocation==1&&chessNumber==PAWN&&j<5;j++){
							step=new ChessLocation( xLocation,yLocation-1*teamValue);
							step.promotedNumber=j;
							listOfLocations.add(step);
						}
						for(int j=7;yLocation==6&&chessNumber==EPAWN&&j<11;j++){
							step=new ChessLocation( xLocation,yLocation-1*teamValue);
							step.promotedNumber=j;
							listOfLocations.add(step);
						}
						if(step.promotedNumber<0){
							step=new ChessLocation( xLocation,yLocation-1*teamValue);
							listOfLocations.add(step);
						}
						// }else{
							// listOfLocations.add(step);
						// }
					}
					if((yLocation==6&&chessNumber<6||yLocation==1&&chessNumber>=6)&&board[xLocation][yLocation-2*teamValue]==null){//board[xLocation][yLocation-2*teamValue]==null){//first time move.
						step=new ChessLocation( xLocation,yLocation-2*teamValue);
						if(willNotKillKing(king, board[xLocation][yLocation], step, board)){
						
							listOfLocations.add(step);
						
						}
					}
				}

				//determine diagonal attack.
				if(xLocation<=6){
				
					ChessPiece cp=board[xLocation+1][yLocation-1*teamValue];
				
					int enemyDeterminer=0;
					if(cp!=null){
						enemyDeterminer=cp.chessNumber;
						// //////System.out.println(enemyDeterminer);
						if(chessNumber<6&&enemyDeterminer>=6||chessNumber>=6&&enemyDeterminer<6){
							step=new ChessLocation( xLocation+1,yLocation-1*teamValue);
							if(willNotKillKing(king, board[xLocation][yLocation], step, board)){
								for(int j=1;yLocation==1&&chessNumber==PAWN&&j<5;j++){
									step=new ChessLocation( xLocation+1,yLocation-1*teamValue);
									step.promotedNumber=j;
									listOfLocations.add(step);
								}
								for(int j=7;yLocation==6&&chessNumber==EPAWN&&j<11;j++){
									step=new ChessLocation( xLocation+1,yLocation-1*teamValue);
									step.promotedNumber=j;
									listOfLocations.add(step);
								}
								if(step.promotedNumber<0){
									step=new ChessLocation( xLocation+1,yLocation-1*teamValue);
									listOfLocations.add(step);
								}
							}
						}
					}
				}
				if(xLocation>=1){
					ChessPiece cp=board[xLocation-1][yLocation-1*teamValue];
					
					int enemyDeterminer=0;
					if(cp!=null){
						enemyDeterminer=cp.chessNumber;
						if(chessNumber<6&&enemyDeterminer>=6||chessNumber>=6&&enemyDeterminer<6){
							step=new ChessLocation(xLocation-1,yLocation-1*teamValue);//-1][yLocation-1*teamValue]);
							if(willNotKillKing(king, board[xLocation][yLocation], step, board)){
								for(int j=1;yLocation==1&&chessNumber==PAWN&&j<5;j++){
									step=new ChessLocation( xLocation-1,yLocation-1*teamValue);
									step.promotedNumber=j;
									listOfLocations.add(step);
								}
								for(int j=7;yLocation==6&&chessNumber==EPAWN&&j<11;j++){
									step=new ChessLocation( xLocation-1,yLocation-1*teamValue);
									step.promotedNumber=j;
									listOfLocations.add(step);
								}
								if(step.promotedNumber<0){
									step=new ChessLocation( xLocation-1,yLocation-1*teamValue);
									listOfLocations.add(step);
								}
							}
						}
					}
				}
				
				//DETERMINE ENCOMPASSE ATTACK
				if(xLocation>=1){
					ChessPiece cp=board[xLocation-1][yLocation];
					if(cp!=null&&(cp.chessNumber==EPAWN&&chessNumber==PAWN||cp.chessNumber==PAWN&&chessNumber==EPAWN)&&cp.wentStraightForward&&canEnpassant&&cp.canBeEnpassanted){
						
						step=new ChessLocation(xLocation-1,yLocation-1*teamValue);//-1][yLocation-1*teamValue]);
						if(willNotKillKing(king, board[xLocation][yLocation], step, board)){
							listOfLocations.add(step);
						}
					}
				}
				if(xLocation<7){
					ChessPiece cp=board[xLocation+1][yLocation];
					if(cp!=null&&(cp.chessNumber==EPAWN&&chessNumber==PAWN||cp.chessNumber==PAWN&&chessNumber==EPAWN)&&cp.wentStraightForward&&cp.canBeEnpassanted){
						step=new ChessLocation(xLocation+1,yLocation-1*teamValue);//+1][yLocation-1*teamValue]);
						if(willNotKillKing(king, board[xLocation][yLocation], step, board)){
							listOfLocations.add(step);
						}
					}
				}
				
					
				//CHECK OPPONENTS.
				
			}
			
			
			if(chessNumber==ROOKIE||chessNumber==QUEEN||chessNumber==EROOKIE||chessNumber==EQUEEN){
				int rqx=xLocation-1;
				step=null;
				ChessPiece occupiedPiece=null;
				// if(rqx>=0){
					// occupiedPiece=board[rqx][y];
				// }
				while(rqx>=0&&occupiedPiece==null){
					occupiedPiece=board[rqx][yLocation];
					if(occupiedPiece==null){
						
						step=new ChessLocation(rqx,yLocation);//,this,null);
						if(willNotKillKing(king, board[xLocation][yLocation], step, board)){
							listOfLocations.add(step);
						}
					}
					
					rqx--;
				}
				if(occupiedPiece!=null&&(chessNumber<6&&occupiedPiece.chessNumber>=6||chessNumber>=6&&occupiedPiece.chessNumber<6)){//if facing opposite sides,
						
					step=new ChessLocation(occupiedPiece.xLocation,yLocation);//,this,board[occupiedPiece.xLocation][yLocation]);
					if(willNotKillKing(king, board[xLocation][yLocation], step, board)){
						listOfLocations.add(step);
					}
				}
				
				rqx=xLocation+1;
				step=null;
				occupiedPiece=null;
				// if(rqx>=0){
					// occupiedPiece=board[rqx][y];
				// }
				// ////System.out.println("here");
				while(rqx<=7&&occupiedPiece==null){
					occupiedPiece=board[rqx][yLocation];
					if(occupiedPiece==null){
						
						step=new ChessLocation(rqx,yLocation);//
						
						if(willNotKillKing(king, board[xLocation][yLocation], step, board)){
							listOfLocations.add(step);
						}
					}
					
					rqx++;
				}
				if(occupiedPiece!=null&&(chessNumber<6&&occupiedPiece.chessNumber>=6||chessNumber>=6&&occupiedPiece.chessNumber<6)){//if facing opposite sides,
						
					step=new ChessLocation(occupiedPiece.xLocation,yLocation);//occupiedPiece.xLocation][yLocation]);
					
					if(willNotKillKing(king, board[xLocation][yLocation], step, board)){//problem here 1
						listOfLocations.add(step);
					}
				}
				
				int rqy=yLocation-1;
				step=null;
				occupiedPiece=null;
				// if(rqx>=0){
					// occupiedPiece=board[rqx][y];
				// }
				while(rqy>=0&&occupiedPiece==null){
					occupiedPiece=board[xLocation][rqy];
					if(occupiedPiece==null){
						
						step=new ChessLocation(xLocation,rqy);//xLocation][rqy]);
						if(willNotKillKing(king, board[xLocation][yLocation], step, board)){
							listOfLocations.add(step);
						}
					}
					
					rqy--;
				}
				if(occupiedPiece!=null&&(chessNumber<6&&occupiedPiece.chessNumber>=6||chessNumber>=6&&occupiedPiece.chessNumber<6)){//if facing opposite sides,
						
					step=new ChessLocation(xLocation,occupiedPiece.yLocation);//xLocation][occupiedPiece.yLocation]);
					if(willNotKillKing(king, board[xLocation][yLocation], step, board)){
						listOfLocations.add(step);
					}
				}
				
				rqy=yLocation+1;
				step=null;
				occupiedPiece=null;
				// if(rqx>=0){
					// occupiedPiece=board[rqx][y];
				// }
				while(rqy<=7&&occupiedPiece==null){
					occupiedPiece=board[xLocation][rqy];
					if(occupiedPiece==null){
						
						step=new ChessLocation(xLocation,rqy);//xLocation][rqy]);
						
						if(willNotKillKing(king, board[xLocation][yLocation], step, board)){
							listOfLocations.add(step);
						}
					}
					
					rqy++;
				}
				if(occupiedPiece!=null&&(chessNumber<6&&occupiedPiece.chessNumber>=6||chessNumber>=6&&occupiedPiece.chessNumber<6)){//if facing opposite sides,
					step=new ChessLocation(xLocation,occupiedPiece.yLocation);//xLocation][occupiedPiece.yLocation]);
					if(willNotKillKing(king, board[xLocation][yLocation], step, board)){
						listOfLocations.add(step);
					}
				}
				
			}
			
			if(chessNumber==BISHOP||chessNumber==EBISHOP||chessNumber==QUEEN||chessNumber==EQUEEN){
				int rx=xLocation-1;
				int ry=yLocation-1;
				ChessPiece ep=null;//board[rx][ry];
				while(rx>=0&&ry>=0&&ep==null){
					ep=board[rx][ry];
					if(ep==null){
						step=new ChessLocation(rx,ry);//rx][ry]);
						if(willNotKillKing(king,board[xLocation][yLocation],step,board)){
							listOfLocations.add(step);
						}
					}
					rx--;
					ry--;

				}
				if(ep!=null&&(ep.chessNumber>=6&&chessNumber<6||ep.chessNumber<6&&chessNumber>=6)){
					step=new ChessLocation(ep.xLocation,ep.yLocation);//ep.xLocation][ep.yLocation]);
					if(willNotKillKing(king,board[xLocation][yLocation],step,board)){
						listOfLocations.add(step);
					}
				}
				
				rx=xLocation+1;
				ry=yLocation+1;
				ep=null;//board[rx][ry];
				while(rx<=7&&ry<=7&&ep==null){
					ep=board[rx][ry];
					if(ep==null){
						step=new ChessLocation(rx,ry);//rx][ry]);
						if(willNotKillKing(king,board[xLocation][yLocation],step,board)){
							listOfLocations.add(step);
						}
					}
					rx++;
					ry++;
				}
				if(ep!=null&&(ep.chessNumber>=6&&chessNumber<6||ep.chessNumber<6&&chessNumber>=6)){
					step=new ChessLocation(ep.xLocation,ep.yLocation);//ep.xLocation][ep.yLocation]);
					if(willNotKillKing(king,board[xLocation][yLocation],step,board)){
						listOfLocations.add(step);
					}
				}
				
				rx=xLocation+1;
				ry=yLocation-1;
				ep=null;//board[rx][ry];
				while(rx<=7&&ry>=0&&ep==null){
					ep=board[rx][ry];
					if(ep==null){
						step=new ChessLocation(rx,ry);//rx][ry]);
						if(willNotKillKing(king,board[xLocation][yLocation],step,board)){
							listOfLocations.add(step);
						}
					}
					rx++;
					ry--;
				}
				if(ep!=null&&(ep.chessNumber>=6&&chessNumber<6||ep.chessNumber<6&&chessNumber>=6)){
					step=new ChessLocation(ep.xLocation,ep.yLocation);//ep.xLocation][ep.yLocation]);
					if(willNotKillKing(king,board[xLocation][yLocation],step,board)){
						listOfLocations.add(step);
					}
				}
				
				rx=xLocation-1;
				ry=yLocation+1;
				ep=null;//board[rx][ry];
				while(rx>=0&&ry<=7&&ep==null){
					ep=board[rx][ry];
					if(ep==null){
						step=new ChessLocation(rx,ry);//rx][ry]);
						if(willNotKillKing(king,board[xLocation][yLocation],step,board)){
							listOfLocations.add(step);
						}
					}
					rx--;
					ry++;
				}
				if(ep!=null&&(ep.chessNumber>=6&&chessNumber<6||ep.chessNumber<6&&chessNumber>=6)){
					step=new ChessLocation(ep.xLocation,ep.yLocation);//ep.xLocation][ep.yLocation]);
					if(willNotKillKing(king,board[xLocation][yLocation],step,board)){
						listOfLocations.add(step);
					}
				}
			}
			
			if(chessNumber==KNIGHT||chessNumber==EKNIGHT){
				ChessPiece []listOfSteps=new ChessPiece[8];
				ChessLocation []stepArray=new ChessLocation[8];
				if(xLocation-1>=0&&yLocation-2>=0){
					listOfSteps[0]=board[xLocation-1][yLocation-2];
					stepArray[0]=new ChessLocation(xLocation-1,yLocation-2);//xLocation-1][yLocation-2]);	
					
					
				}
				if(xLocation-2>=0&&yLocation-1>=0){
					listOfSteps[1]=board[xLocation-2][yLocation-1];
					stepArray[1]=new ChessLocation(xLocation-2,yLocation-1);//xLocation-2][yLocation-1]);
				}
				// //////System.out.println("Yikes"+x+" "+y);
				if(xLocation+1<=7&&yLocation-2>=0){
					listOfSteps[2]=board[xLocation+1][yLocation-2];
					stepArray[2]=new ChessLocation(xLocation+1,yLocation-2);//xLocation+1][yLocation-2]);
					// //////System.out.println(identities[2]);
				}
				if(xLocation+2<=7&&yLocation-1>=0){
					listOfSteps[3]=board[xLocation+2][yLocation-1];
					stepArray[3]=new ChessLocation(xLocation+2,yLocation-1);//xLocation+2][yLocation-1]);
				}
				if(xLocation-1>=0&&yLocation+2<=7){
					listOfSteps[4]=board[xLocation-1][yLocation+2];
					stepArray[4]=new ChessLocation(xLocation-1,yLocation+2);//xLocation-1][yLocation+2]);
					// //////System.out.println(stepArray[4].x+" "+stepArray[4].y);
				}
				if(xLocation-2>=0&&yLocation+1<=7){
					listOfSteps[5]=board[xLocation-2][yLocation+1];
					stepArray[5]=new ChessLocation(xLocation-2,yLocation+1);//xLocation-2][yLocation+1]);
				}
				if(xLocation+1<=7&&yLocation+2<=7){
				// //////System.out.println(x+" "+y);
					listOfSteps[6]=board[xLocation+1][yLocation+2];
					stepArray[6]=new ChessLocation(xLocation+1,yLocation+2);//xLocation+1][yLocation+2]);
				}
				if(xLocation+2<=7&&yLocation+1<=7){
					listOfSteps[7]=board[xLocation+2][yLocation+1];
					stepArray[7]=new ChessLocation(xLocation+2,yLocation+1);//xLocation+2][yLocation+1]);
				}
				for(int j=0;j<8;j++){
					// //////System.out.println(stepArray[j]);
					if(listOfSteps[j]!=null){
						if(listOfSteps[j].chessNumber>=6&&chessNumber<6||listOfSteps[j].chessNumber<6&&chessNumber>=6){
							if(willNotKillKing(king,board[xLocation][yLocation],stepArray[j],board)){
								listOfLocations.add(stepArray[j]);
							}
						}
						
					}else if(stepArray[j]!=null&&willNotKillKing(king,board[xLocation][yLocation],stepArray[j],board)){
						listOfLocations.add(stepArray[j]);
						
					}
				}
						
			}
			
			if(chessNumber==KING||chessNumber==EKING){
				ChessPiece occupiedPiece=null;
				ChessLocation []possibleNextPosition=new ChessLocation[10];
				ChessPiece []possiblePiece=new ChessPiece[10];
				
				if(xLocation>=1&&yLocation>=1){
					possiblePiece[0]=board[xLocation-1][yLocation-1];
					possibleNextPosition[0]=new ChessLocation(xLocation-1,yLocation-1);//xLocation-1][yLocation-1]);
				}
				if(yLocation>=1){
					possiblePiece[1]=board[xLocation][yLocation-1];
					possibleNextPosition[1]=new ChessLocation(xLocation,yLocation-1);//xLocation][yLocation-1]);
				}
				if(xLocation<7&&yLocation>=1){
					possiblePiece[2]=board[xLocation+1][yLocation-1];
					possibleNextPosition[2]=new ChessLocation(xLocation+1,yLocation-1);//xLocation+1][yLocation-1]);
				}
				
				if(xLocation<7){
					possiblePiece[3]=board[xLocation+1][yLocation];
					possibleNextPosition[3]=new ChessLocation(xLocation+1,yLocation);//xLocation+1][yLocation]);
				}
				if(xLocation<7&&yLocation<7){
					possiblePiece[4]=board[xLocation+1][yLocation+1];
					possibleNextPosition[4]=new ChessLocation(xLocation+1,yLocation+1);//xLocation+1][yLocation+1]);
				}
				if(yLocation<7){
					possiblePiece[5]=board[xLocation][yLocation+1];
					possibleNextPosition[5]=new ChessLocation(xLocation,yLocation+1);//xLocation][yLocation+1]);
				}
				if(xLocation>=1&&yLocation<7){
					possiblePiece[6]=board[xLocation-1][yLocation+1];
					possibleNextPosition[6]=new ChessLocation(xLocation-1,yLocation+1);//xLocation-1][yLocation+1]);
				}
				if(xLocation>=1){
					possiblePiece[7]=board[xLocation-1][yLocation];
					possibleNextPosition[7]=new ChessLocation(xLocation-1,yLocation);//xLocation-1][yLocation]);
				}
				boolean canCastleOnLeft=canLeftCastle<0&&chessNumber<6||canEnemyLeftCastle<0&&chessNumber>=6;
				boolean canCastleOnRight=canRightCastle<0&&chessNumber<6||canEnemyRightCastle<0&&chessNumber>=6;
				if(canCastleOnLeft||canCastleOnRight){
					canCastleOnLeft=canCastleOnLeft&&willNotKillKing(board[xLocation][yLocation], null, null, board);
					canCastleOnRight=canCastleOnRight&&willNotKillKing(board[xLocation][yLocation], null, null, board);
						// if(chessNumber<6){
					if(canCastleOnLeft||canCastleOnRight){
						for(int j=1;j<4;j++){
							if(board[j][7]!=null&&chessNumber==KING||board[j][0]!=null&&chessNumber==EKING){
								canCastleOnLeft=false;
							}
							if(j<3&&(board[j+4][7]!=null&&chessNumber==KING||board[j+4][0]!=null&&chessNumber==EKING)){
								canCastleOnRight=false;
							}
						}
					}
					ChessMove movesHistory=copyChessMove(moveHistory,true);
					if(movesHistory!=null&&(canCastleOnLeft||canCastleOnRight)){
						while(movesHistory.previousMove!=null){
							movesHistory=movesHistory.previousMove;
						}
						while(movesHistory.nextMove!=null&&(canCastleOnLeft||canCastleOnRight)){
							if(movesHistory.from.x==0&&movesHistory.from.y==7&&chessNumber<6||movesHistory.from.x==0&&movesHistory.from.y==0&&chessNumber>=6){
								canCastleOnLeft=false;
							}
							if(movesHistory.from.x==7&&movesHistory.from.y==7&&chessNumber<6||movesHistory.from.x==7&&movesHistory.from.y==0&&chessNumber>=6){
								canCastleOnRight=false;
							}
							if(movesHistory.from.x==4&&movesHistory.from.y==7&&chessNumber<6||movesHistory.from.x==4&&movesHistory.from.y==0&&chessNumber>=6){
								canCastleOnLeft=false;
								canCastleOnRight=false;
							}
							movesHistory=movesHistory.nextMove;
						}

						if(canCastleOnLeft||canCastleOnRight){
							if(movesHistory.from.x==0&&movesHistory.from.y==7&&chessNumber<6||movesHistory.from.x==0&&movesHistory.from.y==0&&chessNumber>=6){
								canCastleOnLeft=false;
							}
							if(movesHistory.from.x==7&&movesHistory.from.y==7&&chessNumber<6||movesHistory.from.x==7&&movesHistory.from.y==0&&chessNumber>=6){
								canCastleOnRight=false;
							}
							if(movesHistory.from.x==4&&movesHistory.from.y==7&&chessNumber<6||movesHistory.from.x==4&&movesHistory.from.y==0&&chessNumber>=6){
								canCastleOnLeft=false;
								canCastleOnRight=false;
							}
						}
						while(movesHistory.nextMove!=null){
							movesHistory=movesHistory.nextMove;
						}
						
						if(canCastleOnLeft){
							if(chessNumber==KING){
							
								possibleNextPosition[8]=new ChessLocation(2,7);
							}else{
								possibleNextPosition[8]=new ChessLocation(2,0);
							}
							if(willNotKillKing(king,king,possibleNextPosition[8],board)){
								listOfLocations.add(possibleNextPosition[8]);
							}
						}
						if(canCastleOnRight){
							if(chessNumber==KING){
							
								possibleNextPosition[9]=new ChessLocation(6,7);
							}else{
								possibleNextPosition[9]=new ChessLocation(6,0);
							}
							if(willNotKillKing(king,king,possibleNextPosition[9],board)){
								listOfLocations.add(possibleNextPosition[9]);
							}
						}
					}
				}
				/*boolean canCastle=true;
				if(hasNeverMoved&&willNotKillKing(board[xLocation][yLocation],null,null,board)){
					if(board[0][yLocation]!=null&&board[0][yLocation].hasNeverMoved){
						for(int j=xLocation-1;j>0;j--){
							if(board[j][yLocation]!=null){
								canCastle=false;
								break;
							}
						}
					}
					if(canCastle){
						possibleNextPosition[8]=new ChessLocation(xLocation-2,yLocation);
					}
					canCastle=true;
					if(board[7][yLocation]!=null&&board[7][yLocation].hasNeverMoved){
						for(int j=xLocation+1;j<7;j++){
							if(board[j][yLocation]!=null){
								canCastle=false;
								break;
							}
						}
					}
					if(canCastle){
						possibleNextPosition[9]=new ChessLocation(xLocation+2,yLocation);
					}
				}*/
				for(int j=0;j<8;j++){
				
					// occupiedPiece=possiblePiece[j];
					if(possiblePiece[j]==null){
						
						// step=new ChessLocation(xLocation-1,yLocation-1);
						//////System.out.println("King: "+king);
						if(possibleNextPosition[j]!=null&&willNotKillKing(king,king,possibleNextPosition[j],board)){
							listOfLocations.add(possibleNextPosition[j]);
						}
					}else if(chessNumber==KING&&possiblePiece[j].chessNumber>=6||chessNumber==EKING&&possiblePiece[j].chessNumber<6){
						if(willNotKillKing(king,king,possibleNextPosition[j],board)){
							listOfLocations.add(possibleNextPosition[j]);
						}
					}
				}
				
			}
				
			return listOfLocations;
		}
		// boolean hasChecked=false;
		public java.util.ArrayList<ChessLocation> getListOfMoves2(ChessPiece [][]afterBoard, int teamNo){
			java.util.ArrayList<ChessLocation> listOfLocations=new java.util.ArrayList<ChessLocation>();
			ChessPiece king=null;
			ChessLocation step=null;
			// if(chessNumber<6&&teamNo<6||chessNumber>=6&&teamNo>=6){
				// king=kingLocation;
			// }else{
				// king=enemyKingLocation;
			// 
			for(int j=0;j<afterBoard.length;j++){
				for(int k=0;k<afterBoard[j].length;k++){
					if(afterBoard[j][k]!=null){
						if(afterBoard[j][k].chessNumber==KING&&chessNumber<6||afterBoard[j][k].chessNumber==EKING&&chessNumber>=6){
							king=afterBoard[j][k];
							break;
						}
					}
				}
			}
				
			if(chessNumber==PAWN||chessNumber==EPAWN){
				int teamValue=-1;//enemy team.
				if(chessNumber<6){	//can only go forward. TEAM 1
					teamValue=1;//good team.
				}
				//determine front move.
				
				if(yLocation>0&&yLocation<7&&afterBoard[xLocation][yLocation-1*teamValue]==null){
					step=new ChessLocation( xLocation,yLocation-1*teamValue);
					// if(afterBoard[xLocation][yLocation].chessNumber==4){
						// System.out.println("Strange here: "+afterBoard[xLocation][yLocation].chessNumber+" ("+xLocation+","+yLocation+") to "+step);
						
						// printBoard(afterBoard);
					// }
					if(willNotKillKing(king, afterBoard[xLocation][yLocation], step, afterBoard)){
						for(int j=1;yLocation==1&&chessNumber==PAWN&&j<5;j++){
							step=new ChessLocation( xLocation,yLocation-1*teamValue);
							step.promotedNumber=j;
							listOfLocations.add(step);
						}
						for(int j=7;yLocation==6&&chessNumber==EPAWN&&j<11;j++){
							step=new ChessLocation( xLocation,yLocation-1*teamValue);
							step.promotedNumber=j;
							listOfLocations.add(step);
						}
						if(step.promotedNumber<0){
							step=new ChessLocation( xLocation,yLocation-1*teamValue);
							listOfLocations.add(step);
						}
					}
					if((yLocation==6&&chessNumber<6||yLocation==1&&chessNumber>=6)&&afterBoard[xLocation][yLocation-2*teamValue]==null){//afterBoard[xLocation][yLocation-2*teamValue]==null){//first time move.
						step=new ChessLocation( xLocation,yLocation-2*teamValue);
						if(willNotKillKing(king, afterBoard[xLocation][yLocation], step, afterBoard)){
							listOfLocations.add(step);
						}
					}
				}

				//determine diagonal attack.
				if(xLocation<=6){
					if(yLocation-1*teamValue<0){
						printBoard(afterBoard);
						// System.out.println("ChessNumber :"+chessNumber+" Chess value "+chessValue);
						// System.out.println("("+xLocation+","+yLocation+")");
					}
					ChessPiece cp=afterBoard[xLocation+1][yLocation-1*teamValue];
				
					int enemyDeterminer=0;
					if(cp!=null){
						enemyDeterminer=cp.chessNumber;
						// //////System.out.println(enemyDeterminer);
						if(chessNumber<6&&enemyDeterminer>=6||chessNumber>=6&&enemyDeterminer<6){
							step=new ChessLocation( xLocation+1,yLocation-1*teamValue);
							if(willNotKillKing(king, afterBoard[xLocation][yLocation], step, afterBoard)){
								for(int j=1;yLocation==1&&chessNumber==PAWN&&j<5;j++){
									step=new ChessLocation( xLocation+1,yLocation-1*teamValue);
									step.promotedNumber=j;
									listOfLocations.add(step);
								}
								for(int j=7;yLocation==6&&chessNumber==EPAWN&&j<11;j++){
									step=new ChessLocation( xLocation+1,yLocation-1*teamValue);
									step.promotedNumber=j;
									listOfLocations.add(step);
								}
								if(step.promotedNumber<0){
									step=new ChessLocation( xLocation+1,yLocation-1*teamValue);
									listOfLocations.add(step);
								}
							}
						}
					}
				}
				if(xLocation>=1){
				// if(!hasChecked){
				// System.out.println("should be a queen" +xLocation+" "+yLocation);
				// hasChecked=true;
				// }
				if(xLocation<0||xLocation>7||yLocation-1*teamValue<0||yLocation-1*teamValue>7){
					System.out.println("here is error"+xLocation+" "+yLocation);
				}
					ChessPiece cp=afterBoard[xLocation-1][yLocation-1*teamValue];
					int enemyDeterminer=0;
					if(cp!=null){
						enemyDeterminer=cp.chessNumber;
						if(chessNumber<6&&enemyDeterminer>=6||chessNumber>=6&&enemyDeterminer<6){
							step=new ChessLocation(xLocation-1,yLocation-1*teamValue);//-1][yLocation-1*teamValue]);
							if(willNotKillKing(king, afterBoard[xLocation][yLocation], step, afterBoard)){
								// if(yLocation==1){
								// System.out.println("came here ");
								// }
								for(int j=1;yLocation==1&&chessNumber==PAWN&&j<5;j++){
									step=new ChessLocation( xLocation-1,yLocation-1*teamValue);
									step.promotedNumber=j;
									listOfLocations.add(step);
								}
								for(int j=7;yLocation==6&&chessNumber==EPAWN&&j<11;j++){
									step=new ChessLocation( xLocation-1,yLocation-1*teamValue);
									step.promotedNumber=j;
									listOfLocations.add(step);
								}
								if(step.promotedNumber<0){
									step=new ChessLocation( xLocation-1,yLocation-1*teamValue);
									listOfLocations.add(step);
								}
							}
						}
					}
				}
				
				//DETERMINE ENCOMPASSE ATTACK
				if(xLocation>=1){
					ChessPiece cp=afterBoard[xLocation-1][yLocation];
					if(cp!=null&&(cp.chessNumber==EPAWN&&chessNumber==PAWN||cp.chessNumber==PAWN&&chessNumber==EPAWN)&&cp.wentStraightForward&&cp.canBeEnpassanted&&canEnpassant){
						
						step=new ChessLocation(xLocation-1,yLocation-1*teamValue);//-1][yLocation-1*teamValue]);
						if(willNotKillKing(king, afterBoard[xLocation][yLocation], step, afterBoard)){
							listOfLocations.add(step);
						}
					}
				}
				if(xLocation<7){
					ChessPiece cp=afterBoard[xLocation+1][yLocation];
					if(cp!=null&&(cp.chessNumber==EPAWN&&chessNumber==PAWN||cp.chessNumber==PAWN&&chessNumber==EPAWN)&&cp.wentStraightForward&&cp.canBeEnpassanted){
						step=new ChessLocation(xLocation+1,yLocation-1*teamValue);//+1][yLocation-1*teamValue]);
						if(willNotKillKing(king, afterBoard[xLocation][yLocation], step, afterBoard)){
							listOfLocations.add(step);
						}
					}
				}
				//CHECK OPPONENTS.
				
			}
			
			
			if(chessNumber==ROOKIE||chessNumber==QUEEN||chessNumber==EROOKIE||chessNumber==EQUEEN){
				int rqx=xLocation-1;
				step=null;
				ChessPiece occupiedPiece=null;
				// if(rqx>=0){
					// occupiedPiece=afterBoard[rqx][y];
				// }
				while(rqx>=0&&occupiedPiece==null){
					occupiedPiece=afterBoard[rqx][yLocation];
					if(occupiedPiece==null){
						
						step=new ChessLocation(rqx,yLocation);//,this,null);
						if(willNotKillKing(king, afterBoard[xLocation][yLocation], step, afterBoard)){
							listOfLocations.add(step);
						}
					}
					
					rqx--;
				}
				if(occupiedPiece!=null&&(chessNumber<6&&occupiedPiece.chessNumber>=6||chessNumber>=6&&occupiedPiece.chessNumber<6)){//if facing opposite sides,
						
					step=new ChessLocation(occupiedPiece.xLocation,yLocation);//,this,afterBoard[occupiedPiece.xLocation][yLocation]);
					if(willNotKillKing(king, afterBoard[xLocation][yLocation], step, afterBoard)){
						listOfLocations.add(step);
					}
				}
				
				rqx=xLocation+1;
				step=null;
				occupiedPiece=null;
				// if(rqx>=0){
					// occupiedPiece=afterBoard[rqx][y];
				// }
				// ////System.out.println("here");
				while(rqx<=7&&occupiedPiece==null){
					occupiedPiece=afterBoard[rqx][yLocation];
					if(occupiedPiece==null){
						
						step=new ChessLocation(rqx,yLocation);//
						
						if(willNotKillKing(king, afterBoard[xLocation][yLocation], step, afterBoard)){
							listOfLocations.add(step);
						}
					}
					
					rqx++;
				}
				if(occupiedPiece!=null&&(chessNumber<6&&occupiedPiece.chessNumber>=6||chessNumber>=6&&occupiedPiece.chessNumber<6)){//if facing opposite sides,
						
					step=new ChessLocation(occupiedPiece.xLocation,yLocation);//occupiedPiece.xLocation][yLocation]);
					
					if(willNotKillKing(king, afterBoard[xLocation][yLocation], step, afterBoard)){//problem here 1
						listOfLocations.add(step);
					}
				}
				
				int rqy=yLocation-1;
				step=null;
				occupiedPiece=null;
				// if(rqx>=0){
					// occupiedPiece=afterBoard[rqx][y];
				// }
				while(rqy>=0&&occupiedPiece==null){
					occupiedPiece=afterBoard[xLocation][rqy];
				
					if(occupiedPiece==null){
						
						step=new ChessLocation(xLocation,rqy);//xLocation][rqy]);
						if(willNotKillKing(king, afterBoard[xLocation][yLocation], step, afterBoard)){
							listOfLocations.add(step);
						}
					}
					
					rqy--;
				}
				if(occupiedPiece!=null&&(chessNumber<6&&occupiedPiece.chessNumber>=6||chessNumber>=6&&occupiedPiece.chessNumber<6)){//if facing opposite sides,
						
					step=new ChessLocation(xLocation,occupiedPiece.yLocation);//xLocation][occupiedPiece.yLocation]);
					if(willNotKillKing(king, afterBoard[xLocation][yLocation], step, afterBoard)){
						listOfLocations.add(step);
					}
				}
				
				rqy=yLocation+1;
				step=null;
				occupiedPiece=null;
				// if(rqx>=0){
					// occupiedPiece=afterBoard[rqx][y];
				// }
				while(rqy<=7&&occupiedPiece==null){
					occupiedPiece=afterBoard[xLocation][rqy];
					if(occupiedPiece==null){
						
						step=new ChessLocation(xLocation,rqy);//xLocation][rqy]);
						
						if(willNotKillKing(king, afterBoard[xLocation][yLocation], step, afterBoard)){
							listOfLocations.add(step);
						}
					}
					
					rqy++;
				}
				if(occupiedPiece!=null&&(chessNumber<6&&occupiedPiece.chessNumber>=6||chessNumber>=6&&occupiedPiece.chessNumber<6)){//if facing opposite sides,
					step=new ChessLocation(xLocation,occupiedPiece.yLocation);//xLocation][occupiedPiece.yLocation]);
					if(willNotKillKing(king, afterBoard[xLocation][yLocation], step, afterBoard)){
						listOfLocations.add(step);
					}
				}
				
			}
			
			if(chessNumber==BISHOP||chessNumber==EBISHOP||chessNumber==QUEEN||chessNumber==EQUEEN){
				int rx=xLocation-1;
				int ry=yLocation-1;
				ChessPiece ep=null;//afterBoard[rx][ry];
				while(rx>=0&&ry>=0&&ep==null){
					ep=afterBoard[rx][ry];
					if(ep==null){
						step=new ChessLocation(rx,ry);//rx][ry]);
						if(willNotKillKing(king,afterBoard[xLocation][yLocation],step,afterBoard)){
							listOfLocations.add(step);
						}
					}
					rx--;
					ry--;

				}
				if(ep!=null&&(ep.chessNumber>=6&&chessNumber<6||ep.chessNumber<6&&chessNumber>=6)){
					step=new ChessLocation(ep.xLocation,ep.yLocation);//ep.xLocation][ep.yLocation]);
					if(willNotKillKing(king,afterBoard[xLocation][yLocation],step,afterBoard)){
						listOfLocations.add(step);
					}
				}
				
				rx=xLocation+1;
				ry=yLocation+1;
				ep=null;//afterBoard[rx][ry];
				while(rx<=7&&ry<=7&&ep==null){
					ep=afterBoard[rx][ry];
					if(ep==null){
						step=new ChessLocation(rx,ry);//rx][ry]);
						if(willNotKillKing(king,afterBoard[xLocation][yLocation],step,afterBoard)){
							listOfLocations.add(step);
						}
					}
					rx++;
					ry++;
				}
				if(ep!=null&&(ep.chessNumber>=6&&chessNumber<6||ep.chessNumber<6&&chessNumber>=6)){
					step=new ChessLocation(ep.xLocation,ep.yLocation);//ep.xLocation][ep.yLocation]);
					if(willNotKillKing(king,afterBoard[xLocation][yLocation],step,afterBoard)){
						listOfLocations.add(step);
					}
				}
				
				rx=xLocation+1;
				ry=yLocation-1;
				ep=null;//afterBoard[rx][ry];
				while(rx<=7&&ry>=0&&ep==null){
					ep=afterBoard[rx][ry];
					if(ep==null){
						step=new ChessLocation(rx,ry);//rx][ry]);
						if(willNotKillKing(king,afterBoard[xLocation][yLocation],step,afterBoard)){
							listOfLocations.add(step);
						}
					}
					rx++;
					ry--;
				}
				if(ep!=null&&(ep.chessNumber>=6&&chessNumber<6||ep.chessNumber<6&&chessNumber>=6)){
					step=new ChessLocation(ep.xLocation,ep.yLocation);//ep.xLocation][ep.yLocation]);
					if(willNotKillKing(king,afterBoard[xLocation][yLocation],step,afterBoard)){
						listOfLocations.add(step);
					}
				}
				
				rx=xLocation-1;
				ry=yLocation+1;
				ep=null;//afterBoard[rx][ry];
				while(rx>=0&&ry<=7&&ep==null){
					ep=afterBoard[rx][ry];
					if(ep==null){
						step=new ChessLocation(rx,ry);//rx][ry]);
						if(willNotKillKing(king,afterBoard[xLocation][yLocation],step,afterBoard)){
							listOfLocations.add(step);
						}
					}
					rx--;
					ry++;
				}
				if(ep!=null&&(ep.chessNumber>=6&&chessNumber<6||ep.chessNumber<6&&chessNumber>=6)){
					step=new ChessLocation(ep.xLocation,ep.yLocation);//ep.xLocation][ep.yLocation]);
					if(willNotKillKing(king,afterBoard[xLocation][yLocation],step,afterBoard)){
						listOfLocations.add(step);
					}
				}
				
			}
			
			if(chessNumber==KNIGHT||chessNumber==EKNIGHT){
				ChessPiece []listOfSteps=new ChessPiece[8];
				ChessLocation []stepArray=new ChessLocation[8];
				if(xLocation-1>=0&&yLocation-2>=0){
					listOfSteps[0]=afterBoard[xLocation-1][yLocation-2];
					stepArray[0]=new ChessLocation(xLocation-1,yLocation-2);//xLocation-1][yLocation-2]);	
					
					
				}
				if(xLocation-2>=0&&yLocation-1>=0){
					listOfSteps[1]=afterBoard[xLocation-2][yLocation-1];
					stepArray[1]=new ChessLocation(xLocation-2,yLocation-1);//xLocation-2][yLocation-1]);
				}
				// //////System.out.println("Yikes"+x+" "+y);
				if(xLocation+1<=7&&yLocation-2>=0){
					listOfSteps[2]=afterBoard[xLocation+1][yLocation-2];
					stepArray[2]=new ChessLocation(xLocation+1,yLocation-2);//xLocation+1][yLocation-2]);
					// //////System.out.println(identities[2]);
				}
				if(xLocation+2<=7&&yLocation-1>=0){
					listOfSteps[3]=afterBoard[xLocation+2][yLocation-1];
					stepArray[3]=new ChessLocation(xLocation+2,yLocation-1);//xLocation+2][yLocation-1]);
				}
				if(xLocation-1>=0&&yLocation+2<=7){
					listOfSteps[4]=afterBoard[xLocation-1][yLocation+2];
					stepArray[4]=new ChessLocation(xLocation-1,yLocation+2);//xLocation-1][yLocation+2]);
					// //////System.out.println(stepArray[4].x+" "+stepArray[4].y);
				}
				if(xLocation-2>=0&&yLocation+1<=7){
					listOfSteps[5]=afterBoard[xLocation-2][yLocation+1];
					stepArray[5]=new ChessLocation(xLocation-2,yLocation+1);//xLocation-2][yLocation+1]);
				}
				if(xLocation+1<=7&&yLocation+2<=7){
				// //////System.out.println(x+" "+y);
					listOfSteps[6]=afterBoard[xLocation+1][yLocation+2];
					stepArray[6]=new ChessLocation(xLocation+1,yLocation+2);//xLocation+1][yLocation+2]);
				}
				if(xLocation+2<=7&&yLocation+1<=7){
					listOfSteps[7]=afterBoard[xLocation+2][yLocation+1];
					stepArray[7]=new ChessLocation(xLocation+2,yLocation+1);//xLocation+2][yLocation+1]);
				}
				for(int j=0;j<8;j++){
					// //////System.out.println(stepArray[j]);
					if(listOfSteps[j]!=null){
						if(listOfSteps[j].chessNumber>=6&&chessNumber<6||listOfSteps[j].chessNumber<6&&chessNumber>=6){
							if(willNotKillKing(king,afterBoard[xLocation][yLocation],stepArray[j],afterBoard)){
								listOfLocations.add(stepArray[j]);
							}
						}
						
					}else if(stepArray[j]!=null&&willNotKillKing(king,afterBoard[xLocation][yLocation],stepArray[j],afterBoard)){
						listOfLocations.add(stepArray[j]);
						
					}
				}
						
			}
			
			if(chessNumber==KING||chessNumber==EKING){
				ChessPiece occupiedPiece=null;
				ChessLocation []possibleNextPosition=new ChessLocation[10];
				ChessPiece []possiblePiece=new ChessPiece[10];
				
				if(xLocation>=1&&yLocation>=1){
					possiblePiece[0]=afterBoard[xLocation-1][yLocation-1];
					possibleNextPosition[0]=new ChessLocation(xLocation-1,yLocation-1);//xLocation-1][yLocation-1]);
				}
				if(yLocation>=1){
					possiblePiece[1]=afterBoard[xLocation][yLocation-1];
					possibleNextPosition[1]=new ChessLocation(xLocation,yLocation-1);//xLocation][yLocation-1]);
				}
				if(xLocation<7&&yLocation>=1){
					possiblePiece[2]=afterBoard[xLocation+1][yLocation-1];
					possibleNextPosition[2]=new ChessLocation(xLocation+1,yLocation-1);//xLocation+1][yLocation-1]);
				}
				
				if(xLocation<7){
					possiblePiece[3]=afterBoard[xLocation+1][yLocation];
					possibleNextPosition[3]=new ChessLocation(xLocation+1,yLocation);//xLocation+1][yLocation]);
				}
				if(xLocation<7&&yLocation<7){
					possiblePiece[4]=afterBoard[xLocation+1][yLocation+1];
					possibleNextPosition[4]=new ChessLocation(xLocation+1,yLocation+1);//xLocation+1][yLocation+1]);
				}
				if(yLocation<7){
					possiblePiece[5]=afterBoard[xLocation][yLocation+1];
					possibleNextPosition[5]=new ChessLocation(xLocation,yLocation+1);//xLocation][yLocation+1]);
				}
				if(xLocation>=1&&yLocation<7){
					possiblePiece[6]=afterBoard[xLocation-1][yLocation+1];
					possibleNextPosition[6]=new ChessLocation(xLocation-1,yLocation+1);//xLocation-1][yLocation+1]);
				}
				if(xLocation>=1){
					possiblePiece[7]=afterBoard[xLocation-1][yLocation];
					possibleNextPosition[7]=new ChessLocation(xLocation-1,yLocation);//xLocation-1][yLocation]);
				}
				boolean canCastleOnLeft=canLeftCastle<0&&chessNumber<6||canEnemyLeftCastle<0&&chessNumber>=6;
				boolean canCastleOnRight=canRightCastle<0&&chessNumber<6||canEnemyRightCastle<0&&chessNumber>=6;
				// if(chessNumber>=6){
					// canCastleOnLeft=canEnemyLeftCastle;
					// canCastleOnRight=canEnemyRightCastle;
				// }
				if(canCastleOnLeft||canCastleOnRight){
					if(canCastleOnLeft){
						canCastleOnLeft=willNotKillKing(afterBoard[xLocation][yLocation], null, null, afterBoard);
					}
					if(canCastleOnRight){
						canCastleOnRight=willNotKillKing(afterBoard[xLocation][yLocation], null, null, afterBoard);
					}
						// if(chessNumber<6){
					if(canCastleOnLeft||canCastleOnRight){
						for(int j=1;j<4;j++){
							if(afterBoard[j][7]!=null&&chessNumber==KING||afterBoard[j][0]!=null&&chessNumber==EKING){
								canCastleOnLeft=false;
							}
							if(j<3&&(afterBoard[j+4][7]!=null&&chessNumber==KING||afterBoard[j+4][0]!=null&&chessNumber==EKING)){
								canCastleOnRight=false;
							}
						}
					}
					
					if(predictedMoves!=null&&(canCastleOnLeft||canCastleOnRight)){
						while(predictedMoves.previousMove!=null){
							predictedMoves=predictedMoves.previousMove;
						}
						while(predictedMoves.nextMove!=null&&(canCastleOnLeft||canCastleOnRight)){
							if(predictedMoves.from.x==0&&predictedMoves.from.y==7&&chessNumber<6||predictedMoves.from.x==0&&predictedMoves.from.y==0&&chessNumber>=6){
								canCastleOnLeft=false;
							}
							if(predictedMoves.from.x==7&&predictedMoves.from.y==7&&chessNumber<6||predictedMoves.from.x==7&&predictedMoves.from.y==0&&chessNumber>=6){
								canCastleOnRight=false;
							}
							if(predictedMoves.from.x==4&&predictedMoves.from.y==7&&chessNumber<6||predictedMoves.from.x==4&&predictedMoves.from.y==0&&chessNumber>=6){
								canCastleOnLeft=false;
								canCastleOnRight=false;
							}
							predictedMoves=predictedMoves.nextMove;
						}

						// if(canCastleOnLeft||canCastleOnRight){
							// if(movesHistory.from.x==0&&movesHistory.from.y==7&&chessNumber<6||movesHistory.from.x==0&&movesHistory.from.y==0&&chessNumber>=6){
								// canCastleOnLeft=false;
							// }
							// if(movesHistory.from.x==7&&movesHistory.from.y==7&&chessNumber<6||movesHistory.from.x==7&&movesHistory.from.y==0&&chessNumber>=6){
								// canCastleOnRight=false;
							// }
							// if(movesHistory.from.x==4&&movesHistory.from.y==7&&chessNumber<6||movesHistory.from.x==4&&movesHistory.from.y==0&&chessNumber>=6){
								// canCastleOnLeft=false;
								// canCastleOnRight=false;
							// }
						// }
						while(predictedMoves.nextMove!=null){
							predictedMoves=predictedMoves.nextMove;
						}
						
						if(canCastleOnLeft){
							if(chessNumber==KING){
							
								possibleNextPosition[8]=new ChessLocation(2,7);
							}else{
								possibleNextPosition[8]=new ChessLocation(2,0);
							}
							if(willNotKillKing(king,king,possibleNextPosition[8],afterBoard)){
								listOfLocations.add(possibleNextPosition[8]);
							}
						}
						if(canCastleOnRight){
							if(chessNumber==KING){
							
								possibleNextPosition[9]=new ChessLocation(6,7);
							}else{
								possibleNextPosition[9]=new ChessLocation(6,0);
							}
							if(willNotKillKing(king,king,possibleNextPosition[9],afterBoard)){
								listOfLocations.add(possibleNextPosition[9]);
							}
						}
					}
				}
				for(int j=0;j<8;j++){
					// occupiedPiece=possiblePiece[j];
					if(possiblePiece[j]==null){
						
						// step=new ChessLocation(xLocation-1,yLocation-1);
						//////System.out.println("King: "+king);
						if(possibleNextPosition[j]!=null&&willNotKillKing(king,king,possibleNextPosition[j],afterBoard)){
							listOfLocations.add(possibleNextPosition[j]);
						}
					}else if(chessNumber==KING&&possiblePiece[j].chessNumber>=6||chessNumber==EKING&&possiblePiece[j].chessNumber<6){
						if(willNotKillKing(king,king,possibleNextPosition[j],afterBoard)){
							listOfLocations.add(possibleNextPosition[j]);
						}
					}
				}
			}
			
			return listOfLocations;
		}
		
		
		boolean pawnFirstTime=false;
		boolean pawnCanEncompasse=false;
		public void pawnAttack(){
		
		}
		public String toString(){
			return ""+chessNumber;
		}
		
	}

	
}



