//package com.prac_icsd2.controller;
//
//import java.util.List;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.CrossOrigin;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import com.prac_icsd2.model.DeptModel;
//import com.prac_icsd2.serviceI.DeptService;
//
//import lombok.RequiredArgsConstructor;
//
//@RestController
//@RequiredArgsConstructor
//@CrossOrigin(value="*")
//@RequestMapping(value="/dept")
//public class DeptController {
//	
//	private final DeptService deptService;
//	
//	@PostMapping(value="/create")
//	public DeptModel saveDept()//@RequestBody DeptModel dept)
//	{
//		DeptModel dept1=new DeptModel(10, "ACCOUNTING", "NEW YORK");
//		DeptModel dept2=new DeptModel(20, "RESEARCH", "DALLAS");
//		DeptModel dept3=new DeptModel(30, "SALES", "CHICAGO");
//		DeptModel dept4=new DeptModel(40, "OPERATIONS", "BOSTON");
//		
//		deptService.saveDept(dept1);
//		deptService.saveDept(dept2);
//		deptService.saveDept(dept3);
//		DeptModel d=deptService.saveDept(dept4);
//		return d;
//	}
//
//	@GetMapping(value="/get")
//	public List<DeptModel> getAllDepts()
//	{
//		return deptService.getAllDepts();
//	}
//}
