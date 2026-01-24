package org.delicias;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.jwt.JsonWebToken;

@ApplicationScoped
public class SecurityContextService {

    @Inject
    JsonWebToken jwt;

    public String userId() {
        return jwt.getSubject();      // = "sub"
    }

    public String email() {
        return jwt.getClaim("email");
    }

}
