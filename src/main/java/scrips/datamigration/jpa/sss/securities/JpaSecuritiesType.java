package scrips.datamigration.jpa.sss.securities;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Setter
@Data
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "securities_type")
@Builder
public class JpaSecuritiesType {
	
	@Id
	private String id;
	@Column(name = "facility_eligibility_id")
	private String facilityEligibilityId;
	@Column(name = "haircut_id")
	private String  haircutId;
	@Column(name = "benchmark_indicator")
	private String benchmarkIndicator;
}
