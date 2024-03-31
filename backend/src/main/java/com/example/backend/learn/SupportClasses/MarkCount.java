package com.example.backend.learn.SupportClasses;

public class MarkCount {
    private final int mark_zero;
    private final int mark_one;
    private final int mark_two;
    private final int mark_three;
    private final int mark_four;
    private final int mark_five;
    
    public MarkCount(int mark_zero, int mark_one, int mark_two, int mark_three, int mark_four, int mark_five) {
        this.mark_zero = mark_zero;
        this.mark_one = mark_one;
        this.mark_two = mark_two;
        this.mark_three = mark_three;
        this.mark_four = mark_four;
        this.mark_five = mark_five;
    }

    public int getMark_zero() {
        return mark_zero;
    }

    public int getMark_one() {
        return mark_one;
    }

    public int getMark_two() {
        return mark_two;
    }

    public int getMark_three() {
        return mark_three;
    }

    public int getMark_four() {
        return mark_four;
    }

    public int getMark_five() {
        return mark_five;
    }

    

}
