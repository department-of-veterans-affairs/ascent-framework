package gov.va.ascent.framework.security;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * Created by vgadda on 6/6/17.
 */

public class PersonTraits extends User {

    private Date birthDate;
    private String firstName;
    private String lastName;
    private String middleName;
    private String prefix;
    private String suffix;
    private String gender;
    private String assuranceLevel;
    private String email;
    private String dodedipnid;
    private String pnidType;
    private String pnid;
    private String pid;
    private String icn;
    private String fileNumber;
    private String tokenId;
    
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

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAssuranceLevel() {
        return assuranceLevel;
    }

    public void setAssuranceLevel(String assuranceLevel) {
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
    public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		if (!super.equals(o))
			return false;

		PersonTraits that = (PersonTraits) o;

		if (!dodedipnid.equals(that.dodedipnid))
			return false;
		if (firstName != null ? !firstName.equals(that.firstName) : that.firstName != null)
			return false;
		if (lastName != null ? !lastName.equals(that.lastName) : that.lastName != null)
			return false;
		if (middleName != null ? !middleName.equals(that.middleName) : that.middleName != null)
			return false;
		if (prefix != null ? !prefix.equals(that.prefix) : that.prefix != null)
			return false;
		if (suffix != null ? !suffix.equals(that.suffix) : that.suffix != null)
			return false;
		if (birthDate != null ? !birthDate.equals(that.birthDate) : that.birthDate != null)
			return false;
		if (gender != null ? !gender.equals(that.gender) : that.gender != null)
			return false;
		if (assuranceLevel != null ? !assuranceLevel.equals(that.assuranceLevel) : that.assuranceLevel != null)
			return false;
		if (email != null ? !email.equals(that.email) : that.email != null)
			return false;
		if (pnidType != null ? !pnidType.equals(that.pnidType) : that.pnidType != null)
			return false;
		if (pnid != null ? !pnid.equals(that.pnid) : that.pnid != null)
			return false;
		if (pid != null ? !pid.equals(that.pid) : that.pid != null)
			return false;
		if (icn != null ? !icn.equals(that.icn) : that.icn != null)
			return false;
		return fileNumber != null ? fileNumber.equals(that.fileNumber) : that.fileNumber == null;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + dodedipnid.hashCode();
        result = 31 * result + (firstName != null ? firstName.hashCode() : 0);
        result = 31 * result + (lastName != null ? lastName.hashCode() : 0);
        result = 31 * result + (middleName != null ? middleName.hashCode() : 0);
        result = 31 * result + (prefix != null ? prefix.hashCode() : 0);
        result = 31 * result + (suffix != null ? suffix.hashCode() : 0);
        result = 31 * result + (birthDate != null ? birthDate.hashCode() : 0);
        result = 31 * result + (gender != null ? gender.hashCode() : 0);
        result = 31 * result + (assuranceLevel != null ? assuranceLevel.hashCode() : 0);
        result = 31 * result + (email != null ? email.hashCode() : 0);
        result = 31 * result + (pnidType != null ? pnidType.hashCode() : 0);
        result = 31 * result + (pnid != null ? pnid.hashCode() : 0);
        result = 31 * result + (pid != null ? pid.hashCode() : 0);
        result = 31 * result + (icn != null ? icn.hashCode() : 0);
        result = 31 * result + (fileNumber != null ? fileNumber.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "PersonTraits{" +
                "dodedipnid='" + dodedipnid + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", middleName='" + middleName + '\'' +
                ", prefix='" + prefix + '\'' +
                ", suffix='" + suffix + '\'' +
                ", birthDate=" + birthDate +
                ", gender='" + gender + '\'' +
                ", assuranceLevel='" + assuranceLevel + '\'' +
                ", email='" + email + '\'' +
                ", pnidType='" + pnidType + '\'' +
                ", pnid='" + pnid + '\'' +
                ", pid='" + pid + '\'' +
                ", icn='" + icn + '\'' +
                ", fileNumber='" + fileNumber + '\'' +
                '}';
    }
}
