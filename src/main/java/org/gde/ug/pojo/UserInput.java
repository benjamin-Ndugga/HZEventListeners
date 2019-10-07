package org.gde.ug.pojo;

import java.io.Serializable;

/**
 *
 * @author Benjamin E Ndugga
 */
public class UserInput implements Comparable<UserInput>, Serializable {

    private int itemNo;
    private String displayDesc;
    private String itemDesc;

    public UserInput(int itemNo, String displayDesc, String itemDesc) {
        this.itemNo = itemNo;
        this.displayDesc = displayDesc;
        this.itemDesc = itemDesc;
    }

    public UserInput() {
    }

    public int getItemNo() {
        return itemNo;
    }

    public void setItemNo(int itemNo) {
        this.itemNo = itemNo;
    }

    public String getItemDesc() {
        return itemDesc;
    }

    public void setItemDesc(String itemDesc) {
        this.itemDesc = itemDesc;
    }

    @Override
    public String toString() {
        return displayDesc + ":" + itemDesc;
    }

    @Override
    public int compareTo(UserInput userInput) {

        if (userInput.getItemNo() == getItemNo()) {
            return 0;
        } else if (userInput.getItemNo() > getItemNo()) {
            return -1;
        } else {
            return 1;
        }
    }

}
