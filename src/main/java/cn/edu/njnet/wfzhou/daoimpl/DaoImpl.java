package cn.edu.njnet.wfzhou.daoimpl;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import cn.edu.njnet.wfzhou.core.Connect;
import cn.edu.njnet.wfzhou.core.Prest;
import cn.edu.njnet.wfzhou.core.Reflect;
import cn.edu.njnet.wfzhou.core.SqlCreate;
import cn.edu.njnet.wfzhou.dao.Dao;
/**
 * 
 * @author wfzhou
 * create 2017/08/02 给出了对数据库表的增删改查的模版实现
 * 对于用户的每一张表,都需要有一个类继承DaoImpl,用户可以直接使用DaoImpl
 * 实现的功能也可以对其功能实现扩展,比如更新完数据库后还需要同时更新缓存
 *
 */
public abstract class DaoImpl implements Dao {

	@Override
	public int insert(Object obj) {
		int result = 0;
		// 反射工具类
		Reflect reflect = new Reflect(obj);
		// 动态生成sql语句
		String sql = SqlCreate.insert(reflect.getTableName(),
				reflect.getAttributeNum());
		System.out.println(sql);
		// 获取对象属性值
		String attributeValue[] = reflect.getAttributeValue();
		// 获取对象属性类型
		int attributeType[] = reflect.getAttributeType();
		// 获取数据库连接对象
		Connection connect = Connect.getConnect();
		Prest prest = new Prest(connect, sql, attributeValue, attributeType,
				false);
		result = prest.executeUpdate();
		// 关闭数据库
		prest.closeDB();
		return result;
	}

	@Override
	public int update(String tableName, String[] attributeName,
			String[] condition, int[] type, String[] value) {
		int result = 0;
		String sql = SqlCreate.update(tableName, attributeName, condition);
		System.out.println(sql);
		Connection connection = Connect.getConnect();
		Prest prest = new Prest(connection, sql, value, type, false);
		result = prest.executeUpdate();
		prest.closeDB();
		return result;
	}

	@Override
	public int delete(String tableName, String[] condition, int[] type,
			String[] value) {
		int result = 0;
		String sql = SqlCreate.delete(tableName, condition);
		System.out.println(sql);
		Connection connection = Connect.getConnect();
		Prest prest = new Prest(connection, sql, value, type, false);
		result = prest.executeUpdate();
		prest.closeDB();
		return result;
	}

	@Override
	public List<Object> search(Object obj, String[] attributeName,
			String[] condition, int[] type, String[] value) {
		Reflect reflect = new Reflect(obj);
		String tableName = reflect.getTableName();
		String sql = SqlCreate.search(tableName, attributeName, condition);
		System.out.println(sql);
		Connection connection = Connect.getConnect();
		Prest prest = new Prest(connection, sql, value, type, false);
		ResultSet resultSet = prest.executeQuery();
		List<Object> list = new ArrayList<Object>();
		if (resultSet == null) {
			return null;
		}
		try {
			while (resultSet.next()) {
				Object objects[] = new Object[attributeName.length];
				Reflect r = new Reflect(obj.getClass().getName());
				for (int i = 0; i < attributeName.length; i++) {
					objects[i] = resultSet.getObject(attributeName[i]);
				}
				r.setValues(objects, attributeName);
				list.add(r.getObject());
			}
			return list;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				resultSet.close();
				prest.closeDB();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

}
