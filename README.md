## 运行方式

```bash
# 要求：JDK 17 + Maven 3.6+
mvn spring-boot:run
# 或打包后运行
mvn package -DskipTests
java -jar target/smart-wait-0.0.1-SNAPSHOT.jar
```

- 默认端口 `8099`（见 `application.yml`，若冲突改 `server.port`）
- 数据库：H2 内嵌文件库 `./data/smartwait.mv.db`，**零安装**，数据自动持久化
- H2 控制台：`http://localhost:8099/h2-console`（JDBC URL：`jdbc:h2:file:./data/smartwait;MODE=MySQL`，用户 `sa`，空密码）

##  Postman 接口测试集合

### 一、认证模块（Auth）

#### 1. 管理员登录
| 项目 | 值 |
|---|---|
| **Method** | `POST` |
| **URL** | `/api/auth/login` |
| **Headers** | `Content-Type: application/json` |
| **Body** | `{"role":"ADMIN","username":"admin","password":"admin123"}` |

#### 2. 医生登录（张医生-内科）
| 项目 | 值 |
|---|---|
| **Method** | `POST` |
| **URL** | `/api/auth/login` |
| **Headers** | `Content-Type: application/json` |
| **Body** | `{"role":"DOCTOR","username":"zhang","password":"123"}` |

#### 3. 医生登录（李医生-外科）
| 项目 | 值 |
|---|---|
| **Method** | `POST` |
| **URL** | `/api/auth/login` |
| **Headers** | `Content-Type: application/json` |
| **Body** | `{"role":"DOCTOR","username":"li","password":"123"}` |

#### 4. 患者登录（张三）
| 项目 | 值 |
|---|---|
| **Method** | `POST` |
| **URL** | `/api/auth/login` |
| **Headers** | `Content-Type: application/json` |
| **Body** | `{"role":"PATIENT","username":"zhang3","password":"123"}` |

#### 5. 注销登录
| 项目 | 值 |
|---|---|
| **Method** | `POST` |
| **URL** | `/api/auth/logout` |
| **Headers** | `Authorization: Bearer <token>` |

---

### 二、科室管理（Dept）— 需 ADMIN token

#### 6. 查询所有科室
| 项目 | 值 |
|---|---|
| **Method** | `GET` |
| **URL** | `/api/depts` |
| **Headers** | `Authorization: Bearer <admin_token>` |

#### 7. 查询科室详情
| 项目 | 值 |
|---|---|
| **Method** | `GET` |
| **URL** | `/api/depts/1` |
| **Headers** | `Authorization: Bearer <admin_token>` |

#### 8. 新增科室
| 项目 | 值 |
|---|---|
| **Method** | `POST` |
| **URL** | `/api/depts` |
| **Headers** | `Authorization: Bearer <admin_token>`, `Content-Type: application/json` |
| **Body** | `{"name":"皮肤科","description":"皮肤疾病门诊"}` |

#### 9. 修改科室
| 项目 | 值 |
|---|---|
| **Method** | `PUT` |
| **URL** | `/api/depts/1` |
| **Headers** | `Authorization: Bearer <admin_token>`, `Content-Type: application/json` |
| **Body** | `{"name":"内科（更新）","description":"常见内科疾病门诊（更新）"}` |

#### 10. 删除科室
| 项目 | 值 |
|---|---|
| **Method** | `DELETE` |
| **URL** | `/api/depts/4` |
| **Headers** | `Authorization: Bearer <admin_token>` |

---

### 三、医生管理（Doctor）— 需 ADMIN token

#### 11. 查询所有医生
| 项目 | 值 |
|---|---|
| **Method** | `GET` |
| **URL** | `/api/doctors` |
| **Headers** | `Authorization: Bearer <admin_token>` |

#### 12. 按科室查询医生
| 项目 | 值 |
|---|---|
| **Method** | `GET` |
| **URL** | `/api/doctors?deptId=1` |
| **Headers** | `Authorization: Bearer <admin_token>` |

#### 13. 新增医生
| 项目 | 值 |
|---|---|
| **Method** | `POST` |
| **URL** | `/api/doctors` |
| **Headers** | `Authorization: Bearer <admin_token>`, `Content-Type: application/json` |
| **Body** | `{"name":"赵医生","title":"住院医师","deptId":"1","username":"zhao","password":"123"}` |

#### 14. 修改医生
| 项目 | 值 |
|---|---|
| **Method** | `PUT` |
| **URL** | `/api/doctors/1` |
| **Headers** | `Authorization: Bearer <admin_token>`, `Content-Type: application/json` |
| **Body** | `{"name":"张医生（更新）","title":"主任医师","deptId":"1"}` |

#### 15. 删除医生
| 项目 | 值 |
|---|---|
| **Method** | `DELETE` |
| **URL** | `/api/doctors/4` |
| **Headers** | `Authorization: Bearer <admin_token>` |

