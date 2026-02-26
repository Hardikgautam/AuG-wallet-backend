//package com.prac_icsd2.ServiceImpl;
//
//import java.util.List;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import com.prac_icsd2.model.DeptModel;
//import com.prac_icsd2.repo.DeptRepo;
//import com.prac_icsd2.serviceI.DeptService;
//
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//
//@Service
//@Slf4j
//@RequiredArgsConstructor
//public class DeptServiceImpl implements DeptService{
//	private final DeptRepo deptRepo;
//
//	@Override
//	public DeptModel saveDept(DeptModel dept) {
//		// TODO Auto-generated method stub
//		return deptRepo.save(dept);
//		
//	}
//
//	@Override
//	public List<DeptModel> getAllDepts() {
//		// TODO Auto-generated method stub
//		return deptRepo.findAll();
//	}
//	
//
//}
