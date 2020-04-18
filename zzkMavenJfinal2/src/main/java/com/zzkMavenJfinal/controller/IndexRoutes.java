package com.zzkMavenJfinal.controller;

import com.jfinal.config.Routes;

import com.zzkMavenJfinal.controller.index.indexController;

public class IndexRoutes extends Routes {
	@Override
	public void config() {
		// TODO Auto-generated method stub
		setBaseViewPath("/index/");
		add("/", indexController.class);
	}
}
