package com.zzkMavenJfinal.controller.index;

import java.io.File;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSONObject;
import com.jfinal.core.Controller;
import com.jfinal.kit.HttpKit;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.upload.UploadFile;
import com.zzkMavenJfinal.model.SysUser;
import com.zzkMavenJfinal.model.tb_Enclosure;
import com.zzkMavenJfinal.model.tb_Interest;
import com.zzkMavenJfinal.model.tb_staff;


public class indexController extends Controller {
	IndexService service = IndexService.SERVICE;
	public void index1() {
		String Nationality="";
		String field="";
		String order="";
		Integer ii=0;
		String sql=" from tb_staff ";
		if (getPara("key[parameter][Nationality]")!=null&&!"".equals(getPara("key[parameter][Nationality]"))) {
			ii++;
			String kw = getPara("key[parameter][Nationality]");
			Nationality="%"+kw+"%";;
			sql+=" where Nationality like ? ";
		}
		if (getPara("field")!=null&&!"".equals(getPara("field"))) {
			field=getPara("field");
		}
		if (getPara("order")!=null&&!"".equals(getPara("order"))) {
			order=getPara("order");
			sql+=" order by "+field+" "+order+" ";
		}
    	Object[] para = new Object[]{Nationality};
		Object[] obj = new Object[ii];
		for (int i = 0; i < obj.length; i++) {
			for (int j = 0; j < para.length; j++) {
				if (para[j]!=""&&para[j]!=null) {
					obj[i]=para[j];
					para[j]="";
					break;
				}
			}
		}
		Integer page = Integer.parseInt(getPara("page"));
		Integer limit = Integer.parseInt(getPara("limit"));
        Page<tb_staff> blogs=service.queryList1(page,limit,sql,obj);
        Ret ret=Ret.create();
        ret.setOk();
        ret.set("data",blogs.getList());
        ret.set("code",0);
        ret.set("msg","");
        ret.set("count",blogs.getTotalRow());
        //setAttr("ret", ret);
        renderJson(ret);
	}
	/**
     * 查找所有blog
     */
    public void index(){
        render("index.jsp");
    }
    public void delete() {
    	Integer code=0;
    	String msg="";
    	String string = HttpKit.readData(getRequest());
		//System.out.println(string);/
		JSONObject jsonObject = JSONObject.parseObject(string);
		int Uid = jsonObject.getIntValue("Uid");
		if (Uid!=0) {
			try {
				Integer kw = Uid;
				if (service.delect(kw)) {
					code=1;
					msg="删除成功！";
				} else {
					code=0;
					msg="删除失败！";
				}
			} catch (Exception e) {
				code=0;
				msg="删除失败！"+e.getMessage();
			}
		}
		Ret ret=Ret.create();
        ret.setOk();
        ret.set("code",code);
        ret.set("msg",msg);
        renderJson(ret);
    }

    public void add(){
        render("add.jsp");
    }
    public void submitAdd(){
    	Integer code=0;
    	String msg="";
    	String string = HttpKit.readData(getRequest());
		//System.out.println(string);/
		JSONObject jsonObject = JSONObject.parseObject(string);
		String Name = jsonObject.getString("Name");
		String Phone = jsonObject.getString("Phone");
		String Age = jsonObject.getString("Age");
		String Salary = jsonObject.getString("Salary");
		String Nationalty = jsonObject.getString("Nationalty");
		String sex = jsonObject.getString("sex");
		List<tb_staff> tbstasEs=new ArrayList<tb_staff>();
		tb_staff tbs=new tb_staff();
		tbs.set("Name", Name);
		tbs.set("Phone", Phone);
		tbs.set("Age", Age);
		tbs.set("Salary", Salary);
		tbs.set("Nationality", Nationalty);
		tbs.set("Sex", sex);
		tbstasEs.add(tbs);
		try {
			if (service.submitAdd(tbstasEs)) {
				code=1;
				msg="新增成功！";
			} else {
				code=0;
				msg="新增失败！";
			}
		} catch (Exception e) {
			code=0;
			msg="新增失败！"+e.getMessage();
		}
		Ret ret=Ret.create();
        ret.setOk();
        ret.set("code",code);
        ret.set("msg",msg);
        renderJson(ret);
    }
    public void submitEdit(){
    	Integer code=0;
    	String msg="";
    	String string = HttpKit.readData(getRequest());
		//System.out.println(string);/
		JSONObject jsonObject = JSONObject.parseObject(string);
		String Id = jsonObject.getString("Id");
		String Name = jsonObject.getString("Name");
		String Phone = jsonObject.getString("Phone");
		String Age = jsonObject.getString("Age");
		String Salary = jsonObject.getString("Salary");
		String Nationalty = jsonObject.getString("Nationalty");
		String sex = jsonObject.getString("sex");
		List<tb_staff> tbstasEs=new ArrayList<tb_staff>();
		tb_staff tbs=new tb_staff();
		tbs.set("Id", Id);
		tbs.set("Name", Name);
		tbs.set("Phone", Phone);
		tbs.set("Age", Age);
		tbs.set("Salary", Salary);
		tbs.set("Nationality", Nationalty);
		tbs.set("Sex", sex);
		tbstasEs.add(tbs);
		try {
			if (service.submitUpdate(tbstasEs)) {
				code=1;
				msg="修改成功！";
			} else {
				code=0;
				msg="修改失败！";
			}
		} catch (Exception e) {
			code=0;
			msg="修改失败！"+e.getMessage();
		}
		Ret ret=Ret.create();
        ret.setOk();
        ret.set("code",code);
        ret.set("msg",msg);
        renderJson(ret);
    }
    public void upload() {
		Ret ret=Ret.create();
        ret.setOk();
		Ret ret1=Ret.create();
        ret1.setOk();
        try {
            UploadFile file = getFile();
            System.out.println("--------file--------");
            File delfile = new File(file.getUploadPath()+"\\"+file.getFileName());
            System.out.println("=========="+delfile.getPath());
            ret.set("code",1);
            ret.set("msg","上传成功！");
            ret1.set("filePath",delfile.getPath());
            ret1.set("fileSize",delfile.length()/1024+"");
            ret.set("ret1",ret1);
        } catch (Exception e) {
            e.printStackTrace();
            ret.set("code",0);
            ret.set("msg",e.getMessage());
        }
        renderJson(ret);        
    }
    public void edit() {
    	Ret ret =new Ret();
    	ret.setOk();
    	try {
			Integer idInteger=getParaToInt("editId");
			//List<tb_staff> lstbList=new ArrayList<tb_staff>();
			List<tb_staff> lstbList=service.queryList2(idInteger);
            ret.set("code",1);
            ret.set("msg","ok");
            ret.set("data",lstbList);
		} catch (Exception e) {
			// TODO: handle exception
            ret.set("code",0);
            ret.set("msg",e.getMessage());
		}
		setAttr("ret", ret);
		render("edit.jsp");
    }
    public void select() {
    	Ret ret =new Ret();
    	ret.setOk();
    	try {
			List<tb_Interest> lstbList=service.querytbi();
            ret.set("code",1);
            ret.set("msg","ok");
            ret.set("data",lstbList);
		} catch (Exception e) {
			// TODO: handle exception
            ret.set("code",0);
            ret.set("msg",e.getMessage());
		}
    	renderJson(ret);
    }
}
