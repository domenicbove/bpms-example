package org.acme.insurance.policyquote;

import static org.junit.Assert.assertEquals;

import org.acme.insurance.Driver;
import org.acme.insurance.Policy;
import org.acme.insurance.testing.BaseRulesTest;
import org.junit.Test;
import org.kie.api.runtime.rule.FactHandle;

public class SurchargeTest extends BaseRulesTest {

	@Test
	public void accidentSurchargeTest(){
		Driver driver = new Driver();
		driver.setNumberOfAccidents(3);
		Policy policy = new Policy();
		policy.setPrice(100);
		policy.setPolicyType("AUTO");
		
		FactHandle driverFH = ksession.insert(driver);
		FactHandle policyFH = ksession.insert(policy);
		ksession.fireAllRules();
		ksession.delete(driverFH);
		ksession.delete(policyFH);
		
		assertEquals(policy.getPrice(), new Integer(100+200));
	}
	
	@Test
	public void noviceDriverTest(){
		Driver driver = new Driver();
		driver.setAge(19);
		Policy policy = new Policy();
		policy.setPrice(100);
		policy.setPolicyType("AUTO");
		
		FactHandle driverFH = ksession.insert(driver);
		FactHandle policyFH = ksession.insert(policy);
		ksession.fireAllRules();
		ksession.delete(driverFH);
		ksession.delete(policyFH);
		
		assertEquals(policy.getPrice(), new Integer(100+250));
	}
	
	@Test
	public void newerVehicleTest(){
		Policy policy = new Policy();
		policy.setPrice(100);
		policy.setPolicyType("AUTO");
		policy.setVehicleYear(2006);
		
		FactHandle policyFH = ksession.insert(policy);
		ksession.fireAllRules();
		ksession.delete(policyFH);
		
		assertEquals(policy.getPrice(), new Integer(100+100));
	}
	
}
