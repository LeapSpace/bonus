package com.huilianjk.bonus.pojo;

/**
 * Created by space on 2017/10/15.
 */
public class ClickState {
    private long clickId;
    private long bonusId;
    private long userId;

    public ClickState(long bonusId, long userId) {
        this.bonusId = bonusId;
        this.userId = userId;
    }

    public long getClickId() {
        return clickId;
    }

    public void setClickId(long clickId) {
        this.clickId = clickId;
    }

    public long getBonusId() {
        return bonusId;
    }

    public void setBonusId(long bonusId) {
        this.bonusId = bonusId;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }
}
