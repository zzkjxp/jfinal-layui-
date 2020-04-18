package com.zzkMavenJfinal.common;

import com.alibaba.druid.filter.stat.StatFilter;
import com.alibaba.druid.wall.WallFilter;
import com.jfinal.config.Constants;
import com.jfinal.config.Handlers;
import com.jfinal.config.Interceptors;
import com.jfinal.config.JFinalConfig;
import com.jfinal.config.Plugins;
import com.jfinal.config.Routes;
import com.jfinal.kit.Prop;
import com.jfinal.kit.PropKit;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.activerecord.dialect.AnsiSqlDialect;
import com.jfinal.plugin.activerecord.dialect.MysqlDialect;
import com.jfinal.plugin.druid.DruidPlugin;
import com.jfinal.render.ViewType;
import com.jfinal.template.Engine;
import com.zzkMavenJfinal.controller.IndexRoutes;
import com.zzkMavenJfinal.model.SysUser;
import com.zzkMavenJfinal.model.tb_Enclosure;
import com.zzkMavenJfinal.model.tb_Interest;
import com.zzkMavenJfinal.model.tb_staff;

public class Myconfig extends JFinalConfig{
	/**
	 * 将全局配置提出来 方便其他地方重用
	 */
	private static Prop p;
	private WallFilter wallFilter;

	public void configConstant(Constants constants) {
        constants.setDevMode(true);
        constants.setEncoding("UTF-8");
        constants.setViewType(ViewType.JSP);
    }

    public void configRoute(Routes routes) {
    	// 推荐拆分方式 如果需要就解开注释 创建对应的 Routes
    	routes.add(new IndexRoutes());// 配置网站前台路由
    }

	// 先加载开发环境配置，再追加生产环境的少量配置覆盖掉开发环境配置
	static void loadConfig() {
		if (p == null) {
			p = PropKit.use("config.properties").appendIfExists("config-pro.properties");
		}
	}
    public void configEngine(Engine engine) {

    }

    public void configInterceptor(Interceptors interceptors) {
    }

    public void configHandler(Handlers handlers) {
    }
    /**
	 * 配置JFinal插件 数据库连接池 ActiveRecordPlugin 缓存 定时任务 自定义插件
	 */
	@Override
	public void configPlugin(Plugins me) {
		loadConfig();
		wallFilter = new WallFilter(); // 加强数据库安全
		//c3p0连接池插件
		DruidPlugin C3p0Plugin = createC3p0Plugin();//c3p0太过老旧笨重，像一头年迈的老狗
		wallFilter.setDbType("sqlserver");
		C3p0Plugin.addFilter(wallFilter);
		C3p0Plugin.addFilter(new StatFilter()); // 添加 StatFilter 才会有统计数据
				me.add(C3p0Plugin);
				//数据库操作插件
				ActiveRecordPlugin arp = new ActiveRecordPlugin("sqlserver",C3p0Plugin);
				arp.setShowSql(p.getBoolean("devMode"));
				//设置方言（很重要，一定要设置）
				arp.setDialect(new AnsiSqlDialect());
				arp.addMapping("tb_Enclosure", "Id", tb_Enclosure.class);
				arp.addMapping("tb_staff", "Id", tb_staff.class);
				me.add(arp);
		
		
		
		DruidPlugin dbMysql = creatDruidPlugins();
		wallFilter.setDbType("mysql");
		dbMysql.addFilter(wallFilter);
		dbMysql.addFilter(new StatFilter()); // 添加 StatFilter 才会有统计数据
		me.add(dbMysql);
		ActiveRecordPlugin arpMysql = new ActiveRecordPlugin("mysql", dbMysql);
		arpMysql.setShowSql(p.getBoolean("devMode"));
		arpMysql.setDialect(new MysqlDialect());
		arpMysql.addMapping("tb_vote", "Id", SysUser.class);
		arpMysql.addMapping("tb_Interest", "id", tb_Interest.class);
		me.add(arpMysql);
	}
    // 系统启动完成后回调
    public void afterJFinalStart() {
    }

    // 系统关闭之前回调
    public void beforeJFinalStop() {
    }
    /**
     * 获取数据连接池
     * @return
     */
    public static DruidPlugin creatDruidPlugins(){
		loadConfig();
        return new DruidPlugin(PropKit.get("mysqljdbcUrl"),PropKit.get("mysqluser"),PropKit.get("mysqlpassword").trim());
    }
    public static DruidPlugin createC3p0Plugin() {
		loadConfig();
		return new DruidPlugin(PropKit.get("jdbcsqlurl"), PropKit.get("sqluser"),PropKit.get("sqlpwd").trim());
	}
    public static void main(String[] args) {
        // 第二个参数为端口
        //JFinal.start("src/main/webapp", 8085, "/");
    }
}
