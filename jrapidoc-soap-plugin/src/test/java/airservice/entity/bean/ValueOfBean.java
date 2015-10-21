package airservice.entity.bean;

public class ValueOfBean {

    private String name;

    public static ValueOfBean valueOf(String s){
        ValueOfBean v = new ValueOfBean();
        v.name = s;
        return v;
    }
}
