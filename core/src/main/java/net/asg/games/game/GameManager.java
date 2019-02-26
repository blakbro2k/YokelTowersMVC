package net.asg.games.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.github.czyzby.autumn.annotation.Initiate;
import com.github.czyzby.autumn.annotation.Inject;
import com.github.czyzby.autumn.annotation.OnMessage;
import com.github.czyzby.autumn.mvc.component.ui.InterfaceService;
import com.github.czyzby.autumn.mvc.config.AutumnMessage;
import com.github.czyzby.autumn.mvc.stereotype.Asset;

public class GameManager {
    @Inject private InterfaceService interfaceService;

    private boolean regionsAssigned;

    @Asset("game/game.atlas") private TextureAtlas gameAtlas;



    @OnMessage(AutumnMessage.ASSETS_LOADED)
    private void assignRegions() {
        if (!regionsAssigned) {
            regionsAssigned = true;
            interfaceService.getSkin().addRegions(gameAtlas);
        }
    }

    @Initiate
    @SuppressWarnings("unchecked")
    private void loadGameData() {

    }

    public void initiateGame() {

    }






    public void update(){}
    public void handleMoveRight(){}
    public void handleMoveLeft(){}
    public void handleSetPiece(){}
    public String[] getBoardState(){ return null;}
}
