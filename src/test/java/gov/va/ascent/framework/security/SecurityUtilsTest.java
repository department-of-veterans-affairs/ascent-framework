package gov.va.ascent.framework.security;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
@RunWith(MockitoJUnitRunner.class)
public class SecurityUtilsTest  {
	@Mock
	SecurityContextHolder mockSecurityContextHolder;
	@Mock
	SecurityContext mockSecurityContext;
	@Mock
	Authentication mockAuthentication;
	@Mock
	PersonTraits mockPersonalTraits;
	
	
	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testSecurityUtils() {
		 SecurityUtils test = new SecurityUtils();
		 assertNotNull(test);
	}

	@Test
	public void testGetUserId() {
		//when(SecurityContextHolder.getContext().getAuthentication()).thenReturn(auth);
		//when(mockAuthentication.isAuthenticated()).thenReturn(true);
		//when(mockAuthentication.getPrincipal()).thenReturn(mockPersonalTraits);
		assertNull(SecurityUtils.getUserId());
	}

	@Test
	public void testGetPersonTraits() {
		assertNull(SecurityUtils.getPersonTraits());
	}

	@Test
	public void testGetAuthorities() {
		assertEquals(0,SecurityUtils.getAuthorities().size());
	}

	@Test
	public void testIsUserInRole() {
		assertFalse(SecurityUtils.isUserInRole("test"));
	}

	@Test
	public void testLogout() {
		SecurityUtils.logout();
	}

}
