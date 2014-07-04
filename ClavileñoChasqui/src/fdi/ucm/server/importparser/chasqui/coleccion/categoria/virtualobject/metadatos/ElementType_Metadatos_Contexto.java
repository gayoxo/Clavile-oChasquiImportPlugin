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
import fdi.ucm.server.modelComplete.collection.grammar.CompleteIterator;
import fdi.ucm.server.modelComplete.collection.grammar.CompleteOperationalValueType;
import fdi.ucm.server.modelComplete.collection.grammar.CompleteOperationalView;
import fdi.ucm.server.modelComplete.collection.grammar.CompleteStructure;
import fdi.ucm.server.modelComplete.collection.grammar.CompleteTextElementType;

/**
 * Clase que parsea el contexto en la categoria de los metadatos
 * @author Joaquin Gayoso-Cabada
 *
 */
public class ElementType_Metadatos_Contexto implements InterfaceChasquiparser {


	private CompleteTextElementType atributosmetadatoscontexto;
	private CompleteIterator Father;
	private ArrayList<String> Vocabulario;
	private LoadCollectionChasqui LCole;
	
	/**
	 * Constructor por parametros de la categoria.
	 * @param name nombre del Meta
	 * @param browseable navegabilidad del meta
	 * @param father categoria padre
	 * @param multi si es multievaluado 
	 */
	public ElementType_Metadatos_Contexto(
			String name, boolean browseable, CompleteStructure father,LoadCollectionChasqui lcole) {
		LCole=lcole;
		Father = (CompleteIterator) father;
		atributosmetadatoscontexto=new CompleteTextElementType(name, Father);

		CompleteOperationalView VistaOV=new CompleteOperationalView(NameConstantsChasqui.PRESNTACION);
		
		CompleteOperationalValueType Valor=new CompleteOperationalValueType(NameConstantsChasqui.VISIBLESHOWN,Boolean.toString(true),VistaOV);
		CompleteOperationalValueType Valor2=new CompleteOperationalValueType(NameConstantsChasqui.BROWSERSHOWN,Boolean.toString(browseable),VistaOV);
		CompleteOperationalValueType Valor3=new CompleteOperationalValueType(NameConstantsChasqui.SUMMARYSHOWN,Boolean.toString(false),VistaOV);
		
		
		VistaOV.getValues().add(Valor);
		VistaOV.getValues().add(Valor2);
		VistaOV.getValues().add(Valor3);
		
		CompleteOperationalView VistaMetaType=new CompleteOperationalView(NameConstantsChasqui.METATYPE);
		CompleteOperationalValueType MetaType=new CompleteOperationalValueType(NameConstantsChasqui.METATYPETYPE,NameConstantsChasqui.CONTROLED,VistaMetaType);
		VistaMetaType.getValues().add(MetaType);
		atributosmetadatoscontexto.getShows().add(VistaMetaType);
		
		atributosmetadatoscontexto.getShows().add(VistaOV);
		
		Vocabulario= new ArrayList<String>();
		LCole.getCollection().getVocabularios().put(atributosmetadatoscontexto, Vocabulario);
	}

	/**
	 * Funcion que procesa el vocabulario de la categoria
	 */
	protected void process_Vocabulary() {
		try {
			ResultSet rs=LCole.getSQL().RunQuerrySELECT("SELECT distinct contenido FROM metadatos Where ruta = '/manifest/metadata/lom/general/keyword/langstring' ORDER BY contenido;");
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
			atributosmetadatoscontexto.getFather().getSons().remove(atributosmetadatoscontexto);
	}

	
	/**
	 * 
	 * @return el ExtendMetaControlled
	 */
	public CompleteTextElementType getExtendMetaControlled() {
		return atributosmetadatoscontexto;
	}



	@Override
	public void ProcessInstances() {
		try {
			ResultSet rs=LCole.getSQL().RunQuerrySELECT("SELECT * FROM metadatos Where ruta = '/manifest/metadata/lom/general/keyword/langstring' ORDER BY idov;");
			if (rs!=null) 
			{
				Integer preIdov=null;
				int MaxCount=0;
				int count=1;
				while (rs.next()) {
					
					String idov=rs.getObject("idov").toString();
					Object temp=rs.getObject("contenido");
					String Valor="";
					if (temp!=null)
						Valor=temp.toString();
					if (idov!=null&&!idov.isEmpty()&&!Valor.isEmpty())
						{
						
						Integer Idov=Integer.parseInt(idov);
						if (preIdov!=null&&preIdov.intValue()==Idov.intValue())
							count++;
						else
							{
							if (count>MaxCount)
								MaxCount=count;
							preIdov=Idov;
							count=1;
							}
						CompleteTextElement EMCV=new CompleteTextElement(atributosmetadatoscontexto,Valor);
						EMCV.getAmbitos().add(count);
						CompleteDocuments OV=LCole.getCollection().getObjetoVirtual().get(Idov);
						OV.getDescription().add(EMCV);

						}
					
				}
			rs.close();
			Father.setAmbitoSTotales(MaxCount);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}


	
	
}
