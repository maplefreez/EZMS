package ez.game.ezms.sql.models;

import java.util.Calendar;

/**
 * 数据库的账户模型。
 */
public class Account {

	// '账号ID，全局唯一主键'
	private int id;
	//'账号，目前支持16位,全局唯一,不是主键'
	private String account;
	// '存储使用了SHA1()加密的密文',
	private String password;
	// '性别, 0-female, 1-male',
	private boolean gender;
	// 'GM等级,初步设定是值越大等级越高',
	private short GMlevel;
	// '是否已经登录,存储登录状态。当登录时需要设置此值。'
	private boolean islogined;
	// '上次登录时间',
	private Calendar lastLogintime;
	// 账号创建时间',
	private Calendar createddate;
	// '生日'
	private Calendar birthday;
	// '电子邮箱,目前给予设置的是最高64个字符',
	private String Email;
	// '账号是否被封',
	private boolean isbanned;
	//'被封原因代码',
	private short bannedReason;
	// '点券数',
	private int points;
	// '抵用券数'
	private int vpoints;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public boolean getGender() {
		return gender;
	}

	public void setGender(boolean gender) {
		this.gender = gender;
	}

	public short getGMlevel() {
		return GMlevel;
	}

	public void setGMlevel(short GMlevel) {
		this.GMlevel = GMlevel;
	}

	public boolean isIslogined() {
		return islogined;
	}

	public void setIslogined(boolean islogined) {
		this.islogined = islogined;
	}

	public Calendar getLastLogintime() {
		return lastLogintime;
	}

	public void setLastLogintime(Calendar lastLogintime) {
		this.lastLogintime = lastLogintime;
	}

	public Calendar getCreateddate() {
		return createddate;
	}

	public void setCreateddate(Calendar createddate) {
		this.createddate = createddate;
	}

	public Calendar getBirthday() {
		return birthday;
	}

	public void setBirthday(Calendar birthday) {
		this.birthday = birthday;
	}

	public String getEmail() {
		return Email;
	}

	public void setEmail(String email) {
		Email = email;
	}

	public boolean isIsbanned() {
		return isbanned;
	}

	public void setIsbanned(boolean isbanned) {
		this.isbanned = isbanned;
	}

	public short getBannedReason() {
		return bannedReason;
	}

	public void setBannedReason(short bannedReason) {
		this.bannedReason = bannedReason;
	}

	public int getPoints() {
		return points;
	}

	public void setPoints(int points) {
		this.points = points;
	}

	public int getVpoints() {
		return vpoints;
	}

	public void setVpoints(int vpoints) {
		this.vpoints = vpoints;
	}
}
