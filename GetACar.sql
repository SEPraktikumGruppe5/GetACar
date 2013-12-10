-- MySQL dump 10.13  Distrib 5.5.32, for Win32 (x86)
--
-- Host: localhost    Database: getacar
-- ------------------------------------------------------
-- Server version	5.5.32

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
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `benutzer`
--

LOCK TABLES `benutzer` WRITE;
/*!40000 ALTER TABLE `benutzer` DISABLE KEYS */;
INSERT INTO `benutzer` VALUES (1,'\1','admin@getacar.de','admin','admin','admin','$shiro1$SHA-256$500000$iJRddPc2jpRrerxLRXskmQ==$U88i9zJCUZOKJ++1J3tqW5qTa2xUnCho5AaTHyevmsE='),(2,'\1','user@getacar.de','user','user','user','$shiro1$SHA-256$500000$A1AfKFLSCVbP3/9C7nBM/A==$NzZuNFZ05acKN5zqgC449FXk8iShHoxUfJrVuCGezm0=');
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
INSERT INTO `benutzer_rolle` VALUES (1,1),(1,2),(2,2);
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
  `f_akt_koord_laenge` decimal(9,6) DEFAULT NULL,
  `f_akt_koord_breite` decimal(9,6) DEFAULT NULL,
  `f_bemerkung` text NOT NULL,
  `f_kennzeichen` varchar(20) NOT NULL,
  `f_bild` text NOT NULL,
  `f_typ` varchar(100) NOT NULL,
  PRIMARY KEY (`f_id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `fahrzeug`
--

LOCK TABLES `fahrzeug` WRITE;
/*!40000 ALTER TABLE `fahrzeug` DISABLE KEYS */;
INSERT INTO `fahrzeug` VALUES (1,'',12.927389,50.839203,'Ein Fahrzeug','C-IA 666','auto.jpg','Auto'),(2,'',12.927389,50.839203,'Ein zweites Auto','C-IA 667','auto2.jpg','Auto');
/*!40000 ALTER TABLE `fahrzeug` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `reservierung`
--

DROP TABLE IF EXISTS `reservierung`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `reservierung` (
  `re_id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `re_endkoord_laenge` decimal(9,6) NOT NULL,
  `re_endkoord_breite` decimal(9,6) NOT NULL,
  `re_endzeit` datetime NOT NULL,
  `re_startzeit` datetime NOT NULL,
  `b_id` int(10) unsigned NOT NULL,
  `f_id` int(10) unsigned NOT NULL,
  PRIMARY KEY (`re_id`),
  KEY `FK_ormsdpu23mgh1pixs8arrfjbi` (`b_id`),
  KEY `FK_bk49px1iafeb7y1okrkit6nx8` (`f_id`),
  CONSTRAINT `FK_bk49px1iafeb7y1okrkit6nx8` FOREIGN KEY (`f_id`) REFERENCES `fahrzeug` (`f_id`),
  CONSTRAINT `FK_ormsdpu23mgh1pixs8arrfjbi` FOREIGN KEY (`b_id`) REFERENCES `benutzer` (`b_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `reservierung`
--

LOCK TABLES `reservierung` WRITE;
/*!40000 ALTER TABLE `reservierung` DISABLE KEYS */;
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

-- Dump completed on 2013-11-24 20:31:34

-- Create user

drop user getacar;
CREATE USER 'getacar'@'%' IDENTIFIED BY 'getacar';
GRANT USAGE ON * . * TO 'getacar'@'%' IDENTIFIED BY 'getacar';
GRANT ALL PRIVILEGES ON `getacar` . * TO 'getacar'@'%';
