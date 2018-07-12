package com.epay.xj.common;

import org.hibernate.boot.model.naming.Identifier;
import org.hibernate.boot.model.naming.PhysicalNamingStrategyStandardImpl;
import org.hibernate.engine.jdbc.env.spi.JdbcEnvironment;

public class MySQLUpperCaseStrategy extends PhysicalNamingStrategyStandardImpl {

	private static final long serialVersionUID = 1L;

	public Identifier toPhysicalTableName(Identifier name, JdbcEnvironment context) {
		String tableName = name.getText();
		return name.toIdentifier(tableName);
	}

}
