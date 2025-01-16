package sn.zahra.thiaw.gestiondesfichiers.Services.Impl;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import sn.zahra.thiaw.gestiondesfichiers.Datas.Entities.UserEntity;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.security.Key;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

//@Service
public class JwtService {

    @Value("${security.jwt.secret-key}")
    private String secretKey;

    @Value("${security.jwt.expiration-time}")
    private long jwtExpiration;


    public String getTokenFromRequest() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes == null) {
            throw new RuntimeException("Impossible d'obtenir les attributs de la requête");
        }
        HttpServletRequest request = attributes.getRequest();
        String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            throw new RuntimeException("Token non fourni ou mal formé");
        }
        return authorizationHeader.substring(7); // Supprime "Bearer " pour récupérer uniquement le token
    }

    public String extractUsernameFromToken() {
        String token = getTokenFromRequest();
        return extractUsername(token);
    }

    // Extraire le nom d'utilisateur (username) à partir du token
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // Extraire un claim spécifique à partir du token
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    // Générer un token JWT avec les rôles de l'utilisateur
    public String generateToken(UserDetails userDetails) {
        // Récupérer les rôles de l'utilisateur
        String types = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        // Construire le JWT avec les rôles comme claim
        return Jwts.builder()
                .setSubject(userDetails.getUsername())
                .claim("types", types)  // Ajouter les rôles dans les claims
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpiration))  // Définir l'expiration du token
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)  // Signer le token avec la clé secrète
                .compact();
    }

    // Générer un token avec des claims supplémentaires
    public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
        return buildToken(extraClaims, userDetails, jwtExpiration);
    }

    // Construire un token avec des claims supplémentaires et une durée d'expiration définie
    private String buildToken(Map<String, Object> extraClaims, UserDetails userDetails, long expiration) {
        // Extraire les rôles de l'utilisateur
        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());
        // Ajouter les rôles aux extraClaims
        extraClaims.put("roles", roles);

        return Jwts.builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }
    // Récupérer les informations de l'utilisateur à partir du token
    public UserEntity getUserFromToken(String token) {
        Claims claims = extractAllClaims(token);
        String nom = claims.get("nom", String.class);
        String prenom = claims.get("prenom", String.class);
        String email = claims.get("email", String.class);
        UserEntity.Role role = UserEntity.Role.valueOf(claims.get("role", String.class));

        UserEntity user = new UserEntity();
        user.setNom(nom);
        user.setPrenom(prenom);
        user.setEmail(email);
        user.setRole(role);

        return user;
    }


    // Valider le token en vérifiant si l'utilisateur correspond et si le token n'est pas expiré
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    // Vérifier si le token est expiré
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    // Extraire la date d'expiration du token
    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    // Extraire tous les claims du token
    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }


    // Générer la clé pour signer les tokens
    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
