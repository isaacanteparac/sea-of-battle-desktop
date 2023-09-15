package com.iac.shipwar.models.enums;

public enum Column {
    C1(0),
    C2(1),
    C3(2),
    C4(3),
    C5(4),
    C6(5),
    C7(6),
    C8(7);

    private final int index;

    Column(int index){
        this.index = index;
    }

    public int getIndex() {
        return index;
    }

}
