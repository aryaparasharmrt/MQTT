package com.dwellsmart.constants;

public final class Constants {
	
	private Constants() {}
	
	public static final String CLIENT_IOS = "IOS";
	public static final String CLIENT_AND = "AND";
	public static final String CLIENT_WEB = "WEB";
	
	public static final String TOKEN_PREFIX = "Bearer ";

	public static final String TXN_TYPE_CREDIT = "Credit";
	public static final String TXN_TYPE_DEBIT = "Debit";
	public static final String TXN_TYPE_RECHARGE = "Recharge";

	public static final String TXN_MODE_AUTO = "Auto";
	public static final String TXN_MODE_MANUAL = "Manual";
	public static final String TXN_MODE_CHEQUE = "Cheque";
	public static final String TXN_MODE_CASH = "Cash";
	public static final String TXN_MODE_NEFT = "NEFT";
	public static final String TXN_MODE_ONLINE = "Online";

	public static final String MESSAGE_INVALIDUSERNAME = "Invalid username.";
	public static final String MESSAGE_INVALIDPASSWORD = "Invalid password.";
	public static final String MESSAGE_INVALIDCURRENCY = "Invalid currency.";
	public static final String MESSAGE_INVALIDAMOUNT = "Invalid amount.";
	public static final String MESSAGE_INVALIDTCNO = "Invalid TC No.";
	public static final String MESSAGE_EXCEEDEDMAXVALUE = "Exceeded Maximum Value Per Transaction.";
	public static final String MESSAGE_EXCEEDEDMAXVALUEFORDAY = "Exceeded Maximum Transaction Value For the Day.";
	public static final String MESSAGE_SAMEUSERTRANSACTION = "You can't send money to yourself.";
	public static final String MESSAGE_SAMEUSERNAMEEXIST = "User with the same name exists.";
	public static final String MESSAGE_SAMETCNOEXIST = "User with the same tc no exists.";
	public static final String MESSAGE_EXCHANGESWITHMAINCURRENCY = "You can't make exchange transactions with ";

}
