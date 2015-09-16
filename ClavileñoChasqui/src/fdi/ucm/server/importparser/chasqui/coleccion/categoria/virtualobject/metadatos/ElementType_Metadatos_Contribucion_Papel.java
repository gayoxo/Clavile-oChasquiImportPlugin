package fdi.ucm.server.importparser.chasqui.coleccion.categoria.virtualobject.metadatos;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import fdi.ucm.server.importparser.chasqui.LoadCollectionChasqui;
import fdi.ucm.server.importparser.chasqui.NameConstantsChasqui;
import fdi.ucm.server.importparser.chasqui.StaticFunctionsChasqui;
import fdi.ucm.server.importparser.chasqui.InterfaceChasquiparser;
import fdi.ucm.server.modelComplete.collection.grammar.CompleteElementType;
import fdi.ucm.server.modelComplete.collection.grammar.CompleteOperationalValueType;
import fdi.ucm.server.modelComplete.collection.grammar.CompleteTextElementType;

/**
 * Clase que parsea el papel del los contribuidores para los metadatos
 * @author Joaquin Gayoso-Cabada
 *
 */
public class ElementType_Metadatos_Contribucion_Papel implements InterfaceChasquiparser{

	private CompleteTextElementType atributosmetadatospapel;
	private ArrayList<String> Vocabulario;
	private LoadCollectionChasqui LCole;

	/**
	 * Constructor por parametros
	 * @param name Nombre del Meta
	 * @param browseable Navegabilidad.
	 * @param father Padre del Meta
	 */
	public ElementType_Metadatos_Contribucion_Papel(
			String name, boolean browseable, CompleteElementType father,LoadCollectionChasqui lcole) {
		LCole=lcole;
		atributosmetadatospapel=new CompleteTextElementType(name, father);
		
		String VistaOV=new String(NameConstantsChasqui.PRESNTACION);
		
		CompleteOperationalValueType Valor=new CompleteOperationalValueType(NameConstantsChasqui.VISIBLESHOWN,Boolean.toString(true),VistaOV);
		CompleteOperationalValueType Valor2=new CompleteOperationalValueType(NameConstantsChasqui.BROWSERSHOWN,Boolean.toString(browseable),VistaOV);
		CompleteOperationalValueType Valor3=new CompleteOperationalValueType(NameConstantsChasqui.SUMMARYSHOWN,Boolean.toString(false),VistaOV);
		
		atributosmetadatospapel.getShows().add(Valor);
		atributosmetadatospapel.getShows().add(Valor2);
		atributosmetadatospapel.getShows().add(Valor3);
		
		String VistaMetaType=new String(NameConstantsChasqui.METATYPE);
		CompleteOperationalValueType MetaType=new CompleteOperationalValueType(NameConstantsChasqui.METATYPETYPE,NameConstantsChasqui.CONTROLED,VistaMetaType);
		atributosmetadatospapel.getShows().add(MetaType);

		
		Vocabulario= new ArrayList<String>();
		LCole.getCollection().getVocabularios().put(atributosmetadatospapel, Vocabulario);
	}


	@Override
	public void ProcessAttributes() {
		process_Vocabulary();
		if (Vocabulario.isEmpty())
			atributosmetadatospapel.getFather().getSons().remove(atributosmetadatospapel);
	}

	/**
	 * Funcion que procesa el vocabulario
	 */
	protected void process_Vocabulary() {
		try {
			ResultSet rs=LCole.getSQL().RunQuerrySELECT("SELECT distinct contenido FROM metadatos WHERE ruta='/manifest/metadata/lom/lifecycle/contribute/role/value/langstring' ORDER BY contenido;");
			if (rs!=null) 
			{
				while (rs.next()) {
					String Dato=rs.getObject("contenido").toString();
					if (Dato!=null&&!Dato.isEmpty())
						{
						StaticFunctionsChasqui.addTerm(Vocabulario,Dato);
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
		return atributosmetadatospapel;
	}


	@Override
	public void ProcessInstances() {
		
		
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
