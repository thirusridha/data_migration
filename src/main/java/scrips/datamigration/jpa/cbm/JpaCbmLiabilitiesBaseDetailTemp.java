package scrips.datamigration.jpa.cbm;

import java.util.Date;

import javax.annotation.Generated;
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
@Table(name="cbm_liabilities_base_detail_temp")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class JpaCbmLiabilitiesBaseDetailTemp {
	@Id
	@Column(name = "id")
	private String id;
	@Column(name = "member_id")
	private String memberId;
	@Column(name = "start_date")
	private Date startDate;
	@Column(name = "end_date")
	private Date endDate;
	@Column(name = "ql_lb")
	private Double qlLb;
	@Column(name="remarks")
	private String remarks;
	@Column(name = "live_table_id")
	private Long liveTableId;
}

