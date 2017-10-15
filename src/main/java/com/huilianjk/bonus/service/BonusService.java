package com.huilianjk.bonus.service;

import com.huilianjk.bonus.dao.BonusMapper;
import com.huilianjk.bonus.dao.ClickStateMapper;
import com.huilianjk.bonus.pojo.Bonus;
import com.huilianjk.bonus.pojo.ClickState;
import com.huilianjk.bonus.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by space on 2017/10/15.
 */
@Service
public class BonusService {
    @Autowired
    private BonusMapper bonusMapper;

    @Autowired
    private ClickStateMapper clickStateMapper;

    public void add(Bonus bonus) {
        Bonus ifExist = bonusMapper.getBonusByPlatformAndSn(bonus.getPlatform(), bonus.getSn());
        if (ifExist != null) {
            return;
        }
        bonusMapper.insert(bonus);
    }

    public Bonus getBonusById(long bonusId) {
        return bonusMapper.getBonusById(bonusId);
    }

    @Transactional
    public void addClickCount(Bonus bonus, User user) {
        ClickState clickState = new ClickState(bonus.getBonusId(), user.getUserId());
        clickStateMapper.insert(clickState);
        bonusMapper.addClickCount(bonus.getBonusId());
    }
}
