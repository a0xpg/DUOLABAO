package com.hualong.duolabao.tool;

import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.commons.dbcp2.BasicDataSourceFactory;
import org.json.JSONArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;


public class DB {
	static Logger logger = LoggerFactory.getLogger(DB.class);
	public static void main(String args[]) throws Exception {
		// Select_(GetConnection.getStoreConn());

//		String a = "1234567890123";
//		a = a.substring(1 - 1, 2);
//		System.out.println(a);
//		String fQuantity = "0344";
//
//		Double Kg = (double) (Integer.parseInt(fQuantity) / 1000);// 由g改成kg
//		System.out.println(Kg );
//		System.out.println(0344/1000);
		Select_(GetConnection.getPos_SaleConn()) ;

	}

	public static String Select_(Connection conn) { // 测试方法
		CallableStatement c = null;
		ResultSet rs = null;
		try {
			PreparedStatement past = conn.prepareStatement(" select top 10 *  from dbo.t_SaleSheetDetail");
			rs = past.executeQuery();
			String str = ResultSet_To_JSON.resultSetTostr(rs);
			// JSONArray array=ResultSet_To_JSON.resultSetToJsonArray(rs);
			System.out.println(str);
			return str;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DB.closeRs_Con(c, conn);
		}
		return null;
	}


	public static JSONArray select(Connection conn) { // 测试方法
		String sql = "select cGoodsNo,cUnitedNo,cGoodsName,cGoodsTypeno,cGoodsTypename,cBarcode,cUnit,cSpec,fNormalPrice,fVipPrice,fPreservationUp,fPreservationDown,fVipScore,bWeight,fCKPrice,cSupNo,cSupName  from dbo.t_Goods";
		try {
			PreparedStatement past = conn.prepareStatement(sql);
			ResultSet rs = past.executeQuery();
			JSONArray array = ResultSet_To_JSON.resultSetToJsonArray(rs);
			System.out.println(array);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	private static Map<String, DataSource> map = new HashMap<String, DataSource>();

	public static void init(String Ip, String DataSourceName) {
		if (String_Tool.isEmpty(Ip) || String_Tool.isEmpty(DataSourceName)) {
			System.out.println("配置文件没有数据");
			return;
		}
		Properties p = new Properties();
		p.setProperty("driverClassName", "com.microsoft.sqlserver.jdbc.SQLServerDriver");
		p.setProperty("url", "jdbc:sqlserver://" + Ip + ";databaseName=" + DataSourceName);
		p.setProperty("username", "sa");
		p.setProperty("password", new ReadConfig().getprop().getProperty("PassWord"));
		p.setProperty("maxActive", "30");
		p.setProperty("maxIdle", "100");
		p.setProperty("maxWait", "1000");
		p.setProperty("removeAbandoned", "true"); // 回收
		p.setProperty("removeAbandonedTimeout", "300"); // 回收超时时间
		p.setProperty("testOnBorrow", "true"); // 获取当前连接
		p.setProperty("logAbandoner", "true"); // 返回
		try {
			BasicDataSource dataSource = (BasicDataSource) BasicDataSourceFactory.createDataSource(p);
			map.put(DataSourceName, dataSource);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("数据库配置");
		}
	}

	public static void init(String Ip, String DataSourceName, String PassWord) {
		Properties p = new Properties();
		p.setProperty("driverClassName", "com.microsoft.sqlserver.jdbc.SQLServerDriver");
		p.setProperty("url", "jdbc:sqlserver://" + Ip + ";databaseName=" + DataSourceName);
		p.setProperty("username", "sa");
		p.setProperty("password", PassWord);
		p.setProperty("maxActive", "30");
		p.setProperty("maxIdle", "100");
		p.setProperty("maxWait", "1000");
		p.setProperty("removeAbandoned", "true"); // 回收
		p.setProperty("removeAbandonedTimeout", "300"); // 回收超时时间
		p.setProperty("testOnBorrow", "true"); // 获取当前连接
		p.setProperty("logAbandoner", "true"); // 返回
		try {
			BasicDataSource dataSource = (BasicDataSource) BasicDataSourceFactory.createDataSource(p);
			map.put(DataSourceName, dataSource);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("数据库配置");
		}
	}

	public static Connection getConnection(String Ip, String DataSourceName) throws SQLException {
		BasicDataSource dataSource = (BasicDataSource) map.get(DataSourceName);
		if (dataSource == null) {
			init(Ip, DataSourceName);
			System.out.println("新建连接池" + DataSourceName);
			dataSource = (BasicDataSource) map.get(DataSourceName);
		}
		return dataSource.getConnection();
	}

	public static Connection getConnection(String Ip, String DataSourceName, String PassWord) throws SQLException {
		BasicDataSource dataSource = (BasicDataSource) map.get(DataSourceName);
		if (dataSource == null) {
			init(Ip, DataSourceName, PassWord);
			System.out.println("新建连接池" + DataSourceName);
			dataSource = (BasicDataSource) map.get(DataSourceName);
		}
		return dataSource.getConnection();
	}

	public static void closeResultSet(ResultSet rs) { // 关闭结果集
		try {
			if (rs != null) {
				rs.close();
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void closeCallState(CallableStatement c) { // 关闭存储过程
		try {
			if (c != null) {
				c.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void closeConn(Connection conn) {// 关闭链接
		try {
			if (conn != null) {
				conn.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void past(Statement pstmt) {
		if (pstmt != null) {
			try {
				pstmt.close();
			} catch (SQLException e) {

				e.printStackTrace();
			}
		}
	}

	public static void closeRs_Con(CallableStatement c, Connection conn) {
		closeCallState(c);
		closeConn(conn);
	}

	public static void closeRs_Con(ResultSet rs, CallableStatement c, Connection conn) {
		closeResultSet(rs);
		closeRs_Con(c, conn);
	}

	public static void closeRs_Con(ResultSet rs, Statement pstmt, Connection conn) {
		closeResultSet(rs);
		closePreparedStatement(pstmt);
		closeConn(conn);
	}
	public static void closePreparedStatement(Statement pstmt){
		past(pstmt);
	}


	public static void closeAll(ResultSet rs, Statement pstmt, CallableStatement c, Connection conn) {
		closeResultSet(rs);
		closePreparedStatement(pstmt);
		closeRs_Con(c, conn);
	}
}
