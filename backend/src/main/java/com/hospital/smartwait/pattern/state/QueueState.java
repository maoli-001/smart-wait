package com.hospital.smartwait.pattern.state;

import com.hospital.smartwait.common.BizException;

/**
 * 排队状态机（状态模式：State）
 *
 * <p>// @Pattern 状态模式（State）
 * // @UseCase UC-02 候诊排队叫号（含过号与重排）
 *
 * <p>排队记录的状态流转被封装在各状态常量中，非法流转直接抛出业务异常：
 *
 * <pre>
 *   WAITING（候诊） --call--> CALLED（叫号中）
 *   CALLED  --seen-->   SEEN（已就诊）
 *   CALLED  --missed--> MISSED（过号）
 *   MISSED  --reorder--> WAITING（重排，仅一次）
 *   MISSED  --leave-->   LEFT（离场）
 *   WAITING --leave-->   LEFT（取消签到）
 * </pre>
 */
public enum QueueState {

    /** 候诊中 */
    WAITING {
        @Override
        public QueueState call() {
            return CALLED;
        }

        @Override
        public QueueState leave() {
            return LEFT;
        }
    },

    /** 叫号中 */
    CALLED {
        @Override
        public QueueState seen() {
            return SEEN;
        }

        @Override
        public QueueState missed() {
            return MISSED;
        }
    },

    /** 过号 */
    MISSED {
        @Override
        public QueueState reorder() {
            return WAITING;
        }

        @Override
        public QueueState leave() {
            return LEFT;
        }
    },

    /** 已就诊（终态） */
    SEEN {
        // 终态，不允许再流转
    },

    /** 离场（终态） */
    LEFT {
        // 终态，不允许再流转
    };

    /** 叫号：WAITING → CALLED */
    public QueueState call() {
        throw illegal("叫号");
    }

    /** 到场就诊：CALLED → SEEN */
    public QueueState seen() {
        throw illegal("确认就诊");
    }

    /** 过号：CALLED → MISSED */
    public QueueState missed() {
        throw illegal("标记过号");
    }

    /** 重排：MISSED → WAITING */
    public QueueState reorder() {
        throw illegal("申请重排");
    }

    /** 离场/取消：WAITING/MISSED → LEFT */
    public QueueState leave() {
        throw illegal("取消/离场");
    }

    private BizException illegal(String action) {
        return new BizException(4001, "当前状态（" + name() + "）不允许执行" + action + "操作");
    }
}
