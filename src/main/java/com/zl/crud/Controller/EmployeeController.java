package com.zl.crud.Controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zl.crud.Bean.Employee;
import com.zl.crud.Bean.Msg;
import com.zl.crud.Service.EmployeeService;

/**
 * 处理员工增删改查
 * 
 * @author llz
 *
 */
@Controller
public class EmployeeController {

	@Autowired
	EmployeeService employeeService;

	// 删除数据
	// 1.单个批量二合一
	@RequestMapping(value = "/emp/{id}", method = RequestMethod.DELETE)
	@ResponseBody
	public Msg deleteEmpById(@PathVariable("id") String ids) {
		if (ids.contains("-")) {
			List<Integer> del_ids = new ArrayList<Integer>();
			String[] str_ids = ids.split("-");
			for (String string : str_ids) {

				del_ids.add(Integer.parseInt(string));
			}
			employeeService.deleteBatch(del_ids);

		} else {
			Integer id = Integer.parseInt(ids);
			employeeService.deleteEmp(id);
		}
		return Msg.success();
	}

	// 更新数据
	@ResponseBody
	@RequestMapping(value = "/emp/{empId}", method = RequestMethod.PUT)
	public Msg saveEmp(Employee employee) {
		System.out.println("将要更新的数据" + employee);
		employeeService.updateEmp(employee);

		return Msg.success();
	}

	@RequestMapping(value = "/emp/{id}", method = RequestMethod.GET)
	@ResponseBody
	public Msg getEmp(@PathVariable("id") Integer id) {
		Employee employee = employeeService.getEmp(id);
		return Msg.success().add("emp", employee);
	}

	@ResponseBody
	@RequestMapping("/checkuser")
	public Msg checkUse(@RequestParam("empName") String empName) {
		// 判断用户名是否是合法的表达式
		String regx = "(^[a-zA-Z0-9]{6,16}$)|(^[\\u4e00-\\u9fa5]{2,5})";
											
		if (!empName.matches(regx)) {
			return Msg.fail().add("va_msg", "用户名必须是6-16位数字和字母的组合或者2-5位中文");
		}
		// 数据库用户名重复校验
		Boolean b = employeeService.checkUser(empName);
		if (b) {
			return Msg.success();
		} else {
			return Msg.fail().add("va_msg", "用户名不可用");
		}
	}

	// 员工保存
	@RequestMapping(value = "/emp", method = RequestMethod.POST)
	@ResponseBody
	public Msg saveEmp(@Valid Employee employee, BindingResult result) {
		if (result.hasErrors()) {
			Map<String, Object> map = new HashMap<String, Object>();
			List<FieldError> errors = result.getFieldErrors();
			for (FieldError fieldError : errors) {
				map.put(fieldError.getField(), fieldError.getDefaultMessage());
			}
			return Msg.fail().add("errorFields", map);
		} else {
			employeeService.saveEmp(employee);
			return Msg.success();
		}
	}

	/**
	 * 导入josn包
	 * 
	 * @return
	 */
	@RequestMapping("/emps")
	@ResponseBody
	public Msg getEmpsWithJson(@RequestParam(value = "pn", defaultValue = "1") Integer pn) {
		// 传入页码以及每页的大小
		PageHelper.startPage(pn, 5);
		// startPage后紧跟的查询就是分页查询
		List<Employee> employees = employeeService.getAll();
		// 使用pageinfo包装查询结果
		// 连续显示的页数(五页)
		PageInfo pageInfo = new PageInfo(employees, 5);
		return Msg.success().add("pageInfo", pageInfo);
	}

//	@RequestMapping("/emps")
	public String getEmps(@RequestParam(value = "pn", defaultValue = "1") Integer pn, Model model) {

		// 传入页码以及每页的大小
		PageHelper.startPage(pn, 5);
		// startPage后紧跟的查询就是分页查询
		List<Employee> employees = employeeService.getAll();
		// 使用pageinfo包装查询结果
		// 连续显示的页数(五页)
		PageInfo pageInfo = new PageInfo(employees, 5);
		model.addAttribute("pageInfo", pageInfo);
		return "list";
	}
}
