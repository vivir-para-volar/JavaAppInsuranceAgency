package com.insuranceagency.model;

import org.jetbrains.annotations.NotNull;
import java.time.LocalDate;

/**
 * Класс Фотография со свойствами <b>id</b>, <b>path</b>, <b>uploadDate</b> и <b>carId</b>.
 * <p>Данный класс позволяет описать экземпляр фотографии закреплённой с автомобилем с заданным параметрами.</p>
 */
public class Photo {
    /**
     * Поле Id
     */
    private int id;

    /**
     * Поле Путь
     */
    @NotNull
    private String path;

    /**
     * Поле Дата загрузки
     */
    @NotNull
    private LocalDate uploadDate;

    /**
     * Поле Id автомобиля
     */
    private int carId;

    /**
     * Инициализирует новый экземпляр класса Photo с заданными параметрами
     * @param id Id
     * @param path Путь
     * @param uploadDate Дата загрузки
     * @param carId Id автомобиля
     */
    public Photo(int id, @NotNull String path, @NotNull LocalDate uploadDate, int carId) {
        this.id = id;
        this.path = path;
        this.uploadDate = uploadDate;
        this.carId = carId;
    }

    /**
     * Инициализирует новый экземпляр класса Photo с заданными параметрами
     * @param path Путь
     * @param uploadDate Дата загрузки
     * @param carId Id автомобиля
     */
    public Photo(@NotNull String path, @NotNull LocalDate uploadDate, int carId) {
        this.path = path;
        this.uploadDate = uploadDate;
        this.carId = carId;
    }

    /**
     * Функция получения значение поля {@link Photo#id}
     * @return Id
     */
    public int getId() {
        return id;
    }

    /**
     * Функция получения значение поля {@link Photo#path}
     * @return Путь
     */
    public @NotNull String getPath() {
        return path;
    }

    /**
     * Функция получения значение поля {@link Photo#uploadDate}
     * @return Дата загрузки
     */
    public @NotNull LocalDate getUploadDate() {
        return uploadDate;
    }

    /**
     * Функция получения значение поля {@link Photo#carId}
     * @return Id автомобиля
     */
    public int getCarId() {
        return carId;
    }
}