package cn.edu.njnet.wfzhou.daofactory;

import cn.edu.njnet.wfzhou.dao.Dao;
/**
 * 
 * @author wfzhou
 * create in 2017/08/02 根据类的权限定名获取Dao的实现类,静态工厂模式
 *
 */
public class DaoFactory {
		public static Dao getDaoImpl(String className){
			checkClassName(className);
			Dao dao=null;
			try {
				dao=(Dao) Class.forName(className).newInstance();
				return dao;
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
			return null;
		}
		private static void checkClassName(String className){
			if(className==null||className==""){
				throw new NullPointerException("DAO工厂创建DAOImpl实例：className=null");
			}
		}
}
