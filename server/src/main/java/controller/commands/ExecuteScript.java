package controller.commands;

import entity.Color;
import entity.Difficulty;
import entity.LabWorkDTO;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;
import java.util.Scanner;

/**
 * Класс команды по запуску скрипта из указанного файла
 */

/**
 * > This class is a command that executes a script
 */
public class ExecuteScript implements Serializable, Commandable {
    @Override
    public String execute(Object o, String login) {
        String START = (String) o;
        String anwser = "";
        Invoker inv = new Invoker();
        inv.register(new Add(), new Help(), new Info(), new Show(), new Clear(), new Exit(), new UpdateId(),
                new RemoveById(), new RemoveFirst(), new AddIfMin(), new RemoveLower(), new CountLessThanMinimalPoint(),
                new PrintDescending(), new RemoveById(), new PrintUniqueMinimalPoint());
        File file = new File((String) o);
        try {
            Scanner in = new Scanner(file);
            while (in.hasNextLine()) {
                String str = in.nextLine();
                String[] ComAndArg = str.trim().split(" ");
                if (str.trim().isEmpty())
                    continue;

                switch (ComAndArg[0]) {
                    //case "help": inv.execute("help");
                    //case "info":
                    case "add":
                        LabWorkDTO lab = new LabWorkDTO();
                        Boolean reading = true;
                        String buffer = "";
                        int counter = 0;
                        while (reading) {
                            String arg = in.nextLine().trim();
                            counter++;
                            if (!inv.check(arg) && counter < 10) {
                                buffer += " " + arg.trim();
                            } else {
                                buffer += " " + arg.trim();
                                reading = false;
                            }

                        }
                        String[] data = buffer.trim().split(" ");
                        if (data.length == 12)
                            try {
                                lab.setName(data[0]);
                                lab.setMinimalPoint(Double.parseDouble(data[1]));
                                lab.setPersonalQualitiesMinimum(Integer.parseInt(data[2]));
                                LabWorkDTO.CoordinatesDTO c = new LabWorkDTO.CoordinatesDTO();
                                c.setX(Float.parseFloat(data[3]));
                                c.setY(Long.parseLong(data[4]));
                                lab.setCoordinates(c);
                                LabWorkDTO.PersonDTO auth = new LabWorkDTO.PersonDTO();
                                auth.setName(data[5]);
                                auth.setWeight(Float.parseFloat(data[6]));
                                auth.setEyeColor(Color.valueOf(data[7].toUpperCase()));
                                auth.setBirthday(LocalDate.of(Integer.parseInt(data[10]), Integer.parseInt(data[9]), Integer.parseInt(data[8])));
                                lab.setDifficulty(Difficulty.valueOf(data[11].toUpperCase()));
                                lab.setAuthor(auth);
                                anwser += new Add().execute(lab, login);
                                break;
                            } catch (Exception e) {
                                System.out.println("В скрипте ошибка, команда add не выполнена\n");
                                anwser +="В скрипте ошибка, команда add не выполнена\n";
                                break;
                            }
                        else {
                            System.out.println("В скрипте команде add не хватает аргументов\n");
                            anwser +="В скрипте команде add не хватает аргументов\n";
                            break;
                        }
                    case "execute_script":
                        try {
                            Object argument = ComAndArg[1];
                            Commandable command = new Invoker().commands.get(ComAndArg[0]);

                            if (!Objects.equals(argument, START)) {
                                anwser += "\n" + command.execute(argument, login);
                                break;
                            } else {
                                System.out.println("В скрипте рекурсия, команда пропущена");
                                anwser +="\nВ скрипте рекурсия, команда пропущена";
                                break;
                            }

                        } catch (IndexOutOfBoundsException e) {
                            System.out.println("Ошибка, должно быть имя файла");
                            anwser +="\nОшибка, должно быть имя файла";
                            break;
                        }
                    case "add_if_min":
                        lab = new LabWorkDTO();
                        reading = true;
                        buffer = "";
                        counter = 0;
                        while (reading) {
                            String arg = in.nextLine().trim();
                            counter++;
                            if (!inv.check(arg) && counter < 10) {
                                buffer += " " + arg.trim();
                            } else {
                                buffer += " " + arg.trim();
                                reading = false;
                            }

                        }
                        data = buffer.trim().split(" ");
                        if (data.length == 12)
                            try {
//                                lab.setCreationDate();
                                lab.setName(data[0]);
                                lab.setMinimalPoint(Double.parseDouble(data[1]));
                                lab.setPersonalQualitiesMinimum(Integer.parseInt(data[2]));
                                LabWorkDTO.CoordinatesDTO c = new LabWorkDTO.CoordinatesDTO();
                                c.setX(Float.parseFloat(data[3]));
                                c.setY(Long.parseLong(data[4]));
                                lab.setCoordinates(c);
                                LabWorkDTO.PersonDTO auth = new LabWorkDTO.PersonDTO();
                                auth.setName(data[5]);
                                auth.setWeight(Float.parseFloat(data[6]));
                                auth.setEyeColor(Color.valueOf(data[7].toUpperCase()));
                                auth.setBirthday(LocalDate.of(Integer.parseInt(data[10]), Integer.parseInt(data[9]), Integer.parseInt(data[8])));
                                lab.setDifficulty(Difficulty.valueOf(data[11].toUpperCase()));
                                lab.setAuthor(auth);
//                                Vector<LabWork> copy = LabCollection.getClone();
//                                copy.sort(new LabComparator());
                                try {
//                                    if (lab.compareTo(copy.get(0)) < 0) {
//                                        anwser += new controller.commands.Add().execute(lab,login);
//                                        System.out.println("Элемент из скрипта добавлен");
//                                    } else {
//                                        System.out.println("Элемент из скрипта больше минимального, поэтому не добавлен");
//                                    }
                                    anwser += "\n" + new AddIfMin().execute(lab, login);
                                } catch (IndexOutOfBoundsException e) {
                                    System.out.println("Коллекция пуста, команда не будет выполняться");
                                }
                                break;
                            } catch (Exception e) {
                                System.out.println("В скрипте ошибка, команда add не выполнена");
                                anwser += "\nВ скрипте ошибка, команда add не выполнена";
                                break;
                            }
                        else {
                            System.out.println("В скрипте команде add_if_min не хватает аргументов");
                            anwser += "\nВ скрипте ошибка, команда add не выполнена";
                            break;
                        }
                    case "update_id":
                        lab = new LabWorkDTO();
                        reading = true;
                        buffer = "";
                        counter = 0;
                        while (reading) {
                            String arg = in.nextLine().trim();
                            counter++;
                            if (!inv.check(arg) && counter < 10) {
                                buffer += " " + arg.trim();
                            } else {
                                buffer += " " + arg.trim();
                                reading = false;
                            }

                        }
                        data = buffer.trim().split(" ");
                        if (data.length == 12)
                            try {
//                                lab.setCreationDate();
                                lab.setName(data[0]);
                                lab.setMinimalPoint(Double.parseDouble(data[1]));
                                lab.setPersonalQualitiesMinimum(Integer.parseInt(data[2]));
                                LabWorkDTO.CoordinatesDTO c = new LabWorkDTO.CoordinatesDTO();
                                c.setX(Float.parseFloat(data[3]));
                                c.setY(Long.parseLong(data[4]));
                                lab.setCoordinates(c);
                                LabWorkDTO.PersonDTO auth = new LabWorkDTO.PersonDTO();
                                auth.setName(data[5]);
                                auth.setWeight(Float.parseFloat(data[6]));
                                auth.setEyeColor(Color.valueOf(data[7].toUpperCase()));
                                auth.setBirthday(LocalDate.of(Integer.parseInt(data[10]), Integer.parseInt(data[9]), Integer.parseInt(data[8])));
                                lab.setDifficulty(Difficulty.valueOf(data[11].toUpperCase()));
                                lab.setAuthor(auth);
                            } catch (Exception e) {
                                System.out.println("В скрипте ошибка, команда update не выполнена");
                                anwser += "\nВ скрипте ошибка, команда update не выполнена";
                                break;
                            }
                        else {
                            System.out.println("В скрипте команде update не хватает аргументов");
                            anwser += "\nВ скрипте команде update не хватает аргументов";
                            break;
                        }
                        try {
                            int id = Integer.parseInt((String) ComAndArg[1]);
//                            lab.setId(id);
                            anwser += "\n" + new UpdateId().execute(lab,login);
                            break;
//                            if (LabCollection.ids.contains(id)) {
//                                for (LabWork elems : LabCollection.collection) {
//                                    if (elems.getId().equals(id)) {
//                                        LabCollection.ids.remove(elems.getId());
//                                        elems.clone(lab);
//
//                                    }
//                                }
//                                System.out.println("Замена прошла успешно");
//                                break;
//                            } else {
//                                System.out.println("Нет элемента с таким ID");
//                                break;
//                            }
                        } catch (NumberFormatException e) {
                            System.out.println("Id должен быть целым числом в команде update id скрипта");
                            break;
                        }

                    case "remove_lower":
                        lab = new LabWorkDTO();
                        reading = true;
                        buffer = "";
                        counter = 0;
                        while (reading) {
                            String arg = in.nextLine().trim();
                            counter++;
                            if (!inv.check(arg) && counter < 10) {
                                buffer += " " + arg.trim();
                            } else {
                                buffer += " " + arg.trim();
                                reading = false;
                            }

                        }
                        data = buffer.trim().split(" ");
                        if (data.length == 12)
                            try {
//                                lab.setCreationDate();

                                lab.setName(data[0]);
                                lab.setMinimalPoint(Double.parseDouble(data[1]));
                                lab.setPersonalQualitiesMinimum(Integer.parseInt(data[2]));
                                LabWorkDTO.CoordinatesDTO c = new LabWorkDTO.CoordinatesDTO();
                                c.setX(Float.parseFloat(data[3]));
                                c.setY(Long.parseLong(data[4]));
                                lab.setCoordinates(c);
                                LabWorkDTO.PersonDTO auth = new LabWorkDTO.PersonDTO();
                                auth.setName(data[5]);
                                auth.setWeight(Float.parseFloat(data[6]));
                                auth.setEyeColor(Color.valueOf(data[7].toUpperCase()));
                                auth.setBirthday(LocalDate.of(Integer.parseInt(data[10]), Integer.parseInt(data[9]), Integer.parseInt(data[8])));
                                lab.setDifficulty(Difficulty.valueOf(data[11].toUpperCase()));
                                lab.setAuthor(auth);
                                //LabCollection.collection.add(lab);

                                //Vector<LabWork> buff = new Vector<>();
                                anwser += "\n" + new RemoveLower().execute(lab, login);
                                break;
//                                for (LabWork elems : LabCollection.collection) {
//                                    if (elems.compareTo(lab) < 0) {
//                                        LabCollection.ids.remove(elems.getId());
//                                        buff.add(elems);
//                                    }
//                                }
//                                if (buff.isEmpty()) {
//                                    System.out.println("Таких элементов нет");
//                                    break;
//                                } else {
//                                    for (LabWork elems : buff) {
//                                        labcollection.remove(elems);
//                                    }
//                                    System.out.println("Элементы меньше данного удалены");
//                                    break;
//                                }
                            } catch (Exception e) {
                                System.out.println("В скрипте ошибка, команда add не выполнена");
                                anwser +="\nВ скрипте ошибка, команда add не выполнена";
                                break;
                            }


                    default:
                        Object argument = null;
                        Commandable command = new Invoker().commands.get(ComAndArg[0]);
                        try {
                            if (command == null)
                                throw new IllegalStateException();

                            if (ComAndArg.length == 2)
                                argument = ComAndArg[1];

                            anwser +="\n" + command.execute(argument, login);
                        } catch (IllegalStateException e) {
                            System.out.println("Команды " + ComAndArg[0] + " из скрипта не существует, введите \"help\", чтобы ознакомиться со всем перечнем команд.");
                            anwser += "\nКоманды " + ComAndArg[0] + " из скрипта не существует, введите \"help\", чтобы ознакомиться со всем перечнем команд.\n";
                        }

                }

            }
        } catch (FileNotFoundException e) {
            System.out.println("Файл не существует или закрыт для чтения");
            return "Файл не существует или закрыт для чтения";
        }
        return anwser;
    }

//    class LabWorkCreate {
//        private Scanner in;
//
//        LabWorkCreate(Scanner in) {
//            LabWork a = new LabWork();
//        }
//
//        public LabWork create() {
//            LabWork lab = new LabWork();
//            lab.setCreationDate();
//            setId(lab);
//
//            return lab;
//        }
//
//        private void setId(LabWork lab) {
//            try {
//                lab.setId(LabCollection.getFreeId());
//            } catch (IdBusyException e) {
//                e.printStackTrace();
//            }
//        }
//
//        private static void setPersonalQualitiesMinimum(LabWork l) {
//            System.out.print("Введите минимальный балл за личные качества:\n" + ">");
//            try {
//                String s = entity.ConsoleIO.ConsoleIn();
//                if (s.equals("") || s.equals("null")) {
//                    System.out.println("Не может быть null или \"\" ");
//                    setPersonalQualitiesMinimum(l);
//                } else {
//                    int point = Integer.parseInt(s);
//                    if (point > 0) {
//                        l.setPersonalQualitiesMinimum(point);
//                    } else {
//                        System.out.println("Балл должен быть больше 0");
//                        setPersonalQualitiesMinimum(l);
//                    }
//                }
//            } catch (NumberFormatException a) {
//                System.out.println("Нужно ввести балл в формате целого числа");
//                setPersonalQualitiesMinimum(l);
//            }
//        }
//
//
//    }

    @Override
    public String getDescription() {
        return ": выполняет набор команд из файла";
    }

    @Override
    public String getName() {
        return "execute_script";
    }
}
