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
import com.wjx.crm.workbench.service.ClueService;
import com.wjx.crm.workbench.service.ContactsService;
import com.wjx.crm.workbench.service.CustomerService;
import com.wjx.crm.workbench.service.TranService;
import com.wjx.crm.workbench.service.impl.ClueServiceImpl;
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


public class CustomerController extends HttpServlet {

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        System.out.println("进入到客户控制器");

        String path = request.getServletPath();

        if("/workbench/customer/getUserList.do".equals(path)){
            getUserList(request,response);
        }else  if("/workbench/customer/save.do".equals(path)){
            save(request,response);
        }else  if("/workbench/customer/pageList.do".equals(path)){
            pageList(request,response);
        }else  if("/workbench/customer/delete.do".equals(path)){
            delete(request,response);
        }else  if("/workbench/customer/getUserListAndCustomer.do".equals(path)){
            getUserListAndCustomer(request,response);
        }else  if("/workbench/customer/update.do".equals(path)){
            update(request,response);
        }else  if("/workbench/customer/detail.do".equals(path)){
            detail(request,response);
        }else  if("/workbench/customer/getRemarkListByCid.do".equals(path)){
            getRemarkListByCid(request,response);
        }else  if("/workbench/customer/deleteRemark.do".equals(path)){
            deleteRemark(request,response);
        }else  if("/workbench/customer/saveRemark.do".equals(path)){
            saveRemark(request,response);
        }else  if("/workbench/customer/updateRemark.do".equals(path)){
            updateRemark(request,response);
        }else  if("/workbench/customer/getTransactionListByCustomerId.do".equals(path)){
            getTransactionListByCustomerId(request,response);
        }else  if("/workbench/customer/deleteTransaction.do".equals(path)){
            deleteTransaction(request,response);
        }else  if("/workbench/customer/getContactsListByCustomerId.do".equals(path)){
            getContactsListByCustomerId(request,response);
        }else  if("/workbench/customer/deleteContacts.do".equals(path)){
            deleteContacts(request,response);
        }
    }

    private void deleteContacts(HttpServletRequest request, HttpServletResponse response) {

        System.out.println("删除联系人操作");

        String id = request.getParameter("id");

        String[] ids = {id};

        ContactsService cs = (ContactsService) ServiceFactory.getService(new ContactsServiceImpl());

        boolean flag = cs.deleteContacts(ids);

        PrintJson.printJsonFlag(response,flag);
    }

    private void getContactsListByCustomerId(HttpServletRequest request, HttpServletResponse response) {

        System.out.println("通过客户id获取客户联系人列表操作");

        String customerId = request.getParameter("customerId");

        ContactsService cs = (ContactsService) ServiceFactory.getService(new ContactsServiceImpl());

        List<Contacts> cList = cs.getContactsListByCustomerId(customerId);

        PrintJson.printJsonObj(response,cList);
    }

    private void deleteTransaction(HttpServletRequest request, HttpServletResponse response) {

        System.out.println("删除交易操作");

        String id = request.getParameter("id");

        String[] ids = {id};

        TranService ts = (TranService) ServiceFactory.getService(new TranServiceImpl());

        boolean flag = ts.deleteTransaction(ids);

        PrintJson.printJsonFlag(response,flag);
    }

    private void getTransactionListByCustomerId(HttpServletRequest request, HttpServletResponse response) {

        System.out.println("通过客户id获得与其关联的交易列表");

        String customerId = request.getParameter("customerId");

        TranService ts = (TranService) ServiceFactory.getService(new TranServiceImpl());

        List<Tran> tList = ts.getTransactionListByCustomerId(customerId);

        PrintJson.printJsonObj(response,tList);
    }


    private void updateRemark(HttpServletRequest request, HttpServletResponse response) {

        System.out.println("执行修改备注的操作");

        String id = request.getParameter("id");
        String noteContent = request.getParameter("noteContent");
        String editTime = DateTimeUtil.getSysTime();
        String editBy = ((User)request.getSession().getAttribute("user")).getName();
        String editFlag = "1";

        CustomerRemark cusr = new CustomerRemark();
        cusr.setId(id);
        cusr.setNoteContent(noteContent);
        cusr.setEditTime(editTime);
        cusr.setEditBy(editBy);
        cusr.setEditFlag(editFlag);

        CustomerService cs = (CustomerService) ServiceFactory.getService(new CustomerServiceImpl());

        boolean flag = cs.updateRemark(cusr);

        Map<String,Object> map = new HashMap<String, Object>();
        map.put("success",flag);
        map.put("cusr",cusr);

        PrintJson.printJsonObj(response,map);
    }

    private void saveRemark(HttpServletRequest request, HttpServletResponse response) {


        System.out.println("执行添加备注操作");

        String noteContent = request.getParameter("noteContent");
        String customerId = request.getParameter("customerId");
        String id =  UUIDUtil.getUUID();
        String createTime = DateTimeUtil.getSysTime();
        String createBy = ((User)request.getSession().getAttribute("user")).getName();
        String editFlag = "0";

        CustomerRemark cusr = new CustomerRemark();
        cusr.setId(id);
        cusr.setCustomerId(customerId);
        cusr.setNoteContent(noteContent);
        cusr.setCreateTime(createTime);
        cusr.setCreateBy(createBy);
        cusr.setEditFlag(editFlag);

        CustomerService cs = (CustomerService) ServiceFactory.getService(new CustomerServiceImpl());

        boolean flag = cs.saveRemark(cusr);

        Map<String,Object> map = new HashMap<String, Object>();
        map.put("success",flag);
        map.put("cusr",cusr);

        PrintJson.printJsonObj(response,map);
    }

    private void deleteRemark(HttpServletRequest request, HttpServletResponse response) {

        System.out.println("删除备注操作");

        String id = request.getParameter("id");

        CustomerService cs = (CustomerService) ServiceFactory.getService(new CustomerServiceImpl());

        boolean flag = cs.deleteRemark(id);

        PrintJson.printJsonFlag(response,flag);
    }

    private void getRemarkListByCid(HttpServletRequest request, HttpServletResponse response) {

        System.out.println("根据客户id，取得备注信息列表");

        String customerId = request.getParameter("customerId");

        CustomerService cs = (CustomerService) ServiceFactory.getService(new CustomerServiceImpl());

        List<CustomerRemark> cList = cs.getRemarkListByCid(customerId);


        PrintJson.printJsonObj(response,cList);
    }

    private void detail(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        System.out.println("进入到跳转到详细信息页操作");

        String id = request.getParameter("id");

        CustomerService cs = (CustomerService) ServiceFactory.getService(new CustomerServiceImpl());

        Customer c = cs.detail(id);

        request.setAttribute("c",c);
        //将a放进request域中，不能使用重定向，使用转发
        request.getRequestDispatcher("/workbench/customer/detail.jsp").forward(request,response);
    }

    private void update(HttpServletRequest request, HttpServletResponse response) {

        System.out.println("执行客户信息修改操作");

        String id = request.getParameter("id");
        String owner = request.getParameter("owner");
        String name = request.getParameter("name");
        String phone = request.getParameter("phone");
        String website = request.getParameter("website");
        String description = request.getParameter("description");
        String contactSummary = request.getParameter("contactSummary");
        String nextContactTime = request.getParameter("nextContactTime");
        String address = request.getParameter("address");

        //修改时间，当前系统时间
        String editTime = DateTimeUtil.getSysTime();
        //修改人，当前登录用户
        String editBy = ((User)request.getSession().getAttribute("user")).getName();

        Customer c = new Customer();
        c.setId(id);
        c.setOwner(owner);
        c.setName(name);
        c.setWebsite(website);
        c.setPhone(phone);
        c.setDescription(description);
        c.setContactSummary(contactSummary);
        c.setNextContactTime(nextContactTime);
        c.setAddress(address);
        c.setEditTime(editTime);
        c.setEditBy(editBy);

        CustomerService cs = (CustomerService) ServiceFactory.getService(new CustomerServiceImpl());

        boolean flag = cs.update(c);

        PrintJson.printJsonFlag(response,flag);
    }

    private void getUserListAndCustomer(HttpServletRequest request, HttpServletResponse response) {

        System.out.println("进入到查询用户信息列表和根据线索id查询单条记录的操作");

        String id = request.getParameter("id");

        /*
            调用业务层获得一个前端需要的uList和clue （用c代替）对象
         */
        CustomerService cs = (CustomerService) ServiceFactory.getService(new CustomerServiceImpl());

        /*
            uList
            c

            同时获得这两个值复用性不高不用vo,用map
         */
        Map<String,Object> map = cs.getUserListAndCustomer(id);

        PrintJson.printJsonObj(response,map);

    }

    private void delete(HttpServletRequest request, HttpServletResponse response) {

        System.out.println("执行删除客户操作");

        //接收前端数据
        String[] ids = request.getParameterValues("id");

        CustomerService cs = (CustomerService) ServiceFactory.getService(new CustomerServiceImpl());

        boolean flag = cs.delete(ids);

        PrintJson.printJsonFlag(response,flag);
    }

    private void pageList(HttpServletRequest request, HttpServletResponse response) {

        System.out.println("进入到查询客户信息列表的操作（结合条件查询+分页查询）");

        //接收前端数据
        String name = request.getParameter("name");
        String owner = request.getParameter("owner");
        String phone = request.getParameter("phone");
        String website = request.getParameter("website");
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
        map.put("phone",phone);
        map.put("website",website);
        map.put("skipCount",skipCount);
        map.put("pageSize",pageSize);

        CustomerService cs = (CustomerService) ServiceFactory.getService(new CustomerServiceImpl());


        PaginationVO<Customer> vo = cs.pageList(map);

        PrintJson.printJsonObj(response,vo);
    }

    private void save(HttpServletRequest request, HttpServletResponse response) {

        System.out.println("执行客户添加操作");

        String id = UUIDUtil.getUUID();
        String name = request.getParameter("name");
        String owner = request.getParameter("owner");
        String phone = request.getParameter("phone");
        String website = request.getParameter("website");
        String createBy = ((User)request.getSession().getAttribute("user")).getName();
        String createTime = DateTimeUtil.getSysTime();
        String description = request.getParameter("description");
        String contactSummary = request.getParameter("contactSummary");
        String nextContactTime = request.getParameter("nextContactTime");
        String address = request.getParameter("address");

        Customer c = new Customer();
        c.setId(id);
        c.setName(name);
        c.setOwner(owner);
        c.setPhone(phone);
        c.setWebsite(website);
        c.setCreateBy(createBy);
        c.setCreateTime(createTime);
        c.setDescription(description);
        c.setContactSummary(contactSummary);
        c.setNextContactTime(nextContactTime);
        c.setAddress(address);

        CustomerService cs = (CustomerService) ServiceFactory.getService(new CustomerServiceImpl());

        boolean flag = cs.save(c);

        PrintJson.printJsonFlag(response,flag);
    }

    private void getUserList(HttpServletRequest request, HttpServletResponse response) {

        System.out.println("取得用户信息列表");

        UserService us = (UserService) ServiceFactory.getService(new UserServiceImpl());

        List<User> uList = us.getUserList();

        PrintJson.printJsonObj(response,uList);
    }
}
