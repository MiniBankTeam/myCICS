package com.ibm.cics.savvy.trans;

import com.ibm.cics.savvy.util.ContainerUtil;
import com.ibm.cics.savvy.util.DBUtil;
import com.ibm.cics.savvy.util.IConstants;
import com.ibm.cics.savvy.util.PropertiesUtil;
import com.ibm.cics.server.Channel;
import com.ibm.cics.server.CommAreaHolder;

/**
 * OSGi program to create user
 */
public class CreateUser extends Transaction implements ITransaction {

	public static void main(CommAreaHolder cah) {
		System.out.println("\nCreateUser is being invoked...");
		CreateUser txCreUsr = new CreateUser();
		txCreUsr.doTransaction(txCreUsr);
		System.out.println("CreateUser returns...");
	}

	@Override
	public void transactionLogic(Channel channel) {
		// get transaction data from containers
		String userId = ContainerUtil.getContainerData(channel, IConstants.CUST_ID);
		String name = ContainerUtil.getContainerData(channel, IConstants.CUST_NAME);
		String gender = ContainerUtil.getContainerData(channel, IConstants.CUST_GENDER);
		String age = ContainerUtil.getContainerData(channel, IConstants.CUST_AGE);
		String address = ContainerUtil.getContainerData(channel, IConstants.CUST_ADDR);

		// construct the SQL command
		String sqlCmd = "INSERT INTO " + PropertiesUtil.getPropertiesUtil().getTableCustomer() + "("
				+ PropertiesUtil.getPropertiesUtil().getFieldCustID() + ", "
				+ PropertiesUtil.getPropertiesUtil().getFieldCustName() + ", "
				+ PropertiesUtil.getPropertiesUtil().getFieldCustGender() + ", "
				+ PropertiesUtil.getPropertiesUtil().getFieldCustAge() + ", "
				+ PropertiesUtil.getPropertiesUtil().getFieldCustAddress()
				+ ") VALUES("
				+ "'" + userId + "', "
				+ "'" + name + "', "
				+ "'" + gender.charAt(0) + "', "
				+ "'" + age + "', "
				+ "'" + address + "'"
				+ ")";
		// update the database table
		int numUpd = DBUtil.getDBUtilInstance().execUpdateSQL(sqlCmd);
		// put the transaction status to return container
		String tranCode = (new Integer(numUpd)).toString();
		ContainerUtil.putContainerData(channel, IConstants.TRAN_CODE, tranCode);
		// put the detail transaction message to return container
		String message = null;
		if ( numUpd > 0 ) {
			message = "Create user " + userId + " is successful";
		} else {
			message = "Create user " + userId + " is failed";
		}
		ContainerUtil.putContainerData(channel, IConstants.TRAN_MSG, message);

	}

}
