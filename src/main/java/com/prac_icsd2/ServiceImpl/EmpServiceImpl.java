//package com.prac_icsd2.ServiceImpl;
//
//import java.util.List;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.data.domain.Pageable;
//import org.springframework.data.domain.Slice;
//import org.springframework.stereotype.Service;
//
//import com.prac_icsd2.model.EmpModel;
//import com.prac_icsd2.repo.EmpRepo;
//import com.prac_icsd2.serviceI.EmpService;
//
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//
//@Service
//@RequiredArgsConstructor
//@Slf4j
//public class EmpServiceImpl implements EmpService{
//
//	private final EmpRepo empRepo;
//	@Override
//	public EmpModel saveEmp(EmpModel e) {
//		// TODO Auto-generated method stub
//		EmpModel emp=empRepo.save(e);
//		return emp;
//	}
//
//	@Override
//	public List<EmpModel> getAllEmps() {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	@Override
//	public EmpModel getEmpByEmpno(int empno) {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	@Override
//	public EmpModel removeEmp(int empno) {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	@Override
//	public boolean existByEname(String ename) {
//		// TODO Auto-generated method stub
//		return empRepo.existsByEname(ename);
//	}
//
//	@Override
//	public List<EmpModel> findAllByJob(String job) {
//		// TODO Auto-generated method stub
//		//https://www.baeldung.com/spring-data-jpa-pagination-sorting
//		Pageable firstPageWithTwoElements = PageRequest.of(0, 2);//pageno , size
//
//		Pageable secondPageWithTwoElements = PageRequest.of(1, 3);
//		
//		Page<EmpModel> allEmps = empRepo.findAll(firstPageWithTwoElements);
//
//		List<EmpModel> allClerk = 
//				empRepo.findAllByJob("CLERK", secondPageWithTwoElements);
//		return allClerk;
//	}
//	
//	@Override
//	public Slice<EmpModel> findAllByDeptno(int dno) {
//		// TODO Auto-generated method stub
//		//https://www.baeldung.com/spring-data-jpa-pagination-sorting
//		Pageable firstPageWithTwoElements = PageRequest.of(0, 2);//pageno , size
//
//		
//		
//		
//
//		Slice<EmpModel> allClerk = 
//				empRepo.findByDeptModelDeptno(20, firstPageWithTwoElements);
//		return allClerk;
//	}
//
//	@Override
//	public Page<EmpModel> findByJob(String job, Pageable pagination) {
//		// TODO Auto-generated method stub
//		return empRepo.findByJob(job, pagination);
//	}
//
//}
