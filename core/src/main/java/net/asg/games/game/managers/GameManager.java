package net.asg.games.game.managers;

import net.asg.games.game.objects.YokelGameBoard;
import net.asg.games.game.objects.YokelSeat;
import net.asg.games.game.objects.YokelTable;

import java.util.ArrayList;
import java.util.List;

public class GameManager {
    private YokelTable table;
    private List<YokelGameBoard> gameBoards;
    private boolean isGameRunning;

    public GameManager(YokelTable table){
        this.table = table;
        gameBoards = new ArrayList<>();
        init();
    }

    private void loadGameData() {}

    public void update(float delta){
        if(!isGameRunning) return;

        for(int i = 0; i < 8; i++){
            YokelSeat seat = table.getSeat(i);
            YokelGameBoard board = gameBoards.get(i);

            System.out.println(board);

            if(isOccupied(seat)){
                if(board != null){
                    board.update(delta);
                }
            }
        }

        if(isGameOver()){
            stopGame();
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

    private void init(){System.out.println("init Ran");
        long seed = getSeed();
        isGameRunning = false;
        for(int i = 0; i < 8; i++){
            gameBoards.add(new YokelGameBoard(seed));
        }
    }

    private long getSeed() {
        return 1L;
        //return System.currentTimeMillis();
    }

    public void resetGameBoards(){}

    public void handleMoveRight(int player){
        gameBoards.get(player).attemptMovePieceRight();
    }

    public void handleMoveLeft(int player){
        gameBoards.get(player).attemptMovePieceLeft();
    }

    public void handleSetPiece(){}

    public String[] getBoardState(){
        return null;
    }

    public String printTables(){
        StringBuilder sbSeats = new StringBuilder();
        //String t = "";
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
        return isGameRunning = false;
    }

    public boolean isRunning() {
        return isGameRunning;
    }

    private boolean isGameOver(){
        boolean player1 = isPlayerDead(gameBoards.get(0));
        boolean player2 = isPlayerDead(gameBoards.get(1));
        boolean player3 = isPlayerDead(gameBoards.get(2));
        boolean player4 = isPlayerDead(gameBoards.get(3));
        boolean player5 = isPlayerDead(gameBoards.get(4));
        boolean player6 = isPlayerDead(gameBoards.get(5));
        boolean player7 = isPlayerDead(gameBoards.get(6));
        boolean player8 = isPlayerDead(gameBoards.get(7));

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

    private class GameState{}
}