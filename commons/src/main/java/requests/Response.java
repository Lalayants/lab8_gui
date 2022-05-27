package requests;

import entity.LabWorkDTO;

import java.io.Serializable;

public class Response implements Serializable {
    private String answer;
    private boolean forUpdate = false;
    private String command;
    private LabWorkDTO LabWork;
    private Object objectAnswer;

    public Response(String answer) {
        this.answer = answer;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public LabWorkDTO getLabWork() {
        return LabWork;
    }

    public void setLabWork(LabWorkDTO labWork) {
        LabWork = labWork;
    }


    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public boolean isForUpdate() {
        return forUpdate;
    }

    public void setForUpdate() {
        this.forUpdate = true;
    }

    public Object getObjectAnswer() {
        return objectAnswer;
    }

    public void setObjectAnswer(Object objectAnswer) {
        this.objectAnswer = objectAnswer;
    }
}
