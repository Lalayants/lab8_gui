package controller.commands;

import model.Repository;

import java.io.Serializable;


/**
 * Класс команды, выводящий количество уникальных значений полей минимальный балл
 */
public class PrintUniqueMinimalPoint implements Serializable, Commandable {
    @Override
    public String execute(Object o, String login) {
        return Repository.getUniqueMinimalPoint();
    }


    @Override
    public String getDescription() {
        return ": вывести уникальные значения поля minimalPoint всех элементов в коллекции";
    }

    @Override
    public String getName() {
        return "print_unique_minimal_point";
    }
}
