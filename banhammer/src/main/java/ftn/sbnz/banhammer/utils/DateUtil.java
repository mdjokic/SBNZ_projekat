package ftn.sbnz.banhammer.utils;

import org.apache.commons.lang3.time.DateUtils;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

public class DateUtil {

    public static Date minusMonths(int months){
        Date newDate = new Date();
        newDate = DateUtils.addMonths(newDate, months * (-1));
        return newDate;
    }

    public static long daysBetween(Date date1, Date date2){
        LocalDateTime dateTime1 = date1.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        LocalDateTime dateTime2 = date2.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        return Duration.between(dateTime1, dateTime2).toDays();
    }
}
