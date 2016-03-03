package org.acme.insurance.exclusion;

import static org.junit.Assert.assertEquals;

import java.util.Collection;

import org.acme.insurance.Driver;
import org.acme.insurance.Rejection;
import org.acme.insurance.testing.BaseRulesTest;
import org.junit.Test;
import org.kie.api.runtime.rule.FactHandle;

public class ExclusionTest extends BaseRulesTest {
	
	@Test
	public void tooYoungTest() {
		Driver driver= new Driver();
		driver.setAge(14);
		FactHandle driverFH = ksession.insert(driver);
		
		ksession.getAgenda().getAgendaGroup( "rejection" ).setFocus();
		ksession.fireAllRules();
		ksession.delete(driverFH);
		
		Rejection r = null;
		Collection objects = ksession.getObjects();
		for(Object o: objects){
			if(o instanceof Rejection){
				r = (Rejection) o;
				break;
			}
		}
		
		for(FactHandle f: ksession.getFactHandles()){
			ksession.delete(f);
		}
		
		assertEquals(r.getReason(), "Too Young");
	}
	
	@Test
	public void tooOldTest() {
		Driver driver= new Driver();
		driver.setAge(114);
		FactHandle driverFH = ksession.insert(driver);
		
		ksession.getAgenda().getAgendaGroup( "rejection" ).setFocus();
		ksession.fireAllRules();
		ksession.delete(driverFH);
		
		Rejection r = null;
		Collection objects = ksession.getObjects();
		for(Object o: objects){
			if(o instanceof Rejection){
				r = (Rejection) o;
				break;
			}
		}
		assertEquals(r.getReason(), "Too Old");
		
		for(FactHandle f: ksession.getFactHandles()){
			ksession.delete(f);
		}
	}
	
	@Test
	public void tooManyTicketsTest() {
		Driver driver= new Driver();
		driver.setNumberOfTickets(8);
		FactHandle driverFH = ksession.insert(driver);
		
		ksession.getAgenda().getAgendaGroup( "rejection" ).setFocus();
		ksession.fireAllRules();
		ksession.delete(driverFH);
		
		Rejection r = null;
		Collection objects = ksession.getObjects();
		for(Object o: objects){
			if(o instanceof Rejection){
				r = (Rejection) o;
				break;
			}
		}
		assertEquals(r.getReason(), "Too Many Tickets");
		
		for(FactHandle f: ksession.getFactHandles()){
			ksession.delete(f);
		}
	}
	
	@Test
	public void tooManyAccidentsTest() {
		Driver driver= new Driver();
		driver.setNumberOfAccidents(10);
		FactHandle driverFH = ksession.insert(driver);
		
		ksession.getAgenda().getAgendaGroup( "rejection" ).setFocus();
		ksession.fireAllRules();
		ksession.delete(driverFH);
		
		Rejection r = null;
		Collection objects = ksession.getObjects();
		for(Object o: objects){
			if(o instanceof Rejection){
				r = (Rejection) o;
				break;
			}
		}
		assertEquals(r.getReason(), "Too Many Accidents");
		
		for(FactHandle f: ksession.getFactHandles()){
			ksession.delete(f);
		}
	}
}
