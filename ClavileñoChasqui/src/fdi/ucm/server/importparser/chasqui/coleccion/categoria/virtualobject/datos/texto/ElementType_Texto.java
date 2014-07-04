package fdi.ucm.server.importparser.chasqui.coleccion.categoria.virtualobject.datos.texto;

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
import fdi.ucm.server.modelComplete.collection.grammar.CompleteOperationalView;
import fdi.ucm.server.modelComplete.collection.grammar.CompleteTextElementType;

/**
 * Clase que implementa la construccion de los atributos de tipo texto controlados para cada categoria
 * @author Joaquin Gayoso-Cabada
 *
 */
public class ElementType_Texto  implements InterfaceChasquiparser  {



	private CompleteTextElementType atributoTexto;
	private ArrayList<String> Vocabulario;
	private LoadCollectionChasqui LCole;
	private static ArrayList<CompleteTextElementType> Hermanos;

	/**
	 * Constructor por defecto
	 * @param name nombre de la nueva clase
	 * @param browseable si es navegable
	 * @param father padre del objeto en la jerarquia
	 */
	public ElementType_Texto(String name,
			boolean browseable, CompleteElementType father,LoadCollectionChasqui lcole) {
		atributoTexto=new CompleteTextElementType(name,father);
		LCole=lcole;
		
		CompleteOperationalView VistaOV=new CompleteOperationalView(NameConstantsChasqui.PRESNTACION);
		
		CompleteOperationalValueType Valor=new CompleteOperationalValueType(NameConstantsChasqui.VISIBLESHOWN,Boolean.toString(true),VistaOV);
		CompleteOperationalValueType Valor2=new CompleteOperationalValueType(NameConstantsChasqui.BROWSERSHOWN,Boolean.toString(browseable),VistaOV);
		CompleteOperationalValueType Valor3=new CompleteOperationalValueType(NameConstantsChasqui.SUMMARYSHOWN,Boolean.toString(false),VistaOV);
		
		VistaOV.getValues().add(Valor);
		VistaOV.getValues().add(Valor2);
		VistaOV.getValues().add(Valor3);
		
		atributoTexto.getShows().add(VistaOV);
		
		CompleteOperationalView VistaMetaType=new CompleteOperationalView(NameConstantsChasqui.METATYPE);
		CompleteOperationalValueType MetaType=new CompleteOperationalValueType(NameConstantsChasqui.METATYPETYPE,NameConstantsChasqui.CONTROLED,VistaMetaType);
		VistaMetaType.getValues().add(MetaType);
		atributoTexto.getShows().add(VistaMetaType);
		
		Vocabulario= new ArrayList<String>();
		LCole.getCollection().getVocabularios().put(atributoTexto, Vocabulario);
		
	}
	
	/**
	 * Conctructor con todos los parametros
	  * @param name nombre de la nueva clase
	 * @param browseable si es navegable
	 * @param father padre del objeto en la jerarquia
	 * @param vocabulary vocabulario inicial
	 */
	public ElementType_Texto(String name,
			boolean browseable, CompleteElementType father,ArrayList<String> vocabulary,LoadCollectionChasqui lcole) {
		atributoTexto=new CompleteTextElementType(name,father);
		LCole=lcole;
		CompleteOperationalView VistaOV=new CompleteOperationalView(NameConstantsChasqui.PRESNTACION);
		
		CompleteOperationalValueType Valor=new CompleteOperationalValueType(NameConstantsChasqui.VISIBLESHOWN,Boolean.toString(true),VistaOV);
		CompleteOperationalValueType Valor2=new CompleteOperationalValueType(NameConstantsChasqui.BROWSERSHOWN,Boolean.toString(browseable),VistaOV);
		CompleteOperationalValueType Valor3=new CompleteOperationalValueType(NameConstantsChasqui.SUMMARYSHOWN,Boolean.toString(false),VistaOV);
		
		VistaOV.getValues().add(Valor);
		VistaOV.getValues().add(Valor2);
		VistaOV.getValues().add(Valor3);
		
		atributoTexto.getShows().add(VistaOV);
		
		CompleteOperationalView VistaMetaType=new CompleteOperationalView(NameConstantsChasqui.METATYPE);
		CompleteOperationalValueType MetaType=new CompleteOperationalValueType(NameConstantsChasqui.METATYPETYPE,NameConstantsChasqui.CONTROLED,VistaMetaType);
		VistaMetaType.getValues().add(MetaType);
		atributoTexto.getShows().add(VistaMetaType);
		
		Vocabulario= vocabulary;
		
		LCole.getCollection().getVocabularios().put(atributoTexto, Vocabulario);
		
	}
	
	@Override
	public void ProcessAttributes() {
		process_Vocabulary();
		if (Vocabulario.isEmpty())
			atributoTexto.getFather().getSons().remove(atributoTexto);
		
	}




	/**
	 * Funcion que procesa el vocabulario
	 */
	public void process_Vocabulary() {
		try {
			ResultSet rs=LCole.getSQL().RunQuerrySELECT("SELECT distinct valor FROM atributos_texto WHERE categoria='"
					+ 
					((CompleteElementType)atributoTexto.getFather()).getName()
					+"' AND nom_atrib='"
					+
					atributoTexto.getName()
					+ "' ORDER BY valor;");
			if (rs!=null) 
			{
				while (rs.next()) {
					
					String Dato=rs.getObject("valor").toString();
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
	 * Funcion que retorna el controlado
	 * @return Meta controlado
	 */
	public CompleteTextElementType getExtendMetaControlled() {
		return atributoTexto;
	}

	

	@Override
	public void ProcessInstances() {
		try {
		ResultSet rs=LCole.getSQL().RunQuerrySELECT("SELECT * FROM atributos_texto WHERE categoria='"
				+ 
				((CompleteElementType)atributoTexto.getFather()).getName()
				+"' AND nom_atrib='"
				+
				atributoTexto.getName()
				+ "' ORDER BY idov;");
		if (rs!=null) 
		{
			Integer idovPre=null;
			int counterBrother=0;
			while (rs.next()) {
				
				String idov=rs.getObject("idov").toString();
				Object temp=rs.getObject("valor");
				String Valor="";
				if (temp!=null)
					Valor=rs.getObject("valor").toString();
				if (idov!=null&&!idov.isEmpty())
					{
					int idovInt = Integer.parseInt(idov);
					if (idovPre!=null&&idovInt==idovPre)
						counterBrother++;
					else 
						{
						idovPre=idovInt;
						counterBrother=0;
						}
	
					CompleteDocuments DObject= LCole.getCollection().getObjetoVirtual().get(idovInt);
					CompleteTextElement EMNV=new CompleteTextElement(ElementType_Texto.getHermanos().get(counterBrother), Valor);
					DObject.getDescription().add(EMNV);
	
					}
				
			}
		rs.close();
		}
	} catch (SQLException e) {
		e.printStackTrace();
	}
		
	}

	/**
	 * @return the hermanos
	 */
	public static ArrayList<CompleteTextElementType> getHermanos() {
		return Hermanos;
	}

	/**
	 * @param hermanos the hermanos to set
	 */
	public static void setHermanos(ArrayList<CompleteTextElementType> hermanos) {
		Hermanos = hermanos;
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
