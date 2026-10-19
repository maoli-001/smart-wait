package com.hospital.smartwait.controller;

import com.hospital.smartwait.common.BizException;
import com.hospital.smartwait.common.Result;
import com.hospital.smartwait.model.QueueEntry;
import com.hospital.smartwait.repository.QueueEntryRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 候诊信息接口（UC-03 查看候诊信息）
 *
 * <p>接口清单：
 *  GET /api/waiting/{queueNo}  查询排队位置与状态（UC-03）
 */
@RestController
@RequestMapping("/api/waiting")
public class WaitingController {

    private final QueueEntryRepository queueEntryRepository;

    public WaitingController(QueueEntryRepository queueEntryRepository) {
        this.queueEntryRepository = queueEntryRepository;
    }

    /** // @UseCase UC-03 查看候诊信息 */
    @GetMapping("/{queueNo}")
    public Result<Map<String, Object>> waiting(@PathVariable String queueNo) {
        QueueEntry entry = queueEntryRepository.findByQueueNo(queueNo)
                .orElseThrow(() -> new BizException(4007, "排队记录不存在：" + queueNo));
        // 排队位置 = 同医生队列中排在该患者前面的人数 + 1
        List<QueueEntry> ahead = queueEntryRepository
                .findByDoctorIdAndStatusOrderByQueueNoAsc(entry.getDoctorId(), QueueEntry.STATUS_WAITING)
                .stream()
                .filter(e -> e.getQueueNo().compareTo(queueNo) < 0)
                .toList();

        Map<String, Object> data = new HashMap<>();
        data.put("queueNo", entry.getQueueNo());
        data.put("status", entry.getStatus());
        data.put("position", ahead.size() + 1);
        return Result.ok(data);
    }
}
