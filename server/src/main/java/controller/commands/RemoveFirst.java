package controller.commands;

import model.Repository;

import java.io.Serializable;


/**
 * Класс команды, удаляющей первый элемент
 */

public class RemoveFirst implements Serializable, Commandable {

    @Override
    public String execute(Object o, String login) {
        return Repository.removeFirst(login);
    }

    @Override
    public String getDescription() {
        return ": удалить первый элемент из коллекции, созданный вами";
    }

    @Override
    public String getName() {
        return "remove_first";
    }
}
