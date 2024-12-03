package com.otsira.service;

import com.otsira.vo.OrderReportVO;
import com.otsira.vo.SalesTop10ReportVO;
import com.otsira.vo.TurnoverReportVO;
import com.otsira.vo.UserReportVO;

import java.time.LocalDate;

/**
 * @program: sun-take-out
 * @author: HANYIIK
 * @description: 数据统计相关功能的 service 层接口
 * @create: 2024/12/02 17:59
 */
public interface ReportService {
    TurnoverReportVO getTurnoverStatistics(LocalDate begin, LocalDate end);
    UserReportVO getUserStatistics(LocalDate begin, LocalDate end);
    OrderReportVO getOrdersStatistics(LocalDate begin, LocalDate end);
    SalesTop10ReportVO getTop10(LocalDate begin, LocalDate end);
}
