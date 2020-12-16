package com.syzton.scoket.producer.control;

import com.syzton.scoket.producer.bean.MsgBean;
import com.syzton.scoket.producer.rbbitmq.RabbitProduct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @program: teaching
 * @description:
 * @author: fengxx
 * @create: 2020-12-15 17:18
 **/
@Controller
@RequestMapping("/")
public class JspControl {

    @Autowired
    private RabbitProduct product;

    @RequestMapping("/index")
    public String index(){
        return "index";
    }


    @ResponseBody
    @RequestMapping("send")
    public String sendMessage(@RequestParam String msg, String userId){

        MsgBean bean = new MsgBean();
        bean.setMsg(msg);
        bean.setUserId(userId);
        product.sendMSG(bean);

        return "发送成功";
    }
}
