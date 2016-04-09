package com.bushealthsystem.model;

import java.util.Date;

import com.lidroid.xutils.db.annotation.Id;
import com.lidroid.xutils.db.annotation.Table;

@Table(name = "wxinfo")
public class WXInfo {
	@Id
	private int id;
	private String wxy;
	private Date wxrq;
	private String wxsj;

	private String kcbh;
	private String ktbh;

	private String gzyy;
	private String wxjl;

	public Date getWxrq() {
		return wxrq;
	}

	public void setWxrq(Date wxrq) {
		this.wxrq = wxrq;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getWxy() {
		return wxy;
	}

	public void setWxy(String wxy) {
		this.wxy = wxy;
	}

	public String getWxsj() {
		return wxsj;
	}

	public void setWxsj(String wxsj) {
		this.wxsj = wxsj;
	}

	public String getKcbh() {
		return kcbh;
	}

	public void setKcbh(String kcbh) {
		this.kcbh = kcbh;
	}

	public String getKtbh() {
		return ktbh;
	}

	public void setKtbh(String ktbh) {
		this.ktbh = ktbh;
	}

	public String getGzyy() {
		return gzyy;
	}

	public void setGzyy(String gzyy) {
		this.gzyy = gzyy;
	}

	public String getWxjl() {
		return wxjl;
	}

	public void setWxjl(String wxjl) {
		this.wxjl = wxjl;
	}

}
