package com.huilianjk.bonus.controller;

import com.huilianjk.bonus.aop.UserInfo;
import com.huilianjk.bonus.pojo.Bonus;
import com.huilianjk.bonus.pojo.User;
import com.huilianjk.bonus.service.BonusService;
import com.huilianjk.bonus.handler.exceptions.DefException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;

/**
 * Created by space on 2017/10/15.
 */
@Controller
@RequestMapping("/bonus")
public class BonusController {

    @Autowired
    private BonusService bonusService;

    @RequestMapping("/{bonusId}")
    public String bonusDetail(@UserInfo User user,
                              @PathVariable Long bonusId, ModelMap modelMap) throws UnsupportedEncodingException{
//        if (user == null) {
//            return "redirect:/wx?target=" + URLEncoder.encode("/bonus/" + bonusId, "UTF-8");
//        }
        Bonus bonus = bonusService.getBonusById(bonusId);
        if (bonus == null) {
            throw new DefException("无此红包");
        }
        modelMap.addAttribute("platform", bonus.getPlatform());
        modelMap.addAttribute("sn", bonus.getSn());
        modelMap.addAttribute("bonusOwner", bonus.getOwnerName());
        modelMap.addAttribute("luckNumber", bonus.getLuckNumber());
        modelMap.addAttribute("currentCount", bonus.getCurrentCount());
        return "bonus_detail";
    }

    @RequestMapping("/addClickCount.json")
    @ResponseBody
    public String addClickCount(@UserInfo User user, @RequestParam Long bonusId) {
        Bonus bonus = bonusService.getBonusById(bonusId);
        bonusService.addClickCount(bonus, user);
        return "success";
    }

}
