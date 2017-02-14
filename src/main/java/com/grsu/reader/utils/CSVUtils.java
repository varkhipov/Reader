package com.grsu.reader.utils;

import com.grsu.reader.dao.EntityDAO;
import com.grsu.reader.dao.GroupDAO;
import com.grsu.reader.dao.StudentDAO;
import com.grsu.reader.entities.Department;
import com.grsu.reader.entities.Group;
import com.grsu.reader.entities.Student;
import com.opencsv.CSVReader;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.grsu.reader.utils.FileUtils.CSV_EXTENSION;
import static org.apache.commons.lang3.StringUtils.lowerCase;
import static org.apache.commons.lang3.StringUtils.capitalize;

/**
 * This class parses provided *.csv files with group students and adds them to database if needed.
 * Example of *.csv file is named 'Group-Sample.csv' and can be found in resources/samples folder.
 * To be processed files must be located at ../tomcat_home/app_files/csv/.
 */

public class CSVUtils {
	private static final char SEPARATOR = ';';

	// TODO: analyze once again
	public static void updateGroupsFromCSV() {
		EntityDAO entityDAO = new EntityDAO();
		for (Group group : parseGroups()) {
			if (group.getStudents().isEmpty()) {
				System.out.println("Group [ " + group.getName() + " ] is empty and not added to database.");
				continue;
			}

			if (group.getDepartment().getName() != null) {
				Department department = null;
				List<Department> departments = entityDAO.getAll(Department.class);
				for (Department d : departments) {
					if (group.getDepartment().getName().equals(d.getName())) {
						department = d;
						break;
					}
				}

				if (department == null) {
					department = group.getDepartment();
					entityDAO.add(department);
				}

				group.setDepartment(department);
			}

			List<Student> students = new ArrayList<>(group.getStudents());
			Group groupFromDB = new GroupDAO().getByName(group.getName());
			if (groupFromDB != null) {
				System.out.println("Group [ " + group.getName() + " ] already exists. Updating...");
				group = groupFromDB;
			}
			group.getStudents().clear();
			entityDAO.save(group);
			processStudents(group, students);
			System.out.println("Group [ " + group.getName() + " ] processed.");
		}
	}

	private static void processStudents(Group group, List<Student> students) {
		StudentDAO studentDAO = new StudentDAO();
		for (Student student : students) {
			Student studentFromDB = studentDAO.getByCardUid(student.getCardUid());
			if (studentFromDB == null) {
				studentDAO.add(student);
				student.setGroups(new ArrayList<>());
			} else {
				studentFromDB.setLastName(student.getLastName());
				studentFromDB.setFirstName(student.getFirstName());
				studentFromDB.setPatronymic(student.getPatronymic());
				studentFromDB.setCardId(student.getCardId());
				student = studentFromDB;
				studentDAO.update(student);
			}
			student.getGroups().add(group);
			studentDAO.update(student);
		}
	}

	private static CSVReader getReader(File file) {
		CSVReader reader = null;
		try {
			BufferedReader in = new BufferedReader(
					new InputStreamReader(
							new FileInputStream(file), "Cp1251")
			);

			reader = new CSVReader(in, SEPARATOR);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return reader;
	}

	private static List<Group> parseGroups() {
		List<File> files = FileUtils.getCSVFilesFromAppFilesFolder();
		if (files.isEmpty()) {
			System.out.println("CSV files not found. Processing skipped.");
			return Collections.emptyList();
		}

		System.out.println("CSV files founded. Processing...");
		List<Group> groups = new ArrayList<>();
		for (File file : files) {
			Group group = parseGroup(file);
			if (group != null) {
				groups.add(group);
			}
		}
		return groups;
	}

	private static Group parseGroup(File file) {
		CSVReader reader = getReader(file);
		if (reader == null) return null;

		String fileName = file.getName();
		String groupName = fileName.substring(0, fileName.length() - CSV_EXTENSION.length());

		Group group = null;
		String departmentName = null;
		List<Student> students = new ArrayList<>();

		try {
			reader.readNext(); //skip first line, it contains only column headers
			String[] line;
			boolean departmentFound = false;

			while ((line = reader.readNext()) != null) {
				Student student = parseStudent(line);
				if (student != null) {
					students.add(student);

					if (!departmentFound && line[5] != null && !line[5].isEmpty()) {
						departmentName = line[5];
						departmentFound = true;
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		if (!students.isEmpty()) {
			group = new Group();
			group.setName(groupName);
			group.setStudents(students);
			Department department = new Department();
			department.setName(departmentName);
			group.setDepartment(department);
		} else {
			System.out.println("Group [ " + groupName + " ] is empty and not added to database.");
		}

		try {
			reader.close();
			if (file.delete()) {
				System.out.println("File [" + fileName + "] is deleted.");
			} else {
				System.out.println("Delete operation for file [" + fileName + "] is failed.");
			}
		} catch (IOException e) {
			System.out.println("Warning! Close stream for file [" + fileName + "] failed!");
		}

		return group;
	}

	private static Student parseStudent(String[] record) {
		if (record[0] == null || record[0].isEmpty()) {
			return null;
		}
		Student student = new Student();
		student.setLastName(capitalize(lowerCase(record[0])));
		student.setFirstName(capitalize(lowerCase(record[1])));
		student.setPatronymic(capitalize(lowerCase(record[2])));

		String parsedUid = record[3];
		if (parsedUid == null || parsedUid.isEmpty()) {
			System.out.println("No card ID for [ " + student.getFullName() + " ]. Need to update UID manually.");
			student.setCardUid("0");
		} else {
			student.setCardId(Integer.parseInt(record[3]));
		}
		return student;
	}
}
