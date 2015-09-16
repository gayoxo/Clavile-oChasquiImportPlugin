package fdi.ucm.server.importparser.chasqui.coleccion.categoria.virtualobject.metadatos.taxonomia;

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
 * Parseado de un node de la taxonomia
 * @author Joaquin Gayoso-Cabada
 *
 */
public class ElementType_Taxonomias_Taxonomia_Nodo implements InterfaceChasquiparser{

	
	private CompleteTextElementType atributosmetadatosCategoria;
	private ArrayList<String> Vocabulario;
	
	private String num_ruta;
	private String idov;
	private LoadCollectionChasqui LCole;

	public ElementType_Taxonomias_Taxonomia_Nodo(
			String name, boolean browseable, CompleteElementType father,String num_ruta, String idov,LoadCollectionChasqui lcole) {
		LCole=lcole;
		atributosmetadatosCategoria= new CompleteTextElementType(name, father);
		
		String VistaOV=new String(NameConstantsChasqui.PRESNTACION);
		
		CompleteOperationalValueType Valor=new CompleteOperationalValueType(NameConstantsChasqui.VISIBLESHOWN,Boolean.toString(true),VistaOV);
		CompleteOperationalValueType Valor2=new CompleteOperationalValueType(NameConstantsChasqui.BROWSERSHOWN,Boolean.toString(browseable),VistaOV);
		CompleteOperationalValueType Valor3=new CompleteOperationalValueType(NameConstantsChasqui.SUMMARYSHOWN,Boolean.toString(false),VistaOV);
		
	
		
		atributosmetadatosCategoria.getShows().add(Valor);
		atributosmetadatosCategoria.getShows().add(Valor2);
		atributosmetadatosCategoria.getShows().add(Valor3);
		this.num_ruta=num_ruta;
		this.idov=idov;
		Vocabulario=new ArrayList<String>();
		
		LCole.getCollection().getVocabularios().put(atributosmetadatosCategoria, Vocabulario);
		
		String VistaMetaType=new String(NameConstantsChasqui.METATYPE);
		CompleteOperationalValueType MetaType=new CompleteOperationalValueType(NameConstantsChasqui.METATYPETYPE,NameConstantsChasqui.CONTROLED,VistaMetaType);
		atributosmetadatosCategoria.getShows().add(MetaType);
		
	}

	public ElementType_Taxonomias_Taxonomia_Nodo(CompleteTextElementType atributosmetadatosCategoria,
			String num_ruta, String idov, ArrayList<String> Vocabulario,LoadCollectionChasqui lcole) {
		this.atributosmetadatosCategoria=atributosmetadatosCategoria;
		this.num_ruta=num_ruta;
		this.idov=idov;
		this.Vocabulario=Vocabulario;
		LCole=lcole;
		LCole.getCollection().getVocabularios().put(atributosmetadatosCategoria, Vocabulario);
	}

	
	
	

	@Override
	public void ProcessAttributes() {
		process_Vocabulary();		
	}

	@Override
	public void ProcessInstances() {
		try {
			ResultSet rs=LCole.getSQL().RunQuerrySELECT("SELECT contenido,num_ruta FROM metadatos WHERE (ruta= '/manifest/metadata/lom/classification/taxonpath/taxon/entry/langstring' AND idov='"+idov+"') ORDER BY contenido;");
			if (rs!=null) 
			{
				while (rs.next()) {
					String contenido=rs.getObject("contenido").toString();
					String num_rutaValid=rs.getObject("num_ruta").toString();
					String[] DatosRutaSection=num_ruta.split("\\.");
					String[] DatosRutaEntry=num_rutaValid.split("\\.");
					if (contenido!=null&&num_rutaValid!=null&&!num_rutaValid.isEmpty()&&!contenido.isEmpty()&&DatosRutaSection[4].equals(DatosRutaEntry[4]))
						{
						
//						Term T=StaticFunctionsChasqui.findTerm(atributosmetadatosCategoria,contenido);
						Integer idovInt=Integer.parseInt(idov);
						CompleteDocuments OV=LCole.getCollection().getObjetoVirtual().get(idovInt);
							OV.getDescription().add(new CompleteTextElement(atributosmetadatosCategoria, contenido));
						}
					
				}
			rs.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}

	protected void process_Vocabulary() {
		try {
			ResultSet rs=LCole.getSQL().RunQuerrySELECT("SELECT distinct contenido,num_ruta FROM metadatos WHERE (ruta= '/manifest/metadata/lom/classification/taxonpath/taxon/entry/langstring' AND idov='"+idov+"') ORDER BY contenido;");
			if (rs!=null) 
			{
				while (rs.next()) {
					String contenido=rs.getObject("contenido").toString();
					String num_rutaValid=rs.getObject("num_ruta").toString();
					String[] DatosRutaSection=num_ruta.split("\\.");
					String[] DatosRutaEntry=num_rutaValid.split("\\.");
					if (contenido!=null&&num_rutaValid!=null&&!num_rutaValid.isEmpty()&&!contenido.isEmpty()&&DatosRutaSection[4].equals(DatosRutaEntry[4]))
						{
						StaticFunctionsChasqui.addTerm(Vocabulario,contenido);				
						}
					
				}
			rs.close();
			}
			
			
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	
public CompleteTextElementType getExtendMetaControlled() {
	return atributosmetadatosCategoria;
}

	public void setIdov(String idov) {
		this.idov = idov;
	}
	
	public void setNum_ruta(String num_ruta) {
		this.num_ruta = num_ruta;
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
