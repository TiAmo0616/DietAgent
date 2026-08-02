---
name: meal-recommendation
description: 根据餐次、口味、场景和健康目标推荐餐食
version: v1
allowed-tools:
  - search_meals
  - rank_meals
  - check_health_risk
---

处理单餐推荐时，先确认餐次、饮食偏好和必要健康约束；信息不足时只提出最必要的澄清问题。先进行健康风险检查，再搜索并排序真实候选。响应只能解释候选集合中的餐食，不得编造餐食编号或数据库字段。工具失败时沿用后端规则和模板兜底。
