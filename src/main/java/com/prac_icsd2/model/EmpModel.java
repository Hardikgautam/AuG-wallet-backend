//package com.prac_icsd2.model;
//
//import java.time.LocalDate;
//
//import com.fasterxml.jackson.annotation.JsonInclude;
//
//import jakarta.persistence.Entity;
//import jakarta.persistence.Id;
//import jakarta.persistence.JoinColumn;
//import jakarta.persistence.ManyToOne;
//import jakarta.persistence.Table;
//import lombok.AllArgsConstructor;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//
//@Entity
//@Table(name="emp")
//@Data
//@NoArgsConstructor
//@AllArgsConstructor
//public class EmpModel {
//	@Id
//	private int empno;
//	private String ename;
//	private String job;
//	@JsonInclude(JsonInclude.Include.NON_NULL)
//	private int mgr;//to handle null values - as mgr is having null value 
//	private LocalDate hiredate;
//	private double sal;
//	@JsonInclude(JsonInclude.Include.NON_NULL)
//	private double comm;//to handle null values - as comm is having null value
//	@ManyToOne
//	@JoinColumn(name ="deptno")
//	private DeptModel deptModel;
//}