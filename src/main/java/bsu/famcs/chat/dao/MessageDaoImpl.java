package bsu.famcs.chat.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import bsu.famcs.chat.db.ConnectionManager;
import bsu.famcs.chat.model.Message;

public class MessageDaoImpl implements MessageDao {
    private static Logger logger = Logger.getLogger(MessageDaoImpl.class.getName());

    private int uniqueId(){
        int random = (int)Math.floor(Math.random() * 999999 * Math.random());
        return random;
    }

    @Override
    public void add(Message message) {
        Connection connection = null;
        PreparedStatement insIntoMes = null;
        PreparedStatement insertIntoUsers = null;
        PreparedStatement getUserId = null;
        Integer id;
        try {
            connection = ConnectionManager.getConnection();

            getUserId = connection.prepareStatement("SELECT * FROM users where name = ?");
            getUserId.setString(1, message.getName());
            ResultSet res =  getUserId.executeQuery();
            if (res.next()){
                id = res.getInt("id");
                System.out.println("id: " + id);
            }
            else {
                insertIntoUsers = connection.prepareStatement("INSERT INTO users (id, name) VALUES (?, ?)");
                id = uniqueId();
                insertIntoUsers.setInt(1, id);
                insertIntoUsers.setString(2, message.getName());
                insertIntoUsers.executeUpdate();
                System.out.println("id: " + id);
            }

            insIntoMes = connection.prepareStatement("INSERT INTO messages (id, text, date, method, userId) VALUES (?, ?, ?, ?, ?)");
            insIntoMes.setInt(1, Integer.parseInt(message.getId()));
            insIntoMes.setString(2, message.getText());
            insIntoMes.setString(3, message.getDate());
            insIntoMes.setString(4, message.getMethod());
            insIntoMes.setInt(5, id);
            insIntoMes.executeUpdate();

        } catch (SQLException e) {
            logger.error(e);
        } finally {
            if (insIntoMes != null) {
                try {
                    insIntoMes.close();
                } catch (SQLException e) {
                    logger.error(e);
                }
            }

            if (insertIntoUsers != null) {
                try {
                    insertIntoUsers.close();
                } catch (SQLException e) {
                    logger.error(e);
                }
            }

            if (getUserId != null) {
                try {
                    getUserId.close();
                } catch (SQLException e) {
                    logger.error(e);
                }
            }

            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    logger.error(e);
                }
            }
        }
    }

    @Override
    public void update(Message message) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = ConnectionManager.getConnection();
            preparedStatement = connection.prepareStatement("Update messages SET method = ?, text = ?, date = ? WHERE id = ?");
            preparedStatement.setString(1, message.getMethod());
            preparedStatement.setString(2, message.getText());
            preparedStatement.setString(3, message.getDate());
            preparedStatement.setInt(4, Integer.parseInt(message.getId()));
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            logger.error(e);
        } finally {
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    logger.error(e);
                }
            }

            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    logger.error(e);
                }
            }
        }
    }

    @Override
    public Message selectById(Message task) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<Message> selectAll() {
        List<Message> messages = new ArrayList<>();
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;

        try {
            connection = ConnectionManager.getConnection();
            statement = connection.createStatement();
            resultSet = statement.executeQuery("SELECT * FROM messages LEFT JOIN users ON messages.userId = users.id");
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String method = resultSet.getString("method");
                String name = resultSet.getString("name");
                String date = resultSet.getString("date");
                String text = resultSet.getString("text");
                messages.add(new Message(name, text, date, String.valueOf(id), method));
            }
        } catch (SQLException e) {
            logger.error(e);
        } finally {
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (SQLException e) {
                    logger.error(e);
                }
            }
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    logger.error(e);
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    logger.error(e);
                }
            }
        }
        return messages;
    }

    @Override
    public void delete(int id) {
        throw new UnsupportedOperationException();
    }
}