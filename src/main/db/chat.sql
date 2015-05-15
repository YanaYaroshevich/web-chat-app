-- MySQL dump 10.13  Distrib 5.6.24, for Win64 (x86_64)
--
-- Host: localhost    Database: chat
-- ------------------------------------------------------
-- Server version	5.6.24

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `messages`
--

DROP TABLE IF EXISTS `messages`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `messages` (
  `id` int(10) unsigned NOT NULL,
  `text` varchar(255) NOT NULL,
  `date` varchar(255) NOT NULL,
  `method` varchar(255) NOT NULL,
  `userId` int(10) unsigned NOT NULL,
  PRIMARY KEY (`id`),
  KEY `userId` (`userId`),
  CONSTRAINT `messages_ibfk_1` FOREIGN KEY (`userId`) REFERENCES `users` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `messages`
--

LOCK TABLES `messages` WRITE;
/*!40000 ALTER TABLE `messages` DISABLE KEYS */;
INSERT INTO `messages` VALUES (1,'Поди-ка, старуха, по коробу поскреби, по сусеку помети, не наскребешь ли муки на колобок.','16:55 15.05.2015','POST',1),(2,'Колобок, Колобок, я тебя съем!','17:00 15.05.2015','POST',4),(3,'Не ешь меня, Заяц, я тебе песенку спою!','17:00 15.05.2015','POST',3),(4,'Колобок, Колобок, я тебя съем!','17:01 15.05.2015','POST',5),(5,'Не ешь меня, Серый Волк, я тебе песенку спою!','17:01 15.05.2015','POST',3),(6,'Колобок, Колобок, я тебя съем!','17:02 15.05.2015','POST',6),(7,'Где тебе, косолапому, съесть меня!','17:02 15.05.2015','POST',3),(8,'Колобок, Колобок, куда катишься?','17:04 15.05.2015','POST',7),(9,'Качусь по дорожке.','17:04 15.05.2015','POST',3),(10,'Колобок, Колобок, спой мне песенку!','17:05 15.05.2015','POST',7),(11,'Я Колобок, Колобок, Я по коробу скребен, По сусеку метен, На сметане мешон Да в масле пряжон, На окошке стужон. Я от дедушки ушел, Я от бабушки ушел, Я от зайца ушел, Я от волка ушел, От медведя ушел, От тебя, лисы, нехитро уйти!','17:07 15.05.2015','POST',3),(12,'Ах, песенка хороша, да слышу я плохо. Колобок, Колобок, сядь ко мне на носок да спой еще разок, погромче.','17:09 15.05.2015','POST',7),(13,'Колобок, Колобок, сядь ко мне на язычок да пропой в последний разок.','17:10 15.05.2015','POST',7),(14,'Пользователь колобок покинул чат.','17:11 15.05.2015','POST',8),(15,'Голубая луна','17:22 15.05.2015','POST',9),(16,'Голубая луна','17:22 15.05.2015','POST',10),(17,'Голубая луна','17:22 15.05.2015','POST',9),(18,'Голубая','17:22 15.05.2015','POST',10),(19,'Как никто его любила голубая луна','17:22 15.05.2015','POST',9),(20,'Голубая луна','17:23 15.05.2015','POST',9),(21,'Луна','17:23 15.05.2015','POST',10),(22,'Голубая луна','17:23 15.05.2015','POST',9),(23,'Просто синяя','17:23 15.05.2015','POST',10),(24,'Звезды сладостью поила голубая луна!','17:23 15.05.2015','POST',9);
/*!40000 ALTER TABLE `messages` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `users` (
  `id` int(10) unsigned NOT NULL,
  `name` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES (1,'старик'),(2,'старуха'),(3,'колобок'),(4,'заяц'),(5,'волк'),(6,'медведь'),(7,'лиса'),(8,'админ'),(9,'Николай Трубач'),(10,'Борис Моисеев');
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2015-05-15 17:35:47
