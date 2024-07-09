package scrips.datamigration.jpa.cbm;

import java.sql.Timestamp;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Entity
@Table(name = "cbm_cost_centre_source")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class JpaCbmCostCentreSource {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private String seq;
	private String id;
	@Column(name = "cost_centre")
	private String costCentre;
	@Column(name = "description")
	private String description;
	@Column(name = "created_date")
	private String createdDate;
	@Column(name = "modified_date")
	private String modifiedDate;
	private String remarks;
	
}
