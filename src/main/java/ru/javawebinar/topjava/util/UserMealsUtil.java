package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExceed;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.*;
import java.util.stream.*;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.mapping;

public class UserMealsUtil {
    public static void main(String[] args) {
        List<UserMeal> mealList = Arrays.asList(
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30, 10, 0), "Завтрак", 500),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30, 11, 0), "Обед", 100),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30, 20, 0), "Ужин", 500),

                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31, 10, 0), "Завтрак", 1000),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31, 13, 0), "Обед", 500),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31, 20, 0), "Ужин", 510)
        );
//        List<UserMealWithExceed> filteredWithExceeded = getFilteredWithExceeded(mealList,
        List<UserMealWithExceed> filteredWithExceeded = getFilteredWithExceededShortened(mealList,
                LocalTime.of(7, 0),
                LocalTime.of(12, 0), 2000);
        for (UserMealWithExceed userMeal : filteredWithExceeded) {
            System.out.println(userMeal.toString());
        }
    }

    private static List<UserMealWithExceed> getFilteredWithExceeded(List<UserMeal> mealList, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        List<UserMeal> userMealsBetweenStartAndEndTime = mealList.stream()
                .filter(userMeal -> userMeal.getDateTime().toLocalTime().isAfter(startTime) && userMeal.getDateTime().toLocalTime().isBefore(endTime))
                .collect(Collectors.toList());
        Map<LocalDate, List<UserMeal>> mealsByDate = mealList.stream()
                .collect(groupingBy(UserMeal::getDate));
        Set<LocalDate> datesWithExceed = new HashSet<>();
        for (Map.Entry<LocalDate, List<UserMeal>> entry : mealsByDate.entrySet()) {
            if (entry.getValue().stream().mapToInt(UserMeal::getCalories).sum() > caloriesPerDay) {
                datesWithExceed.add(entry.getKey());
            }
        }
        return userMealsBetweenStartAndEndTime.stream()
                .map(i -> new UserMealWithExceed(i.getDateTime(), i.getDescription(), i.getCalories(), datesWithExceed.contains(i.getDate())))
                .collect(Collectors.toList());
    }

    private static List<UserMealWithExceed> getFilteredWithExceededShortened(List<UserMeal> mealList, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        Map<LocalDate, Integer> mealDatesAndCalories = new HashMap<>();
        for (UserMeal userMeal : mealList) {
            mealDatesAndCalories.merge(userMeal.getDateTime().toLocalDate(), userMeal.getCalories(), (i1, i2) -> i1 + i2);
        }
        return mealList.stream()
                .filter(userMeal -> userMeal.getDateTime().toLocalTime().isAfter(startTime) && userMeal.getDateTime().toLocalTime().isBefore(endTime))
                .map(i -> new UserMealWithExceed(i.getDateTime(), i.getDescription(), i.getCalories(),
                        mealDatesAndCalories.get(i.getDate()) > caloriesPerDay))
                .collect(Collectors.toList());
    }
}
