package com.pjh5365.servermanagementpageapp;
//ListView 에 들어갈 아이템 데이터
public class ListViewItem {
    private String title, userID, listNum;

    public ListViewItem() {
    }

    public ListViewItem(String title, String userID, String listNum) {
        this.title = title;
        this.userID = userID;
        this.listNum = listNum;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getListNum() {
        return listNum;
    }

    public void setListNum(String listNum) {
        this.listNum = listNum;
    }
}
