package scrips.datamigration.jpa.coupon.data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
//@NoArgsConstructor
@Entity
@Table(name = "stepup_coupon")
public class JpaStepupCoupon 
{	
	@Id
	private String id;
    @Column(name = "security_code")
	private String securityCode;
    @Column(name = "coupon_payment_date")
	private String couponPaymentDate;
    @Column(name = "coupon_rate")
	private String couponRate;
//	public JpaStepupCoupon()
//	{
//		
//	}
}
