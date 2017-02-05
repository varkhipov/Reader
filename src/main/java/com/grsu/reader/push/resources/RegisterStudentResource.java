package com.grsu.reader.push.resources;

import com.grsu.reader.models.Student;
import org.primefaces.push.annotation.OnMessage;
import org.primefaces.push.annotation.PushEndpoint;
import org.primefaces.push.impl.JSONEncoder;

@PushEndpoint("/register")
public class RegisterStudentResource {

	@OnMessage(encoders = {JSONEncoder.class})
	public Student onMessage(Student student) {
		return student;
	}
}
