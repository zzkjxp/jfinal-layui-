package com.zzkMavenJfinal.controller.index;

import java.sql.SQLException;
import java.util.List;

import org.eclipse.jdt.internal.compiler.flow.FinallyFlowContext;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.IAtom;
import com.jfinal.plugin.activerecord.Page;
import com.zzkMavenJfinal.model.SysUser;
import com.zzkMavenJfinal.model.tb_Enclosure;
import com.zzkMavenJfinal.model.tb_Interest;
import com.zzkMavenJfinal.model.tb_staff;


public class IndexService {
	public static final IndexService SERVICE = new IndexService();
	private SysUser blog=new SysUser().dao();
	private tb_Enclosure tb=new tb_Enclosure().dao();
	private tb_staff stb=new tb_staff().dao();
	private tb_Interest tbiInterest=new tb_Interest().dao();


    public SysUser queryById(int id){
        return blog.findById(id);
    }

    public List<SysUser> queryList(){
        List<SysUser> blogList=blog.find("select * from tb_vote");
        return blogList;
    }
    public Page<tb_staff> queryList1(Integer page,Integer limit,String sql,Object[] obj){
        //List<tb_Enclosure> blogList=tb.find("select * from tb_Enclosure");
    	Page<tb_staff> blogList=stb.paginate(page, limit, "select *", sql, obj);
        return blogList;
    }
    public List<tb_staff> queryList2(Integer id){
        List<tb_staff> blogList=stb.find("select * from tb_staff where Id=?",id);
        return blogList;
    }
	public Boolean delect(int iid) {
		Integer ss=iid;
		int count = Db.delete("delete tb_staff where Id=?",iid);
    	return count == 1;
		//Boolean bool =stb.deleteById(id);
		//return bool;
	}

    public void delectById(int id){
        blog.deleteById(id);
    }
    public Boolean submitAdd(final List<tb_staff> votes) {

		return Db.tx(new IAtom() {
			public boolean run() throws SQLException {
				// TODO Auto-generated method stub
				String sql = "insert into tb_staff(Name, Sex, Age, Phone, Salary, Nationality) values(?,?,?,?,?,?)";
				Db.batch(sql, "Name, Sex, Age, Phone, Salary, Nationality", votes,
						votes.size());
				return true;
			}
		});
    }
    public Boolean submitUpdate(final List<tb_staff> votes) {

		return Db.tx(new IAtom() {
			public boolean run() throws SQLException {
				// TODO Auto-generated method stub
				String sql = "update tb_staff set Name=?, Sex=?, Age=?, Phone=?, Salary=?, Nationality=? where Id=? ";
				Db.batch(sql, "Name, Sex, Age, Phone, Salary, Nationality,Id", votes,
						votes.size());
				return true;
			}
		});
    }
    public List<tb_Interest> querytbi(){
        List<tb_Interest> blogList=tbiInterest.find("select * from tb_Interest ");
        return blogList;
    }
}
