package com.example.hellojd.logistics;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.*;

public class LogisticsDataset {
    private final static Logger LOGGER = LoggerFactory.getLogger(LogisticsDataset.class);
    private final HashMap<String, byte[]> collect;
    private final HashMap<String, byte[]> route;
    private final HashMap<String, byte[]> dispatch;
    private final int startDay;
    private final int[] collectDDL;
    private final int[] collectDelay;
    private final String[] routeDelay;
    private final int[] dispathDelay;
    public String getArrivalTime(String src, String dst, Long startTime) throws ParseException {
        int a, b, c;
        a = getCollectionTime(src, dst, startTime);
        if (a < 0) return "-1";
        b = getRouterTime(src, dst, startTime);
        if (b < 0) return "-1";
        c = getDispatchTime(dst, startTime);
//        (a >=0 && b >= 0 && c >= 0)
        return (c >= 0)
                ? trans2TimeTuple(startTime, (long)a, (long)b, (long)c)
                : "-1" ;
    }

//    public String getMultiArrivalTime(String src, String dst, Long startTime) throws Exception{
//        // 三个线程的线程池，核心线程=最大线程，没有临时线程，阻塞队列无界
//        ExecutorService executor = Executors.newFixedThreadPool(3);
//
//        // 开启线程执行
//        // 注意，此处Future对象接收线程执行结果不会阻塞，只有future.get()时候才会阻塞（直到线程执行完返回结果）
//        Future future1 = executor.submit(new CollectCalculator(src, dst, startTime));
//
//        Future future2 = executor.submit(new RouteCalculator(src, dst, startTime));
//
//        Future future3 = executor.submit(new DispatchCalculator(src, dst, startTime));
////        此处用循环保证三个线程执行完毕，再去拼接三个结果
//        do{
//            ;
//        }while (!(future1.isDone() && future2.isDone() && future3.isDone()));
////        executor.awaitTermination(5, TimeUnit.SECONDS);
//        int a, b, c;
//        a = (int) future1.get();
//        b = (int) future2.get();
//        c = (int) future3.get();
//
//        return (a >=0 && b >= 0 && c >= 0)
//                ? trans2TimeTuple(startTime, (long)a, (long)b, (long)c)
//                : "-1" ;
//    }
//    public class CollectCalculator implements Callable<Integer> {
//        String src;
//        String dst;
//        Long startTime;
//        public CollectCalculator(String src, String dst, Long startTime) {
//            this.src = src;
//            this.dst = dst;
//            this.startTime = startTime;
//        }
//        @Override
//        public Integer call() throws Exception {
//            return getCollectionTime(src, dst, startTime);
//        }
//    }
//    public class RouteCalculator implements Callable<Integer> {
//        String src;
//        String dst;
//        Long startTime;
//        public RouteCalculator(String src, String dst, Long startTime) {
//            this.src = src;
//            this.dst = dst;
//            this.startTime = startTime;
//        }
//        @Override
//        public Integer call() throws Exception {
//            return getRouterTime(src, dst, startTime);
//        }
//    }
//    public class DispatchCalculator implements Callable<Integer> {
//        String src;
//        String dst;
//        Long startTime;
//        public DispatchCalculator(String src, String dst, Long startTime) {
//            this.src = src;
//            this.dst = dst;
//            this.startTime = startTime;
//        }
//        @Override
//        public Integer call() throws Exception {
//            return getDispatchTime(dst, startTime);
//        }
//    }
    public LogisticsDataset() {
        this.collect = new HashMap<>();
        this.route = new HashMap<>();
        this.dispatch = new HashMap<>();
        this.startDay = 18381;
//        this.collectDDL = new String[]{"16:00", "17:00", "17:30", "18:00", "19:00", "20:00"};
        this.collectDDL = new int[]{57600, 61200, 63000, 64800, 68400, 72000};
        this.collectDelay = new int[]{1,2,3,4,5};
        this.routeDelay = new String[]{"4D2200", "11D2359", "7D1800", "2D1700", "5D1500", "4D1500",
                "8D2200", "5D1800", "2D1500", "3D2200", "8D1500", "8D2359",
                "5D2200", "6D1700", "12D2359", "5D2359", "21D2200", "10D1500",
                "13D2359", "2D1800", "6D2359", "12D2200", "9D2359", "4D1700",
                "11D1500", "3D2359", "4D2359", "15D1500", "20D2200", "9D1500",
                "16D1500", "17D2200", "17D1500", "5D1700", "3D1700", "18D2200",
                "9D2200", "16D2200", "13D1500", "11D2200", "15D2200", "3D1800",
                "3D1500", "7D2200", "6D1800", "4D1800", "10D2359", "14D1500",
                "22D2200", "14D2200", "7D2359", "7D1700", "7D1500", "2D2200",
                "14D2359", "6D1500", "6D2200", "19D2200", "13D2200", "10D2200"};
        this.dispathDelay =new int[]{0,1,24};
        LOGGER.info("collect");
        LOGGER.info("route");
        LOGGER.info("dispatch");
//        LOGGER.info("collect", collect);
//        LOGGER.info("collect", collect);

        JSONParser parser = new JSONParser();
        try {
//            Object obj = parser.parse(new FileReader("src/main/resources/dataset/collect2bitmap.json"));
//            Object obj = parser.parse(new FileReader(LogisticsDataset.class.getResource("/dataset/collect2bitmap.json").getFile()));
            InputStream stream = getClass()
                    .getClassLoader()
                    .getResourceAsStream("dataset/collect2bitmap.json");
            BufferedReader br = new BufferedReader(new InputStreamReader(stream));
            Object obj = parser.parse(br);

            Map<String, JSONArray> jsonObject = (Map<String, JSONArray>) obj;
            for(Map.Entry<String, JSONArray> addr : jsonObject.entrySet()){
                int len = addr.getValue().size();
                byte[] oneAddressCollect = new byte[len];
                for (int i = 0; i < len; i += 1){
                    oneAddressCollect[i] = (byte)((long) addr.getValue().get(i));
                }
                collect.put(addr.getKey(), oneAddressCollect);
            }
//            System.out.println(collect.get("18:")[415]);
//            obj = parser.parse(new FileReader("src/main/resources/dataset/route2bitmap.json"));
//            obj = parser.parse(new FileReader(LogisticsDataset.class.getResource("/dataset/route2bitmap.json").getFile()));
            stream = getClass()
                    .getClassLoader()
                    .getResourceAsStream("dataset/route2bitmap.json");
            br = new BufferedReader(new InputStreamReader(stream));
            obj = parser.parse(br);
            jsonObject = (Map<String, JSONArray>) obj;
            for(Map.Entry<String, JSONArray> addr : jsonObject.entrySet()){
                int len = addr.getValue().size();
                byte[] oneAddressCollect = new byte[len];
                for (int i = 0; i < len; i += 1){
                    oneAddressCollect[i] = (byte)((long) addr.getValue().get(i));
                }
                route.put(addr.getKey(), oneAddressCollect);
            }
//            obj = parser.parse(new FileReader(LogisticsDataset.class.getResource("/dataset/dispatch2bitmap.json").getFile()));
            stream = getClass()
                    .getClassLoader()
                    .getResourceAsStream("dataset/dispatch2bitmap.json");
            br = new BufferedReader(new InputStreamReader(stream));
            obj = parser.parse(br);
            jsonObject = (Map<String, JSONArray>) obj;
            for(Map.Entry<String, JSONArray> addr : jsonObject.entrySet()){
                int len = addr.getValue().size();
                byte[] oneAddressCollect = new byte[len];
                for (int i = 0; i < len; i += 1){
                    oneAddressCollect[i] = (byte)((long) addr.getValue().get(i));
                }
                dispatch.put(addr.getKey(), oneAddressCollect);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private int getCollectionTime(String src, String dst, Long startTime){
        String[] srcA = src.split(":");
        String[] dstA = dst.split(":");
        dstA = Arrays.copyOfRange(dstA, Math.max(0, dstA.length-2), dstA.length);
        int nDay = (int) ((startTime + 8*3600)/86400 - startDay);
        int nSec = (int) (startTime + 8*3600) % 86400;
        if (nDay < 0 || nDay >= 586){
            return -1;
        }
        for (String s: srcA){
            for (String d: dstA){
                String addr = s+":"+d;
//                System.out.println(addr);
//                LOGGER.info(src+" "+ dst + "  " + startTime );
//                LOGGER.info("collect  "+ s + " " +d);
                if(collect.containsKey(addr)){
                    int collectInfo = collect.get(addr)[nDay];
//                    System.out.println(nDay);
//                    LOGGER.info("eval the addr :" + d + " "+ s);
                    if(collectInfo > 0){
//                        LOGGER.info("collectDDL", collectDDL);
//                        LOGGER.info("collectDelay", collectDelay);
//                        LOGGER.info("collect info ", collectInfo);
//                        LOGGER.info(s + ":" + d +" match the dataset at day"+ nDay);
                        return collectDDL[(collectInfo%8 - 1)]
                               - collectDelay[(collectInfo/8 - 1)]*3600
                                - nSec >=0 ? 0 : 86400;
                    }
                }
            }
            String addr = s+":";
//            LOGGER.info("eval the addr :" + " "+ s);
            if(collect.containsKey(addr)){
                int collectInfo = collect.get(addr)[nDay];
                if(collectInfo > 0){
//                    LOGGER.info("OOK");
//                    LOGGER.info("collectDDL", collectDDL.toString());
//                    LOGGER.info("collectDelay", collectDelay.toString());
//                    LOGGER.info("collect info ", collectInfo);
//                    LOGGER.info(s+": " +" match the dataset at day"+ nDay);
                    return collectDDL[(collectInfo%8 - 1)]
                           - collectDelay[(collectInfo/8 - 1)]*3600
                            - nSec >=0 ? 0 : 86400;
                }
            }

        }
//        LOGGER.info("NO COLLECT MATCH");
        return -1;
    }

    private int getRouterTime(String src, String dst, Long startTime){
        String[] srcA = src.split(":");
        String[] dstA = dst.split(":");
        srcA = Arrays.copyOfRange(srcA, Math.max(0, srcA.length-3), srcA.length);
        int nDay = (int) ((startTime + 8*3600)/86400 - startDay);
        if (nDay < 0 || nDay >= 586){
            return -1;
        }
        for (String s: srcA){
            for (String d: dstA){
                String addr = s+":"+d;
//                System.out.println(addr);
//                LOGGER.info(src+" "+ dst + "  " + startTime );
//                LOGGER.info("route  "+ s + " " +d);
//                LOGGER.info("ROUTE            "+addr);
                if(route.containsKey(addr)){
                    int routeInfo = route.get(addr)[nDay];
//                    System.out.println(nDay);
                    if(routeInfo > 0){
//                        LOGGER.info("routeDelay", routeDelay.toString());
//                        LOGGER.info("route info ", routeInfo);
//                        LOGGER.info(s + " " + d +" match the dataset at day"+ nDay);

                        String[] time = routeDelay[routeInfo-1].split("D");
                        Long days = 86400L * Long.parseLong(time[0]) - 86400L;
                        Long hours = 3600L * Long.parseLong(time[1].substring(0, 2));
                        Long minutes = 60L * Long.parseLong(time[1].substring(2, 4));

                        return (int) (days + hours + minutes);
                    }
                }
            }


        }
        return -1;
    }

    private int getDispatchTime(String dst, Long startTime){
        String[] dstA = dst.split(":");
        int nDay = (int) ((startTime + 8*3600)/86400 - startDay);
        if (nDay < 0 || nDay >= 586){
            return -1;
        }
        for (String d: dstA){
//            LOGGER.info(dst + "  " + startTime );
//            LOGGER.info("dispatch  " +d);
            if(dispatch.containsKey(d)){
                int dispatchInfo = dispatch.get(d)[nDay];
//                System.out.println(nDay);
                if(dispatchInfo > 0){
//                    LOGGER.info("dispatch "+ dispatchInfo + "  " + "disapatch" + " " + d);
                    return dispathDelay[dispatchInfo-1]*3600;
                }
            }

        }
        return -1;
    }

    private String trans2TimeTuple(Long startTime ,Long collectDelay, Long routeDelay, Long dispatchDelay) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
        String s = sdf.format(new Date(startTime*1000L));
        Date startDate =  sdf.parse(s);
        Date arriveTime = DateUtils.addSeconds(startDate, (int)(collectDelay + routeDelay + dispatchDelay));
        String format = DateFormatUtils.format(arriveTime, "yyyy-MM-dd HH:mm:ss");
        return ""+ format;
    }
}
