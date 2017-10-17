package gov.va.ascent.framework.security;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

@RunWith(SpringRunner.class)
@ContextConfiguration
public class SecurityUtilsTest  {

	@Before
	public void setup() {
		PersonTraits personTraits = new PersonTraits("user", "password",
				AuthorityUtils.createAuthorityList("ROLE_TEST"));
		Authentication auth = new UsernamePasswordAuthenticationToken(personTraits,
				personTraits.getPassword(), personTraits.getAuthorities());
		SecurityContextHolder.getContext().setAuthentication(auth);
	}

	@After
	public void tearDown(){
		SecurityContextHolder.clearContext();
	}

	@Test
	public void testSecurityUtils() {
		 SecurityUtils test = new SecurityUtils();
		 assertNotNull(test);
	}

	@Test
	public void testGetUserId() {
		SecurityContextHolder.clearContext();
		assertNull(SecurityUtils.getUserId());
	}

	@Test
	@WithMockUser
	public void testGetUserIdNotNull() {
		assertEquals("user",SecurityUtils.getUserId());
	}

	@Test
	public void testGetPersonTraits() {

		assertTrue(SecurityUtils.getPersonTraits() instanceof PersonTraits);
	}

	@Test
	public void testGetPersonTraitsNotPersonTraits() {
		SecurityContextHolder.clearContext();
		assertFalse(SecurityUtils.getPersonTraits() instanceof PersonTraits);
	}

	@Test
	public void testGetPersonTraitsAuthenticationFalse() {
		SecurityContextHolder.getContext().getAuthentication().setAuthenticated(false);
		assertNull(SecurityUtils.getPersonTraits());
	}

	@Test
	public void testGetAuthorities() {
		assertEquals(1,SecurityUtils.getAuthorities().size());
		SimpleGrantedAuthority result = (SimpleGrantedAuthority) SecurityUtils.getAuthorities().stream()
				.filter(role -> "ROLE_TEST".equals(role.getAuthority()))
				.findFirst()
				.orElse(null);
		assertEquals("ROLE_TEST", result.getAuthority());
	}

	@Test
	public void testGetAuthoritiesRoleTestNull() {
		SecurityContextHolder.clearContext();
		assertEquals(0,SecurityUtils.getAuthorities().size());
	}

	@Test
	public void testIsUserInRole() {
		assertFalse(SecurityUtils.isUserInRole("test"));
	}

	@Test
	public void testIsUserInRoleTrue() {
		assertTrue(SecurityUtils.isUserInRole("ROLE_TEST"));
	}

	@Test
	public void testLogout() {
		SecurityUtils.logout();
		assertTrue(SecurityContextHolder.getContext().getAuthentication() == null);
	}

}
