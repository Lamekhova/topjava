package ru.javawebinar.topjava.repository.mock;

import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.DateTimeUtil;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.util.Util;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Repository
public class InMemoryMealRepositoryImpl implements MealRepository {

    private Map<Integer, Map <Integer, Meal>> repository = new ConcurrentHashMap<>();
    private AtomicInteger counter = new AtomicInteger(0);

    public static final Comparator<Meal> MEAL_COMPARATOR = (meal1, meal2) -> meal2.getDateTime().compareTo(meal1.getDateTime());

    public static final int USER_ID = 1;
    public static final int ADMIN_ID = 2;

    {
        save(USER_ID, new Meal(LocalDateTime.of(2015, Month.MAY, 30, 10, 0), "затврак юзер", 300));
        save(ADMIN_ID, new Meal(LocalDateTime.of(2015, Month.MAY, 30, 10, 0), "затврак админ", 330));
    }

    @Override
    public Meal save(int userId, Meal meal) {
        Map <Integer, Meal> mealsOfUser = repository.computeIfAbsent(userId, ConcurrentHashMap::new);
        if (meal.isNew()) {
            meal.setId(counter.incrementAndGet());
            mealsOfUser.put(meal.getId(), meal);
            return meal;
        }
        // treat case: update, but absent in storage
        return mealsOfUser.computeIfPresent(meal.getId(), (id, oldMeal) -> meal);
    }

    @Override
    public boolean delete(int mealId, int userId) {
        Map <Integer, Meal> mealsOfUser = repository.get(userId);
        return mealsOfUser != null && mealsOfUser.remove(mealId) != null;
    }

    @Override
    public Meal get(int mealId, int userId) {
        Map <Integer, Meal> mealsOfUser = repository.get(userId);
        return mealsOfUser != null ? mealsOfUser.get(mealId) : null;
    }

    @Override
    public List<Meal> getAll(int userId) {
        Map <Integer, Meal> mealsOfUser = repository.get(userId);
        return mealsOfUser == null ?
                Collections.emptyList() :
                mealsOfUser.values().stream().sorted(MEAL_COMPARATOR).collect(Collectors.toList());
    }

    @Override
    public List<Meal> getBetween(LocalDateTime startDateTime, LocalDateTime endDateTime, int userId) {
        Objects.requireNonNull(startDateTime);
        Objects.requireNonNull(endDateTime);
        return getAllFiltered(userId, meal -> Util.isBetween(meal.getDateTime(), startDateTime, endDateTime));
    }

    private List<Meal> getAllFiltered(int userId, Predicate<Meal> filter) {
        Map<Integer, Meal> meals = repository.get(userId);
        return CollectionUtils.isEmpty(meals) ? Collections.emptyList() :
                meals.values().stream()
                        .filter(filter)
                        .sorted(Comparator.comparing(Meal::getDateTime).reversed())
                        .collect(Collectors.toList());
    }
}

