package org.acme.insurance.testing;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.kie.api.KieServices;
import org.kie.api.logger.KieRuntimeLogger;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;

public abstract class BaseRulesTest {
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
}
