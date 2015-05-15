use chat;
select * from messages where userId = (select id from users where name = 'колобок');