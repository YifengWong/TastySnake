package com.example.stevennl.tastysnake.model;

import android.util.Log;

import java.io.Serializable;

/**
 * A custom data packet for bluetooth data transfer.
 * Author: LCY
 */
public class Packet implements Serializable {
    private static final String TAG = "Packet";
    public static final int SIZE = 5;  // Fixed packet size in bytes
    private static final char TYPE_FOOD_LENGTHEN = 'a';
    private static final char TYPE_FOOD_SHORTEN = 'b';
    private static final char TYPE_DIRECTION = 'c';
    private static final char TYPE_RESTART = 'd';
    private static final char TYPE_TIME = 'e';
    private static final char TYPE_WIN = 'f';

    private Type type;
    private int x;
    private int y;
    private Direction direc = Direction.NONE;

    /**
     * Type of a packet.
     */
    public enum Type {
        FOOD_LENGTHEN,
        FOOD_SHORTEN,
        DIRECTION,
        RESTART,
        TIME,
        WIN
    }

    /**
     * Delete default constructor.
     */
    private Packet() {}

    /**
     * Initialize the packet from a byte array.
     *
     * @param raw A packet represented in bytes.
     */
    public Packet(byte[] raw) {
        String str = new String(raw);
        switch (str.charAt(0)) {
            case TYPE_FOOD_LENGTHEN:
                type = Type.FOOD_LENGTHEN;
                x = Integer.parseInt(str.substring(1, 3));
                y = Integer.parseInt(str.substring(3, 5));
                break;
            case TYPE_FOOD_SHORTEN:
                type = Type.FOOD_SHORTEN;
                x = Integer.parseInt(str.substring(1, 3));
                y = Integer.parseInt(str.substring(3, 5));
                break;
            case TYPE_DIRECTION:
                type = Type.DIRECTION;
                direc = Direction.values()[str.charAt(1) - '0'];
                break;
            case TYPE_RESTART:
                type = Type.RESTART;
                x = str.charAt(1) - '0';
                break;
            case TYPE_TIME:
                type = Type.TIME;
                x = Integer.parseInt(str.substring(1, 3));
                break;
            case TYPE_WIN:
                type = Type.WIN;
                x = str.charAt(1) - '0';
            default:
                break;
        }
    }

    /**
     * Return the packet represented in bytes.
     */
    public byte[] toBytes() {
        StringBuilder builder = new StringBuilder();
        switch (type) {
            case FOOD_LENGTHEN:
                builder.append(TYPE_FOOD_LENGTHEN);
                builder.append(x / 10 == 0 ? "0" + x : x);
                builder.append(y / 10 == 0 ? "0" + y : y);
                break;
            case FOOD_SHORTEN:
                builder.append(TYPE_FOOD_SHORTEN);
                builder.append(x / 10 == 0 ? "0" + x : x);
                builder.append(y / 10 == 0 ? "0" + y : y);
                break;
            case DIRECTION:
                builder.append(TYPE_DIRECTION);
                builder.append(direc.ordinal());
                break;
            case RESTART:
                builder.append(TYPE_RESTART);
                builder.append(x);
                break;
            case TIME:
                builder.append(TYPE_TIME);
                builder.append((x == -1 || x / 10 != 0) ? x : "0" + x);
                break;
            case WIN:
                builder.append(TYPE_WIN);
                builder.append(x);
            default:
                break;
        }
        // Pad
        int length = builder.length();
        for (int i = 0; i < SIZE - length; ++i) {
            builder.append(' ');
        }
        return builder.toString().getBytes();
    }

    /**
     * Create a FOOD packet.
     *
     * @param x The row number of the food
     * @param y The column number of the food
     * @param lengthen True if the food could lengthen the snake, false otherwise
     */
    public static Packet food(int x, int y, boolean lengthen) {
        Packet pkt = new Packet();
        pkt.type = lengthen ? Type.FOOD_LENGTHEN : Type.FOOD_SHORTEN;
        pkt.x = x;
        pkt.y = y;
        return pkt;
    }

    /**
     * Create a DIRECTION packet.
     *
     * @param direc The direction to be stored in the packet
     */
    public static Packet direction(Direction direc) {
        Packet pkt = new Packet();
        pkt.type = Type.DIRECTION;
        pkt.direc = direc;
        return pkt;
    }

    /**
     * Create a RESTART packet.
     *
     * @param attacker Current attacker
     */
    public static Packet restart(Snake.Type attacker) {
        Packet pkt = new Packet();
        pkt.type = Type.RESTART;
        pkt.x = attacker.ordinal();
        return pkt;
    }

    /**
     * Create a TIME packet.
     *
     * @param t The time stored in the packet
     */
    public static Packet time(int t) {
        Packet pkt = new Packet();
        pkt.type = Type.TIME;
        pkt.x = t;
        return pkt;
    }

    /**
     * Create a WIN packet.
     *
     * @param winner The type of the winner snake
     */
    public static Packet win(Snake.Type winner) {
        Packet pkt = new Packet();
        pkt.type = Type.WIN;
        pkt.x = winner.ordinal();
        return pkt;
    }

    /**
     * Return the type of the packet.
     */
    public Type getType() {
        return type;
    }

    /**
     * Return the x-coordinate of food position.
     */
    public int getFoodX() {
        return x;
    }

    /**
     * Return the y-coordinate of food position.
     */
    public int getFoodY() {
        return y;
    }

    /**
     * Return the direction of the packet.
     */
    public Direction getDirec() {
        return direc;
    }

    /**
     * Return the time of the packet.
     */
    public int getTime() {
        return x;
    }

    /**
     * Return the attacker.
     */
    public Snake.Type getAttacker() {
        return Snake.Type.values()[x];
    }

    /**
     * Return the winner.
     */
    public Snake.Type getWinner() {
        return Snake.Type.values()[x];
    }

    /**
     * Return the string description of the packet.
     */
    @Override
    public String toString() {
        String str = "Type: " + getType().name();
        switch (type) {
            case FOOD_LENGTHEN:
            case FOOD_SHORTEN:
                str = str + " FoodX: " + getFoodX() + " FoodY: " + getFoodY();
                break;
            case DIRECTION:
                str = str + " Direc: " + direc.name();
                break;
            case RESTART:
                str = str + " Attacker: " + getAttacker().name();
                break;
            case TIME:
                str = str + " Time: " + getTime();
                break;
            case WIN:
                str = str + " Winner: " + getWinner().name();
            default:
                break;
        }
        return str;
    }
}