---

### 四、号源排班（Schedule）— 需 ADMIN token

#### 16. 查询排班列表
| 项目 | 值 |
|---|---|
| **Method** | `GET` |
| **URL** | `/api/schedules` |
| **Headers** | `Authorization: Bearer <admin_token>` |

#### 17. 新增排班
| 项目 | 值 |
|---|---|
| **Method** | `POST` |
| **URL** | `/api/schedules` |
| **Headers** | `Authorization: Bearer <admin_token>`, `Content-Type: application/json` |
| **Body** | `{"doctorId":"1","workDate":"2026-10-20","slotCount":"15"}` |

#### 18. 修改号源数
| 项目 | 值 |
|---|---|
| **Method** | `PUT` |
| **URL** | `/api/schedules/1` |
| **Headers** | `Authorization: Bearer <admin_token>`, `Content-Type: application/json` |
| **Body** | `{"slotCount":"20"}` |

#### 19. 删除排班
| 项目 | 值 |
|---|---|
| **Method** | `DELETE` |
| **URL** | `/api/schedules/1` |
| **Headers** | `Authorization: Bearer <admin_token>` |

---

### 五、签到 / 取消签到（Checkin）

#### 20. 患者签到
| 项目 | 值 |
|---|---|
| **Method** | `POST` |
| **URL** | `/api/checkin` |
| **Headers** | `Authorization: Bearer <patient_token>`, `Content-Type: application/json` |
| **Body** | `{"deptId":"1","doctorId":"1","patientName":"张三"}` |

#### 21. 取消签到
| 项目 | 值 |
|---|---|
| **Method** | `DELETE` |
| **URL** | `/api/checkin/Q-xxxxx`（替换为实际 queueNo） |
| **Headers** | `Authorization: Bearer <patient_token>` |

---

### 六、候诊查询（Waiting / Board）

#### 22. 查询排队位置
| 项目 | 值 |
|---|---|
| **Method** | `GET` |
| **URL** | `/api/waiting/Q-xxxxx`（替换为实际 queueNo） |
| **Headers** | `Authorization: Bearer <token>` |

#### 23. 候诊大屏数据
| 项目 | 值 |
|---|---|
| **Method** | `GET` |
| **URL** | `/api/board/status?deptId=1` |
| **Headers** | `Authorization: Bearer <token>` |

---

### 七、排队叫号（Queue）— call 需 DOCTOR token

#### 24. 医生叫号
| 项目 | 值 |
|---|---|
| **Method** | `POST` |
| **URL** | `/api/queue/call` |
| **Headers** | `Authorization: Bearer <doctor_token>`, `Content-Type: application/json` |
| **Body** | `{"doctorId":"1","roomNo":"101"}` |

#### 25. 患者到场确认
| 项目 | 值 |
|---|---|
| **Method** | `POST` |
| **URL** | `/api/queue/seen` |
| **Headers** | `Authorization: Bearer <token>`, `Content-Type: application/json` |
| **Body** | `{"queueNo":"Q-xxxxx"}` |

#### 26. 标记过号
| 项目 | 值 |
|---|---|
| **Method** | `POST` |
| **URL** | `/api/queue/missed` |
| **Headers** | `Authorization: Bearer <token>`, `Content-Type: application/json` |
| **Body** | `{"queueNo":"Q-xxxxx"}` |

#### 27. 过号重排（仅一次）
| 项目 | 值 |
|---|---|
| **Method** | `POST` |
| **URL** | `/api/queue/reorder` |
| **Headers** | `Authorization: Bearer <token>`, `Content-Type: application/json` |
| **Body** | `{"queueNo":"Q-xxxxx"}` |

#### 28. 放弃离场
| 项目 | 值 |
|---|---|
| **Method** | `POST` |
| **URL** | `/api/queue/leave` |
| **Headers** | `Authorization: Bearer <token>`, `Content-Type: application/json` |
| **Body** | `{"queueNo":"Q-xxxxx"}` |

---

### 八、就诊流程（Visit）— start 需 DOCTOR token

#### 29. 开始接诊
| 项目 | 值 |
|---|---|
| **Method** | `POST` |
| **URL** | `/api/visit/start` |
| **Headers** | `Authorization: Bearer <doctor_token>`, `Content-Type: application/json` |
| **Body** | `{"queueNo":"Q-xxxxx","doctorId":"1"}` |

#### 30. 保存诊断记录
| 项目 | 值 |
|---|---|
| **Method** | `POST` |
| **URL** | `/api/visit/1/diagnosis` |
| **Headers** | `Authorization: Bearer <token>`, `Content-Type: application/json` |
| **Body** | `{"diagnosis":"上呼吸道感染，建议多休息多饮水"}` |

#### 31. 开医嘱单
| 项目 | 值 |
|---|---|
| **Method** | `POST` |
| **URL** | `/api/visit/1/prescription` |
| **Headers** | `Authorization: Bearer <token>`, `Content-Type: application/json` |
| **Body** | `{"prescription":"阿莫西林胶囊 0.5g tid × 3天"}` |

