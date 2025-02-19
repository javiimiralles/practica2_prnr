package es.uv.prnr.p2;

import javax.persistence.*;


@Entity
@Table(name = "project_hours")
public class ProjectHours {
	int id;
	int month;
	int year;
	int hours;

	@ManyToOne
	@JoinColumn(name = "employee_id", nullable = false)
	Employee employee;

	@ManyToOne
	@JoinColumn(name = "project_id", nullable = false)
	Project project;
	
	public ProjectHours() {
		
	}
	
	public ProjectHours(int month, int year, int hours, Employee employee, Project project) {
		this.month = month;
		this.year = year;
		this.hours = hours;
		this.employee = employee;
		this.project = project;
	}

	public int getHours() {
		return hours;
	}

	public void setHours(int hours) {
		this.hours = hours;
	}

	public int getMonth() {
		return month;
	}

	public int getYear() {
		return year;
	}

	public Employee getEmployee() {
		return employee;
	}

	public Project getProject() {
		return project;
	}
	
	
	
}
