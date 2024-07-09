package scrips.datamigration.jpa.member;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
@Entity
@Table(name = "member_liaison")
public class JpaMemberLiaison {
	@Id
	private String memberCode;
	private String liaisonOfficerName;
	private String liaisonOfficerContactNumber;
	private String liaisonOfficerEmailAddress;
	private String registeredAddress;
	
	@Override
	public String toString() {
		return "[liaisonOfficerContactNumber=" + liaisonOfficerContactNumber
				+ ", liaisonOfficerEmailAddress=" + liaisonOfficerEmailAddress + ", liaisonOfficerName="
				+ liaisonOfficerName + ", memberCode=" + memberCode + ", registeredAddress=" + registeredAddress + "]";
	}
	public String getMemberCode() {
		return memberCode;
	}
	public void setMemberCode(String memberCode) {
		this.memberCode = memberCode;
	}
	public String getLiaisonOfficerName() {
		return liaisonOfficerName;
	}
	public void setLiaisonOfficerName(String liaisonOfficerName) {
		this.liaisonOfficerName = liaisonOfficerName;
	}
	public String getLiaisonOfficerContactNumber() {
		return liaisonOfficerContactNumber;
	}
	public void setLiaisonOfficerContactNumber(String liaisonOfficerContactNumber) {
		this.liaisonOfficerContactNumber = liaisonOfficerContactNumber;
	}
	public String getLiaisonOfficerEmailAddress() {
		return liaisonOfficerEmailAddress;
	}
	public void setLiaisonOfficerEmailAddress(String liaisonOfficerEmailAddress) {
		this.liaisonOfficerEmailAddress = liaisonOfficerEmailAddress;
	}
	public String getRegisteredAddress() {
		return registeredAddress;
	}
	public void setRegisteredAddress(String registeredAddress) {
		this.registeredAddress = registeredAddress;
	}

}
