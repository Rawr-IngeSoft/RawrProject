// IRemoteService.aidl
package com.example.david.rawr;

// Declare any non-default types here with import statements

interface IRemoteService {

    List<String> getFriendsList();
    void sendMessage(String sender, String reciever, String msg);
    void changePet(String petUsername);
}
