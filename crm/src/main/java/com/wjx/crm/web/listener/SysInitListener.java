package com.wjx.crm.web.listener;

import com.wjx.crm.settings.domain.DicValue;
import com.wjx.crm.settings.service.DicService;
import com.wjx.crm.settings.service.impl.DicServiceImpl;
import com.wjx.crm.utils.ServiceFactory;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class SysInitListener implements ServletContextListener {

    /*
        该方法是用来监听上下文域对象的方法，当服务器启动，上下文域对象创建
        对象创建完毕后立即执行该方法

        event:该参数能够取得监听的对象
                监听的是什么对象，就可以通过该参数能取得什么对象
                例如：现在监听的是上下文域对象，通过该参数就可以取得上下文域对象

     */


    public void contextInitialized(ServletContextEvent event) {

        System.out.println("服务器缓存处理数据字典开始");

        ServletContext application = event.getServletContext();

        DicService ds = (DicService) ServiceFactory.getService(new DicServiceImpl());

        /*
            从业务层获取七个List
            可以打包为一个map
                业务层：map.put("applicationList",dvList1);
                      map.put("StateList",dvList2);
                      map.put("StageList",dvList3);
                      ....
         */

        Map<String, List<DicValue>> map = ds.getAll();
        //将map解析为上下文域对象中保存的键值对
        Set<String> set = map.keySet();
        for (String key:set){

            application.setAttribute(key,map.get(key));
        }
        System.out.println("服务器缓存处理数据字典结束");





    }
}
