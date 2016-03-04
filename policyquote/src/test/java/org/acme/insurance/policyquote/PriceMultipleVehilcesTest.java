package org.acme.insurance.policyquote;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.Collection;

import org.acme.insurance.Driver;
import org.acme.insurance.Policy;
import org.acme.insurance.Rejection;
import org.acme.insurance.testing.BaseRulesTest;
import org.junit.Test;
import org.kie.api.runtime.rule.FactHandle;

public class PriceMultipleVehilcesTest extends BaseRulesTest {
	
	@Test
	public void entireFlowTest() {
		Driver d1= new Driver();
		d1.setAge(30);
		d1.setCreditScore(715);
		d1.setNumberOfAccidents(0);
		d1.setNumberOfTickets(0);
		
		Policy p1 = new Policy();
		p1.setPolicyType("AUTO");
		p1.setVehicleYear(2000);
		p1.setDriver(d1);
		
		Policy p2 = new Policy();
		p2.setPolicyType("AUTO");
		p2.setVehicleYear(2001);
		p2.setDriver(d1);
		
		Policy pM = new Policy();
		pM.setPolicyType("MASTER");
		pM.setVehicleYear(0);
		pM.setDriver(d1);
		
		FactHandle driverFH = ksession.insert(d1);
		FactHandle policy1FH = ksession.insert(p1);
		FactHandle policy2FH = ksession.insert(p2);
		FactHandle policyMFH = ksession.insert(pM);
		
		ksession.getAgenda().getAgendaGroup( "rejection" ).setFocus();
		ksession.fireAllRules();
		
		ksession.getAgenda().getAgendaGroup( "calculation" ).setFocus();
		ksession.fireAllRules();
		
		ksession.getAgenda().getAgendaGroup( "surcharge" ).setFocus();
		ksession.fireAllRules();
		
		ksession.getAgenda().getAgendaGroup( "discount" ).setFocus();
		ksession.fireAllRules();
		
		ksession.getAgenda().getAgendaGroup( "total" ).setFocus();
		ksession.fireAllRules();
		
		ksession.delete(driverFH);
		ksession.delete(policy1FH);
		ksession.delete(policy2FH);
		ksession.delete(policyMFH);
		
		assertEquals(p1.getPrice(), new Integer(120));
		assertEquals(p1.getPriceDiscount(), new Integer(50));
		
		assertEquals(p2.getPrice(), new Integer(120));
		assertEquals(p2.getPriceDiscount(), new Integer(50));
		
		assertEquals(pM.getPrice(), new Integer(240));
		
	}
	
	//Same Test as above except uses the process
	@Test
	public void processTest(){
		Driver d1= new Driver();
		d1.setAge(30);
		d1.setCreditScore(715);
		d1.setNumberOfAccidents(0);
		d1.setNumberOfTickets(0);
		
		Policy p1 = new Policy();
		p1.setPolicyType("AUTO");
		p1.setVehicleYear(2000);
		p1.setDriver(d1);
		
		Policy p2 = new Policy();
		p2.setPolicyType("AUTO");
		p2.setVehicleYear(2001);
		p2.setDriver(d1);
		
		Policy pM = new Policy();
		pM.setPolicyType("MASTER");
		pM.setVehicleYear(0);
		pM.setDriver(d1);
		
		FactHandle driverFH = ksession.insert(d1);
		FactHandle policy1FH = ksession.insert(p1);
		FactHandle policy2FH = ksession.insert(p2);
		FactHandle policyMFH = ksession.insert(pM);
		
		ksession.startProcess("policyquote.policyquotecalculationprocess");
		ksession.fireAllRules();
		
		ksession.delete(driverFH);
		ksession.delete(policy1FH);
		ksession.delete(policy2FH);
		ksession.delete(policyMFH);
		
		assertEquals(p1.getPrice(), new Integer(120));
		assertEquals(p1.getPriceDiscount(), new Integer(50));
		assertEquals(p2.getPrice(), new Integer(120));
		assertEquals(p2.getPriceDiscount(), new Integer(50));
		assertEquals(pM.getPrice(), new Integer(240));
	}
	
	@Test
	public void rejectionThruProcess(){
		Driver d1= new Driver();
		d1.setAge(14);
		d1.setCreditScore(715);
		d1.setNumberOfAccidents(0);
		d1.setNumberOfTickets(0);
		
		Policy p1 = new Policy();
		p1.setPolicyType("AUTO");
		p1.setVehicleYear(2000);
		p1.setDriver(d1);
		
		FactHandle driverFH = ksession.insert(d1);
		FactHandle policy1FH = ksession.insert(p1);
		
		ksession.startProcess("policyquote.policyquotecalculationprocess");
		
		ksession.fireAllRules();
		
		ksession.delete(driverFH);
		ksession.delete(policy1FH);

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
		assertNull(p1.getPrice());
	}
}
