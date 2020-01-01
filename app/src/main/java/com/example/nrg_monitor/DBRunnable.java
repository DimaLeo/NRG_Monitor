package com.example.nrg_monitor;

import android.content.Context;

import okhttp3.Request;

public interface DBRunnable {
    public Request executeDBTask();
    public void postExecuteDBTask(Request request);
}

