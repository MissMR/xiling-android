package com.xiling.ddui.bean;

/**
 * created by Jigsaw at 2018/8/31
 */
public class HomeBuyOrShare {

    /**
     * event : link
     * target : https://www.baidu.com/?inviteCode=99988889
     * image : http://192.168.1.161/group1/M00/00/04/wKgBoVuGPESAVxHiAACd7ApNjDU940.jpg
     * type : share
     */

    private String event;
    private String target;
    private String image;
    private String type;

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
