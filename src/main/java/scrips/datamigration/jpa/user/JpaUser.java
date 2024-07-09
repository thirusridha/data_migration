package scrips.datamigration.jpa.user;



import java.util.Date;

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
import scrips.datamigration.jpa.sss.securities.JpaTaxScheme;


@Entity
@Table(name="user")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class JpaUser  {
	@Id
	@Column(name="id")
	private String id;
	@Column(name="user_id")
	private String userId;	
	@Column(name = "name")
	private String name;
	@Column(name = "email")
	private String email;
	@Column(name="phone_no")
	private String phoneNo;
	@Column(name="password")
	private String password;	
	@Column(name = "action")
	private String action;
	@Column(name = "approved_by")
	private String approvedBy;
	@Column(name="approved_date")
	private Date approvedDate;
	@Column(name="created_date")
	private Date createdDate;	
	@Column(name = "effective_date")
	private Date effectiveDate;
	@Column(name = "modified_by")
	private String modifiedBy;
	@Column(name="modified_date")
	private Date modifiedDate;
	@Column(name="status")
	private String status;	
	@Column(name = "access_level")
	private String accessLevel;
	@Column(name = "currency_code")
	private String currencyCode;
	@Column(name = "login_attempts")
	private Integer loginAttempts;
	@Column(name="password_expiry_date")
	private Date passwordExpiryDate;
	@Column(name="user_status")
	private String userStatus;	
	@Column(name = "first_activation_date")
	private Date firstActivationDate;
	@Column(name = "member_id")
	private String memberId;
	@Column(name="last_login_datetime")
	private Date lastLoginDatetime;
	@Column(name="approval_remark")
	private String approvalRemark;	
	@Column(name = "workflow_status_id")
	private Integer workflowStatusId;
	@Column(name = "swift_net_id")
	private String swiftNetId;

}
