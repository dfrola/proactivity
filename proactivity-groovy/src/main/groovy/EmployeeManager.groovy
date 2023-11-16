import com.proactivity.groovy.dto.request.EmployeeDTO;

class EmployeeManager {
  
    EmployeeDTO adjustName(EmployeeDTO employee) {
       
        employee.setName( employee.getName() + " - miao");
        
        return employee;
    } 
    
    EmployeeDTO create() {
    
    	EmployeeDTO employee = new EmployeeDTO();
       
        employee.setName("Davide");
        employee.setLastName("Frola");
        employee.setRepository("Hazelcast");
        
        return employee;
    }    
}