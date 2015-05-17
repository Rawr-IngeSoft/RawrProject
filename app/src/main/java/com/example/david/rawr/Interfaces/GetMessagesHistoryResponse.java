package com.example.david.rawr.Interfaces;

import com.example.david.rawr.Models.Message;

import java.util.ArrayList;

/**
 * Created by david on 16/05/2015.
 */
public interface GetMessagesHistoryResponse {
    void getMessageHistoryFinish(ArrayList<Message> output);
}
