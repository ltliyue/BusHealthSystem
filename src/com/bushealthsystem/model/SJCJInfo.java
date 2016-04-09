package com.bushealthsystem.model;

import java.util.Date;

import com.lidroid.xutils.db.annotation.Id;
import com.lidroid.xutils.db.annotation.Table;

@Table(name = "sjcjinfo")
public class SJCJInfo {
	@Id
	private int id;
	private String sbid;// 设备id
	private Date cjrq;// 采集日起

	private String cnwd;
	private String cwwd;
	private String dy;
	private String dl;
	private String fjzs;
	private String gykg;// 高压开关
	private String dykg;// 低压开关

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getSbid() {
		return sbid;
	}

	public void setSbid(String sbid) {
		this.sbid = sbid;
	}

	public Date getCjrq() {
		return cjrq;
	}

	public void setCjrq(Date cjrq) {
		this.cjrq = cjrq;
	}

	public String getCnwd() {
		return cnwd;
	}

	public void setCnwd(String cnwd) {
		this.cnwd = cnwd;
	}

	public String getCwwd() {
		return cwwd;
	}

	public void setCwwd(String cwwd) {
		this.cwwd = cwwd;
	}

	public String getDy() {
		return dy;
	}

	public void setDy(String dy) {
		this.dy = dy;
	}

	public String getDl() {
		return dl;
	}

	public void setDl(String dl) {
		this.dl = dl;
	}

	public String getFjzs() {
		return fjzs;
	}

	public void setFjzs(String fjzs) {
		this.fjzs = fjzs;
	}

	public String getGykg() {
		return gykg;
	}

	public void setGykg(String gykg) {
		this.gykg = gykg;
	}

	public String getDykg() {
		return dykg;
	}

	public void setDykg(String dykg) {
		this.dykg = dykg;
	}

}
