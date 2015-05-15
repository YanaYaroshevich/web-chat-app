use chat;
select * from messages where userId = (select id from users where name = 'Борис Моисеев')
and '17:22 15.05.2015' < date and date < '17:24 15.05.2015' ;