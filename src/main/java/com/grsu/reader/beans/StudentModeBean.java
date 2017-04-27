package com.grsu.reader.beans;

import com.grsu.reader.entities.Stream;
import com.grsu.reader.entities.Student;
import lombok.Data;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import java.io.Serializable;

/**
 * Created by zaychick-pavel on 4/27/17.
 */
@ManagedBean(name = "studentModeBean")
@ViewScoped
@Data
public class StudentModeBean implements Serializable {
	private Student student;
	private Stream stream;
}
