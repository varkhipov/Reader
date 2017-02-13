package com.grsu.reader.models;

public class Department extends Entity {
	private int id;
	private String name;
	private String abbreviation;

	public Department() {
	}

	public Department(String name) {
		this.name = name;
	}

	public Department(int id, String name, String abbreviation) {
		this.id = id;
		this.name = name;
		this.abbreviation = abbreviation;
	}

	public Department(int id, String name) {
		this(id, name, null);
	}

	public Department(Department department) {
		this(department.getId(), department.getName(), department.getAbbreviation());
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAbbreviation() {
		return abbreviation;
	}

	public void setAbbreviation(String abbreviation) {
		this.abbreviation = abbreviation;
	}

	@Override
	public String toString() {
		return "Department{" +
				"id=" + id +
				", name='" + name + '\'' +
				", abbreviation='" + abbreviation + '\'' +
				'}';
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		Department that = (Department) o;

		if (id != that.id) return false;
		if (name != null ? !name.equals(that.name) : that.name != null) return false;
		return abbreviation != null ? abbreviation.equals(that.abbreviation) : that.abbreviation == null;
	}

	@Override
	public int hashCode() {
		int result = id;
		result = 31 * result + (name != null ? name.hashCode() : 0);
		result = 31 * result + (abbreviation != null ? abbreviation.hashCode() : 0);
		return result;
	}
}
