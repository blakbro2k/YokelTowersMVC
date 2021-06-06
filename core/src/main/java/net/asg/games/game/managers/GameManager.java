package net.asg.games.game.managers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.Queue;
import com.github.czyzby.kiwi.util.gdx.collection.GdxArrays;
import com.github.czyzby.kiwi.util.gdx.collection.GdxMaps;

import net.asg.games.game.objects.YokelBlock;
import net.asg.games.game.objects.YokelBlockEval;
import net.asg.games.game.objects.YokelGameBoard;
import net.asg.games.game.objects.YokelPlayer;
import net.asg.games.game.objects.YokelSeat;
import net.asg.games.game.objects.YokelTable;
import net.asg.games.provider.actors.GameBlockArea;
import net.asg.games.utils.Log4LibGDXLogger;
import net.asg.games.utils.Log4LibGDXLoggerService;
import net.asg.games.utils.YokelUtilities;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Stack;
import java.util.Vector;

public class GameManager implements Disposable {
    private final Log4LibGDXLogger logger = Log4LibGDXLoggerService.forClass(GameBlockArea.class);

    private final int MEDUSA_GAME_PIECE = 1;
    private final int MIDAS_GAME_PIECE = 2;

    private YokelTable table;
    private YokelGameBoard[] gameBoards = new YokelGameBoard[8];
    private Vector[] boardCellsToDrop = new Vector[8];
    private Array<YokelPlayer> winners = GdxArrays.newArray();
    private boolean isGameRunning;
    private boolean hasGameStarted;
    private boolean showGameOver;
    private long seed = 1L;

    public GameManager(YokelTable table){
        if(table == null) throw new GdxRuntimeException("Table cannot be null for GameManager.");
        setTable(table);
        init();
    }

    public void setTable(YokelTable table) {
        this.table = table;
    }

    public YokelTable getTable() {
        return this.table;
    }

    private void loadGameData() {}

    public void update(float delta){
        if(!isGameRunning) return;

        for(int i = 0; i < YokelTable.MAX_SEATS; i++){
            YokelSeat seat = table.getSeat(i);

            if(isOccupied(seat)){
                YokelGameBoard board = gameBoards[i];

                if(board != null){
                    board.begin();
                    updateBoard(board, delta, i);
                }
            }
        }

        if(isGameOver()){
            stopGame();
        }
    }

    private void updateBoard(YokelGameBoard board, float delta, int index){
        logger.debug("Enter updateBoard(delta=" + delta + ", index=" + index + ")");
        if(board != null){
            //Move Piece down, flag matches
            board.update(delta);
        }
        logger.debug("Exit updateBoard(delta=" + delta + ", index="+ index + ")");
    }

    private boolean isOccupied(YokelSeat seat){
        if(seat != null){
            return seat.isOccupied();
        }
        return false;
    }

    public boolean showGameOver(){
        boolean temp = showGameOver;
        if(showGameOver){
            showGameOver = false;
        }
        return temp;
    }

    private boolean isPlayerDead(YokelGameBoard board){
        logger.debug("Checking gameBoard if player has died.");
        if(board != null && board.hasGameStarted()){
            logger.debug("Game started and player is dead=" + board.hasPlayerDied());
            return board.hasPlayerDied();
        }
        logger.debug("Game has not started");
        return true;
    }

    public boolean isPlayerDead(int i){
        logger.debug("entering isPlayerDead for board index=" + i);
        return isPlayerDead(getGameBoard(i));
    }

    private void init(){
        resetGameBoards();
    }

    public YokelGameBoard getGameBoard(int i){
        if(i < 0 || i > 8) throw new GdxRuntimeException("Invalid Gameboard index: " + i);
        return gameBoards[i];
    }

    public ObjectMap<Integer, YokelGameBoard> getActiveGameBoards(){
        //TODO: Should be a persisted map in the object, not new everytime
        ObjectMap<Integer, YokelGameBoard> returnBoards = GdxMaps.newObjectMap();
        int index = 0;
        for(YokelGameBoard board : gameBoards){
            if(board != null && board.hasGameStarted() && !board.hasPlayerDied()){
                returnBoards.put(index, board);
            }
            ++index;
        }
        return returnBoards;
    }

