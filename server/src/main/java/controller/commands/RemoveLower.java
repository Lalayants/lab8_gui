package controller.commands;

import entity.LabWorkDTO;
import model.Repository;
import requests.Response;

import java.io.Serializable;


/**
 * Класс команды, удаляющей все элементы меньше указанного
 */

public class RemoveLower implements Commandable, Serializable {
    @Override
    public Response execute(Object o, String login) {
        LabWorkDTO labWork = (LabWorkDTO) o;
        return Repository.removeLower(labWork, login);
    }

    @Override
    public String getDescription() {
        return ": удалить из коллекции созданные вами элементы, меньшие, чем заданный";
    }

    @Override
    public String getName() {
        return "remove_lower";
    }
}
