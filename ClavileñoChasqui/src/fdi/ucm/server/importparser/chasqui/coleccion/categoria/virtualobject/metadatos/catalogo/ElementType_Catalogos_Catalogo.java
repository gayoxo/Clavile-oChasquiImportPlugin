package fdi.ucm.server.importparser.chasqui.coleccion.categoria.virtualobject.metadatos.catalogo;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import fdi.ucm.server.importparser.chasqui.LoadCollectionChasqui;
import fdi.ucm.server.importparser.chasqui.NameConstantsChasqui;
import fdi.ucm.server.importparser.chasqui.StaticFunctionsChasqui;
import fdi.ucm.server.importparser.chasqui.InterfaceChasquiparser;
import fdi.ucm.server.modelComplete.collection.document.CompleteDocuments;
import fdi.ucm.server.modelComplete.collection.document.CompleteTextElement;
import fdi.ucm.server.modelComplete.collection.grammar.CompleteElementType;
import fdi.ucm.server.modelComplete.collection.grammar.CompleteOperationalValueType;
import fdi.ucm.server.modelComplete.collection.grammar.CompleteTextElementType;

/**
 * Clase que parsea cada una de la categorias
 * @author Joaquin Gayoso-Cabada
 *
 */
public class ElementType_Catalogos_Catalogo implements InterfaceChasquiparser{

	private CompleteTextElementType CatalogosCatalogo;
	private static ArrayList<CompleteTextElementType> CatalogsSons;
	private ArrayList<String> Vocabulario;
	private LoadCollectionChasqui LCole;

	/**
	 * Constructor por parametros del Meta	
	 * @param name Nombre del nuevo Meta
	 * @param browseable Navegabilidad
	 * @param father Padre del Meta
	 */
	public ElementType_Catalogos_Catalogo(
			String name, boolean browseable, CompleteElementType father,LoadCollectionChasqui lcole) {
		LCole=lcole;
		CatalogosCatalogo= new CompleteTextElementType(name, father);
		
		String VistaOV=new String(NameConstantsChasqui.PRESNTACION);
		
		CompleteOperationalValueType Valor=new CompleteOperationalValueType(NameConstantsChasqui.VISIBLESHOWN,Boolean.toString(true),VistaOV);
		CompleteOperationalValueType Valor2=new CompleteOperationalValueType(NameConstantsChasqui.BROWSERSHOWN,Boolean.toString(browseable),VistaOV);
		CompleteOperationalValueType Valor3=new CompleteOperationalValueType(NameConstantsChasqui.SUMMARYSHOWN,Boolean.toString(false),VistaOV);
		
		
		CatalogosCatalogo.getShows().add(Valor);
		CatalogosCatalogo.getShows().add(Valor2);
		CatalogosCatalogo.getShows().add(Valor3);
		
		String VistaMetaType=new String(NameConstantsChasqui.METATYPE);
		CompleteOperationalValueType MetaType=new CompleteOperationalValueType(NameConstantsChasqui.METATYPETYPE,NameConstantsChasqui.CONTROLED,VistaMetaType);
		CatalogosCatalogo.getShows().add(MetaType);
		
		Vocabulario=new ArrayList<String>();
		
		LCole.getCollection().getVocabularios().put(CatalogosCatalogo, Vocabulario);
	}

	
	
	/**
	 * Constructor por copia del Meta
	 * @param catalogosCatalogo elemento que sobrescribe el meta
	 */
	public ElementType_Catalogos_Catalogo(
			CompleteTextElementType catalogosCatalogo,LoadCollectionChasqui lcole) {
		CatalogosCatalogo = catalogosCatalogo;
		LCole=lcole;
	}



	@Override
	public void ProcessAttributes() {
		process_Vocabulary();
		if (Vocabulario.isEmpty())
			CatalogosCatalogo.getFather().getSons().remove(this);
	}

