package com.projectcarlton.fbljk.projectcarlton.Data;

public class Group {

    public String groupId;
    public String groupName;
    public String groupDescription;
    public String groupAdmin;
    public String groupPhoto;

    public Group() {}

    public Group(String groupId, String groupName, String groupDescription, String groupAdmin, String groupPhoto) {
        this.groupId = groupId;
        this.groupName = groupName;
        this.groupDescription = groupDescription;
        this.groupAdmin = groupAdmin;
        this.groupPhoto = groupPhoto;
    }

}