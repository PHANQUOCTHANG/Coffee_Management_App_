package com.example.javafxapp.Helpper;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public class TimeConverter {

    // Chuyển đổi LocalDateTime sang ZonedDateTime với múi giờ GMT+7 (Asia/Ho_Chi_Minh)
    public static ZonedDateTime toGmtPlus7(LocalDateTime localDateTime) {
        ZoneId zoneVN = ZoneId.of("Asia/Ho_Chi_Minh");
        return localDateTime.atZone(zoneVN);
    }
}

