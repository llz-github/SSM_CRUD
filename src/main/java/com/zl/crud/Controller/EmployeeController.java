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
 * ����Ա����ɾ�Ĳ�
 * 
 * @author llz
 *
 */
@Controller
public class EmployeeController {

	@Autowired
	EmployeeService employeeService;

	// ɾ������
	// 1.������������һ
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

	// ��������
	@ResponseBody
	@RequestMapping(value = "/emp/{empId}", method = RequestMethod.PUT)
	public Msg saveEmp(Employee employee) {
		System.out.println("��Ҫ���µ�����" + employee);
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
		// �ж��û����Ƿ��ǺϷ��ı��ʽ
		String regx = "(^[a-zA-Z0-9]{6,16}$)|(^[\\u4e00-\\u9fa5]{2,5})";
											
		if (!empName.matches(regx)) {
			return Msg.fail().add("va_msg", "�û���������6-16λ���ֺ���ĸ����ϻ���2-5λ����");
		}
		// ���ݿ��û����ظ�У��
		Boolean b = employeeService.checkUser(empName);
		if (b) {
			return Msg.success();
		} else {
			return Msg.fail().add("va_msg", "�û���������");
		}
	}

	// Ա������
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
	 * ����josn��
	 * 
	 * @return
	 */
	@RequestMapping("/emps")
	@ResponseBody
	public Msg getEmpsWithJson(@RequestParam(value = "pn", defaultValue = "1") Integer pn) {
		// ����ҳ���Լ�ÿҳ�Ĵ�С
		PageHelper.startPage(pn, 5);
		// startPage������Ĳ�ѯ���Ƿ�ҳ��ѯ
		List<Employee> employees = employeeService.getAll();
		// ʹ��pageinfo��װ��ѯ���
		// ������ʾ��ҳ��(��ҳ)
		PageInfo pageInfo = new PageInfo(employees, 5);
		return Msg.success().add("pageInfo", pageInfo);
	}

//	@RequestMapping("/emps")
	public String getEmps(@RequestParam(value = "pn", defaultValue = "1") Integer pn, Model model) {

		// ����ҳ���Լ�ÿҳ�Ĵ�С
		PageHelper.startPage(pn, 5);
		// startPage������Ĳ�ѯ���Ƿ�ҳ��ѯ
		List<Employee> employees = employeeService.getAll();
		// ʹ��pageinfo��װ��ѯ���
		// ������ʾ��ҳ��(��ҳ)
		PageInfo pageInfo = new PageInfo(employees, 5);
		model.addAttribute("pageInfo", pageInfo);
		return "list";
	}
}
