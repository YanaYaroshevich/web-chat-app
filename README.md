# WebChatApplication
We work with two histories: 1) History of messages, 2) History of id-s of requests (methods)
The second history will help us to work with many users.
We will form response using last id-s: look for messages with such id-s in history of messages and add them to response.
Both of these histories are added to XML-files. It will help us to restart last session.
