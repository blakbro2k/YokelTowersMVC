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
    public void descriptionTest() throws Exception {
        System.out.println(YokelBlockEval.getPowerUseDescriptionLabel(YokelBlockEval.setPowerFlag(YokelBlock.Y_BLOCK, YokelBlock.OFFENSIVE_MINOR)) + "]Y_BLOCK-(OFFENSIVE_MINOR)=" + YokelBlockEval.setPowerFlag(YokelBlock.Y_BLOCK, YokelBlock.OFFENSIVE_MINOR));
        System.out.println(YokelBlockEval.getPowerUseDescriptionLabel(YokelBlockEval.setPowerFlag(YokelBlock.Y_BLOCK, YokelBlock.OFFENSIVE_REGULAR)) + "]Y_BLOCK-(OFFENSIVE_REGULAR)=" + YokelBlockEval.setPowerFlag(YokelBlock.Y_BLOCK, YokelBlock.OFFENSIVE_REGULAR));
        System.out.println(YokelBlockEval.getPowerUseDescriptionLabel(YokelBlockEval.setPowerFlag(YokelBlock.Y_BLOCK, YokelBlock.OFFENSIVE_MEGA)) + "]Y_BLOCK-(OFFENSIVE_MEGA)=" + YokelBlockEval.setPowerFlag(YokelBlock.Y_BLOCK, YokelBlock.OFFENSIVE_MEGA));
        System.out.println(YokelBlockEval.getPowerUseDescriptionLabel(YokelBlockEval.setPowerFlag(YokelBlock.Y_BLOCK, YokelBlock.DEFENSIVE_MINOR)) + "]Y_BLOCK-(DEFENSIVE_MINOR)=" + YokelBlockEval.setPowerFlag(YokelBlock.Y_BLOCK, YokelBlock.DEFENSIVE_MINOR));
        System.out.println(YokelBlockEval.getPowerUseDescriptionLabel(YokelBlockEval.setPowerFlag(YokelBlock.Y_BLOCK, YokelBlock.DEFENSIVE_REGULAR)) + "]Y_BLOCK-(DEFENSIVE_REGULAR)=" + YokelBlockEval.setPowerFlag(YokelBlock.Y_BLOCK, YokelBlock.DEFENSIVE_REGULAR));
        System.out.println(YokelBlockEval.getPowerUseDescriptionLabel(YokelBlockEval.setPowerFlag(YokelBlock.Y_BLOCK, YokelBlock.DEFENSIVE_MEGA)) + "]Y_BLOCK-(DEFENSIVE_MEGA)=" + YokelBlockEval.setPowerFlag(YokelBlock.Y_BLOCK, YokelBlock.DEFENSIVE_MEGA));

        System.out.println("Offensive Y minor=" + YokelBlockEval.setPowerFlag(YokelBlock.Y_BLOCK, YokelBlock.OFFENSIVE_MINOR));

        System.out.println("O_BLOCK-(OFFENSIVE_MINOR)=" + YokelBlockEval.setPowerFlag(YokelBlock.O_BLOCK, YokelBlock.OFFENSIVE_MINOR));
        System.out.println("O_BLOCK-(OFFENSIVE_REGULAR)=" + YokelBlockEval.setPowerFlag(YokelBlock.O_BLOCK, YokelBlock.OFFENSIVE_REGULAR));
        System.out.println("O_BLOCK-(OFFENSIVE_MEGA)=" + YokelBlockEval.setPowerFlag(YokelBlock.O_BLOCK, YokelBlock.OFFENSIVE_MEGA));
        System.out.println("O_BLOCK-(DEFENSIVE_MINOR)=" + YokelBlockEval.setPowerFlag(YokelBlock.O_BLOCK, YokelBlock.DEFENSIVE_MINOR));
        System.out.println("O_BLOCK-(DEFENSIVE_REGULAR)=" + YokelBlockEval.setPowerFlag(YokelBlock.O_BLOCK, YokelBlock.DEFENSIVE_REGULAR));
        System.out.println("O_BLOCK-(DEFENSIVE_MEGA)=" + YokelBlockEval.setPowerFlag(YokelBlock.O_BLOCK, YokelBlock.DEFENSIVE_MEGA));

        System.out.println("K_BLOCK-(OFFENSIVE_MINOR)=" + YokelBlockEval.setPowerFlag(YokelBlock.K_BLOCK, YokelBlock.OFFENSIVE_MINOR));
        System.out.println("K_BLOCK-(OFFENSIVE_REGULAR)=" + YokelBlockEval.setPowerFlag(YokelBlock.K_BLOCK, YokelBlock.OFFENSIVE_REGULAR));
        System.out.println("K_BLOCK-(OFFENSIVE_MEGA)=" + YokelBlockEval.setPowerFlag(YokelBlock.K_BLOCK, YokelBlock.OFFENSIVE_MEGA));
        System.out.println("K_BLOCK-(DEFENSIVE_MINOR)=" + YokelBlockEval.setPowerFlag(YokelBlock.K_BLOCK, YokelBlock.DEFENSIVE_MINOR));
        System.out.println("K_BLOCK-(DEFENSIVE_REGULAR)=" + YokelBlockEval.setPowerFlag(YokelBlock.K_BLOCK, YokelBlock.DEFENSIVE_REGULAR));
        System.out.println("K_BLOCK-(DEFENSIVE_MEGA)=" + YokelBlockEval.setPowerFlag(YokelBlock.K_BLOCK, YokelBlock.DEFENSIVE_MEGA));

        System.out.println("E_BLOCK-(OFFENSIVE_MINOR)=" + YokelBlockEval.setPowerFlag(YokelBlock.E_BLOCK, YokelBlock.OFFENSIVE_MINOR));
        System.out.println("E_BLOCK-(OFFENSIVE_REGULAR)=" + YokelBlockEval.setPowerFlag(YokelBlock.E_BLOCK, YokelBlock.OFFENSIVE_REGULAR));
        System.out.println("E_BLOCK-(OFFENSIVE_MEGA)=" + YokelBlockEval.setPowerFlag(YokelBlock.E_BLOCK, YokelBlock.OFFENSIVE_MEGA));
        System.out.println("E_BLOCK-(DEFENSIVE_MINOR)=" + YokelBlockEval.setPowerFlag(YokelBlock.E_BLOCK, YokelBlock.DEFENSIVE_MINOR));
        System.out.println("E_BLOCK-(DEFENSIVE_REGULAR)=" + YokelBlockEval.setPowerFlag(YokelBlock.E_BLOCK, YokelBlock.DEFENSIVE_REGULAR));
        System.out.println("E_BLOCK-(DEFENSIVE_MEGA)=" + YokelBlockEval.setPowerFlag(YokelBlock.E_BLOCK, YokelBlock.DEFENSIVE_MEGA));

        System.out.println("L_BLOCK-(OFFENSIVE_MINOR)=" + YokelBlockEval.setPowerFlag(YokelBlock.L_BLOCK, YokelBlock.OFFENSIVE_MINOR));
        System.out.println("L_BLOCK-(OFFENSIVE_REGULAR)=" + YokelBlockEval.setPowerFlag(YokelBlock.L_BLOCK, YokelBlock.OFFENSIVE_REGULAR));
        System.out.println("L_BLOCK-(OFFENSIVE_MEGA)=" + YokelBlockEval.setPowerFlag(YokelBlock.L_BLOCK, YokelBlock.OFFENSIVE_MEGA));
        System.out.println("L_BLOCK-(DEFENSIVE_MINOR)=" + YokelBlockEval.setPowerFlag(YokelBlock.L_BLOCK, YokelBlock.DEFENSIVE_MINOR));
        System.out.println("L_BLOCK-(DEFENSIVE_REGULAR)=" + YokelBlockEval.setPowerFlag(YokelBlock.L_BLOCK, YokelBlock.DEFENSIVE_REGULAR));
        System.out.println("L_BLOCK-(DEFENSIVE_MEGA)=" + YokelBlockEval.setPowerFlag(YokelBlock.L_BLOCK, YokelBlock.DEFENSIVE_MEGA));

        System.out.println("EX_BLOCK-(OFFENSIVE_MINOR)=" + YokelBlockEval.setPowerFlag(YokelBlock.EX_BLOCK, YokelBlock.OFFENSIVE_MINOR));
        System.out.println("EX_BLOCK-(OFFENSIVE_REGULAR)=" + YokelBlockEval.setPowerFlag(YokelBlock.EX_BLOCK, YokelBlock.OFFENSIVE_REGULAR));
        System.out.println("EX_BLOCK-(OFFENSIVE_MEGA)=" + YokelBlockEval.setPowerFlag(YokelBlock.EX_BLOCK, YokelBlock.OFFENSIVE_MEGA));
        System.out.println("EX_BLOCK-(DEFENSIVE_MINOR)=" + YokelBlockEval.setPowerFlag(YokelBlock.EX_BLOCK, YokelBlock.DEFENSIVE_MINOR));
        System.out.println("EX_BLOCK-(DEFENSIVE_REGULAR)=" + YokelBlockEval.setPowerFlag(YokelBlock.EX_BLOCK, YokelBlock.DEFENSIVE_REGULAR));
        System.out.println("EX_BLOCK-(DEFENSIVE_MEGA)=" + YokelBlockEval.setPowerFlag(YokelBlock.EX_BLOCK, YokelBlock.DEFENSIVE_MEGA));
    }
    @Test
    public void yokelblock_Y_Test() throws Exception {
        System.out.println("Start yokelblock_Y_Test()");
        int y_block = YokelBlock.Y_BLOCK;
        int block;
        Assert.assertEquals(YokelBlock.Y_BLOCK, y_block);

        System.out.println("Y Block: " + YokelBlock.Y_BLOCK);
        //OFFENSIVE_MINOR = 3;
        block = YokelBlockEval.setPowerFlag(YokelBlock.Y_BLOCK, YokelBlock.OFFENSIVE_MINOR);
        System.out.println("OFFENSIVE_MINOR: " + block);
        printBlockLabel(block);

        //OFFENSIVE_REGULAR = 5;
        block = YokelBlockEval.setPowerFlag(YokelBlock.Y_BLOCK, YokelBlock.OFFENSIVE_REGULAR);
        System.out.println("OFFENSIVE_REGULAR: " + block);
        printBlockLabel(block);

        //OFFENSIVE_MEGA = 7;
        block = YokelBlockEval.setPowerFlag(YokelBlock.Y_BLOCK, YokelBlock.OFFENSIVE_MEGA);
        System.out.println("OFFENSIVE_MEGA: " + block);
        printBlockLabel(block);

        //DEFENSIVE_MINOR = 2;
        block = YokelBlockEval.setPowerFlag(YokelBlock.Y_BLOCK, YokelBlock.DEFENSIVE_MINOR);
        System.out.println("DEFENSIVE_MINOR: " + block);
        printBlockLabel(block);

        //DEFENSIVE_REGULAR = 4;
        block = YokelBlockEval.setPowerFlag(YokelBlock.Y_BLOCK, YokelBlock.DEFENSIVE_REGULAR);
        System.out.println("DEFENSIVE_REGULAR: " + block);
        printBlockLabel(block);

        //DEFENSIVE_MEGA = 6;
        block = YokelBlockEval.setPowerFlag(YokelBlock.Y_BLOCK, YokelBlock.DEFENSIVE_MEGA);
        System.out.println("DEFENSIVE_MEGA: " + block);
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
        int o_block = YokelBlock.O_BLOCK;
        int block;
        Assert.assertEquals(YokelBlock.O_BLOCK, o_block);

        System.out.println("O Block: " + YokelBlock.O_BLOCK);
        //OFFENSIVE_MINOR = 3;
        block = YokelBlockEval.setPowerFlag(YokelBlock.O_BLOCK, YokelBlock.OFFENSIVE_MINOR);
        System.out.println("OFFENSIVE_MINOR: " + block);
        printBlockLabel(block);

        //OFFENSIVE_REGULAR = 5;
        block = YokelBlockEval.setPowerFlag(YokelBlock.O_BLOCK, YokelBlock.OFFENSIVE_REGULAR);
        System.out.println("OFFENSIVE_REGULAR: " + block);
        printBlockLabel(block);

        //OFFENSIVE_MEGA = 7;
        block = YokelBlockEval.setPowerFlag(YokelBlock.O_BLOCK, YokelBlock.OFFENSIVE_MEGA);
        System.out.println("OFFENSIVE_MEGA: " + block);
        printBlockLabel(block);

        //DEFENSIVE_MINOR = 2;
        block = YokelBlockEval.setPowerFlag(YokelBlock.O_BLOCK, YokelBlock.DEFENSIVE_MINOR);
        System.out.println("DEFENSIVE_MINOR: " + block);
        printBlockLabel(block);

        //DEFENSIVE_REGULAR = 4;
        block = YokelBlockEval.setPowerFlag(YokelBlock.O_BLOCK, YokelBlock.DEFENSIVE_REGULAR);
        System.out.println("DEFENSIVE_REGULAR: " + block);
        printBlockLabel(block);

        //DEFENSIVE_MEGA = 6;
        block = YokelBlockEval.setPowerFlag(YokelBlock.O_BLOCK, YokelBlock.DEFENSIVE_MEGA);
        System.out.println("DEFENSIVE_MEGA: " + block);
        printBlockLabel(block);

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
        int k_block = YokelBlock.K_BLOCK;
        int block;
        Assert.assertEquals(YokelBlock.K_BLOCK, k_block);

        System.out.println("K Block: " + YokelBlock.K_BLOCK);
        //OFFENSIVE_MINOR = 3;
        block = YokelBlockEval.setPowerFlag(YokelBlock.K_BLOCK, YokelBlock.OFFENSIVE_MINOR);
        System.out.println("OFFENSIVE_MINOR: " + block);
        printBlockLabel(block);

        //OFFENSIVE_REGULAR = 5;
        block = YokelBlockEval.setPowerFlag(YokelBlock.K_BLOCK, YokelBlock.OFFENSIVE_REGULAR);
        System.out.println("OFFENSIVE_REGULAR: " + block);
        printBlockLabel(block);

        //OFFENSIVE_MEGA = 7;
        block = YokelBlockEval.setPowerFlag(YokelBlock.K_BLOCK, YokelBlock.OFFENSIVE_MEGA);
        System.out.println("OFFENSIVE_MEGA: " + block);
        printBlockLabel(block);

        //DEFENSIVE_MINOR = 2;
        block = YokelBlockEval.setPowerFlag(YokelBlock.K_BLOCK, YokelBlock.DEFENSIVE_MINOR);
        System.out.println("DEFENSIVE_MINOR: " + block);
        printBlockLabel(block);

        //DEFENSIVE_REGULAR = 4;
        block = YokelBlockEval.setPowerFlag(YokelBlock.K_BLOCK, YokelBlock.DEFENSIVE_REGULAR);
        System.out.println("DEFENSIVE_REGULAR: " + block);
        printBlockLabel(block);

        //DEFENSIVE_MEGA = 6;
        block = YokelBlockEval.setPowerFlag(YokelBlock.K_BLOCK, YokelBlock.DEFENSIVE_MEGA);
        System.out.println("DEFENSIVE_MEGA: " + block);
        printBlockLabel(block);

        Assert.assertFalse(YokelBlockEval.hasBrokenFlag(k_block));
        k_block = YokelBlockEval.addBrokenFlag(k_block);
        Assert.assertTrue(YokelBlockEval.hasBrokenFlag(k_block));
        k_block = YokelBlockEval.removeBrokenFlag(k_block);
        Assert.assertFalse(YokelBlockEval.hasBrokenFlag(k_block));
        System.out.println("End yokelblock_K_Test()");
    }

    @Test
    public void yokelblock_E_Test() throws Exception {
        System.out.println("Start yokelblock_E_Test()");
        int e_block = YokelBlock.E_BLOCK;
        int block;
        Assert.assertEquals(YokelBlock.E_BLOCK, e_block);

        System.out.println("E Block: " + YokelBlock.E_BLOCK);
        //OFFENSIVE_MINOR = 3;
        block = YokelBlockEval.setPowerFlag(YokelBlock.E_BLOCK, YokelBlock.OFFENSIVE_MINOR);
        System.out.println("OFFENSIVE_MINOR: " + block);
        printBlockLabel(block);

        //OFFENSIVE_REGULAR = 5;
        block = YokelBlockEval.setPowerFlag(YokelBlock.E_BLOCK, YokelBlock.OFFENSIVE_REGULAR);
        System.out.println("OFFENSIVE_REGULAR: " + block);
        printBlockLabel(block);

        //OFFENSIVE_MEGA = 7;
        block = YokelBlockEval.setPowerFlag(YokelBlock.E_BLOCK, YokelBlock.OFFENSIVE_MEGA);
        System.out.println("OFFENSIVE_MEGA: " + block);
        printBlockLabel(block);

        //DEFENSIVE_MINOR = 2;
        block = YokelBlockEval.setPowerFlag(YokelBlock.E_BLOCK, YokelBlock.DEFENSIVE_MINOR);
        System.out.println("DEFENSIVE_MINOR: " + block);
        printBlockLabel(block);

        //DEFENSIVE_REGULAR = 4;
        block = YokelBlockEval.setPowerFlag(YokelBlock.E_BLOCK, YokelBlock.DEFENSIVE_REGULAR);
        System.out.println("DEFENSIVE_REGULAR: " + block);
        printBlockLabel(block);

        //DEFENSIVE_MEGA = 6;
        block = YokelBlockEval.setPowerFlag(YokelBlock.E_BLOCK, YokelBlock.DEFENSIVE_MEGA);
        System.out.println("DEFENSIVE_MEGA: " + block);
        printBlockLabel(block);

        Assert.assertFalse(YokelBlockEval.hasBrokenFlag(e_block));
        e_block = YokelBlockEval.addBrokenFlag(e_block);
        Assert.assertTrue(YokelBlockEval.hasBrokenFlag(e_block));
        e_block = YokelBlockEval.removeBrokenFlag(e_block);
        Assert.assertFalse(YokelBlockEval.hasBrokenFlag(e_block));
        System.out.println("End yokelblock_E_Test()");
    }

    @Test
    public void yokelblock_L_Test() throws Exception {
        System.out.println("Start yokelblock_L_Test()");
        int l_block = YokelBlock.L_BLOCK;
        int block;
        Assert.assertEquals(YokelBlock.L_BLOCK, l_block);

        System.out.println("L Block: " + YokelBlock.L_BLOCK);
        //OFFENSIVE_MINOR = 3;
        block = YokelBlockEval.setPowerFlag(YokelBlock.L_BLOCK, YokelBlock.OFFENSIVE_MINOR);
        System.out.println("OFFENSIVE_MINOR: " + block);
        printBlockLabel(block);

        //OFFENSIVE_REGULAR = 5;
        block = YokelBlockEval.setPowerFlag(YokelBlock.L_BLOCK, YokelBlock.OFFENSIVE_REGULAR);
        System.out.println("OFFENSIVE_REGULAR: " + block);
        printBlockLabel(block);

        //OFFENSIVE_MEGA = 7;
        block = YokelBlockEval.setPowerFlag(YokelBlock.L_BLOCK, YokelBlock.OFFENSIVE_MEGA);
        System.out.println("OFFENSIVE_MEGA: " + block);
        printBlockLabel(block);

        //DEFENSIVE_MINOR = 2;
        block = YokelBlockEval.setPowerFlag(YokelBlock.L_BLOCK, YokelBlock.DEFENSIVE_MINOR);
        System.out.println("DEFENSIVE_MINOR: " + block);
        printBlockLabel(block);

        //DEFENSIVE_REGULAR = 4;
        block = YokelBlockEval.setPowerFlag(YokelBlock.L_BLOCK, YokelBlock.DEFENSIVE_REGULAR);
        System.out.println("DEFENSIVE_REGULAR: " + block);
        printBlockLabel(block);

        //DEFENSIVE_MEGA = 6;
        block = YokelBlockEval.setPowerFlag(YokelBlock.L_BLOCK, YokelBlock.DEFENSIVE_MEGA);
        System.out.println("DEFENSIVE_MEGA: " + block);
        printBlockLabel(block);

        Assert.assertFalse(YokelBlockEval.hasBrokenFlag(l_block));
        l_block = YokelBlockEval.addBrokenFlag(l_block);
        Assert.assertTrue(YokelBlockEval.hasBrokenFlag(l_block));
        l_block = YokelBlockEval.removeBrokenFlag(l_block);
        Assert.assertFalse(YokelBlockEval.hasBrokenFlag(l_block));
        System.out.println("End yokelblock_L_Test()");
    }

    @Test
    public void yokelblock_EX_Test() {
        System.out.println("Start yokelblock_EX_Test()");
        int ex_block = YokelBlock.EX_BLOCK;
        int block;
        Assert.assertEquals(YokelBlock.EX_BLOCK, ex_block);

        System.out.println("EX Block: " + YokelBlock.EX_BLOCK);
        //OFFENSIVE_MINOR = 3;
        block = YokelBlockEval.setPowerFlag(YokelBlock.EX_BLOCK, YokelBlock.OFFENSIVE_MINOR);
        System.out.println("OFFENSIVE_MINOR: " + block);
        printBlockLabel(block);

        //OFFENSIVE_REGULAR = 5;
        block = YokelBlockEval.setPowerFlag(YokelBlock.EX_BLOCK, YokelBlock.OFFENSIVE_REGULAR);
        System.out.println("OFFENSIVE_REGULAR: " + block);
        printBlockLabel(block);

        //OFFENSIVE_MEGA = 7;
        block = YokelBlockEval.setPowerFlag(YokelBlock.EX_BLOCK, YokelBlock.OFFENSIVE_MEGA);
        System.out.println("OFFENSIVE_MEGA: " + block);
        printBlockLabel(block);

        //DEFENSIVE_MINOR = 2;
        block = YokelBlockEval.setPowerFlag(YokelBlock.EX_BLOCK, YokelBlock.DEFENSIVE_MINOR);
        System.out.println("DEFENSIVE_MINOR: " + block);
        printBlockLabel(block);

        //DEFENSIVE_REGULAR = 4;
        block = YokelBlockEval.setPowerFlag(YokelBlock.EX_BLOCK, YokelBlock.DEFENSIVE_REGULAR);
        System.out.println("DEFENSIVE_REGULAR: " + block);
        printBlockLabel(block);

        //DEFENSIVE_MEGA = 6;
        block = YokelBlockEval.setPowerFlag(YokelBlock.EX_BLOCK, YokelBlock.DEFENSIVE_MEGA);
        System.out.println("DEFENSIVE_MEGA: " + block);
        printBlockLabel(block);

        Assert.assertFalse(YokelBlockEval.hasBrokenFlag(ex_block));
        ex_block = YokelBlockEval.addBrokenFlag(ex_block);
        Assert.assertTrue(YokelBlockEval.hasBrokenFlag(ex_block));
        ex_block = YokelBlockEval.removeBrokenFlag(ex_block);
        Assert.assertFalse(YokelBlockEval.hasBrokenFlag(ex_block));
        System.out.println("End yokelblock_EX_Test()");
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
    public void testPartnerBreakFlags() {
        System.out.println("Start testPartnerBreakFlag()");
        int block;
        int actual;

        //Normal Blocks
        block = YokelBlock.Y_BLOCK;
        actual = YokelBlockEval.removePartnerBreakFlag(block);
        Assert.assertEquals(false, YokelBlockEval.hasPartnerBreakFlag(actual));
        actual = YokelBlockEval.addPartnerBreakFlag(block);
        Assert.assertEquals(true, YokelBlockEval.hasPartnerBreakFlag(actual));
        actual = YokelBlockEval.removePartnerBreakFlag(block);
        Assert.assertEquals(false, YokelBlockEval.hasPartnerBreakFlag(actual));

        //block = YokelBlockEval.addPowerBlockFlag(block, YokelBlockEval.OFFENSIVE_MINOR);

        block = YokelBlock.O_BLOCK;
        Assert.assertEquals(false, YokelBlockEval.hasPartnerBreakFlag(actual));
        actual = YokelBlockEval.addPartnerBreakFlag(block);
        Assert.assertEquals(true, YokelBlockEval.hasPartnerBreakFlag(actual));
        actual = YokelBlockEval.removePartnerBreakFlag(block);
        Assert.assertEquals(false, YokelBlockEval.hasPartnerBreakFlag(actual));

        block = YokelBlock.K_BLOCK;
        Assert.assertEquals(false, YokelBlockEval.hasPartnerBreakFlag(actual));
        actual = YokelBlockEval.addPartnerBreakFlag(block);
        Assert.assertEquals(true, YokelBlockEval.hasPartnerBreakFlag(actual));
        actual = YokelBlockEval.removePartnerBreakFlag(block);
        Assert.assertEquals(false, YokelBlockEval.hasPartnerBreakFlag(actual));

        block = YokelBlock.E_BLOCK;
        Assert.assertEquals(false, YokelBlockEval.hasPartnerBreakFlag(actual));
        actual = YokelBlockEval.addPartnerBreakFlag(block);
        Assert.assertEquals(true, YokelBlockEval.hasPartnerBreakFlag(actual));
        actual = YokelBlockEval.removePartnerBreakFlag(block);
        Assert.assertEquals(false, YokelBlockEval.hasPartnerBreakFlag(actual));

        block = YokelBlock.L_BLOCK;
        Assert.assertEquals(false, YokelBlockEval.hasPartnerBreakFlag(actual));
        actual = YokelBlockEval.addPartnerBreakFlag(block);
        Assert.assertEquals(true, YokelBlockEval.hasPartnerBreakFlag(actual));
        actual = YokelBlockEval.removePartnerBreakFlag(block);
        Assert.assertEquals(false, YokelBlockEval.hasPartnerBreakFlag(actual));

        block = YokelBlock.EX_BLOCK;
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
        System.out.println("Start testAddPowerBlockFlag()");

        int block;
        int actual;

        //Normal Blocks
        block = YokelBlock.Y_BLOCK;
        actual = YokelBlockEval.addPowerBlockFlag(block);

        System.out.println("BLOCK: " + block);
        System.out.println("POWER_BLOCK: " + actual);
        System.out.println("HAS Block Flag: " + YokelBlockEval.hasPowerBlockFlag(actual));
        System.out.println("BLOCK: " + YokelBlock.O_BLOCK);
        System.out.println("POWER_BLOCK: " + YokelBlockEval.addPowerBlockFlag(YokelBlock.O_BLOCK));

        System.out.println("End testAddPowerBlockFlag()");
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
}