package airservice.entity.bean;

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
