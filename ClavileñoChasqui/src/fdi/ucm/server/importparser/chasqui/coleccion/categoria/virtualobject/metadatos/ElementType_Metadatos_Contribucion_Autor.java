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
import fdi.ucm.server.modelComplete.collection.grammar.CompleteOperationalView;
import fdi.ucm.server.modelComplete.collection.grammar.CompleteTextElementType;

/**
 * Clase que parsea el autor del los contribuidores para los metadatos
 * @author Joaquin Gayoso-Cabada
 *
 */
public class ElementType_Metadatos_Contribucion_Autor implements InterfaceChasquiparser{

	private CompleteTextElementType atributosmetadatosautor;
	private ArrayList<String> Vocabulario;
	private LoadCollectionChasqui LCole;

	/**
	 * Constructor por parametros
	 * @param name Nombre del Meta
	 * @param browseable Navegabilidad.
	 * @param father Padre del Meta
	 */
	public ElementType_Metadatos_Contribucion_Autor(
			String name, boolean browseable, CompleteElementType father,LoadCollectionChasqui lcole) {
		LCole=lcole;
		atributosmetadatosautor=new CompleteTextElementType(name, father);
		CompleteOperationalView VistaOV=new CompleteOperationalView(NameConstantsChasqui.PRESNTACION);
		
		CompleteOperationalValueType Valor=new CompleteOperationalValueType(NameConstantsChasqui.VISIBLESHOWN,Boolean.toString(true),VistaOV);
		CompleteOperationalValueType Valor2=new CompleteOperationalValueType(NameConstantsChasqui.BROWSERSHOWN,Boolean.toString(browseable),VistaOV);
		CompleteOperationalValueType Valor3=new CompleteOperationalValueType(NameConstantsChasqui.SUMMARYSHOWN,Boolean.toString(false),VistaOV);
		
		VistaOV.getValues().add(Valor);
		VistaOV.getValues().add(Valor2);
		VistaOV.getValues().add(Valor3);
		atributosmetadatosautor.getShows().add(VistaOV);
		
		CompleteOperationalView VistaMetaType=new CompleteOperationalView(NameConstantsChasqui.METATYPE);
		CompleteOperationalValueType MetaType=new CompleteOperationalValueType(NameConstantsChasqui.METATYPETYPE,NameConstantsChasqui.CONTROLED,VistaMetaType);
		VistaMetaType.getValues().add(MetaType);
		atributosmetadatosautor.getShows().add(VistaMetaType);
		
		Vocabulario= new ArrayList<String>();
		LCole.getCollection().getVocabularios().put(atributosmetadatosautor, Vocabulario);
	}


	@Override
	public void ProcessAttributes() {
		process_Vocabulary();
		if (Vocabulario.isEmpty())
			atributosmetadatosautor.getFather().getSons().remove(atributosmetadatosautor);
	}

	/**
	 * Funcion que procesa el vocabulario
	 */
	protected void process_Vocabulary() {
		try {
			ResultSet rs=LCole.getSQL().RunQuerrySELECT("SELECT distinct contenido FROM metadatos WHERE ruta='/manifest/metadata/lom/lifecycle/contribute/centity/vcard' ORDER BY contenido;");
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
		return atributosmetadatosautor;
	}



	@Override
	public void ProcessInstances() {
		// Ninguna instancia
		
	}
}
