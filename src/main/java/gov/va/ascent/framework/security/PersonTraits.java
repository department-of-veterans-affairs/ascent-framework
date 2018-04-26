package gov.va.ascent.framework.security;

import com.fasterxml.jackson.annotation.JsonIgnore;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by vgadda on 6/6/17.
 */

public class PersonTraits extends User {
	
	enum PATTERN_FORMAT {
	    BIRTHDATE_YYYYMMDD     ("YYYY-MM-dd");
		private String pattern;

	    private PATTERN_FORMAT(String pattern) {
	        this.pattern = pattern;
	    }
	    
	    public String getPattern() {
	        return pattern;
	    }

	    public static PATTERN_FORMAT getDefault() { return BIRTHDATE_YYYYMMDD; }
	}

    private String birthDate;
    private String firstName;
    private String lastName;
    private String middleName;
    private String prefix;
    private String suffix;
    private String gender;
    private Integer assuranceLevel;
    private String email;
    private String dodedipnid;
    private String pnidType;
    private String pnid;
    private String pid;
    private String icn;
    private String fileNumber;
    private String tokenId;
    private List<String> correlationIds;

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public PersonTraits(String username, String password, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);
    }

    public PersonTraits() {
        super("NA","NA", AuthorityUtils.NO_AUTHORITIES);
    }

    protected String[] getToStringEqualsHashExcludeFields() {
        return new String[] {};
    }

    @Override
    @JsonIgnore
    public List<GrantedAuthority> getAuthorities() {
        return new ArrayList<>(super.getAuthorities());
    }

    @Override
    @JsonIgnore
    public String getPassword() {
        return super.getPassword();
    }

    @Override
    @JsonIgnore
    public String getUsername() {
        return super.getUsername();
    }

    @Override
    @JsonIgnore
    public boolean isEnabled() {
        return super.isEnabled();
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonExpired() {
        return super.isAccountNonExpired();
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonLocked() {
        return super.isAccountNonLocked();
    }

    @Override
    @JsonIgnore
    public boolean isCredentialsNonExpired() {
        return super.isCredentialsNonExpired();
    }

    public String getDodedipnid() {
        return dodedipnid;
    }

    public void setDodedipnid(String dodedipnid) {
        this.dodedipnid = dodedipnid;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getSuffix() {
        return suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Integer getAssuranceLevel() {
        return assuranceLevel;
    }

    public void setAssuranceLevel(Integer assuranceLevel) {
        this.assuranceLevel = assuranceLevel;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPnidType() {
        return pnidType;
    }

    public void setPnidType(String pnidType) {
        this.pnidType = pnidType;
    }

    public String getPnid() {
        return pnid;
    }

    public void setPnid(String pnid) {
        this.pnid = pnid;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getIcn() {
        return icn;
    }

    public void setIcn(String icn) {
        this.icn = icn;
    }

    public String getFileNumber() {
        return fileNumber;
    }

    public void setFileNumber(String fileNumber) {
        this.fileNumber = fileNumber;
    }

    public List<String> getCorrelationIds() {
        return correlationIds;
    }

    public void setCorrelationIds(List<String> correlationIds) {
        this.correlationIds = correlationIds;
    }
    
    public String getTokenId() {
        return tokenId;
    }

    public void setTokenId(String tokenId) {
        this.tokenId = tokenId;
    }
    public String getUser(){
        return  getFirstName() + " " + getLastName();
    }

    @Override
    public boolean equals(final Object obj) {
        return EqualsBuilder.reflectionEquals(this, obj, getToStringEqualsHashExcludeFields());
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this, getToStringEqualsHashExcludeFields());
    }

    @Override
    public String toString() {
        final ReflectionToStringBuilder reflectionToStringBuilder = new ReflectionToStringBuilder(this);
        reflectionToStringBuilder.setExcludeFieldNames(getToStringEqualsHashExcludeFields());
        return reflectionToStringBuilder.toString();
    }

	public boolean hasFirstName() {
		return !StringUtils.isEmpty(firstName);
	}

	public boolean hasLastName() {
		return !StringUtils.isEmpty(lastName);
	}

	public boolean hasMiddleName() {
		return !StringUtils.isEmpty(middleName);
	}

	public boolean hasPrefix() {
		return !StringUtils.isEmpty(prefix);
	}

	public boolean hasBirthDate() {
		return !StringUtils.isEmpty(birthDate);
	}

	public boolean hasGender() {
		return !StringUtils.isEmpty(gender);
	}

	public boolean hasEmail() {
		return !StringUtils.isEmpty(email);
	}

	public boolean hasDodedipnid() {
		return !StringUtils.isEmpty(dodedipnid);
	}

	public boolean hasPnidType() {
		return !StringUtils.isEmpty(pnidType);
	}

	public boolean hasPnid() {
		return !StringUtils.isEmpty(pnid);
	}

	public boolean hasPid() {
		return !StringUtils.isEmpty(pid);
	}

	public boolean hasIcn() {
		return !StringUtils.isEmpty(icn);
	}

	public boolean hasFileNumber() {
		return !StringUtils.isEmpty(fileNumber);
	}

	public boolean hasTokenId() {
		return !StringUtils.isEmpty(tokenId);
	}
}