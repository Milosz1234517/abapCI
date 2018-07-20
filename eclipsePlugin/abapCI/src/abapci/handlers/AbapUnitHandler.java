package abapci.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.preference.IPreferenceStore;

import com.sap.adt.communication.exceptions.CommunicationException;
import com.sap.adt.tools.abapsource.abapunit.AbapUnitTask;
import com.sap.adt.tools.abapsource.abapunit.AbapUnitTestDuration;
import com.sap.adt.tools.abapsource.abapunit.AbapUnitTestRiskLevel;
import com.sap.adt.tools.abapsource.abapunit.IAbapUnitResult;
import com.sap.adt.tools.abapsource.abapunit.IAbapUnitService;
import com.sap.adt.tools.abapsource.abapunit.TestItem;
import com.sap.adt.tools.abapsource.abapunit.TestRunException;
import com.sap.adt.tools.abapsource.abapunit.services.AdtServicesPlugin;
import com.sap.adt.tools.abapsource.abapunit.services.IAdtServicesFactory;

import abapci.AbapCiPlugin;
import abapci.domain.UnitTestResultSummary;
import abapci.feature.FeatureFacade;
import abapci.preferences.PreferenceConstants;
import abapci.result.TestResultSummaryFactory;

/**
 * Our sample handler extends AbstractHandler, an IHandler base class.
 *
 * @see org.eclipse.core.commands.IHandler
 * @see org.eclipse.core.commands.AbstractHandler
 */
public class AbapUnitHandler extends AbstractHandler {
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		String packageName = event.getParameter("1");
		return executePackage(packageName);
	}

	public UnitTestResultSummary executePackage(String packageName) {

		IPreferenceStore prefs = AbapCiPlugin.getDefault().getPreferenceStore();
		String destinationId = prefs.getString(PreferenceConstants.PREF_DEV_PROJECT);
		boolean flag = false;
		IAdtServicesFactory servicesFactory = AdtServicesPlugin.getDefault().getFactory();
		IAbapUnitService abapUnitService = servicesFactory.createAbapUnitService(destinationId, flag);
		AbapUnitTask task = new AbapUnitTask(packageName);

		String testobjectUrl = "/sap/bc/adt/vit/wb/object_type/devck/object_name/" + packageName;
		TestItem itemObject = new TestItem(testobjectUrl, testobjectUrl);
		task.addTestItem(itemObject);

		/**
		 * Unit test level selection seems currently not supported ( at least with 7.50)
		 * - therefore its deactived for the moment int riskLevels =
		 * evaluateRiskLevels();
		 * task.setRiskLevels(AbapUnitTestRiskLevel.getAsEnum(riskLevels));
		 * 
		 * int durations = evaluateDurations();
		 * task.setDurations(AbapUnitTestDuration.getAsEnum(durations));
		 */

		UnitTestResultSummary unitTestResultSummary;

		try {
			IAbapUnitResult abapUnitResult = abapUnitService.executeUnitTests(task, false, packageName);
			return TestResultSummaryFactory.create(packageName, abapUnitResult);
		} catch (TestRunException e) {
			if (e.getCause() instanceof CommunicationException) {
				unitTestResultSummary = TestResultSummaryFactory.createOffline(packageName);
			} else {
				unitTestResultSummary = TestResultSummaryFactory.createUndefined(packageName);
			}
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (CommunicationException e) {
			e.printStackTrace();
			unitTestResultSummary = TestResultSummaryFactory.createUndefined(packageName);
		} catch (Exception ex) {
			unitTestResultSummary = TestResultSummaryFactory.createUndefined(packageName);
		}

		return unitTestResultSummary;
	}

	private int evaluateDurations() {
		int durations = 0;
		FeatureFacade featureFacade = new FeatureFacade();

		if (featureFacade.getUnitTestDurationLongFeature().isActive()) {
			durations = durations | AbapUnitTestDuration.LONG.getAsIntFlag();
		}

		if (featureFacade.getUnitTestDurationMediumFeature().isActive()) {
			durations = durations | AbapUnitTestDuration.MEDIUM.getAsIntFlag();
		}

		if (featureFacade.getUnitTestDurationShortFeature().isActive()) {
			durations = durations | AbapUnitTestDuration.SHORT.getAsIntFlag();
		}

		return durations;
	}

	private int evaluateRiskLevels() {
		int riskLevels = 0;
		FeatureFacade featureFacade = new FeatureFacade();

		if (featureFacade.getUnitCriticalActiveFeature().isActive()) {
			riskLevels = riskLevels | AbapUnitTestRiskLevel.CRITICAL.getAsIntFlag();
		}

		if (featureFacade.getUnitDangerousActiveFeature().isActive()) {
			riskLevels = riskLevels | AbapUnitTestRiskLevel.DANGEROUS.getAsIntFlag();
		}

		if (featureFacade.getUnitHarmlessActiveFeature().isActive()) {
			riskLevels = riskLevels | AbapUnitTestRiskLevel.HARMLESS.getAsIntFlag();
		}

		return riskLevels;
	}
}
