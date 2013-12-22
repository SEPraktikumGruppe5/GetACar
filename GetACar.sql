-- MySQL dump 10.13  Distrib 5.1.70, for pc-linux-gnu (x86_64)
--
-- Host: localhost    Database: getacar
-- ------------------------------------------------------
-- Server version	5.1.70-log

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
-- Table structure for table `benutzer`
--

DROP TABLE IF EXISTS `benutzer`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `benutzer` (
  `b_id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `b_aktiv` bit(1) NOT NULL DEFAULT b'0',
  `b_email` varchar(50) NOT NULL,
  `b_vorname` varchar(30) NOT NULL,
  `b_nachname` varchar(30) NOT NULL,
  `b_login` varchar(20) NOT NULL,
  `b_passwort` text NOT NULL,
  PRIMARY KEY (`b_id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `benutzer`
--

LOCK TABLES `benutzer` WRITE;
/*!40000 ALTER TABLE `benutzer` DISABLE KEYS */;
INSERT INTO `benutzer` VALUES (1,'','admin@getacar.de','admin','admin','admin','$shiro1$SHA-256$500000$iJRddPc2jpRrerxLRXskmQ==$U88i9zJCUZOKJ++1J3tqW5qTa2xUnCho5AaTHyevmsE='),(2,'','user@getacar.de','user','user','user','$shiro1$SHA-256$500000$A1AfKFLSCVbP3/9C7nBM/A==$NzZuNFZ05acKN5zqgC449FXk8iShHoxUfJrVuCGezm0='),(3,'','tobias.torbaum@tobtor.com','Tobias','Torbaum','tobtor','$shiro1$SHA-256$500000$kx1IcP/1MtPmH3DcMQmK4g==$o3OIyJqLDDgco0g5xQpqy1Pb1yA9t8zsY4VUt/dTWo8=');
/*!40000 ALTER TABLE `benutzer` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `benutzer_rolle`
--

DROP TABLE IF EXISTS `benutzer_rolle`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `benutzer_rolle` (
  `benutzer_b_id` int(10) unsigned NOT NULL,
  `rolle_ro_id` int(10) unsigned NOT NULL,
  UNIQUE KEY `UK_arq7llj7l2qb3a02e3gwtyaon` (`benutzer_b_id`,`rolle_ro_id`),
  KEY `FK_bocdnuape4ed8b4ivrtvhwlrw` (`rolle_ro_id`),
  KEY `FK_34hcs3md2ajdynmnanc4ldcv3` (`benutzer_b_id`),
  CONSTRAINT `FK_34hcs3md2ajdynmnanc4ldcv3` FOREIGN KEY (`benutzer_b_id`) REFERENCES `benutzer` (`b_id`),
  CONSTRAINT `FK_bocdnuape4ed8b4ivrtvhwlrw` FOREIGN KEY (`rolle_ro_id`) REFERENCES `rolle` (`ro_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `benutzer_rolle`
--

LOCK TABLES `benutzer_rolle` WRITE;
/*!40000 ALTER TABLE `benutzer_rolle` DISABLE KEYS */;
INSERT INTO `benutzer_rolle` VALUES (1,1),(1,2),(2,2),(3,2);
/*!40000 ALTER TABLE `benutzer_rolle` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `connection_test_table`
--

DROP TABLE IF EXISTS `connection_test_table`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `connection_test_table` (
  `a` char(1) DEFAULT NULL
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `connection_test_table`
--

LOCK TABLES `connection_test_table` WRITE;
/*!40000 ALTER TABLE `connection_test_table` DISABLE KEYS */;
/*!40000 ALTER TABLE `connection_test_table` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `fahrzeug`
--

DROP TABLE IF EXISTS `fahrzeug`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `fahrzeug` (
  `f_id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `f_aktiv` bit(1) NOT NULL DEFAULT b'0',
  `f_laengengrad_init` decimal(10,7) NOT NULL,
  `f_breitengrad_init` decimal(10,7) NOT NULL,
  `f_bemerkung` text NOT NULL,
  `f_kennzeichen` varchar(20) NOT NULL,
  `ft_id` int(10) unsigned NOT NULL,
  PRIMARY KEY (`f_id`),
  KEY `FK_93sfioc9b5lk3hmp86iq33i7k` (`ft_id`),
  CONSTRAINT `FK_93sfioc9b5lk3hmp86iq33i7k` FOREIGN KEY (`ft_id`) REFERENCES `fahrzeugtyp` (`ft_id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `fahrzeug`
--

LOCK TABLES `fahrzeug` WRITE;
/*!40000 ALTER TABLE `fahrzeug` DISABLE KEYS */;
INSERT INTO `fahrzeug` VALUES (1,'','13.4000000','52.5200000','Ein Fahrzeug','C-IA 666',1),(2,'','12.9273890','50.8392030','Ein zweites Auto','C-IA 667',3),(3,'','12.9149700','50.8427300','Macht Brumbrum','C-IA 668',2),(4,'','12.9149710','50.8427300','Rechtslenker','C-IA 669',2),(5,'','12.9149680','50.8427310','Nr. 5 lebt','C-AT 123',3),(6,'','12.9149690','50.8427310','Sechstes Auto','C-AR 6',1),(7,'','12.9149700','50.8427310','The Magic Number','S-IE 777',1),(8,'','12.9149710','50.8427310','Auto Nummero acht','DD-D 888',3);
/*!40000 ALTER TABLE `fahrzeug` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `fahrzeugbild`
--

DROP TABLE IF EXISTS `fahrzeugbild`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `fahrzeugbild` (
  `fb_id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `fb_dateiname` varchar(75) NOT NULL,
  `f_id` int(10) unsigned NOT NULL,
  PRIMARY KEY (`fb_id`),
  UNIQUE KEY `UK_rjdp9hvho0xi1hksy6yjbju35` (`fb_dateiname`),
  KEY `FK_tjc1724lmw59ci1beies026an` (`f_id`),
  CONSTRAINT `FK_tjc1724lmw59ci1beies026an` FOREIGN KEY (`f_id`) REFERENCES `fahrzeug` (`f_id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `fahrzeugbild`
--

LOCK TABLES `fahrzeugbild` WRITE;
/*!40000 ALTER TABLE `fahrzeugbild` DISABLE KEYS */;
INSERT INTO `fahrzeugbild` VALUES (1,'vehicle1.png',1),(2,'vehicle2.png',2),(3,'vehicle3.png',3),(5,'vehicle4.png',4),(6,'vehicle5.png',5),(7,'vehicle6.png',6),(8,'vehicle7.png',7),(9,'vehicle8.png',8);
/*!40000 ALTER TABLE `fahrzeugbild` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `fahrzeugtyp`
--

DROP TABLE IF EXISTS `fahrzeugtyp`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `fahrzeugtyp` (
  `ft_id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `ft_beschreibung` text NOT NULL,
  `ft_name` varchar(100) NOT NULL,
  `ft_icon` varchar(100) NOT NULL,
  PRIMARY KEY (`ft_id`),
  UNIQUE KEY `UK_isei1uyxue1tt64ijn3235sts` (`ft_name`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `fahrzeugtyp`
--

LOCK TABLES `fahrzeugtyp` WRITE;
/*!40000 ALTER TABLE `fahrzeugtyp` DISABLE KEYS */;
INSERT INTO `fahrzeugtyp` VALUES (1,'A car','Car','car.png'),(2,'A van','Van','van.png'),(3,'A pickup','Pickup','pickup.png');
/*!40000 ALTER TABLE `fahrzeugtyp` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `reservierung`
--

DROP TABLE IF EXISTS `reservierung`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `reservierung` (
  `re_id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `re_end_laengengrad` decimal(10,7) NOT NULL,
  `re_end_breitengrad` decimal(10,7) NOT NULL,
  `re_startzeit` datetime NOT NULL,
  `b_id` int(10) unsigned NOT NULL,
  `f_id` int(10) unsigned NOT NULL,
  `re_endzeit` datetime NOT NULL,
  PRIMARY KEY (`re_id`),
  KEY `FK_ormsdpu23mgh1pixs8arrfjbi` (`b_id`),
  KEY `FK_bk49px1iafeb7y1okrkit6nx8` (`f_id`),
  CONSTRAINT `FK_bk49px1iafeb7y1okrkit6nx8` FOREIGN KEY (`f_id`) REFERENCES `fahrzeug` (`f_id`),
  CONSTRAINT `FK_ormsdpu23mgh1pixs8arrfjbi` FOREIGN KEY (`b_id`) REFERENCES `benutzer` (`b_id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `reservierung`
--

LOCK TABLES `reservierung` WRITE;
/*!40000 ALTER TABLE `reservierung` DISABLE KEYS */;
INSERT INTO `reservierung` VALUES (1,'12.8978770','50.8428160','2013-12-15 09:00:00',1,1,'2013-12-16 09:00:00'),(2,'12.0000000','48.0000000','2013-12-12 09:00:00',1,1,'2013-12-13 09:00:00'),(3,'12.9298980','50.8127220','2013-12-27 19:00:00',2,1,'2013-12-27 23:00:00'),(4,'13.7351170','51.0544570','2013-12-24 10:45:00',2,5,'2013-12-25 10:15:00'),(5,'12.9053440','50.8042220','2014-01-01 03:35:00',1,7,'2014-01-01 06:15:00'),(6,'12.9209200','50.8337910','2013-12-19 10:00:00',3,8,'2013-12-23 10:00:00'),(7,'12.9272890','50.8391990','2014-01-06 08:00:00',1,4,'2014-01-06 16:00:00'),(8,'12.8678670','50.8633680','2014-01-06 16:15:00',2,4,'2014-01-06 20:15:00');
/*!40000 ALTER TABLE `reservierung` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `rolle`
--

DROP TABLE IF EXISTS `rolle`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `rolle` (
  `ro_id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `ro_name` varchar(75) NOT NULL,
  PRIMARY KEY (`ro_id`),
  UNIQUE KEY `UK_d27e58n47eiv2hh23g3ag73m6` (`ro_name`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `rolle`
--

LOCK TABLES `rolle` WRITE;
/*!40000 ALTER TABLE `rolle` DISABLE KEYS */;
INSERT INTO `rolle` VALUES (1,'Admin'),(2,'Benutzer');
/*!40000 ALTER TABLE `rolle` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2013-12-22 14:59:28

drop user getacar;
CREATE USER 'getacar'@'%' IDENTIFIED BY 'getacar';
GRANT USAGE ON * . * TO 'getacar'@'%' IDENTIFIED BY 'getacar';
GRANT ALL PRIVILEGES ON `getacar` . * TO 'getacar'@'%';