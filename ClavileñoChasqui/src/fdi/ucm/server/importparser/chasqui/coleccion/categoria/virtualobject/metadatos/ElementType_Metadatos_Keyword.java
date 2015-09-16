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
 * Clase que parsea las keyword en los metadatos
 * @author Joaquin Gayoso-Cabada
 *
 */
public class ElementType_Metadatos_Keyword implements InterfaceChasquiparser{

	private CompleteTextElementType extendcontroledkeywords;
	private ArrayList<String> Vocabulario;
	private LoadCollectionChasqui LCole;
	private static ArrayList<CompleteTextElementType> keywordElements;
	
	/**
	 * Constructor por parametros
	 * @param name Nombre del objeto
	 * @param browseable Navegabilidad
	 * @param father Padre del objeto
	 */
	public ElementType_Metadatos_Keyword(
			String name, boolean browseable, CompleteElementType father,LoadCollectionChasqui lcole) {
		LCole=lcole;
		extendcontroledkeywords=new CompleteTextElementType(name, father);

		String VistaOV=new String(NameConstantsChasqui.PRESNTACION);
		
		CompleteOperationalValueType Valor=new CompleteOperationalValueType(NameConstantsChasqui.VISIBLESHOWN,Boolean.toString(true),VistaOV);
		CompleteOperationalValueType Valor2=new CompleteOperationalValueType(NameConstantsChasqui.BROWSERSHOWN,Boolean.toString(browseable),VistaOV);
		CompleteOperationalValueType Valor3=new CompleteOperationalValueType(NameConstantsChasqui.SUMMARYSHOWN,Boolean.toString(false),VistaOV);
		
		extendcontroledkeywords.getShows().add(Valor);
		extendcontroledkeywords.getShows().add(Valor2);
		extendcontroledkeywords.getShows().add(Valor3);
		
		String VistaMetaType=new String(NameConstantsChasqui.METATYPE);
		CompleteOperationalValueType MetaType=new CompleteOperationalValueType(NameConstantsChasqui.METATYPETYPE,NameConstantsChasqui.CONTROLED,VistaMetaType);
		extendcontroledkeywords.getShows().add(MetaType);
		
		Vocabulario= new ArrayList<String>();
		LCole.getCollection().getVocabularios().put(extendcontroledkeywords, Vocabulario);
	}

	/**
	 * Funcion que procesa el vocabulario del objeto
	 */
	protected void process_Vocabulary() {
		try {
			ResultSet rs=LCole.getSQL().RunQuerrySELECT("SELECT distinct contenido FROM metadatos Where ruta = '/manifest/metadata/lom/general/coverage/langstring' ORDER BY contenido;");
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
	public void ProcessAttributes() {
		process_Vocabulary();
		if (Vocabulario.isEmpty())
			extendcontroledkeywords.getFather().getSons().remove(extendcontroledkeywords);
		
	}

	@Override
	public void ProcessInstances() {
		try {
		
		ResultSet rs=LCole.getSQL().RunQuerrySELECT("SELECT * FROM metadatos Where ruta = '/manifest/metadata/lom/general/coverage/langstring' ORDER BY idov;");
		if (rs!=null) 
		{
			Integer preIdov=null;
			int count=0;
			while (rs.next()) {
				
				String idov=rs.getObject("idov").toString();
				Object temp=rs.getObject("contenido");
				String Unidades="";
				if (temp!=null)
					Unidades=temp.toString();
				if (idov!=null&&!idov.isEmpty()&&!Unidades.isEmpty())
					{
					Integer Idov=Integer.parseInt(idov);
					if (preIdov!=null&&preIdov==Idov)
						count++;
					else
						{
						preIdov=Idov;
						count=0;
						}
					CompleteTextElement EMCV=new CompleteTextElement(keywordElements.get(count),Unidades);
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
		return extendcontroledkeywords;
	}

	/**
	 * @return the keywordElements
	 */
	public static ArrayList<CompleteTextElementType> getKeywordElements() {
		return keywordElements;
	}

	/**
	 * @param keywordElements the keywordElements to set
	 */
	public static void setKeywordElements(ArrayList<CompleteTextElementType> keywordElements) {
		ElementType_Metadatos_Keyword.keywordElements = keywordElements;
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
