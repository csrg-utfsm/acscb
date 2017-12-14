package alma.acs.tmcdb;
// Generated Jun 5, 2017 7:15:51 PM by Hibernate Tools 4.3.1.Final


import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * BL_AntennaToPad generated by hbm2java
 */
@SuppressWarnings("serial")
@Entity
@Table(name="`BL_ANTENNATOPAD`"
)
public class BL_AntennaToPad extends alma.acs.tmcdb.translator.TmcdbObject implements java.io.Serializable {


     protected BL_AntennaToPadId id;
     protected String who;
     protected String changeDesc;
     protected Double mountMetrologyAN0Coeff;
     protected Double mountMetrologyAW0Coeff;

    public BL_AntennaToPad() {
    }
   
       @EmbeddedId

    
    @AttributeOverrides( {
        @AttributeOverride(name="`version`", column=@Column(name="VERSION`", nullable=false) ), 
        @AttributeOverride(name="modTime`", column=@Column(name="MODTIME`", nullable=false) ), 
        @AttributeOverride(name="operation`", column=@Column(name="OPERATION`", nullable=false, length=1) ), 
        @AttributeOverride(name="antennaToPadId`", column=@Column(name="ANTENNATOPADID`", nullable=false) ) } )
    public BL_AntennaToPadId getId() {
        return this.id;
    }
    
    public void setId(BL_AntennaToPadId id) {    
        if( propertyChangeSupport != null )
            propertyChangeSupport.firePropertyChange("id", this.id, this.id = id);
        else
            this.id = id;
    }


    
    @Column(name="`WHO`", length=128)
    public String getWho() {
        return this.who;
    }
    
    public void setWho(String who) {    
        if( propertyChangeSupport != null )
            propertyChangeSupport.firePropertyChange("who", this.who, this.who = who);
        else
            this.who = who;
    }


    
    @Column(name="`CHANGEDESC`", length=16777216)
    public String getChangeDesc() {
        return this.changeDesc;
    }
    
    public void setChangeDesc(String changeDesc) {    
        if( propertyChangeSupport != null )
            propertyChangeSupport.firePropertyChange("changeDesc", this.changeDesc, this.changeDesc = changeDesc);
        else
            this.changeDesc = changeDesc;
    }


    
    @Column(name="`MOUNTMETROLOGYAN0COEFF`", precision=64, scale=0)
    public Double getMountMetrologyAN0Coeff() {
        return this.mountMetrologyAN0Coeff;
    }
    
    public void setMountMetrologyAN0Coeff(Double mountMetrologyAN0Coeff) {    
        if( propertyChangeSupport != null )
            propertyChangeSupport.firePropertyChange("mountMetrologyAN0Coeff", this.mountMetrologyAN0Coeff, this.mountMetrologyAN0Coeff = mountMetrologyAN0Coeff);
        else
            this.mountMetrologyAN0Coeff = mountMetrologyAN0Coeff;
    }


    
    @Column(name="`MOUNTMETROLOGYAW0COEFF`", precision=64, scale=0)
    public Double getMountMetrologyAW0Coeff() {
        return this.mountMetrologyAW0Coeff;
    }
    
    public void setMountMetrologyAW0Coeff(Double mountMetrologyAW0Coeff) {    
        if( propertyChangeSupport != null )
            propertyChangeSupport.firePropertyChange("mountMetrologyAW0Coeff", this.mountMetrologyAW0Coeff, this.mountMetrologyAW0Coeff = mountMetrologyAW0Coeff);
        else
            this.mountMetrologyAW0Coeff = mountMetrologyAW0Coeff;
    }





}

