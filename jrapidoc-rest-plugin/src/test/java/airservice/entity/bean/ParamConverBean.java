package airservice.entity.bean;

/**
 * Created by papa on 3.1.15.
 */
public class ParamConverBean {

    private String name;

    public ParamConverBean(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
