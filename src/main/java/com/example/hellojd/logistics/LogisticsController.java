package com.example.hellojd.logistics;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;

@RequestMapping
@RestController
public class LogisticsController {
//    @Autowired
//    private StatefulRediSearchConnection<String, String> connection;
//    @Autowired
//    private LogisticsService logisticsService;
//    @Autowired
//    private LogisticsTimerService logisticsTimerService;
    @Autowired
    private LogisticsDataset logisticsDataset;
    @GetMapping(path = "{src}/{dst}/{start}")
    String myTest(@PathVariable("src") String src,
                  @PathVariable("dst") String dst,
                  @PathVariable("start") String start) throws ParseException {

//        long s = System.currentTimeMillis();
//        for(int i = 0; i < 100000; i += 1) {
//            logisticsDataset.getArrivalTime(src,dst,Long.valueOf(start));
//        }
//        System.out.println(System.currentTimeMillis() - s);

        return
//                "HELLO WORLD<br>"
//                + src + "\t"
//                + dst + "\t"
//                + start + "\t"
//                + "<br> database" + logisticsService.getArrivalTime(src, dst, Long.valueOf(start));
                logisticsDataset.getArrivalTime(src,dst,Long.valueOf(start));
//                + logisticsService.getArrivalTime(src,dst,Long.valueOf(start));

    }
}
