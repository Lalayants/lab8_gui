package controller.commands;

import model.Repository;
import requests.Response;

import java.io.Serializable;

/**
 * Класс команды подсчета элементов, минимальный балл которых меньше заданного
 */

/**
 * This class is a commandable class that counts the number of points in a given set that are less than a given point
 */
public class CountLessThanMinimalPoint implements Serializable, Commandable {
    @Override
    public Response execute(Object o, String login) {
            Double ref = (Double) o;
//          Connection c = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres", "postgres", "kpop");
            return Repository.CountLessThanMinimalPoint(ref);


    }

//    @Override
//    public String execute(Object o, String login) {
//        try {
//            int i = 0;
//            int ref = Integer.parseInt((String) o);
//            return ("Таких " + LabCollection.collection.stream().filter(lab->lab.getMinimalPoint()<ref).count());
////            for (LabWork elems : LabCollection.collection) {
////                if (elems.getMinimalPoint() < ref) {
////                    i++;
////                }
////            }
////            System.out.println("Таких " + i);
//        } catch(NumberFormatException e){
//            return "MinimalPoint должен быть целым числом";
//        }
//
//    }
    @Override
    public String getDescription() {
        return ": вывести количество элементов, значение поля minimalPoint которых меньше заданного";
    }

    @Override
    public String getName() {
        return "count_less_than_minimal_point";
    }
}
