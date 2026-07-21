package com.team3.persistence;

import com.example._6su_team3.User;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class RegistrationPersistence {

    private final String fileName;

    public RegistrationPersistence() {
        this("users.json");
    }

    public RegistrationPersistence(String fileName) {
        this.fileName = fileName;
    }

    public void saveUsers(List<User> users) {
        Gson gson = new Gson();

        try (FileWriter writer = new FileWriter(fileName)) {
            gson.toJson(users, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<User> loadUsers() {
        File file = new File(fileName);

        if (!file.exists()) {
            return new ArrayList<>();
        }

        Gson gson = new Gson();

        try (FileReader reader = new FileReader(file)) {
            Type userListType =
                    new TypeToken<List<User>>() { }.getType();

            List<User> users = gson.fromJson(reader, userListType);

            if (users == null) {
                return new ArrayList<>();
            }

            return users;
        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
}
