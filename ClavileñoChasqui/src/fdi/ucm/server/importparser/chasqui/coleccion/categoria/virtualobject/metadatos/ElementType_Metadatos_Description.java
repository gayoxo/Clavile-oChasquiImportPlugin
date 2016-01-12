package fdi.ucm.server.importparser.chasqui.coleccion.categoria.virtualobject.metadatos;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import fdi.ucm.server.importparser.chasqui.LoadCollectionChasqui;
import fdi.ucm.server.importparser.chasqui.NameConstantsChasqui;
import fdi.ucm.server.importparser.chasqui.InterfaceChasquiparser;
import fdi.ucm.server.modelComplete.collection.document.CompleteDocuments;
import fdi.ucm.server.modelComplete.collection.document.CompleteTextElement;
import fdi.ucm.server.modelComplete.collection.grammar.CompleteElementType;
import fdi.ucm.server.modelComplete.collection.grammar.CompleteOperationalValueType;
import fdi.ucm.server.modelComplete.collection.grammar.CompleteOperationalView;
import fdi.ucm.server.modelComplete.collection.grammar.CompleteTextElementType;

/**
 * Clase que implementa el parseado de la descripcion para los metadatos.
 * @author Joaquin Gayoso-Cabada.
 *
 */
public class ElementType_Metadatos_Description implements InterfaceChasquiparser{

	private CompleteTextElementType atributometadatosdescripcion;
	private LoadCollectionChasqui LCole;
	private static ArrayList<CompleteTextElementType> DescriptionElements;
	/**
	 * Constructor con parametros de la clase.
	 * @param name nombre del meta.
	 * @param browseable navegabilidad.
	 * @param fathe padre del meta.
	 */
	public ElementType_Metadatos_Description(String name, boolean browseable,
			CompleteElementType father,LoadCollectionChasqui lcole) {
		LCole=lcole;
		atributometadatosdescripcion=new CompleteTextElementType(name, father);

		CompleteOperationalView VistaOV=new CompleteOperationalView(NameConstantsChasqui.PRESNTACION);
		
		CompleteOperationalValueType Valor=new CompleteOperationalValueType(NameConstantsChasqui.VISIBLESHOWN,Boolean.toString(true),VistaOV);
		CompleteOperationalValueType Valor2=new CompleteOperationalValueType(NameConstantsChasqui.BROWSERSHOWN,Boolean.toString(browseable),VistaOV);
		CompleteOperationalValueType Valor3=new CompleteOperationalValueType(NameConstantsChasqui.SUMMARYSHOWN,Boolean.toString(false),VistaOV);
		
		VistaOV.getValues().add(Valor);
		VistaOV.getValues().add(Valor2);
		VistaOV.getValues().add(Valor3);
		
		CompleteOperationalView VistaOVMeta=new CompleteOperationalView(NameConstantsChasqui.META);
		CompleteOperationalValueType ValorMeta=new CompleteOperationalValueType(NameConstantsChasqui.TYPE,NameConstantsChasqui.DESCRIPTION,VistaOVMeta);
		VistaOVMeta.getValues().add(ValorMeta);
		
		
		CompleteOperationalView VistaMetaType=new CompleteOperationalView(NameConstantsChasqui.METATYPE);
		CompleteOperationalValueType MetaType=new CompleteOperationalValueType(NameConstantsChasqui.METATYPETYPE,NameConstantsChasqui.TEXT,VistaMetaType);
		VistaMetaType.getValues().add(MetaType);
		atributometadatosdescripcion.getShows().add(VistaMetaType);
		
		atributometadatosdescripcion.getShows().add(VistaOVMeta);
		
		atributometadatosdescripcion.getShows().add(VistaOV);
		
		
		
	}
	


	@Override
	public void ProcessInstances() {
		try {
		ResultSet rs=LCole.getSQL().RunQuerrySELECT("SELECT * FROM metadatos where ruta='/manifest/metadata/lom/general/description/langstring' ORDER BY idov;");
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
					CompleteTextElement EMCV=new CompleteTextElement(DescriptionElements.get(count), Valor);
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
	
	
	@Override
	public void ProcessAttributes() {

	}
	
	
	public CompleteTextElementType getExtendMetaText() {
		return atributometadatosdescripcion;
	}
	
	/**
	 * @return the descriptionElements
	 */
	public static ArrayList<CompleteTextElementType> getDescriptionElements() {
		return DescriptionElements;
	}
	/**
	 * @param descriptionElements the descriptionElements to set
	 */
	public static void setDescriptionElements(
			ArrayList<CompleteTextElementType> descriptionElements) {
		DescriptionElements = descriptionElements;
	}

	
}
