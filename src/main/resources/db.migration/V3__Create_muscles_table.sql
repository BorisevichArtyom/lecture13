CREATE TABLE `Muscles` (
  `Muscle_id` int(11) NOT NULL AUTO_INCREMENT,
  `Muscle_name` varchar(45) NOT NULL,
  PRIMARY KEY (`Muscle_id`),
  UNIQUE KEY `Muscle_id_UNIQUE` (`Muscle_id`),
  UNIQUE KEY `Muscle_name_UNIQUE` (`Muscle_name`)
)





