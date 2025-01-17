package KGUcapstone.OutDecision.global.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class DateTimeFormatUtil {

    public static String formatDeadline(Date dateTime) {
        SimpleDateFormat sdf = new SimpleDateFormat("yy.MM.dd HH:mm");
        return sdf.format(dateTime);
    }

    public static String formatCreatedAt(LocalDateTime createdAt) {
        if (createdAt.toLocalDate().isEqual(LocalDate.now())) {
            // 오늘이라면 HH:mm 시간만 표시
            return createdAt.format(DateTimeFormatter.ofPattern("HH:mm"));
        } else {
            // 오늘이 아니라면 MM-dd 형식으로 표시
            return createdAt.format(DateTimeFormatter.ofPattern("MM-dd"));
        }
    }

    public static String formatCreatedAt2(LocalDateTime createdAt) {
        return createdAt.format(DateTimeFormatter.ofPattern("yy.MM.dd HH:mm"));
    }

    public static Date parseStringToDate(String date){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yy.MM.dd HH:mm");
        try {
            return dateFormat.parse(date);
        } catch (ParseException e) {return new Date();}
    }
}
