package ru.javawebinar.topjava.web.meal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.to.MealWithExceed;
import ru.javawebinar.topjava.util.DateTimeUtil;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.web.SecurityUtil;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Controller
public class MealRestController {

    private MealService service;

    @Autowired
    public MealRestController(MealService service) {
        this.service = service;
    }

    protected final Logger log = LoggerFactory.getLogger(MealRestController.class);

    public List<MealWithExceed> getAll() {
        int userId = SecurityUtil.authUserId();
        log.info("Get all for user {}", userId);
        return MealsUtil.getWithExceeded(service.getAll(userId), SecurityUtil.authUserCaloriesPerDay());
    }

    public Meal get(int mealid) {
        int userId = SecurityUtil.authUserId();
        log.info("Get meal {} for user {}", mealid, userId);
        return service.get(mealid, userId);
    }

    public Meal create(Meal meal) {
        int userId = SecurityUtil.authUserId();
        log.info("Create meal {} for user {}", meal, userId);
        return service.create(meal, userId);
    }

    public void delete(int mealId) {
        int userId = SecurityUtil.authUserId();
        log.info("Delete meal {} for user {}", mealId, userId);
        service.delete(mealId, userId);
    }

    public void update(Meal meal, int mealId) {
        int userId = SecurityUtil.authUserId();
        log.info("Update meal {} for user {}", meal, userId);
        service.update(meal, userId);
    }

    public List<MealWithExceed> getBetween(LocalDate startDate, LocalTime startTime, LocalDate endDate, LocalTime endTime) {
        int userId = SecurityUtil.authUserId();
        log.info("getBetween dates({} - {}) time({} - {}) for user {}", startDate, endDate, startTime, endTime, userId);

        List<Meal> mealsDateFiltered = service.getBetweenDates(
                startDate != null ? startDate : DateTimeUtil.MIN_DATE,
                endDate != null ? endDate : DateTimeUtil.MAX_DATE, userId);

        return MealsUtil.getFilteredWithExceeded(mealsDateFiltered, SecurityUtil.authUserCaloriesPerDay(),
                startTime != null ? startTime : LocalTime.MIN,
                endTime != null ? endTime : LocalTime.MAX
        );
    }

}