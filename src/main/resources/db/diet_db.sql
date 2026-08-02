/*
 Navicat Premium Dump SQL

 Source Server         : 本地MySQL
 Source Server Type    : MySQL
 Source Server Version : 80400 (8.4.0)
 Source Host           : localhost:3306
 Source Schema         : diet_db

 Target Server Type    : MySQL
 Target Server Version : 80400 (8.4.0)
 File Encoding         : 65001

 Date: 13/07/2026 20:24:38
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for diet_messages
-- ----------------------------
DROP TABLE IF EXISTS `diet_messages`;
CREATE TABLE `diet_messages`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `session_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `role` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `intent` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `agent_trace_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `created_at` datetime NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_message_session`(`session_id` ASC, `created_at` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 39 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of diet_messages
-- ----------------------------

-- ----------------------------
-- Table structure for diet_request_trace
-- ----------------------------
DROP TABLE IF EXISTS `diet_request_trace`;
CREATE TABLE `diet_request_trace`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `trace_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `session_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `user_id` bigint NOT NULL,
  `status` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `event_count` int NOT NULL DEFAULT 0,
  `duration_ms` bigint NULL DEFAULT NULL,
  `error_message` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL,
  `trace_json` json NOT NULL,
  `created_at` datetime NOT NULL,
  `updated_at` datetime NOT NULL,
  `expected_intent` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `expected_slots` json NULL,
  `expected_clarify_action` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `labeled_by` bigint NULL DEFAULT NULL,
  `labeled_at` datetime NULL DEFAULT NULL,
  `label_note` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_request_trace`(`trace_id` ASC) USING BTREE,
  INDEX `idx_request_trace_session`(`session_id` ASC, `created_at` ASC) USING BTREE,
  INDEX `idx_request_trace_user`(`user_id` ASC, `created_at` ASC) USING BTREE,
  INDEX `idx_request_trace_status`(`status` ASC, `created_at` ASC) USING BTREE,
  INDEX `idx_request_trace_label`(`expected_intent` ASC, `labeled_at` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 20 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of diet_request_trace
-- ----------------------------

-- ----------------------------
-- Table structure for diet_sessions
-- ----------------------------
DROP TABLE IF EXISTS `diet_sessions`;
CREATE TABLE `diet_sessions`  (
  `id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `user_id` bigint NOT NULL,
  `phase` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `slots` json NOT NULL,
  `last_recommendations` json NOT NULL,
  `created_at` datetime NOT NULL,
  `updated_at` datetime NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_session_user`(`user_id` ASC, `updated_at` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of diet_sessions
-- ----------------------------

-- ----------------------------
-- Table structure for diet_slot_option
-- ----------------------------
DROP TABLE IF EXISTS `diet_slot_option`;
CREATE TABLE `diet_slot_option`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `slot_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `option_value` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `sort_order` int NOT NULL DEFAULT 0,
  `enabled` tinyint NOT NULL DEFAULT 1,
  `created_at` datetime NOT NULL,
  `updated_at` datetime NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_slot_option`(`slot_name` ASC, `option_value` ASC) USING BTREE,
  INDEX `idx_slot_enabled`(`slot_name` ASC, `enabled` ASC, `sort_order` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 281 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of diet_slot_option
-- ----------------------------
INSERT INTO `diet_slot_option` VALUES (1, 'mealTime', '早餐', 10, 1, '2026-06-28 17:37:55', '2026-06-28 17:37:55');
INSERT INTO `diet_slot_option` VALUES (2, 'mealTime', '早午餐', 20, 1, '2026-06-28 17:37:55', '2026-06-28 17:37:55');
INSERT INTO `diet_slot_option` VALUES (3, 'mealTime', '午餐', 30, 1, '2026-06-28 17:37:55', '2026-06-28 17:37:55');
INSERT INTO `diet_slot_option` VALUES (4, 'mealTime', '下午茶', 40, 1, '2026-06-28 17:37:55', '2026-06-28 17:37:55');
INSERT INTO `diet_slot_option` VALUES (5, 'mealTime', '晚餐', 50, 1, '2026-06-28 17:37:55', '2026-06-28 17:37:55');
INSERT INTO `diet_slot_option` VALUES (6, 'mealTime', '夜宵', 60, 1, '2026-06-28 17:37:55', '2026-06-28 17:37:55');
INSERT INTO `diet_slot_option` VALUES (7, 'mealTime', '加餐', 70, 1, '2026-06-28 17:37:55', '2026-06-28 17:37:55');
INSERT INTO `diet_slot_option` VALUES (8, 'mealTime', '三餐', 80, 1, '2026-06-28 17:37:55', '2026-06-28 17:37:55');
INSERT INTO `diet_slot_option` VALUES (9, 'mood', '疲惫', 10, 1, '2026-06-28 17:37:55', '2026-06-28 17:37:55');
INSERT INTO `diet_slot_option` VALUES (10, 'mood', '烦躁', 20, 1, '2026-06-28 17:37:55', '2026-06-28 17:37:55');
INSERT INTO `diet_slot_option` VALUES (11, 'mood', '开心', 30, 1, '2026-06-28 17:37:55', '2026-06-28 17:37:55');
INSERT INTO `diet_slot_option` VALUES (12, 'mood', '焦虑', 40, 1, '2026-06-28 17:37:55', '2026-06-28 17:37:55');
INSERT INTO `diet_slot_option` VALUES (13, 'mood', '低落', 50, 1, '2026-06-28 17:37:55', '2026-06-28 17:37:55');
INSERT INTO `diet_slot_option` VALUES (14, 'mood', '平静', 60, 1, '2026-06-28 17:37:55', '2026-06-28 17:37:55');
INSERT INTO `diet_slot_option` VALUES (15, 'mood', '压力大', 70, 1, '2026-06-28 17:37:55', '2026-06-28 17:37:55');
INSERT INTO `diet_slot_option` VALUES (16, 'mood', '没胃口', 80, 1, '2026-06-28 17:37:55', '2026-06-28 17:37:55');
INSERT INTO `diet_slot_option` VALUES (17, 'mood', '想放松', 90, 1, '2026-06-28 17:37:55', '2026-06-28 17:37:55');
INSERT INTO `diet_slot_option` VALUES (18, 'mood', '想奖励自己', 100, 1, '2026-06-28 17:37:55', '2026-06-28 17:37:55');
INSERT INTO `diet_slot_option` VALUES (19, 'scene', '工作', 10, 1, '2026-06-28 17:37:55', '2026-06-28 17:37:55');
INSERT INTO `diet_slot_option` VALUES (20, 'scene', '校园', 20, 1, '2026-06-28 17:37:55', '2026-06-28 17:37:55');
INSERT INTO `diet_slot_option` VALUES (21, 'scene', '家里', 30, 1, '2026-06-28 17:37:55', '2026-06-28 17:37:55');
INSERT INTO `diet_slot_option` VALUES (22, 'scene', '周末', 40, 1, '2026-06-28 17:37:55', '2026-06-28 17:37:55');
INSERT INTO `diet_slot_option` VALUES (23, 'scene', '加班', 50, 1, '2026-06-28 17:37:55', '2026-06-28 17:37:55');
INSERT INTO `diet_slot_option` VALUES (24, 'scene', '运动后', 60, 1, '2026-06-28 17:37:55', '2026-06-28 17:37:55');
INSERT INTO `diet_slot_option` VALUES (25, 'scene', '通勤', 70, 1, '2026-06-28 17:37:55', '2026-06-28 17:37:55');
INSERT INTO `diet_slot_option` VALUES (26, 'scene', '聚餐', 80, 1, '2026-06-28 17:37:55', '2026-06-28 17:37:55');
INSERT INTO `diet_slot_option` VALUES (27, 'scene', '独处', 90, 1, '2026-06-28 17:37:55', '2026-06-28 17:37:55');
INSERT INTO `diet_slot_option` VALUES (28, 'scene', '旅行', 100, 1, '2026-06-28 17:37:55', '2026-06-28 17:37:55');
INSERT INTO `diet_slot_option` VALUES (30, 'healthGoal', '减脂', 10, 1, '2026-06-28 17:37:55', '2026-06-28 17:37:55');
INSERT INTO `diet_slot_option` VALUES (31, 'healthGoal', '清淡', 20, 1, '2026-06-28 17:37:55', '2026-06-28 17:37:55');
INSERT INTO `diet_slot_option` VALUES (32, 'healthGoal', '养胃', 30, 1, '2026-06-28 17:37:55', '2026-06-28 17:37:55');
INSERT INTO `diet_slot_option` VALUES (33, 'healthGoal', '高蛋白', 40, 1, '2026-06-28 17:37:55', '2026-06-28 17:37:55');
INSERT INTO `diet_slot_option` VALUES (34, 'healthGoal', '均衡', 50, 1, '2026-06-28 17:37:55', '2026-06-28 17:37:55');
INSERT INTO `diet_slot_option` VALUES (35, 'healthGoal', '降火', 60, 1, '2026-06-28 17:37:55', '2026-06-28 17:37:55');
INSERT INTO `diet_slot_option` VALUES (36, 'healthGoal', '低油', 70, 1, '2026-06-28 17:37:55', '2026-06-28 17:37:55');
INSERT INTO `diet_slot_option` VALUES (37, 'healthGoal', '低盐', 80, 1, '2026-06-28 17:37:55', '2026-06-28 17:37:55');
INSERT INTO `diet_slot_option` VALUES (38, 'healthGoal', '低糖', 90, 1, '2026-06-28 17:37:55', '2026-06-28 17:37:55');
INSERT INTO `diet_slot_option` VALUES (39, 'healthGoal', '补能', 100, 1, '2026-06-28 17:37:55', '2026-06-28 17:37:55');
INSERT INTO `diet_slot_option` VALUES (40, 'healthGoal', '增肌', 110, 1, '2026-06-28 17:37:55', '2026-06-28 17:37:55');
INSERT INTO `diet_slot_option` VALUES (41, 'healthGoal', '控碳水', 120, 1, '2026-06-28 17:37:55', '2026-06-28 17:37:55');
INSERT INTO `diet_slot_option` VALUES (42, 'healthGoal', '易消化', 130, 1, '2026-06-28 17:37:55', '2026-06-28 17:37:55');
INSERT INTO `diet_slot_option` VALUES (43, 'healthGoal', '暖胃', 140, 1, '2026-06-28 17:37:55', '2026-06-28 17:37:55');
INSERT INTO `diet_slot_option` VALUES (44, 'cuisine', '川菜', 10, 1, '2026-06-28 17:37:55', '2026-06-28 17:37:55');
INSERT INTO `diet_slot_option` VALUES (45, 'cuisine', '粤菜', 20, 1, '2026-06-28 17:37:55', '2026-06-28 17:37:55');
INSERT INTO `diet_slot_option` VALUES (46, 'cuisine', '湘菜', 30, 1, '2026-06-28 17:37:55', '2026-06-28 17:37:55');
INSERT INTO `diet_slot_option` VALUES (47, 'cuisine', '江浙菜', 40, 1, '2026-06-28 17:37:55', '2026-06-28 17:37:55');
INSERT INTO `diet_slot_option` VALUES (48, 'cuisine', '东北菜', 50, 1, '2026-06-28 17:37:55', '2026-06-28 17:37:55');
INSERT INTO `diet_slot_option` VALUES (49, 'cuisine', '鲁菜', 60, 1, '2026-06-28 17:37:55', '2026-06-28 17:37:55');
INSERT INTO `diet_slot_option` VALUES (50, 'cuisine', '闽南菜', 70, 1, '2026-06-28 17:37:55', '2026-06-28 17:37:55');
INSERT INTO `diet_slot_option` VALUES (51, 'cuisine', '云南菜', 80, 1, '2026-06-28 17:37:55', '2026-06-28 17:37:55');
INSERT INTO `diet_slot_option` VALUES (52, 'cuisine', '新疆菜', 90, 1, '2026-06-28 17:37:55', '2026-06-28 17:37:55');
INSERT INTO `diet_slot_option` VALUES (53, 'cuisine', '轻食', 100, 1, '2026-06-28 17:37:55', '2026-06-28 17:37:55');
INSERT INTO `diet_slot_option` VALUES (54, 'cuisine', '西餐', 110, 1, '2026-06-28 17:37:55', '2026-06-28 17:37:55');
INSERT INTO `diet_slot_option` VALUES (55, 'cuisine', '日料', 120, 1, '2026-06-28 17:37:55', '2026-06-28 17:37:55');
INSERT INTO `diet_slot_option` VALUES (56, 'cuisine', '韩餐', 130, 1, '2026-06-28 17:37:55', '2026-06-28 17:37:55');
INSERT INTO `diet_slot_option` VALUES (57, 'cuisine', '东南亚菜', 140, 1, '2026-06-28 17:37:55', '2026-06-28 17:37:55');
INSERT INTO `diet_slot_option` VALUES (58, 'cuisine', '火锅', 150, 1, '2026-06-28 17:37:55', '2026-06-28 17:37:55');
INSERT INTO `diet_slot_option` VALUES (59, 'cuisine', '烧烤', 160, 1, '2026-06-28 17:37:55', '2026-06-28 17:37:55');
INSERT INTO `diet_slot_option` VALUES (60, 'cuisine', '海鲜', 170, 1, '2026-06-28 17:37:55', '2026-06-28 17:37:55');
INSERT INTO `diet_slot_option` VALUES (61, 'cuisine', '素食', 180, 1, '2026-06-28 17:37:55', '2026-06-28 17:37:55');
INSERT INTO `diet_slot_option` VALUES (62, 'cuisine', '家常', 190, 1, '2026-06-28 17:37:55', '2026-06-28 17:37:55');
INSERT INTO `diet_slot_option` VALUES (63, 'cuisine', '小吃', 200, 1, '2026-06-28 17:37:55', '2026-06-28 17:37:55');
INSERT INTO `diet_slot_option` VALUES (64, 'cuisine', '粉面', 210, 1, '2026-06-28 17:37:55', '2026-06-28 17:37:55');
INSERT INTO `diet_slot_option` VALUES (65, 'cuisine', '粥汤', 220, 1, '2026-06-28 17:37:55', '2026-06-28 17:37:55');
INSERT INTO `diet_slot_option` VALUES (66, 'cuisine', '快餐', 230, 1, '2026-06-28 17:37:55', '2026-06-28 17:37:55');
INSERT INTO `diet_slot_option` VALUES (67, 'cuisine', '甜品', 240, 1, '2026-06-28 17:37:55', '2026-06-28 17:37:55');
INSERT INTO `diet_slot_option` VALUES (69, 'taste', '辣', 20, 1, '2026-06-28 17:37:55', '2026-06-28 17:37:55');
INSERT INTO `diet_slot_option` VALUES (70, 'taste', '微辣', 30, 1, '2026-06-28 17:37:55', '2026-06-28 17:37:55');
INSERT INTO `diet_slot_option` VALUES (71, 'taste', '中辣', 40, 1, '2026-06-28 17:37:55', '2026-06-28 17:37:55');
INSERT INTO `diet_slot_option` VALUES (72, 'taste', '麻辣', 50, 1, '2026-06-28 17:37:55', '2026-06-28 17:37:55');
INSERT INTO `diet_slot_option` VALUES (73, 'taste', '甜', 60, 1, '2026-06-28 17:37:55', '2026-06-28 17:37:55');
INSERT INTO `diet_slot_option` VALUES (74, 'taste', '酸甜', 70, 1, '2026-06-28 17:37:55', '2026-06-28 17:37:55');
INSERT INTO `diet_slot_option` VALUES (75, 'taste', '咸鲜', 80, 1, '2026-06-28 17:37:55', '2026-06-28 17:37:55');
INSERT INTO `diet_slot_option` VALUES (76, 'taste', '鲜香', 90, 1, '2026-06-28 17:37:55', '2026-06-28 17:37:55');
INSERT INTO `diet_slot_option` VALUES (77, 'taste', '酱香', 100, 1, '2026-06-28 17:37:55', '2026-06-28 17:37:55');
INSERT INTO `diet_slot_option` VALUES (78, 'taste', '蒜香', 110, 1, '2026-06-28 17:37:55', '2026-06-28 17:37:55');
INSERT INTO `diet_slot_option` VALUES (79, 'taste', '番茄味', 120, 1, '2026-06-28 17:37:55', '2026-06-28 17:37:55');
INSERT INTO `diet_slot_option` VALUES (80, 'taste', '咖喱味', 130, 1, '2026-06-28 17:37:55', '2026-06-28 17:37:55');
INSERT INTO `diet_slot_option` VALUES (81, 'taste', '奶香', 140, 1, '2026-06-28 17:37:55', '2026-06-28 17:37:55');
INSERT INTO `diet_slot_option` VALUES (82, 'taste', '油香', 150, 1, '2026-06-28 17:37:55', '2026-06-28 17:37:55');
INSERT INTO `diet_slot_option` VALUES (83, 'taste', '烟火气', 160, 1, '2026-06-28 17:37:55', '2026-06-28 17:37:55');
INSERT INTO `diet_slot_option` VALUES (84, 'convenience', '快速', 10, 1, '2026-06-28 17:37:55', '2026-06-28 17:37:55');
INSERT INTO `diet_slot_option` VALUES (85, 'convenience', '慢享', 20, 1, '2026-06-28 17:37:55', '2026-06-28 17:37:55');
INSERT INTO `diet_slot_option` VALUES (86, 'convenience', '外带方便', 30, 1, '2026-06-28 17:37:55', '2026-06-28 17:37:55');
INSERT INTO `diet_slot_option` VALUES (87, 'convenience', '堂食舒服', 40, 1, '2026-06-28 17:37:55', '2026-06-28 17:37:55');
INSERT INTO `diet_slot_option` VALUES (88, 'convenience', '少排队', 50, 1, '2026-06-28 17:37:55', '2026-06-28 17:37:55');
INSERT INTO `diet_slot_option` VALUES (89, 'convenience', '少餐具', 60, 1, '2026-06-28 17:37:55', '2026-06-28 17:37:55');
INSERT INTO `diet_slot_option` VALUES (90, 'convenience', '一人食', 70, 1, '2026-06-28 17:37:55', '2026-06-28 17:37:55');
INSERT INTO `diet_slot_option` VALUES (91, 'convenience', '多人共享', 80, 1, '2026-06-28 17:37:55', '2026-06-28 17:37:55');
INSERT INTO `diet_slot_option` VALUES (92, 'convenience', '适合备餐', 90, 1, '2026-06-28 17:37:55', '2026-06-28 17:37:55');
INSERT INTO `diet_slot_option` VALUES (93, 'convenience', '适合边走边吃', 100, 1, '2026-06-28 17:37:55', '2026-06-28 17:37:55');

-- ----------------------------
-- Table structure for meal_item
-- ----------------------------
DROP TABLE IF EXISTS `meal_item`;
CREATE TABLE `meal_item`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `source_type` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `owner_user_id` bigint NULL DEFAULT NULL,
  `name` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `meal_time` json NOT NULL,
  `mood` json NOT NULL,
  `scene` json NOT NULL,
  `health_goal` json NOT NULL,
  `cuisine` json NOT NULL,
  `taste` json NOT NULL,
  `convenience` json NOT NULL,
  `created_at` datetime NOT NULL,
  `updated_at` datetime NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_public_meal_source`(`source_type` ASC) USING BTREE,
  INDEX `idx_private_meal_source`(`owner_user_id` ASC, `source_type` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of meal_item
-- ----------------------------
INSERT INTO `meal_item` VALUES (1, 'PUBLIC', NULL, '番茄鸡蛋面', '[\"午餐\", \"晚餐\", \"三餐\"]', '[\"疲惫\", \"低落\"]', '[\"工作\", \"校园\", \"家里\"]', '[\"清淡\", \"养胃\", \"易消化\"]', '[\"家常\", \"粉面\"]', '[\"清淡\", \"番茄味\"]', '[\"快速\", \"一人食\"]', '2026-06-28 17:37:55', '2026-06-28 17:37:55');
INSERT INTO `meal_item` VALUES (2, 'PUBLIC', NULL, '清汤馄饨', '[\"早餐\", \"午餐\", \"晚餐\", \"三餐\"]', '[\"疲惫\", \"没胃口\"]', '[\"工作\", \"校园\", \"家里\"]', '[\"清淡\", \"养胃\", \"暖胃\"]', '[\"小吃\", \"粥汤\"]', '[\"清淡\", \"咸鲜\"]', '[\"快速\", \"少餐具\"]', '2026-06-28 17:37:55', '2026-06-28 17:37:55');
INSERT INTO `meal_item` VALUES (3, 'PUBLIC', NULL, '鸡胸肉轻食碗', '[\"午餐\", \"晚餐\"]', '[\"平静\", \"想放松\"]', '[\"工作\", \"运动后\"]', '[\"减脂\", \"高蛋白\", \"低油\", \"均衡\"]', '[\"轻食\"]', '[\"清淡\", \"咸鲜\"]', '[\"快速\", \"一人食\"]', '2026-06-28 17:37:55', '2026-06-28 17:37:55');
INSERT INTO `meal_item` VALUES (4, 'PUBLIC', NULL, '麻辣香锅', '[\"午餐\", \"晚餐\", \"夜宵\"]', '[\"开心\", \"想奖励自己\"]', '[\"周末\", \"聚餐\", \"夜宵\"]', '[\"均衡\", \"补能\"]', '[\"川菜\", \"小吃\"]', '[\"麻辣\", \"烟火气\"]', '[\"慢享\", \"多人共享\"]', '2026-06-28 17:37:55', '2026-06-28 17:37:55');
INSERT INTO `meal_item` VALUES (5, 'PERSONAL', 1, '土豆炖牛肉', '[\"晚餐\"]', '[\"平静\"]', '[\"校园\"]', '[\"补能\"]', '[\"湘菜\"]', '[\"辣\"]', '[]', '2026-07-01 23:22:49', '2026-07-01 23:22:49');
INSERT INTO `meal_item` VALUES (6, 'PUBLIC', NULL, '南瓜小米粥', '[\"早餐\", \"三餐\"]', '[\"疲惫\", \"没胃口\"]', '[\"家里\", \"校园\"]', '[\"养胃\", \"清淡\", \"易消化\", \"暖胃\"]', '[\"粥汤\"]', '[\"甜\"]', '[\"快速\", \"少餐具\"]', NOW(), NOW());
INSERT INTO `meal_item` VALUES (7, 'PUBLIC', NULL, '燕麦牛奶杯', '[\"早餐\", \"加餐\"]', '[\"平静\", \"想放松\"]', '[\"工作\", \"通勤\"]', '[\"减脂\", \"均衡\", \"低糖\"]', '[\"轻食\", \"甜品\"]', '[\"奶香\"]', '[\"快速\", \"外带方便\", \"一人食\"]', NOW(), NOW());
INSERT INTO `meal_item` VALUES (8, 'PUBLIC', NULL, '全麦鸡蛋三明治', '[\"早餐\", \"早午餐\"]', '[\"疲惫\"]', '[\"工作\", \"通勤\", \"校园\"]', '[\"高蛋白\", \"均衡\", \"低油\"]', '[\"西餐\", \"快餐\"]', '[\"咸鲜\", \"奶香\"]', '[\"快速\", \"外带方便\", \"适合边走边吃\"]', NOW(), NOW());
INSERT INTO `meal_item` VALUES (9, 'PUBLIC', NULL, '豆浆蔬菜包', '[\"早餐\"]', '[\"疲惫\", \"没胃口\"]', '[\"工作\", \"校园\"]', '[\"清淡\", \"均衡\", \"易消化\"]', '[\"快餐\", \"家常\"]', '[\"咸鲜\"]', '[\"快速\", \"少排队\", \"外带方便\"]', NOW(), NOW());
INSERT INTO `meal_item` VALUES (10, 'PUBLIC', NULL, '皮蛋瘦肉粥', '[\"早餐\", \"夜宵\", \"三餐\"]', '[\"疲惫\", \"没胃口\"]', '[\"家里\", \"加班\"]', '[\"养胃\", \"暖胃\", \"补能\"]', '[\"粥汤\"]', '[\"咸鲜\", \"鲜香\"]', '[\"快速\", \"少餐具\"]', NOW(), NOW());
INSERT INTO `meal_item` VALUES (11, 'PUBLIC', NULL, '紫薯酸奶碗', '[\"早餐\", \"下午茶\", \"加餐\"]', '[\"开心\", \"想放松\"]', '[\"工作\", \"独处\"]', '[\"减脂\", \"低糖\", \"均衡\"]', '[\"轻食\", \"甜品\"]', '[\"酸甜\", \"奶香\"]', '[\"快速\", \"一人食\", \"适合备餐\"]', NOW(), NOW());
INSERT INTO `meal_item` VALUES (12, 'PUBLIC', NULL, '鲜虾云吞面', '[\"午餐\", \"晚餐\", \"夜宵\"]', '[\"疲惫\", \"没胃口\"]', '[\"工作\", \"校园\", \"旅行\"]', '[\"补能\", \"易消化\"]', '[\"小吃\", \"粉面\"]', '[\"咸鲜\", \"鲜香\"]', '[\"快速\", \"少餐具\"]', NOW(), NOW());
INSERT INTO `meal_item` VALUES (13, 'PUBLIC', NULL, '牛肉蔬菜沙拉', '[\"午餐\", \"晚餐\"]', '[\"平静\"]', '[\"工作\", \"运动后\"]', '[\"减脂\", \"高蛋白\", \"低油\", \"控碳水\"]', '[\"轻食\", \"西餐\"]', '[\"咸鲜\"]', '[\"快速\", \"一人食\", \"适合备餐\"]', NOW(), NOW());
INSERT INTO `meal_item` VALUES (14, 'PUBLIC', NULL, '鸡胸藜麦轻食碗', '[\"午餐\", \"晚餐\"]', '[\"平静\", \"想放松\"]', '[\"运动后\", \"工作\"]', '[\"增肌\", \"高蛋白\", \"减脂\", \"低油\"]', '[\"轻食\"]', '[\"咸鲜\", \"鲜香\"]', '[\"快速\", \"一人食\", \"适合备餐\"]', NOW(), NOW());
INSERT INTO `meal_item` VALUES (15, 'PUBLIC', NULL, '照烧鸡腿饭', '[\"午餐\", \"晚餐\"]', '[\"开心\", \"疲惫\"]', '[\"工作\", \"校园\", \"加班\"]', '[\"高蛋白\", \"补能\", \"均衡\"]', '[\"日料\", \"快餐\"]', '[\"甜\", \"酱香\"]', '[\"快速\", \"外带方便\", \"少排队\"]', NOW(), NOW());
INSERT INTO `meal_item` VALUES (16, 'PUBLIC', NULL, '黑椒牛柳意面', '[\"午餐\", \"晚餐\"]', '[\"想奖励自己\", \"开心\"]', '[\"聚餐\", \"周末\"]', '[\"高蛋白\", \"补能\"]', '[\"西餐\", \"粉面\"]', '[\"咸鲜\", \"酱香\"]', '[\"堂食舒服\", \"慢享\"]', NOW(), NOW());
INSERT INTO `meal_item` VALUES (17, 'PUBLIC', NULL, '香煎鳕鱼配时蔬', '[\"晚餐\"]', '[\"平静\", \"想放松\"]', '[\"家里\", \"独处\"]', '[\"清淡\", \"高蛋白\", \"低油\", \"低盐\"]', '[\"西餐\", \"海鲜\"]', '[\"咸鲜\", \"鲜香\"]', '[\"慢享\", \"一人食\"]', NOW(), NOW());
INSERT INTO `meal_item` VALUES (18, 'PUBLIC', NULL, '番茄牛肉烩饭', '[\"午餐\", \"晚餐\"]', '[\"疲惫\", \"低落\"]', '[\"工作\", \"家里\"]', '[\"补能\", \"高蛋白\", \"均衡\"]', '[\"西餐\", \"家常\"]', '[\"番茄味\", \"鲜香\"]', '[\"快速\", \"一人食\"]', NOW(), NOW());
INSERT INTO `meal_item` VALUES (19, 'PUBLIC', NULL, '日式三文鱼饭团', '[\"早餐\", \"午餐\", \"加餐\"]', '[\"平静\"]', '[\"通勤\", \"旅行\", \"工作\"]', '[\"高蛋白\", \"均衡\", \"补能\"]', '[\"日料\", \"海鲜\"]', '[\"咸鲜\", \"鲜香\"]', '[\"外带方便\", \"适合边走边吃\", \"少餐具\"]', NOW(), NOW());
INSERT INTO `meal_item` VALUES (20, 'PUBLIC', NULL, '寿喜烧牛肉饭', '[\"午餐\", \"晚餐\"]', '[\"想奖励自己\", \"开心\"]', '[\"周末\", \"聚餐\"]', '[\"高蛋白\", \"补能\"]', '[\"日料\"]', '[\"甜\", \"咸鲜\"]', '[\"堂食舒服\", \"慢享\"]', NOW(), NOW());
INSERT INTO `meal_item` VALUES (21, 'PUBLIC', NULL, '韩式石锅拌饭', '[\"午餐\", \"晚餐\"]', '[\"开心\", \"想奖励自己\"]', '[\"聚餐\", \"工作\"]', '[\"均衡\", \"补能\"]', '[\"韩餐\"]', '[\"微辣\", \"酱香\"]', '[\"堂食舒服\", \"多人共享\"]', NOW(), NOW());
INSERT INTO `meal_item` VALUES (22, 'PUBLIC', NULL, '越南鸡肉米粉', '[\"午餐\", \"晚餐\"]', '[\"疲惫\", \"没胃口\"]', '[\"工作\", \"旅行\"]', '[\"清淡\", \"高蛋白\", \"易消化\"]', '[\"东南亚菜\", \"粉面\"]', '[\"咸鲜\", \"鲜香\"]', '[\"快速\", \"少餐具\"]', NOW(), NOW());
INSERT INTO `meal_item` VALUES (23, 'PUBLIC', NULL, '泰式咖喱鸡饭', '[\"午餐\", \"晚餐\"]', '[\"开心\", \"想奖励自己\"]', '[\"工作\", \"聚餐\"]', '[\"高蛋白\", \"补能\"]', '[\"东南亚菜\"]', '[\"咖喱味\", \"微辣\"]', '[\"快速\", \"外带方便\"]', NOW(), NOW());
INSERT INTO `meal_item` VALUES (24, 'PUBLIC', NULL, '广式白切鸡饭', '[\"午餐\", \"晚餐\"]', '[\"平静\", \"疲惫\"]', '[\"工作\", \"家里\"]', '[\"高蛋白\", \"清淡\", \"低油\"]', '[\"粤菜\", \"快餐\"]', '[\"咸鲜\", \"鲜香\"]', '[\"快速\", \"外带方便\"]', NOW(), NOW());
INSERT INTO `meal_item` VALUES (25, 'PUBLIC', NULL, '清蒸鲈鱼套餐', '[\"午餐\", \"晚餐\"]', '[\"平静\", \"没胃口\"]', '[\"家里\", \"聚餐\"]', '[\"清淡\", \"高蛋白\", \"低油\", \"低盐\"]', '[\"江浙菜\", \"海鲜\"]', '[\"咸鲜\", \"鲜香\"]', '[\"堂食舒服\", \"多人共享\"]', NOW(), NOW());
INSERT INTO `meal_item` VALUES (26, 'PUBLIC', NULL, '西兰花虾仁', '[\"午餐\", \"晚餐\"]', '[\"平静\"]', '[\"家里\", \"运动后\"]', '[\"减脂\", \"高蛋白\", \"低油\", \"低盐\"]', '[\"海鲜\", \"家常\"]', '[\"蒜香\", \"鲜香\"]', '[\"快速\", \"适合备餐\"]', NOW(), NOW());
INSERT INTO `meal_item` VALUES (27, 'PUBLIC', NULL, '芹菜炒牛肉', '[\"午餐\", \"晚餐\"]', '[\"疲惫\"]', '[\"家里\", \"工作\"]', '[\"高蛋白\", \"补能\", \"均衡\"]', '[\"湘菜\", \"家常\"]', '[\"微辣\", \"鲜香\"]', '[\"快速\", \"适合备餐\"]', NOW(), NOW());
INSERT INTO `meal_item` VALUES (28, 'PUBLIC', NULL, '冬瓜排骨汤', '[\"午餐\", \"晚餐\", \"三餐\"]', '[\"疲惫\", \"没胃口\"]', '[\"家里\"]', '[\"清淡\", \"降火\", \"易消化\"]', '[\"粤菜\", \"粥汤\"]', '[\"咸鲜\"]', '[\"慢享\", \"多人共享\"]', NOW(), NOW());
INSERT INTO `meal_item` VALUES (29, 'PUBLIC', NULL, '山药鸡汤', '[\"午餐\", \"晚餐\", \"三餐\"]', '[\"低落\", \"疲惫\"]', '[\"家里\", \"周末\"]', '[\"养胃\", \"暖胃\", \"高蛋白\", \"易消化\"]', '[\"家常\", \"粥汤\"]', '[\"鲜香\", \"咸鲜\"]', '[\"慢享\", \"多人共享\"]', NOW(), NOW());
INSERT INTO `meal_item` VALUES (30, 'PUBLIC', NULL, '菌菇豆腐煲', '[\"午餐\", \"晚餐\"]', '[\"平静\", \"想放松\"]', '[\"家里\", \"独处\"]', '[\"清淡\", \"均衡\", \"低油\", \"降火\"]', '[\"素食\", \"家常\"]', '[\"鲜香\", \"咸鲜\"]', '[\"快速\", \"一人食\"]', NOW(), NOW());
INSERT INTO `meal_item` VALUES (31, 'PUBLIC', NULL, '蒜蓉西兰花', '[\"午餐\", \"晚餐\"]', '[\"平静\"]', '[\"家里\", \"工作\"]', '[\"减脂\", \"低油\", \"低盐\", \"控碳水\"]', '[\"素食\", \"家常\"]', '[\"蒜香\", \"咸鲜\"]', '[\"快速\", \"适合备餐\"]', NOW(), NOW());
INSERT INTO `meal_item` VALUES (32, 'PUBLIC', NULL, '麻婆豆腐', '[\"午餐\", \"晚餐\"]', '[\"烦躁\", \"想奖励自己\"]', '[\"工作\", \"聚餐\"]', '[\"补能\", \"均衡\"]', '[\"川菜\", \"家常\"]', '[\"辣\", \"麻辣\"]', '[\"快速\", \"外带方便\"]', NOW(), NOW());
INSERT INTO `meal_item` VALUES (33, 'PUBLIC', NULL, '宫保鸡丁', '[\"午餐\", \"晚餐\"]', '[\"开心\", \"想奖励自己\"]', '[\"聚餐\", \"工作\"]', '[\"高蛋白\", \"补能\"]', '[\"川菜\", \"快餐\"]', '[\"中辣\", \"酸甜\"]', '[\"快速\", \"外带方便\"]', NOW(), NOW());
INSERT INTO `meal_item` VALUES (34, 'PUBLIC', NULL, '剁椒鱼头', '[\"午餐\", \"晚餐\"]', '[\"开心\", \"想奖励自己\"]', '[\"周末\", \"聚餐\"]', '[\"高蛋白\", \"补能\"]', '[\"湘菜\", \"海鲜\"]', '[\"中辣\", \"鲜香\"]', '[\"慢享\", \"多人共享\"]', NOW(), NOW());
INSERT INTO `meal_item` VALUES (35, 'PUBLIC', NULL, '东北乱炖', '[\"午餐\", \"晚餐\"]', '[\"疲惫\", \"低落\"]', '[\"家里\", \"周末\"]', '[\"补能\", \"均衡\", \"暖胃\"]', '[\"东北菜\", \"家常\"]', '[\"咸鲜\", \"酱香\"]', '[\"多人共享\", \"慢享\"]', NOW(), NOW());
INSERT INTO `meal_item` VALUES (36, 'PUBLIC', NULL, '鲁味酱牛肉', '[\"午餐\", \"晚餐\", \"加餐\"]', '[\"平静\", \"想奖励自己\"]', '[\"家里\", \"聚餐\"]', '[\"高蛋白\", \"增肌\", \"补能\"]', '[\"鲁菜\"]', '[\"酱香\", \"咸鲜\"]', '[\"适合备餐\", \"多人共享\"]', NOW(), NOW());
INSERT INTO `meal_item` VALUES (37, 'PUBLIC', NULL, '闽南海鲜粥', '[\"早餐\", \"午餐\", \"晚餐\", \"夜宵\"]', '[\"疲惫\", \"没胃口\"]', '[\"家里\", \"旅行\"]', '[\"养胃\", \"清淡\", \"易消化\", \"暖胃\"]', '[\"闽南菜\", \"海鲜\", \"粥汤\"]', '[\"鲜香\", \"咸鲜\"]', '[\"少餐具\", \"慢享\"]', NOW(), NOW());
INSERT INTO `meal_item` VALUES (38, 'PUBLIC', NULL, '云南过桥米线', '[\"午餐\", \"晚餐\"]', '[\"疲惫\", \"想放松\"]', '[\"旅行\", \"工作\"]', '[\"补能\", \"暖胃\"]', '[\"云南菜\", \"粉面\"]', '[\"鲜香\", \"微辣\"]', '[\"快速\", \"堂食舒服\"]', NOW(), NOW());
INSERT INTO `meal_item` VALUES (39, 'PUBLIC', NULL, '新疆鸡肉手抓饭', '[\"午餐\", \"晚餐\"]', '[\"疲惫\", \"想奖励自己\"]', '[\"旅行\", \"聚餐\"]', '[\"高蛋白\", \"补能\", \"增肌\"]', '[\"新疆菜\"]', '[\"油香\", \"鲜香\"]', '[\"多人共享\", \"堂食舒服\"]', NOW(), NOW());
INSERT INTO `meal_item` VALUES (40, 'PUBLIC', NULL, '素食杂粮饭', '[\"午餐\", \"晚餐\"]', '[\"平静\", \"想放松\"]', '[\"工作\", \"独处\"]', '[\"减脂\", \"均衡\", \"低油\", \"低糖\"]', '[\"素食\", \"轻食\"]', '[\"咸鲜\"]', '[\"快速\", \"一人食\", \"适合备餐\"]', NOW(), NOW());
INSERT INTO `meal_item` VALUES (41, 'PUBLIC', NULL, '低糖南瓜燕麦饼', '[\"早餐\", \"下午茶\", \"加餐\"]', '[\"开心\", \"想放松\"]', '[\"家里\", \"工作\"]', '[\"低糖\", \"减脂\", \"均衡\"]', '[\"甜品\", \"轻食\"]', '[\"甜\", \"奶香\"]', '[\"外带方便\", \"适合备餐\"]', NOW(), NOW());
INSERT INTO `meal_item` VALUES (42, 'PUBLIC', NULL, '牛油果鸡蛋卷', '[\"早餐\", \"早午餐\", \"午餐\"]', '[\"平静\"]', '[\"工作\", \"通勤\"]', '[\"高蛋白\", \"减脂\", \"控碳水\", \"均衡\"]', '[\"西餐\", \"轻食\"]', '[\"咸鲜\", \"奶香\"]', '[\"快速\", \"外带方便\", \"一人食\"]', NOW(), NOW());
INSERT INTO `meal_item` VALUES (43, 'PUBLIC', NULL, '金枪鱼全麦卷', '[\"早餐\", \"午餐\", \"加餐\"]', '[\"疲惫\"]', '[\"工作\", \"运动后\", \"通勤\"]', '[\"高蛋白\", \"减脂\", \"低油\", \"控碳水\"]', '[\"西餐\", \"海鲜\", \"轻食\"]', '[\"咸鲜\", \"鲜香\"]', '[\"外带方便\", \"适合边走边吃\", \"适合备餐\"]', NOW(), NOW());
INSERT INTO `meal_item` VALUES (44, 'PUBLIC', NULL, '牛肉荞麦面', '[\"午餐\", \"晚餐\"]', '[\"疲惫\", \"没胃口\"]', '[\"工作\", \"运动后\"]', '[\"高蛋白\", \"补能\", \"低油\"]', '[\"粉面\", \"日料\"]', '[\"咸鲜\", \"酱香\"]', '[\"快速\", \"少餐具\"]', NOW(), NOW());
INSERT INTO `meal_item` VALUES (45, 'PUBLIC', NULL, '番茄虾仁意面', '[\"午餐\", \"晚餐\"]', '[\"开心\", \"想放松\"]', '[\"工作\", \"独处\"]', '[\"清淡\", \"高蛋白\", \"低油\"]', '[\"西餐\", \"海鲜\", \"粉面\"]', '[\"番茄味\", \"鲜香\"]', '[\"快速\", \"一人食\"]', NOW(), NOW());
INSERT INTO `meal_item` VALUES (46, 'PUBLIC', NULL, '奶香蘑菇鸡肉意面', '[\"午餐\", \"晚餐\"]', '[\"低落\", \"想奖励自己\"]', '[\"周末\", \"聚餐\"]', '[\"高蛋白\", \"补能\"]', '[\"西餐\", \"粉面\"]', '[\"奶香\", \"鲜香\"]', '[\"堂食舒服\", \"慢享\"]', NOW(), NOW());
INSERT INTO `meal_item` VALUES (47, 'PUBLIC', NULL, '芝士蔬菜烘蛋', '[\"早餐\", \"早午餐\", \"晚餐\"]', '[\"开心\", \"想放松\"]', '[\"家里\", \"周末\"]', '[\"高蛋白\", \"均衡\", \"控碳水\"]', '[\"西餐\", \"轻食\"]', '[\"奶香\", \"咸鲜\"]', '[\"慢享\", \"一人食\"]', NOW(), NOW());
INSERT INTO `meal_item` VALUES (48, 'PUBLIC', NULL, '日式荞麦冷面', '[\"午餐\", \"晚餐\"]', '[\"没胃口\", \"平静\"]', '[\"工作\", \"独处\"]', '[\"减脂\", \"低油\", \"清淡\"]', '[\"日料\", \"粉面\"]', '[\"咸鲜\"]', '[\"快速\", \"少餐具\"]', NOW(), NOW());
INSERT INTO `meal_item` VALUES (49, 'PUBLIC', NULL, '韩式海带豆腐汤', '[\"早餐\", \"午餐\", \"晚餐\"]', '[\"疲惫\", \"没胃口\"]', '[\"家里\", \"工作\"]', '[\"清淡\", \"低油\", \"降火\", \"易消化\"]', '[\"韩餐\", \"粥汤\"]', '[\"咸鲜\", \"鲜香\"]', '[\"快速\", \"少餐具\"]', NOW(), NOW());
INSERT INTO `meal_item` VALUES (50, 'PUBLIC', NULL, '冬阴功海鲜汤', '[\"午餐\", \"晚餐\"]', '[\"烦躁\", \"想奖励自己\"]', '[\"聚餐\", \"旅行\"]', '[\"高蛋白\", \"补能\"]', '[\"东南亚菜\", \"海鲜\", \"粥汤\"]', '[\"酸甜\", \"辣\"]', '[\"堂食舒服\", \"多人共享\"]', NOW(), NOW());
INSERT INTO `meal_item` VALUES (51, 'PUBLIC', NULL, '清汤菌菇火锅', '[\"午餐\", \"晚餐\"]', '[\"想放松\", \"平静\"]', '[\"周末\", \"聚餐\"]', '[\"清淡\", \"降火\", \"均衡\", \"低油\"]', '[\"火锅\", \"素食\"]', '[\"鲜香\", \"咸鲜\"]', '[\"慢享\", \"多人共享\"]', NOW(), NOW());
INSERT INTO `meal_item` VALUES (52, 'PUBLIC', NULL, '番茄牛肉火锅', '[\"午餐\", \"晚餐\"]', '[\"开心\", \"想奖励自己\"]', '[\"周末\", \"聚餐\"]', '[\"高蛋白\", \"补能\", \"均衡\"]', '[\"火锅\"]', '[\"番茄味\", \"鲜香\"]', '[\"慢享\", \"多人共享\"]', NOW(), NOW());
INSERT INTO `meal_item` VALUES (53, 'PUBLIC', NULL, '麻辣烤鸡翅', '[\"晚餐\", \"夜宵\"]', '[\"开心\", \"想奖励自己\"]', '[\"周末\", \"聚餐\"]', '[\"高蛋白\", \"补能\"]', '[\"烧烤\"]', '[\"麻辣\", \"烟火气\"]', '[\"多人共享\", \"慢享\"]', NOW(), NOW());
INSERT INTO `meal_item` VALUES (54, 'PUBLIC', NULL, '蒜香烤生蚝', '[\"晚餐\", \"夜宵\"]', '[\"开心\", \"想奖励自己\"]', '[\"聚餐\", \"旅行\"]', '[\"高蛋白\", \"补能\"]', '[\"烧烤\", \"海鲜\"]', '[\"蒜香\", \"鲜香\", \"烟火气\"]', '[\"多人共享\", \"堂食舒服\"]', NOW(), NOW());
INSERT INTO `meal_item` VALUES (55, 'PUBLIC', NULL, '孜然烤羊肉串', '[\"晚餐\", \"夜宵\"]', '[\"开心\", \"压力大\"]', '[\"周末\", \"聚餐\"]', '[\"高蛋白\", \"补能\", \"增肌\"]', '[\"烧烤\", \"新疆菜\"]', '[\"油香\", \"烟火气\", \"中辣\"]', '[\"多人共享\", \"适合边走边吃\"]', NOW(), NOW());
INSERT INTO `meal_item` VALUES (56, 'PUBLIC', NULL, '炭烤玉米', '[\"下午茶\", \"夜宵\", \"加餐\"]', '[\"开心\", \"想放松\"]', '[\"周末\", \"旅行\"]', '[\"补能\", \"均衡\"]', '[\"烧烤\", \"素食\", \"小吃\"]', '[\"甜\", \"烟火气\"]', '[\"适合边走边吃\", \"少餐具\"]', NOW(), NOW());
INSERT INTO `meal_item` VALUES (57, 'PUBLIC', NULL, '低糖红豆酸奶杯', '[\"下午茶\", \"加餐\"]', '[\"开心\", \"想奖励自己\"]', '[\"工作\", \"独处\"]', '[\"低糖\", \"减脂\", \"均衡\"]', '[\"甜品\", \"轻食\"]', '[\"酸甜\", \"奶香\"]', '[\"快速\", \"外带方便\", \"一人食\"]', NOW(), NOW());
INSERT INTO `meal_item` VALUES (58, 'PUBLIC', NULL, '水果燕麦杯', '[\"早餐\", \"下午茶\", \"加餐\"]', '[\"开心\", \"平静\"]', '[\"工作\", \"通勤\", \"旅行\"]', '[\"低糖\", \"减脂\", \"均衡\"]', '[\"甜品\", \"轻食\"]', '[\"甜\", \"酸甜\"]', '[\"快速\", \"外带方便\", \"适合边走边吃\"]', NOW(), NOW());
INSERT INTO `meal_item` VALUES (59, 'PUBLIC', NULL, '银耳雪梨羹', '[\"下午茶\", \"夜宵\", \"加餐\"]', '[\"没胃口\", \"想放松\", \"焦虑\"]', '[\"家里\", \"独处\"]', '[\"降火\", \"清淡\", \"低糖\", \"易消化\"]', '[\"甜品\", \"粥汤\"]', '[\"甜\"]', '[\"少餐具\", \"慢享\"]', NOW(), NOW());
INSERT INTO `meal_item` VALUES (60, 'PUBLIC', NULL, '红枣山药糕', '[\"早餐\", \"下午茶\", \"加餐\"]', '[\"疲惫\", \"低落\"]', '[\"家里\", \"工作\"]', '[\"养胃\", \"补能\", \"易消化\"]', '[\"甜品\", \"小吃\"]', '[\"甜\"]', '[\"外带方便\", \"适合备餐\"]', NOW(), NOW());
INSERT INTO `meal_item` VALUES (61, 'PUBLIC', NULL, '香蕉花生能量棒', '[\"早餐\", \"加餐\"]', '[\"疲惫\", \"压力大\"]', '[\"运动后\", \"通勤\", \"工作\"]', '[\"补能\", \"增肌\", \"高蛋白\"]', '[\"甜品\", \"轻食\"]', '[\"甜\", \"油香\"]', '[\"外带方便\", \"适合边走边吃\", \"适合备餐\"]', NOW(), NOW());

-- ----------------------------
-- Table structure for recommend_feedback
-- ----------------------------
DROP TABLE IF EXISTS `recommend_feedback`;
CREATE TABLE `recommend_feedback`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint NOT NULL,
  `session_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `item_id` bigint NULL DEFAULT NULL,
  `action` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `rating` int NULL DEFAULT NULL,
  `reason` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `created_at` datetime NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_feedback_user`(`user_id` ASC, `created_at` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of recommend_feedback
-- ----------------------------

SET FOREIGN_KEY_CHECKS = 1;
