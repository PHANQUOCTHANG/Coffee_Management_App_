package com.example.javafxapp.Model;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

public class Member {
    private int memberId ;
    private String memberPhone ;
    private int point ;

    private final BooleanProperty selected = new SimpleBooleanProperty(false);

    public Member() {
    }

    public Member(String memberPhone, int point) {
        this.memberPhone = memberPhone;
        this.point = point;
    }

    public Member(int memberId, String memberPhone, int point) {
        this.memberId = memberId;
        this.memberPhone = memberPhone;
        this.point = point;
    }

    public int getMemberId() {
        return memberId;
    }

    public void setMemberId(int memberId) {
        this.memberId = memberId;
    }

    public String getMemberPhone() {
        return memberPhone;
    }

    public void setMemberPhone(String memberPhone) {
        this.memberPhone = memberPhone;
    }

    public int getPoint() {
        return point;
    }

    public void setPoint(int point) {
        this.point = point;
    }

    public BooleanProperty selectedProperty() {
        return selected;
    }

    public boolean isSelected() {
        return selected.get();
    }

    public void setSelected(boolean selected) {
        this.selected.set(selected);
    }


}
