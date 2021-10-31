CREATE TABLE `Diary_user` (
  `user_id` int(255) NOT NULL AUTO_INCREMENT,
  `email` varchar(255) NOT NULL,
  `user_password` varchar(255) NOT NULL,
  `first_name` varchar(255) NOT NULL,
  `last_name` varchar(255) NOT NULL,
  `balance_amount` decimal(10,0) NOT NULL DEFAULT '0',
  `user_role_id` int(11) DEFAULT '1',
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `UNIQUE` (`email`),
  KEY `user_role_idx` (`user_role_id`),
  CONSTRAINT `user_role` FOREIGN KEY (`user_role_id`) REFERENCES `User_role` (`id`) ON DELETE NO ACTION ON UPDATE CASCADE
)



