package requests;

import entity.LabWorkDTO;

import java.io.Serializable;

/**
 * It's a class that represents a request to the server
 */
public class Request implements Serializable {
    private String command_name;
    private String login;
    private Object args;

    public Request() {

    }

    public String getLogin() {
        return login;
    }

    @Override
    public String toString() {
        return "request.Request{" +
                "command=" + command_name +
                ", args=" + args +
                '}';
    }
    public Request(String comm, Object arg) {
        command_name = comm;
        args = arg;
        login = "null";
    }
    
    public Request(String comm, Object arg, String login) {
        command_name = comm;
        args = arg;
        this.login = login;
    }
    
    public String getCommand_name() {
        return command_name;
    }

    public Object getArgs() {
        return args;
    }

    public void setLogin(String login){
        this.login = login;
    }

    public void setId(int id) {
        LabWorkDTO a = (LabWorkDTO) args;
        a.setId(id);
        args = a;
    }

}
