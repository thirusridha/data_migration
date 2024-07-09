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
@Table(name = "tax_scheme")
@Builder
public class JpaTaxScheme {

	@Id
	private String id;
	@Column(name = "tax_scheme_id")
	private String taxSchemeId;
}
