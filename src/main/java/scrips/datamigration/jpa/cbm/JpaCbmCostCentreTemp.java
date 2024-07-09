package scrips.datamigration.jpa.cbm;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "cbm_cost_centre_temp")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class JpaCbmCostCentreTemp {
		@Id
		private String id;
		@Column(name = "cost_centre")
		private String costCentre;
		@Column(name = "description")
		private String description;
		@Column(name = "created_date")
		private Date createdDate;
		@Column(name = "modified_date")
		private Date modifiedDate;
		@Column(name = "remarks")
		private String remarks;
}