
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
@Table(name = "account_position_source")
@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JpaAccountPositionSource {
	@Id
	@GeneratedValue(strategy =GenerationType.IDENTITY)
	private String seq;
	private String id;
	@Column(name = "account_id")
	private String accountId;
	@Column(name = "currency_code")
	private String currencyCode;
	@Column(name = "member_id")
	private String memberId;
	@Column(name = "current_account_balance")
	private String currentAccountBalance;
	@Column(name = "opening_account_balance")
	private String openingAccountBalance;
	@Column(name = "available_balance")
	private String availableBalance;
	@Column(name = "queue_count")
	private String queueCount;
	@Column(name = "queue_amount")
	private String queueAmount;
	@Column(name = "settled_payments_count")
	private String settledPaymentsCount;
	@Column(name = "settled_payments_amount")
	private String settledPaymentsAmount;
	@Column(name = "settled_receipts_count")
	private String settledReceiptsCount;	
	@Column(name = "settled_receipts_amount")
	private String settledReceiptsAmount;
	@Column(name = "created_date")
	private String createdDate;
	@Column(name = "modified_date")
	private String modifiedDate;
	private String remarks;
}