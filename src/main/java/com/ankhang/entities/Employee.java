package com.ankhang.entities;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "Employee")
public class Employee implements Serializable{

	private static final long serialVersionUID = 3190142731218702118L;

	@Id
	@Column(name = "empid")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long empId;
	
	@Column(name = "empname")
	private String empName;
}
