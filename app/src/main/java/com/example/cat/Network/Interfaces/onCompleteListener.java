package com.example.cat.Network.Interfaces;


public interface onCompleteListener<T> {
    void onSuccess(T data);
    void onFailure(Exception e);
}
