package com.wjx.crm.workbench.web.controller;


import com.wjx.crm.settings.domain.User;
import com.wjx.crm.settings.service.UserService;
import com.wjx.crm.settings.service.impl.UserServiceImpl;
import com.wjx.crm.utils.DateTimeUtil;
import com.wjx.crm.utils.PrintJson;
import com.wjx.crm.utils.ServiceFactory;
import com.wjx.crm.utils.UUIDUtil;
import com.wjx.crm.vo.PaginationVO;
import com.wjx.crm.workbench.domain.Clue;
import com.wjx.crm.workbench.domain.Contacts;
import com.wjx.crm.workbench.domain.ContactsRemark;
import com.wjx.crm.workbench.service.ClueService;
import com.wjx.crm.workbench.service.ContactsService;
import com.wjx.crm.workbench.service.CustomerService;
import com.wjx.crm.workbench.service.impl.ClueServiceImpl;
import com.wjx.crm.workbench.service.impl.ContactsServiceImpl;
import com.wjx.crm.workbench.service.impl.CustomerServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ContactsController extends HttpServlet {

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        System.out.println("进入到联系人控制器");

        String path = request.getServletPath();

        if("/workbench/contacts/getUserList.do".equals(path)){
            getUserList(request,response);
        }else  if("/workbench/contacts/getCustomerName.do".equals(path)) {
            getCustomerName(request, response);
        }else  if("/workbench/contacts/save.do".equals(path)) {
            save(request, response);
        }else  if("/workbench/contacts/delete.do".equals(path)) {
            delete(request, response);
        }else  if("/workbench/contacts/pageList.do".equals(path)) {
            pageList(request, response);
        }else  if("/workbench/contacts/getUserListAndContacts.do".equals(path)) {
            getUserListAndContacts(request, response);
        }else  if("/workbench/contacts/update.do".equals(path)) {
            update(request, response);
        }else  if("/workbench/contacts/detail.do".equals(path)) {
            detail(request, response);
        }else  if("/workbench/contacts/getRemarkListByCid.do".equals(path)) {
            getRemarkListByCid(request, response);
        }else  if("/workbench/contacts/saveRemark.do".equals(path)) {
            saveRemark(request, response);
        }else  if("/workbench/contacts/updateRemark.do".equals(path)) {
            updateRemark(request, response);
        }else  if("/workbench/contacts/deleteRemark.do".equals(path)) {
            deleteRemark(request, response);
        }
    }

    private void deleteRemark(HttpServletRequest request, HttpServletResponse response) {

        System.out.println("删除备注操作");

        String id = request.getParameter("id");

        ContactsService cs = (ContactsService) ServiceFactory.getService(new ContactsServiceImpl());

        boolean flag = cs.deleteRemark(id);

        PrintJson.printJsonFlag(response,flag);
    }

    private void updateRemark(HttpServletRequest request, HttpServletResponse response) {

        System.out.println("修改备注操作");

        String id = request.getParameter("id");
        String noteContent = request.getParameter("noteContent");
        String editTime = DateTimeUtil.getSysTime();
        String editBy = ((User)request.getSession().getAttribute("user")).getName();
        String editFlag = "1";

        ContactsRemark cr = new ContactsRemark();
        cr.setId(id);
        cr.setNoteContent(noteContent);
        cr.setEditTime(editTime);
        cr.setEditBy(editBy);
        cr.setEditFlag(editFlag);

        ContactsService cs = (ContactsService) ServiceFactory.getService(new ContactsServiceImpl());

        boolean flag = cs.updateRemark(cr);

        Map<String,Object> map = new HashMap<String, Object>();
        map.put("success",flag);
        map.put("cr",cr);

        PrintJson.printJsonObj(response,map);
    }

    private void saveRemark(HttpServletRequest request, HttpServletResponse response) {

        System.out.println("备注添加操作");

        String noteContent = request.getParameter("noteContent");
        String contactsId = request.getParameter("contactsId");
        String id =  UUIDUtil.getUUID();
        String createTime = DateTimeUtil.getSysTime();
        String createBy = ((User)request.getSession().getAttribute("user")).getName();
        String editFlag = "0";

        ContactsRemark cr = new ContactsRemark();
        cr.setId(id);
        cr.setContactsId(contactsId);
        cr.setNoteContent(noteContent);
        cr.setCreateTime(createTime);
        cr.setCreateBy(createBy);
        cr.setEditFlag(editFlag);

        ContactsService cs = (ContactsService) ServiceFactory.getService(new ContactsServiceImpl());

        boolean flag = cs.saveRemark(cr);

        Map<String,Object> map = new HashMap<String, Object>();
        map.put("success",flag);
        map.put("cr",cr);

        PrintJson.printJsonObj(response,map);
    }

    private void getRemarkListByCid(HttpServletRequest request, HttpServletResponse response) {

        System.out.println("根据contactsId查询联系人备注列表");

        String contactsId = request.getParameter("contactsId");

        ContactsService cs = (ContactsService) ServiceFactory.getService(new ContactsServiceImpl());

        List<ContactsRemark> cList = cs.getRemarkListByCid(contactsId);

        PrintJson.printJsonObj(response,cList);
    }

    private void detail(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        System.out.println("跳转到联系人详细信息操作");

        String id = request.getParameter("id");

        ContactsService cs = (ContactsService) ServiceFactory.getService(new ContactsServiceImpl());

        Contacts c = cs.detail(id);

        request.setAttribute("c",c);
        //将a放进request域中，不能使用重定向，使用转发
        request.getRequestDispatcher("/workbench/contacts/detail.jsp").forward(request,response);
    }

    private void update(HttpServletRequest request, HttpServletResponse response) {

        System.out.println("执行联系人修改操作");

        String id = request.getParameter("id");
        String owner = request.getParameter("owner");
        String fullname = request.getParameter("fullname");
        String appellation = request.getParameter("appellation");
        String job = request.getParameter("job");
        String email = request.getParameter("email");
        String mphone = request.getParameter("mphone");
        String birth = request.getParameter("birth");
        String source = request.getParameter("source");
        String customerName = request.getParameter("customerName");
        String description = request.getParameter("description");
        String contactSummary = request.getParameter("contactSummary");
        String nextContactTime = request.getParameter("nextContactTime");
        String address = request.getParameter("address");

        //修改时间，当前系统时间
        String editTime = DateTimeUtil.getSysTime();
        //修改人，当前登录用户
        String editBy = ((User)request.getSession().getAttribute("user")).getName();

        Contacts c = new Contacts();
        c.setId(id);
        c.setOwner(owner);
        c.setFullname(fullname);
        c.setAppellation(appellation);
        c.setJob(job);
        c.setEmail(email);
        c.setMphone(mphone);
        c.setBirth(birth);
        c.setSource(source);
        c.setDescription(description);
        c.setContactSummary(contactSummary);
        c.setNextContactTime(nextContactTime);
        c.setAddress(address);
        c.setEditTime(editTime);
        c.setEditBy(editBy);

        ContactsService cs = (ContactsService) ServiceFactory.getService(new ContactsServiceImpl());

        boolean flag = cs.update(c,customerName);

        PrintJson.printJsonFlag(response,flag);
    }

    private void getUserListAndContacts(HttpServletRequest request, HttpServletResponse response) {

        System.out.println("进入到查询用户信息列表和根据线索id查询单条记录的操作");

        String id = request.getParameter("id");

        /*
            调用业务层获得一个前端需要的uList和clue （用c代替）对象
         */
        ContactsService cs = (ContactsService) ServiceFactory.getService(new ContactsServiceImpl());

        /*
            uList
            c

            同时获得这两个值复用性不高不用vo,用map
         */
        Map<String,Object> map = cs.getUserListAndContacts(id);

        PrintJson.printJsonObj(response,map);
    }

    private void delete(HttpServletRequest request, HttpServletResponse response) {

        System.out.println("执行删除联系人操作");

        //接收前端数据
        String ids[] = request.getParameterValues("id");

        ContactsService cs = (ContactsService) ServiceFactory.getService(new ContactsServiceImpl());

        boolean flag = cs.delete(ids);

        PrintJson.printJsonFlag(response,flag);
    }

    private void pageList(HttpServletRequest request, HttpServletResponse response) {

        System.out.println("进入到查询线索信息列表的操作（结合条件查询+分页查询）");

        //接收前端数据
        String fullname = request.getParameter("fullname");
        String owner = request.getParameter("owner");
        String birth = request.getParameter("birth");
        String customerName = request.getParameter("customerName");
        String source = request.getParameter("source");
        String pageNoStr = request.getParameter("pageNo");
        int pageNo = Integer.valueOf(pageNoStr);
        //每页展现的记录数
        String pageSizeStr = request.getParameter("pageSize");
        int pageSize = Integer.valueOf(pageSizeStr);
        //计算略过的记录数
        int skipCount = (pageNo-1)*pageSize;

        Map<String,Object> map = new HashMap<String,Object>();
        map.put("fullname",fullname);
        map.put("owner",owner);
        map.put("birth",birth);
        map.put("customerName",customerName);
        map.put("source",source);
        map.put("skipCount",skipCount);
        map.put("pageSize",pageSize);

        ContactsService cs = (ContactsService) ServiceFactory.getService(new ContactsServiceImpl());

        PaginationVO<Contacts> vo = cs.pageList(map);


        PrintJson.printJsonObj(response,vo);
    }

    private void save(HttpServletRequest request, HttpServletResponse response) {

        System.out.println("执行联系人添加操作");

        String id = UUIDUtil.getUUID();
        String fullname = request.getParameter("fullname");
        String appellation = request.getParameter("appellation");
        String owner = request.getParameter("owner");
        String job = request.getParameter("job");
        String mphone = request.getParameter("mphone");
        String email = request.getParameter("email");
        String birth = request.getParameter("birth");
        String customerName = request.getParameter("customerName");
        String source = request.getParameter("source");
        String createBy = ((User)request.getSession().getAttribute("user")).getName();
        String createTime = DateTimeUtil.getSysTime();
        String description = request.getParameter("description");
        String contactSummary = request.getParameter("contactSummary");
        String nextContactTime = request.getParameter("nextContactTime");
        String address = request.getParameter("address");

        Contacts c = new Contacts();
        c.setId(id);
        c.setFullname(fullname);
        c.setAppellation(appellation);
        c.setOwner(owner);
        c.setJob(job);
        c.setMphone(mphone);
        c.setEmail(email);
        c.setBirth(birth);
        c.setSource(source);
        c.setCreateBy(createBy);
        c.setCreateTime(createTime);
        c.setDescription(description);
        c.setContactSummary(contactSummary);
        c.setNextContactTime(nextContactTime);
        c.setAddress(address);

        ContactsService cs = (ContactsService) ServiceFactory.getService(new ContactsServiceImpl());

        boolean flag = cs.save(c,customerName);

        PrintJson.printJsonFlag(response,flag);
    }

    private void getCustomerName(HttpServletRequest request, HttpServletResponse response) {

        System.out.println("取得客户名称列表（按照客户名称进行模糊查询）");

        String name = request.getParameter("name");

        CustomerService cs = (CustomerService) ServiceFactory.getService(new CustomerServiceImpl());

        List<String> sList = cs.getCustomerName(name);

        PrintJson.printJsonObj(response,sList);
    }

    private void getUserList(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("取得用户信息列表");

        UserService us = (UserService) ServiceFactory.getService(new UserServiceImpl());

        List<User> uList = us.getUserList();

        PrintJson.printJsonObj(response,uList);
    }

}
