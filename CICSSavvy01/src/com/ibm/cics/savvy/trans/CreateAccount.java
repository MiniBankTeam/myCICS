package com.ibm.cics.savvy.trans;

import com.ibm.cics.savvy.util.ContainerUtil;
import com.ibm.cics.savvy.util.DBUtil;
import com.ibm.cics.savvy.util.IConstants;
import com.ibm.cics.savvy.util.PropertiesUtil;
import com.ibm.cics.server.Channel;
import com.ibm.cics.server.CommAreaHolder;

/**
 * OSGi program to create account
 */
public class CreateAccount extends Transaction implements ITransaction {

	/**
	 * @param args
	 */
	public static void main(CommAreaHolder cah) {
		System.out.println("CreateAccount is being invoked...");
		CreateAccount txCreAcct = new CreateAccount();
		txCreAcct.doTransaction(txCreAcct);
		System.out.println("CreateAccount returns...");
	}

	@Override
	public void transactionLogic(Channel channel) {
		// get transaction data from containers
		String acctNum = ContainerUtil.getContainerData(channel, IConstants.ACCT_NUMBER);
		String acctCustID = ContainerUtil.getContainerData(channel, IConstants.ACCT_CUST_ID);
		String balance = ContainerUtil.getContainerData(channel, IConstants.ACCT_BALANCE);
		String changeTime = ContainerUtil.getContainerData(channel, IConstants.ACCT_CHANGE);
		
		// construct SQL command
		String sqlCmd = "INSERT INTO " + PropertiesUtil.getPropertiesUtil().getTableAccount() + "("
				+ PropertiesUtil.getPropertiesUtil().getFieldAcctNummber() + ", "
				+ PropertiesUtil.getPropertiesUtil().getFieldAcctCustID() + ", "
				+ PropertiesUtil.getPropertiesUtil().getFieldAcctBalance() + ", "
				+ PropertiesUtil.getPropertiesUtil().getFieldAcctLastChange()
				+ ") VALUES("
				+ "'" + acctNum + "', "
				+ "'" + acctCustID + "', "
				//+ "'" + balance + "', "
				+ (new Float(balance)).floatValue() + ", "
				+ "'" + changeTime + "'"
				+ ")";
		// update the database table
		int numUpd = DBUtil.getDBUtilInstance().execUpdateSQL(sqlCmd);
		// put the transaction status to container 
		ContainerUtil.putContainerData(channel, IConstants.TRAN_CODE, (new Integer(numUpd)).toString());
		// put the transaction detail message to container
		String message = null;
		if ( numUpd > 0 ) {
			message = "Create account " + acctNum + " is successful";
		} else {
			message = "Create account " + acctNum + " is failed";
		}
		ContainerUtil.putContainerData(channel, IConstants.TRAN_MSG, message);
	}

}
