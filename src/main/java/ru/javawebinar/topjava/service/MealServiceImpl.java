package ru.javawebinar.topjava.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.time.LocalDateTime;
import java.util.List;

import static ru.javawebinar.topjava.util.ValidationUtil.checkNotFoundWithId;

@Service
public class MealServiceImpl implements MealService {

    private MealRepository repository;

    @Autowired
    public MealServiceImpl(MealRepository repository) {
        this.repository = repository;
    }

    @Override
    public Meal create(Meal meal, int userId) {
        return repository.save(userId, meal);
    }

    @Override
    public void delete(int mealId, int userId) throws NotFoundException {
        checkNotFoundWithId(repository.delete(mealId, userId), mealId);
    }

    @Override
    public Meal get(int mealId, int userId) throws NotFoundException {
        return checkNotFoundWithId(repository.get(mealId, userId), mealId);
    }

    @Override
    public void update(Meal meal, int userId) throws NotFoundException {
        checkNotFoundWithId(repository.save(userId, meal), meal.getId());
    }

    @Override
    public List<Meal> getAll(int userId) {
        return repository.getAll(userId);
    }

    @Override
    public List<Meal> getBetweenDateTimes(LocalDateTime startDateTime, LocalDateTime endDateTime, int userId) {
        return repository.getBetween(startDateTime, endDateTime, userId);
    }
}