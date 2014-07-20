package com.vincent.fb.report.to;


public class RowTO {
	private int month;
	private int day;
	private String subject;
	private String category;
	private String member;
	private String channel;
	private Double amount;
	private String merchant;
	private String comment;
	public int getMonth() {
		return month;
	}
	public void setMonth(int month) {
		this.month = month;
	}
	public int getDay() {
		return day;
	}
	public void setDay(int day) {
		this.day = day;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public String getMember() {
		return member;
	}
	public void setMember(String member) {
		this.member = member;
	}
	public String getChannel() {
		return channel;
	}
	public void setChannel(String channel) {
		this.channel = channel;
	}
	public Double getAmount() {
		return amount;
	}
	public void setAmount(Double amount) {
		this.amount = amount;
	}
	public String getMerchant() {
		return merchant;
	}
	public void setMerchant(String merchant) {
		this.merchant = merchant;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	
	@Override
	public String toString() {
		return String
				.format("month : %s,  day : %s,  subject : %s, member : %s, channel : %s, amount : %s, merchant : %s",
						month, day, subject, member, channel, amount, merchant);
	}
	
	public boolean flag(){
		return amount != null;
	}
	
	
}
