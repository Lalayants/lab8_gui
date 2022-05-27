package entity;

import exceptions.ComaInsteadOfDotException;


/**
 * Класс для создания элементов коллекции
 */

public class LabWorkDTOCreator {
//    //public static Scanner in = new Scanner(System.in);
//
//    public LabWorkDTO create() {
//        LabWorkDTO labWork = new LabWorkDTO();
//        setName(labWork);
//        setMinimalPoint(labWork);
//        setPersonalQualitiesMinimum(labWork);
//        setCoordinates(labWork);
//        setPerson(labWork);
//        setDifficulty(labWork);
//
//        return labWork;
//    }
//
//    public LabWorkDTO create(int id) {
//        LabWorkDTO labWork = new LabWorkDTO();
//        setName(labWork);
//        setMinimalPoint(labWork);
//        setPersonalQualitiesMinimum(labWork);
//        setCoordinates(labWork);
//        setPerson(labWork);
//        setDifficulty(labWork);
//
//        return labWork;
//    }
//
//
//    private void setPersonalQualitiesMinimum(LabWorkDTO l) {
//        System.out.print("Введите минимальный балл за личные качества:\n" + ">");
//        try {
//            String s = ConsoleIO.ConsoleIn();
//            if (s.equals("") || s.equals("null")) {
//                System.out.println("Не может быть null или \"\" ");
//                setPersonalQualitiesMinimum(l);
//            } else {
//                int point = Integer.parseInt(s);
//                if (point > 0) {
//                    l.setPersonalQualitiesMinimum(point);
//                } else {
//                    System.out.println("Балл должен быть больше 0");
//                    setPersonalQualitiesMinimum(l);
//                }
//            }
//        } catch (NumberFormatException a) {
//            System.out.println("Нужно ввести балл в формате целого числа");
//            setPersonalQualitiesMinimum(l);
//        }
//    }
//
//    public void setMinimalPoint(LabWorkDTO l) {
//        System.out.print("Введите минимальный балл :\n" + ">");
//        try {
//            String s = ConsoleIO.ConsoleIn();
//            if (s.contains(",")) {
//                throw new ComaInsteadOfDotException();
//            } else if (s.equals("") || s.equals("null")) {
//                System.out.println("Не может быть null или \"\" ");
//                setMinimalPoint(l);
//            } else {
//                double point = Double.parseDouble(s);
//                if (point > 0) {
//                    l.setMinimalPoint(point);
//                } else {
//                    System.out.println("Масса должна быть больше 0");
//                    setMinimalPoint(l);
//                }
//            }
//        } catch (NumberFormatException a) {
//            System.out.println("Нужно ввести массу в формате десятичной дроби разделенной точкой");
//            setMinimalPoint(l);
//        } catch (ComaInsteadOfDotException a) {
//            setMinimalPoint(l);
//        }
//    }
//
//    public void setDifficulty(LabWorkDTO lab) {
//        Difficulty[] difficulties = Difficulty.values();
//        String s = "";
//        for (Difficulty a : difficulties) {
//            s += a.name() + ", ";
//        }
//        System.out.print("Оцените лабораторную работу одной из перечисленных сложностей(" + s + "):\n" + ">"); //Так аккуратнее или можно без плюса в одних ""?
//        String name = ConsoleIO.ConsoleIn();
//        try {
//            if (name.equals("") || name.equals("null")) {
//                System.out.println("Ошибка.Сложность не может быть null/пустой строкой");
//                setDifficulty(lab);
//            } else {
//                lab.setDifficulty(Difficulty.valueOf(name.toUpperCase()));
//            }
//        } catch (IllegalArgumentException e) {
//            System.out.println("Вы ошиблись, такой сложности нет");
//            setDifficulty(lab);
//        }
//
//    }
//
//    private void setName(LabWorkDTO lab) {
//        System.out.print("Введите имя лабораторной работы:\n" + ">"); //Так аккуратнее или можно без плюса в одних "?
//        String name = ConsoleIO.ConsoleIn();
//        if (name.equals("") || name.equals("null")) {
//            System.out.println("Ошибка. Имя не может быть null/пустой строкой");
//            setName(lab);
//        } else {
//            lab.setName(name);
//        }
//    }
//
//    private void setCoordinates(LabWorkDTO lab) {
//        LabWorkDTO.CoordinatesDTO coordinates = new LabWorkDTO.CoordinatesDTO();
//        setCoordX(coordinates);
//        setCoordY(coordinates);
//        // coordinates.setY();
//        lab.setCoordinates(coordinates);
//    }
//
//    private void setCoordX(LabWorkDTO.CoordinatesDTO c) {
//        System.out.print("Введите координату X :\n" + ">");
//        try {
//            String s = ConsoleIO.ConsoleIn();
//            if (s.contains(",")) {
//                throw new ComaInsteadOfDotException();
//            } else if (s.equals("") || s.equals("null")) {
//                System.out.println("Не может быть null или \"\" ");
//                this.setCoordX(c);
//            } else {
//                c.setX(Float.parseFloat(s));
//            }
//        } catch (NumberFormatException a) {
//            System.out.println("Нужно ввести десятичную дробь состоящую из цифр и разделительной точки без использования букв");
//            this.setCoordX(c);
//        } catch (ComaInsteadOfDotException a) {
//            this.setCoordX(c);
//        }
//
//    }
//
//    private void setCoordY(LabWorkDTO.CoordinatesDTO c) {
//        System.out.print("Введите координату Y :\n" + ">");
//        try {
//            String s = ConsoleIO.ConsoleIn();
//            if (s.equals("") || s.equals("null")) {
//                System.out.println("Не может быть null или \"\" ");
//                this.setCoordY(c);
//            } else {
//                c.setY(Long.parseLong(s));
//            }
//        } catch (NumberFormatException a) {
//            System.out.println("Нужно ввести целое число, состоящее только из цифр");
//            this.setCoordY(c);
//        }
//
//    }
//
//    private void setPerson(LabWorkDTO lab) {
//        LabWorkDTO.PersonDTO p = new LabWorkDTO.PersonDTO();
//        p.setName();
//        p.setWeight();
//        p.setEyeColor();
//        p.setBirthday();
//        lab.setAuthor(p);
//    }


}
