package bsu.famcs.chat.dao;

import java.util.List;
import bsu.famcs.chat.model.Message;

public interface MessageDao {
    void add(Message message);

    void update(Message message);

    void delete(int id);

    Message selectById(Message task);

    List<Message> selectAll();
}