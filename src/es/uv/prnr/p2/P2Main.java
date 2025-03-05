package es.uv.prnr.p2;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;


/**
 * Clase principal para probar la ejecuci�n de ProjectService
 * Se recomienda descomentar el c�digo de los ejercicios conforme se vayan realizando
 * @author Paco
 *
 */
public class P2Main {

	public static void main(String[] args) {
		
		ProjectService service = new ProjectService();
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("acme");
		EntityManager em = emf.createEntityManager();
		em.getTransaction().begin();
		
		/* Comprobar funcionamiento */
		Employee e = em.find(Employee.class, 222222);
		e.print();
		
		Employee newEmployee = new Employee(1, "Edgar", "Cood",
				LocalDate.of(1923,8,19), LocalDate.now(), Employee.Gender.M);	
		em.persist(newEmployee);
		e = em.find(Employee.class,1);
		e.print();
		em.remove(e);
		em.getTransaction().commit();
		
		/* Ejercicio 2*/
		
		Department proyDepartment = service.getDepartmentById("d005");
		System.out.println("Departamento encontrado: " + proyDepartment);
		System.out.println("Nombre del departamento: " + proyDepartment.getDeptName());
		
		Manager projectManager = service.promoteToManager(10003, 1000L);
		System.out.println("Manager: " + projectManager.getFirstName() + " " + projectManager.getLastName());
		Project acmeProject = service.createBigDataProject("New Big Data Project",proyDepartment,projectManager,new BigDecimal(1500000.99));
		System.out.println("Project: " + acmeProject.getName());
		
		service.assignTeam(acmeProject,10003,10007);

		int totalHours = service.assignInitialHours(acmeProject.getId());
		System.out.println("Total project hours: " + totalHours);
	
		/*
		 * Ejercicio 3. Prueba de consultas
		 *
		*/
		
		if(service.employeeInProject(acmeProject.getId(), "Parto", "Bamford"))
			System.out.println("Parto Bamford assigned to project");
		if(!service.employeeInProject(acmeProject.getId(), "Luke", "Johnson"))
			System.out.println("Luke Johnson is not assigned to project");
			
		 
		// List<Object[]>results = service.getTopHourMonths(acmeProject.getId(), 2025, 3);
		// for(Object[] result : results) {
		// 	System.out.println("Month " + result[0] + " Hours " + result[1]);
		// }
		
		
		List<MonthlyBudget> monthBudgets = service.getMonthlyBudget(acmeProject.getId());
		for(MonthlyBudget budget : monthBudgets) {
			System.out.println("Comienza ejercicio 3 /n");
			System.out.println (budget.getMonth()+"-"+budget.getYear()+" : "+budget.getAmount()+"�");
		}
		
		/* Eliminamos la informaci�n creada */
		em.getTransaction().begin();
		Manager m = em.merge(projectManager);
		Project p = em.merge(acmeProject);
		em.remove(p);
		em.remove(m);
		em.getTransaction().commit();
		
		return;
	}
	

}
