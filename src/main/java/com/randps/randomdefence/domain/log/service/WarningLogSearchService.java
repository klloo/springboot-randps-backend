package com.randps.randomdefence.domain.log.service;

import com.randps.randomdefence.domain.log.domain.PointLog;
import com.randps.randomdefence.domain.log.domain.WarningLog;
import com.randps.randomdefence.domain.log.domain.WarningLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class WarningLogSearchService {

    private final WarningLogRepository warningLogRepository;

    /*
     * 서버의 모든 경고 로그를 조회한다.
     */
    @Transactional
    public List<WarningLog> findAllLog() {
        List<WarningLog> warningLogs =  warningLogRepository.findAll();
        if (warningLogs == null)
            warningLogs = new ArrayList<>();

        return warningLogs;
    }

    /*
     * 특정 유저의 모든 경고 로그를 조회한다.
     */
    @Transactional
    public List<WarningLog> findAllLogByBojHandle(String bojHandle) {
        List<WarningLog> warningLogs = warningLogRepository.findAllByBojHandle(bojHandle);
        if (warningLogs == null)
            warningLogs = new ArrayList<>();
        return warningLogs;
    }

    /*
     * 특정 유저의 모든 경고 로그를 페이지 단위로 조회한다.
     */
    @Transactional
    public List<WarningLog> findPagingLogByBojHandle(String bojHandle, Pageable pageable) {
        List<WarningLog> warningLogs = warningLogRepository.findAllByBojHandleOrderByCreatedDateDesc(bojHandle, pageable);
        if (warningLogs == null)
            warningLogs = new ArrayList<>();
        return warningLogs;
    }
}
