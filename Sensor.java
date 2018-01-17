import java.io.Serializable;

/**
 * Created by 15072935 on 04/12/2017.
 */

public class Sensor implements Serializable {
    private String name;
    private String value;
    private String time;
    public Sensor(String name, String value, String time) {
        this.name = name;
        this.value = value;
        this.time = time;
    }
    public void setName(String name) {this.name = name;}
    public String getName() {return name;}

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
