package net.asg.games.provider.actors;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

import net.asg.games.game.objects.YokelBlock;
import net.asg.games.game.objects.YokelPiece;
import net.asg.games.utils.Util;

public class GamePiece extends Table implements GameObject {
    static final private int GRID_OFFSET = 6;

    public GamePiece(Skin skin){
        this(skin, YokelBlock.CLEAR_BLOCK, YokelBlock.CLEAR_BLOCK, YokelBlock.CLEAR_BLOCK);
    }

    public GamePiece(Skin skin, GameBlock top, GameBlock mid, GameBlock bottom){
        setSkin(skin);
        initialize(top, mid, bottom);
    }

    public GamePiece(Skin skin, int top, int mid, int bottom){
        this(skin, Util.getBlock(top), Util.getBlock(mid), Util.getBlock(bottom));
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

    public void setDebug(boolean enabled) {
        super.setDebug(enabled);
    }

    @Override
    public void setData(String data) {
        YokelPiece piece = Util.getObjectFromJsonString(YokelPiece.class, data);
        if(piece != null){
            this.clearChildren();
            initialize(Util.getBlock(piece.getValueAt(0)),
                    Util.getBlock(piece.getValueAt(1)),
                    Util.getBlock(piece.getValueAt(2)));
        }
    }
}