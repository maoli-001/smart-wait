package com.hospital.smartwait.controller;

import com.hospital.smartwait.common.Result;
import com.hospital.smartwait.model.QueueEntry;
import com.hospital.smartwait.pattern.observer.CallEvent;
import com.hospital.smartwait.repository.QueueEntryRepository;
import com.hospital.smartwait.service.QueueService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 候诊大屏接口（UC-03 查看候诊信息）
 *
 * <p>接口清单：
 *  GET /api/board/status?deptId=  候诊大屏当前叫号与队列（UC-03）
 *
 * <p>前端按默认 5 秒轮询本接口（需求文档 5.1）。
 * 当前叫号信息来自观察者模式中候诊大屏通知器缓存的最新叫号事件。
 */
@RestController
@RequestMapping("/api/board")
public class BoardController {

    private final QueueService queueService;
    private final QueueEntryRepository queueEntryRepository;

    public BoardController(QueueService queueService,
                           QueueEntryRepository queueEntryRepository) {
        this.queueService = queueService;
        this.queueEntryRepository = queueEntryRepository;
    }

    /** // @UseCase UC-03 候诊大屏数据 */
    @GetMapping("/status")
    public Result<Map<String, Object>> status(@RequestParam Long deptId) {
        CallEvent current = queueService.latestCall();
        List<QueueEntry> queue = queueEntryRepository
                .findByDeptIdAndStatusOrderByQueueNoAsc(deptId, QueueEntry.STATUS_WAITING);

        Map<String, Object> data = new HashMap<>();
        if (current != null && current.getDeptId().equals(deptId)) {
            Map<String, Object> currentMap = new HashMap<>();
            currentMap.put("queueNo", current.getQueueNo());
            currentMap.put("patientName", current.getPatientName());
            currentMap.put("roomNo", current.getRoomNo());
            data.put("current", currentMap);
        } else {
            data.put("current", null);
        }
        data.put("queueList", queue);
        return Result.ok(data);
    }
}
