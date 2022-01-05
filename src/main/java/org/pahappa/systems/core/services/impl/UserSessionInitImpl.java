package org.pahappa.systems.core.services.impl;

import org.sers.webutils.server.core.security.service.UserSessionInit;
import org.sers.webutils.server.core.security.service.impl.CustomSessionProvider;
import org.sers.webutils.server.core.utils.ApplicationContextProvider;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * {@link UserSessionInit} is an Interface, if implemented, the method
 * onSessionCreated() is executed by the authentication engine
 * 
 * @author mmc
 *
 */
@Service
@Transactional
@Scope("session")
public class UserSessionInitImpl implements UserSessionInit {

	@Override
	public void onSessionCreated() {
		ApplicationContextProvider.getBean(CustomSessionProvider.class).getLoggedInUser();
	}

}