    private long getSeed() {
        return seed;
    }

    public void setSeed(long seed){
        this.seed = seed;
        resetGameBoards();
    }

    public void resetGameBoards(){
        long seed = getSeed();
        isGameRunning = false;
        for(int i = 0; i < 8; i++){
            YokelGameBoard gameBoard = gameBoards[i];
            if (gameBoard != null) gameBoard.dispose();
            YokelGameBoard newBoard = new YokelGameBoard(seed);
            newBoard.setName(i + "");
            gameBoards[i] = newBoard;
        }
    }

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

    private Stack<Integer> popPowersFromBoard(int boardIndex, int amount){
        Stack<Integer> powerStack = new Stack<>();
        //given board, get player powers
        YokelGameBoard gameBoard = getGameBoard(boardIndex);
        Queue<Integer> powers = gameBoard.getPowers();
        if(powers != null){
            //pop next power
            while(amount-- > 0){
                if(!powers.isEmpty()){
                    powerStack.push(powers.removeFirst());
                }
            }
        }
        return powerStack;
    }

    private void handleAttackGivenAttack(int target, int attackBlock){
        //Get all active boards
        if(attackBlock != YokelBlock.CLEAR_BLOCK){
            ObjectMap<Integer, YokelGameBoard> activeBoards = getActiveGameBoards();
            YokelGameBoard gameBoard = activeBoards.get(target);

            if(gameBoard != null){
                boolean isOffensive = YokelBlockEval.isOffensive(attackBlock);
                int value = YokelBlockEval.getCellFlag(attackBlock);

                if(value == YokelBlock.Oy_BLOCK) {
                    //If offensive Yokel.L, set medusa next piece to target
                    if(isOffensive){
                        gameBoard.addSpecialPiece(MEDUSA_GAME_PIECE);
                    } else {
                        //If defensive Yokel.L, set midas next piece to target
                        gameBoard.addSpecialPiece(MIDAS_GAME_PIECE);
                    }
                } else if(value == YokelBlock.EX_BLOCK) {
                    if(isOffensive){
                        //if offensive Yokel.Ex, need to remove powers from target
                        int level = YokelBlockEval.getPowerLevel(value);
                        Stack<Integer> blockStack = popPowersFromBoard(target, level);
                        gameBoard.addRemovedPowersToBoard(blockStack);
                    } else {
                        gameBoard.handlePower(attackBlock);
                    }
                } else {
                    gameBoard.handlePower(attackBlock);
                }
            }
        }
    }

    public void handleTargetAttack(int currentBoardSeat, int seatTarget){
        Stack<Integer> blocks = popPowersFromBoard(currentBoardSeat, 1);

        if(blocks.size() > 0) {
            int block = blocks.pop();
            handleAttackGivenAttack(seatTarget, block);
        }
    }

