package net.asg.games.game.managers;

import com.badlogic.gdx.utils.GdxRuntimeException;

import net.asg.games.game.objects.YokelBlock;
import net.asg.games.game.objects.YokelGameBoard;
import net.asg.games.game.objects.YokelSeat;
import net.asg.games.game.objects.YokelTable;

import java.util.Vector;

public class GameManager {
    private YokelTable table;
    private YokelGameBoard[] gameBoards = new YokelGameBoard[8];
    private boolean isGameRunning;
    private boolean hasGameStarted;

    public GameManager(YokelTable table){
        this.table = table;
        init();
    }

    private void loadGameData() {}

    public void update(float delta){
        if(!isGameRunning) return;

        for(int i = 0; i < 8; i++){
            YokelSeat seat = table.getSeat(i);

            if(isOccupied(seat)){
                YokelGameBoard board = gameBoards[i];

                if(board != null){
                    board.begin();
                    updateBoard(board, delta);
                }
            }
        }

        if(isGameOver()){
            stopGame();
        }
    }

    private void updateBoard(YokelGameBoard board, float delta){
        if(board != null){
            board.update(delta);

            //Check broken Pieces
            board.flagBoardMatches();
            board.checkForYahoos();

            if(board.getBrokenCellCount() > 0){
                Vector<YokelBlock> broken = board.getBrokenCells();

                System.out.println("Broken Cells: " + broken);
                for (YokelBlock b : broken) {
                    board.addPowerToQueue(b);
                    board.incrementBreakCount(b.getBlockType());
                }
            }
            board.handleBrokenCellDrops();
        }
    }

    private boolean isOccupied(YokelSeat seat){
        if(seat != null){
            return seat.isOccupied();
        }
        return false;
    }

    private boolean isPlayerDead(YokelGameBoard board){
        if(board != null){
            return board.hasPlayerDied();
        }
        return true;
    }

    public boolean isPlayerDead(int i){
        return isPlayerDead(getGameBoard(i));
    }

    private void init(){
        long seed = getSeed();
        isGameRunning = false;
        for(int i = 0; i < 8; i++){
            gameBoards[i] = new YokelGameBoard(seed);
        }
    }

    public YokelGameBoard getGameBoard(int i){
        if(i < 0 || i > 8) throw new GdxRuntimeException("Invalid Gameboard index: " + i);
        return gameBoards[i];
    }

    private long getSeed() {
        return 1L;
        //return System.currentTimeMillis();
    }

    public void resetGameBoards(){}

    public void handleMoveRight(int boardIndex){
        getGameBoard(boardIndex).movePieceRight();
    }

    public void handleMoveLeft(int boardIndex){
        getGameBoard(boardIndex).movePieceLeft();
    }

    public void handleCycleDown(int boardIndex){
        getGameBoard(boardIndex).cycleDown();
    }

    public void handleCycleUp(int boardIndex){
        getGameBoard(boardIndex).cycleUp();
    }

    public void handleStartMoveDown(int boardIndex){
        getGameBoard(boardIndex).startMoveDown();
    }

    public void handleStopMoveDown(int boardIndex){
        getGameBoard(boardIndex).stopMoveDown();
    }

    public String[] getBoardState(){
        return null;
    }

    public String printTables(){
        StringBuilder sbSeats = new StringBuilder();

        if(table != null){
            for(YokelGameBoard board : gameBoards){
                sbSeats.append(board.toString());
            }
        }
        return sbSeats.toString();
    }

    public boolean startGame() {
        if(!isGameRunning){
            isGameRunning = table.isTableStartReady();
        }
        return isGameRunning;
    }

    public boolean stopGame(){
        for(YokelGameBoard gameboard : gameBoards){
            if(gameboard != null){
                gameboard.end();
            }
        }
        return isGameRunning = hasGameStarted = false;
    }

    public boolean isRunning() {
        return isGameRunning;
    }

    private boolean isGameOver(){
        boolean player1 = isPlayerDead(getGameBoard(0));
        boolean player2 = isPlayerDead(getGameBoard(1));
        boolean player3 = isPlayerDead(getGameBoard(2));
        boolean player4 = isPlayerDead(getGameBoard(3));
        boolean player5 = isPlayerDead(getGameBoard(4));
        boolean player6 = isPlayerDead(getGameBoard(5));
        boolean player7 = isPlayerDead(getGameBoard(6));
        boolean player8 = isPlayerDead(getGameBoard(7));

        boolean isGroup1Dead = player1 && player2;
        boolean isGroup2Dead = player3 && player4;
        boolean isGroup3Dead = player5 && player6;
        boolean isGroup4Dead = player7 && player8;

        boolean group1won = !isGroup1Dead && isGroup2Dead && isGroup3Dead && isGroup4Dead;
        boolean group2won = isGroup1Dead && !isGroup2Dead && isGroup3Dead && isGroup4Dead;
        boolean group3won = isGroup1Dead && isGroup2Dead && !isGroup3Dead && isGroup4Dead;
        boolean group4won = isGroup1Dead && isGroup2Dead && isGroup3Dead && !isGroup4Dead;

        return group1won || group2won || group3won || group4won;
    }

    private static class GameState{}
}