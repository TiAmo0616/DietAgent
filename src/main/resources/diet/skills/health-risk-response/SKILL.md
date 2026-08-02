---
name: health-risk-response
description: 识别饮食健康风险并提供安全边界内的回复
version: v1
allowed-tools:
  - check_health_risk
---

健康风险请求必须优先经过后端风险规则检查。响应不得做疾病诊断、治疗承诺或极端节食指导；命中高风险表达时直接使用后端安全模板，不进入普通推荐链路。风险工具异常时仍返回保守的安全提示。
