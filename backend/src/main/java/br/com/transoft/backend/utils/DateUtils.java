package br.com.transoft.backend.utils;

import lombok.experimental.UtilityClass;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@UtilityClass
public class DateUtils {

    public static List<LocalDate> getDatesBetween(LocalDate startDate, LocalDate endDate, List<DayOfWeek> daysOfWeek) {
        List<LocalDate> dates = new ArrayList<>();

        LocalDate currentDate = startDate;

        while (!currentDate.isAfter(endDate)) {
            if (!daysOfWeek.contains(currentDate.getDayOfWeek())) {
                currentDate = currentDate.plusDays(1);
                continue;
            }
            dates.add(currentDate);
            currentDate = currentDate.plusDays(1);
        }

        return dates;
    }

}
