package app;

import static spark.Spark.*;
import service.BeybladeService;


public class Aplicacao {
	
	private static BeybladeService beybladeService = new BeybladeService();
	
    public static void main(String[] args) {
        port(4567);
        
        staticFiles.location("/public");
        
        post("/beyblade/insert", (request, response) -> beybladeService.insert(request, response));

        get("/beyblade/:id", (request, response) -> beybladeService.get(request, response));
        
        get("/beyblade/list/:orderby", (request, response) -> beybladeService.getAll(request, response));

        get("/beyblade/update/:id", (request, response) -> beybladeService.getToUpdate(request, response));
        
        post("/beyblade/update/:id", (request, response) -> beybladeService.update(request, response));
           
        get("/beyblade/delete/:id", (request, response) -> beybladeService.delete(request, response));

    }
}
