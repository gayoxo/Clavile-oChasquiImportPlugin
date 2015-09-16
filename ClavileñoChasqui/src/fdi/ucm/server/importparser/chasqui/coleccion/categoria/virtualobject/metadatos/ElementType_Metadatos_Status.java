package fdi.ucm.server.importparser.chasqui.coleccion.categoria.virtualobject.metadatos;

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
 * Clase que parsea el estatus del meta.
 * @author Joaquin Gayoso-Cabada.
 *
 */
public class ElementType_Metadatos_Status implements InterfaceChasquiparser{


	private CompleteTextElementType atributosmetadatosstatus;
	private ArrayList<String> Vocabulario;
	private LoadCollectionChasqui LCole;
	private static ArrayList<CompleteTextElementType> StatusElements;

	/**
	 * Constructor con parametros para la clase.
	 * @param name nombre del Meta.
	 * @param browseable Navagabilidad
	 * @param father Padre del Meta
	 */
	public ElementType_Metadatos_Status(
			String name, boolean browseable, CompleteElementType father,LoadCollectionChasqui lcole) {
		LCole=lcole;
		atributosmetadatosstatus=new CompleteTextElementType(name, father);
		
		String VistaOV=new String(NameConstantsChasqui.PRESNTACION);
		
		CompleteOperationalValueType Valor=new CompleteOperationalValueType(NameConstantsChasqui.VISIBLESHOWN,Boolean.toString(true),VistaOV);
		CompleteOperationalValueType Valor2=new CompleteOperationalValueType(NameConstantsChasqui.BROWSERSHOWN,Boolean.toString(browseable),VistaOV);
		CompleteOperationalValueType Valor3=new CompleteOperationalValueType(NameConstantsChasqui.SUMMARYSHOWN,Boolean.toString(false),VistaOV);
		
		atributosmetadatosstatus.getShows().add(Valor);
		atributosmetadatosstatus.getShows().add(Valor2);
		atributosmetadatosstatus.getShows().add(Valor3);
		
		String VistaMetaType=new String(NameConstantsChasqui.METATYPE);
		CompleteOperationalValueType MetaType=new CompleteOperationalValueType(NameConstantsChasqui.METATYPETYPE,NameConstantsChasqui.CONTROLED,VistaMetaType);
		atributosmetadatosstatus.getShows().add(MetaType);
		
		Vocabulario= new ArrayList<String>();
		LCole.getCollection().getVocabularios().put(atributosmetadatosstatus, Vocabulario);
	}


	@Override
	public void ProcessAttributes() {
		process_Vocabulary();
		if (Vocabulario.isEmpty())
			atributosmetadatosstatus.getFather().getSons().remove(atributosmetadatosstatus);
	}

	/**
	 * Funcion que procesa el vocabulario.
	 */
	protected void process_Vocabulary() {
		try {
			ResultSet rs=LCole.getSQL().RunQuerrySELECT("SELECT distinct contenido FROM metadatos WHERE ruta='/manifest/metadata/lom/lifecycle/status/value/langstring' ORDER BY contenido;");
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
	
	@Override
	public void ProcessInstances() {
		try {
			ResultSet rs=LCole.getSQL().RunQuerrySELECT("SELECT * FROM metadatos WHERE ruta='/manifest/metadata/lom/lifecycle/status/value/langstring' ORDER BY idov;");
			if (rs!=null) 
			{
				Integer preIdov=null;
				int count=0;
				while (rs.next()) {
					
					
					String idov=rs.getObject("idov").toString();
					Object temp=rs.getObject("contenido");
					String Valor="";
					if (temp!=null)
						Valor=temp.toString();
					if (idov!=null&&!idov.isEmpty()&&!Valor.isEmpty())
						{
							Integer Idov=Integer.parseInt(idov);
							if (preIdov!=null&&preIdov==Idov)
								count++;
							else
								{
								preIdov=Idov;
								count=0;
								}
							CompleteTextElement EMCV=new CompleteTextElement(StatusElements.get(count), Valor);
							CompleteDocuments OV=LCole.getCollection().getObjetoVirtual().get(Idov);
							OV.getDescription().add(EMCV);
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
		return atributosmetadatosstatus;
	}


	/**
	 * @return the statusElements
	 */
	public static ArrayList<CompleteTextElementType> getStatusElements() {
		return StatusElements;
	}


	/**
	 * @param statusElements the statusElements to set
	 */
	public static void setStatusElements(ArrayList<CompleteTextElementType> statusElements) {
		StatusElements = statusElements;
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
