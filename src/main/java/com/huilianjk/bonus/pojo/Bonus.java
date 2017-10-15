package com.huilianjk.bonus.pojo;

/**
 * Created by space on 2017/10/15.
 */
public class Bonus {
    private long bonusId;
    private String platform;
    private String sn;
    private String ownerId;
    private String ownerName;
    private int luckNumber;
    private int currentCount;

    public long getBonusId() {
        return bonusId;
    }

    public void setBonusId(long bonusId) {
        this.bonusId = bonusId;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public int getLuckNumber() {
        return luckNumber;
    }

    public void setLuckNumber(int luckNumber) {
        this.luckNumber = luckNumber;
    }

    public int getCurrentCount() {
        return currentCount;
    }

    public void setCurrentCount(int currentCount) {
        this.currentCount = currentCount;
    }
}
