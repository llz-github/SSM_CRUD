package com.zl.crud.Test;

import java.util.UUID;

import org.apache.ibatis.session.SqlSession;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.zl.crud.Bean.Department;
import com.zl.crud.Bean.Employee;
import com.zl.crud.Dao.DepartmentMapper;
import com.zl.crud.Dao.EmployeeMapper;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:applicationContext.xml"})
public class MapperTest {

	@Autowired
	DepartmentMapper departmentMapper;
	@Autowired
	EmployeeMapper employeeMapper; 
	
	@Autowired
	SqlSession sqlSession;
	
	@Test
	public void testCrud() {
		//测试DepartmentMapper
//		ApplicationContext applicationContext = new ClassPathXmlApplicationContext("applicationContext.xml");
//		applicationContext.getBean(DepartmentMapper);
//		System.out.println(departmentMapper);
		//1.插入几个部门
//		departmentMapper.insertSelective(new Department(null,"开发部"));
//		departmentMapper.insertSelective(new Department(null,"测试部"));	
		//2.插入几个员工
//		employeeMapper.insertSelective(new Employee(null,"llzz","M","123@qq.com",1));
		//3.批量插入多个员工,批量操作的SQL session
		EmployeeMapper employeeMapper = sqlSession.getMapper(EmployeeMapper.class);
		for(int i=0;i<100;i++) {
			String uid = UUID.randomUUID().toString().substring(0, 5)+i;
			employeeMapper.insertSelective(new Employee(null,uid,"M",uid+"@atzl.com",1));	
		}
		
//		EmployeeMapper employeeMapper = sqlSession.getMapper(EmployeeMapper.class);
//		for(int i=0;i<=1000;i++) {
//			employeeMapper.deleteByPrimaryKey(i);	
//		}
	}
}
