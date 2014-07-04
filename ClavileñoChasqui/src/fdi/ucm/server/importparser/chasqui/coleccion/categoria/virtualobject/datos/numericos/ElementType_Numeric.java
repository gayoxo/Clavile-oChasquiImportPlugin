package fdi.ucm.server.importparser.chasqui.coleccion.categoria.virtualobject.datos.numericos;

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
 * Clase que implementa el tipo de cada una de la categorias de el modelo numerico
 * @author Joaquin Gayoso-Cabada
 *
 */
public class ElementType_Numeric implements InterfaceChasquiparser {


	private CompleteTextElementType atributoNumerico;
	private ElementType_Numeric Hermano;
	private ArrayList<String> vocabularioUnidades;
	private LoadCollectionChasqui LCole;
	private static ArrayList<CompleteTextElementType> Hermanos;


	/**
	 * Constructor por parametros de la clase.
	 * @param name del tipo dentro de la categoria.
	 * @param browseable si es navegable.
	 * @param father padre del tipo.
	 */
	public ElementType_Numeric(
			String name, boolean browseable, CompleteElementType father,LoadCollectionChasqui lcole) {
		LCole=lcole;
		atributoNumerico=new CompleteTextElementType(name, father);
		Hermano=null;
		
		CompleteOperationalView VistaOV=new CompleteOperationalView(NameConstantsChasqui.PRESNTACION);
		
		CompleteOperationalValueType Valor=new CompleteOperationalValueType(NameConstantsChasqui.VISIBLESHOWN,Boolean.toString(true),VistaOV);
		CompleteOperationalValueType Valor2=new CompleteOperationalValueType(NameConstantsChasqui.BROWSERSHOWN,Boolean.toString(browseable),VistaOV);
		CompleteOperationalValueType Valor3=new CompleteOperationalValueType(NameConstantsChasqui.SUMMARYSHOWN,Boolean.toString(false),VistaOV);
		
		VistaOV.getValues().add(Valor);
		VistaOV.getValues().add(Valor2);
		VistaOV.getValues().add(Valor3);
		atributoNumerico.getShows().add(VistaOV);
		
		CompleteOperationalView VistaMetaType=new CompleteOperationalView(NameConstantsChasqui.METATYPE);
		CompleteOperationalValueType MetaType=new CompleteOperationalValueType(NameConstantsChasqui.METATYPETYPE,NameConstantsChasqui.NUMERIC,VistaMetaType);
		VistaMetaType.getValues().add(MetaType);
		atributoNumerico.getShows().add(VistaMetaType);
	}


/**
 * Constructor por parametros de la clase
 * @param name del tipo dentro de la categoria.
	 * @param browseable si es navegable.
	 * @param father padre del tipo.
 * @param hermano Objeto que es su hermano, compartiran el vocabulario.
 */
	public ElementType_Numeric(
			String name, boolean browseable, CompleteElementType father,
			ElementType_Numeric hermano,LoadCollectionChasqui lcole) {
		LCole=lcole;
		atributoNumerico=new CompleteTextElementType(name, father);
		Hermano=hermano;

		CompleteOperationalView VistaOV=new CompleteOperationalView(NameConstantsChasqui.PRESNTACION);
		
		CompleteOperationalValueType Valor=new CompleteOperationalValueType(NameConstantsChasqui.VISIBLESHOWN,Boolean.toString(true),VistaOV);
		CompleteOperationalValueType Valor2=new CompleteOperationalValueType(NameConstantsChasqui.BROWSERSHOWN,Boolean.toString(browseable),VistaOV);
		CompleteOperationalValueType Valor3=new CompleteOperationalValueType(NameConstantsChasqui.SUMMARYSHOWN,Boolean.toString(false),VistaOV);
		
		VistaOV.getValues().add(Valor);
		VistaOV.getValues().add(Valor2);
		VistaOV.getValues().add(Valor3);
		atributoNumerico.getShows().add(VistaOV);
		
		CompleteOperationalView VistaMetaType=new CompleteOperationalView(NameConstantsChasqui.METATYPE);
		CompleteOperationalValueType MetaType=new CompleteOperationalValueType(NameConstantsChasqui.METATYPETYPE,NameConstantsChasqui.NUMERIC,VistaMetaType);
		VistaMetaType.getValues().add(MetaType);
		atributoNumerico.getShows().add(VistaMetaType);
		
	}



	/**
	 * Funcion que procesa al elemento.
	 */
	public void ProcessAttributes() {
		ElementType_Numeric_Unidades ATCTU=new ElementType_Numeric_Unidades("Unidades", false, atributoNumerico,LCole);
		//ExtendMetaControlled Salida=(ExtendMetaControlled)atributoNumerico.addMeta(ATCTU.getExtendMetaControlled());
			atributoNumerico.getSons().add(ATCTU.getExtendMetaControlled());
			vocabularioUnidades=ATCTU.getVocabulario();
			if (Hermano==null)
				ATCTU.ProcessAttributes();
			else {
				LCole.getCollection().getVocabularios().put(ATCTU.getExtendMetaControlled(), vocabularioUnidades);
//				ATCTU.getExtendMetaControlled().setVocabulary(Hermano.getVocabularioUnidades());
			}
		
		
	}

	/**
	 * Funcion que retorna el atributo numerico.
	 * @return el atributo numerico.
	 */
	public CompleteTextElementType getExtendMetaNumeric() {
		return atributoNumerico;
	}
	
	/**
	 * Obtiene el vocabulario asociado a las unidades.
	 * @return el vocabulario asociado.
	 */
	public ArrayList<String> getVocabularioUnidades() {
		return vocabularioUnidades;
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


	@Override
	public void ProcessInstances() {
		if (atributoNumerico.getSons().size()>0)
			{
			ElementType_Numeric_Unidades aTCTUnidades_ExtendMetaControlled=new ElementType_Numeric_Unidades((CompleteTextElementType) atributoNumerico.getSons().get(0),LCole);
			aTCTUnidades_ExtendMetaControlled.ProcessInstances();
			}
		else ProcessInstancesSinUnidades();
		}


	private void ProcessInstancesSinUnidades() {
try {
			
			ResultSet rs=LCole.getSQL().RunQuerrySELECT("SELECT * FROM atributos_numericos WHERE categoria='" +  ((CompleteElementType)atributoNumerico.getFather()).getName() +"' AND nom_atrib='" + atributoNumerico.getName() + "' ORDER BY idov;");
			if (rs!=null) 
			{
				Integer idovPre=null;
				int counterBrother=0;
				while (rs.next()) {
					
					String idov=rs.getObject("idov").toString();
					Object temp2=rs.getObject("valor");
					String Valor="";
					if (temp2!=null)
						Valor=temp2.toString();
					if (idov!=null&&!idov.isEmpty()&&!Valor.isEmpty())
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
						}
				}
			rs.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		
	}


	
	
}
