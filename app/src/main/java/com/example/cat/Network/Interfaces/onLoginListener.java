package com.example.cat.Network.Interfaces;


import com.example.cat.dataModels.UserInfo;

public interface onLoginListener {

    void onSuccess(UserInfo userInfo);

    void onFailure(Exception e);

}
