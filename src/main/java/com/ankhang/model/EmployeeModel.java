package com.ankhang.model;

import com.ankhang.entities.Employee;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeModel {
	private Long empId;
	private String empName;
	private InfoModel infoModel;
}
