package com.otsira.service.impl;

import com.otsira.entity.Order;
import com.otsira.mapper.OrderMapper;
import com.otsira.service.ReportService;
import com.otsira.vo.TurnoverReportVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;

/**
 * @program: sun-take-out
 * @author: HANYIIK
 * @description: 数据统计相关功能的 service 层接口实现类
 * @create: 2024/12/02 18:01
 */
@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
@Service
@Slf4j
public class DefaultReportService implements ReportService {
    private OrderMapper orderMapper;

    @Autowired
    public void setOrderMapper(OrderMapper orderMapper) {
        this.orderMapper = orderMapper;
    }

    @Override
    public TurnoverReportVO getTurnoverStatistics(LocalDate begin, LocalDate end) {
        // 1.封装日期列表
        ArrayList<LocalDate> localDates = new ArrayList<>();
        localDates.add(begin);
        while (begin.isBefore(end)) {
            begin = begin.plusDays(1);
            localDates.add(begin);
        }

        // 2.封装营业额列表
        ArrayList<Double> turnoverList = new ArrayList<>();
        Integer status = Order.COMPLETED;
        for (LocalDate localDate : localDates) {
            LocalDateTime beginTime = LocalDateTime.of(localDate, LocalTime.MIN);
            LocalDateTime endTime = LocalDateTime.of(localDate, LocalTime.MAX);
            Double turnover = orderMapper.sumTurnoverByDate(beginTime, endTime, status);
            turnover = turnover == null ? 0 : turnover;
            turnoverList.add(turnover);
        }

        return TurnoverReportVO.builder()
                .dateList(StringUtils.join(localDates, ","))
                .turnoverList(StringUtils.join(turnoverList, ","))
                .build();
    }
}
