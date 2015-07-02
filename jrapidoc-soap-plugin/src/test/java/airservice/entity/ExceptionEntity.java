/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package airservice.entity;

/**
 *
 * @author Tomas "sarzwest" Jiricek
 */
public class ExceptionEntity {
    
    public String desc;
    public int number;
    public boolean something;

    public ExceptionEntity() {
        desc = "description";
        number = Integer.MAX_VALUE;
        something = true;
    }

    public ExceptionEntity(String desc, int number, boolean something) {
        this.desc = desc;
        this.number = number;
        this.something = something;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public void setSomething(boolean something) {
        this.something = something;
    }
    
    
    
}
