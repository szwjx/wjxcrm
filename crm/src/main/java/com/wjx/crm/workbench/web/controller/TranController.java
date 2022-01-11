package com.wjx.crm.workbench.web.controller;

import com.wjx.crm.settings.domain.User;
import com.wjx.crm.settings.service.UserService;
import com.wjx.crm.settings.service.impl.UserServiceImpl;
import com.wjx.crm.utils.DateTimeUtil;
import com.wjx.crm.utils.PrintJson;
import com.wjx.crm.utils.ServiceFactory;
import com.wjx.crm.utils.UUIDUtil;
import com.wjx.crm.vo.PaginationVO;
import com.wjx.crm.workbench.domain.*;
import com.wjx.crm.workbench.service.ActivityService;
import com.wjx.crm.workbench.service.ContactsService;
import com.wjx.crm.workbench.service.CustomerService;
import com.wjx.crm.workbench.service.TranService;
import com.wjx.crm.workbench.service.impl.ActivityServiceImpl;
import com.wjx.crm.workbench.service.impl.ContactsServiceImpl;
import com.wjx.crm.workbench.service.impl.CustomerServiceImpl;
import com.wjx.crm.workbench.service.impl.TranServiceImpl;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class TranController extends HttpServlet {

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        System.out.println("进入到交易控制器");

        String path = request.getServletPath();

        if("/workbench/transaction/getUserList.do".equals(path)){
            getUserList(request,response);
        }else  if("/workbench/transaction/getActivityListByName.do".equals(path)){
            getActivityListByName(request,response);
        }else  if("/workbench/transaction/getContactsListByName.do".equals(path)){
            getContactsListByName(request,response);
        }else  if("/workbench/transaction/getCustomerName.do".equals(path)){
            getCustomerName(request,response);
        }else  if("/workbench/transaction/save.do".equals(path)){
            save(request,response);
        }else  if("/workbench/transaction/pageList.do".equals(path)){
            pageList(request,response);
        }else  if("/workbench/transaction/edit.do".equals(path)){
            edit(request,response);
        }else  if("/workbench/transaction/update.do".equals(path)){
            update(request,response);
        }else  if("/workbench/transaction/delete.do".equals(path)){
            delete(request,response);
        }else  if("/workbench/transaction/detail.do".equals(path)){
            detail(request,response);
        }
    }

    private void detail(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        System.out.println("跳转到详细信息页操作");

        String id = request.getParameter("id");

        TranService ts = (TranService) ServiceFactory.getService(new TranServiceImpl());

         Tran t = ts.detail(id);


          //      处理可能性
         String stage = t.getStage();
         //ServletContext appllication = this.getServletContext();
        Map<String,String> pmap = (Map<String, String>) this.getServletContext().getAttribute("pmap");
        String possibility = pmap.get(stage);
         //或者
        //ServletContext appllication1 = request.getServletContext();
        //ServletContext appllication2 = this.getServletConfig.getServletContext();

        t.setPossibility(possibility);//这里是在Tran中扩充了possibility字段

         request.setAttribute("t",t);
        //request.setAttribute("possibility",possibility);
         request.getRequestDispatcher("/workbench/transaction/detail.jsp").forward(request,response);
    }

    private void delete(HttpServletRequest request, HttpServletResponse response) {

        System.out.println("删除交易操作");

        String[] ids = request.getParameterValues("id");

        TranService ts = (TranService) ServiceFactory.getService(new TranServiceImpl());

        boolean flag = ts.deleteTransaction(ids);

        PrintJson.printJsonFlag(response,flag);
    }

    private void update(HttpServletRequest request, HttpServletResponse response) throws IOException {

        System.out.println("执行交易修改操作");

        String id = request.getParameter("id");
        String owner = request.getParameter("owner");
        String money = request.getParameter("money");
        String name = request.getParameter("name");
        String expectedDate = request.getParameter("expectedDate");
        String customerId = request.getParameter("customerId");
        String stage = request.getParameter("stage");
        String type = request.getParameter("type");
        String source = request.getParameter("source");
        String activityId = request.getParameter("activityId");
        String contactsId = request.getParameter("contactsId");
        //修改时间，当前系统时间
        String editTime = DateTimeUtil.getSysTime();
        //修改人，当前登录用户
        String editBy = ((User)request.getSession().getAttribute("user")).getName();
        String description = request.getParameter("description");
        String contactSummary = request.getParameter("contactSummary");
        String nextContactTime = request.getParameter("nextContactTime");

        Tran t = new Tran();
        t.setId(id);
        t.setOwner(owner);
        t.setMoney(money);
        t.setName(name);
        t.setExpectedDate(expectedDate);
        t.setStage(stage);
        t.setType(type);
        t.setSource(source);
        t.setEditTime(editTime);
        t.setEditBy(editBy);
        t.setContactSummary(contactSummary);
        t.setDescription(description);
        t.setNextContactTime(nextContactTime);
        t.setCustomerId(customerId);
        t.setContactsId(contactsId);
        t.setActivityId(activityId);

        TranService ts = (TranService) ServiceFactory.getService(new TranServiceImpl());

        boolean flag = ts.update(t);

        //PrintJson.printJsonFlag(response,flag);
        response.sendRedirect(request.getContextPath() + "/workbench/transaction/index.jsp");
    }

    private void edit(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        System.out.println("跳转到修改交易页面操作");

        String id = request.getParameter("id");

        //获得所有者下拉框里的内容
        UserService userService= (UserService) ServiceFactory.getService(new UserServiceImpl());
        List<User> uList=userService.getUserList();
        request.setAttribute("uList",uList);

        //获得当前修改交易的详细信息
        TranService ts = (TranService) ServiceFactory.getService(new TranServiceImpl());
        Tran t = ts.getTranById(id);
        request.setAttribute("t",t);

        //获得当前交易的活动名称和联系人id
        TranService ts1 = (TranService) ServiceFactory.getService(new TranServiceImpl());
        Tran t1 = ts1.getAidAndCidByTid(id);
        request.setAttribute("t1",t1);


        request.getRequestDispatcher("/workbench/transaction/edit.jsp").forward(request,response);

    }

    private void pageList(HttpServletRequest request, HttpServletResponse response) {

        System.out.println("查询交易信息列表（结合条件查询和分页查询）");

        String name = request.getParameter("name");
        String owner = request.getParameter("owner");
        String customerId = request.getParameter("customerId");
        String stage = request.getParameter("stage");
        String type = request.getParameter("type");
        String source = request.getParameter("source");
        String contactsId = request.getParameter("contactsId");
        String pageNoStr = request.getParameter("pageNo");
        int pageNo = Integer.valueOf(pageNoStr);
        //每页展现的记录数
        String pageSizeStr = request.getParameter("pageSize");
        int pageSize = Integer.valueOf(pageSizeStr);
        //计算略过的记录数
        int skipCount = (pageNo-1)*pageSize;

        Map<String,Object> map = new HashMap<String,Object>();
        map.put("name",name);
        map.put("owner",owner);
        map.put("customerId",customerId);
        map.put("stage",stage);
        map.put("type",type);
        map.put("source",source);
        map.put("contactsId",contactsId);
        map.put("pageSize",pageSize);
        map.put("skipCount",skipCount);

        TranService ts = (TranService) ServiceFactory.getService(new TranServiceImpl());

        PaginationVO<Tran> vo = ts.pageList(map);

        //自动把vo--->{"total":100,"dataList":[{交易1},{2},{3}]}
        PrintJson.printJsonObj(response,vo);
    }

    private void save(HttpServletRequest request, HttpServletResponse response) throws IOException {

        System.out.println("执行添加交易的操作");

        String id = UUIDUtil.getUUID();
        String owner = request.getParameter("owner");
        String money = request.getParameter("money");
        String name = request.getParameter("name");
        String expectedDate = request.getParameter("expectedDate");
        String customerName = request.getParameter("customerName");
        String stage = request.getParameter("stage");
        String type = request.getParameter("type");
        String source = request.getParameter("source");
        String activityId = request.getParameter("activityId");
        String contactsId = request.getParameter("contactsId");
        String createBy = ((User)request.getSession().getAttribute("user")).getName();
        String createTime = DateTimeUtil.getSysTime();
        String description = request.getParameter("description");
        String contactSummary = request.getParameter("contactSummary");
        String nextContactTime = request.getParameter("nextContactTime");

        Tran t = new Tran();
        t.setId(id);
        t.setOwner(owner);
        t.setMoney(money);
        t.setName(name);
        t.setExpectedDate(expectedDate);
        t.setStage(stage);
        t.setType(type);
        t.setSource(source);
        t.setActivityId(activityId);
        t.setContactsId(contactsId);
        t.setCreateBy(createBy);
        t.setCreateTime(createTime);
        t.setDescription(description);
        t.setContactSummary(contactSummary);
        t.setNextContactTime(nextContactTime);

        TranService ts = (TranService) ServiceFactory.getService(new TranServiceImpl());

        boolean flag = ts.save(t,customerName);

        if (flag){

            //如果添加交易成功，重定向跳转到列表页
            //这里只能使用重定向。因为若是使用请求转发，跳到index页面。但是地址栏仍旧是现在的save。do.然后你再刷新页面的
            //时候，它仍旧会进行save的操作，导致重复保存交易
            response.sendRedirect(request.getContextPath() + "/workbench/transaction/index.jsp");

            //一般request域存值用转发，而且转发路径会保留在老路径。。。。/save.do上，因此不使用转发
            //request.getRequestDispatcher("/workbench/transaction/index.jsp").forward(request,response);

        }

    }

    private void getCustomerName(HttpServletRequest request, HttpServletResponse response) {

        System.out.println("取得客户名称列表（按照客户名称进行模糊查询）");

        String name = request.getParameter("name");

        CustomerService cs = (CustomerService) ServiceFactory.getService(new CustomerServiceImpl());

        List<String> sList = cs.getCustomerName(name);

        PrintJson.printJsonObj(response,sList);
    }

    private void getContactsListByName(HttpServletRequest request, HttpServletResponse response) {

        System.out.println("查询联系人列表（根据名称模糊查）");

        String cname = request.getParameter("cname");

        ContactsService cs = (ContactsService) ServiceFactory.getService(new ContactsServiceImpl());

        List<Contacts> cList = cs.getContactsListByName(cname);

        PrintJson.printJsonObj(response,cList);
    }

    private void getActivityListByName(HttpServletRequest request, HttpServletResponse response) {

        System.out.println("查询市场活动列表（根据名称模糊查）");

        String aname = request.getParameter("aname");

        ActivityService as = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());

        List<Activity> aList = as.getActivityListByName(aname);

        PrintJson.printJsonObj(response,aList);
    }

    private void getUserList(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        System.out.println("取得用户信息列表,跳转到交易添加页");

        UserService us = (UserService) ServiceFactory.getService(new UserServiceImpl());

        List<User> uList = us.getUserList();

        request.setAttribute("uList",uList);

        request.getRequestDispatcher("/workbench/transaction/save.jsp").forward(request,response);
    }


}
