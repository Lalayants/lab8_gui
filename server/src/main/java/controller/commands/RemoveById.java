package controller.commands;

import model.Repository;

import java.io.Serializable;

/**
 * Класс команды, удаляющей элемент по id
 */

public class RemoveById implements Serializable, Commandable {
    @Override
    public String execute(Object o, String login) {
        try {
            int id = Integer.parseInt((String) o);
            return Repository.removeById(id, login);

        } catch (NumberFormatException e) {
            return "Id должен быть целым числом";
        }

    }

    @Override
    public String getDescription() {
        return ": удалить элемент из коллекции по его id(если он был создан вами)";
    }

    @Override
    public String getName() {
        return "remove_by_id";
    }
}
