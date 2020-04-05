package net.asg.games.provider.actors;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

import net.asg.games.game.objects.YokelBlock;
import net.asg.games.game.objects.YokelPiece;
import net.asg.games.utils.UIUtil;
import net.asg.games.utils.Util;

public class GamePiece extends Table implements GameObject {
    static final private int GRID_OFFSET = 6;

    public GamePiece(Skin skin){
        this(skin,null,null,null);
    }

    public GamePiece(Skin skin, GameBlock top, GameBlock mid, GameBlock bottom){
        setSkin(skin);
        initialize(top, mid, bottom);
    }

    public GamePiece(Skin skin, int top, int mid, int bottom){
        this(skin, UIUtil.getInstance().getGameBlock(top), UIUtil.getInstance().getGameBlock(mid), UIUtil.getInstance().getGameBlock(bottom));
    }

    public GamePiece(Skin skin, String data){
        this(skin);
        setSkin(skin);
        setData(data);
    }

    private void initialize(GameBlock top, GameBlock middle, GameBlock bottom){
        if(top == null){
            top = new GameBlock(getSkin(), YokelBlock.CLEAR_BLOCK);
        }
        if(middle == null){
            middle = new GameBlock(getSkin(), YokelBlock.CLEAR_BLOCK);
        }
        if(bottom == null){
            bottom = new GameBlock(getSkin(), YokelBlock.CLEAR_BLOCK);
        }
        add(top).row();
        add(middle).row();
        add(bottom).row();
    }

    private GameBlock getBlock(GameBlock block){
        if(block == null){
            return getClearBlock();
        } else {
            return block;
        }
    }

    public void setDebug(boolean enabled) {
        super.setDebug(enabled);
    }

    private GameBlock getClearBlock(){
        return UIUtil.getInstance().getGameBlock(YokelBlock.CLEAR_BLOCK);
    }

    @Override
    public void setData(String data) {
        YokelPiece piece = Util.getObjectFromJsonString(YokelPiece.class, data);
        if(piece != null){
            this.clearChildren();
            initialize(UIUtil.getInstance().getGameBlock(piece.getValueAt(0)),
                        UIUtil.getInstance().getGameBlock(piece.getValueAt(1)),
                        UIUtil.getInstance().getGameBlock(piece.getValueAt(2)));
        }
    }
}