package org.freedom.infra.driver.scale;

import java.math.BigDecimal;
import java.sql.Time;
import java.util.Date;

public class ScaleResult {
	private BigDecimal weight;

	private Date date;

	private Time time;
	
	public ScaleResult(BigDecimal weight, Date date, Time time) {
		
		this.weight = weight;
		this.date = date;
		this.time = time;
		
	}

	public ScaleResult() {
		super();
	}
	
	public void setWeight(BigDecimal weight) {
		this.weight = weight;
	}

	public BigDecimal getWeight() {
		return weight;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Date getDate() {
		return date;
	}

	public void setTime(Time time) {
		this.time = time;
	}

	public Time getTime() {
		return time;
	}
	
	public boolean equals(Object scaleres) {
		boolean result = false;
		if ( ( scaleres !=null) && ( scaleres instanceof ScaleResult) ) {
	        if ( (this.weight == null) && ( ( (ScaleResult) scaleres).getWeight()==null) ) {
	        	result = true;
	        } else {
	        	result = ((ScaleResult) scaleres).getWeight().equals(this.weight);
	        }
		} 
		return result;
	}
}
