package com.lhs.documentTextExtraction.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "user_mast")
public class UserMast implements java.io.Serializable{

	
	private static final long serialVersionUID = 7925328400089676957L;

	@Id
	@Column(name = "user_code" ,length=15,nullable = false)
	private String user_code;

	@Column(name = "password" ,length=100,nullable = false)
	private String password;
	
	 
	
	
}
