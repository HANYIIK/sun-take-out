package com.otsira.service;

import com.otsira.vo.TurnoverReportVO;

import java.time.LocalDate;

/**
 * @program: sun-take-out
 * @author: HANYIIK
 * @description: 数据统计相关功能的 service 层接口
 * @create: 2024/12/02 17:59
 */
public interface ReportService {
    TurnoverReportVO getTurnoverStatistics(LocalDate begin, LocalDate end);
}
