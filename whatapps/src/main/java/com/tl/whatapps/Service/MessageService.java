package com.tl.whatapps.Service;

import com.tl.whatapps.DTO.Request.SendMessageRequest;
import com.tl.whatapps.Entity.Message;
import com.tl.whatapps.Entity.User;
import com.tl.whatapps.Exception.ChatException;
import com.tl.whatapps.Exception.MessageException;
import com.tl.whatapps.Exception.UserException;

import java.util.List;

public interface MessageService {
    public Message sendMessage(SendMessageRequest req) throws UserException, ChatException;

    public List<Message> getChatsMessages(Integer chatId, User reqUser) throws ChatException, UserException;

    public Message findMessageById(Integer messaageId) throws MessageException;

    public void deleteMessage(Integer messageId, User reqUser) throws MessageException;
}
