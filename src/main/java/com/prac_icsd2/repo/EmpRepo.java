//package com.prac_icsd2.repo;
//
//import java.util.List;
//
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.Pageable;
//import org.springframework.data.domain.Slice;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.Query;
//import com.prac_icsd2.model.EmpModel;
//
//public interface EmpRepo extends JpaRepository<EmpModel,Integer> {
//
//	/*q- differnce between findAll vs findBy
//	 ans- findall return - collection while findBy return optional
//	 */
//   Boolean existsByEname(String Ename); //Checks if there are any records by name
//   Boolean existsBy(); // Checks if there are any records whatsoever
//   List<EmpModel> findAllByJob(String job, Pageable pageable);
//   Page<EmpModel> findByJob(String job,Pageable pagination);
//   
//   
//   
//   //Page<EmpModel> findByJob(String job, Pageable pageable);
//
//   Slice<EmpModel> findByDeptModelDeptno(int dno, Pageable pageable);
//
//   //List<EmpModel> findByEname(String lastname, Sort sort);
//
//  // List<EmpModel> findByDeptno(int deptno, Pageable pageable);
//
//}
