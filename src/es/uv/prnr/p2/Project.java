package es.uv.prnr.p2;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.persistence.*;

import es.uv.prnr.p2.Employee;
import es.uv.prnr.p2.Manager;


//TODO JPQL de Ejercicio3 employeeInProject 
// @NamedQuery(
// 			name="Project.findEmployee",
// 			query = ""
// 			)

//TODO JPQL de Ejercicio3 getTopHoursMonth
// @NamedQuery(
// 			name="Project.getTopMonths",
// 			query=""
// )

//TODO Consulta SQL para getMonthly Budget. Se recomienda encarecidamente testearla con Workbench
//antes de incluirla aqu�
// @NamedNativeQuery(
// 		name="Project.getMonthlyBudget",
// 		query = "",
// 		resultSetMapping = "MonthBudgetMapping"
// )

//TODO Mapeo del ResultSet para la consulta anterior
/*@SqlResultSetMapping(
		name="MonthBudgetMapping",
		classes = {
			@ConstructorResult(
				targetClass=,
				columns= {
				}
			)	
	}
)*/

@Entity
@Table(name="project")
public class Project  {

	@Id
	private int id;
	
	private String name;
	
	@ManyToOne
	@JoinColumn(name = "fk_department", referencedColumnName = "dept_no", nullable = false)
	private Department department;
	
	private BigDecimal budget;
	
	@Column(name = "start_date")
	private LocalDate startDate;
	
	@Column(name = "end_date")
	private LocalDate endDate;

	private String area;
	
	@ManyToOne
	@JoinColumn(name = "fk_manager", referencedColumnName = "emp_no", nullable = false)
	private Manager manager;
	
	@ManyToMany
	@JoinTable(
		name = "project_team",
		joinColumns = @JoinColumn(name = "project_id", referencedColumnName = "id"),
		inverseJoinColumns = @JoinColumn(name = "employee_id", referencedColumnName = "emp_no")
	)
	private List<Employee> team = new ArrayList<>();

	@OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<ProjectHours> hours = new ArrayList<ProjectHours>();
	
	
	public Project() {
	}

	public Project(String name, Department department, Manager manager, BigDecimal budget, LocalDate startDate, LocalDate endDate, String area) {
		this.name = name;
		this.department = department;
		this.manager = manager;
		this.budget = budget;
		this.startDate = startDate;
		this.endDate = endDate;
		this.area = area;
	}
	
	/**
	 * Relaciona el proyecto con el empleado e
	 * @param e
	 */
	public void addEmployee(Employee e) {
		if (!team.contains(e)) {
			team.add(e);
			e.addProject(this);
		}
	}
	
	/**
	 * A�ade un numero de horas al empleado e para un mes-a�o concreto
	 * @param e
	 * @param month
	 * @param year
	 * @param hours
	 */
	public void addHours(Employee e, int month, int year, int hours) {
		ProjectHours projectHours = new ProjectHours(month, year, hours, e, this);
		this.hours.add(projectHours);
		e.addProject(this);
	}

	public int getId() {
		return id;
	}
	
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}
	

	public LocalDate getStartDate() {
		return startDate;
	}

	public void setStartDate(LocalDate startDate) {
		this.startDate = startDate;
	}

	public LocalDate getEndDate() {
		return endDate;
	}

	public void setEndDate(LocalDate endDate) {
		this.endDate = endDate;
	}

	public Department getDepartment() {
		return this.department;
	}

	public void setDepartment(Department Department) {
		this.department = Department;
	}

	public BigDecimal getBudget() {
		return this.budget;
	}

	public void setBudget(BigDecimal budget) {
		this.budget = budget;
	}

	public String getArea() {
		return this.area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public Manager getManager() {
		return manager;
	}

	public List<Employee> getEmployees() {
		return this.team;
	}

	
	public List<ProjectHours> getHours(){
		return this.hours;
	}
	
	public void print () {
		System.out.println("Project " + this.name + " from department " + this.department.getDeptName() );
		System.out.print("Managed by ");
		this.manager.print();
		System.out.println("Project Team");
	}

}
