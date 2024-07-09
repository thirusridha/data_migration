package scrips.datamigration.jpa.sss.coupon;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Column;

public class JpaSssFloatingRatesId implements Serializable {
	
//	@Column(name="reference_rate")
	private String referenceRate;
//	@Column(name="value_date")
	private Integer valueDate;
	@Override
	public int hashCode() {
		return Objects.hash(referenceRate, valueDate);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		JpaSssFloatingRatesId other = (JpaSssFloatingRatesId) obj;
		return Objects.equals(referenceRate, other.referenceRate) && Objects.equals(valueDate, other.valueDate);
	}
	
}
