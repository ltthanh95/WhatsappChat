package com.tl.whatapps.Service;

import com.tl.whatapps.DTO.Request.GroupChatRequest;
import com.tl.whatapps.Entity.Chat;
import com.tl.whatapps.Entity.User;
import com.tl.whatapps.Exception.ChatException;
import com.tl.whatapps.Exception.UserException;
import org.springframework.stereotype.Service;

import java.util.List;


public interface ChatService {
    public Chat createChat(User reqUser, Integer userId) throws UserException;

    public Chat findChatById(Integer chatId) throws ChatException;

    public List<Chat> findAllChatByUserId(Integer userId) throws UserException;

    public Chat createGroup(GroupChatRequest req, User reqUser) throws UserException;

    public Chat addUserToGroup(Integer userId, Integer chatId, User reqUser) throws UserException, ChatException;

    public Chat renameGroup(Integer chatId, String groupName, User reqUser) throws ChatException, UserException;

    public Chat removeFromGroup(Integer chatId, Integer userId, User reqUser) throws UserException, ChatException;

    public void deleteChat(Integer chatId, Integer userId) throws ChatException, UserException;

}
