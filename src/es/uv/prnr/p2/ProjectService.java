package es.uv.prnr.p2;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.persistence.*;


public class ProjectService {
	EntityManagerFactory emf;
	EntityManager em;
	
	public ProjectService() {
		this.emf = Persistence.createEntityManagerFactory("acme");
		this.em = emf.createEntityManager();
	}
	
	/** TODO
	 * Busca un departamento
	 * @param id identificador del departamento	
	 * @return entidad con el deparamenteo encontrado
	 */
	public Department getDepartmentById (String id) {
		return em.find(Department.class, id);
	}
	
	/** 
	 * Asciende a un empleado a manager. Utilizar una estrateg�a de herencia adecuada
	 * en employee. Tened en cuenta que NO puede haber dos entidades con el mismo id
	 * por lo que habr� que eliminar el empleado original en algun momento.
	 * @param employeeId
	 * @param bonus
	 * @return
	 */
	public Manager promoteToManager(int employeeId, long bonus) {
		em.getTransaction().begin();
		Employee employee = em.find(Employee.class, employeeId);
		Manager manager = new Manager(employee, bonus);
		em.remove(employee);
		em.persist(manager);
		em.getTransaction().commit();
		return manager;
	}
	
	/** 
	 * Crea un nuevo proyecto en el area de Big Data que comienza en la fecha actual y que finaliza
	 * en 3 a�os.
	 * @param name 
	 * @param d departamento asignado al proyecto
	 * @param m manager que asignado al proyecto
	 * @param budget
	 * @return el proyecto creado
	 */
	public Project createBigDataProject(String name, Department d, Manager m, BigDecimal budget) {
		LocalDate startDate = LocalDate.now();
		LocalDate endDate = startDate.plusYears(3);
		Project project = new Project(name ,d, m, budget, startDate,endDate, "Big Data" );
		em.getTransaction().begin();
		em.persist(project);
		em.getTransaction().commit();
		return project;
	}
	
	/**TODO
	 * Crea un equipo de proyecto. Se debera implementa el m�todo addEmployee de 
	 * Project para incluir los empleados
	 * @param p proyecto al cual asignar el equipo
	 * @param startId identificador a partir del cual se asignan empleado
	 * @param endId identificador final de empleados. Se asume que start id < endId
	 */
	public void assignTeam (Project p, int startId, int endId) {
		p.getEmployees().clear();
		em.getTransaction().begin();

		for (int i = startId; i <= endId; i++) {
			Employee e = em.find(Employee.class, i);
			if (e != null && !p.getEmployees().contains(e)) {
				p.addEmployee(e);
				em.merge(p);
			}
		}
		em.getTransaction().commit();
	}
	
	/**
	 * Genera un conjunto de horas inicial para cada empleado. El m�todo asigna para cada
	 * mes de duraci�n del proyecto, un n�mero entre 10-165 de horas a cada empleado.
	 * @param projectId
	 * @return total de horas generadas para el proyecto
	 */
	public int assignInitialHours (int projectId) {
		int totalHours = 0;
		Project p = em.find(Project.class, projectId);
		LocalDate start = p.getStartDate();
		while (start.isBefore(p.getEndDate())) {
			for (Employee e: p.getEmployees()) {
				int hours = new Random().nextInt(165) + 10; 
				totalHours += hours;
				p.addHours(e, start.getMonthValue(), start.getYear(), hours);
			}
			start = start.plusMonths(1);
		}
		em.getTransaction().begin();
		em.persist(p);
		em.getTransaction().commit();
		return totalHours;
	}
	
	/**
	 * Busca si un empleado se encuentra asignado en el proyecto utilizando la
	 * namedQuery Project.findEmployee
	 * @param projectId
	 * @param firstName
	 * @param lastName
	 * @return cierto si se encuentra asignado al proyecto
	 */
	public boolean employeeInProject (int projectId, String firstName, String lastName){
		Query query = em.createNamedQuery("Project.findEmployee");
		query.setParameter("projectId", projectId);
		query.setParameter("firstName", firstName);
		query.setParameter("lastName", lastName);
		return !query.getResultList().isEmpty();
	}
	
	/**TODO
	 * Devuelve los meses con mayor n�mero de horas de un a�o determinado
	 * utilizando la namedQuery Project.getTopMonths
	 * @param projectId
	 * @param year a�o a seleccionar
	 * @param rank n�mero de meses a mostrar, se asume que rank <= 12
	 * @return una lista de objetos mes,hora ordenados de mayor a menor
	 */
	public List<Object[]> getTopHourMonths(int projectId, int year, int rank) {
		Query query = em.createNamedQuery("Project.getTopMonths");
		query.setParameter("projectId", projectId);
		query.setParameter("year", year);
		query.setMaxResults(rank);
		return query.getResultList();
	}
	
	/**TODO
	 * Devuelve para cada par mes-a�o el presupuesto teniendo en cuenta el 
	 * coste/hora de los empleados asociado utilizando la namedQuery Project.getMonthlyBudget
	 * que realiza una consulta nativa
	 * @param projectId
	 * @return una colecci�n de objetos MonthlyBudget
	 */
	public List<MonthlyBudget> getMonthlyBudget (int projectId){
		Query query = em.createNamedQuery("Project.getMonthlyBudget");
		query.setParameter("projectId", projectId);
		return query.getResultList();
	}
	
}
