/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package airservice.entity.destination;

import com.fasterxml.jackson.annotation.JsonPropertyDescription;

/**
 *
 * @author Tomas "sarzwest" Jiricek
 */
public class DestinationEntity extends Destination {
    
    private static long idGenerator;

    @JsonPropertyDescription("Description of id")
    private long id;
    private String uri;

    public DestinationEntity(String name) {
        super(name);
        this.id = ++idGenerator;
    }

    public long getId() {
        return id;
    }

    public String getUri() {
        return uri;
    }

    public final void setUri(String uri) {
        this.uri = uri;
    }
}
