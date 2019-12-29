package net.asg.games.game.objects;

public class YokelBlockEval {
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
                        case YokelBlock.NORMAL_POWER_LEVEL: // regular
                            return 174;
                        case YokelBlock.MEGA_POWER_LEVEL: // mega
                            return 199;
                        default: // minor
                            return 169;
                    }
                }
                switch (severity) {
                    case YokelBlock.NORMAL_POWER_LEVEL:
                        return 111;
                    case YokelBlock.MEGA_POWER_LEVEL:
                        return 216;
                    default:
                        return 79;
                }
            case 2: // H powers
                if (isOffensive) {
                    switch (severity) {
                        case YokelBlock.NORMAL_POWER_LEVEL:
                            return 163;
                        case YokelBlock.MEGA_POWER_LEVEL:
                            return 254;
                        default:
                            return 222;
                    }
                }
                switch (severity) {
                    case YokelBlock.NORMAL_POWER_LEVEL:
                        return 107;
                    case YokelBlock.MEGA_POWER_LEVEL:
                        return 75;
                    default:
                        return 181;
                }
            case 3:
                if (isOffensive) {
                    switch (severity) {
                        case YokelBlock.NORMAL_POWER_LEVEL:
                            return 69;
                        case YokelBlock.MEGA_POWER_LEVEL:
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
                        case YokelBlock.NORMAL_POWER_LEVEL: // regular
                            return "Power O";
                        case YokelBlock.MEGA_POWER_LEVEL: // mega
                            return "Mega Power O";
                        default: // minor
                            return "Minor Power O";
                    }
                }
                switch (severity) {
                    case YokelBlock.NORMAL_POWER_LEVEL:
                        return "Defensive O";
                    case YokelBlock.MEGA_POWER_LEVEL:
                        return "Mega Defensive O";
                    default:
                        return "Minor Defensive O";
                }
            case 2: // H powers
                if (isOffensive) {
                    switch (severity) {
                        case YokelBlock.NORMAL_POWER_LEVEL:
                            return "Power K";
                        case YokelBlock.MEGA_POWER_LEVEL:
                            return "Mega Power K";
                        default:
                            return "Minor Power K";
                    }
                }
                switch (severity) {
                    case YokelBlock.NORMAL_POWER_LEVEL:
                        return "Defensive K";
                    case YokelBlock.MEGA_POWER_LEVEL:
                        return "Mega Defensive K";
                    default:
                        return "Minor Defensive K";
                }
            case 3:
                if (isOffensive) {
                    switch (severity) {
                        case YokelBlock.NORMAL_POWER_LEVEL:
                            return "Power E";
                        case YokelBlock.MEGA_POWER_LEVEL:
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
