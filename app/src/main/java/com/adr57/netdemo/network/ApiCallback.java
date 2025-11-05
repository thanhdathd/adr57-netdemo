package com.adr57.netdemo.network;

// Interface cho callback
public interface ApiCallback<T> {
    void onSuccess(T result);
    void onError(String error);
}
