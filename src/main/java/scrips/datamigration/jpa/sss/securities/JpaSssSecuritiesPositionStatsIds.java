package scrips.datamigration.jpa.sss.securities;

import java.io.Serializable;
import java.util.Objects;

public class JpaSssSecuritiesPositionStatsIds implements Serializable {
	@Override
	public int hashCode() {
		return Objects.hash(accountId, securitiesCode, valueDate);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		JpaSssSecuritiesPositionStatsIds other = (JpaSssSecuritiesPositionStatsIds) obj;
		return Objects.equals(accountId, other.accountId) && Objects.equals(securitiesCode, other.securitiesCode)
				&& Objects.equals(valueDate, other.valueDate);
	}
	private String securitiesCode;
	private String accountId;
	private Integer valueDate;
	
}
