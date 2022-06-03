package model;

import entity.LabWorkDTO;
import javafx.beans.property.*;

import java.io.Serializable;
import java.sql.Timestamp;

public class LabModel implements Serializable {
    private IntegerProperty id;
    private StringProperty name; //Поле не может быть null, Строка не может быть пустой
    private FloatProperty x;
    private LongProperty y;
    private ObjectProperty<Timestamp> creationDate; //Поле не может быть null, Значение этого поля должно генерироваться автоматически
    private FloatProperty minimalPoint; //Поле может быть null, Значение поля должно быть больше 0
    private IntegerProperty personalQualitiesMinimum; //Поле не может быть null, Значение поля должно быть больше 0
    private StringProperty difficulty; //Поле не может быть null
    private StringProperty author; //Поле не может быть null, Строка не может быть пустой
    private FloatProperty weight; //Значение поля должно быть больше 0
    private StringProperty eyeColor; //Поле может быть null
    private IntegerProperty creators_id;

    public LabModel(LabWorkDTO lab) {
        setId(lab.getId());
        setX(lab.getX());
        setY(lab.getY());
        setName(lab.getName());
        setCreationDate(lab.getCreationDate());
        setMinimalPoint(lab.getMinimalPoint());
        setPersonalQualitiesMinimum(lab.getPersonalQualitiesMinimum());
        setDifficulty(lab.getDifficulty());
        setAuthor(lab.getAuthor());
        setWeight(lab.getWeight());
        try {
            setEyeColor(lab.getEyeColor());
        } catch (NullPointerException ignored) {
        }
        setCreators_id(lab.getCreators_id());
    }

    public LabWorkDTO toDTO() {
        LabWorkDTO labWorkDTO = new LabWorkDTO();
        labWorkDTO.setName(name.get());
        labWorkDTO.setX(x.get());
        labWorkDTO.setY(y.get());
        labWorkDTO.setMinimalPoint(minimalPoint.get());
        labWorkDTO.setPersonalQualitiesMinimum(personalQualitiesMinimum.get());
        labWorkDTO.setDifficulty(difficulty.get());
        labWorkDTO.setAuthor(author.get());
        labWorkDTO.setWeight(weight.get());
        try {
            labWorkDTO.setId(id.get());
        } catch (NullPointerException ignored) {
        }
        try {
            labWorkDTO.setCreationDate(creationDate.get());
        } catch (NullPointerException ignored) {
        }
        try {
            labWorkDTO.setEyeColor(eyeColor.get());
        } catch (NullPointerException ignored) {
        }
        return labWorkDTO;
    }

    public LabModel() {

    }

    public int getId() {
        return id.get();
    }

    public IntegerProperty idProperty() {
        return id;
    }

    public void setId(int id) {
        this.id = new SimpleIntegerProperty(id);
    }

    public String getName() {
        return name.get();
    }

    public StringProperty nameProperty() {
        return name;
    }

    public void setName(String name) {
        this.name = new SimpleStringProperty(name);
    }

    public float getX() {
        return x.get();
    }

    public FloatProperty xProperty() {
        return x;
    }

    public void setX(float x) {
        this.x = new SimpleFloatProperty(x);
    }

    public long getY() {
        return y.get();
    }

    public LongProperty yProperty() {
        return y;
    }

    public void setY(long y) {
        this.y = new SimpleLongProperty(y);
    }

    public Timestamp getCreationDate() {
        return creationDate.get();
    }

    public ObjectProperty<Timestamp> creationDateProperty() {
        return creationDate;
    }

    public void setCreationDate(Timestamp creationDate) {
        this.creationDate = new SimpleObjectProperty<>(creationDate);
    }

    public double getMinimalPoint() {
        return minimalPoint.get();
    }

    public FloatProperty minimalPointProperty() {
        return minimalPoint;
    }

    public void setMinimalPoint(float minimalPoint) {
        this.minimalPoint = new SimpleFloatProperty(minimalPoint);
    }

    public int getPersonalQualitiesMinimum() {
        return personalQualitiesMinimum.get();
    }

    public IntegerProperty personalQualitiesMinimumProperty() {
        return personalQualitiesMinimum;
    }

    public void setPersonalQualitiesMinimum(int personalQualitiesMinimum) {
        this.personalQualitiesMinimum = new SimpleIntegerProperty(personalQualitiesMinimum);
    }

    public String getDifficulty() {
        return difficulty.get();
    }

    public StringProperty difficultyProperty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = new SimpleStringProperty(difficulty);
    }

    public String getAuthor() {
        return author.get();
    }

    public StringProperty authorProperty() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = new SimpleStringProperty(author);
    }

    public float getWeight() {
        return weight.get();
    }

    public FloatProperty weightProperty() {
        return weight;
    }

    public void setWeight(float weight) {
        this.weight = new SimpleFloatProperty(weight);
    }

    public String getEyeColor() {
        return eyeColor.get();
    }

    public StringProperty eyeColorProperty() {
        return eyeColor;
    }

    public void setEyeColor(String eyeColor) {
        this.eyeColor = new SimpleStringProperty(eyeColor);
    }

    public int getCreators_id() {
        return creators_id.get();
    }

    public IntegerProperty creators_idProperty() {
        return creators_id;
    }

    public void setCreators_id(int creators_id) {
        this.creators_id = new SimpleIntegerProperty(creators_id);
    }

    @Override
    public String toString() {
        return "LabModel{" +
                "id=" + id +
                ", name=" + name +
                ", x=" + x +
                ", y=" + y +
                ", creationDate=" + creationDate +
                ", minimalPoint=" + minimalPoint +
                ", personalQualitiesMinimum=" + personalQualitiesMinimum +
                ", difficulty=" + difficulty +
                ", author=" + author +
                ", weight=" + weight +
                ", eyeColor=" + eyeColor +
                ", creators_id=" + creators_id +
                '}';
    }
}
