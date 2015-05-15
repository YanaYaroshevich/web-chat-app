use chat;
select * from users 
where name in (select name from 
(select users.name, count(messages.id) as NumberOfMessages from messages 
LEFT JOIN users
ON messages.userId = users.id
GROUP BY name) AS coolTable where NumberOfMessages > 3);