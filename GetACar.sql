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
-- Table structure for table `ConnectionTestTable`
--

DROP TABLE IF EXISTS `ConnectionTestTable`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ConnectionTestTable` (
  `a` char(1) DEFAULT NULL
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ConnectionTestTable`
--

LOCK TABLES `ConnectionTestTable` WRITE;
/*!40000 ALTER TABLE `ConnectionTestTable` DISABLE KEYS */;
/*!40000 ALTER TABLE `ConnectionTestTable` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Fahrzeug`
--

DROP TABLE IF EXISTS `Fahrzeug`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Fahrzeug` (
  `FID` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `Aktiv` tinyint(1) NOT NULL DEFAULT '0',
  `Aktuelle_Koordinaten` varchar(40) NOT NULL,
  `Bemerkung` text NOT NULL,
  `Kennzeichen` varchar(20) NOT NULL,
  `Nummer` int(10) NOT NULL,
  `Bild` text NOT NULL,
  `Typ` varchar(100) NOT NULL,
  PRIMARY KEY (`FID`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Fahrzeug`
--

LOCK TABLES `Fahrzeug` WRITE;
/*!40000 ALTER TABLE `Fahrzeug` DISABLE KEYS */;
INSERT INTO `Fahrzeug` VALUES (1,1,'10-10','Dings','C-IA 666',1,'auto.jpg','A-Klasse'),(2,1,'12-12','DingsBums','C-IA 667',2,'auto.jpg','B-Klasse'),(3,1,'14-14','DingsBumsBla','C-IA 668',3,'auto.jpg','C-Klasse');
/*!40000 ALTER TABLE `Fahrzeug` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Nutzer`
--

DROP TABLE IF EXISTS `Nutzer`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Nutzer` (
  `UID` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `Aktiv` tinyint(1) NOT NULL DEFAULT '0',
  `Email` varchar(50) NOT NULL,
  `Vorname` varchar(30) NOT NULL,
  `Nachname` varchar(30) NOT NULL,
  `Login` varchar(20) NOT NULL,
  `Passwort` text NOT NULL,
  PRIMARY KEY (`UID`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Nutzer`
--

LOCK TABLES `Nutzer` WRITE;
/*!40000 ALTER TABLE `Nutzer` DISABLE KEYS */;
INSERT INTO `Nutzer` VALUES (1,1,'admin@getacar.de','Admin','Istrator','admin','$shiro1$SHA-256$500000$iJRddPc2jpRrerxLRXskmQ==$U88i9zJCUZOKJ++1J3tqW5qTa2xUnCho5AaTHyevmsE='),(2,1,'user@getacar.de','Us','Er','user','$shiro1$SHA-256$500000$A1AfKFLSCVbP3/9C7nBM/A==$NzZuNFZ05acKN5zqgC449FXk8iShHoxUfJrVuCGezm0=');
/*!40000 ALTER TABLE `Nutzer` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Nutzer_Rolle`
--

DROP TABLE IF EXISTS `Nutzer_Rolle`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Nutzer_Rolle` (
  `Nutzer_UID` int(10) unsigned NOT NULL,
  `Rolle_ROID` int(10) unsigned NOT NULL,
  UNIQUE KEY `UID_ROID_UC` (`Nutzer_UID`,`Rolle_ROID`),
  KEY `FK_e2bjv48vrvqslf16se35lu6lw` (`Rolle_ROID`),
  KEY `FK_nt9yuwax8xewpxdnnrfa8p3et` (`Nutzer_UID`),
  CONSTRAINT `FK_e2bjv48vrvqslf16se35lu6lw` FOREIGN KEY (`Rolle_ROID`) REFERENCES `Rolle` (`ROID`),
  CONSTRAINT `FK_nt9yuwax8xewpxdnnrfa8p3et` FOREIGN KEY (`Nutzer_UID`) REFERENCES `Nutzer` (`UID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Nutzer_Rolle`
--

LOCK TABLES `Nutzer_Rolle` WRITE;
/*!40000 ALTER TABLE `Nutzer_Rolle` DISABLE KEYS */;
INSERT INTO `Nutzer_Rolle` VALUES (1,1),(1,2),(2,2);
/*!40000 ALTER TABLE `Nutzer_Rolle` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Reservierung`
--

DROP TABLE IF EXISTS `Reservierung`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Reservierung` (
  `RID` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `Endkoordinaten` varchar(40) NOT NULL,
  `Endzeit` datetime NOT NULL,
  `Startkoordinaten` varchar(40) NOT NULL,
  `Startzeit` datetime NOT NULL,
  `UID` int(10) unsigned NOT NULL,
  `CID` int(10) unsigned NOT NULL,
  PRIMARY KEY (`RID`),
  KEY `FK_UID` (`UID`),
  KEY `FK_CID` (`CID`),
  CONSTRAINT `FK_CID` FOREIGN KEY (`CID`) REFERENCES `Fahrzeug` (`FID`),
  CONSTRAINT `FK_UID` FOREIGN KEY (`UID`) REFERENCES `Nutzer` (`UID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Reservierung`
--

LOCK TABLES `Reservierung` WRITE;
/*!40000 ALTER TABLE `Reservierung` DISABLE KEYS */;
/*!40000 ALTER TABLE `Reservierung` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Rolle`
--

DROP TABLE IF EXISTS `Rolle`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Rolle` (
  `ROID` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `r_name` varchar(75) NOT NULL,
  PRIMARY KEY (`ROID`),
  UNIQUE KEY `UK_j33sfc6vevcosj3ohs33fcbd8` (`r_name`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Rolle`
--

LOCK TABLES `Rolle` WRITE;
/*!40000 ALTER TABLE `Rolle` DISABLE KEYS */;
INSERT INTO `Rolle` VALUES (1,'Admin'),(2,'Benutzer');
/*!40000 ALTER TABLE `Rolle` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2013-11-14 15:36:27
