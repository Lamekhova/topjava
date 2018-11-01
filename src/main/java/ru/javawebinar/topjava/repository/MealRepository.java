package ru.javawebinar.topjava.repository;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.util.List;

public interface MealRepository {
    Meal save(int userId, Meal meal);

    boolean delete(int mealId, int userId);

    Meal get(int mealId, int userId);

    // oredred by date
    List<Meal> getAll(int userId);

    //ordered by date
    List<Meal> getBetween(LocalDateTime startDate, LocalDateTime endDate, int userId);
}
