package abapci.handlers;

import java.net.URI;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IProject;
import org.eclipse.jdt.core.IClassFile;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import abapci.activation.Activation;
import abapci.domain.ErrorPriority;
import abapci.domain.InvalidItem;
import abapci.handlers.infinitest.Junit4And3Runner;
import abapci.handlers.infinitest.TestEvent;
import abapci.handlers.infinitest.TestResults;
import abapci.testResult.TestResultSummary;

public class JavaUnitHandler implements IUnitHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TestResultSummary executePackage(IProject project, String packageName, Set<Activation> activations) {
 
		//Method prepared for implementation with Code Mining - for now dummy implementation for E2E testing purposes  		
		return new TestResultSummary(project, packageName, true, 0, new ArrayList<InvalidItem>(), activations);

	}

	@Override
	public TestResultSummary executeObjects(IProject project, String packageName, Set<Activation> activations) {		

		//Method prepared for implementation with Code Mining - for now dummy implementation for E2E testing purposes  		
		List<InvalidItem> invalidItems = simulateJavaTest(activations); 
		return new TestResultSummary(project, packageName, true, activations.size(), invalidItems, activations);
	}
	 

	private List<InvalidItem> simulateJavaTest(Set<Activation> activations) {
		List<InvalidItem> invalidItems = new ArrayList<InvalidItem>(); 
		for(Activation activation : activations) 
		{
			if (activation.getObjectName().startsWith("A")) 
			{
				invalidItems.add(new InvalidItem(activation.getObjectName(), "Testfehler", false, null, ErrorPriority.ERROR));
			}
		}
		
		return invalidItems; 
	}
}
