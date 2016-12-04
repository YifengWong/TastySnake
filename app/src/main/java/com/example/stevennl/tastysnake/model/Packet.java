package com.example.stevennl.tastysnake.model;

import java.io.Serializable;

/**
 * A custom data packet for bluetooth data transfer.
 * Author: LCY
 */
public class Packet implements Serializable {
    private static final String TAG = "Packet";
    private static final char TYPE_FOOD_LENGTHEN = 'a';
    private static final char TYPE_FOOD_SHORTEN = 'b';
    private static final char TYPE_DIRECTION = 'c';
    private static final char TYPE_SWAP = 'd';

    private Type type;
    private Pos food;
    private Direction direc = Direction.NONE;

    /**
     * Type of a packet.
     */
    public enum Type {
        FOOD_LENGTHEN,
        FOOD_SHORTEN,
        DIRECTION,
        SWAP
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
            case TYPE_FOOD_LENGTHEN: {
                type = Type.FOOD_LENGTHEN;
                int x = Integer.parseInt(str.substring(1, 3));
                int y = Integer.parseInt(str.substring(3, 5));
                food = new Pos(x, y);
                break;
            }
            case TYPE_FOOD_SHORTEN: {
                type = Type.FOOD_SHORTEN;
                int x = Integer.parseInt(str.substring(1, 3));
                int y = Integer.parseInt(str.substring(3, 5));
                food = new Pos(x, y);
                break;
            }
            case TYPE_DIRECTION:
                type = Type.DIRECTION;
                direc = Direction.values()[str.charAt(1) - '0'];
                break;
            case TYPE_SWAP:
                type = Type.SWAP;
                break;
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
            case FOOD_LENGTHEN: {
                builder.append(TYPE_FOOD_LENGTHEN);
                int x = food.getX(), y = food.getY();
                builder.append(x / 10 == 0 ? "0" + x : x);
                builder.append(y / 10 == 0 ? "0" + y : y);
                break;
            }
            case FOOD_SHORTEN: {
                builder.append(TYPE_FOOD_SHORTEN);
                int x = food.getX(), y = food.getY();
                builder.append(x / 10 == 0 ? "0" + x : x);
                builder.append(y / 10 == 0 ? "0" + y : y);
                break;
            }
            case DIRECTION:
                builder.append(TYPE_DIRECTION);
                builder.append(direc.ordinal());
                break;
            case SWAP:
                builder.append(TYPE_SWAP);
                break;
            default:
                break;
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
        pkt.food = new Pos(x, y);
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
     * Create a SWAP packet.
     */
    public static Packet swap() {
        Packet pkt = new Packet();
        pkt.type = Type.SWAP;
        return pkt;
    }

    /**
     * Return the type of the packet.
     */
    public Type getType() {
        return type;
    }

    /**
     * Return the food position of the packet.
     */
    public Pos getFood() {
        return food;
    }

    /**
     * Return the direction of the packet.
     */
    public Direction getDirec() {
        return direc;
    }

    /**
     * Return the string description of the packet.
     */
    @Override
    public String toString() {
        return "Type: " + type.ordinal() + " Food: "
                + (food == null ? "null" : food.toString()) + " Direc: " + direc.name();
    }
}
