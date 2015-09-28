package airservice.entity.destination;

import airservice.entity.ObjectInterface;

import java.util.List;

/**
 * Created by papa on 25.4.15.
 */
public class D1<T1 extends Destination & ObjectInterface, T2 extends T1>{//proto getbounds  muze vracet vicero ohraniceni

    public T1 destination;
    public T2 object;
}
