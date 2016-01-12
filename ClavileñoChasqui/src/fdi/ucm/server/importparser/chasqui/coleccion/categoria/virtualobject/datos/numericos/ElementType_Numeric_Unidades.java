package fdi.ucm.server.importparser.chasqui.coleccion.categoria.virtualobject.datos.numericos;

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
 * Clase que implementa las unidades de cada uno de los elementos del tipo
 * @author Joaquin Gayoso-Cabada
 *
 */
public class ElementType_Numeric_Unidades  implements InterfaceChasquiparser{


	private CompleteTextElementType atributonumericounidad;
	private ArrayList<String> Vocabulario;
	private LoadCollectionChasqui LCole;
	
	/**
	 * Constructor por defecto de la clase.
	 * @param name nombre de la unidad.
	 * @param browseable si es navegable.
	 * @param father padre de la unidad.
	 */
	public ElementType_Numeric_Unidades(String name,
			boolean browseable, CompleteTextElementType father,LoadCollectionChasqui lcole) {
		LCole=lcole;
		atributonumericounidad=new CompleteTextElementType(name, father);

		CompleteOperationalView VistaOV=new CompleteOperationalView(NameConstantsChasqui.PRESNTACION);
		
		CompleteOperationalValueType Valor=new CompleteOperationalValueType(NameConstantsChasqui.VISIBLESHOWN,Boolean.toString(true),VistaOV);
		CompleteOperationalValueType Valor2=new CompleteOperationalValueType(NameConstantsChasqui.BROWSERSHOWN,Boolean.toString(browseable),VistaOV);
		CompleteOperationalValueType Valor3=new CompleteOperationalValueType(NameConstantsChasqui.SUMMARYSHOWN,Boolean.toString(false),VistaOV);
		
		
		VistaOV.getValues().add(Valor);
		VistaOV.getValues().add(Valor2);
		VistaOV.getValues().add(Valor3);
		atributonumericounidad.getShows().add(VistaOV);
		
		CompleteOperationalView VistaMetaType=new CompleteOperationalView(NameConstantsChasqui.METATYPE);
		CompleteOperationalValueType MetaType=new CompleteOperationalValueType(NameConstantsChasqui.METATYPETYPE,NameConstantsChasqui.CONTROLED,VistaMetaType);
		VistaMetaType.getValues().add(MetaType);
		atributonumericounidad.getShows().add(VistaMetaType);
		
		Vocabulario= new ArrayList<String>();
		LCole.getCollection().getVocabularios().put(atributonumericounidad, Vocabulario);
		
		
	}

	/**
	 * Funcion que construye la unidad con una copia de ella
	 * @param atributonumericounidad unidad nueva que sustituye
	 */
	public ElementType_Numeric_Unidades(
			CompleteTextElementType atributonumericounidad,LoadCollectionChasqui lcole) {
		LCole=lcole;
		this.atributonumericounidad = atributonumericounidad;
	}

	/**
	 * Funcion que procesa el vocabulario
	 */
	protected void process_Vocabulary() {
		try {
			ResultSet rs=LCole.getSQL().RunQuerrySELECT("SELECT distinct unidades FROM atributos_numericos WHERE categoria='"+ ((CompleteElementType)atributonumericounidad.getFather().getFather()).getName() +"' AND nom_atrib='"+((CompleteElementType)atributonumericounidad.getFather()).getName()+"' ORDER BY unidades;");
			if (rs!=null) 
			{
				while (rs.next()) {
					
					String Dato=rs.getObject("unidades").toString();
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
			atributonumericounidad.getFather().getSons().remove(atributonumericounidad);
		
	}
	

	/**
	 * Funcion que devuelve el elemento controlado.
	 * @return el elemento controlado.
	 */
	public CompleteTextElementType getExtendMetaControlled() {
		return atributonumericounidad;
	}



	@Override
	public void ProcessInstances() {
		try {
			
			ResultSet rs=LCole.getSQL().RunQuerrySELECT("SELECT * FROM atributos_numericos WHERE categoria='" +  ((CompleteElementType)atributonumericounidad.getFather().getFather()).getName() +"' AND nom_atrib='" + ((CompleteElementType)atributonumericounidad.getFather()).getName() + "' ORDER BY idov;");
			if (rs!=null) 
			{
				Integer idovPre=null;
				int counterBrother=0;
				while (rs.next()) {
					
					String idov=rs.getObject("idov").toString();
					Object temp=rs.getObject("unidades");
					String Unidades="";
					if (temp!=null)
						Unidades=temp.toString();
					Object temp2=rs.getObject("valor");
					String Valor="";
					if (temp2!=null)
						Valor=temp2.toString();
					if (idov!=null&&!idov.isEmpty()&&!Unidades.isEmpty()&&!Valor.isEmpty())
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
						CompleteTextElement EMNV=new CompleteTextElement(ElementType_Numeric.getHermanos().get(counterBrother), Valor);
						DObject.getDescription().add(EMNV);
						CompleteTextElement EMCV=new CompleteTextElement((CompleteTextElementType) ElementType_Numeric.getHermanos().get(counterBrother).getSons().get(0),Unidades);
						DObject.getDescription().add(EMCV);
						
						}
					
				}
			rs.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
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
