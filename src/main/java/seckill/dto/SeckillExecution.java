package seckill.dto;

import seckill.entity.SuccessSeckilled;
import seckill.enums.SeckillStatEnum;


/**
 * 封装秒杀执行后的结果
 */
public class SeckillExecution {

    private long seckillId;

    //秒杀执行结果状态
    private int state;

    //状态的标识
    private String stateInfo;

    private SuccessSeckilled successSeckilled;

	public long getSeckillId() {
		return seckillId;
	}

	public void setSeckillId(long seckillId) {
		this.seckillId = seckillId;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public String getStateInfo() {
		return stateInfo;
	}

	public void setStateInfo(String stateInfo) {
		this.stateInfo = stateInfo;
	}

	public SuccessSeckilled getSuccessSeckilled() {
		return successSeckilled;
	}

	public void setSuccessSeckilled(SuccessSeckilled successSeckilled) {
		this.successSeckilled = successSeckilled;
	}

	public SeckillExecution(long seckillId, SeckillStatEnum seckillEnum,
			SuccessSeckilled successSeckilled) {
		super();
		this.seckillId = seckillId;
		this.state = seckillEnum.getState();
		this.stateInfo = seckillEnum.getStateInfo();
		this.successSeckilled = successSeckilled;
	}
	
	public SeckillExecution(long seckillId, SeckillStatEnum seckillEnum) {
		super();
		this.seckillId = seckillId;
		this.state = seckillEnum.getState();
		this.stateInfo = seckillEnum.getStateInfo();
	}

	public SeckillExecution() {
		super();
	}

	@Override
	public String toString() {
		return "SeckillExecution [seckillId=" + seckillId + ", state=" + state
				+ ", stateInfo=" + stateInfo + ", successSeckilled="
				+ successSeckilled + "]";
	}

   
}
