package com.projectcarlton.fbljk.projectcarlton.Data;

public class Invite {

    public String inviteId;
    public String groupName;
    public String senderName;

    public Invite() {}

    public Invite(String inviteId, String groupName, String senderName) {
        this.inviteId = inviteId;
        this.groupName = groupName;
        this.senderName = senderName;
    }
}