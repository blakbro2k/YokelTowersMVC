package net.asg.games.game.objects;

public class YokelBlockEval {
    public static final int CLEAR = 6;
    public static final int NORMAL_Y = 0;
    public static final int NORMAL_O = 1;
    public static final int NORMAL_K = 2;
    public static final int NORMAL_E = 3;
    public static final int NORMAL_L = 4;
    public static final int NORMAL_EX = 5;
    public static final int STONE = 7;
    public static final int MIDAS = 8;
    public static final int MEDUSA = 9;
    public static final int ATTACK_Y = 10;
    public static final int ATTACK_O = 11;
    public static final int ATTACK_K = 12;
    public static final int ATTACK_E = 13;
    public static final int ATTACK_L = 14;
    public static final int ATTACK_EX = 15;
    public static final int DEFENSE_Y = 16;
    public static final int DEFENSE_O = 17;
    public static final int DEFENSE_K = 18;
    public static final int DEFENSE_E = 19;
    public static final int DEFENSE_L = 20;
    public static final int DEFENSE_EX = 21;
    public static final int BROKEN_Y = 27;
    public static final int BROKEN_O = 22;
    public static final int BROKEN_K = 23;
    public static final int BROKEN_E = 24;
    public static final int BROKEN_L = 25;
    public static final int BROKEN_EX = 26;

    public static int getCellFlag(int value) {
        return value & 0xf;
    }

    public static int setValueFlag(int original, int newValue) {
        return original & ~0xf | newValue;
    }

    public static boolean hasAddedByYahooFlag(int i) {
        if ((i & 0x80) == 0)
            return false;
        return true;
    }

    public static int addArtificialFlag(int i) {
        return i | 0x80;
    }

    public static boolean hasBrokenFlag(int i) {
        if ((i & 0x100) == 0)
            return false;
        return true;
    }

    public static int addBrokenFlag(int i) {
        return i | 0x100;
    }

    public static int removeBrokenFlag(int i) {
        return i & ~0x100;
    }

    public static boolean hasPartnerBreakFlag(int i) {
        if ((i & 0x200) == 0)
            return false;
        return true;
    }

    public static int addPartnerBreakFlag(int i) {
        return i | 0x200;
    }

    public static int removePartnerBreakFlag(int i) {
        return i & ~0x200;
    }

    public static int getID(int i) {
        return (i & 0x7f000) >> 12;
    }

    public static int setIDFlag(int i, int id) {
        if (id > 127)
            System.out.println("Assertion failure: invalid id " + id);
        return i & ~0x7f000 | id << 12;
    }

    /* Retrieves power "level"
     * - Even represents defensive powers ( 2, 4, 6 )
     * - Odd represents attack powers ( 3, 5, 7 )
     * ( MINOR, REGULAR, MEGA )
     */
    public static int getPowerFlag(int i) {
        return (i & 0x70) >> 4;
    }

    public static int setPowerFlag(int value, int power) {
        if (power > 7) {
            System.out.println("Assertion failure: invalid special " + power);
        }

        return value & ~0x70 | power << 4;
    }

    public static boolean hasSpecialFlag(int i) {
        if ((i & 0x400) == 0)
            return false;
        return true;
    }

    public static int addSpecialFlag(int i) {
        return i | 0x400;
    }

    public static boolean hasPowerBlockFlag(int i) {
        if ((i & 0x800) == 0)
            return false;
        return true;
    }

    public static int addPowerBlockFlag(int i) {
        return i | 0x800;
    }

    public static boolean isOffensive(int i) {
        if ((getPowerFlag(i) & 0x1) == 0)
            return false;
        return true;
    }

    public static int getPowerLevel(int i) {
        return getPowerFlag(i) >> 1;
    }

    public static char getPowerLabel(int i) {
        //TowersUtils.print( "this deally called" );
        if (hasSpecialFlag(i)) {
            //TowersUtils.print( "this one is okay " + dwi.getStringFromDict(1716519979) );
            //return dwi.getStringFromDict(1716519979);
            return 64;
        }
        boolean isOffensive = isOffensive(i);
        int severity = getPowerLevel(i);
        switch (getCellFlag(i)) {
            case 0: // Y powers
                if (isOffensive)
                    return 159;
                return 165;
            case 1: // A powers
                if (isOffensive) {
                    switch (severity) {
                        case 1: // regular
                            return 174;
                        case 2: // mega
                            return 199;
                        default: // minor
                            return 169;
                    }
                }
                switch (severity) {
                    case 1:
                        return 111;
                    case 2:
                        return 216;
                    default:
                        return 79;
                }
            case 2: // H powers
                if (isOffensive) {
                    switch (severity) {
                        case 1:
                            return 163;
                        case 2:
                            return 254;
                        default:
                            return 222;
                    }
                }
                switch (severity) {
                    case 1:
                        return 107;
                    case 2:
                        return 75;
                    default:
                        return 181;
                }
            case 3:
                if (isOffensive) {
                    switch (severity) {
                        case 1:
                            return 69;
                        case 2:
                            return 203;
                        default:
                            return 201;
                    }
                }
                return 234;
            case 4:
                if (isOffensive)
                    return 76;
                return 207;
            case 5:
                if (isOffensive)
                    return 33;
                return 182;
            default:
                return 63;
        }
    }


    public static String getPowerUseDescriptionLabel(int i) {
        //TowersUtils.print( "this deally called" );
        if (hasSpecialFlag(i)) {
            //TowersUtils.print( "this one is okay " + dwi.getStringFromDict(1716519979) );
            //return dwi.getStringFromDict(1716519979);
            return "Special Cell";
        }
        boolean isOffensive = isOffensive(i);
        int severity = getPowerLevel(i);
        switch (getCellFlag(i)) {
            case 0: // Y powers
                if (isOffensive)
                    return "Power Y";
                return "Defense Y";
            case 1: // A powers
                if (isOffensive) {
                    switch (severity) {
                        case 1: // regular
                            return "Power O";
                        case 2: // mega
                            return "Mega Power O";
                        default: // minor
                            return "Minor Power O";
                    }
                }
                switch (severity) {
                    case 1:
                        return "Defensive O";
                    case 2:
                        return "Mega Defensive O";
                    default:
                        return "Minor Defensive O";
                }
            case 2: // H powers
                if (isOffensive) {
                    switch (severity) {
                        case 1:
                            return "Power K";
                        case 2:
                            return "Mega Power K";
                        default:
                            return "Minor Power K";
                    }
                }
                switch (severity) {
                    case 1:
                        return "Defensive K";
                    case 2:
                        return "Mega Defensive K";
                    default:
                        return "Minor Defensive K";
                }
            case 3:
                if (isOffensive) {
                    switch (severity) {
                        case 1:
                            return "Power E";
                        case 2:
                            return "Mega Power E";
                        default:
                            return "Minor Power E";
                    }
                }
                return "Defensive E";
            case 4:
                if (isOffensive)
                    return "Power L";
                return "Defensive L";
            case 5:
                if (isOffensive)
                    return "Power !";
                return "Defensive !";
            default:
                return "?";
        }
    }

}
