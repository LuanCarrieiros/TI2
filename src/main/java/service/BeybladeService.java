package service;

import java.util.Scanner;
import java.time.LocalDate;
import java.io.File;
import java.time.LocalDateTime;
import java.util.List;
import dao.BeybladeDAO;
import model.Beyblade;
import spark.Request;
import spark.Response;


public class BeybladeService {

	private BeybladeDAO beybladeDAO = new BeybladeDAO();
	private String form;
	private final int FORM_INSERT = 1;
	private final int FORM_DETAIL = 2;
	private final int FORM_UPDATE = 3;
	private final int FORM_ORDERBY_ID = 1;
	private final int FORM_ORDERBY_DESCRICAO = 2;
	private final int FORM_ORDERBY_PRECO = 3;
	
	
	public BeybladeService() {
		makeForm();
	}

	
	public void makeForm() {
		makeForm(FORM_INSERT, new Beyblade(), FORM_ORDERBY_DESCRICAO);
	}

	
	public void makeForm(int orderBy) {
		makeForm(FORM_INSERT, new Beyblade(), orderBy);
	}

	
	public void makeForm(int tipo, Beyblade beyblade, int orderBy) {
		String nomeArquivo = "form.html";
		form = "";
		try{
			Scanner entrada = new Scanner(new File(nomeArquivo));
		    while(entrada.hasNext()){
		    	form += (entrada.nextLine() + "\n");
		    }
		    entrada.close();
		}  catch (Exception e) { System.out.println(e.getMessage()); }
		
		String umBeyblade = "";
		if(tipo != FORM_INSERT) {
			umBeyblade += "\t<table width=\"80%\" bgcolor=\"#f3f3f3\" align=\"center\">";
			umBeyblade += "\t\t<tr>";
			umBeyblade += "\t\t\t<td align=\"left\"><font size=\"+2\"><b>&nbsp;&nbsp;&nbsp;<a href=\"/beyblade/list/1\">Novo Beyblade</a></b></font></td>";
			umBeyblade += "\t\t</tr>";
			umBeyblade += "\t</table>";
			umBeyblade += "\t<br>";			
		}
		
		if(tipo == FORM_INSERT || tipo == FORM_UPDATE) {
			String action = "/beyblade/";
			String name, descricao, buttonLabel;
			if (tipo == FORM_INSERT){
				action += "insert";
				name = "Inserir Beyblade";
				descricao = "leite, pão, ...";
				buttonLabel = "Inserir";
			} else {
				action += "update/" + beyblade.getID();
				name = "Atualizar Beyblade (ID " + beyblade.getID() + ")";
				descricao = beyblade.getDescricao();
				buttonLabel = "Atualizar";
			}
			umBeyblade += "\t<form class=\"form--register\" action=\"" + action + "\" method=\"post\" id=\"form-add\">";
			umBeyblade += "\t<table width=\"80%\" bgcolor=\"#f3f3f3\" align=\"center\">";
			umBeyblade += "\t\t<tr>";
			umBeyblade += "\t\t\t<td colspan=\"3\" align=\"left\"><font size=\"+2\"><b>&nbsp;&nbsp;&nbsp;" + name + "</b></font></td>";
			umBeyblade += "\t\t</tr>";
			umBeyblade += "\t\t<tr>";
			umBeyblade += "\t\t\t<td colspan=\"3\" align=\"left\">&nbsp;</td>";
			umBeyblade += "\t\t</tr>";
			umBeyblade += "\t\t<tr>";
			umBeyblade += "\t\t\t<td>&nbsp;Descrição: <input class=\"input--register\" type=\"text\" name=\"descricao\" value=\""+ descricao +"\"></td>";
			umBeyblade += "\t\t\t<td>Preco: <input class=\"input--register\" type=\"text\" name=\"preco\" value=\""+ beyblade.getPreco() +"\"></td>";
			umBeyblade += "\t\t\t<td>Quantidade: <input class=\"input--register\" type=\"text\" name=\"quantidade\" value=\""+ beyblade.getQuantidade() +"\"></td>";
			umBeyblade += "\t\t</tr>";
			umBeyblade += "\t\t<tr>";
			umBeyblade += "\t\t\t<td>&nbsp;Data de fabricação: <input class=\"input--register\" type=\"text\" name=\"dataFabricacao\" value=\""+ beyblade.getDataFabricacao().toString() + "\"></td>";
			umBeyblade += "\t\t\t<td>Data de validade: <input class=\"input--register\" type=\"text\" name=\"dataValidade\" value=\""+ beyblade.getDataValidade().toString() + "\"></td>";
			umBeyblade += "\t\t\t<td align=\"center\"><input type=\"submit\" value=\""+ buttonLabel +"\" class=\"input--main__style input--button\"></td>";
			umBeyblade += "\t\t</tr>";
			umBeyblade += "\t</table>";
			umBeyblade += "\t</form>";		
		} else if (tipo == FORM_DETAIL){
			umBeyblade += "\t<table width=\"80%\" bgcolor=\"#f3f3f3\" align=\"center\">";
			umBeyblade += "\t\t<tr>";
			umBeyblade += "\t\t\t<td colspan=\"3\" align=\"left\"><font size=\"+2\"><b>&nbsp;&nbsp;&nbsp;Detalhar Beyblade (ID " + beyblade.getID() + ")</b></font></td>";
			umBeyblade += "\t\t</tr>";
			umBeyblade += "\t\t<tr>";
			umBeyblade += "\t\t\t<td colspan=\"3\" align=\"left\">&nbsp;</td>";
			umBeyblade += "\t\t</tr>";
			umBeyblade += "\t\t<tr>";
			umBeyblade += "\t\t\t<td>&nbsp;Descrição: "+ beyblade.getDescricao() +"</td>";
			umBeyblade += "\t\t\t<td>Preco: "+ beyblade.getPreco() +"</td>";
			umBeyblade += "\t\t\t<td>Quantidade: "+ beyblade.getQuantidade() +"</td>";
			umBeyblade += "\t\t</tr>";
			umBeyblade += "\t\t<tr>";
			umBeyblade += "\t\t\t<td>&nbsp;Data de fabricação: "+ beyblade.getDataFabricacao().toString() + "</td>";
			umBeyblade += "\t\t\t<td>Data de validade: "+ beyblade.getDataValidade().toString() + "</td>";
			umBeyblade += "\t\t\t<td>&nbsp;</td>";
			umBeyblade += "\t\t</tr>";
			umBeyblade += "\t</table>";		
		} else {
			System.out.println("ERRO! Tipo não identificado " + tipo);
		}
		form = form.replaceFirst("<UM-BEYBLADE>", umBeyblade);
		
		String list = new String("<table width=\"80%\" align=\"center\" bgcolor=\"#f3f3f3\">");
		list += "\n<tr><td colspan=\"6\" align=\"left\"><font size=\"+2\"><b>&nbsp;&nbsp;&nbsp;Relação de Beyblades</b></font></td></tr>\n" +
				"\n<tr><td colspan=\"6\">&nbsp;</td></tr>\n" +
    			"\n<tr>\n" + 
        		"\t<td><a href=\"/beyblade/list/" + FORM_ORDERBY_ID + "\"><b>ID</b></a></td>\n" +
        		"\t<td><a href=\"/beyblade/list/" + FORM_ORDERBY_DESCRICAO + "\"><b>Descrição</b></a></td>\n" +
        		"\t<td><a href=\"/beyblade/list/" + FORM_ORDERBY_PRECO + "\"><b>Preço</b></a></td>\n" +
        		"\t<td width=\"100\" align=\"center\"><b>Detalhar</b></td>\n" +
        		"\t<td width=\"100\" align=\"center\"><b>Atualizar</b></td>\n" +
        		"\t<td width=\"100\" align=\"center\"><b>Excluir</b></td>\n" +
        		"</tr>\n";
		
		List<Beyblade> beyblades;
		if (orderBy == FORM_ORDERBY_ID) {                 	beyblades = beybladeDAO.getOrderByID();
		} else if (orderBy == FORM_ORDERBY_DESCRICAO) {		beyblades = beybladeDAO.getOrderByDescricao();
		} else if (orderBy == FORM_ORDERBY_PRECO) {			beyblades = beybladeDAO.getOrderByPreco();
		} else {											beyblades = beybladeDAO.get();
		}

		int i = 0;
		String bgcolor = "";
		for (Beyblade p : beyblades) {
			bgcolor = (i++ % 2 == 0) ? "#fff5dd" : "#dddddd";
			list += "\n<tr bgcolor=\""+ bgcolor +"\">\n" + 
            		  "\t<td>" + p.getID() + "</td>\n" +
            		  "\t<td>" + p.getDescricao() + "</td>\n" +
            		  "\t<td>" + p.getPreco() + "</td>\n" +
            		  "\t<td align=\"center\" valign=\"middle\"><a href=\"/beyblade/" + p.getID() + "\"><img src=\"/image/detail.png\" width=\"20\" height=\"20\"/></a></td>\n" +
            		  "\t<td align=\"center\" valign=\"middle\"><a href=\"/beyblade/update/" + p.getID() + "\"><img src=\"/image/update.png\" width=\"20\" height=\"20\"/></a></td>\n" +
            		  "\t<td align=\"center\" valign=\"middle\"><a href=\"javascript:confirmarDeleteBeyblade('" + p.getID() + "', '" + p.getDescricao() + "', '" + p.getPreco() + "');\"><img src=\"/image/delete.png\" width=\"20\" height=\"20\"/></a></td>\n" +
            		  "</tr>\n";
		}
		list += "</table>";		
		form = form.replaceFirst("<LISTAR-BEYBLADE>", list);				
	}
	
	
	public Object insert(Request request, Response response) {
		String descricao = request.queryParams("descricao");
		float preco = Float.parseFloat(request.queryParams("preco"));
		int quantidade = Integer.parseInt(request.queryParams("quantidade"));
		LocalDateTime dataFabricacao = LocalDateTime.parse(request.queryParams("dataFabricacao"));
		LocalDate dataValidade = LocalDate.parse(request.queryParams("dataValidade"));
		
		String resp = "";
		
		Beyblade beyblade = new Beyblade(-1, descricao, preco, quantidade, dataFabricacao, dataValidade);
		
		if(beybladeDAO.insert(beyblade) == true) {
            resp = "Beyblade (" + descricao + ") inserido!";
            response.status(201); // 201 Created
		} else {
			resp = "Beyblade (" + descricao + ") não inserido!";
			response.status(404); // 404 Not found
		}
			
		makeForm();
		return form.replaceFirst("<input type=\"hidden\" id=\"msg\" name=\"msg\" value=\"\">", "<input type=\"hidden\" id=\"msg\" name=\"msg\" value=\""+ resp +"\">");
	}

	
	public Object get(Request request, Response response) {
		int id = Integer.parseInt(request.params(":id"));		
		Beyblade beyblade = (Beyblade) beybladeDAO.get(id);
		
		if (beyblade != null) {
			response.status(200); // success
			makeForm(FORM_DETAIL, beyblade, FORM_ORDERBY_DESCRICAO);
        } else {
            response.status(404); // 404 Not found
            String resp = "Beyblade " + id + " não encontrado.";
    		makeForm();
    		form.replaceFirst("<input type=\"hidden\" id=\"msg\" name=\"msg\" value=\"\">", "<input type=\"hidden\" id=\"msg\" name=\"msg\" value=\""+ resp +"\">");     
        }

		return form;
	}

	
	public Object getToUpdate(Request request, Response response) {
		int id = Integer.parseInt(request.params(":id"));		
		Beyblade beyblade = (Beyblade) beybladeDAO.get(id);
		
		if (beyblade != null) {
			response.status(200); // success
			makeForm(FORM_UPDATE, beyblade, FORM_ORDERBY_DESCRICAO);
        } else {
            response.status(404); // 404 Not found
            String resp = "Beyblade " + id + " não encontrado.";
    		makeForm();
    		form.replaceFirst("<input type=\"hidden\" id=\"msg\" name=\"msg\" value=\"\">", "<input type=\"hidden\" id=\"msg\" name=\"msg\" value=\""+ resp +"\">");     
        }

		return form;
	}
	
	
	public Object getAll(Request request, Response response) {
		int orderBy = Integer.parseInt(request.params(":orderby"));
		makeForm(orderBy);
	    response.header("Content-Type", "text/html");
	    response.header("Content-Encoding", "UTF-8");
		return form;
	}			
	
