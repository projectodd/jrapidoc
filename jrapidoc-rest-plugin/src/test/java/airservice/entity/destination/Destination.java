/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package airservice.entity.destination;

/**
 *
 * @author Tomas "sarzwest" Jiricek
 */

public class Destination{
    @Override
	public String toString() {
		return "Destination [name=" + name + "]";
	}

    /**
     * toto je jmeno
     */
	private String name;

    public Destination() {
    }

    public Destination(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
