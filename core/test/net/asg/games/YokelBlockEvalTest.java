package net.asg.games;

import net.asg.games.game.objects.YokelBlockEval;

import org.junit.Assert;
import org.junit.Test;

public class YokelBlockEvalTest {
    public void printBlockLabel(int block){
        System.out.println(YokelBlockEval.getPowerLabel(block) + " : " + YokelBlockEval.getPowerUseDescriptionLabel(block));
    }


    @Test
    public void yokelblock_Y_Test() throws Exception {
        System.out.println("Start yokelblock_Y_Test()");
        int y_block = YokelBlockEval.NORMAL_Y;
        Assert.assertEquals(YokelBlockEval.NORMAL_Y, y_block);

        System.out.println("Y Block Flag: " + y_block);
        printBlockLabel(YokelBlockEval.setPowerFlag(y_block, 2));
        printBlockLabel(YokelBlockEval.setPowerFlag(y_block, 3));
        printBlockLabel(YokelBlockEval.setPowerFlag(y_block, 4));
        printBlockLabel(YokelBlockEval.setPowerFlag(y_block, 5));
        printBlockLabel(YokelBlockEval.setPowerFlag(y_block, 6));
        printBlockLabel(YokelBlockEval.setPowerFlag(y_block, 7));

        Assert.assertFalse(YokelBlockEval.hasBrokenFlag(y_block));
        y_block = YokelBlockEval.addBrokenFlag(y_block);
        Assert.assertTrue(YokelBlockEval.hasBrokenFlag(y_block));
        y_block = YokelBlockEval.removeBrokenFlag(y_block);
        Assert.assertFalse(YokelBlockEval.hasBrokenFlag(y_block));

        System.out.println("End yokelblock_Y_Test()");

    }

    @Test
    public void yokelblock_O_Test() throws Exception {
        System.out.println("Start yokelblock_O_Test()");
        int o_block = YokelBlockEval.NORMAL_O;
        Assert.assertEquals(YokelBlockEval.NORMAL_O, o_block);

        System.out.println("O Block Flag: " + o_block);
        printBlockLabel(YokelBlockEval.setPowerFlag(o_block, 2));
        printBlockLabel(YokelBlockEval.setPowerFlag(o_block, 3));
        printBlockLabel(YokelBlockEval.setPowerFlag(o_block, 4));
        printBlockLabel(YokelBlockEval.setPowerFlag(o_block, 5));
        printBlockLabel(YokelBlockEval.setPowerFlag(o_block, 6));
        printBlockLabel(YokelBlockEval.setPowerFlag(o_block, 7));

        System.out.println(YokelBlockEval.setValueFlag(o_block, 4));
        System.out.println("End yokelblock_O_Test()");
        throw new Exception("Test is not complete.");
    }

    @Test
    public void yokelblock_K_Test() throws Exception {
        System.out.println("Start yokelblock_K_Test()");
        int k_block = YokelBlockEval.NORMAL_K;
        Assert.assertEquals(YokelBlockEval.NORMAL_K, k_block);

        System.out.println(YokelBlockEval.setValueFlag(k_block, 4));
        System.out.println("End yokelblock_K_Test()");
        throw new Exception("Test is not complete.");
    }

    @Test
    public void yokelblock_E_Test() throws Exception {
        System.out.println("Start yokelblock_E_Test()");
        int e_block = YokelBlockEval.NORMAL_E;
        Assert.assertEquals(YokelBlockEval.NORMAL_E, e_block);

        System.out.println(YokelBlockEval.setValueFlag(e_block, 4));
        System.out.println("End yokelblock_E_Test()");
        throw new Exception("Test is not complete.");
    }

    @Test
    public void yokelblock_L_Test() throws Exception {
        System.out.println("Start yokelblock_L_Test()");
        int l_block = YokelBlockEval.NORMAL_L;
        Assert.assertEquals(YokelBlockEval.NORMAL_L, l_block);

        System.out.println(YokelBlockEval.setValueFlag(l_block, 4));
        System.out.println("End yokelblock_L_Test()");
        throw new Exception("Test is not complete.");
    }

    @Test
    public void yokelblock_EX_Test() throws Exception {
        System.out.println("Start yokelblock_EX_Test()");
        int ex_block = YokelBlockEval.NORMAL_EX;
        Assert.assertEquals(YokelBlockEval.NORMAL_EX, ex_block);

        System.out.println(YokelBlockEval.setValueFlag(ex_block, 4));
        System.out.println("End yokelblock_EX_Test()");
        throw new Exception("Test is not complete.");
    }
}