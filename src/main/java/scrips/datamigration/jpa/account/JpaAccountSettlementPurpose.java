package scrips.datamigration.jpa.account;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "account_settlement_purpose")
public class JpaAccountSettlementPurpose  {
	@Id
	@Column(name = "account_id",nullable = false)
	private String accountId;
	@Column(name="transaction_id")
	private String transactionId;
	@Column(name="currency_code")
	private String currencyCode;
	@MapsId
	@OneToOne(cascade = CascadeType.MERGE)
	@JoinColumn(name = "account_id",referencedColumnName = "account_number")
	@JsonIgnore
	private JpaAccount account;
}
