package com.zl.crud.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zl.crud.Bean.Department;
import com.zl.crud.Bean.Msg;
import com.zl.crud.Service.DepartmentService;

/**
 * 
 * 处理和部门有关的controller
 * @author llz
 *
 */
@Controller
public class DepartmentController {
	
	@Autowired
	private DepartmentService departmentService;
	
	
	//返回所有部门信息
	@RequestMapping("/depts")
	@ResponseBody
	public Msg getDepts() {
		//查处的所有部门信息
		List<Department> list = departmentService.getDepts();
		return Msg.success().add("depts",list);
	}
	
}
