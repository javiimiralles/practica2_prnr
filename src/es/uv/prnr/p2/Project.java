package es.uv.prnr.p2;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.*;


@NamedQuery(
	name="Project.findEmployee",
	query="SELECT e FROM Project p JOIN p.team e WHERE p.id = :projectId AND e.firstName = :firstName AND e.last_name = :lastName"
)

@NamedQuery(
	name="Project.getTopMonths",
	query="SELECT ph.month, SUM(ph.hours) as totalHours " +
			"FROM ProjectHours ph " +
			"WHERE ph.project.id = :projectId AND ph.year = :year " +
			"GROUP BY ph.month " +
			"ORDER BY totalHours DESC"
)



@NamedNativeQuery(
	name="Project.getMonthlyBudget",
	query = "SELECT ph.month, ph.year, SUM(ph.hours * e.hourly_rate) as totalBudget " +
			"FROM ProjectHours ph " +
			"JOIN Employee e ON ph.employee_id = e.emp_no " +
			"WHERE ph.project_id = :projectId " +
			"GROUP BY ph.month, ph.year " +
			"ORDER BY ph.year, ph.month",
	resultSetMapping = "MonthBudgetMapping"
)

@SqlResultSetMapping(
	name="MonthBudgetMapping",
	classes = {
		@ConstructorResult(
			targetClass=MonthlyBudget.class,
			columns= {
				@ColumnResult(name="month", type=Integer.class),
				@ColumnResult(name="year", type=Integer.class),
				@ColumnResult(name="totalBudget", type=BigDecimal.class)
			}
		)
	}
)

@Entity
@Table(name="project")
public class Project  {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private int id;
	
	@Column(name = "name", nullable = false)
	private String name;
	
	@ManyToOne
	@JoinColumn(name = "fk_department", referencedColumnName = "dept_no", nullable = false)
	private Department department;
	
	@Column(name = "budget", nullable = false)
	private BigDecimal budget;
	
	@Column(name = "start_date")
	private LocalDate startDate;
	
	@Column(name = "end_date")
	private LocalDate endDate;

	@Column(name = "area", nullable = false)
	private String area;
	
	@ManyToOne
	@JoinColumn(name = "fk_manager", nullable = false)
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
