package controller.commands;

import entity.LabWorkDTO;
import model.Repository;


import java.io.Serializable;


/**
 * Adds a new element to the collection.
 */
public class Add implements Commandable, Serializable {
    /**
     * @param o
     * @return
     */
    @Override
    public String execute(Object o, String login) {
        return Repository.add((LabWorkDTO) o, login);
    }

    /**
     * @return
     */
    @Override
    public String getName() {
        return "add";
    }

    /**
     * @return
     */
    public String getDescription() {
        return ": добавить новый элемент в коллекцию";
    }
}
