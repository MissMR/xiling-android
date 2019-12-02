package com.dodomall.ddmall.shared.bean;

/**
 * created by Jigsaw at 2018/9/26
 */
public class LoginSwitchBean {

    /**
     * timestamp : 1535338835
     * flag : 0
     * token :
     */

    private long timestamp;
    // flag:业务逻辑启动开关(1:审核中;0:正常),int
    private int flag;
    private String token;

    public boolean isOpen() {
        return flag == 1;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
