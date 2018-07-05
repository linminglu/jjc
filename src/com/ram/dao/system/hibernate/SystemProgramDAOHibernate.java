

package com.ram.dao.system.hibernate;

import java.util.List;

import com.ram.model.Program;
import com.ram.model.User;
import com.ram.dao.system.ISystemProgramDAO;
import org.springframework.orm.ObjectRetrievalFailureException;
import com.framework.dao.hibernate.AbstractBaseDAOHibernate;
import org.hibernate.Query;


/**
 * @author yangjingyue
 * 系统设置层次DAO数据访问控制接口
 */
public class SystemProgramDAOHibernate extends AbstractBaseDAOHibernate implements ISystemProgramDAO{
	
	/**
	 * 保存层次信息
	 * @param program
	 * @return
	 */
	public void saveSystemProgram(Program program,User user){
		saveObject(program,user);		
	};
	
	/**
	 * 通过主键进行查找Program
	 * @param id
	 */
	public Program getSystemProgram(Integer id){
		if(id ==null)return null;
		Program program = (Program)getObject(Program.class,id);
        if (program == null) {
            log.warn("program '" + id + "' not found...");
            throw new ObjectRetrievalFailureException(Program.class, id);
        }
        return program;		
	};
	
	/**
	 * 通过持久化对象找到全部列表
	 * @return
	 */
	public List findSystemPrograms(){
		Query q = getSession().createQuery("from Program program order by program.programId");
		return q.list();				
	}

	/**
	 * 物理删除
	 * @param program
	 */
	public void deleteSystemProgram(Integer id, User user){
		this.deleteObject(Program.class, id, user);
	};
	
	/**
	 * 逻辑删除
	 * @param id
	 */
	public void removeSystemProgram(Integer id, User user){		
		this.removeObject(Program.class, id, user);
	}

	/**
	 * 更改 
	 */
	public void updateSystemProgram(Program program, User user) {
		this.updateObject(program, user);		
	}
}
