/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package airservice.entity.destination;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author Tomas "sarzwest" Jiricek
 */
@XmlRootElement(name = "destination")
public class DestinationOutput extends Destination {

    /**
     * toto je name
     */
    public String neco = "public";
    @JsonIgnore
    private long id;
    private String uri = "toto je uri";
    private List<Destination> list = new ArrayList<Destination>(Arrays.asList(new Destination[]{new DestinationEntity("a"), new DestinationEntity("b")}));

    public DestinationOutput() {
    }

    public DestinationOutput(DestinationEntity destination) {
        super(destination.getName());
        this.id = destination.getId();
        this.uri = destination.getUri();
    }

    @XmlElement(required = true)
    @Override
    public String getName() {
        return super.getName();
    }



    public List<Destination> getList() {
		return list;
	}

    public String getUri() {
        return uri;
    }

    public final void setUri(String uri) {
        this.uri = uri;
    }

    public DestinationOutput setId(long id) {
        this.id = id;
        return this;
    }

    @XmlTransient
    public long getId() {
        return id;
    }

	@Override
	public String toString() {
		return "DestinationOutput [id=" + id + ", uri=" + uri + "]";
	}
}
