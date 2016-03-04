package org.acme.insurance.policyquote;

import static org.junit.Assert.assertEquals;

import org.acme.insurance.Driver;
import org.acme.insurance.Policy;
import org.acme.insurance.testing.BaseRulesTest;
import org.junit.Test;
import org.kie.api.runtime.rule.FactHandle;

/**
 * This is a sample class to test some rules.
 */
public class PolicyQuoteRulesTest extends BaseRulesTest {
	
	@Test
	public void riskyAdultsTest() {
		//now create some test data
		Driver driver= new Driver();
		driver.setAge(30);
		driver.setCreditScore(500);
		driver.setNumberOfAccidents(1);
		driver.setNumberOfTickets(0);
		Policy policy = new Policy();
		policy.setPolicyType("AUTO");
		policy.setVehicleYear(2004);
		// insert objects into working memory
		FactHandle driverFH = ksession.insert(driver);
		FactHandle policyFH = ksession.insert(policy);
		
		//fire rules in calculation rf group
		ksession.getAgenda().getAgendaGroup( "calculation" ).setFocus();
		ksession.fireAllRules();
		
		ksession.delete(driverFH);
		ksession.delete(policyFH);
		assertEquals("Price is 300", new Integer(300), policy.getPrice());
	}
	
	@Test
	public void riskyYouthsTest() {
		//now create some test data
		Driver driver= new Driver();
		driver.setAge(20);
		driver.setCreditScore(500);
		driver.setNumberOfAccidents(1);
		driver.setNumberOfTickets(0);
		Policy policy = new Policy();
		policy.setPolicyType("AUTO");
		policy.setVehicleYear(2004);
		// insert objects into working memory
		FactHandle driverFH = ksession.insert(driver);
		FactHandle policyFH = ksession.insert(policy);
		ksession.getAgenda().getAgendaGroup( "calculation" ).setFocus();
		ksession.fireAllRules();
		ksession.delete(driverFH);
		ksession.delete(policyFH);
		assertEquals("Price is 700", new Integer(700), policy.getPrice());
	}
	
	@Test
	public void safeAdultsTest() {
		//now create some test data
		Driver driver= new Driver();
		driver.setAge(30);
		driver.setCreditScore(500);
		driver.setNumberOfAccidents(0);
		driver.setNumberOfTickets(1);
		Policy policy = new Policy();
		policy.setPolicyType("AUTO");
		policy.setVehicleYear(2004);
		// insert objects into working memory
		FactHandle driverFH = ksession.insert(driver);
		FactHandle policyFH = ksession.insert(policy);
		ksession.getAgenda().getAgendaGroup( "calculation" ).setFocus();
		ksession.fireAllRules();
		ksession.delete(driverFH);
		ksession.delete(policyFH);
		assertEquals("Price is 120", new Integer(120), policy.getPrice());
	}
	
	@Test
	public void safeYouthsTest() {
		//now create some test data
		Driver driver= new Driver();
		driver.setAge(20);
		driver.setCreditScore(500);
		driver.setNumberOfAccidents(0);
		driver.setNumberOfTickets(0);
		Policy policy = new Policy();
		policy.setPolicyType("AUTO");
		policy.setVehicleYear(2004);
		// insert objects into working memory
		FactHandle driverFH = ksession.insert(driver);
		FactHandle policyFH = ksession.insert(policy);
		ksession.getAgenda().getAgendaGroup( "calculation" ).setFocus();
		ksession.fireAllRules();
		ksession.delete(driverFH);
		ksession.delete(policyFH);
		assertEquals("Price is 450", new Integer(450), policy.getPrice());
	}
}