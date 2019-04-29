package net.asg.games.provider.actors;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.github.czyzby.lml.scene2d.ui.reflected.AnimatedImage;

import net.asg.games.utils.UIUtil;
import net.asg.games.utils.Util;

public class GamePiece extends Table {
    public static final String TOP_ATTR = "top";
    public static final String MIDDLE_ATTR = "middle";
    public static final String BOTTOM_ATTR = "bottom";
    private ObjectMap<String, GameBlock> uiBlocks;



    public GamePiece(Skin skin, GameBlock top, GameBlock mid, GameBlock bottom){
        setSkin(skin);
        this.uiBlocks = new ObjectMap<>();
        setDebug(true);
        setTopBlock(top);
        setMiddleBlock(mid);
        setBottomBlock(bottom);
        initializeUiCells();
    }

    public GamePiece(Skin skin, int top, int mid, int bottom){
        this(skin, UIUtil.getInstance().getGameBlock(top), UIUtil.getInstance().getGameBlock(mid), UIUtil.getInstance().getGameBlock(bottom));
    }

    private void initializeUiCells(){
        add(getTopBlock()).row();
        add(getMiddleBlock()).row();
        add(getBottomBlock());
    }

    public GameBlock getTopBlock() {
        return uiBlocks.get(TOP_ATTR);
    }

    public void setTopBlock(GameBlock top) {
        GameBlock uiBlock = uiBlocks.get(TOP_ATTR);
        if(uiBlock != null){
            if(top != null){
                System.out.println("setting top image:" + top.getImage().getName());
                uiBlock.setImage(top.getImage());
            }
        } else {
            uiBlocks.put(TOP_ATTR, UIUtil.getInstance().getGameBlock(0));
        }
    }

    public GameBlock getMiddleBlock() {
        return uiBlocks.get(MIDDLE_ATTR);
    }

    public void setMiddleBlock(GameBlock middle) {
        GameBlock uiBlock = uiBlocks.get(MIDDLE_ATTR);
        if(uiBlock != null){
            if(middle != null){
                System.out.println("setting middle image:" + middle.getImage().getName());

                uiBlock.setImage(middle.getImage());
            }
        } else {
            uiBlocks.put(MIDDLE_ATTR, UIUtil.getInstance().getGameBlock(0));
        }
    }

    public GameBlock getBottomBlock() {
        return uiBlocks.get(BOTTOM_ATTR);
    }

    public void setBottomBlock(GameBlock bottom) {
        GameBlock uiBlock = uiBlocks.get(BOTTOM_ATTR);
        if(uiBlock != null){
            if(bottom != null){
                System.out.println("setting bottom image:" + bottom.getImage().getName());

                uiBlock.setImage(bottom.getImage());
            }
        } else {
            uiBlocks.put(BOTTOM_ATTR, UIUtil.getInstance().getGameBlock(0));
        }
    }

    public void cycleUp(){
        GameBlock topBlock = getTopBlock();
        GameBlock midBlock = getMiddleBlock();
        GameBlock bottomBlock = getBottomBlock();

        setTopBlock(bottomBlock);
        setMiddleBlock(topBlock);
        setBottomBlock(midBlock);
    }

    public void cycleDown(){
        GameBlock topBlock = getTopBlock();
        GameBlock midBlock = getMiddleBlock();
        GameBlock bottomBlock = getBottomBlock();

        setTopBlock(midBlock);
        setMiddleBlock(bottomBlock);
        setBottomBlock(topBlock);
    }

    public void setData(String[] data) {
        if(data != null && data.length < 4){
            setTopBlock(UIUtil.getInstance().getGameBlock(Util.otoi(data[0])));
            setMiddleBlock(UIUtil.getInstance().getGameBlock(Util.otoi(data[1])));
            setBottomBlock(UIUtil.getInstance().getGameBlock(Util.otoi(data[2])));
        }
    }

    public Array<AnimatedImage> getBlockImages(){
        Array<AnimatedImage> images = new Array<AnimatedImage>();
        images.add(getTopBlock().getImage());
        images.add(getMiddleBlock().getImage());
        images.add(getBottomBlock().getImage());
        return images;
    }
}
