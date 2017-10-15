package com.huilianjk.bonus.controller;

import com.huilianjk.bonus.pojo.Bonus;
import com.huilianjk.bonus.service.BonusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by space on 2017/10/15.
 */
@Controller
@RequestMapping("/api")
public class APIController {
    @Autowired
    private BonusService bonusService;

    @RequestMapping("/add")
    @ResponseBody
    public String addNew(@RequestParam String platform,
                         @RequestParam String sn,
                         @RequestParam String ownerId,
                         @RequestParam String ownerName,
                         @RequestParam Integer luckNumber) {
        Bonus bonus = new Bonus();
        bonus.setPlatform(platform);
        bonus.setSn(sn);
        bonus.setOwnerId(ownerId);
        bonus.setOwnerName(ownerName);
        bonus.setLuckNumber(luckNumber);
        bonusService.add(bonus);
        return "success";
    }
}
