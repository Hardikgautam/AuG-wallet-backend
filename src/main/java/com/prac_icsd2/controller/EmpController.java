//package com.prac_icsd2.controller;
//
//import java.time.LocalDate;
//import java.util.List;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.domain.Slice;
//import org.springframework.web.bind.annotation.CrossOrigin;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import com.prac_icsd2.model.DeptModel;
//import com.prac_icsd2.model.EmpModel;
//import com.prac_icsd2.serviceI.DeptService;
//import com.prac_icsd2.serviceI.EmpService;
//
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//@RestController
//@CrossOrigin(value="*")
//@RequiredArgsConstructor
//@Slf4j
//@RequestMapping(value="/emp")
//public class EmpController {
//
//	private final EmpService empService;
//	
//	@PostMapping(value="/create")
//	public EmpModel saveEmp()//@RequestBody EmpModel emp)			
//	{
//		//7369	SMITH	CLERK	7902	17-12-80	800		20
//		//EmpModel D=new EmpModel(0, null, null, 0, null, 0, null, null)
//		EmpModel e1=new EmpModel(7369, "SMITH", "CLERK", 7902, LocalDate.of(1980, 12, 17), 800, null, new DeptModel(20, null, null));
//		EmpModel e2=new EmpModel(7499, "ALLEN", "SALESMAN", 7698, LocalDate.of(1981, 2, 20), 1600, 300.0, new DeptModel(30, null, null));
//		EmpModel e3=new EmpModel(7521, "WARD", "SALESMAN", 7698, LocalDate.of(1981, 2, 22), 1250, 500.0, new DeptModel(30, null, null));
//		EmpModel e4=new EmpModel(7566, "JONES", "MANAGER", 7839, LocalDate.of(1981, 04, 02), 2975, null, new DeptModel(20, null, null));
//		
//		
//		
//		
//		EmpModel e5=new EmpModel(7654, "MARTIN", "CLERK", 7698, LocalDate.of(1981, 9, 28), 1250, 1400.0,  new DeptModel(30, null, null));
//		EmpModel e6=new EmpModel(7698, "BLAKE", "CLERK", 7839, LocalDate.of(1981, 5, 1), 2850, null,  new DeptModel(30, null, null));
//		EmpModel e7=new EmpModel(7782, "CLARK", "CLERK", 7839, LocalDate.of(1981, 6, 9), 2450, null,  new DeptModel(10, null, null));
//		
//		EmpModel e9=new EmpModel(7788, "SCOTT", "CLERK", 7566, LocalDate.of(1982, 12, 9), 3000, null,  new DeptModel(20, null, null));
//		
//		
//		
//		
//		EmpModel e10=new EmpModel(7839, "KING", "PRESIDENT", 0, LocalDate.of(1981, 11, 17), 5000, null,  new DeptModel(10, null, null));
//		EmpModel e11=new EmpModel(7844, "TURNER", "SALESMAN", 7698, LocalDate.of(1981, 9, 8), 1500, 0.0,  new DeptModel(30, null, null));
//		EmpModel e12=new EmpModel(7876, "ADAMS", "CLERK", 7788, LocalDate.of(1983, 1, 12), 1100, null,  new DeptModel(20, null, null));
//		EmpModel e13=new EmpModel(7900, "JAMES", "CLERK", 7698, LocalDate.of(1981, 12, 3), 950,null,  new DeptModel(30, null, null));
//		
//		EmpModel e14=new EmpModel(7902, "FORD", "ANALYST", 7566, LocalDate.of(1980, 12, 3), 3000, null,  new DeptModel(20, null, null));
//		EmpModel e8=new EmpModel(7934, "MILLER", "CLERK", 7782, LocalDate.of(1980, 1, 23), 1300, null,  new DeptModel(10, null, null));
//		
//		empService.saveEmp(e1);
//		empService.saveEmp(e2);
//		empService.saveEmp(e3);
//		empService.saveEmp(e4);
//		empService.saveEmp(e5);
//		empService.saveEmp(e6);
//		empService.saveEmp(e7);
//		empService.saveEmp(e8);
//		empService.saveEmp(e9);
//		empService.saveEmp(e10);
//		empService.saveEmp(e11);
//		empService.saveEmp(e12);
//		empService.saveEmp(e13);
//	EmpModel e=	empService.saveEmp(e14);
//		
//		return e;
//	}
//
//	
////
////	@GetMapping(value="/get/findDistinctByOpeningBalance/{opBal}")
////	public ResponseEntity<ApiResponse> findDistinctByOpeningBalance(@PathVariable double opBal) throws Exception
////	{
//	@GetMapping(value="/get/existByEname/{ename}")
//	public boolean existByEname(@PathVariable String ename)
//	{
//		
//		return empService.existByEname(ename);
//	}
//	@GetMapping(value="/get/findAllByJob/{job}")
//	public List<EmpModel> findAllByJob(String job)
//	{
//		//secondPageWithTwoElements
//		return empService.findAllByJob(job);
//	}
//	
//	@GetMapping(value="/get/findAllByJobSlice/{dno}")
//	public Slice<EmpModel> findAllByDeptnoSlice(@PathVariable int  dno)
//	{
//		//secondPageWithTwoElements
//		return empService.findAllByDeptno(dno);
//	}
////	@GetMapping(value="/get/findByJob/{job}/{pageNo}/{pageSize}")
////    Page<EmpModel> findByJob(@PathVariable String job,@PathVariable int pageNo,@PathVariable int pageSize)
////    {
////		System.out.println(pageNo+ " "+pageSize);
////		Pageable pagination=PageRequest.of(pageNo, pageSize);
////	//	pagination.withSort(Sort.by("ename").ascending());
////		return empService.findByJob(job, pagination);
////    }
//
//}