	/**
	 * Funcion que procesa el vocabularios
	 */
	protected void process_Vocabulary() {

		try {
			ResultSet rs=LCole.getSQL().RunQuerrySELECT("SELECT distinct idov,num_ruta FROM metadatos WHERE ruta='/manifest/metadata/lom/general/catalogentry/catalog' AND contenido='"+CatalogosCatalogo.getName()+"' ORDER BY contenido;");
			if (rs!=null) 
			{
				while (rs.next()) {
					String Dato=rs.getObject("idov").toString();
					String DatoNumRuta=rs.getObject("num_ruta").toString();
					if (Dato!=null&&DatoNumRuta!=null&&!Dato.isEmpty()&&!DatoNumRuta.isEmpty())
						{
						processCatalogForIdov(Dato,DatoNumRuta);
						}
					
				}
			rs.close();
			
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}

	/**
	 * Funcion que procesa los catalogos para cada idov
	 * @param idov idov a procesar
	 * @param datoNumRuta numero de ruta
	 */
	private void processCatalogForIdov(String idov, String datoNumRuta) {

		try {
			ResultSet rs=LCole.getSQL().RunQuerrySELECT("SELECT distinct contenido,num_ruta FROM metadatos WHERE ruta='/manifest/metadata/lom/general/catalogentry/entry/langstring' AND idov='"+idov+"' ORDER BY num_ruta;");
			if (rs!=null) 
			{
				while (rs.next()) {
					String Dato=rs.getObject("num_ruta").toString();
					Object temp=rs.getObject("contenido");
					String Contenido="";
					if (temp!=null)
						Contenido=temp.toString();
					String[] DatosRutaCatalogo=datoNumRuta.split("\\.");
					String[] DatosRutaEntry=Dato.split("\\.");
					if (Dato!=null&&Contenido!=null&&!Contenido.isEmpty()&&!Dato.isEmpty()&&DatosRutaCatalogo[4].equals(DatosRutaEntry[4]))
						{
						StaticFunctionsChasqui.addTerm(Vocabulario,Contenido);
						}
					
				}
			rs.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	@Override
	public void ProcessInstances() {
		try {
			ResultSet rs=LCole.getSQL().RunQuerrySELECT("SELECT distinct idov,num_ruta FROM metadatos WHERE ruta='/manifest/metadata/lom/general/catalogentry/catalog' AND contenido='"+CatalogosCatalogo.getName()+"' ORDER BY idov;");
			if (rs!=null) 
			{
				Integer intOld=null;
				int count=0;
				while (rs.next()) {
					String Dato=rs.getObject("idov").toString();
					String DatoNumRuta=rs.getObject("num_ruta").toString();
					if (Dato!=null&&DatoNumRuta!=null&&!Dato.isEmpty()&&!DatoNumRuta.isEmpty())
						{
						Integer idovInt=Integer.parseInt(Dato);
						if (intOld!=null&&intOld==idovInt)
							count++;	
						else
						{
							count=0;
							intOld=idovInt;
						}
						processCatalogForIdovInstance(Dato,DatoNumRuta,CatalogsSons.get(count));
						}
					
				}
			rs.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	
/**
 * Procesa las instancias y genera los values para los elementos.
 * @param idov identificador con el que buscamos
 * @param datoNumRuta ruta que estamos controlando
 * @param padre padre al que asignarlo
 */
private void processCatalogForIdovInstance(String idov, String datoNumRuta, CompleteTextElementType padre) {
	try {
		ResultSet rs=LCole.getSQL().RunQuerrySELECT("SELECT distinct contenido,num_ruta FROM metadatos WHERE ruta='/manifest/metadata/lom/general/catalogentry/entry/langstring' AND idov='"+idov+"' ORDER BY num_ruta;");
		if (rs!=null) 
		{
			while (rs.next()) {
				String Dato=rs.getObject("num_ruta").toString();
				Object temp=rs.getObject("contenido");
				String Contenido="";
				if (temp!=null)
					Contenido=temp.toString();
				String[] DatosRutaCatalogo=datoNumRuta.split("\\.");
				String[] DatosRutaEntry=Dato.split("\\.");
				if (Dato!=null&&Contenido!=null&&!Contenido.isEmpty()&&!Dato.isEmpty()&&DatosRutaCatalogo[4].equals(DatosRutaEntry[4]))
					{
					
					CompleteTextElement MCV=new CompleteTextElement(padre,Contenido);
					Integer idovInt=Integer.parseInt(idov);
					CompleteDocuments OV=LCole.getCollection().getObjetoVirtual().get(idovInt);
					OV.getDescription().add(MCV);
					
					}
				
			}
		rs.close();
		}
	} catch (SQLException e) {
		e.printStackTrace();
	}
		
	}



/**
 * 
 * @return the ExtendMetaControlled
 */
public CompleteTextElementType getExtendMetaControlled() {
	return CatalogosCatalogo;
}



/**
 * @return the catalogsSons
 */
public static ArrayList<CompleteTextElementType> getCatalogsSons() {
	return CatalogsSons;
}



/**
 * @param catalogsSons the catalogsSons to set
 */
public static void setCatalogsSons(ArrayList<CompleteTextElementType> catalogsSons) {
	CatalogsSons = catalogsSons;
}



/**
 * @return the vocabulario
 */
public ArrayList<String> getVocabulario() {
	return Vocabulario;
}



/**
 * @param vocabulario the vocabulario to set
 */
public void setVocabulario(ArrayList<String> vocabulario) {
	Vocabulario = vocabulario;
}











}
