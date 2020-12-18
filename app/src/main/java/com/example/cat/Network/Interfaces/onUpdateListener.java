package com.example.cat.Network.Interfaces;


import com.example.cat.dataModels.UserInfo;

public interface onUpdateListener {

    void onSuccess(UserInfo userInfo);

    void onFailure(Exception e);

}
