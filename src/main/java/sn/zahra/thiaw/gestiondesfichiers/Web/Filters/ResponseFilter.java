package sn.zahra.thiaw.gestiondesfichiers.Web.Filters;

import jakarta.servlet.FilterConfig;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;

import java.io.IOException;

@WebFilter("/*")
public class ResponseFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // Initialisation du filtre (vide si non nécessaire)
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        // Initialiser l'objet ApiResponse avec des valeurs par défaut
        ApiResponse<Object> apiResponse = new ApiResponse<>();

        try {
            // Passer la requête et la réponse au prochain filtre ou servlet
            chain.doFilter(request, response);
        } catch (Exception e) {
            // Capturer l'exception et renseigner la réponse d'API
            apiResponse.setStatus("ERROR");
            apiResponse.setStatusCode(500); // Code d'erreur HTTP
            apiResponse.setMessage("Une erreur s'est produite lors du traitement de la requête");
            apiResponse.setData(null); // Pas de données spécifiques

            // Vous pouvez ici ajouter la logique pour renvoyer cette réponse en tant que JSON, par exemple.
            response.getWriter().write(apiResponse.toString());
        }
    }

    @Override
    public void destroy() {
        // Libérer les ressources si nécessaire
    }
}
