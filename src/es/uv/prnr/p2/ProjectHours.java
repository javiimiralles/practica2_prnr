package es.uv.prnr.p2;

import javax.persistence.*;


@Entity
@Table(name = "monthly_hours")
public class ProjectHours {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	int id;

	@Column(name = "month", nullable = false)
	int month;
	
	@Column(name = "year", nullable = false)
	int year;

	@Column(name = "hours", nullable = false)
	int hours;

	@ManyToOne
	@JoinColumn(name = "fk_employee", nullable = false)
	Employee employee;

	@ManyToOne
	@JoinColumn(name = "fk_project", nullable = false)
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
