package controller.commands;
import entity.LabWorkDTO;
import model.Repository;
import requests.Response;

import java.io.Serializable;

/**
 * Класс команды, обновляющей элемент по ID
 */

public class UpdateId implements Serializable, Commandable {
    @Override
    public Response execute(Object o, String login) {
        LabWorkDTO lab = (LabWorkDTO) o;
        return Repository.updateID(lab, login);
    }

    @Override
    public String getDescription() {
        return ": обновить значение элемента коллекции, id которого равен заданному";
    }

    @Override
    public String getName() {
        return "update";
    }
}
