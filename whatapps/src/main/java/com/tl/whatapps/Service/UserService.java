package com.tl.whatapps.Service;

import com.tl.whatapps.DTO.Request.UpdateUserRequest;
import com.tl.whatapps.Entity.User;
import com.tl.whatapps.Exception.UserException;

import java.util.List;

public interface UserService {

    public User findUserById(Integer id) throws UserException;

    public User findUserProfile(String jwt) throws UserException;

    public User updateUser(Integer userId, UpdateUserRequest req) throws UserException;

    public List<User> searchUser(String query);
}
