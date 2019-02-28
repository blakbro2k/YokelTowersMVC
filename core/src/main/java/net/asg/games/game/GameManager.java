package net.asg.games.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.Json;
import com.github.czyzby.autumn.annotation.Component;
import com.github.czyzby.autumn.annotation.Initiate;
import com.github.czyzby.autumn.annotation.Inject;
import com.github.czyzby.autumn.annotation.OnMessage;
import com.github.czyzby.autumn.mvc.component.ui.InterfaceService;
import com.github.czyzby.autumn.mvc.config.AutumnMessage;
import com.github.czyzby.autumn.mvc.stereotype.Asset;
import com.github.czyzby.lml.annotation.LmlActor;

import net.asg.games.controller.UITestController;

@Component
public class GameManager {
    @Inject private InterfaceService interfaceService;
    @Inject private UITestController uiView;

    @Initiate
    @SuppressWarnings("unchecked")
    private void loadGameData() {

    }

    public void initiateGame() {
        System.out.println("initiateGame");
        System.out.println("uiView=" + uiView);
        System.out.println("uiView.bashBlockImage=" + uiView.bashBlockImage);

        uiView.yBlockImage.setDrawable(interfaceService.getSkin(), "Y_block");
        uiView.oBlockImage.setDrawable(interfaceService.getSkin(), "O_block");
        uiView.kBlockImage.setDrawable(interfaceService.getSkin(), "K_block");
        uiView.eBlockImage.setDrawable(interfaceService.getSkin(), "E_block");
        uiView.lBlockImage.setDrawable(interfaceService.getSkin(), "L_block");
        uiView.bashBlockImage.setDrawable(interfaceService.getSkin(), "Bash_block");
        System.out.println("Regions=" + getRegions("defense_Bash_block"));
        System.out.println("Regions=" + interfaceService.getSkin().getRegions("defense_Bash_block"));
        System.out.println("retion(Bash_block_Broken_2)=" + interfaceService.getSkin().getRegion("Bash_block_Broken_2"));
    }

    public void update(){}
    public void handleMoveRight(){}
    public void handleMoveLeft(){}
    public void handleSetPiece(){}
    public String[] getBoardState(){ return null;}

    public Array<TextureRegion> getRegions(String regionName) {
        int i = 0;
        StringBuilder regionNames = (new StringBuilder()).append(regionName).append("_");
        TextureRegion region = getRegion(regionNames, i);

        Array<TextureRegion> regions = null;
        while(region != null){
            i++;
            region = getRegion(regionNames, i);
            addRegion(regions,region);
        }
        return regions;
    }

    private TextureRegion getRegion(StringBuilder regionNames, int i){
        try{
            return interfaceService.getSkin().getRegion(regionNames.append(i).toString());
        } catch(GdxRuntimeException e) {
            return null;
        }
    }

    private void addRegion(Array<TextureRegion> regions, TextureRegion region){
        if(region != null){
            if(regions == null){
                regions = new Array<>();
            }
            regions.add(region);
        }
    }
}