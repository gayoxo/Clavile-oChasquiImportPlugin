/**
 * 
 */
package fdi.ucm.server.importparser.chasqui;

import java.util.ArrayList;

import fdi.ucm.server.importparser.chasqui.coleccion.CollectionChasqui;
import fdi.ucm.server.modelComplete.ImportExportDataEnum;
import fdi.ucm.server.modelComplete.ImportExportPair;
import fdi.ucm.server.modelComplete.LoadCollection;
import fdi.ucm.server.modelComplete.collection.CompleteCollectionAndLog;

/**
 * Clase que define una carga de una coleccion chasqui.
 * @author Joaquin Gayoso-Cabada
 *
 */
public class LoadCollectionChasqui extends LoadCollection{

	
	private boolean CloneFiles = false;
	private static ArrayList<ImportExportPair> Parametros;
	private String BaseURLChasqui;
	private MySQLConnectionChasqui MQL;
	private CollectionChasqui chasquiParser;

	public LoadCollectionChasqui() {
		super();
		
	}

	/* (non-Javadoc)
	 * @see fdi.ucm.server.LoadCollection#processCollecccion()
	 */
	@Override
	public CompleteCollectionAndLog processCollecccion(ArrayList<String> DateEntrada) {
		ArrayList<String> Log=new ArrayList<String>();
		chasquiParser= new CollectionChasqui(this);
		if (DateEntrada!=null)
		{
			String Database = RemoveSpecialCharacters(DateEntrada.get(1));
			MQL=new MySQLConnectionChasqui(DateEntrada.get(0),Database,Integer.parseInt(DateEntrada.get(2)),DateEntrada.get(3),DateEntrada.get(4));
			BaseURLChasqui=DateEntrada.get(5);
			CloneFiles=Boolean.parseBoolean(DateEntrada.get(6));
			chasquiParser.ProcessAttributes();
		}
		return new CompleteCollectionAndLog(chasquiParser.getChasquiCollection(),Log);
	}


	@Override
	public ArrayList<ImportExportPair> getConfiguracion() {
		if (Parametros==null)
		{
			ArrayList<ImportExportPair> ListaCampos=new ArrayList<ImportExportPair>();
			ListaCampos.add(new ImportExportPair(ImportExportDataEnum.Text, "Server"));
			ListaCampos.add(new ImportExportPair(ImportExportDataEnum.Text, "Database"));
			ListaCampos.add(new ImportExportPair(ImportExportDataEnum.Number, "Port"));
			ListaCampos.add(new ImportExportPair(ImportExportDataEnum.Text, "User"));
			ListaCampos.add(new ImportExportPair(ImportExportDataEnum.EncriptedText, "Password"));
			ListaCampos.add(new ImportExportPair(ImportExportDataEnum.Text, "Base url for files (if need it)",true));
			ListaCampos.add(new ImportExportPair(ImportExportDataEnum.Boolean, "Clone local files",true));
			Parametros=ListaCampos;
			return ListaCampos;
		}
		else return Parametros;
	}

	
	
	@Override
	public String getName() {
		return "Chasqui";
	}

	

	/**
	 * QUitar caracteres especiales.
	 * @param str texto de entrada.
	 * @return texto sin caracteres especiales.
	 */
	public String RemoveSpecialCharacters(String str) {
		   StringBuilder sb = new StringBuilder();
		   for (int i = 0; i < str.length(); i++) {
			   char c = str.charAt(i);
			   if ((c >= '0' && c <= '9') || (c >= 'A' && c <= 'Z') || (c >= 'a' && c <= 'z') || c == '_') {
			         sb.append(c);
			      }
		}
		   return sb.toString();
		}

	/**
	 * @return the baseURLChasqui
	 */
	public String getBaseURLChasqui() {
		return BaseURLChasqui;
	}

	/**
	 * @param baseURLChasqui the baseURLChasqui to set
	 */
	public void setBaseURLChasqui(String baseURLChasqui) {
		BaseURLChasqui = baseURLChasqui;
	}

	@Override
	public boolean getCloneLocalFiles() {
				return CloneFiles;
	}

	/**
	 * @return the mQL
	 */
	public MySQLConnectionChasqui getSQL() {
		return MQL;
	}

	/**
	 * @param mQL the mQL to set
	 */
	public void setSQL(MySQLConnectionChasqui sQL) {
		MQL = sQL;
	}

	public CollectionChasqui getCollection() {
		return chasquiParser;
	}


	
}