    public void handleRandomAttack(int currentBoardSeat){
        Stack<Integer> blocks = popPowersFromBoard(currentBoardSeat, 1);

        if(blocks.size() > 0) {
            int block = blocks.pop();
            int partnerIndex = (currentBoardSeat % 2 == 0)? currentBoardSeat + 1 : currentBoardSeat - 1;

            Array<Integer> boardIndexes = GdxArrays.newArray();
            for(int i = 0; i < 8; i++){
                boardIndexes.add(i);
            }

            //Offensive should only hit random enemies.
            //Defensive should only hit you and your partner.
            Iterator<Integer> iter = YokelUtilities.getArrayIterator(boardIndexes);
            while(iter.hasNext()){
                int x = iter.next();

                if(YokelBlockEval.isOffensive(block)){
                    if(x == currentBoardSeat || x == partnerIndex){
                        iter.remove();
                    }
                } else {
                    if(x != currentBoardSeat && x != partnerIndex){
                        iter.remove();
                    }
                }
            }

            ObjectMap<Integer, YokelGameBoard> activeGameboards = getActiveGameBoards();
            Array<Integer> activeBoards = YokelUtilities.getMapKeys(activeGameboards).toArray();
            Iterator<Integer> active = YokelUtilities.getArrayIterator(boardIndexes);

            while(active.hasNext()){
                int a = active.next();
                if(!activeBoards.contains(a, true)){
                    active.remove();
                }
            }

            YokelUtilities.flushIterator(iter);
            YokelUtilities.flushIterator(YokelUtilities.getArrayIterator(activeBoards));
            YokelUtilities.flushIterator(active);

            int index = MathUtils.random(boardIndexes.size - 1);
            handleAttackGivenAttack(boardIndexes.get(index), block);
        }
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
        logger.debug("Enter startGame()");
        if(!isGameRunning){
            logger.debug("isTableReady=" + isGameReady());
            isGameRunning = isGameReady();
        }
        logger.debug("isGameRunning=" + isGameRunning);
        logger.debug("Exit startGame()=");
        return isGameRunning;
    }

    public boolean stopGame(){
        for(YokelGameBoard gameboard : gameBoards){
            if(gameboard != null){
                gameboard.end();
                showGameOver = true;
            }
        }
        return isGameRunning = hasGameStarted = false;
    }

    public boolean isRunning() {
        return isGameRunning;
    }

    public boolean isGameOver(){
        //TODO: implement action history to break tie with last placed block.
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

        if(group1won){
            setWinners(getPlayerFromBoard(table.getSeat(0)), getPlayerFromBoard(table.getSeat(1)));
        }

        if(group2won){
            setWinners(getPlayerFromBoard(table.getSeat(2)), getPlayerFromBoard(table.getSeat(3)));
        }

        if(group3won){
            setWinners(getPlayerFromBoard(table.getSeat(4)), getPlayerFromBoard(table.getSeat(5)));
        }

        if(group4won){
            setWinners(getPlayerFromBoard(table.getSeat(6)), getPlayerFromBoard(table.getSeat(7)));
        }

        return group1won || group2won || group3won || group4won;
    }

    private void setWinners(YokelPlayer player1, YokelPlayer player2) {
        winners.clear();
        if(player1 != null){
            winners.add(player1);
        }

        if(player2 != null){
            winners.add(player2);
        }
    }

    public Array<YokelPlayer> getWinners(){
        return winners;
    }

    private YokelPlayer getPlayerFromBoard(YokelSeat seat){
        if(seat != null){
            return seat.getSeatedPlayer();
        }
        return null;
    }

    //TODO: Remove test methods
    public void testMedusa(int target) {
        ObjectMap<Integer, YokelGameBoard> activeBoards = getActiveGameBoards();
        YokelGameBoard gameBoard = activeBoards.get(target);

        if(gameBoard != null){
            System.out.println("Added a Medusa");
            gameBoard.addSpecialPiece(MEDUSA_GAME_PIECE);
        }
    }

    public void testMidas(int target) {
        ObjectMap<Integer, YokelGameBoard> activeBoards = getActiveGameBoards();
        YokelGameBoard gameBoard = activeBoards.get(target);

        if(gameBoard != null){
            System.out.println("Added a Midas");
            gameBoard.addSpecialPiece(MIDAS_GAME_PIECE);
        }
    }

    public void showGameBoard(int target) {
        System.out.println(gameBoards[target]);
    }

    public void testGameBoard(int target) {
        System.out.println(gameBoards[target]);
        int[][] cells = gameBoards[target].getCells();

        for(int[] inner : cells){
            System.out.println(Arrays.toString(inner));
        }
        Gdx.app.exit();
    }

    @Override
    public void dispose() {
        if(winners != null) {
            winners.clear();
        }
    }

    public boolean isGameReady() {
        return table.isTableStartReady();
    }

    public boolean isStartable() {
        return this.table.isStartable();
    }

    private static class GameState {

    }
}