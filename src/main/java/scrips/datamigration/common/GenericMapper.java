package scrips.datamigration.common;

public abstract class GenericMapper<DomainEntityClass, JpaEntityClass, DomainEntityIdClass, JpaEntityIdClass> {
	/**
	 * This method is to get data from live JPA Entity and put data into Domain Entity.
	 * 
	 * @param jpaEntity Live JPA Entity object
	 * @return
	 */
	public abstract DomainEntityClass transformJpaToDomain(JpaEntityClass jpaEntity);
	/**
	 * This method is to get data from Domain Entity and put data into Live JPA Entity.
	 * 
	 * @param domainEntity Domain Entity object
	 * @return
	 */
	public abstract JpaEntityClass transformDomainToJpa(DomainEntityClass domainEntity);
	

	@SuppressWarnings("unchecked")
	public DomainEntityIdClass transformJpaIdToDomainId(JpaEntityIdClass jpaId) {
		return (DomainEntityIdClass)jpaId;
	}
	
	@SuppressWarnings("unchecked")
	public JpaEntityIdClass transformDomainIdToJpaId(DomainEntityIdClass domainId) {
		return (JpaEntityIdClass)domainId;
	}
}
