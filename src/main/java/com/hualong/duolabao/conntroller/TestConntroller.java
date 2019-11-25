package com.hualong.duolabao.conntroller;

import com.alibaba.fastjson.JSONObject;
import com.hualong.duolabao.service.DlbService;
import com.hualong.duolabao.service.PosService;
import org.apache.catalina.Store;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by Administrator on 2019-07-23.
 */
@Controller
@RequestMapping("/layUiTest")
public class TestConntroller {
    private static final Logger log= LoggerFactory.getLogger(TestConntroller.class);

    @Autowired
    private DlbService dlbService;

    @Autowired
    private PosService posService;

    @RequestMapping("/home")
    public String index(){
        return "pages/index";
    }

    @RequestMapping("/vippage")
    public String vippage(){
        return "pages/vippage";
    }

    //
    @RequestMapping("/test")
    public String test123(){
        return "pages/test";
    }

    @RequestMapping("/getJson")
    @ResponseBody
    public String getJson(){
       String s= "{\n" +
                "    \"code\": 0,\n" +
                "    \"msg\": \"\",\n" +
                "    \"count\": 3000000,\n" +
                "    \"data\": [\n" +
                "        {\n" +
                "            \"id\": \"10001\",\n" +
                "            \"username\": \"杜甫\",\n" +
                "            \"email\": \"xianxin@layui.com\",\n" +
                "            \"sex\": \"男\",\n" +
                "            \"city\": \"浙江杭州\",\n" +
                "            \"sign\": \"点击此处，显示更多。当内容超出时，点击单元格会自动显示更多内容。\",\n" +
                "            \"experience\": \"116\",\n" +
                "            \"ip\": \"192.168.0.8\",\n" +
                "            \"logins\": \"108\",\n" +
                "            \"joinTime\": \"2016-10-14\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"id\": \"10002\",\n" +
                "            \"username\": \"李白\",\n" +
                "            \"email\": \"xianxin@layui.com\",\n" +
                "            \"sex\": \"男\",\n" +
                "            \"city\": \"浙江杭州\",\n" +
                "            \"sign\": \"君不见，黄河之水天上来，奔流到海不复回。 君不见，高堂明镜悲白发，朝如青丝暮成雪。 人生得意须尽欢，莫使金樽空对月。 天生我材必有用，千金散尽还复来。 烹羊宰牛且为乐，会须一饮三百杯。 岑夫子，丹丘生，将进酒，杯莫停。 与君歌一曲，请君为我倾耳听。(倾耳听 一作：侧耳听) 钟鼓馔玉不足贵，但愿长醉不复醒。(不足贵 一作：何足贵；不复醒 一作：不愿醒/不用醒) 古来圣贤皆寂寞，惟有饮者留其名。(古来 一作：自古；惟 通：唯) 陈王昔时宴平乐，斗酒十千恣欢谑。 主人何为言少钱，径须沽取对君酌。 五花马，千金裘，呼儿将出换美酒，与尔同销万古愁。\",\n" +
                "            \"experience\": \"12\",\n" +
                "            \"ip\": \"192.168.0.8\",\n" +
                "            \"logins\": \"106\",\n" +
                "            \"joinTime\": \"2016-10-14\",\n" +
                "            \"LAY_CHECKED\": true\n" +
                "        },\n" +
                "        {\n" +
                "            \"id\": \"10003\",\n" +
                "            \"username\": \"王勃\",\n" +
                "            \"email\": \"xianxin@layui.com\",\n" +
                "            \"sex\": \"男\",\n" +
                "            \"city\": \"浙江杭州\",\n" +
                "            \"sign\": \"人生恰似一场修行\",\n" +
                "            \"experience\": \"65\",\n" +
                "            \"ip\": \"192.168.0.8\",\n" +
                "            \"logins\": \"106\",\n" +
                "            \"joinTime\": \"2016-10-14\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"id\": \"10004\",\n" +
                "            \"username\": \"李清照\",\n" +
                "            \"email\": \"xianxin@layui.com\",\n" +
                "            \"sex\": \"女\",\n" +
                "            \"city\": \"浙江杭州\",\n" +
                "            \"sign\": \"人生恰似一场修行\",\n" +
                "            \"experience\": \"666\",\n" +
                "            \"ip\": \"192.168.0.8\",\n" +
                "            \"logins\": \"106\",\n" +
                "            \"joinTime\": \"2016-10-14\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"id\": \"10005\",\n" +
                "            \"username\": \"冰心\",\n" +
                "            \"email\": \"xianxin@layui.com\",\n" +
                "            \"sex\": \"女\",\n" +
                "            \"city\": \"浙江杭州\",\n" +
                "            \"sign\": \"人生恰似一场修行\",\n" +
                "            \"experience\": \"86\",\n" +
                "            \"ip\": \"192.168.0.8\",\n" +
                "            \"logins\": \"106\",\n" +
                "            \"joinTime\": \"2016-10-14\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"id\": \"10006\",\n" +
                "            \"username\": \"贤心\",\n" +
                "            \"email\": \"xianxin@layui.com\",\n" +
                "            \"sex\": \"男\",\n" +
                "            \"city\": \"浙江杭州\",\n" +
                "            \"sign\": \"人生恰似一场修行\",\n" +
                "            \"experience\": \"12\",\n" +
                "            \"ip\": \"192.168.0.8\",\n" +
                "            \"logins\": \"106\",\n" +
                "            \"joinTime\": \"2016-10-14\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"id\": \"10007\",\n" +
                "            \"username\": \"贤心\",\n" +
                "            \"email\": \"xianxin@layui.com\",\n" +
                "            \"sex\": \"男\",\n" +
                "            \"city\": \"浙江杭州\",\n" +
                "            \"sign\": \"人生恰似一场修行\",\n" +
                "            \"experience\": \"16\",\n" +
                "            \"ip\": \"192.168.0.8\",\n" +
                "            \"logins\": \"106\",\n" +
                "            \"joinTime\": \"2016-10-14\"\n" +
                "        },\n" +
               "        {\n" +
               "            \"id\": \"10007\",\n" +
               "            \"username\": \"贤心\",\n" +
               "            \"email\": \"xianxin@layui.com\",\n" +
               "            \"sex\": \"男\",\n" +
               "            \"city\": \"浙江杭州\",\n" +
               "            \"sign\": \"人生恰似一场修行\",\n" +
               "            \"experience\": \"16\",\n" +
               "            \"ip\": \"192.168.0.8\",\n" +
               "            \"logins\": \"106\",\n" +
               "            \"joinTime\": \"2016-10-14\"\n" +
               "        },\n" +
               "        {\n" +
               "            \"id\": \"10007\",\n" +
               "            \"username\": \"贤心\",\n" +
               "            \"email\": \"xianxin@layui.com\",\n" +
               "            \"sex\": \"男\",\n" +
               "            \"city\": \"浙江杭州\",\n" +
               "            \"sign\": \"人生恰似一场修行\",\n" +
               "            \"experience\": \"16\",\n" +
               "            \"ip\": \"192.168.0.8\",\n" +
               "            \"logins\": \"106\",\n" +
               "            \"joinTime\": \"2016-10-14\"\n" +
               "        },\n" +
               "        {\n" +
               "            \"id\": \"10007\",\n" +
               "            \"username\": \"贤心\",\n" +
               "            \"email\": \"xianxin@layui.com\",\n" +
               "            \"sex\": \"男\",\n" +
               "            \"city\": \"浙江杭州\",\n" +
               "            \"sign\": \"人生恰似一场修行\",\n" +
               "            \"experience\": \"16\",\n" +
               "            \"ip\": \"192.168.0.8\",\n" +
               "            \"logins\": \"106\",\n" +
               "            \"joinTime\": \"2016-10-14\"\n" +
               "        },\n" +
               "        {\n" +
               "            \"id\": \"10007\",\n" +
               "            \"username\": \"贤心\",\n" +
               "            \"email\": \"xianxin@layui.com\",\n" +
               "            \"sex\": \"男\",\n" +
               "            \"city\": \"浙江杭州\",\n" +
               "            \"sign\": \"人生恰似一场修行\",\n" +
               "            \"experience\": \"16\",\n" +
               "            \"ip\": \"192.168.0.8\",\n" +
               "            \"logins\": \"106\",\n" +
               "            \"joinTime\": \"2016-10-14\"\n" +
               "        },\n" +
               "        {\n" +
               "            \"id\": \"10007\",\n" +
               "            \"username\": \"贤心\",\n" +
               "            \"email\": \"xianxin@layui.com\",\n" +
               "            \"sex\": \"男\",\n" +
               "            \"city\": \"浙江杭州\",\n" +
               "            \"sign\": \"人生恰似一场修行\",\n" +
               "            \"experience\": \"16\",\n" +
               "            \"ip\": \"192.168.0.8\",\n" +
               "            \"logins\": \"106\",\n" +
               "            \"joinTime\": \"2016-10-14\"\n" +
               "        },\n" +
               "        {\n" +
               "            \"id\": \"10007\",\n" +
               "            \"username\": \"贤心\",\n" +
               "            \"email\": \"xianxin@layui.com\",\n" +
               "            \"sex\": \"男\",\n" +
               "            \"city\": \"浙江杭州\",\n" +
               "            \"sign\": \"人生恰似一场修行\",\n" +
               "            \"experience\": \"16\",\n" +
               "            \"ip\": \"192.168.0.8\",\n" +
               "            \"logins\": \"106\",\n" +
               "            \"joinTime\": \"2016-10-14\"\n" +
               "        },\n" +
               "        {\n" +
               "            \"id\": \"10007\",\n" +
               "            \"username\": \"贤心\",\n" +
               "            \"email\": \"xianxin@layui.com\",\n" +
               "            \"sex\": \"男\",\n" +
               "            \"city\": \"浙江杭州\",\n" +
               "            \"sign\": \"人生恰似一场修行\",\n" +
               "            \"experience\": \"16\",\n" +
               "            \"ip\": \"192.168.0.8\",\n" +
               "            \"logins\": \"106\",\n" +
               "            \"joinTime\": \"2016-10-14\"\n" +
               "        },\n" +
               "        {\n" +
               "            \"id\": \"10007\",\n" +
               "            \"username\": \"贤心\",\n" +
               "            \"email\": \"xianxin@layui.com\",\n" +
               "            \"sex\": \"男\",\n" +
               "            \"city\": \"浙江杭州\",\n" +
               "            \"sign\": \"人生恰似一场修行\",\n" +
               "            \"experience\": \"16\",\n" +
               "            \"ip\": \"192.168.0.8\",\n" +
               "            \"logins\": \"106\",\n" +
               "            \"joinTime\": \"2016-10-14\"\n" +
               "        },\n" +
                "        {\n" +
                "            \"id\": \"10008\",\n" +
                "            \"username\": \"贤心\",\n" +
                "            \"email\": \"xianxin@layui.com\",\n" +
                "            \"sex\": \"男\",\n" +
                "            \"city\": \"浙江杭州\",\n" +
                "            \"sign\": \"人生恰似一场修行\",\n" +
                "            \"experience\": \"106\",\n" +
                "            \"ip\": \"192.168.0.8\",\n" +
                "            \"logins\": \"106\",\n" +
                "            \"joinTime\": \"2016-10-14\"\n" +
                "        }\n" +
                "    ]\n" +
                "}";
       return s;
//        return "{\n" +
//                "    \"code\": 0,\n" +
//                "    \"msg\": \"数据集为空\",\n" +
//                "    \"count\": 0,\n" +
//                "    \"data\": [\n" +
//                "    ]\n" +
//                "}";
    }

    @RequestMapping(value = "/api/updateVersion",method = RequestMethod.GET)
    @ResponseBody
    public  String updateVersion(){
        return "你好啊";
    }



    /**
     *<pre>
     *     搭建项目测试用的
     *</pre>
     * @param cStoreNo       门店编号
     * @param cStoreName     门店名称
     * @return                门店集合
     */
    @RequestMapping(value = "/api/getStore", method = RequestMethod.POST)
    @ResponseBody
    public  String findStore(@RequestParam(value = "cStoreNo",required = false) String cStoreNo,
                             @RequestParam(value = "cStoreName",required = false) String cStoreName){
        try{
            List<Store> list =dlbService.get_cStroeS(cStoreNo);
            String result="";
            if(list!=null && !list.isEmpty()){
                try{
//                    result= listBean.getBeanJson(list);
                    result= JSONObject.toJSONString(list);
                    return result;
                }catch (Exception e){
                    e.printStackTrace();
                    log.info(e.getMessage());
                    return e.getMessage();
                }

            }else {
                return "";
            }
        }catch (Exception e){
            e.printStackTrace();
            log.error(e.getMessage());
            return e.getMessage();
        }
    }
}
