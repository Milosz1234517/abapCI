package abapci.feature;

public class SourceCodeVisualisationFeature {

	private boolean changeStatusBarBackgroundColorEnabled;
	private boolean showStatusBarWidgetEnabled;


	public boolean isChangeStatusBarBackgroundColorEnabled() {
		return changeStatusBarBackgroundColorEnabled;
	}

	public void setChangeStatusBarBackgroundColorEnabled(boolean enabled) {
		changeStatusBarBackgroundColorEnabled = enabled;
	}

	public boolean isShowStatusBarWidgetEnabled() {
		return showStatusBarWidgetEnabled; 
	}

	public void setShowStatusBarWidgetEnabled(boolean enabled) {
		showStatusBarWidgetEnabled = enabled; 
	}

}
