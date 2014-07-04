/**
 * 
 */
package fdi.ucm.server.importparser.chasqui.coleccion.categoria.virtualobject;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import fdi.ucm.server.importparser.chasqui.LoadCollectionChasqui;
import fdi.ucm.server.importparser.chasqui.NameConstantsChasqui;
import fdi.ucm.server.importparser.chasqui.StaticFunctionsChasqui;
import fdi.ucm.server.importparser.chasqui.InterfaceChasquiparser;
import fdi.ucm.server.modelComplete.collection.grammar.CompleteLinkElementType;
import fdi.ucm.server.modelComplete.collection.grammar.CompleteOperationalValueType;
import fdi.ucm.server.modelComplete.collection.grammar.CompleteOperationalView;
import fdi.ucm.server.modelComplete.collection.grammar.CompleteTextElementType;

/**
 * clase que define el parseo de los tipos de recursos en su campo Type
 * @author Joaquin Gayoso-Cabada
 *
 */
public class ElementType_Resources_Type implements InterfaceChasquiparser{

	
	
	private CompleteTextElementType atributostypes;
	private ArrayList<String> Vocabulario;
	private LoadCollectionChasqui LCole;

	public ElementType_Resources_Type(String typeFile,
			boolean b, CompleteLinkElementType attributo,LoadCollectionChasqui lcole) {
		LCole=lcole;
		atributostypes=new CompleteTextElementType(typeFile, attributo);

		CompleteOperationalView VistaOV=new CompleteOperationalView(NameConstantsChasqui.PRESNTACION);
		
		CompleteOperationalValueType Valor=new CompleteOperationalValueType(NameConstantsChasqui.VISIBLESHOWN,Boolean.toString(false),VistaOV);
		CompleteOperationalValueType Valor2=new CompleteOperationalValueType(NameConstantsChasqui.BROWSERSHOWN,Boolean.toString(b),VistaOV);
		CompleteOperationalValueType Valor3=new CompleteOperationalValueType(NameConstantsChasqui.SUMMARYSHOWN,Boolean.toString(false),VistaOV);

		VistaOV.getValues().add(Valor);
		VistaOV.getValues().add(Valor2);
		VistaOV.getValues().add(Valor3);
		atributostypes.getShows().add(VistaOV);
		
		CompleteOperationalView VistaMetaType=new CompleteOperationalView(NameConstantsChasqui.METATYPE);
		CompleteOperationalValueType MetaType=new CompleteOperationalValueType(NameConstantsChasqui.METATYPETYPE,NameConstantsChasqui.CONTROLED,VistaMetaType);
		VistaMetaType.getValues().add(MetaType);
		atributostypes.getShows().add(VistaMetaType);
		
		Vocabulario= new ArrayList<String>();
		LCole.getCollection().getVocabularios().put(atributostypes, Vocabulario);
	}

	@Override
	public void ProcessAttributes() {
		try {
			ResultSet rs=LCole.getSQL().RunQuerrySELECT("SELECT distinct tipo FROM recursos;");
			if (rs!=null) 
			{
				while (rs.next()) {
					String Dato=null;
					if (rs.getObject("tipo")!=null)
						Dato=rs.getObject("tipo").toString();
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
		
	}
	
	/**
	 * 
	 * @return the ExtendMetaControlled
	 */
	public CompleteTextElementType getExtendMetaControlled() {
		return atributostypes;
	}

}
