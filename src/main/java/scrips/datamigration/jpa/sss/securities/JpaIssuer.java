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
@Table(name = "issuer")
@Builder
public class JpaIssuer {

	@Id
	private String id;
	@Column(name = "issuer_code")
	private String issuerCode;
	@Column(name = "issuer_name")
	private String issuerName;
}