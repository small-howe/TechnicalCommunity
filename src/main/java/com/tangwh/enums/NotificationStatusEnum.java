package com.tangwh.enums;

public enum NotificationStatusEnum {
    UNREAD(0), READ(1);
    //通知状态(已读和未读)
    private int status;

    public int getStatus() {
        return status;
    }

    NotificationStatusEnum(int status) {
        this.status = status;
    }
}
