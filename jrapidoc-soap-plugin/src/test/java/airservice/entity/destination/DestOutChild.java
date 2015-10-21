/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package airservice.entity.destination;

import airservice.entity.ObjectInterface;

public class DestOutChild extends DestinationOutput implements ObjectInterface {

    public String varInDestChild = "inDestChild";

    public DestOutChild(String varInDestChild) {
        this.varInDestChild = varInDestChild;
    }

    public DestOutChild() {
    }
}
