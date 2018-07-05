package com.ram.model;

import java.io.Serializable;
import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class ManagerPositionRl implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** identifier field */
    private Integer managerPositionId;

    /** persistent field */
    private com.ram.model.Position position;

    /** persistent field */
    private com.ram.model.Manager manager;

    /** full constructor */
    public ManagerPositionRl(com.ram.model.Position position, com.ram.model.Manager manager) {
        this.position = position;
        this.manager = manager;
    }

    /** default constructor */
    public ManagerPositionRl() {
    }

    public Integer getManagerPositionId() {
        return this.managerPositionId;
    }

    public void setManagerPositionId(Integer managerPositionId) {
        this.managerPositionId = managerPositionId;
    }

    public com.ram.model.Position getPosition() {
        return this.position;
    }

    public void setPosition(com.ram.model.Position position) {
        this.position = position;
    }

    public com.ram.model.Manager getManager() {
        return this.manager;
    }

    public void setManager(com.ram.model.Manager manager) {
        this.manager = manager;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("managerPositionId", getManagerPositionId())
            .toString();
    }

}
