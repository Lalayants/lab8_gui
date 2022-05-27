package controller.commands;
import entity.LabWorkDTO;
import model.Repository;
import requests.Response;

import java.io.Serializable;


/**
 * It's a command that adds a new element to a collection if it's less than the minimum element of the collection
 */
public class AddIfMin implements Serializable, Commandable {
    /**
     * @param o
     * @return
     */
    @Override
    public Response execute(Object o, String login) {
        return Repository.addIfMin((LabWorkDTO) o, login);
    }

    /**
     * @return
     */
    @Override
    public String getDescription() {
        return ": добавить новый элемент в коллекцию, если его значение меньше, чем у наименьшего элемента этой коллекции";
    }

    /**
     * @return
     */
    @Override
    public String getName() {
        return "add_if_min";
    }
}
