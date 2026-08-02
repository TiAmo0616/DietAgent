---
name: meal-planning
description: 按餐次拆分用户需求并生成多餐饮食计划
version: v1
allowed-tools:
  - search_meals
  - rank_meals
  - check_health_risk
---

处理多餐计划时，先按餐次拆分需求，再为每个餐次查询并排序候选。计划中的每个餐食编号必须来自对应候选集合，健康风险检查优先于生成解释。缺少关键餐次信息时先澄清，工具异常时按餐次使用后端模板兜底。
