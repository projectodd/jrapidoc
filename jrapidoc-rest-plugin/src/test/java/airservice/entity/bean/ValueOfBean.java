package airservice.entity.bean;

/**
 * Created by papa on 3.1.15.
 */
public class ValueOfBean {

    private String name;

    public static ValueOfBean valueOf(String s){
        ValueOfBean v = new ValueOfBean();
        v.name = s;
        return v;
    }
}
