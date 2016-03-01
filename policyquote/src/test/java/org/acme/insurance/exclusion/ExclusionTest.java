package org.acme.insurance.exclusion;

import static org.junit.Assert.assertEquals;

import java.util.Collection;

import org.acme.insurance.Driver;
import org.acme.insurance.Policy;
import org.acme.insurance.Rejection;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.kie.api.KieBase;
import org.kie.api.KieServices;
import org.kie.api.logger.KieRuntimeLogger;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.rule.FactHandle;

public class ExclusionTest {

	static KieBase kbase;
	static KieSession ksession;
	static KieRuntimeLogger klogger;
	
	@BeforeClass
	public static void setupKsession() {
		try {
			// load up the knowledge base and create session
			ksession = readKnowledgeBase();
			System.out.println("setupKsession() ksession  = "+ksession);
			klogger = KieServices.Factory.get().getLoggers().newFileLogger(ksession, "src/test/java/org/acme/insurance/policyquote/policyQuote");
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}
	
	@Test
	public void tooYoungTest() {
		Driver driver= new Driver();
		driver.setAge(14);
		FactHandle driverFH = ksession.insert(driver);
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
	
	@AfterClass
	public static void closeKsession() {
		try {
			// closing resources
			klogger.close();
			ksession.dispose();
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}
	
	private static KieSession readKnowledgeBase() throws Exception {
		KieServices ks = KieServices.Factory.get();
		KieContainer kContainer = ks.getKieClasspathContainer();
		KieSession kSession = kContainer.newKieSession();
		return kSession;
	}
}
