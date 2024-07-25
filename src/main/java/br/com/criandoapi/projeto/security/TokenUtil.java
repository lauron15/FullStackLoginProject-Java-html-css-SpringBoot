package br.com.criandoapi.projeto.security;

import br.com.criandoapi.projeto.model.Usuario;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Collections;
import java.util.Date;

import static io.jsonwebtoken.Jwts.*;

public class TokenUtil {
    private static final String HEADER = "Authorization";
    private static final String PREFIX = "Bearer ";
    private static final long EXPIRATION = 12 * 60 * 60 * 1000;
    private static final String SECRET_KEY = "Abcvgfdrtyuioplçksrtewqçlksabcdf";
    private static final String EMISSOR = "LauroDev";

    public static String createToken(Usuario usuario) {
        Key secretKey = Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8));

        String token = builder()
                .setSubject(usuario.getNome())
                .setIssuer(EMISSOR)
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION))
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
        return PREFIX + token;
    }

    private static boolean isExpirationValid(Date expiration) {
        return expiration.after(new Date(System.currentTimeMillis()));
    }

    private static boolean isEmissorValid(String emissor) {
        return emissor.equals(EMISSOR);
    }

    private static boolean isSubjectValid(String username) {
        return username != null && username.length() > 0;
    }

    public static UsernamePasswordAuthenticationToken validate(HttpServletRequest request) {
        String token = request.getHeader(HEADER);
        token = token.replace(PREFIX, "");
        Jws<Claims> JwsClaims = Jwts.parserBuilder().setSigningKey(SECRET_KEY.getBytes())
                .build()
                .parseClaimsJws(token);

        String username = JwsClaims.getBody().getSubject();
        String issuer = JwsClaims.getBody().getIssuer();
        Date expira = JwsClaims.getBody().getExpiration();
        if (isSubjectValid(username) && isEmissorValid(issuer) && isExpirationValid(expira)) {

            return new UsernamePasswordAuthenticationToken(username, null, Collections.emptyList());
        }
        return null;
    }
}

// o parserBuilder não estava rodando por causa da versão das dependências