---
name: meal-adjustment
description: 根据用户对上一轮推荐的反馈调整餐食候选
version: v1
allowed-tools:
  - search_meals
  - rank_meals
  - check_health_risk
---

处理调整请求时，读取后端保存的上一轮候选和结构化槽位，明确识别用户要替换的约束。排除已推荐编号后重新搜索和排序，保持健康风险规则优先。只能返回本轮真实候选，工具失败时回退到已有推荐调整模板。