#### 32. 结束接诊
| 项目 | 值 |
|---|---|
| **Method** | `POST` |
| **URL** | `/api/visit/end` |
| **Headers** | `Authorization: Bearer <token>`, `Content-Type: application/json` |
| **Body** | `{"visitId":"1"}` |

---

### 九、异常场景测试（权限校验）

#### 33. 越权：患者调用医生叫号
| 项目 | 值 |
|---|---|
| **Method** | `POST` |
| **URL** | `/api/queue/call` |
| **Headers** | `Authorization: Bearer <patient_token>`, `Content-Type: application/json` |
| **Body** | `{"doctorId":"1","roomNo":"101"}` |
| **预期** | 返回 `403`，提示"无权限：仅医生可执行该操作" |

#### 34. 越权：医生新增科室
| 项目 | 值 |
|---|---|
| **Method** | `POST` |
| **URL** | `/api/depts` |
| **Headers** | `Authorization: Bearer <doctor_token>`, `Content-Type: application/json` |
| **Body** | `{"name":"测试科室","description":"测试"}` |
| **预期** | 返回 `403`，提示"无权限：仅科室管理员可执行该操作" |

#### 35. 未认证访问受保护接口
| 项目 | 值 |
|---|---|
| **Method** | `GET` |
| **URL** | `/api/depts` |
| **Headers** | 无 Authorization |
| **预期** | 返回 `401` |

---

### 推荐测试流程（端到端）

按以下顺序执行可完整演示业务流程：

```
1. 管理员登录 → 获取 admin_token
2. 查询科室/医生 → 确认演示数据
3. 患者登录 → 获取 patient_token
4. 患者签到 → 获取 queueNo
5. 查询排队位置 → 确认 position
6. 医生登录 → 获取 doctor_token
7. 医生叫号 → 触发观察者通知
8. 候诊大屏查询 → 验证当前叫号
9. 患者到场确认 → seen
10. 医生开始接诊 → start
11. 填写诊断 → diagnosis
12. 开医嘱 → prescription
13. 结束接诊 → end
14. 过号重排测试 → missed → reorder → call
15. 取消签到测试 → checkin → cancel
```

---

## 接口清单（16 条，与《需求规格说明书》第五章一致）

| 接口 | 说明 | 用例 |
| ---- | ---- | ---- |
| POST /api/auth/login | 登录认证 | UC-10 |
| POST /api/checkin、DELETE /api/checkin/{queueNo} | 签到 / 取消签到 | UC-01 / UC-12 |
| GET /api/waiting/{queueNo} | 查询排队位置 | UC-03 |
| GET /api/board/status?deptId= | 候诊大屏（前端 5 秒轮询） | UC-03 |
| POST /api/queue/call、/seen、/missed、/reorder、/leave | 叫号 / 到场 / 过号 / 重排 / 离场 | UC-02 |
| POST /api/visit/start、/end、/{id}/diagnosis、/{id}/prescription | 接诊 / 诊断 / 医嘱 | UC-04~06 |
| GET/POST/PUT/DELETE /api/depts、/api/doctors、/api/schedules | 科室 / 医生 / 号源排班维护 | UC-07~09、UC-11 |

## 设计模式落地（指导书要求 ≥3 种）

| 模式 | 位置 | 说明 |
| ---- | ---- | ---- |
| 观察者模式 | pattern/observer | 叫号事件 EventPublisher 广播 → 大屏/短信/语音通知器；大屏缓存供轮询 |
| 状态模式 | pattern/state | QueueState 状态机：WAITING→CALLED→SEEN/MISSED→WAITING(仅一次)/LEFT，非法流转抛异常 |
| 工厂模式 | pattern/factory | NotificationFactory 按通知类型从容器挑选通知器，新增通知方式不改发布器 |

## 目录结构

```
src/main/java/com/hospital/smartwait/
├── SmartWaitApplication.java
├── common/     统一响应 Result、业务异常 BizException、全局异常处理
├── config/     Token 认证拦截器、Web 配置、演示数据初始化
├── util/       轻量内存 Token
├── model/      实体：科室/医生/排班/患者/排队/就诊记录（JPA）
├── repository/ JPA 数据访问
├── pattern/    设计模式（observer / state / factory）
├── service/    业务服务（认证/签到/叫号/就诊/管理）
└── controller/ 接口层（9 个 Controller，每个方法标注 // @UseCase UC-xx）
```

## 已通过验证

编译 BUILD SUCCESS（42 个源文件）；运行冒烟覆盖：登录（3 角色）、签到防重（BR-1）、排队查询、大屏轮询、叫号互斥（BR-6）、接诊状态流转、诊断医嘱、过号重排仅一次（BR-5）、越权拦截（403）。

