# WebChatApplication

#Task11

We work with two histories: 1) History of messages, 2) History of id-s of requests (methods)
The second history will help us to work with AJAX-polling.
We will form response using last id-s: look for messages with such id-s in history of messages and add them to response.
Both of these histories are added to XML-files. It will help us to restart last session.

Messages are not deleted from history.xml. They stay there in format: 
{
    "id" : "id",
    "date" : "date",
    "name" : "name",
    "text" : "",
    "method" : "DELETE"
}
On user side with "trash"-icon.

Method PUT is not allowed.

Switch on the sound! ;)

#Task14

Realization of work with DB:
We work with to tables: users (user(id, name)) and messages (message(id, text, date, method, userId))
When the user writes a message in chat for the first time, we add his/her name in table users with new unique id and then add his/her message in table messages.
 If it is not the first time, we find user by name (name is unique) in table users and then add his/her message with this id in table messages.
 When we restart the server we get all history with the help of function public List<Message> selectAll(){..} in MessageDaoImpl/MessageDao class. We form one table from users and their messages using left join and then form list of messaages.  
 
 Logging:
 Before starting the program create a table in the database chat:
 
 CREATE TABLE LOGS
   (USER_ID VARCHAR(20)    NOT NULL,
    DATED   VARCHAR(100)           NOT NULL,
    LOGGER  VARCHAR(500)    NOT NULL,
    LEVEL   VARCHAR(10)    NOT NULL,
    MESSAGE VARCHAR(100000)  NOT NULL
   );
   
   to see the result use "select * from logs".