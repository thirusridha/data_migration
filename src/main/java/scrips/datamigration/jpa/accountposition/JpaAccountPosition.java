
package scrips.datamigration.jpa.accountposition;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "account_position")
@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JpaAccountPosition {
	@Id
//	private String id;
	@Column(name = "account_id")
	private String accountId;
	@Column(name = "currency_code")
	private String currencyCode;
	@Column(name = "member_id")
	private String memberId;
	@Column(name = "current_account_balance")
	private Double currentAccountBalance;
	@Column(name = "opening_account_balance")
	private Double openingAccountBalance;
	@Column(name = "available_balance")
	private Double availableBalance;
	@Column(name = "queue_count")
	private Integer queueCount;
	@Column(name = "queue_amount")
	private Double queueAmount;
	@Column(name = "settled_payments_count")
	private Integer settledPaymentsCount;
	@Column(name = "settled_payments_amount")
	private Double settledPaymentsAmount;
	@Column(name = "settled_receipts_count")
	private Integer settledReceiptsCount;
	@Column(name = "settled_receipts_amount")
	private Double settledReceiptsAmount;
	@Column(name = "created_date")
	private Date createdDate;
	@Column(name = "modified_date")
	private Date modifiedDate;
}