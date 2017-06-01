package seckill.entity;

import java.util.Date;

public class SuccessSeckilled {
    private Short state;

    private Date createTime;
    
    private Long seckillId;
    
    private Long userPhone;
    
    //多对一
    private Seckill seckill;

	public Short getState() {
		return state;
	}

	public void setState(Short state) {
		this.state = state;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Long getSeckillId() {
		return seckillId;
	}

	public void setSeckillId(Long seckillId) {
		this.seckillId = seckillId;
	}

	public Long getUserPhone() {
		return userPhone;
	}

	public void setUserPhone(Long userPhone) {
		this.userPhone = userPhone;
	}

	public Seckill getSeckill() {
		return seckill;
	}

	public void setSeckill(Seckill seckill) {
		this.seckill = seckill;
	}

	@Override
	public String toString() {
		return "SuccessSeckilled [state=" + state + ", createTime="
				+ createTime + ", seckillId=" + seckillId + ", userPhone="
				+ userPhone + ", seckill=" + seckill + "]";
	}

	public SuccessSeckilled() {
		super();
	}

    
}