package dmiaes.app.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "qbs_hospitaluser")
public class User {
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "HUserID")
	private Long userid; 
	
	@Column(name = "HUserName")
	private String username;
	
	@Column(name = "HUserPassword")
	private String password;
	
	@Column(name = "HUserStatus")
	private boolean userstatus;
	
	@Column(name = "HospitalID")
	private String hospitalid;
	
	@Column(name = "CityID")
	private Integer cityid;
	
	@Column(name = "DeptID")
	private Integer deptid;
	
	@Column(name = "DMIAESPGID")
	private Integer dmiaespgid;
	
	@Column(name = "DeptGID")
	private Integer deptgid;
	
	public Long getUserid() {
		return userid;
	}

	public void setUserid(Long userid) {
		this.userid = userid;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public boolean isUserstatus() {
		return userstatus;
	}

	public void setUserstatus(boolean userstatus) {
		this.userstatus = userstatus;
	}

	public String getHospitalid() {
		return hospitalid;
	}

	public void setHospitalid(String hospitalid) {
		this.hospitalid = hospitalid;
	}

	public Integer getCityid() {
		return cityid;
	}

	public void setCityid(Integer cityid) {
		this.cityid = cityid;
	}

	public Integer getDeptid() {
		return deptid;
	}

	public void setDeptid(Integer deptid) {
		this.deptid = deptid;
	}

	public Integer getDmiaespgid() {
		return dmiaespgid;
	}

	public void setDmiaespgid(Integer dmiaespgid) {
		this.dmiaespgid = dmiaespgid;
	}

	public Integer getDeptgid() {
		return deptgid;
	}

	public void setDeptgid(Integer deptgid) {
		this.deptgid = deptgid;
	}
	

	
}
