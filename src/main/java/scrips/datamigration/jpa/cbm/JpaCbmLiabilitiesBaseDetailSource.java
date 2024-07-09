package scrips.datamigration.jpa.cbm;

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
@Table(name="cbm_liabilities_base_detail_source")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class JpaCbmLiabilitiesBaseDetailSource {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name ="seq" )
	private String seq;
	@Column(name = "id")
	private String id;
	@Column(name = "member_id")
	private String memberId;
	@Column(name = "start_date")
	private String startDate;
	@Column(name = "end_date")
	private String endDate;
	@Column(name = "ql_lb")
	private String qlLb;
	@Column(name = "remarks")
	private String remarks;

}
