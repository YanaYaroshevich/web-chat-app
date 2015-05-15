use chat;
select * from messages where userId = (select id from users where name = 'Борис Моисеев') 
and text like '%луна%';