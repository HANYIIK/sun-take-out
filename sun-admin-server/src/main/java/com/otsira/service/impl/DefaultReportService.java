package com.otsira.service.impl;

import com.otsira.client.UserClient;
import com.otsira.entity.Order;
import com.otsira.mapper.OrderMapper;
import com.otsira.service.ReportService;
import com.otsira.vo.TurnoverReportVO;
import com.otsira.vo.UserReportVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

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
    private UserClient userClient;

    @Autowired
    public void setOrderMapper(OrderMapper orderMapper) {
        this.orderMapper = orderMapper;
    }

    @Autowired
    public void setUserClient(@Qualifier("userClient") UserClient userClient) {
        this.userClient = userClient;
    }

    @Override
    public TurnoverReportVO getTurnoverStatistics(LocalDate begin, LocalDate end) {
        // 1.封装日期列表
        List<LocalDate> localDates = getDateList(begin, end);

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

    @Override
    public UserReportVO getUserStatistics(LocalDate begin, LocalDate end) {
        // 1.封装日期列表
        List<LocalDate> localDates = getDateList(begin, end);

        List<Integer> newUserList = new ArrayList<>();
        List<Integer> totalUserList = new ArrayList<>();

        // 2.封装新/总用户列表
        for (LocalDate localDate : localDates) {
            LocalDateTime beginTime = LocalDateTime.of(localDate, LocalTime.MIN);
            LocalDateTime endTime = LocalDateTime.of(localDate, LocalTime.MAX);
            // 使用 openFeign 请求用户端微服务查询新/总用户数
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String beginStr = beginTime.format(formatter);
            String endStr = endTime.format(formatter);
            // userClient 的 findNewAndTotalUserNum() 方法需要 String 类型的参数
            List<Integer> newAndTotalUserNum = userClient.findNewAndTotalUserNum(beginStr, endStr);
            // 第一个参数: 该日的新用户数
            // 第二个参数: 该日的总用户数
            newUserList.add(newAndTotalUserNum.get(0));
            totalUserList.add(newAndTotalUserNum.get(1));
        }

        return UserReportVO.builder()
                .dateList(StringUtils.join(localDates, ","))
                .newUserList(StringUtils.join(newUserList, ","))
                .totalUserList(StringUtils.join(totalUserList, ","))
                .build();
    }

    /**
     * 封装日期列表
     * @param begin 开始日期
     * @param end 结束日期
     * @return 日期列表
     */
    private List<LocalDate> getDateList(LocalDate begin, LocalDate end) {
        ArrayList<LocalDate> localDates = new ArrayList<>();
        localDates.add(begin);
        while (begin.isBefore(end)) {
            begin = begin.plusDays(1);
            localDates.add(begin);
        }
        return localDates;
    }
}
