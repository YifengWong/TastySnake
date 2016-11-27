package com.example.stevennl.tastysnake.util.bluetooth.listener;

/**
 * Listener for data transfer.
 * Author: LCY
 */
public interface ConnectListener {
    int ERR_SOCKET_CREATE = 1;
    int ERR_SOCKET_CLOSE = 2;
    int ERR_SERVER_SOCKET_ACCEPT = 3;
    int ERR_CLIENT_SOCKET_CONNECT = 4;
    int ERR_STREAM_CREATE = 5;
    int ERR_STREAM_READ = 6;
    int ERR_STREAM_WRITE = 7;

    /**
     * Called when the client socket is established.
     */
    void onClientSocketEstablished();

    /**
     * Called when the server socket is established.
     */
    void onServerSocketEstablished();

    /**
     * Called when the data transfer channel is established.
     */
    void onDataChannelEstablished();

    /**
     * Called when receiving data from remote device
     *
     * @param bytesCount the number of bytes received
     * @param data the data received
     */
    void onReceive(int bytesCount, byte[] data);

    /**
     * Called when error occurs.
     *
     * @param code the error code. Refer to the static final fields in this interface.
     * @param e the exception
     */
    void onError(int code, Exception e);
}
