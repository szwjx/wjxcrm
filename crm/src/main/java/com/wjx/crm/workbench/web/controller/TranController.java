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
        }
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
