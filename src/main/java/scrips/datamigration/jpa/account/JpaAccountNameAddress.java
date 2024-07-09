package scrips.datamigration.jpa.account;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "account_name_address")
public class JpaAccountNameAddress {

	@Id
	@Column(name = "account_id", nullable = false)
	private String accountId;
	@Column(name="name_and_address")
	private String nameAndAddress;
	@MapsId
	@OneToOne(cascade = CascadeType.MERGE)
	@JoinColumn(name = "account_id",referencedColumnName = "account_number")
	@JsonIgnore
	private JpaAccount account;

}
