package org.acme.insurance.processes;

import org.acme.insurance.Driver;
import org.acme.insurance.Policy;
import org.jbpm.test.JbpmJUnitTestCase;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.kie.api.KieServices;
import org.kie.api.logger.KieRuntimeLogger;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.process.ProcessInstance;
import org.kie.api.runtime.rule.FactHandle;

public class ProcessTest extends JbpmJUnitTestCase {

	protected static KieSession ksession;
	protected static KieRuntimeLogger klogger;

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

	//@Test
	public void testSimple(){

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

		ProcessInstance processInstance = ksession.startProcess("policyquote.policyquotecalculationprocess");

		ksession.delete(driverFH);
		ksession.delete(policy1FH);
		ksession.delete(policy2FH);
		ksession.delete(policyMFH);

		// check whether the process instance has completed successfully
		//assertProcessInstanceCompleted(processInstance.getId(), ksession);
		//8314644222

		// check whether the given nodes were executed during the process execution
		assertNodeTriggered(processInstance.getId(), "StartProcess");
	}

	//@Test
	public void testProcess() {

		// create your session and load the given process(es)

		//StatefulKnowledgeSession ksession = createKnowledgeSession("sample.bpmn");

		// start the process

		ProcessInstance processInstance = ksession.startProcess("process_1");
		ksession.fireAllRules();

		// check whether the process instance has completed successfully

		//assertProcessInstanceCompleted(processInstance.getId(), ksession);

		// check whether the given nodes were executed during the process execution

		assertNodeTriggered(processInstance.getId(), "StartEvent_1");

	}

}
