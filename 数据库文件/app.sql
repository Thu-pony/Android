/*
Navicat MySQL Data Transfer

Source Server         : AnyiAssistor
Source Server Version : 80035
Source Host           : localhost:3306
Source Database       : app

Target Server Type    : MYSQL
Target Server Version : 80035
File Encoding         : 65001

Date: 2023-12-06 20:00:43
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for orders
-- ----------------------------
DROP TABLE IF EXISTS `orders`;
CREATE TABLE `orders` (
  `order_id` int NOT NULL AUTO_INCREMENT,
  `product_type` varchar(50) COLLATE utf16_bin DEFAULT NULL,
  `creation_time` datetime DEFAULT NULL,
  `status` varchar(50) COLLATE utf16_bin DEFAULT NULL,
  PRIMARY KEY (`order_id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf16 COLLATE=utf16_bin;

-- ----------------------------
-- Records of orders
-- ----------------------------
INSERT INTO `orders` VALUES ('1', 'apple', '2023-12-03 20:19:56', '已完成');
INSERT INTO `orders` VALUES ('5', 'orange', '2023-12-04 21:13:51', '处理中');

-- ----------------------------
-- Table structure for users
-- ----------------------------
DROP TABLE IF EXISTS `users`;
CREATE TABLE `users` (
  `UID` int NOT NULL AUTO_INCREMENT,
  `username` varchar(50) COLLATE utf16_bin NOT NULL,
  `password` varchar(255) COLLATE utf16_bin NOT NULL,
  `user_type` varchar(50) COLLATE utf16_bin DEFAULT NULL,
  PRIMARY KEY (`UID`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf16 COLLATE=utf16_bin;

-- ----------------------------
-- Records of users
-- ----------------------------
INSERT INTO `users` VALUES ('1', 'aaaaa', '123456789a', '0');
INSERT INTO `users` VALUES ('2', 'bbbb', 'cccccsad1q4', '0');
INSERT INTO `users` VALUES ('3', 'bbbba', 'bbbb232131', '0');
INSERT INTO `users` VALUES ('4', 'qwer', 'asfbsdasdas', '0');
INSERT INTO `users` VALUES ('5', 'bbbbaaa', 'bbbb232131a', '0');
INSERT INTO `users` VALUES ('6', '123456', '123456', '0');
INSERT INTO `users` VALUES ('7', '?', '123124', '0');

-- ----------------------------
-- Table structure for user_order_relations
-- ----------------------------
DROP TABLE IF EXISTS `user_order_relations`;
CREATE TABLE `user_order_relations` (
  `relation_id` int NOT NULL AUTO_INCREMENT,
  `user_UID` int DEFAULT NULL,
  `order_order_id` int DEFAULT NULL,
  PRIMARY KEY (`relation_id`),
  KEY `user_UID` (`user_UID`),
  KEY `order_order_id` (`order_order_id`),
  CONSTRAINT `user_order_relations_ibfk_1` FOREIGN KEY (`user_UID`) REFERENCES `users` (`UID`),
  CONSTRAINT `user_order_relations_ibfk_2` FOREIGN KEY (`order_order_id`) REFERENCES `orders` (`order_id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf16 COLLATE=utf16_bin;

-- ----------------------------
-- Records of user_order_relations
-- ----------------------------
INSERT INTO `user_order_relations` VALUES ('1', '3', '1');
INSERT INTO `user_order_relations` VALUES ('3', '3', '5');
