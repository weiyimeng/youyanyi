package com.uyu.device.devicetraining.domain.motor;

import android.net.LocalSocket;
import android.net.LocalSocketAddress;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import timber.log.Timber;

/**
 * Created by windern on 2016/1/22.
 */
public class LocalSocketClient {
    private static final String name = "uYuCom485";
    private LocalSocket localSocket;
    private int timeout = 30000;

    public void connect() throws IOException {
        localSocket = new LocalSocket();
        localSocket.connect(new LocalSocketAddress(name, LocalSocketAddress.Namespace.ABSTRACT));
        localSocket.setSoTimeout(timeout);
    }

    public void send(String msg) throws IOException {
        PrintWriter os = new PrintWriter(localSocket.getOutputStream());
        os.println(msg);
        os.flush();
    }

    public String recv() throws IOException {
//        Timber.d("recv");
        String result = null;
        BufferedReader is = new BufferedReader(new InputStreamReader(localSocket.getInputStream()));
        result = is.readLine();
        return result;
    }

    public void close() {
        try {
            localSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
