package com.zshy.datacleaning.dao;

import java.sql.SQLException;
import java.util.List;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import com.zshy.datacleaning.domain.TradeDetail;
import com.zshy.datacleaning.util.DBUtil;

public class TradeDetailDaoImpl implements TradeDetailDao {
	private QueryRunner runner = null;// 查询运行器
	public TradeDetailDaoImpl() {
		runner = new QueryRunner();
	}

	// 方法：向数据库中添加一条记录
	@Override
	public void add(TradeDetail t) throws SQLException {
		String sql = "insert into trade_detail(txn_date, txn_seq_id, cert_no, "
				+ "card_no, mer_id, mer_type, amount, sf_type, return_code) "
				+ "values(?,?,?, ?,?,?, ?,?,?)";
		runner.update(DBUtil.getConnection(), sql, t.getTxn_date(), t.getTxn_seq_id(), t.getCert_no(),
				t.getCard_no(), t.getMer_id(), t.getMer_type(), t.getAmount(), t.getSF_TYPE(), t.getReturn_code());
	}

	// 方法：根据id向数据库中修改某条记录
	@Override
	public void update(TradeDetail t) throws SQLException {
//		String sql = "update trade_detail set name=?,age=?,description=? where id=?";
//		runner.update(DBUtil.getConnection(), sql, t.getName(), t.getAge(),
//				t.getDescription(), p.getId());
	}

	// 方法：根据id删除数据库中的某条记录
	@Override
	public void delete(int id) throws SQLException {
		String sql = "delete from person where id=?";
		runner.update(DBUtil.getConnection(), sql, id);
	}

	// 方法：使用BeanHandler查询一个对象
	@Override
	public TradeDetail findById(int id) throws SQLException {
//		String sql = "select id, name, age, description from person where id=?";
//		TradeDetail p = runner.query(DBUtil.getConnection(), sql,
//				new BeanHandler<Person>(Person.class), id);
//		return p;
		return null;
	}

	// 方法：使用BeanListHandler查询所有对象
	@Override
	public List<TradeDetail> findAll() throws SQLException {
		String sql = "select name,age,description from person";
		List<TradeDetail> tradeDetails = runner.query(DBUtil.getConnection(),
				sql, new BeanListHandler<TradeDetail>(TradeDetail.class));
		return tradeDetails;
	}

	// 方法：使用ScalarHandler查询一共有几条记录
	@Override
	public long personCount() throws SQLException {
		String sql = "select count(id) from person";
		return runner.query(DBUtil.getConnection(), sql,
				new ScalarHandler<Long>());
	}

}
