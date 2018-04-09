CREATE TABLE `ACCESS` (
  `ID` bigint(10) NOT NULL AUTO_INCREMENT,
  `IP_ADDRESS` varchar(45) NOT NULL,
  `METHOD` varchar(45) NOT NULL,
  `STATUS` int(11) NOT NULL,
  `USER_AGENT` varchar(300) NOT NULL,
  `TIMESTAMP` datetime(3) NOT NULL,
  PRIMARY KEY (`ID`,`IP_ADDRESS`,`TIMESTAMP`),
  KEY `IP_ADDRESS_IX` (`IP_ADDRESS`),
  KEY `TIMESTAMP_IX` (`TIMESTAMP`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=latin1;