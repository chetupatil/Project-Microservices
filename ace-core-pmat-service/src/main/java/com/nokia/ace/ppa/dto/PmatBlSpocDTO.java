package com.nokia.ace.ppa.dto;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "ba_spoc_drp")
public class PmatBlSpocDTO {
	@Id
	private int id;
	
	@Column(name= "BL")
	private String bl;
	
	@Column(name = "EUR")
	private String eur;
	
	@Column(name = "NAM")
	private String nam;
	
	@Column(name = "LAT")
	private String lat;
	
	@Column(name = "MEA")
	private String mea;
	
	@Column(name = "APJ")
	private String apj;
	
	@Column(name = "IND")
	private String ind;
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getBl() {
		return bl;
	}

	public void setBl(String bl) {
		this.bl = bl;
	}

	public String getEur() {
		return eur;
	}

	public void setEur(String eur) {
		this.eur = eur;
	}

	public String getNam() {
		return nam;
	}

	public void setNam(String nam) {
		this.nam = nam;
	}

	public String getLat() {
		return lat;
	}

	public void setLat(String lat) {
		this.lat = lat;
	}

	public String getMea() {
		return mea;
	}

	public void setMea(String mea) {
		this.mea = mea;
	}

	public String getApj() {
		return apj;
	}

	public void setApj(String apj) {
		this.apj = apj;
	}

	public String getInd() {
		return ind;
	}

	public void setInd(String ind) {
		this.ind = ind;
	}

	public String getGchn() {
		return gchn;
	}

	public void setGchn(String gchn) {
		this.gchn = gchn;
	}

	@Column(name = "GCHN")
	private String gchn;
	
	
	

}
