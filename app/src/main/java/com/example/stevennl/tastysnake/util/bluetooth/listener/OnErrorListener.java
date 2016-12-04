package com.example.stevennl.tastysnake.util.bluetooth.listener;

/**
 * Listener for errors during connection.
 * Author: LCY
 */
public interface OnErrorListener {
    int ERR_SOCKET_CREATE = 1;
    int ERR_SOCKET_CLOSE = 2;
    int ERR_SERVER_SOCKET_ACCEPT = 3;
    int ERR_CLIENT_SOCKET_CONNECT = 4;
    int ERR_STREAM_CREATE = 5;
    int ERR_STREAM_READ = 6;
    int ERR_STREAM_WRITE = 7;

    /**
     * Called when error occurs.
     *
     * @param code The error code. Refer to the constant fields in this interface.
     * @param e The exception
     */
    void onError(int code, Exception e);
}
