package net.asg.games.game.objects;

import junit.framework.Assert;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class YokelBlockEvalTest {

    @BeforeMethod
    public void setUp() {
    }

    @AfterMethod
    public void tearDown() {
    }

    public void printBlockLabel(int block){
        System.out.println(YokelBlockEval.getPowerLabel(block) + " : " + YokelBlockEval.getPowerUseDescriptionLabel(block));
    }

    @Test
    public void yokelblock_Y_Test() throws Exception {
        System.out.println("Start yokelblock_Y_Test()");
        int y_block = YokelBlockEval.Y_BLOCK;
        int block = 0;
        Assert.assertEquals(YokelBlockEval.Y_BLOCK, y_block);

        System.out.println("Y Block Flag: " + y_block);
        //OFFENSIVE_MINOR = 3;
        block = YokelBlockEval.setPowerFlag(y_block, YokelBlockEval.OFFENSIVE_MINOR);
        System.out.println("OFFENSIVE_MINOR: " + block);
        System.out.println("POWER_BLOCK: " + YokelBlockEval.addPowerBlockFlag(block));
        printBlockLabel(block);

        //OFFENSIVE_REGULAR = 5;
        block = YokelBlockEval.setPowerFlag(y_block, YokelBlockEval.OFFENSIVE_REGULAR);
        System.out.println("OFFENSIVE_REGULAR: " + block);
        System.out.println("POWER_BLOCK: " + YokelBlockEval.addPowerBlockFlag(block));
        printBlockLabel(block);

        //OFFENSIVE_MEGA = 7;
        block = YokelBlockEval.setPowerFlag(y_block, YokelBlockEval.OFFENSIVE_MEGA);
        System.out.println("OFFENSIVE_MEGA: " + block);
        System.out.println("POWER_BLOCK: " + YokelBlockEval.addPowerBlockFlag(block));
        printBlockLabel(block);

        //DEFENSIVE_MINOR = 2;
        block = YokelBlockEval.setPowerFlag(y_block, YokelBlockEval.DEFENSIVE_MINOR);
        System.out.println("DEFENSIVE_MINOR: " + block);
        System.out.println("POWER_BLOCK: " + YokelBlockEval.addPowerBlockFlag(block));
        printBlockLabel(block);

        //DEFENSIVE_REGULAR = 4;
        block = YokelBlockEval.setPowerFlag(y_block, YokelBlockEval.DEFENSIVE_REGULAR);
        System.out.println("DEFENSIVE_REGULAR: " + block);
        System.out.println("POWER_BLOCK: " + YokelBlockEval.addPowerBlockFlag(block));
        printBlockLabel(block);

        //DEFENSIVE_MEGA = 6;
        block = YokelBlockEval.setPowerFlag(y_block, YokelBlockEval.DEFENSIVE_MEGA);
        System.out.println("DEFENSIVE_MEGA: " + block);
        System.out.println("POWER_BLOCK: " + YokelBlockEval.addPowerBlockFlag(block));
        printBlockLabel(block);

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
        int o_block = YokelBlockEval.O_BLOCK;
        int block = 0;
        Assert.assertEquals(YokelBlockEval.O_BLOCK, o_block);

        System.out.println("O Block Flag: " + o_block);
        //OFFENSIVE_MINOR = 3;
        block = YokelBlockEval.setPowerFlag(o_block, YokelBlockEval.OFFENSIVE_MINOR);
        System.out.println("OFFENSIVE_MINOR: " + block);
        System.out.println("POWER_LEVEL: " + YokelBlockEval.getPowerLevel(block));

        //OFFENSIVE_REGULAR = 5;
        block = YokelBlockEval.setPowerFlag(o_block, YokelBlockEval.OFFENSIVE_REGULAR);
        System.out.println("OFFENSIVE_REGULAR: " + block);

        //OFFENSIVE_MEGA = 7;
        block = YokelBlockEval.setPowerFlag(o_block, YokelBlockEval.OFFENSIVE_MEGA);
        System.out.println("OFFENSIVE_MEGA: " + block);

        //DEFENSIVE_MINOR = 2;
        block = YokelBlockEval.setPowerFlag(o_block, YokelBlockEval.DEFENSIVE_MINOR);
        System.out.println("DEFENSIVE_MINOR: " + block);

        //DEFENSIVE_REGULAR = 4;
        block = YokelBlockEval.setPowerFlag(o_block, YokelBlockEval.DEFENSIVE_REGULAR);
        System.out.println("DEFENSIVE_REGULAR: " + block);

        //DEFENSIVE_MEGA = 6;
        block = YokelBlockEval.setPowerFlag(o_block, YokelBlockEval.DEFENSIVE_MEGA);
        System.out.println("DEFENSIVE_MEGA: " + block);

        Assert.assertFalse(YokelBlockEval.hasBrokenFlag(o_block));
        o_block = YokelBlockEval.addBrokenFlag(o_block);
        Assert.assertTrue(YokelBlockEval.hasBrokenFlag(o_block));
        o_block = YokelBlockEval.removeBrokenFlag(o_block);
        Assert.assertFalse(YokelBlockEval.hasBrokenFlag(o_block));

        System.out.println("End yokelblock_O_Test()");
    }

    @Test
    public void yokelblock_K_Test() throws Exception {
        System.out.println("Start yokelblock_K_Test()");
        int k_block = YokelBlockEval.K_BLOCK;
        Assert.assertEquals(YokelBlockEval.K_BLOCK, k_block);

        System.out.println(YokelBlockEval.setValueFlag(k_block, 4));
        System.out.println("End yokelblock_K_Test()");
        throw new Exception("Test is not complete.");
    }

    @Test
    public void yokelblock_E_Test() throws Exception {
        System.out.println("Start yokelblock_E_Test()");
        int e_block = YokelBlockEval.E_BLOCK;
        Assert.assertEquals(YokelBlockEval.E_BLOCK, e_block);

        System.out.println(YokelBlockEval.setValueFlag(e_block, 4));
        System.out.println("End yokelblock_E_Test()");
        throw new Exception("Test is not complete.");
    }

    @Test
    public void yokelblock_L_Test() throws Exception {
        System.out.println("Start yokelblock_L_Test()");
        int l_block = YokelBlockEval.L_BLOCK;
        Assert.assertEquals(YokelBlockEval.L_BLOCK, l_block);

        System.out.println(YokelBlockEval.setValueFlag(l_block, 4));
        System.out.println("End yokelblock_L_Test()");
        throw new Exception("Test is not complete.");
    }

    @Test
    public void yokelblock_EX_Test() throws Exception {
        System.out.println("Start yokelblock_EX_Test()");
        int ex_block = YokelBlockEval.EX_BLOCK;
        Assert.assertEquals(YokelBlockEval.EX_BLOCK, ex_block);

        System.out.println(YokelBlockEval.setValueFlag(ex_block, 4));
        System.out.println("End yokelblock_EX_Test()");
        throw new Exception("Test is not complete.");
    }

    @Test
    public void testGetCellFlag() throws Exception {
        throw new Exception("Test not implemented.");
    }

    @Test
    public void testSetValueFlag() throws Exception {
        throw new Exception("Test not implemented.");
    }

    @Test
    public void testHasAddedByYahooFlag() throws Exception {
        throw new Exception("Test not implemented.");
    }

    @Test
    public void testAddArtificialFlag() throws Exception {
        throw new Exception("Test not implemented.");
    }

    @Test
    public void testBrokenFlags() throws Exception {
        throw new Exception("Test not implemented.");
    }

    @Test
    public void testAddBrokenFlag() throws Exception {
        throw new Exception("Test not implemented.");
    }

    @Test
    public void testRemoveBrokenFlag() throws Exception {
        throw new Exception("Test not implemented.");
    }

    @Test
    public void testPartnerBreakFlags() throws Exception {
        System.out.println("Start testPartnerBreakFlag()");
        //Normal Blocks

        int block = YokelBlockEval.Y_BLOCK;
        int actual = YokelBlockEval.removePartnerBreakFlag(block);
        Assert.assertEquals(false, YokelBlockEval.hasPartnerBreakFlag(actual));
        actual = YokelBlockEval.addPartnerBreakFlag(block);
        Assert.assertEquals(true, YokelBlockEval.hasPartnerBreakFlag(actual));
        actual = YokelBlockEval.removePartnerBreakFlag(block);
        Assert.assertEquals(false, YokelBlockEval.hasPartnerBreakFlag(actual));

        block = YokelBlockEval.O_BLOCK;
        Assert.assertEquals(false, YokelBlockEval.hasPartnerBreakFlag(actual));
        actual = YokelBlockEval.addPartnerBreakFlag(block);
        Assert.assertEquals(true, YokelBlockEval.hasPartnerBreakFlag(actual));
        actual = YokelBlockEval.removePartnerBreakFlag(block);
        Assert.assertEquals(false, YokelBlockEval.hasPartnerBreakFlag(actual));

        block = YokelBlockEval.K_BLOCK;
        Assert.assertEquals(false, YokelBlockEval.hasPartnerBreakFlag(actual));
        actual = YokelBlockEval.addPartnerBreakFlag(block);
        Assert.assertEquals(true, YokelBlockEval.hasPartnerBreakFlag(actual));
        actual = YokelBlockEval.removePartnerBreakFlag(block);
        Assert.assertEquals(false, YokelBlockEval.hasPartnerBreakFlag(actual));

        block = YokelBlockEval.E_BLOCK;
        Assert.assertEquals(false, YokelBlockEval.hasPartnerBreakFlag(actual));
        actual = YokelBlockEval.addPartnerBreakFlag(block);
        Assert.assertEquals(true, YokelBlockEval.hasPartnerBreakFlag(actual));
        actual = YokelBlockEval.removePartnerBreakFlag(block);
        Assert.assertEquals(false, YokelBlockEval.hasPartnerBreakFlag(actual));

        block = YokelBlockEval.L_BLOCK;
        Assert.assertEquals(false, YokelBlockEval.hasPartnerBreakFlag(actual));
        actual = YokelBlockEval.addPartnerBreakFlag(block);
        Assert.assertEquals(true, YokelBlockEval.hasPartnerBreakFlag(actual));
        actual = YokelBlockEval.removePartnerBreakFlag(block);
        Assert.assertEquals(false, YokelBlockEval.hasPartnerBreakFlag(actual));

        block = YokelBlockEval.EX_BLOCK;
        Assert.assertEquals(false, YokelBlockEval.hasPartnerBreakFlag(actual));
        actual = YokelBlockEval.addPartnerBreakFlag(block);
        Assert.assertEquals(true, YokelBlockEval.hasPartnerBreakFlag(actual));
        actual = YokelBlockEval.removePartnerBreakFlag(block);
        Assert.assertEquals(false, YokelBlockEval.hasPartnerBreakFlag(actual));

        //Offensive Blocks
        /*block = YokelBlockEval.addPowerBlockFlag(YokelBlockEval.Y_BLOCK, YokelBlockEval.OFFENSIVE_MINOR);
        actual = YokelBlockEval.removePartnerBreakFlag(block);
        Assert.assertEquals(false, YokelBlockEval.hasPartnerBreakFlag(actual));
        actual = YokelBlockEval.addPartnerBreakFlag(block);
        Assert.assertEquals(true, YokelBlockEval.hasPartnerBreakFlag(actual));
        actual = YokelBlockEval.removePartnerBreakFlag(block);
        Assert.assertEquals(false, YokelBlockEval.hasPartnerBreakFlag(actual));

        block = YokelBlockEval.O_BLOCK;
        Assert.assertEquals(false, YokelBlockEval.hasPartnerBreakFlag(actual));
        actual = YokelBlockEval.addPartnerBreakFlag(block);
        Assert.assertEquals(true, YokelBlockEval.hasPartnerBreakFlag(actual));
        actual = YokelBlockEval.removePartnerBreakFlag(block);
        Assert.assertEquals(false, YokelBlockEval.hasPartnerBreakFlag(actual));

        block = YokelBlockEval.K_BLOCK;
        Assert.assertEquals(false, YokelBlockEval.hasPartnerBreakFlag(actual));
        actual = YokelBlockEval.addPartnerBreakFlag(block);
        Assert.assertEquals(true, YokelBlockEval.hasPartnerBreakFlag(actual));
        actual = YokelBlockEval.removePartnerBreakFlag(block);
        Assert.assertEquals(false, YokelBlockEval.hasPartnerBreakFlag(actual));

        block = YokelBlockEval.E_BLOCK;
        Assert.assertEquals(false, YokelBlockEval.hasPartnerBreakFlag(actual));
        actual = YokelBlockEval.addPartnerBreakFlag(block);
        Assert.assertEquals(true, YokelBlockEval.hasPartnerBreakFlag(actual));
        actual = YokelBlockEval.removePartnerBreakFlag(block);
        Assert.assertEquals(false, YokelBlockEval.hasPartnerBreakFlag(actual));

        block = YokelBlockEval.L_BLOCK;
        Assert.assertEquals(false, YokelBlockEval.hasPartnerBreakFlag(actual));
        actual = YokelBlockEval.addPartnerBreakFlag(block);
        Assert.assertEquals(true, YokelBlockEval.hasPartnerBreakFlag(actual));
        actual = YokelBlockEval.removePartnerBreakFlag(block);
        Assert.assertEquals(false, YokelBlockEval.hasPartnerBreakFlag(actual));

        block = YokelBlockEval.EX_BLOCK;
        Assert.assertEquals(false, YokelBlockEval.hasPartnerBreakFlag(actual));
        actual = YokelBlockEval.addPartnerBreakFlag(block);
        Assert.assertEquals(true, YokelBlockEval.hasPartnerBreakFlag(actual));
        actual = YokelBlockEval.removePartnerBreakFlag(block);
        Assert.assertEquals(false, YokelBlockEval.hasPartnerBreakFlag(actual));*/
        System.out.println("End testPartnerBreakFlag()");
    }

    @Test
    public void testGetID() throws Exception {
        throw new Exception("Test not implemented.");
    }

    @Test
    public void testSetIDFlag() throws Exception {
        throw new Exception("Test not implemented.");
    }

    @Test
    public void testGetPowerFlag() throws Exception {
        throw new Exception("Test not implemented.");
    }

    @Test
    public void testSetPowerFlag() throws Exception {
        throw new Exception("Test not implemented.");
    }

    @Test
    public void testHasSpecialFlag() throws Exception {
        throw new Exception("Test not implemented.");
    }

    @Test
    public void testAddSpecialFlag() throws Exception {
        throw new Exception("Test not implemented.");
    }

    @Test
    public void testHasPowerBlockFlag() throws Exception {
        throw new Exception("Test not implemented.");
    }

    @Test
    public void testAddPowerBlockFlag() throws Exception {
        throw new Exception("Test not implemented.");
    }

    @Test
    public void testIsOffensive() throws Exception {
        throw new Exception("Test not implemented.");
    }

    @Test
    public void testGetPowerLevel() throws Exception {
        throw new Exception("Test not implemented.");
    }

    @Test
    public void testGetPowerLabel() throws Exception {
        throw new Exception("Test not implemented.");
    }

    @Test
    public void testGetPowerUseDescriptionLabel() throws Exception {
        throw new Exception("Test not implemented.");
    }
}