	public Object update(Request request, Response response) {
        int id = Integer.parseInt(request.params(":id"));
		Beyblade beyblade = beybladeDAO.get(id);
        String resp = "";       

        if (beyblade != null) {
        	beyblade.setDescricao(request.queryParams("descricao"));
        	beyblade.setPreco(Float.parseFloat(request.queryParams("preco")));
        	beyblade.setQuantidade(Integer.parseInt(request.queryParams("quantidade")));
        	beyblade.setDataFabricacao(LocalDateTime.parse(request.queryParams("dataFabricacao")));
        	beyblade.setDataValidade(LocalDate.parse(request.queryParams("dataValidade")));
        	beybladeDAO.update(beyblade);
        	response.status(200); // success
            resp = "Beyblade (ID " + beyblade.getID() + ") atualizado!";
        } else {
            response.status(404); // 404 Not found
            resp = "Beyblade (ID \" + beyblade.getId() + \") não encontrado!";
        }
		makeForm();
		return form.replaceFirst("<input type=\"hidden\" id=\"msg\" name=\"msg\" value=\"\">", "<input type=\"hidden\" id=\"msg\" name=\"msg\" value=\""+ resp +"\">");
	}

	
	public Object delete(Request request, Response response) {
        int id = Integer.parseInt(request.params(":id"));
        Beyblade beyblade = beybladeDAO.get(id);
        String resp = "";       

        if (beyblade != null) {
            beybladeDAO.delete(id);
            response.status(200); // success
            resp = "Beyblade (" + id + ") excluído!";
        } else {
            response.status(404); // 404 Not found
            resp = "Beyblade (" + id + ") não encontrado!";
        }
		makeForm();
		return form.replaceFirst("<input type=\"hidden\" id=\"msg\" name=\"msg\" value=\"\">", "<input type=\"hidden\" id=\"msg\" name=\"msg\" value=\""+ resp +"\">");
	}
}