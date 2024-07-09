package scrips.datamigration.common;

public enum Report {
	
	CONTROL_TOTAL_REPORT("CONTROL TOTAL REPORT", "RTGSREP001"),
	SUMMARY_REPORT("SUMMARY REPORT", "RTGSREP002"),
	DETAIL_REPORT("DETAIL REPORT", "RTGSREP003");
	
	private String displayName;
	private String reportId;

	Report(String displayName, String reportId) {
        this.displayName = displayName;
        this.reportId = reportId;
    }

	public String getDisplayName() {
		return displayName;
	}

	public String getReportId() {
		return reportId;
	}
	
}
